package javaBot;

import java.io.IOException;
import java.net.UnknownHostException;

import exceptions.NotConnectedException;

public class JavaBotConnection extends IRCConnection {

	private JavaBot bot;

	public JavaBotConnection(JavaBot bot, String host, int port) {
		super(host, port);
		this.bot = bot;
	}

	@Override
	public void parse(String line) throws NotConnectedException {
		super.parse(line);
		CmdParser.parse(line, this);
	}

	@Override
	protected void connect() throws UnknownHostException, IOException {
		super.connect();
		this.sendUserData(bot.getUser());
	}
	
	@Override
	protected void disconnect() {
		this.getBot().onDisconnect();
		this.send(IRCCommands.QUIT, "");
		super.disconnect();
	}

	private void sendUserData(IRCUser user) {
		String uline = "USER " + user.getUser() + " * * :" + user.getUser();
		String nline = "NICK " + user.getNick();
		try {
			this.send(uline);
			this.send(nline);
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}
	
	public JavaBot getBot() {
		return this.bot;
	}
	
	public void send(String command, String data) {
		try {
			super.send(command + " " + data);
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
	}

}
