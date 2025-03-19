SUMMARY = "The comprehensive WSGI web application library"
DESCRIPTION = "Werkzeug is a comprehensive WSGI web application library."
HOMEPAGE = "https://palletsprojects.com/p/werkzeug/"
AUTHOR = "Pallets"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=5dc88300786f1c214c1e9827a5229462"

SRC_URI = "https://files.pythonhosted.org/packages/0d/cc/ff1904eb5eb4b455e442834dabf9427331ac0fa02853bf83db817a7dd53d/werkzeug-3.0.6.tar.gz"
SRC_URI[md5sum] = "0d13f3dbe9b08aecdebe3d9b61cc58aa"
SRC_URI[sha256sum] = "a8dd59d4de28ca70471a34cba79bed5f7ef2e036a76b3ab0835474246eb41f8d"

S = "${WORKDIR}/werkzeug-${PV}"

RDEPENDS:${PN} += " \
    python3-markupsafe \
    python3-core \
    python3-email \
    python3-html \
    python3-io \
    python3-json \
    python3-misc \
    python3-netclient \
    python3-profile \
    python3-pprint \
    python3-typing-extensions \
"

inherit python_pep517

# Fix FILES and use INSANE_SKIP to avoid the "installed but not shipped" error
FILES:${PN} = "${PYTHON_SITEPACKAGES_DIR}/werkzeug/* ${PYTHON_SITEPACKAGES_DIR}/Werkzeug*"
FILES:${PN} += "/usr/lib/python3.12/site-packages/werkzeug/*"

# Skip the "installed but not shipped" check
INSANE_SKIP:${PN} += "installed-vs-shipped"

# This allows native and nativesdk builds
BBCLASSEXTEND = "native nativesdk"
