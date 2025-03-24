To generate the `ssl_cert.pem` and `ssl_key.pem` files on Ubuntu 24.04, follow these steps:

### Step 1: Install OpenSSL
Ensure that OpenSSL is installed on your system. Open a terminal and run:
```bash
sudo apt update
sudo apt install openssl
```

### Step 2: Generate a Private Key
Run the following command to generate a 2048-bit private key:
```bash
openssl genrsa -out ssl_key.pem 2048
```

This creates a private key file named `ssl_key.pem`.

### Step 3: Generate a Certificate Signing Request (CSR)
Generate a CSR using the private key:
```bash
openssl req -new -key ssl_key.pem -out server.csr
```

You will be prompted to enter information for the certificate. For example:
- **Country Name (2 letter code)**: `US`
- **State or Province Name**: `California`
- **Locality Name**: `San Francisco`
- **Organization Name**: `My Company`
- **Organizational Unit Name**: `IT`
- **Common Name**: The domain name of your server (e.g., `example.com` or `localhost` for testing).
- **Email Address**: Your email.

Press **Enter** for any fields you want to leave blank.

### Step 4: Generate a Self-Signed Certificate
Generate a self-signed certificate valid for 365 days using the CSR and private key:
```bash
openssl x509 -req -days 365 -in server.csr -signkey ssl_key.pem -out ssl_cert.pem
```

This creates the `ssl_cert.pem` file.

### Step 5: Verify the Generated Files
Ensure both files are present:
```bash
ls -l ssl_cert.pem ssl_key.pem
```

### Step 6: Use the Generated Files in Your Flask Application
Replace `/path/to/ssl_cert.pem` and `/path/to/ssl_key.pem` in your code with the paths to these files. For example:
```python
context = ('ssl_cert.pem', 'ssl_key.pem')
```

### Notes:
- This self-signed certificate is suitable for testing or development purposes. For production, use a trusted Certificate Authority (CA) like Let's Encrypt.
- If using Let's Encrypt, tools like **Certbot** can automate the process of obtaining and renewing certificates.
