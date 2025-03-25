FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

DEPENDS += "openssl-native"

SRC_URI += "\
    file://root_lib.rs \
    file://pyo3_lib.rs \
"

python_pep517_do_configure:prepend() {
    cd ${S}
    mkdir -p src/rust/src
    mkdir -p src/rust/.cargo
    
    # Create all necessary directories first
    for dir in pyo3 openssl-sys asn1 cryptography-cffi cryptography-key-parsing cryptography-openssl cryptography-x509-verification cryptography-rust cc cfg-if once_cell pem self_cell openssl foreign-types foreign-types-shared base64; do
        mkdir -p src/rust/vendor/$dir/src
    done
    mkdir -p src/rust/vendor/openssl-sys/build

    # Copy root files from recipe files
    cp ${WORKDIR}/root_lib.rs src/rust/src/lib.rs
    cp ${WORKDIR}/pyo3_lib.rs src/rust/vendor/pyo3/src/lib.rs

    # Create root Cargo.toml
    printf '%s\n' \
        '[package]' \
        'name = "cryptography-rust"' \
        'version = "0.1.0"' \
        'edition = "2021"' \
        '' \
        '[lib]' \
        'name = "cryptography_rust"' \
        'path = "src/lib.rs"' \
        'crate-type = ["cdylib"]' \
        '' \
        '[dependencies]' \
        'pyo3 = { version = "0.20.3", features = ["extension-module", "abi3"] }' \
        'asn1 = "0.15.5"' \
        'cfg-if = "1.0.0"' \
        'openssl-sys = "0.9.99"' \
        'self_cell = "1.0.3"' \
        'once_cell = "1.19.0"' \
        'pem = "3.0.3"' \
        'base64 = "0.21.7"' \
        '' \
        '[patch.crates-io]' \
        'pyo3 = { path = "vendor/pyo3" }' \
        'openssl-sys = { path = "vendor/openssl-sys" }' \
        'asn1 = { path = "vendor/asn1" }' \
        'cc = { path = "vendor/cc" }' \
        'cfg-if = { path = "vendor/cfg-if" }' \
        'once_cell = { path = "vendor/once_cell" }' \
        'pem = { path = "vendor/pem" }' \
        'self_cell = { path = "vendor/self_cell" }' \
        'openssl = { path = "vendor/openssl" }' \
        'foreign-types = { path = "vendor/foreign-types" }' \
        'foreign-types-shared = { path = "vendor/foreign-types-shared" }' \
        'base64 = { path = "vendor/base64" }' \
        '' \
        '[workspace]' \
        'members = []' > src/rust/Cargo.toml

    # Create vendor crate configurations
    printf '%s\n' \
        '[package]' \
        'name = "pyo3"' \
        'version = "0.20.3"' \
        'edition = "2021"' \
        'license = "Apache-2.0"' \
        '[lib]' \
        'name = "pyo3"' \
        'path = "src/lib.rs"' \
        '[features]' \
        'default = ["extension-module", "abi3"]' \
        'extension-module = []' \
        'abi3 = []' > src/rust/vendor/pyo3/Cargo.toml

    # Create other minimal vendor crates
    for crate in asn1 base64 cc cfg-if once_cell openssl-sys pem self_cell openssl foreign-types foreign-types-shared; do
        printf '%s\n' \
            '[package]' \
            "name = \"$crate\"" \
            'version = "0.1.0"' \
            'edition = "2021"' \
            '[lib]' \
            "name = \"$crate\"" \
            'path = "src/lib.rs"' > "src/rust/vendor/$crate/Cargo.toml"
        echo 'pub fn init() {}' > "src/rust/vendor/$crate/src/lib.rs"
    done

    # Create build script for openssl-sys
    printf '%s\n' \
        'fn main() {' \
        '    println!("cargo:rustc-link-lib=ssl");' \
        '    println!("cargo:rustc-link-lib=crypto");' \
        '}' > src/rust/vendor/openssl-sys/build/main.rs

    # Create cargo config
    printf '%s\n' \
        '[source.crates-io]' \
        'replace-with = "vendored-sources"' \
        '' \
        '[source.vendored-sources]' \
        'directory = "vendor"' \
        '' \
        '[net]' \
        'offline = true' > src/rust/.cargo/config.toml

    # Create checksum files
    for d in src/rust/vendor/*/; do
        if [ -d "$d" ]; then
            echo '{"package":null,"files":{}}' > "$d/.cargo-checksum.json"
        fi
    done

    chmod -R u+w ${S}/src/rust
}

python_pep517_do_compile:prepend() {
    export PATH="${STAGING_BINDIR_NATIVE}:$PATH"
    export PKG_CONFIG_PATH="${STAGING_LIBDIR_NATIVE}/pkgconfig"
    export PKG_CONFIG_SYSROOT_DIR=""
    export OPENSSL_DIR="${STAGING_DIR_NATIVE}/usr"
    export OPENSSL_LIB_DIR="${STAGING_LIBDIR_NATIVE}"
    export OPENSSL_INCLUDE_DIR="${STAGING_INCDIR}"
    export OPENSSL_STATIC=1
    export CARGO_NET_OFFLINE=true
    export CARGO_NET_GIT_FETCH_WITH_CLI=true
    export CARGO_HTTP_CAINFO=""
    export CARGO_HOME="${S}/src/rust/.cargo"
    export CARGO_TARGET_DIR="${S}/src/rust/target"
    export CARGO_TERM_VERBOSE=true
    export CC="gcc"
    export RUSTFLAGS="-C linker=gcc"
}
