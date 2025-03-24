# Claive Attest Rest

Claive Attest Rest is a REST server designed to provide attestation reports for confidential virtual machines (VMs). The server runs over HTTPS and exposes two endpoints:

- `/status`: Reports the status of the server.
- `/attestation`: Provides an attestation report based on the information returned by an internal process.

## Features
- Secure communication over HTTPS.
- Lightweight and built using Flask.
- Logs server activity for easier monitoring and debugging.

## Requirements
- Ubuntu 24.04
- Python 3.9 or higher
- OpenSSL (for generating SSL certificates)
- Flask and Flask-SSLify libraries

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/scrtlabs/claive-attest-rest.git
   cd claive-attest-rest
   ```

2. Create a virtual environment and install dependencies:
   ```bash
   python3 -m venv venv
   source venv/bin/activate
   pip install -r requirements.txt
   ```

3. Generate SSL certificates:
   ```bash
   openssl genrsa -out ssl_key.pem 2048
   openssl req -new -key ssl_key.pem -out server.csr
   openssl x509 -req -days 365 -in server.csr -signkey ssl_key.pem -out ssl_cert.pem
   ```
   Place `ssl_cert.pem` and `ssl_key.pem` in the project directory.

## Configuration
Set the port for the server by modifying the `FLASK_PORT` constant in `app.py`:
```python
FLASK_PORT = 29343
```

## Running the Server
Start the server with:
```bash
python server.py
```

The server will be accessible over HTTPS at `https://<server-ip>:29343`.

## Endpoints
### `/status`
**Method**: `GET`

**Description**: Returns the status of the server.

**Response**:
```json
{
  "status": "Server is running"
}
```

### `/attestation`
**Method**: `GET`

**Description**: Calls the internal `attest` process and returns its output as a JSON payload.

**Response** (Example):
```json
{
  "attestation": "Sample attestation report",
  "timestamp": "2025-01-13T12:00:00Z"
}
```

**Error Responses**:
- If the `attest` process fails:
  ```json
  {
    "error": "Failed to generate attestation."
  }
  ```
- If the process output is not valid JSON:
  ```json
  {
    "error": "Invalid attestation format."
  }
  ```

## Logging
All server activity is logged, including function calls and errors. Logs are printed in the format:
```
<timestamp> - <log level> - <function name> - <message>
```

## License
This project is licensed under the MIT License.

