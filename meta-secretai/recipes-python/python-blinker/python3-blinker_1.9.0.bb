SUMMARY = "Fast, simple object-to-object and broadcast signaling"
DESCRIPTION = "Blinker provides a fast dispatching system that allows any number of interested parties to subscribe to events, or 'signals'."
HOMEPAGE = "https://pythonhosted.org/blinker/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=8ca5206fbd00026e7da477f6e5298733"

SRC_URI[sha256sum] = "e6820ff6fa4e4d1d8e2747c2283749c3f547e4fee112b98555cdcdae32996182"

inherit pypi setuptools3

# Fix FILES_${PN} to include all files
FILES:${PN} = "${PYTHON_SITEPACKAGES_DIR}/*"

# Ensure cross-compilation works correctly
BBCLASSEXTEND = "native nativesdk"
