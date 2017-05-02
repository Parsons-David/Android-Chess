package com.example.david.chess16.control;
import com.example.david.chess16.pieces.*;

import java.util.ArrayList;

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
		return getTitles();
	}

	public static ArrayList<String> getSortedDates() {
		savedMatches.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
		return getTitles();
	}

	private static ArrayList<String> getTitles() {
		ArrayList<String> titles = new ArrayList<String>();
		for (WatchableMatch m : savedMatches) {
			titles.add(m.getTitle());
		}
		return titles;
	}

	public static WatchableMatch getMatchByIndex(int index) {
		if (savedMatches.isEmpty()) {
			return null;
		}
		return savedMatches.get(index);
	}

}
