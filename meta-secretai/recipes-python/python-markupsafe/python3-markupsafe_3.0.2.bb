SUMMARY = "Safely add untrusted strings to HTML/XML markup"
HOMEPAGE = "https://palletsprojects.com/p/markupsafe/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=ffeffa59c90c9c4a033c7574f8f3fb75"

inherit pypi setuptools3

SRC_URI[md5sum] = "cb0071711b573b155cc8f86e1de72167"
SRC_URI[sha256sum] = "ee55d3edf80167e48ea11a923c7386f4669df67d7994554387f84e7d8b0a2bf0"

RDEPENDS:${PN} += "${PYTHON_PN}-stringold"

BBCLASSEXTEND = "native nativesdk"
