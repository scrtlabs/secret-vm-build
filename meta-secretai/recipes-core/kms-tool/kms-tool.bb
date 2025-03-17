SUMMARY = "TDX attest tool"
DESCRIPTION = "${SUMMARY}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit systemd

SRC_URI = "file://kms.sh \
          file://kms_query.py \
          file://requirements.txt \
          file://kms-tool.service"

S = "${WORKDIR}"

# Python dependencies
RDEPENDS:${PN} += "\
    cryptsetup \
    crypt-tool \
    attest-tool \
    python3-secret-sdk \
    python3-authlib \
    python3-certifi \
    python3-charset-normalizer \
    python3-cryptography \
    python3-idna \
    python3-pycparser \
    python3-urllib3 \
    python3-core \
"

inherit python3-dir

SYSTEMD_SERVICE:${PN} = "kms-tool.service"
SYSTEMD_AUTO_ENABLE = "enable"

do_install() {
    # Install binaries
    install -d ${D}${bindir}
    # install -m 0755 ${S}/attest_tool ${D}${bindir}
    
    # Install shell scripts
    install -m 0755 ${S}/kms.sh ${D}${bindir}
    
    # Install Python scripts and modules
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}/kms_tool
    
    # Install all Python scripts from the workdir
    for py_file in ${S}/*.py; do
        if [ -f "$py_file" ]; then
            install -m 0644 $py_file ${D}${PYTHON_SITEPACKAGES_DIR}/kms_tool/
        fi
    done
    
    # Make sure we have an __init__.py file
    touch ${D}${PYTHON_SITEPACKAGES_DIR}/kms_tool/__init__.py
    
    # Also copy Python scripts to bindir for direct execution
    for py_script in ${S}/*.py; do
        if [ -f "$py_script" ] && grep -q "^#!/usr/bin/env python" "$py_script"; then
            base_name=$(basename "$py_script")
            install -m 0755 $py_script ${D}${bindir}/$base_name
        fi
    done

    # Install systemd service file
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/kms-tool.service ${D}${systemd_system_unitdir}/
}

FILES:${PN} += "\
    ${bindir}/* \
    ${PYTHON_SITEPACKAGES_DIR}/kms_tool/* \
    ${PYTHON_SITEPACKAGES_DIR}/kms_tool \
    ${systemd_system_unitdir}/* \
"
