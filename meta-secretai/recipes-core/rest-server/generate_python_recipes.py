#!/usr/bin/env python3

import os
import re
import sys
from pathlib import Path
def extract_version_from_constraint(version_constraint):
    """Extract clean version from version constraint string"""
    # Remove any version specifiers like >=, ==, etc.
    return re.sub(r'^[<>=!~]+', '', version_constraint)
def normalize_package_name(name):
    """Convert PyPI package name to Yocto naming convention"""
    # Convert to lowercase and replace hyphens with underscores
    return name.lower().replace('-', '_')

def parse_requirements(req_file):
    """Parse requirements.txt file to extract package names and versions"""
    packages = []
    
    with open(req_file, 'r') as f:
        for line in f:
            line = line.strip()
            
            # Skip empty lines and comments
            if not line or line.startswith('#'):
                continue
                
            # Remove inline comments
            if '#' in line:
                line = line.split('#')[0].strip()
                
            # Skip git+, local file installations
            if line.startswith(('git+', 'file://', '-e ')):
                print(f"Skipping unsupported requirement: {line}")
                continue
                
            # Handle package with version constraint
            match = re.match(r'^([a-zA-Z0-9_.-]+)([<>=!~]+)(.*)$', line)
            if match:
                package_name = match.group(1)
                version_constraint = match.group(2)
                version = match.group(3)
                packages.append((package_name, version_constraint + version))
            else:
                # Handle package without version
                packages.append((line, ""))
                
    return packages

def create_recipe_file(package_name, version, output_dir):
    """Create a Yocto recipe file for a Python package"""
    
    # Create normalized package name for recipe
    normalized_name = normalize_package_name(package_name)
    recipe_name = f"python3-{normalized_name}"
    
    # Create directory for the recipe
    recipe_dir = os.path.join(output_dir, f"python-{normalized_name}")
    os.makedirs(recipe_dir, exist_ok=True)
    
    # Use default version if not provided
    if not version:
        version = "0.0.0"  # Default if no version info available
    else:
        # Clean up version string by removing constraints
        version = extract_version_from_constraint(version)

    # Prepare recipe content
    recipe_content = [
        f'SUMMARY = "Python {package_name} module"',
        f'HOMEPAGE = "https://pypi.org/project/{package_name}/"',
        f'LICENSE = "CLOSED"  # Update this with the actual license',
    ]
    
    # Add PyPI specific fields
    recipe_content.extend([
        '',
        'inherit pypi setuptools3',
        '',
        f'SRC_URI[md5sum] = "md5sum_placeholder"  # Replace with actual md5sum',
        f'SRC_URI[sha256sum] = "sha256sum_placeholder"  # Replace with actual sha256sum',
        '',
        f'PYPI_PACKAGE = "{package_name}"',
        '',
        'RDEPENDS:${PN} += " \\\\\\n    ${PYTHON_PN}-core \\\\\\n"',
        ''
    ])
    
    # Write recipe file
    recipe_file = os.path.join(recipe_dir, f"{recipe_name}_{version}.bb")
    with open(recipe_file, 'w') as f:
        f.write('\n'.join(recipe_content))
    
    print(f"Created recipe: {recipe_file}")
    return recipe_file

def main():
    if len(sys.argv) < 2:
        req_file = "requirements.txt"
        if not os.path.exists(req_file):
            req_file = "files/requirements.txt"
            if not os.path.exists(req_file):
                print("Error: requirements.txt not found. Please specify the path to requirements.txt as an argument.")
                sys.exit(1)
    else:
        req_file = sys.argv[1]
        
    # Determine output directory
    current_dir = os.path.dirname(os.path.abspath(__file__))
    output_dir = os.path.join(os.path.dirname(current_dir), "recipes-python")
    
    # Create output directory if it doesn't exist
    os.makedirs(output_dir, exist_ok=True)
    
    print(f"Parsing requirements from: {req_file}")
    print(f"Output directory: {output_dir}")
    
    # Parse requirements.txt
    packages = parse_requirements(req_file)
    print(f"Found {len(packages)} packages")
    
    # Create recipe for each package
    for package_name, version in packages:
        print(f"Processing {package_name} {version}")
        
        # Create recipe file with template values
        create_recipe_file(package_name, version, output_dir)
    
    print("\nRecipe generation complete.")
    print(f"Remember to update the md5sum and sha256sum values in the recipe files.")
    print(f"You can generate these by downloading the packages and using the md5sum and sha256sum commands.")
    print(f"\nAdd the following to your recipe that depends on these packages:")
    print('RDEPENDS:${PN} += " \\')
    for package_name, _ in packages:
        normalized_name = normalize_package_name(package_name)
        print(f'    python3-{normalized_name} \\')
    print('"')

if __name__ == "__main__":
    main()

