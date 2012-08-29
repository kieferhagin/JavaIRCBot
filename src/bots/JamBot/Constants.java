package bots.JamBot;

import bots.JamBot.CmdExecute;

public class Constants {
	public static String[] CHANNELS = new String[] { "#jambot", "#jambot.chat" };
	public static String SERVER = "irc.strictfp.com";
	public static String PASSWORD = "0614216aaa";
	public static String ADMIN_HOST = "JamBot.net";
	
	public static String COMMAND_CHAR = "@";

	public static String[] ON_JOIN_NOTICE;
	public static String[] HELP;
	
	public static String SITE_URL = "http://www.jambot.net/";
	public static String CLASS_LIST = SITE_URL + "api/jdoc/allclasses-frame.html";

	static {

		ON_JOIN_NOTICE = new String[] {
				"Welcome to JamBot! Please visit our forums at " + SITE_URL,
				"Commands: " + CmdExecute.listCommands() };
		
		HELP = new String[] {
				"Hello, I am JamBot and these are my commands:",
				CmdExecute.listCommands()
		};
	}

}
