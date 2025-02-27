//! OIDs used by the RATLS protocol.

/// OID for the SGX/TDX quote extension.
pub const PHALA_RATLS_QUOTE: &[u64] = &[1, 3, 6, 1, 4, 1, 62397, 1, 1];
/// OID for the TDX event log extension.
pub const PHALA_RATLS_EVENT_LOG: &[u64] = &[1, 3, 6, 1, 4, 1, 62397, 1, 2];
/// OID for the application info extension.
pub const PHALA_RATLS_APP_INFO: &[u64] = &[1, 3, 6, 1, 4, 1, 62397, 1, 3];
