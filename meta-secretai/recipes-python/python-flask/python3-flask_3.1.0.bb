SUMMARY = "A simple framework for building complex web applications."
HOMEPAGE = "https://flask.palletsprojects.com/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=ffeffa59c90c9c4a033c7574f8f3fb75"

# Explicitly set the URL to the correct case-sensitive version
SRC_URI = "https://files.pythonhosted.org/packages/89/50/dff6380f1c7f84135484e176e0cac8690af72fa90e932ad2a0a60e28c69b/flask-3.1.0.tar.gz"
SRC_URI[sha256sum] = "5f873c5184c897c8d9d1b05df1e3d01b14910ce69607a117bd3277098a5836ac"

PYPI_PACKAGE = "Flask"

inherit python_pep517

RDEPENDS:${PN} = "\
    python3-werkzeug \
    python3-jinja2 \
    python3-itsdangerous \
    python3-click \
    python3-blinker \
    ${@bb.utils.contains('PYTHON_VERSION', '3.9', 'python3-importlib-metadata', '', d)} \
"

CLEANBROKEN = "1"
S = "${WORKDIR}/flask-${PV}"
