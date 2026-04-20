SUMMARY = "REST Server for SecretVM"
DESCRIPTION = "A REST API server providing attestation and verification services"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/scrtlabs/secret-vm-attest-rest-server.git;branch=add-ita-jwt;protocol=https"
SRCREV = "4af80ca92b3ca6d0262b35adc3a9a0252771a926"

GO_IMPORT = "github.com/scrtlabs/secret-vm-attest-rest-server"
GO_INSTALL = "${GO_IMPORT}"

do_compile[network] = "1"

inherit go-mod
