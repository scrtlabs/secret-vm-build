FROM ubuntu:22.04@sha256:0e5e4a57c2499249aafc3b40fcd541e9a456aab7296681a3994d631587203f97

# Prevent timezone prompt by setting noninteractive frontend and configuring tzdata
ENV DEBIAN_FRONTEND=noninteractive \
    TZ=Etc/UTC \
    TZDATA=Etc/UTC \
    LC_ALL=en_US.UTF-8 \
    LANG=en_US.UTF-8

# Set timezone
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Install required packages
RUN apt update && apt install -y \
    build-essential \
    chrpath \
    cpio \
    diffstat \
    file \
    gawk \
    lz4 \
    wget \
    zstd \
    python3 \
    locales \
    git \
    git-lfs \
    xorriso \
    qemu-utils

# Generate locale for bitbake
RUN locale-gen en_US.UTF-8
