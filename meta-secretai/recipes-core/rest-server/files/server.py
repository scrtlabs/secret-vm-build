"""
server.py

This module implements a REST server for Claive Attest REST server, providing endpoints for 
checking server status and submitting attestation data.

Endpoints:
    - /status: Returns the current status of the server.
    - /attestation: Retreaves attestation data and sends to the requester.

Author: Alex H
Email: alexh@scrtlabs.com
License: MIT

Usage:
    Run this script to start the server. It will expose the endpoints as defined
    for interacting with the Claive Attest REST service.

Dependencies:
    - Flask (or similar framework)
    - Other necessary libraries

"""

from flask import Flask, request, jsonify, redirect
from flask_sslify import SSLify
import os
from typing import Any, Optional, AnyStr, Tuple
import argparse
import logging
import subprocess
import json
import env # pylint: disable=unused-import
import config

# Initialize Flask app
app = Flask(__name__)
sslify: Optional[SSLify] = None

logger = logging.getLogger(__name__)

is_secure = True

@app.before_request
def before_request():
    """
    Flask before_request handler that enforces HTTPS.
    Redirects HTTP requests to HTTPS if is_secure flag is True.
    
    Returns:
        Redirect response to HTTPS URL if request is HTTP, None otherwise
    """
    if is_secure:
        if not request.is_secure:
            url = request.url.replace('http://', 'https://')
            return redirect(url, code=301)


# Status endpoint
@app.route('/status', methods=['GET'])
def status():
    """Return the current status of the server.
    
    This endpoint provides a simple health check to verify the server is running and responsive.
    It logs the request details and returns a JSON response indicating the server is alive.

    HTTP Method:
        GET

    Returns:
        tuple: A tuple containing:
            - JSON response with status message
            - HTTP 200 status code
            
    Example Response:
        {
            "status": "server is alive"
        }

    Logging:
        Logs the remote IP address and request URL at INFO level
    """
    logger.info(f'📫  {request.remote_addr} -> {request.base_url}')
    return jsonify({"status": "server is alive"}), 200

@app.route('/attestation', methods=['GET'])
def attestation():
    """Return an attestation payload from the attest_tool.
    This endpoint executes the external attest_tool command to generate an attestation
    and returns it as a JSON response.

    Returns:
        tuple: A tuple containing:
            - JSON response with attestation payload if successful
            - HTTP 200 status code on success
            - HTTP 500 status code with error message on failure

    Raises:
        FileNotFoundError: If attest_tool executable is not found
        subprocess.CalledProcessError: If attest_tool command fails
        json.JSONDecodeError: If attestation output is not valid JSON
    """
    logger.info(f'📫  {request.remote_addr} -> {request.base_url}')

    try:
        # Call the external 'attest' process
        result = subprocess.run(['./attest_tool', 'attest'], capture_output=True, text=True, check=True)
        attestation_payload = json.loads(result.stdout)  # Parse JSON output
    except FileNotFoundError:
        logger.error("attest_tool not found in PATH or current directory")
        raise    
    except subprocess.CalledProcessError as e:
        logger.error(f"Error calling 'attest': {e.stderr}")
        return jsonify({"error": "Failed to generate attestation."}), 500
    except json.JSONDecodeError as e:
        logger.error(f"Error decoding JSON output from 'attest': {e}")
        return jsonify({"error": "Invalid attestation format."}), 500

    return jsonify(attestation_payload), 200


