package javaBot;

public class IRCUser {
	
	private String nick, user, mask;
	
	public static IRCUser stringToUser(String str) {
		String s = str;
		if (str.startsWith(":"))
		s = str.substring(1);
		String nick = s.split("!")[0];
		String user = s.split("!")[1].split("@")[0];
		if (user.startsWith("~"))
			user = user.substring(1);
		String mask = s.split("@")[1];
		
		return new IRCUser(user, nick, mask);
	}
	
	public IRCUser(String user, String nick) {
		this(user, nick, "");
	}
	
	public IRCUser(String user, String nick, String mask) {
		this.setUser(user);
		this.setNick(nick);
		this.setMask(mask);
	}
	
	public String toString() {
		return "User: " + this.user + " Nick: " + this.nick + " Mask: " + this.mask;
	}
	
	public boolean equals(IRCUser user) {
		return (this.getNick().equalsIgnoreCase(user.getNick())
				&& this.getUser().equalsIgnoreCase(user.getUser())
				&& this.getMask().equalsIgnoreCase(user.getMask()));
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getNick() {
		return nick;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getMask() {
		return mask;
	}
}
