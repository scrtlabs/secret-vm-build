SUMMARY = "REST Server for SecretVM"
DESCRIPTION = "A REST API server providing attestation and verification services"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/scrtlabs/secret-vm-attest-rest-server.git;branch=master;protocol=https"
SRCREV = "d0012733be47086f28b0dc8158ce299c40d49e96"

GO_IMPORT = "github.com/scrtlabs/secret-vm-attest-rest-server"
GO_INSTALL = "${GO_IMPORT}"

do_compile[network] = "1"

inherit go-mod