def main():
    """
    Main function to initialize and run the Flask server.

    This function handles server configuration and startup:
    - Parses command line arguments for port, SSL, and logging options
    - Sets up logging configuration
    - Initializes SSL if enabled
    - Starts the Flask server

    Command line arguments:
        --secure: Enable/disable SSL (default: True)
        -p, --port: Port number to bind to (default: from config)
        -l, --loglevel: Logging level (default: from config)

    Environment variables used:
        CLAIVE_FLASK_SERVER_PORT: Server port
        CLAIVE_LOG_LEVEL: Logging level
        CLAIVE_REST_SERVER_IP: Server IP address

    Raises:
        OSError: If server fails to start due to port/permission issues
        ValueError: If invalid arguments are passed
        Exception: For other unexpected errors

    Returns:
        None
    """
    # Global variables should be minimized - consider making these class attributes instead
    global is_secure # pylint: disable=global-statement
    global sslify # pylint: disable=global-statement
    
    default_port = int(os.getenv(config.CLAIVE_FLASK_SERVER_PORT, config.CLAIVE_FLASK_SERVER_PORT_DEFAULT))
    default_log = os.getenv(config.CLAIVE_LOG_LEVEL, config.CLAIVE_LOG_LEVEL_DEFAULT)
    
    parser = argparse.ArgumentParser(description="SecretAI Attestation REST Server")
    parser.add_argument("--secure", type=bool, default=True, action=argparse.BooleanOptionalAction) # --no-secure will turn off ssl
    parser.add_argument("-p", "--port", type=int, default=default_port, help="Port number to bind to")
    parser.add_argument("-l", "--loglevel", type=str, default=default_log, help=f"Log level to use: {"|".join(config.ALLOWED_LOG_LEVELS)}")
    args = parser.parse_args()
    
    # Add validation for server IP environment variable
    if not os.getenv(config.CLAIVE_REST_SERVER_IP):
        logger.warning(f"⚠️  Missing required environment variable: {config.CLAIVE_REST_SERVER_IP}")
        
    server_ip = os.getenv(config.CLAIVE_REST_SERVER_IP, config.CLAIVE_REST_SERVER_IP_DEFAULT)

    server_port = args.port
    is_secure = args.secure # --no-secure will turn off ssl

    # Add validation for log level format
    arg_loglevel: str = args.loglevel
    if not isinstance(arg_loglevel, str):
        logger.error("🛑 Log level must be a string")
        return
        
    if arg_loglevel.lower() not in config.ALLOWED_LOG_LEVELS:
        arg_loglevel = config.LOG_LEVEL_DEFAULT
    log_level_map = logging.getLevelNamesMapping()
    app_log_level = log_level_map[arg_loglevel.upper()]

    logging.basicConfig(
        encoding="utf-8",
        format="{asctime}|{levelname:>4.4}|{funcName:>10.10}| {message}",
        style="{",
        level=app_log_level,
    )

    # Add validation for SSL certificate files
    ssl_context: Tuple[AnyStr | Any, AnyStr | Any] | None = None
    if is_secure:
        cert_path = 'cert/ssl_cert.pem'
        key_path = 'cert/ssl_key.pem'
        if not os.path.exists(cert_path) or not os.path.exists(key_path):
            logger.error("🛑 SSL certificate files not found")
            return
        ssl_context = (cert_path, key_path)
        sslify = SSLify(app)
        
    logger.info(f'⚙️  Running on {server_ip}:{server_port}' + (f' with {ssl_context}' if ssl_context else ''))
    try:
        # Add validation for port number range
        if not 1024 <= server_port <= 65535:
            raise ValueError("Port number must be between 0 and 65535")
            
        app.run(host=server_ip, port=server_port, ssl_context=ssl_context)
    except OSError as e:
        # Raised when port is already in use or permission denied
        logger.error(f"🛑 Failed to start Flask server - OS error: {e}")
        return
    except ValueError as e:
        # Raised when invalid arguments are passed to app.run()
        logger.error(f"🛑 Failed to start Flask server - Invalid arguments: {e}")
        return
    except KeyboardInterrupt:
        # Handle Ctrl+C gracefully
        logger.info("🛑 Server shutting down...")
        return
    except Exception as e:
        # Catch any other unexpected exceptions
        logger.error(f"🛑 Failed to start Flask server: {e}")
        return

if __name__ == '__main__':
    main()
