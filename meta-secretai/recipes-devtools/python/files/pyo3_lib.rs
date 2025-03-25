#[allow(dead_code)]
pub mod prelude {
    pub use crate::types::PyModule;
    pub use crate::PyResult;
    pub use crate::Python;
}
#[allow(dead_code)]
pub mod types {
    pub struct PyModule;
}
pub struct Python;
pub type PyResult<T> = Result<T, ()>;
