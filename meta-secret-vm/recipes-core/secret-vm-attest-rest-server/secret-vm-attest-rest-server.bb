SUMMARY = "REST Server for SecretVM"
DESCRIPTION = "A REST API server providing attestation and verification services"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/scrtlabs/secret-vm-attest-rest-server.git;branch=add-ita-jwt;protocol=https"
SRCREV = "abd5695e255e08570c4457da41dfa1965e5c6f11"

GO_IMPORT = "github.com/scrtlabs/secret-vm-attest-rest-server"
GO_INSTALL = "${GO_IMPORT}"

do_compile[network] = "1"

inherit go-mod
