SUMMARY = "Python bs4 module"
HOMEPAGE = "https://pypi.org/project/bs4/"
LICENSE = "CLOSED"  # Update this with the actual license

inherit pypi setuptools3

SRC_URI[md5sum] = "c631ce2af9d7405ad2f8eccf1bb45a77"  # Replace with actual md5sum
SRC_URI[sha256sum] = "a48685c58f50fe127722417bae83fe6badf500d54b55f7e39ffe43b798653925"  # Replace with actual sha256sum

PYPI_PACKAGE = "bs4"

RDEPENDS:${PN} += " \\\n    ${PYTHON_PN}-core \\\n"
