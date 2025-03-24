do_install:append() {
    # Install systemd service file
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${systemd_system_unitdir}
        
        # Create systemd service file
        echo '[Unit]' > ${D}${systemd_system_unitdir}/caddy.service
        echo 'Description=Caddy HTTP/2 web server' >> ${D}${systemd_system_unitdir}/caddy.service
        echo 'Documentation=https://caddyserver.com/docs' >> ${D}${systemd_system_unitdir}/caddy.service
        echo 'After=network-online.target' >> ${D}${systemd_system_unitdir}/caddy.service
        echo 'Wants=network-online.target systemd-networkd-wait-online.service' >> ${D}${systemd_system_unitdir}/caddy.service
        echo '' >> ${D}${systemd_system_unitdir}/caddy.service
        echo '[Service]' >> ${D}${systemd_system_unitdir}/caddy.service
        echo 'Type=notify' >> ${D}${systemd_system_unitdir}/caddy.service
        echo 'ExecStart=${bindir}/caddy run --environ --config /etc/caddy/Caddyfile' >> ${D}${systemd_system_unitdir}/caddy.service
        echo 'ExecReload=${bindir}/caddy reload --config /etc/caddy/Caddyfile' >> ${D}${systemd_system_unitdir}/caddy.service
        echo 'TimeoutStopSec=5s' >> ${D}${systemd_system_unitdir}/caddy.service
        echo 'LimitNOFILE=1048576' >> ${D}${systemd_system_unitdir}/caddy.service
        echo 'LimitNPROC=512' >> ${D}${systemd_system_unitdir}/caddy.service
        echo 'PrivateTmp=true' >> ${D}${systemd_system_unitdir}/caddy.service
        echo 'ProtectSystem=full' >> ${D}${systemd_system_unitdir}/caddy.service
        echo 'AmbientCapabilities=CAP_NET_BIND_SERVICE' >> ${D}${systemd_system_unitdir}/caddy.service
        echo '' >> ${D}${systemd_system_unitdir}/caddy.service
        echo '[Install]' >> ${D}${systemd_system_unitdir}/caddy.service
        echo 'WantedBy=multi-user.target' >> ${D}${systemd_system_unitdir}/caddy.service
    fi

    # Create configuration directory
    install -d ${D}${sysconfdir}/caddy
    
    # Create a default Caddyfile
    echo '# Default Caddy configuration file' > ${D}${sysconfdir}/caddy/Caddyfile
    echo '# See https://caddyserver.com/docs/caddyfile for details' >> ${D}${sysconfdir}/caddy/Caddyfile
    echo '' >> ${D}${sysconfdir}/caddy/Caddyfile
    echo ':80 {' >> ${D}${sysconfdir}/caddy/Caddyfile
    echo '    respond "Caddy works!"' >> ${D}${sysconfdir}/caddy/Caddyfile
    echo '}' >> ${D}${sysconfdir}/caddy/Caddyfile
}
