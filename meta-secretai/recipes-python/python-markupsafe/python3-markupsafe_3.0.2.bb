SUMMARY = "Python MarkupSafe module"
HOMEPAGE = "https://pypi.org/project/MarkupSafe/"
# BSD-3-Clause license according to PyPI
LICENSE = "BSD-3-Clause"

inherit pypi setuptools3

# MD5 hash for the package
SRC_URI[md5sum] = "cb0071711b573b155cc8f86e1de72167"
# SHA256 hash for the package
SRC_URI[sha256sum] = "ee55d3edf80167e48ea11a923c7386f4669df67d7994554387f84e7d8b0a2bf0"

PYPI_PACKAGE = "MarkupSafe"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"
