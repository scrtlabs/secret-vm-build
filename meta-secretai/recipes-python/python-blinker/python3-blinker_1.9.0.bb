SUMMARY = "Fast, simple object-to-object and broadcast signaling"
DESCRIPTION = "Blinker provides a fast dispatching system that allows any number of interested parties to subscribe to events, or 'signals'."
HOMEPAGE = "https://pythonhosted.org/blinker/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=8ca5206fbd00026e7da477f6e5298733"

SRC_URI[sha256sum] = "b4ce2265a7abece45e7cc896e98dbebe6cead56bcf805a3d23136d145f5445bf"

inherit pypi setuptools3

# Fix FILES_${PN} to include all files
FILES:${PN} = "${PYTHON_SITEPACKAGES_DIR}/*"

# Ensure cross-compilation works correctly
BBCLASSEXTEND = "native nativesdk"
