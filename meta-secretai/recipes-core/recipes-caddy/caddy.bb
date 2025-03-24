SUMMARY = "Caddy web server with Claive API reverse proxy module"
DESCRIPTION = "Caddy is a powerful, enterprise-ready, open source web server with automatic HTTPS written in Go"
HOMEPAGE = "https://caddyserver.com"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"
SRC_URI = "git://${GO_IMPORT};protocol=https;branch=master"
SRCREV = "0e570e0cc717f02cf3800ae741df70cd074c7275"

GO_IMPORT = "github.com/caddyserver/caddy"
GO_INSTALL = "${GO_IMPORT}"

do_compile[network] = "1"

inherit go-mod

# Add this to declare the systemd service
SYSTEMD_SERVICE:${PN} = "caddy.service"

# Ensure systemd unit gets packaged
FILES:${PN} += "${systemd_system_unitdir}/caddy.service"
