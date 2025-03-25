use pyo3;

#[pyo3::pymodule]
fn _rust(py: pyo3::Python, m: &pyo3::types::PyModule) -> pyo3::PyResult<()> {
    Ok(())
}
