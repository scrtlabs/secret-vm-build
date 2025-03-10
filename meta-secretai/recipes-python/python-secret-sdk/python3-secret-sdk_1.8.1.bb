SUMMARY = "Python secret-sdk module"
HOMEPAGE = "https://pypi.org/project/secret-sdk/"
# License is MIT according to PyPI
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3966467009164958d9b246c45bfbd060"

inherit pypi setuptools3

# MD5 hash for the package
SRC_URI[md5sum] = "d968c4491fda27ff6c2e349773f1acc2"
# SHA256 hash for the package
SRC_URI[sha256sum] = "b2a6964a774ab04a06d606122a6d92aa6cde76d79d65d6fdc325ab03500366c9"

PYPI_PACKAGE = "secret_sdk"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-core \
"
