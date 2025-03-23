SUMMARY = "Python XML Signature and XAdES library"
HOMEPAGE = "https://github.com/kislyuk/signxml"
AUTHOR = "Andrey Kislyuk"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "https://files.pythonhosted.org/packages/9e/7f/5ca7978ad2a255c6044b87c493b81737ad63658c4b20e5cd70875cc2a38a/signxml-${PV}.tar.gz"
SRC_URI[md5sum] = "0ce0320639f2c31eec057f6671bcbb62"
SRC_URI[sha256sum] = "113264176bfe5597b8e3ebf01d37e06f02827b284952e956e7f22efa02f66634"

S = "${WORKDIR}/signxml-${PV}"

inherit setuptools3

RDEPENDS:${PN} += " \
    python3-lxml \
    python3-cryptography \
    python3-certifi \
    python3-core \
"

BBCLASSEXTEND = "native nativesdk"
