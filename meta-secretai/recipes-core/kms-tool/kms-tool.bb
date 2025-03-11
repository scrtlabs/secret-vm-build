SUMMARY = "TDX attest tool"
DESCRIPTION = "${SUMMARY}"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

<<<<<<< Updated upstream
SRC_URI = "file://${THISDIR}/files"
S = "${WORKDIR}/${THISDIR}/files"
=======
SRC_URI = "file://kms.sh \
           file://kms_query.py \
           file://requirements.txt"

S = "${WORKDIR}"
>>>>>>> Stashed changes

# Python dependencies
RDEPENDS:${PN} += "\
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
}

do_run_script() {
    # Execute your script
    ${S}/kms.sh
}

# Set up proper task dependencies
addtask run_script after do_install before do_package
