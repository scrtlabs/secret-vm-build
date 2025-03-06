# Add an RPROVIDES to satisfy python3-nest_asyncio dependencies
RPROVIDES:${PN} += "python3-nest_asyncio"

# Make this the preferred provider
PREFERRED_VERSION_python3-nest-asyncio = "1.5.5"
