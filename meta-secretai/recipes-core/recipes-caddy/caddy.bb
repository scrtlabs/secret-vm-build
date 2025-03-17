SUMMARY = "Caddy web server with Claive API reverse proxy module"
DESCRIPTION = "Caddy is a powerful, enterprise-ready, open source web server with automatic HTTPS written in Go"
HOMEPAGE = "https://caddyserver.com"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

GO_IMPORT = "github.com/caddyserver/caddy/v2"
SRC_URI = "git://github.com/caddyserver/caddy;protocol=https;nobranch=1;name=caddy \
           file://secretai-caddy.service \
           file://Caddyfile.template \
           file://claive-api-reverse-proxy/ \
           file://scripts/secretai_build_install_caddy.sh \
           file://scripts/secretai_build_install_caddy.md \
"

SRCREV_caddy = "6d9a83376b5e19b3c0368541ee46044ab284038b"



inherit go systemd useradd

RDEPENDS:${PN} = "bash"

# Path for Caddy configuration
CADDY_CONFIG_DIR = "/etc/caddy"
CADDY_DATA_DIR = "/var/lib/caddy"
CADDY_SCRIPTS_DIR = "/usr/share/caddy/scripts"

# Caddy user/group
CADDY_USER = "caddy"
CADDY_GROUP = "caddy"

# Systemd service
SYSTEMD_SERVICE:${PN} = "secretai-caddy.service"
SYSTEMD_AUTO_ENABLE = "enable"

# For permissions
USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "--system --home ${CADDY_DATA_DIR} --no-create-home --shell /sbin/nologin -g ${CADDY_GROUP} ${CADDY_USER}"
GROUPADD_PARAM:${PN} = "--system ${CADDY_GROUP}"

