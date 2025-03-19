SUMMARY = "Python cryptography module"
HOMEPAGE = "https://pypi.org/project/cryptography/"
LICENSE = "Apache-2.0 | BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8c3617db4fb6fae01f1d253ab91511e4 \
                    file://LICENSE.APACHE;md5=4e168cce331e5c827d4c2b68a6200e1b \
                    file://LICENSE.BSD;md5=5ae30ba4123bc4f2fa49aa0b0dce887b"

inherit pypi python_setuptools_build_meta python_setuptools3_rust

# Include crates
require ${BPN}-crates.inc

SRC_URI[md5sum] = "9cb2411324687347a27d349d3e74eb7c"
SRC_URI[sha256sum] = "c63454aa261a0cf0c5b4718349629793e9e634993538db841165b3df74f37ec0"

PYPI_PACKAGE = "cryptography"

DEPENDS += " \
    python3-cffi-native \
    python3-pip-native \
    python3-setuptools-rust-native \
    openssl \
"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-cffi \
    ${PYTHON_PN}-core \
"

do_compile:prepend() {
    # Create config file to vendor crates
    mkdir -p ${S}/.cargo
    cat > ${S}/.cargo/config.toml << EOCFG
[source.crates-io]
replace-with = "vendored-sources"

[source.vendored-sources]
directory = "${WORKDIR}/cargo_home"
EOCFG
}

# Allow network access during compilation
do_compile[network] = "1"
