package control;

import java.util.ArrayList;
import java.util.Date;

public class Engine {

	private static Match currentMatch;
	private static ArrayList<WatchableMatch> savedMatches = new ArrayList<WatchableMatch>();

	public static void saveMatch() {
		savedMatches.add(currentMatch.getWatchableMatch());
	}

	public static Match startNewMatch(String title) {
		Match m = new Match();
		m.setTitle(title);
		currentMatch = m;
		return m;
	}

	public static ArrayList<String> getSortedTitles() {
		savedMatches.sort((o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle()));
		ArrayList<String> titles = new ArrayList<String>();
		for (WatchableMatch m : savedMatches) {
			titles.add(m.getTitle());
		}
		return titles;
	}

	public static ArrayList<Date> getSortedDates() {
		savedMatches.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
		ArrayList<Date> dates = new ArrayList<Date>();
		for (WatchableMatch m : savedMatches) {
			dates.add(m.getDate());
		}
		return dates;
	}

	public static WatchableMatch getMatchByIndex(int index) {
		return savedMatches.get(index);
	}

}
