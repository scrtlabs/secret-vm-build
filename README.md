# Secret VM Build System

## Project Overview
The Secret VM Build System is a specialized Yocto-based build system designed for creating secure virtual machines, primarily used within the Secret Network ecosystem. This build system enables the creation of secure, attestable virtual machine images that form the foundation for confidential computing environments.

Built for the Secret Network, a privacy-first blockchain platform, this system ensures that computational environments maintain the highest standards of security and privacy. The build system leverages TEE (Trusted Execution Environment) technology and remote attestation to provide verifiable guarantees about the integrity and confidentiality of running environments.

> **Security Note**: This build system is designed for production-grade secure environments. Proper handling of attestation keys and secure configurations is critical for maintaining the security guarantees.

## Build Instructions

### Prerequisites
- Docker
- Git
- 120GB+ free disk space
- Linux host system (recommended: Ubuntu 20.04 or later)

### Environment Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/scrtlabs/secret-vm-build.git
   cd secret-vm-build
   ```

2. Initializes and updates all submodules:
   ```bash
   git submodule update --init
   ```

3. Build:
   ```bash
   scripts/build_reproducible.sh
   ```

## Output Artifacts

Build artifacts are located in `./artifacts`:
- `rootfs.cpio`, `rootfs.iso`: Root file system for VMs without GPU
- `rootfs-gpu.cpio`. `rootfs-gpu.iso`: Root file system for VMs with GPU
- `bzImage`: Linux kernel
- `initramfs.cpio.gz`: Initial RAM filesystem
- `ovmf.fd`: UEFI firmware image
- `encryptedfs.qcow2`: empty image for the encrypted file system

## Usage Instructions

### Starting the VM
1. Without GPU:
   ```bash
   scripts/start_vm.sh
   ```
2. With GPU:
   ```bash
   sudo scripts/start_vm_gpu.sh
   ```

### GPU Pass-through Configuration
1. Enable IOMMU in your host system
2. Configure PCI pass-through in your VM launch script
3. Use GPU-enabled image build
