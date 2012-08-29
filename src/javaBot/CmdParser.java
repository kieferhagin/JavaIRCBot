package javaBot;

import java.util.ArrayList;

import exceptions.NickInUseException;
import exceptions.NotConnectedException;

public class CmdParser {

	public static void parse(String line, JavaBotConnection con) throws NotConnectedException {
		if (line.startsWith(IRCCommands.PING)) {
			con.send(IRCCommands.PONG + " " + toArgs(line)[1]);
			return;
		}

		if (line.startsWith(":")) {
			line = line.substring(1);
			String u = line.split(" ")[0];
			String d = line.split(" ", 2)[1];
			if (u.contains("!") && u.contains("@")) {
				parseUser(IRCUser.stringToUser(u), d.split(" ")[0], d.split(" ", 2)[1], con);
			} else {
				try {
					parseServer(u, Integer.parseInt(toArgs(line)[1]), line.split(" ", 3)[2], con);
				} catch (NumberFormatException e) {}
			}
		}
	}

	private static void parseServer(String server, int cmd, String data, JavaBotConnection con) {
		System.out.println(server + " (" + cmd + "): " + data);
		switch (cmd) {
		case IRCCommands.CONNECTED_RESPONSE:
			con.getBot().onConnect();
			break;
		case IRCCommands.NICK_IN_USE_RESPONSE:
			try {
				con.getBot().onNickInUse();
				throw new NickInUseException();
			} catch (NickInUseException e) {
				e.printStackTrace();
			}
			break;
		case IRCCommands.CHANNEL_USERS_RESPONSE:
			String[] args = data.split(" ", 4);
			ArrayList<ChannelUser> users = new ArrayList<ChannelUser>();
			System.out.println(args[3]);
			String[] u = args[3].substring(1).split(" ");

			for (String s : u) {
				if (Character.getType(s.charAt(0)) != Character.LETTER_NUMBER && !s.substring(0, 1).equals(":"))
					users.add(new ChannelUser(s.substring(1), s.substring(0, 1)));
				else
					users.add(new ChannelUser(s, ""));
			}

			IRCChannel detectold = con.getBot().getChannelByName(args[2]);
			con.getBot().getChannels().add(new IRCChannel(args[2], users));
			if (!(detectold == null)) {
				con.getBot().getChannels().remove(detectold);
			}
			con.getBot().onChannelUpdate(con.getBot().getChannelByName(args[2]));
		}
		con.getBot().onServerMessage(server, cmd, data);
	}

	private static void parseUser(IRCUser user, String cmd, String data, JavaBotConnection con) {
		boolean chan = false;
		if (data.startsWith(con.getBot().getUser().getNick())) {
			data = data.split(" :", 2)[1];
		} else {
			chan = true;
		}

		if (chan) {
			String channel = data.split(" ")[0];
			if (channel.startsWith(":"))
				channel = channel.substring(1);
			if (cmd.equals(IRCCommands.PRIVMSG)) {
				String msg = data.split(":", 2)[1];
				if (msg.toCharArray()[0] == IRCCommands.CONTROL_CHARS.ACTION 
						&& msg.substring(1).startsWith(IRCCommands.ACTION))
					con.getBot().onAction(channel, user, JavaBot.stripControlChars(msg.split(
							IRCCommands.ACTION + " ", 2)[1]));
				else
					con.getBot().onMessage(channel, user, msg);
			} else if (cmd.equals(IRCCommands.JOIN)) {
				CmdParser.addUserToChannel(con.getBot(), user.getNick(), channel);
				con.getBot().onJoin(channel, user);
			} else if (cmd.equals(IRCCommands.QUIT)) {
				CmdParser.removeUserFromChans(con.getBot(), user.getNick());
				con.getBot().onQuit(user, data);
			} else if (cmd.equals(IRCCommands.PART))  {
				CmdParser.removeUserFromChannel(con.getBot(), user.getNick(), data);
				con.getBot().onPart(user, data);
			} else if (cmd.equals(IRCCommands.KICK)) {
				String kicked = data.split(" ")[1];
				CmdParser.removeUserFromChannel(con.getBot(), kicked, channel);
				con.getBot().onKick(user, kicked, channel, "");
			} else if (cmd.equals(IRCCommands.NICK)) {
				String newnick = data;
				if (newnick.startsWith(":"))
					newnick = newnick.substring(1);
				CmdParser.changeUserNick(con.getBot(), user.getNick(), newnick);
				con.getBot().onNickChange(user, newnick);
			} else if (cmd.equals(IRCCommands.MODE)) {
				CmdParser.modeChange(con, channel, data.split(" ")[1]);
				con.getBot().onModeChange(channel, user, data);
			} else if (cmd.equals(IRCCommands.TOPIC)) {
				con.getBot().onTopicChange(channel, user, data.split(" :", 2)[1]);
			}
		} else {
			if (cmd.equals(IRCCommands.PRIVMSG)) {
				con.getBot().onPrivMessage(user, data);
			}

		}

		System.out.println(user.getNick() + " (" + cmd + "): " + data);
	}

	private static void removeUserFromChans(JavaBot bot, String user) {
		IRCChannel[] chans = bot.getChannels().toArray(new IRCChannel[0]);
		for (IRCChannel c : chans) {
			for (int i = 0; i < c.getUsers().size(); i++) {
				if (c.getUsers().get(i).getNick().equals(user)) {
					c.getUsers().remove(i);
					bot.onChannelUpdate(c);
					break;
				}
			}
		}
	}

	private static void removeUserFromChannel(JavaBot bot, String user, String channel) {
		for (IRCChannel c : bot.getChannels()) {
			if (c.getName().equals(channel)) {
				c.getUsers().remove(user);
				bot.onChannelUpdate(c);
				return;
			}
		}
	}

	private static void addUserToChannel(JavaBot bot, String user, String channel) {
		for (IRCChannel c : bot.getChannels()) {
			if (c.getName().equals(channel)) {
				c.getUsers().add(new ChannelUser(user, ""));
				bot.onChannelUpdate(c);
				return;
			}
		}
	}

	private static void changeUserNick(JavaBot bot, String oldnick, String newnick) {
		for (IRCChannel c : bot.getChannels()) {
			for (int i = 0; i < c.getUsers().size(); i++) {
				if (c.getUsers().get(i).getNick().equals(oldnick)) {
					c.getUsers().get(i).setNick(newnick);
					
					bot.onChannelUpdate(c);
				}
			}
		}
	}

	private static void modeChange(JavaBotConnection con, String channel, String mode) {
		if (mode.contains(IRCCommands.USER_MODES.OWNER)
				|| mode.contains(IRCCommands.USER_MODES.SERVICE)
				|| mode.contains(IRCCommands.USER_MODES.OP)
				|| mode.contains(IRCCommands.USER_MODES.HALFOP)
				|| mode.contains(IRCCommands.USER_MODES.VOICE)) {
			con.send(IRCCommands.NAMES, channel);
		}
	}

	private static String[] toArgs(String line) {
		return line.split(" ");
	}

}
