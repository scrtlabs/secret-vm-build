SUMMARY = "The Secret Network SDK for Python"
HOMEPAGE = "https://github.com/scrtlabs/secret.py"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=53a6efb3a4d4f1a0815772c53cb6ae87"

SRC_URI[md5sum] = "669e154742d0c19a5bc344db228c110f"
SRC_URI[sha256sum] = "08f8ddc340a98e6aa135f9f0536739f21d81d8e29605ba2bfa6bb45a044f90e1"

inherit pypi setuptools3

PYPI_PACKAGE = "secret_sdk"
PYPI_PACKAGE_EXT = "whl"

RDEPENDS:${PN} += " \
    python3-aiohttp \
    python3-attrs \
    python3-bech32 \
    python3-betterproto \
    python3-bip32utils \
    python3-boltons \
    python3-ecdsa \
    python3-furl \
    python3-miscreant \
    python3-mnemonic \
    python3-nest-asyncio \
    python3-protobuf \
    python3-wrapt \
    python3-core \
"

RPROVIDES:${PN} += "python3-secret_sdk"
