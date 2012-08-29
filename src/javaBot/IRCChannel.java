package javaBot;

import java.util.ArrayList;

public class IRCChannel {

	private String name;
	private ArrayList<ChannelUser> users;

	public IRCChannel(String name, ArrayList<ChannelUser> users) {
		this.setName(name);
		this.setUsers(users);
	}

	public static int prefixToInt(String prefix) {
		if (prefix.equals(IRCCommands.RANK_PREFIXES.VOICE))
			return 4;
		if (prefix.equals(IRCCommands.RANK_PREFIXES.HALF_OP))
			return 3;
		if (prefix.equals(IRCCommands.RANK_PREFIXES.OPERATOR))
			return 2;
		if (prefix.equals(IRCCommands.RANK_PREFIXES.SERVICE))
			return 1;
		if (prefix.equals(IRCCommands.RANK_PREFIXES.OWNER))
			return 0;

		return 5;
	}

	public ChannelUser[] getUsersByRank() {
		ChannelUser[] u = new ChannelUser[users.size()];
		int storeindex = 0;
		while (storeindex < u.length) {
			for (int curlvl = 0; curlvl < 6; curlvl++) {
				for (int i = 0; i < u.length; i++) {
					int rank = IRCChannel.prefixToInt(this.users.get(i).getPrefix());
					if (rank == curlvl) {
						u[storeindex] = this.getUsers().get(i);
						storeindex++;
					}
				}
			}
		}

		return u;
	}
	
	public ChannelUser getUserByNick(String nick) {
		for (ChannelUser u : this.users) {
			if (u.getNick().equals(nick))
				return u;
		}
		return null;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUsers(ArrayList<ChannelUser> users) {
		this.users = users;
	}

	public ArrayList<ChannelUser> getUsers() {
		return users;
	}

}
