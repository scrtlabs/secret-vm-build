# Extend python3-dateutil recipe to use version 2.9.0.post0

# Override the SRC_URI checksum for the new version
SRC_URI[sha256sum] = "37dd54208da7e1cd875388217d5e00ebd4179249f90fb72437e91a35459a0ad3"

# Set the PV to include the post0 suffix
PV = "2.9.0.post0"
