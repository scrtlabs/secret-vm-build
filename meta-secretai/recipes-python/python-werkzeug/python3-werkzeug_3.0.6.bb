SUMMARY = "The Swiss Army knife of Python web development"
HOMEPAGE = "https://werkzeug.palletsprojects.com/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=3ea6657e09a296fab6b49d02929fb8bc"

PV = "3.0.6"

inherit pypi python_pep517

SRC_URI[md5sum] = "b6005d403d01d08b9fe2330a0cfea05a"
SRC_URI[sha256sum] = "60723ce945c19328679790e3282cc758aa4a6040e4bb330f53d30fa546d44746"

PYPI_PACKAGE = "werkzeug"

RDEPENDS:${PN} += "\
    python3-core \
    python3-io \
    python3-datetime \
    python3-html \
    python3-markupsafe \
    python3-pprint \
    python3-profile \
    "

# Set FILES to ensure all the installed files are included in the package
FILES:${PN} = "${PYTHON_SITEPACKAGES_DIR}/*"

# This recipe provides werkzeug 3.0.6 but is named to match the version in meta-python
# for override purposes due to layer priority
PROVIDES += "python3-werkzeug"
RPROVIDES:${PN} += "python3-werkzeug"
