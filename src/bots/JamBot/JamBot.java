package bots.JamBot;

import java.util.HashMap;

import javaBot.IRCUser;
import javaBot.JavaBot;

public class JamBot extends JavaBot {

	public HashMap<Integer, String> itemsById;
	public HashMap<String, Integer> itemsByName;

	public HashMap<String, Boolean> autoVoice;

	public JamBot() {
		super(new IRCUser("J", "JamBot"));
		loadItems();
		autoVoice = new HashMap<String, Boolean>();
		this.connect("irc.strictfp.com");
	}

	private void loadItems() {
		System.out.print("Loading items...");
		String[] byline = FileIO.getFile("c:\\users\\kiefer\\documents\\ITEM.dat");
		itemsById = new HashMap<Integer, String>();
		itemsByName = new HashMap<String, Integer>();
		for (String s : byline) {			
			int id = Integer.parseInt(s.split(":")[0]);
			String name = s.split(":")[1].toLowerCase();
			itemsById.put(id, name);
			itemsByName.put(name, id);
		}
		System.out.println("done.");
	}


	@Override
	protected void onConnect() {
		//put password here
		this.identify("");
		this.join(Constants.CHANNELS);
	}

	@Override
	protected void onJoin(String channel, IRCUser user) {

		boolean hasaccess = false;
		for (String s : Constants.CHANNELS) {
			if (s.equalsIgnoreCase(channel)) {
				hasaccess = true;
				break;
			}
		}

		if (hasaccess) {
			if (!user.getNick().equals(this.getUser().getNick())) {
				if (this.autoVoice.get(channel) != null && this.autoVoice.get(channel).booleanValue()) {
					if (!user.getNick().startsWith("JamBotIRC"))
						this.setMode(channel, "+v", user.getNick());
				}
			} else {
				this.autoVoice.put(channel, true);
			}
		}
	}

	@Override
	protected void onMessage(String channel, IRCUser user, String msg) {
		CmdExecute.execute(msg, this, channel, user);
	}

	public void setAutoVoice(String channel, boolean b) {
		String say = b ? "ON" : "OFF";
		
		this.autoVoice.put(channel, b);

		this.sendMessage(channel, "AutoVoice is now " + say);
	}

	public static void main(String[] args) {
		new JamBot();
	}

}
