# Secret VM Build System

## Project Overview
The Secret VM Build System is a specialized Yocto-based build system designed for creating secure virtual machines, primarily used within the Secret Network ecosystem. This build system enables the creation of secure, attestable virtual machine images that form the foundation for confidential computing environments.

Built for the Secret Network, a privacy-first blockchain platform, this system ensures that computational environments maintain the highest standards of security and privacy. The build system leverages TEE (Trusted Execution Environment) technology and remote attestation to provide verifiable guarantees about the integrity and confidentiality of running environments.

> **Security Note**: This build system is designed for production-grade secure environments. Proper handling of attestation keys and secure configurations is critical for maintaining the security guarantees.

## Repository Structure

### Meta Layers
- **meta-secret-vm**: Core layer containing Secret Network specific configurations and recipes
- **meta-secretai**: Layer for AI-specific optimizations and configurations
- **poky**: The reference distribution of the Yocto Project
- **meta-rust-bin**: Support for Rust programming language and toolchain
- **meta-openembedded**: Collection of layers providing additional recipes and configurations
- **meta-virtualization**: Support for virtualization technologies

### Key Directories
- **config**: Contains build configurations and template files
- **scripts**: Utility scripts for building and managing the environment

## Build System Components

### Key Recipes in meta-secret-vm
- Attestation tools for secure enclave verification
- Cryptographic tools and libraries
- OVMF firmware configurations
- Custom kernel modifications for security
- Root filesystem configurations and customizations

## Build Instructions

### Prerequisites
- Docker
- Git
- 50GB+ free disk space
- Linux host system (recommended: Ubuntu 20.04 or later)

### Environment Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/scrtlabs/secret-vm-build.git
   cd secret-vm-build
   ```

2. Launch the build environment:
   ```bash
   ./scripts/launch-docker.sh
   ```

3. Initialize the build environment:
   ```bash
   source ./oe-init-build-env
   ```

### Build Commands
- Basic VM image build:
  ```bash
  bitbake secret-vm-image
  ```

- GPU-enabled image build:
  ```bash
  MACHINE=secret-vm-gpu bitbake secret-vm-image
  ```

### Available Build Targets
- `secret-vm-image`: Standard VM image
- `secret-vm-image-gpu`: GPU-enabled VM image
- `secret-vm-image-dev`: Development VM image with additional tools

## Output Artifacts

Build artifacts are located in `build/tmp/deploy/images/secret-vm/`:
- `secret-vm.qcow2`: Main VM image
- `bzImage`: Linux kernel
- `initramfs.cpio.gz`: Initial RAM filesystem
- `OVMF.fd`: UEFI firmware image

## Usage Instructions

### Starting the VM
1. Navigate to the deployment directory:
   ```bash
   cd build/tmp/deploy/images/secret-vm/
   ```

2. Launch the VM using the provided script:
   ```bash
   ./start_vm.sh
   ```

### GPU Pass-through Configuration
1. Enable IOMMU in your host system
2. Configure PCI pass-through in your VM launch script
3. Use GPU-enabled image build

### Attestation Setup
> **Important**: Attestation keys are critical security components. Never commit them to version control or share them through insecure channels. Use proper key management systems in production environments.

1. Configure attestation service endpoints
2. Generate attestation keys
3. Start attestation services before VM launch

## Development and Contributing

### Customizing the Build System
1. Create a new layer:
   ```bash
   bitbake-layers create-layer meta-custom
   ```

2. Add custom recipes to your layer:
   ```
   meta-custom/
   ├── conf/
   │   └── layer.conf
   └── recipes-example/
       └── example/
           └── example_1.0.bb
   ```

### Contributing Guidelines
1. Fork the repository
2. Create a feature branch
3. Submit a Pull Request with:
   - Clear description of changes
   - Test results
   - Documentation updates
   - Signed-off commits

For issues and feature requests, please use the GitHub Issues tracker.

