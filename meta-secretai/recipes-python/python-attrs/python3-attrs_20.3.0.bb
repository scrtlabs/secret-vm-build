SUMMARY = "Python attrs module"
HOMEPAGE = "https://pypi.org/project/attrs/"
# Update this with the actual license
LICENSE = "CLOSED"

inherit pypi setuptools3

SRC_URI[md5sum] = "4fe38f89297b2b446d83190fce189f29"
SRC_URI[sha256sum] = "832aa3cde19744e49938b91fea06d69ecb9e649c93ba974535d08ad92164f700"

PYPI_PACKAGE = "attrs"

RDEPENDS:${PN} += " \\\n    ${PYTHON_PN}-core \\\n"
