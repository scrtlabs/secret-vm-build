FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://httpd.cfg \
            file://truncate.cfg \
            file://base64.cfg \
            file://cron.cfg"
