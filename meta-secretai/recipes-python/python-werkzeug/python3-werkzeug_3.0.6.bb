SUMMARY = "The Swiss Army knife of Python web development"
HOMEPAGE = "https://werkzeug.palletsprojects.com/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=3ea6657e09a296fab6b49d02929fb8bc"

PV = "3.0.6"

inherit pypi python_pep517

SRC_URI[md5sum] = "0d13f3dbe9b08aecdebe3d9b61cc58aa"
SRC_URI[sha256sum] = "a8dd59d4de28ca70471a34cba79bed5f7ef2e036a76b3ab0835474246eb41f8d"

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
