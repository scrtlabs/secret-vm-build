SUMMARY = "Python multidict module"
HOMEPAGE = "https://pypi.org/project/multidict/"
LICENSE = "CLOSED"  # Update this with the actual license

inherit pypi setuptools3

SRC_URI[md5sum] = "2c8cf03b6e92e1b9c335de56b606c2fc"
SRC_URI[sha256sum] = "22ae2ebf9b0c69d206c003e2f6a914ea33f0a932d4aa16f236afc049d9958f4a"

PYPI_PACKAGE = "multidict"

RDEPENDS:${PN} += " \\\n    ${PYTHON_PN}-core \\\n"
