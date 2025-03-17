SUMMARY = "Python cffi module"
HOMEPAGE = "https://pypi.org/project/cffi/"
# According to PyPI, license is MIT
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5677e2fdbf7cdda61d6dd2b57df547bf"

inherit pypi setuptools3

# MD5 hash for the package
SRC_URI[md5sum] = "4336ca58b2df0cc3b163884d5fa2e5e2"
# SHA256 hash for the package
SRC_URI[sha256sum] = "1c39c6016c32bc48dd54561950ebd6836e1670f2ae46128f67cf49e789c52824"

PYPI_PACKAGE = "cffi"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
    python3-pycparser \
"

# Include all installed files in the package
FILES:${PN} = "${PYTHON_SITEPACKAGES_DIR}/*"
