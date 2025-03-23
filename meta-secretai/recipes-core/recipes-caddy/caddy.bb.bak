# Recipe for Caddy webserver
SUMMARY = "Caddy is a powerful, enterprise-ready, open source web server with automatic HTTPS"
HOMEPAGE = "https://caddyserver.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://src/github.com/caddyserver/caddy/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = " \
    git://github.com/caddyserver/caddy.git;protocol=https;branch=master;destsuffix=src/github.com/caddyserver/caddy;name=v2 \
    file://caddy.service \
    file://Caddyfile \
    file://0001-Update-acmez-imports-to-v2.patch \
"

SRCREV_v2 = "8dc76676fb5156f971db49c50a5c509d1c93613b"

PV = "1.0"

SYSTEMD_SERVICE:${PN} = "caddy.service"

S = "${WORKDIR}"

# This is needed for compiling with gcc
export CGO_ENABLED = "1"

INSANE_SKIP:${PN} = "ldflags"

# Used by the systemd service file
USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "caddy"
USERADD_PARAM:${PN} = "-M -d /var/lib/caddy -r -g caddy -s /sbin/nologin caddy"

inherit systemd
inherit go
inherit useradd

GO_INSTALL = "github.com/caddyserver/caddy/cmd/caddy"

do_configure() {
    cd ${S}
    patch -p1 < ${WORKDIR}/0001-Update-acmez-imports-to-v2.patch
}

do_install:append() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/caddy.service ${D}${systemd_system_unitdir}/caddy.service

    chmod 755 ${D}${bindir}/caddy

    # Create directories required by caddy
    install -d ${D}/var/lib/caddy
    install -d ${D}/etc/caddy
    install -d ${D}/var/log/caddy
    install -d ${D}/var/www/html

    # Install Caddyfile
    install -m 0644 ${WORKDIR}/Caddyfile ${D}/etc/caddy/Caddyfile
}

FILES:${PN} += "\
    ${systemd_system_unitdir}/caddy.service \
    /var/lib/caddy \
    /var/log/caddy \
    /var/www/html \
    /etc/caddy \
"

RDEPENDS:${PN} += "openssl"
