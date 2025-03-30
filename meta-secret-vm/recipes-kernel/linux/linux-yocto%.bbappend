FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://secret-vm.scc"

KERNEL_FEATURES:append = " secret-vm.scc"
