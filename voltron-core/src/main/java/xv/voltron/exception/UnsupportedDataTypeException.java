package xv.voltron.exception;

public class UnsupportedDataTypeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnsupportedDataTypeException() {
		
	}
	
	public UnsupportedDataTypeException(String msg) {
		super(msg);
	}
	
	public UnsupportedDataTypeException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public UnsupportedDataTypeException(Throwable cause) {
		super(cause);
	}
}
