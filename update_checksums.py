#!/usr/bin/env python3

import os
import re
import subprocess
import json

def get_package_info(recipe_path):
    # Extract package name and version from recipe filename
    filename = os.path.basename(recipe_path)
    match = re.match(r'python3-(.+)_(.+)\.bb', filename)
    if not match:
        return None, None
        
    pkg_name = match.group(1)
    version = match.group(2)
    
    # Handle special cases
    if pkg_name == 'flask_sslify':
        pkg_name = 'Flask-SSLify'
    elif pkg_name == 'python_dateutil':
        pkg_name = 'python-dateutil'
    elif pkg_name == 'bs4':
        pkg_name = 'beautifulsoup4'
    else:
        # Convert package name to PyPI format (hyphens instead of underscores)
        pkg_name = pkg_name.replace('_', '-')
    
    return pkg_name, version

def get_checksums(pkg_name, version):
    try:
        # Query PyPI for package information
        pypi_url = f"https://pypi.org/pypi/{pkg_name}/{version}/json"
        cmd = ['curl', '-s', pypi_url]
        result = subprocess.run(cmd, capture_output=True, text=True)
        
        if result.returncode != 0:
            print(f"Failed to fetch data for {pkg_name} {version}")
            return None, None
            
        pypi_data = json.loads(result.stdout)
        
        # Find the source distribution (*.tar.gz) URL
        sdist_url = None
        for url_info in pypi_data['urls']:
            if url_info['packagetype'] == 'sdist':
                sdist_url = url_info['url']
                md5_sum = url_info['md5_digest']
                sha256_sum = url_info['digests']['sha256']
                return md5_sum, sha256_sum
        
        print(f"No source distribution found for {pkg_name} {version}")
        return None, None
        
    except Exception as e:
        print(f"Error fetching checksums for {pkg_name} {version}: {e}")
        return None, None

def update_recipe(recipe_path, md5_sum, sha256_sum):
    with open(recipe_path, 'r') as file:
        content = file.read()
    
    if md5_sum:
        content = re.sub(r'SRC_URI\[md5sum\] = "md5sum_placeholder".*', f'SRC_URI[md5sum] = "{md5_sum}"', content)
    
    if sha256_sum:
        content = re.sub(r'SRC_URI\[sha256sum\] = "sha256sum_placeholder".*', f'SRC_URI[sha256sum] = "{sha256_sum}"', content)
    
    with open(recipe_path, 'w') as file:
        file.write(content)

def main():
    # Manually specify the recipes that need updating
    recipes = [
        './meta-secretai/recipes-python/python-attrs/python3-attrs_20.3.0.bb',
        './meta-secretai/recipes-python/python-bs4/python3-bs4_0.0.2.bb',
        './meta-secretai/recipes-python/python-click/python3-click_8.1.8.bb',
        './meta-secretai/recipes-python/python-cryptography/python3-cryptography_44.0.2.bb',
        './meta-secretai/recipes-python/python-flask/python3-flask_3.1.0.bb',
        './meta-secretai/recipes-python/python-flask_sslify/python3-flask_sslify_0.1.5.bb',
        './meta-secretai/recipes-python/python-grpclib/python3-grpclib_0.4.7.bb',
        './meta-secretai/recipes-python/python-h2/python3-h2_4.2.0.bb',
        './meta-secretai/recipes-python/python-itsdangerous/python3-itsdangerous_2.2.0.bb',
        './meta-secretai/recipes-python/python-jinja2/python3-jinja2_3.1.5.bb',
        './meta-secretai/recipes-python/python-multidict/python3-multidict_6.1.0.bb',
        './meta-secretai/recipes-python/python-pycparser/python3-pycparser_2.22.bb',
        './meta-secretai/recipes-python/python-python_dateutil/python3-python_dateutil_2.9.0.post0.bb',
        './meta-secretai/recipes-python/python-wheel/python3-wheel_0.44.0.bb',
        './meta-secretai/recipes-python/python-wrapt/python3-wrapt_1.17.2.bb'
    ]
    
    for recipe_path in recipes:
        print(f"Processing {recipe_path}...")
        pkg_name, version = get_package_info(recipe_path)
        
        if not pkg_name or not version:
            print(f"  Could not extract package name and version from {recipe_path}")
            continue
            
        print(f"  Package: {pkg_name}, Version: {version}")
        md5_sum, sha256_sum = get_checksums(pkg_name, version)
        
        if md5_sum and sha256_sum:
            print(f"  MD5: {md5_sum}")
            print(f"  SHA256: {sha256_sum}")
            update_recipe(recipe_path, md5_sum, sha256_sum)
            print(f"  Updated {recipe_path}")
        else:
            print(f"  Failed to get checksums for {pkg_name} {version}")

if __name__ == "__main__":
    main()
