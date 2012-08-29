package bots.JamBot;

import java.io.IOException;

import javaBot.IRCUser;
import javaBot.JavaBot;

public class CmdExecute {

	public static String[] COMMANDS = new String[] { "commands", "help", "forum", "autovoice", "itemid", "itemname",
		"online", "jamdocs", "version"};
	public static String[] COMMANDHELP = new String[] {
		"commands: Lists all available commands.",
		"help <command>: Provides help information about a command.",
		"forum: Displays the forum URL.",
		"autovoice <on, off>: OWNER ONLY. Sets autovoice on or off.",
		"itemid <name>: Retrieves the ID of the item with the given name.",
		"itemname <id>: Retrieves the name of the item with the given id.",
		"online <today, ever, list>: Retrieves either the most users on today, most users online ever with a date, or a list of recently active users.",
		"jamdocs <className>: Searches the JamBot javadoc for the specified class name and supplies a link if found.",
		"version: Retrieves the most recent JamBot client version number."
	};

	public static String HELP_SYNTAX = "Help Syntax: " + Constants.COMMAND_CHAR + "help <command>";

	public static String listCommands() {
		String list = "";
		for (int i = 0; i < COMMANDS.length; i++) {
			if (i == COMMANDS.length - 1)
				list += Constants.COMMAND_CHAR + COMMANDS[i];
			else
				list += Constants.COMMAND_CHAR + COMMANDS[i] + ", ";
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
					break;
				}
			}

			if (cmd.equals(""))
				return;

			String[] args = line.split(" ");

			CmdExecute.execute(cmd.toLowerCase(), args, line, bot, channel, user);
		}
	}

	private static boolean commandExists(String command) {
		for (String s : CmdExecute.COMMANDS) {
			if (command.equals(s))
				return true;
		}

		return false;
	}

	private static int getCommandHelpIndex(String command) {
		for (int i = 0; i < COMMANDS.length ; i++) {
			if (COMMANDS[i].equals(command))
				return i;
		}
		return -1;
	}

	private static void execute(String cmd, String[] args, String fullargs, JavaBot bot, String channel, IRCUser user) {

		if (cmd.equals("commands")) {
			for (String s : Constants.HELP) {
				bot.sendNotice(user.getNick(), s);
			}
			return;
		}

		if (cmd.equals("help")) {
			//for (String s : args)
			//	System.out.println(s);
			if (args.length < 1 || args[0].equals(""))
				bot.sendNotice(user.getNick(), CmdExecute.HELP_SYNTAX);
			else {
				if (CmdExecute.commandExists(args[0])) {
					bot.sendNotice(user.getNick(), Constants.COMMAND_CHAR + COMMANDHELP[CmdExecute.getCommandHelpIndex(args[0])]);
				} else {
					bot.sendNotice(user.getNick(), Constants.COMMAND_CHAR + args[0] + " is not a valid command.");
				}
			}
			return;	
		}

		if (cmd.equals("forum")) {
			bot.sendNotice(user.getNick(), "JamBot Forums: " + Constants.SITE_URL);
			return;
		}

		if (cmd.equals("autovoice")) {
			if (user.getMask().equals(Constants.ADMIN_HOST)) {
				String state = args[0];
				((JamBot)bot).setAutoVoice(channel, state.equalsIgnoreCase("on"));
			}
			return;
		}

		if (cmd.equals("itemid")) {
			int id = -1;
			System.out.println(fullargs);
			try {
				id = ((JamBot)bot).itemsByName.get(fullargs.toLowerCase());
			} catch (NullPointerException e) {}
			if (id > -1)
				bot.sendNotice(user.getNick(), "ID for " + fullargs + ": " + id);
			else
				bot.sendNotice(user.getNick(), "No item with that name found.");
			return;
		}

		if (cmd.equals("itemname")) {
			String name = "";
			try {
				name = ((JamBot)bot).itemsById.get(Integer.parseInt(args[0]));
			} catch (NullPointerException e) {
				name = "";
			} catch (NumberFormatException e) {
				name = "";
			}
			if (!(name == null) && !name.equals(""))
				bot.sendNotice(user.getNick(), "Name for " + args[0] + ": " + name);
			else
				bot.sendNotice(user.getNick(), "No item with that ID found.");
			return;
		}
		
		if (cmd.equals("online")) {
			if (fullargs.equalsIgnoreCase("today"))
				bot.sendNotice(user.getNick(), "Most users on the forums today: " + OnlineLookup.getMostToday());
			if (fullargs.equalsIgnoreCase("ever"))
				bot.sendNotice(user.getNick(), "Most users on the forums ever: " + OnlineLookup.getMostEver());
			if (fullargs.equalsIgnoreCase("list"))
				bot.sendNotice(user.getNick(), "Users Online: " + OnlineLookup.getActiveList());
			
			return;
		}
		
		if (cmd.equals("jamdocs")) {
			String[] byline = null;
			try {
				byline = FileIO.getPageSource(Constants.CLASS_LIST).split("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (byline == null) {
				bot.sendNotice(channel, "[javadoc]: Error reading from javadoc.");
				return;
			}
			
			for (String s : byline) {
				if (s.toLowerCase().contains(">" + fullargs.toLowerCase() + "<")) {
					String url = "http://www.jambot.net/api/jdoc/" + s.split("<A HREF=\"")[1].split("\" ")[0];
					bot.sendNotice(channel, "[javadoc]: " + url);
					return;
				}
			}
			bot.sendNotice(channel, "[javadoc]: Could not find class '" + fullargs + "'.");
			
			return;
		}
		
		if (cmd.equals("version")) {
			try {
				String v = FileIO.getPageSource("http://www.jambot.net/version.php");
				bot.sendNotice(user.getNick(), "[version]: Current version is " + v);
			} catch (IOException e) {
				bot.sendNotice(user.getNick(), "[version]: Error retrieving version number.");
			}
			return;
		}


	}

}
