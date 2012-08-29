package javaBot;

public class ChannelUser {
	
	private String prefix = "", nick = "";

	public ChannelUser(String nick, String prefix) {
		this.setNick(nick);
		this.setPrefix(prefix);
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getNick() {
		return nick;
	}

}
