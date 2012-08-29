package javaBot;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import exceptions.NotConnectedException;

public class JavaBot {

	private JavaBotConnection con;
	private IRCUser me;
	private ArrayList<IRCChannel> channels;

	public JavaBot(IRCUser me) {
		channels = new ArrayList<IRCChannel>();
		this.me = me;
		con = null;
	}

	public static void main(String[] args) {
		new JavaBot(new IRCUser("JB", "JavaBot", "localhost")).connect("irc.strictfp.com", IRCConnection.DEFAULT_PORT);
	}

	protected void connect(String host) {
		this.connect(host, IRCConnection.DEFAULT_PORT);
	}

	protected void connect(String host, int port) {
		con = new JavaBotConnection(this, host, port);
		try {
			con.connect();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public IRCUser getUser() { return me; }

	protected void onConnect() {}
	
	protected void onDisconnect() {}

	protected void onPrivMessage(IRCUser user, String msg) {}
	
	protected void onMessage(String channel, IRCUser user, String msg) {}
	
	protected void onJoin(String channel, IRCUser user) {}

	protected void onServerMessage(String server, int cmd, String data) {}
	
	protected void onQuit(IRCUser user, String reason) {}
	
	protected void onPart(IRCUser user, String channel) {}
	
	protected void onKick(IRCUser kicker, String kicked, String channel, String reason) {}
	
	protected void onNickChange(IRCUser origUser, String newNick) {}
	
	protected void onNickInUse() {}
	
	protected void onChannelUpdate(IRCChannel channel) {}
	
	protected void onAction(String channel, IRCUser user, String msg) {}
	
	protected void onTopicChange(String channel, IRCUser user, String topic) {}
	
	protected void onModeChange(String channel, IRCUser user, String mode) {}
	
	private void joinStr(String chanstr) {
		try {
			con.send(IRCCommands.JOIN + " " + chanstr);
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isConnected() {
		return con.isConnected();
	}

	public void join(String channel) {
		this.joinStr(channel);
	}

	public void join(String[] channels) {
		String str = "";
		for (String s : channels)
			str += s + ",";
		this.joinStr(str.substring(0, str.lastIndexOf(',')));
	}
	
	public void kick(String channel, String nick) {
		this.kick(channel, nick, nick);
	}
	
	public void kick(String channel, String nick, String msg) {
		con.send(IRCCommands.KICK, channel + " " + nick + " " + msg);
	}
	
	public void sendMessage(String target, String msg) {
		con.send(IRCCommands.PRIVMSG, target + " :" + msg);
	}
	
	public void sendAction(String target, String action) {
		this.sendMessage(target, IRCCommands.CONTROL_CHARS.ACTION +
				IRCCommands.ACTION +
				" " + action + IRCCommands.CONTROL_CHARS.ACTION);
	}
	
	public void sendNotice(String target, String msg) {
		con.send(IRCCommands.NOTICE, target + " :" + msg);
	}
	
	public void identify(String password) {
		this.sendMessage("NickServ", "identify " + password);
	}
	
	public void changeNick(String newNick) {
		this.getUser().setNick(newNick);
		con.send(IRCCommands.NICK, newNick);
	}
	
	public void disconnect() {
		this.con.disconnect();
	}
	
	public void setMode(String channel, String mode) {
		this.setMode(channel, mode, "");
	}
	
	public void setMode(String channel, String mode, String arguments) {
		String arg = "";
		if (!arguments.equals(""))
			arg = " " + arguments;
		con.send(IRCCommands.MODE, channel + " " + mode + arg);
	}

	public void setChannels(ArrayList<IRCChannel> channels) {
		this.channels = channels;
	}
	
	public IRCChannel getChannelByName(String name) {
		for (IRCChannel c : this.getChannels()) {
			if (c.getName().equals(name))
				return c;
		}

		return null;
	}

	public ArrayList<IRCChannel> getChannels() {
		return channels;
	}
	
	public static String stripControlChars(String orig) {
		String n = orig;
		//regex nub
		n = n.replaceAll("[0-9],[0-9]", "");
		n = n.replaceAll("[0-9][0-9],[0-9]", "");
		n = n.replaceAll("[0-9][0-9],[0-9][0-9]", "");
		n = n.replaceAll("[0-9],[0-9][0-9]", "");
		n = n.replaceAll("\\d", "");
		n = n.replaceAll("\\p{Cntrl}", "");
		
		return n;
	}

}