package exceptions;

public class NotConnectedException extends Exception {

	private static final long serialVersionUID = 5514155330516924614L;
	
	public NotConnectedException() {
		super("The bot is not currently connected to a server.");
	}

}
