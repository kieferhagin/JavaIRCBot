package bots.DracoBot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javaBot.IRCUser;
import javaBot.JavaBot;

@SuppressWarnings("unchecked")
public class DracoBot extends JavaBot {
	
	public static HashMap<String, ArrayList<String>> botNames;
	
	static {
		File f = new File("botnames.list");
		if (f.exists())
			botNames = (HashMap<String, ArrayList<String>>) FileIO.loadObject("botnames.list");
		else {
			botNames = new HashMap<String, ArrayList<String>>();
			FileIO.writeObject("botnames.list", botNames);
		}
			
	}

	public DracoBot() {
		super(new IRCUser("Dracom", "Dracomancer"));
		this.connect("cafe.strictfp.com", 6667);
	}
	
	@Override
	protected void onConnect() {
		this.identify("");
		this.join("#dracomancer-rpg"); 
	}
	
	@Override
	protected void onMessage(String channel, IRCUser user, String msg) {
		if (channel.equalsIgnoreCase("#dracomancer-rpg")) {
			CmdExecute.execute(msg, this, channel, user);
		}
	}
	
	@Override
	protected void onJoin(String channel, IRCUser user) {
		if (channel.equalsIgnoreCase("#dracomancer-rpg") &&
				!user.getNick().equalsIgnoreCase(this.getUser().getNick())) {
			this.setMode(channel, "+v", user.getNick());
			for (String s : Constants.ON_JOIN_NOTICE)
				this.sendNotice(user.getNick(), s);
		}
	}
	
	public static void main(String[] args) {
		new DracoBot();
	}
	
	public static void writeBotNames() {
		File f = new File("botnames.list");
		if (f.exists())
			f.delete();
		FileIO.writeObject("botnames.list", DracoBot.botNames);
	}

}
