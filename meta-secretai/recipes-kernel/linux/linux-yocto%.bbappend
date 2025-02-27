FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://secretai.scc"

KERNEL_FEATURES:append = " secretai.scc"
