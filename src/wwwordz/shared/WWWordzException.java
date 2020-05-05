package wwwordz.shared;

/**
 * <b>A exception in WWWordz. All constructors delegate in the super class.</b><br>
 * @author Rúben Lôpo, Rui Ramos
 */
public class WWWordzException
extends java.lang.Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WWWordzException() {
		super();
	}

	public WWWordzException(java.lang.String message,
            				java.lang.Throwable cause) {
		super(message, cause);
	}
	
	public WWWordzException(java.lang.String message) {
		super(message);
	}
	
	public WWWordzException(java.lang.Throwable cause) {
		super(cause);
	}
}
