SUMMARY = "Patch asyncio to allow nested event loops"
HOMEPAGE = "https://github.com/erdewit/nest_asyncio"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1c78810ce529b662e3fce481cea9e246"

SRC_URI[md5sum] = "0a321e2fec8a51bd0568ec7181d9469b"
SRC_URI[sha256sum] = "b98e3ec1b246135e4642eceffa5a6c23a3ab12c82ff816a92c612d68205813b2"

inherit pypi setuptools3

PYPI_PACKAGE = "nest_asyncio"

RDEPENDS:${PN} += "python3-core"
