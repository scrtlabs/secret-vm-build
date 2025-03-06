SUMMARY = "Python itsdangerous module"
HOMEPAGE = "https://pypi.org/project/itsdangerous/"
LICENSE = "CLOSED"  # Update this with the actual license

inherit pypi setuptools3

SRC_URI[md5sum] = "a901babde35694c3577f7655010cd380"
SRC_URI[sha256sum] = "e0050c0b7da1eea53ffaf149c0cfbb5c6e2e2b69c4bef22c81fa6eb73e5f6173"

PYPI_PACKAGE = "itsdangerous"

RDEPENDS:${PN} += " \\\n    ${PYTHON_PN}-core \\\n"
