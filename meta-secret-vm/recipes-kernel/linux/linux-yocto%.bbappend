FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://secret-vm.scc"

KERNEL_FEATURES:append = " secret-vm.scc"

COMPATIBLE_MACHINE:secret-vm-tdx = "secret-vm-tdx"
COMPATIBLE_MACHINE:secret-vm-sev = "secret-vm-sev"

KMACHINE:secret-vm-tdx = "qemux86-64"
KMACHINE:secret-vm-sev = "qemux86-64"
