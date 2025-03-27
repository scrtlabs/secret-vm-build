SUMMARY = "REST Server for Secret AI"
DESCRIPTION = "A REST API server providing attestation and verification services"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/scrtlabs/secretai_attest_rest_go.git;branch=master;protocol=https"
SRCREV = "a3f99ac2c12d467f800c0d9b72e161e5152967f6"

GO_IMPORT = "github.com/scrtlabs/secretai_attest_rest_go"
GO_INSTALL = "${GO_IMPORT}"

do_compile[network] = "1"

inherit go-mod
