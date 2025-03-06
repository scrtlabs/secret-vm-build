SUMMARY = "REST Server for Secret AI"
DESCRIPTION = "A REST API server providing attestation and verification services"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit systemd python3-dir

SRC_URI = "file://server.py \
           file://env.py \
           file://config.py \
           file://attest_tool \
"

S = "${WORKDIR}"

# Python dependencies generated in the previous step
RDEPENDS:${PN} += " \
    python3-blinker \
    python3-bs4 \
    python3-click \
    python3-flask \
    python3-flask-sslify \
    python3-itsdangerous \
    python3-jinja2 \
    python3-markupsafe \
    python3-dotenv \
    python3-setuptools \
    python3-tensorboardx \
    python3-werkzeug \
    python3-wheel \
    python3-core \
"

# Install the application files
do_install() {
    # Create the necessary directory structure
    install -d ${D}${bindir}/rest-server
    install -d ${D}${sysconfdir}/rest-server
    
    # Install Python files
    install -m 0755 ${WORKDIR}/server.py ${D}${bindir}/rest-server/
    install -m 0644 ${WORKDIR}/env.py ${D}${bindir}/rest-server/
    install -m 0644 ${WORKDIR}/config.py ${D}${bindir}/rest-server/
    
    # Install the attestation tool
    install -m 0755 ${WORKDIR}/attest_tool ${D}${bindir}/rest-server/
    
    # Install systemd service
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${systemd_system_unitdir}
        install -m 0644 ${WORKDIR}/rest-server.service ${D}${systemd_system_unitdir}/
    fi
}

# Create systemd service file dynamically since it's not in the source files
do_compile:append() {
    cat > ${WORKDIR}/rest-server.service << EOF
[Unit]
Description=REST Server for Secret AI
After=network.target

[Service]
Type=simple
User=root
WorkingDirectory=${bindir}/rest-server
ExecStart=/usr/bin/python3 ${bindir}/rest-server/server.py
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
EOF
}

# Enable the systemd service if systemd is in use
SYSTEMD_SERVICE:${PN} = "rest-server.service"
SYSTEMD_AUTO_ENABLE = "enable"

FILES:${PN} += "${bindir}/rest-server/* ${sysconfdir}/rest-server/*"

