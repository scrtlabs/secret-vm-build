SUMMARY = "Python blinker module - fast, simple object-to-object and broadcast signaling"
HOMEPAGE = "https://pypi.org/project/blinker/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=42cd19c88fc13d1307a4efd64ee90e4e"

# Set the package version to 1.9.0 even though filename is 1.7.0 to match meta-python
PV = "1.9.0"

inherit pypi python_pep517

SRC_URI[md5sum] = "1ffce54aca3d568ab18ee921d479274f"
SRC_URI[sha256sum] = "b4ce2265a7abece45e7cc896e98dbebe6cead56bcf805a3d23136d145f5445bf"

PYPI_PACKAGE = "blinker"

RDEPENDS:${PN} += " \
    python3-core \
"

# Set FILES to ensure all the installed files are included in the package
FILES:${PN} = "${PYTHON_SITEPACKAGES_DIR}/*"

# This recipe provides blinker 1.9.0 but is named to match the version in meta-python
# for override purposes due to layer priority
PROVIDES += "python3-blinker"
RPROVIDES:${PN} += "python3-blinker"
