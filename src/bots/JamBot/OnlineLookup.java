package bots.JamBot;

import java.io.IOException;

public class OnlineLookup {

	private static String MOST_TODAY_STR = "Most Online Today: <b>";
	private static String MOST_EVER_STR = "Most Online Ever: ";
	private static String ACTIVE_LIST_STR = "Users active in past 15 minutes:<br /><a href=";

	public static String getMostToday() {

		for (String s : getSauceByLine()) {
			if (s.contains(MOST_TODAY_STR))
				return s.split(MOST_TODAY_STR, 2)[1].split("</b>")[0];
		}

		return "";

	}

	public static String getMostEver() {

		for (String s : getSauceByLine()) {
			if (s.contains(MOST_EVER_STR))
				return s.split(MOST_EVER_STR, 2)[1];
		}

		return "";

	}
	
	public static String getActiveList() {
		try {
		for (String s : getSauceByLine()) {
			if (s.contains(ACTIVE_LIST_STR)) {
				String l = s.split(ACTIVE_LIST_STR)[1];
				String u[] = l.split(",");
				
				String ret = "";
				for (int i = 0; i < u.length; i++) {
					if (i != u.length - 1)
						ret += u[i].split("</a")[0].split(">")[1] + " | ";
					else
						ret += u[i].split("</a")[0].split(">")[1];
				}
				
				return ret;
			}
		}

		return "";
		} catch (Exception e) {
			return "None";
		}
	}

	private static String[] getSauceByLine() {
		String sauce = "";
		try {
			sauce = FileIO.getPageSource(Constants.SITE_URL);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sauce.split("\n");

	}
	
	public static void main(String[] args) {
		System.out.println(OnlineLookup.getActiveList());
	}

}
