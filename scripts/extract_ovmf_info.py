#!/usr/bin/env python3
# SPDX-License-Identifier: Apache-2.0
#
# extract_ovmf_info.py — Extract all OVMF metadata needed for dstack KMS
# measurement verification, so that the KMS host does NOT need the OVMF file.
#
# Output (JSON on stdout):
#   {
#     "ovmf_hash":             "<96 hex chars>",   # GCTX hash of OVMF pages
#     "sev_hashes_table_gpa":  <uint64>,            # GPA of the SEV hashes table
#     "sev_es_reset_eip":      <uint32>,             # AP reset EIP
#     "ovmf_sections": [
#       {"gpa": <uint32>, "size": <uint32>, "section_type": <uint32>}
#     ]
#   }
#
# Usage:
#   python3 extract_ovmf_info.py <ovmf.fd>
#   python3 extract_ovmf_info.py <ovmf.fd> >> config/sev_image_fingerprints.json
#
# Pure Python 3 — no external dependencies.

import hashlib
import json
import struct
import sys
import uuid

FOUR_GB = 0x1_0000_0000

# Footer table entry: size_u16(2) + guid_le(16) = 18 bytes
ENTRY_HDR = 18

def guid_le(guid_str: str) -> bytes:
    """Convert a GUID string to its little-endian 16-byte representation."""
    return uuid.UUID("{" + guid_str + "}").bytes_le

GUID_FOOTER_TABLE      = guid_le("96b582de-1fb2-45f7-baea-a366c55a082d")
GUID_SEV_HASH_TABLE_RV = guid_le("7255371f-3a3b-4b04-927b-1da6efa8d454")
GUID_SEV_ES_RESET_BLK  = guid_le("00f771de-1a7e-4fcb-890e-68c77e2fb44e")
GUID_SEV_META_DATA     = guid_le("dc886566-984a-4798-a75e-5585a7bf67cc")


def parse_ovmf(path: str) -> dict:
    with open(path, "rb") as f:
        data = f.read()

    size = len(data)
    gpa_base = FOUR_GB - size  # GPA where OVMF is mapped

    # ------------------------------------------------------------------ footer
    footer_off = size - 32 - ENTRY_HDR
    if data[footer_off + 2:footer_off + 18] != GUID_FOOTER_TABLE:
        raise RuntimeError(
            f"OVMF footer GUID not found at offset {footer_off:#x}. "
            "Is this an AMD SEV OVMF file?"
        )
    footer_total = struct.unpack_from("<H", data, footer_off)[0]
    table_size   = footer_total - ENTRY_HDR
    table_start  = footer_off - table_size
    table        = data[table_start:footer_off]

    sev_hashes_table_gpa = 0
    sev_es_reset_eip     = 0
    meta_offset_from_end = None

    pos = len(table)
    while pos >= ENTRY_HDR:
        entry_off  = pos - ENTRY_HDR
        entry_size = struct.unpack_from("<H", table, entry_off)[0]
        if entry_size < ENTRY_HDR:
            raise RuntimeError(f"Invalid footer table entry size {entry_size}")
        guid       = table[entry_off + 2:entry_off + 18]
        data_start = pos - entry_size
        data_end   = pos - ENTRY_HDR
        entry_data = table[data_start:data_end]

        if guid == GUID_SEV_HASH_TABLE_RV and len(entry_data) >= 4:
            sev_hashes_table_gpa = struct.unpack_from("<I", entry_data)[0]
        elif guid == GUID_SEV_ES_RESET_BLK and len(entry_data) >= 4:
            sev_es_reset_eip = struct.unpack_from("<I", entry_data)[0]
        elif guid == GUID_SEV_META_DATA and len(entry_data) >= 4:
            meta_offset_from_end = struct.unpack_from("<I", entry_data)[0]

        pos -= entry_size

    if sev_es_reset_eip == 0:
        raise RuntimeError("SEV_ES_RESET_BLOCK not found in OVMF footer table")

    # --------------------------------------------------------- SEV metadata
    sections = []
    if meta_offset_from_end is not None:
        meta_start = size - meta_offset_from_end
        sig = data[meta_start:meta_start + 4]
        if sig != b"ASEV":
            raise RuntimeError(f"Bad SEV metadata signature: {sig!r}")
        meta_version = struct.unpack_from("<I", data, meta_start + 8)[0]
        if meta_version != 1:
            raise RuntimeError(f"Unsupported SEV metadata version {meta_version}")
        num_items   = struct.unpack_from("<I", data, meta_start + 12)[0]
        items_start = meta_start + 16
        for i in range(num_items):
            off      = items_start + i * 12
            sec_gpa  = struct.unpack_from("<I", data, off)[0]
            sec_size = struct.unpack_from("<I", data, off + 4)[0]
            sec_type = struct.unpack_from("<I", data, off + 8)[0]
            sections.append({"gpa": sec_gpa, "size": sec_size, "section_type": sec_type})

    return {
        "data":                  data,
        "gpa_base":              gpa_base,
        "sev_hashes_table_gpa":  sev_hashes_table_gpa,
        "sev_es_reset_eip":      sev_es_reset_eip,
        "sections":              sections,
    }


def compute_gctx_ovmf_hash(data: bytes, gpa_base: int) -> str:
    """
    Compute the GCTX launch digest for OVMF normal pages.

    This is the digest you get after feeding every 4 KiB OVMF page into the
    SNP GCTX accumulator (AMD SNP spec §8.17.2, PAGE_INFO page_type=0x01).
    It seeds the GCTX for the full measurement computation, binding the KMS
    to a specific OVMF binary without storing the full file.

    Matches: sev-snp-measure --mode snp:ovmf-hash --ovmf <file>
    """
    ld            = bytes(48)   # 48-byte SHA-384 launch digest, starts at all-zeros
    page_type     = 0x01        # Normal
    page_info_len = 0x70        # 112 bytes

    for i in range(0, len(data), 4096):
        page    = data[i:i + 4096]
        gpa     = gpa_base + i
        content = hashlib.sha384(page).digest()   # SHA-384 of page content

        # PAGE_INFO layout (AMD SNP spec §8.17.2 Table 67):
        #   [  0.. 48) current LD
        #   [ 48.. 96) page content hash
        #   [ 96.. 98) page_info_len u16 LE
        #   [ 98]      page_type
        #   [ 99]      is_imi = 0
        #   [100..104) vmpl3/2/1_perms + reserved = 0
        #   [104..112) GPA u64 LE
        page_info = (
            ld
            + content
            + struct.pack("<H", page_info_len)
            + bytes([page_type, 0, 0, 0, 0, 0])   # type + is_imi + perms(3) + reserved
            + struct.pack("<Q", gpa)
        )
        assert len(page_info) == page_info_len, len(page_info)
        ld = hashlib.sha384(page_info).digest()

    return ld.hex()


def main():
    if len(sys.argv) < 2:
        print(f"Usage: {sys.argv[0]} <ovmf.fd>", file=sys.stderr)
        sys.exit(1)

    ovmf_path = sys.argv[1]
    info = parse_ovmf(ovmf_path)

    ovmf_hash = compute_gctx_ovmf_hash(info["data"], info["gpa_base"])

    result = {
        "ovmf_hash":            ovmf_hash,
        "sev_hashes_table_gpa": info["sev_hashes_table_gpa"],
        "sev_es_reset_eip":     info["sev_es_reset_eip"],
        "ovmf_sections":        info["sections"],
    }
    print(json.dumps(result, indent=2))


if __name__ == "__main__":
    main()
