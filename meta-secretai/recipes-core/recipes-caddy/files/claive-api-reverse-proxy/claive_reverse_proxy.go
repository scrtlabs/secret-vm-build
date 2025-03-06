package claive_reverse_proxy

import (
	"bufio"
	querycontract "claive-reverse-proxy-module/query-contract"
	"crypto/sha256"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"net/http"
	"os"
	"strings"
	"sync"
	"time"

	"github.com/caddyserver/caddy/v2"
	"github.com/caddyserver/caddy/v2/caddyconfig/caddyfile"
	"github.com/caddyserver/caddy/v2/caddyconfig/httpcaddyfile"
	"github.com/caddyserver/caddy/v2/modules/caddyhttp"
)

func init() {
	caddy.RegisterModule(&Middleware{})
	httpcaddyfile.RegisterHandlerDirective("claive_reverse_proxy", parseCaddyfile)
}

var (
	cache        = make(map[string]bool)
	cacheMutex   = sync.RWMutex{}
	cacheTTL     = time.Minute * 30
	lastUpdate   time.Time
	apiMasterKey string // Local variable for API_MASTER_KEY
)

// Middleware is the Caddy module that checks the API key.
type Middleware struct{}

// CaddyModule returns the Caddy module information.
func (Middleware) CaddyModule() caddy.ModuleInfo {
	return caddy.ModuleInfo{
		ID:  "http.handlers.claive_reverse_proxy",
		New: func() caddy.Module { return new(Middleware) },
	}
}

// Provision implements caddy.Provisioner.
func (m *Middleware) Provision(ctx caddy.Context) error {
	if apiMasterKey != "" {
		caddy.Log().Info(fmt.Sprintf("API_MASTER_KEY set to: %s", apiMasterKey))
	} else {
		caddy.Log().Warn("API_MASTER_KEY is not set")
	}
	return nil
}

// Validate implements caddy.Validator.
func (m *Middleware) Validate() error {
	return nil
}

// ServeHTTP implements caddyhttp.MiddlewareHandler.
func (m Middleware) ServeHTTP(w http.ResponseWriter, r *http.Request, next caddyhttp.Handler) error {
	authHeader := r.Header.Get("Authorization")
	if authHeader == "" {
		http.Error(w, "Missing Authorization header", http.StatusUnauthorized)
		return nil
	}

	// Remove "Basic " or "Bearer " prefix if present
	prefix := "Basic "
	apiKey := strings.TrimPrefix(authHeader, prefix)
	prefix = "Bearer "
	apiKey = strings.TrimPrefix(apiKey, prefix)

	caddy.Log().Debug(fmt.Sprintf("Received API key: %s", apiKey))

	isValid, err := checkApiKey(apiKey)
	if err != nil {
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return nil
	}

	if !isValid {
		http.Error(w, "Invalid API key", http.StatusUnauthorized)
		return nil
	}

	return next.ServeHTTP(w, r)
}

// UnmarshalCaddyfile implements caddyfile.Unmarshaler.
func (m *Middleware) UnmarshalCaddyfile(d *caddyfile.Dispenser) error {
	for d.Next() {
		for d.NextBlock(0) {
			switch d.Val() {
			case "API_MASTER_KEY":
				if !d.Args(&apiMasterKey) {
					return d.ArgErr()
				}
			default:
				return d.Errf("unknown subdirective: %s", d.Val())
			}
		}
	}
	return nil
}

// checkApiKey validates the API key by first checking the cache and master keys.
func checkApiKey(apiKey string) (bool, error) {
	if apiMasterKey != "" && apiKey == apiMasterKey {
		return true, nil
	}

	// Check against the master keys
	isMasterKey, err := checkMasterKeys(apiKey)
	if err != nil {
		return false, err
	}
	if isMasterKey {
		return true, nil
	}
	
	hasher := sha256.New()
	hasher.Write([]byte(apiKey))
	apiKeyHash := hex.EncodeToString(hasher.Sum(nil))

	cacheMutex.RLock()
	cached, found := cache[apiKeyHash]
	isStale := time.Since(lastUpdate) > cacheTTL
	cacheMutex.RUnlock()

	if found && !isStale {
		return cached, nil
	}

	if err := updateApiKeyCache(); err != nil {
		return false, fmt.Errorf("failed to update API key cache: %w", err)
	}

	cacheMutex.RLock()
	defer cacheMutex.RUnlock()
	return cache[apiKeyHash], nil
}

// checkMasterKeys checks if the provided API key exists in the master keys file.
func checkMasterKeys(apiKey string) (bool, error) {
	file, err := os.Open("master_keys.txt")
	if err != nil {
		return false, fmt.Errorf("failed to open master_keys.txt: %w", err)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		nextMasterKey := strings.TrimSpace(scanner.Text())
		if nextMasterKey != "" && nextMasterKey == apiKey {
			return true, nil
		}
	}

	if err := scanner.Err(); err != nil {
		return false, fmt.Errorf("error reading master_keys.txt: %w", err)
	}

	return false, nil
}

// updateApiKeyCache queries the contract and updates the in-memory cache.
func updateApiKeyCache() error {
	permitFilePath := "permit.json"
	permit, err := readPermitFromFile(permitFilePath)
	if err != nil {
		return fmt.Errorf("failed to read permit from file: %w", err)
	}

	contractAddress := "secret1ttm9axv8hqwjv3qxvxseecppsrw4cd68getrvr"
	query := map[string]interface{}{
		"api_keys_with_permit": map[string]interface{}{
			"permit": permit,
		},
	}

	result, err := querycontract.QueryContract(contractAddress, query)
	if err != nil {
		return fmt.Errorf("failed to query contract: %w", err)
	}

	apiKeys, ok := result["api_keys"].([]interface{})
	if !ok {
		return fmt.Errorf("unexpected format for api_keys response")
	}

	newCache := make(map[string]bool)
	for _, entry := range apiKeys {
		entryMap, ok := entry.(map[string]interface{})
		if !ok {
			continue
		}
		hashedKey, ok := entryMap["hashed_key"].(string)
		if ok {
			newCache[hashedKey] = true
		}
	}

	cacheMutex.Lock()
	defer cacheMutex.Unlock()
	cache = newCache
	lastUpdate = time.Now()

	return nil
}

// readPermitFromFile reads the permit from a JSON file.
func readPermitFromFile(filePath string) (map[string]interface{}, error) {
	file, err := os.Open(filePath)
	if err != nil {
		return nil, fmt.Errorf("failed to open permit file: %w", err)
	}
	defer file.Close()

	var permit map[string]interface{}
	if err := json.NewDecoder(file).Decode(&permit); err != nil {
		return nil, fmt.Errorf("failed to decode permit file: %w", err)
	}

	return permit, nil
}

// parseCaddyfile unmarshals tokens from h into a new Middleware.
func parseCaddyfile(h httpcaddyfile.Helper) (caddyhttp.MiddlewareHandler, error) {
	var m Middleware
	err := m.UnmarshalCaddyfile(h.Dispenser)
	return m, err
}

// Interface guards
var (
	_ caddy.Module                = (*Middleware)(nil)
	_ caddy.Provisioner           = (*Middleware)(nil)
	_ caddy.Validator             = (*Middleware)(nil)
	_ caddyhttp.MiddlewareHandler = (*Middleware)(nil)
	_ caddyfile.Unmarshaler       = (*Middleware)(nil)
)
