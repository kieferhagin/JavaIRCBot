package bots.DracoBot;

import java.util.ArrayList;

import javaBot.IRCUser;
import javaBot.JavaBot;

public class CmdExecute {

	public static String[] COMMANDS = new String[] { "commands", "blog", "botname" };

	public static String listCommands() {
		String list = "";
		for (int i = 0; i < COMMANDS.length; i++) {
			if (i == COMMANDS.length - 1)
				list += "!" + COMMANDS[i];
			else
				list += "!" + COMMANDS[i] + ", ";
		}

		return list;
	}

	public static void execute(String line, JavaBot bot, String channel, IRCUser user) {
		if (line.toLowerCase().startsWith(Constants.COMMAND_CHAR)) {
			line = line.substring(1);
			String cmd = "";
			for (String s : COMMANDS) {
				if (line.startsWith(s)) {
					cmd = s;
					line = line.substring(cmd.length()).trim();
				}
			}

			if (cmd.equals(""))
				return;

			String[] args = line.split(" ");

			CmdExecute.execute(cmd.toLowerCase(), args, bot, channel, user);
		}
	}

	private static void execute(String cmd, String[] args, JavaBot bot, String channel, IRCUser user) {

		if (cmd.equals("commands")) {
			for (String s : Constants.HELP) {
				bot.sendNotice(user.getNick(), s);
			}
			return;
		}

		if (cmd.equals("blog")) {
			bot.sendNotice(user.getNick(), "khagin's Dev Blog: " + Constants.BLOG_URL);
		}

		if (cmd.equals("botname")) {
			if (user.getMask().equals(Constants.ADMIN_HOST)) {
				if (args[0].equals("remove")) {
					try {
						ArrayList<ArrayList<String>> removal = new ArrayList<ArrayList<String>>();
						for (ArrayList<String> list : DracoBot.botNames.values()) {
							for (String s : list) {
								if (s.equals(args[1])) {
									removal.add(list);
									break;
								}
							}
						}
						for (ArrayList<String> list : removal) {
							list.remove(args[1]);
						}
						
						bot.sendMessage(channel, "Removed " + args[1] + " from bot name list.");
						DracoBot.writeBotNames();
					} catch (Exception e) {
						bot.sendMessage(channel, "Error removing bot name entry.");
						e.printStackTrace();
					}
				}
				if (args[0].equals("clear")) {
					try {
						DracoBot.botNames.clear();
						bot.sendMessage(channel, "Cleared bot names.");
						DracoBot.writeBotNames();
					} catch (Exception e) {
						bot.sendMessage(channel, "Error clearing bot names.");
						e.printStackTrace();
					}
				}
			}
			if (args[0].equals("add")) {
				try {
					if (DracoBot.botNames.containsKey(user.getNick())) {
						if (!DracoBot.botNames.get(user.getNick()).contains(args[1]))  {
							DracoBot.botNames.get(user.getNick()).add(args[1]);
							bot.sendMessage(channel, "Added " + args[1] + " to bot name list.");
							DracoBot.writeBotNames();
						} else {
							bot.sendMessage(channel, args[1] + " is already on the list.");
						}
					} else {
						DracoBot.botNames.put(user.getNick(), new ArrayList<String>());
						DracoBot.botNames.get(user.getNick()).add(args[1]);
						bot.sendMessage(channel, "Added " + args[1] + " to bot name list.");
						DracoBot.writeBotNames();
					}
				} catch (Exception e) {
					bot.sendMessage(channel, "Error adding bot name.");
					e.printStackTrace();
				}
			}

			if (args[0].equals("list")) {
				if (DracoBot.botNames.isEmpty()) {
					bot.sendNotice(user.getNick(), "List is empty.");
					return;
				}
				String ret = "";
				for (String s : DracoBot.botNames.keySet()) {
					for (String l : DracoBot.botNames.get(s)) {
						ret += "(" + s + ") " + l + ", ";
					}
				}
				ret = ret.substring(0, ret.trim().length() - 1);
				bot.sendNotice(user.getNick(), ret);
			}
		}
	}

}
