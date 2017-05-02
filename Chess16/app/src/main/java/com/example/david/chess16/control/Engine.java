package com.example.david.chess16.control;
import android.content.Context;

import com.example.david.chess16.pieces.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Engine {

	private static Match currentMatch;
	static ArrayList<WatchableMatch> savedMatches = new ArrayList<WatchableMatch>();

	/**
	 * Local Directory where the serialized object state is stored.
	 * 
	 */
	public static final String storeDir = "dat";

	/**
	 * File name of where the serialized object state is stored.
	 *
	 */
	public static final String storeFile = "engine.dat";

	/**
	 * Serializes the current state of the static arraylist of watchableMatches.
	 * 
	 * @throws IOException
	 *             when serialization fails when file is not found
	 */
	public static void saveState(Context c) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(c.openFileOutput(storeFile, Context.MODE_PRIVATE));
		oos.writeObject(savedMatches);
	}

	/**
	 * De-serializes the stored state of the static arraylist of
	 * watchableMatches.
	 * 
	 * @throws IOException
	 *             when serialization fails when file is not found
	 * @throws ClassNotFoundException
	 *             when classes are out of sync
	 */
	public static void load(Context c) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(c.openFileInput(storeFile));
		savedMatches = (ArrayList) ois.readObject();
	}

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