do_configure:prepend() {
    # Copy Claive API reverse proxy module to the appropriate Go source directory
    
    # Fix: Create the full import path structure instead of a relative one
    mkdir -p ${S}/src/${GO_IMPORT}/vendor/claive-reverse-proxy-module/query-contract
    # Copy module files to the vendor directory
    cp -r ${WORKDIR}/claive-api-reverse-proxy/*.go ${S}/src/${GO_IMPORT}/vendor/claive-reverse-proxy-module/
    cp -r ${WORKDIR}/claive-api-reverse-proxy/go.mod ${S}/src/${GO_IMPORT}/vendor/claive-reverse-proxy-module/
    cp -r ${WORKDIR}/claive-api-reverse-proxy/go.sum ${S}/src/${GO_IMPORT}/vendor/claive-reverse-proxy-module/ 2>/dev/null || :
    cp -r ${WORKDIR}/claive-api-reverse-proxy/query-contract/* ${S}/src/${GO_IMPORT}/vendor/claive-reverse-proxy-module/query-contract/

    # Also copy these files for runtime access
    mkdir -p ${S}/src/${GO_IMPORT}/cmd/caddy/claive-api-reverse-proxy/
    cp -r ${WORKDIR}/claive-api-reverse-proxy/*.txt ${S}/src/${GO_IMPORT}/cmd/caddy/claive-api-reverse-proxy/ 2>/dev/null || :
    cp -r ${WORKDIR}/claive-api-reverse-proxy/*.json ${S}/src/${GO_IMPORT}/cmd/caddy/claive-api-reverse-proxy/ 2>/dev/null || :
    cp -r ${WORKDIR}/claive-api-reverse-proxy/*.pem ${S}/src/${GO_IMPORT}/cmd/caddy/claive-api-reverse-proxy/ 2>/dev/null || :

    
    # Configure to build Caddy with standard modules and Claive API reverse proxy module
    echo 'package main' > ${S}/src/${GO_IMPORT}/cmd/caddy/main.go
    echo '' >> ${S}/src/${GO_IMPORT}/cmd/caddy/main.go
    echo 'import (' >> ${S}/src/${GO_IMPORT}/cmd/caddy/main.go
    echo '    caddycmd "github.com/caddyserver/caddy/v2/cmd"' >> ${S}/src/${GO_IMPORT}/cmd/caddy/main.go
    echo '    _ "github.com/caddyserver/caddy/v2/modules/standard"' >> ${S}/src/${GO_IMPORT}/cmd/caddy/main.go
    # Fix: Use the full import path from the vendor directory
    echo '    _ "github.com/caddyserver/caddy/v2/vendor/claive-reverse-proxy-module"' >> ${S}/src/${GO_IMPORT}/cmd/caddy/main.go
    echo ')' >> ${S}/src/${GO_IMPORT}/cmd/caddy/main.go
    echo '' >> ${S}/src/${GO_IMPORT}/cmd/caddy/main.go
    echo 'func main() {' >> ${S}/src/${GO_IMPORT}/cmd/caddy/main.go
    echo '    caddycmd.Main()' >> ${S}/src/${GO_IMPORT}/cmd/caddy/main.go
    echo '}' >> ${S}/src/${GO_IMPORT}/cmd/caddy/main.go
}
do_compile[network] = "1"

do_compile() {
    cd ${S}/src/${GO_IMPORT}/cmd/caddy
    # Fix: Add vendor mode to use the modules in vendor/
    ${GO} build -mod=vendor -o ${B}/caddy -trimpath
}

do_install() {
    # Install caddy binary
    install -d ${D}${bindir}
    install -m 0755 ${B}/caddy ${D}${bindir}/caddy

    # Install Caddyfile template
    install -d ${D}${CADDY_CONFIG_DIR}
    install -m 0644 ${WORKDIR}/Caddyfile.template ${D}${CADDY_CONFIG_DIR}/

    # Create data directories
    install -d ${D}${CADDY_DATA_DIR}
    install -d ${D}${CADDY_DATA_DIR}/data

    # Install systemd service
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${WORKDIR}/secretai-caddy.service ${D}${systemd_unitdir}/system/
    fi

    # Install module data files
    install -d ${D}${CADDY_DATA_DIR}/claive-api-reverse-proxy
    for file in ${S}/src/${GO_IMPORT}/cmd/caddy/claive-api-reverse-proxy/*; do
        if [ -f "$file" ]; then
            install -m 0644 $file ${D}${CADDY_DATA_DIR}/claive-api-reverse-proxy/
        fi
    done

    # Set appropriate ownership
    chown -R ${CADDY_USER}:${CADDY_GROUP} ${D}${CADDY_DATA_DIR}
    chown -R ${CADDY_USER}:${CADDY_GROUP} ${D}${CADDY_CONFIG_DIR}
    
    # Install helper scripts
    install -d ${D}${CADDY_SCRIPTS_DIR}
    install -m 0755 ${WORKDIR}/scripts/secretai_build_install_caddy.sh ${D}${CADDY_SCRIPTS_DIR}/
    install -m 0644 ${WORKDIR}/scripts/secretai_build_install_caddy.md ${D}${CADDY_SCRIPTS_DIR}/
}

pkg_postinst_ontarget:${PN}() {
    # Set capabilities for caddy (to bind to privileged ports without root)
    setcap cap_net_bind_service=+ep ${bindir}/caddy
    
    # Create Caddyfile if it doesn't exist
    if [ ! -f ${CADDY_CONFIG_DIR}/Caddyfile ]; then
        cp ${CADDY_CONFIG_DIR}/Caddyfile.template ${CADDY_CONFIG_DIR}/Caddyfile
        chown ${CADDY_USER}:${CADDY_GROUP} ${CADDY_CONFIG_DIR}/Caddyfile
    fi
    
    # Ensure proper folder permissions
    chown -R ${CADDY_USER}:${CADDY_GROUP} ${CADDY_DATA_DIR}
    chmod 0750 ${CADDY_DATA_DIR}
}

FILES:${PN} += " \
    ${CADDY_CONFIG_DIR} \
    ${CADDY_DATA_DIR} \
    ${CADDY_SCRIPTS_DIR} \
    ${systemd_unitdir}/system/secretai-caddy.service \
"

INSANE_SKIP:${PN} = "ldflags"
