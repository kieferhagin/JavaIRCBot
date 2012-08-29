package exceptions;

public class NickInUseException extends Exception {

	private static final long serialVersionUID = -3826154399883055932L;

	public NickInUseException() {
		super("That nickname is already in use on the server.");
	}

}
