SUMMARY = "Force SSL on your Flask app"
DESCRIPTION = "This is a simple Flask extension that configures your Flask application to redirect all incoming requests to https"
HOMEPAGE = "https://github.com/kennethreitz/flask-sslify"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI[md5sum] = "1282f5af7d621a32130296ad8dd6a70c"
SRC_URI[sha256sum] = "d33e1d3c09cd95154176aa8a7319418e52129fc482dd56d8a8ad7c24500d543e"

PYPI_PACKAGE = "Flask-SSLify"

inherit pypi setuptools3

RDEPENDS:${PN} = "python3-flask"

BBCLASSEXTEND = "native nativesdk"
