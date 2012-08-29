package javaBot;

public class IRCCommands {
	
	public final static String JOIN = "JOIN";
	public final static String KICK = "KICK";
	public final static String MODE = "MODE";
	public final static String NAMES = "NAMES";
	public final static String NICK = "NICK";
	public final static String NOTICE = "NOTICE";
	public final static String PART = "PART";
	public final static String PING = "PING";
	public final static String PONG = "PONG";
	public final static String PRIVMSG = "PRIVMSG";
	public final static String USERHOST = "USERHOST";
	public final static String QUIT = "QUIT";
	public final static String TOPIC = "TOPIC";
	
	public final static String ACTION = "ACTION";
	
	public final static int CONNECTED_RESPONSE = 001;
	public final static int USERHOST_RESPONSE = 302;
	public final static int CHANNEL_TOPIC = 332;
	public final static int CHANNEL_USERS_RESPONSE = 353;
	public final static int NICK_IN_USE_RESPONSE = 433;
	public final static int CANNOT_JOIN_BANNED = 474;
	
	public static class RANK_PREFIXES {
		public final static String OWNER = "~", SERVICE = "&", OPERATOR = "@", HALF_OP = "%", VOICE = "+";
	}
	
	public static class USER_MODES {
		public final static String OP = "o";
		public final static String HALFOP = "h";
		public final static String VOICE = "v";
		public final static String SERVICE = "a";
		public final static String OWNER = "q";
		public final static String BAN = "b";
	}
	
	public static class CONTROL_CHARS {
		public final static char COLOR = '', BOLD = '', ACTION = '';
	}
}
