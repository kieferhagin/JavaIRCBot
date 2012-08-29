package bots.DracoBot;

public class Constants {
	public static String[] CHANNELS = new String[] { "#dracomancer-rpg" };
	public static String SERVER = "irc.strictfp.com";
	public static String PASSWORD = "0614216aaa";
	public static String ADMIN_HOST = "g.i.n";
	
	public static String COMMAND_CHAR = "!";

	public static String[] ON_JOIN_NOTICE;
	public static String[] HELP;
	
	public static String BLOG_URL = "http://khagin.tumblr.com/";

	static {

		ON_JOIN_NOTICE = new String[] {
				"Dracomancer is currently in development. Check out khagin's dev blog at " + BLOG_URL,
				"Commands: " + CmdExecute.listCommands() };
		
		HELP = new String[] {
				"Hello, I am Dracomancer and these are my commands:",
				CmdExecute.listCommands()
		};
	}

}
