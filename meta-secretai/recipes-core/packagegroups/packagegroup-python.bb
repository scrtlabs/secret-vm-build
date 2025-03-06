SUMMARY = "Python packages for SecretAI"
LICENSE = "MIT"

inherit packagegroup

RDEPENDS:${PN} = " \
    python3-aiohappyeyeballs \
    python3-aiohttp \
    python3-aiosignal \
    python3-attrs \
    python3-bech32 \
    python3-betterproto \
    python3-bip32utils \
    python3-blinker \
    python3-boltons \
    python3-bs4 \
    python3-cffi \
    python3-click \
    python3-cryptography \
    python3-ecdsa \
    python3-flask \
    python3-flask_sslify \
    python3-frozenlist \
    python3-furl \
    python3-grpclib \
    python3-h2 \
    python3-hpack \
    python3-hyperframe \
    python3-idna \
    python3-itsdangerous \
    python3-jinja2 \
    python3-markupsafe \
    python3-miscreant \
    python3-mnemonic \
    python3-multidict \
    python3-nest_asyncio \
    python3-orderedmultidict \
    python3-propcache \
    python3-protobuf \
    python3-pycparser \
    python3-dateutil \
    python3-dotenv \
    python3-secret_sdk \
    python3-setuptools \
    python3-six \
    python3-tensorboardx \
    python3-werkzeug \
    python3-wheel \
    python3-wrapt \
    python3-yarl \
"
