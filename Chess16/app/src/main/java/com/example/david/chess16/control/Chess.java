package control;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * Main Class
 * 
 * @author David Parsons
 * @author Phil Plucinski
 */
public class Chess {

	/**
	 * Buffer read for obtaining input from user
	 */
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * Board for chess match
	 */
	public static Match m = null;

	/**
	 * Keeps track of any pending draws
	 */
	public static boolean pendingDraw = true;

	/**
	 * origin point of move
	 */
	public static Point origin = null;

	/**
	 * target point of move
	 */
	public static Point target = null;

	/**
	 * end of parsed move (holds "why?")
	 */
	public static String end = "";

	/**
	 * promotion if needed for current move
	 */
	public static char promotion = 'Q';

	private static Boolean undo = false;

	private static Boolean AI = false;

	/**
	 * Main
	 */
	public static void main(String args[]) {

		System.out.println("Welcome to Chess.");
		m = new Match();

		do {

			printPiecesFromDisplayBoard(m.getCurrentDisplayBoard());

			Board b = m.engineBoard;

			System.out.println(b);

			readMove();

			if (undo) {
				m.undo();
				undo = false;
				continue;
			} else if (AI) {
				m.makeAIMove();
				AI = false;
				continue;
			}

			while ((m.executeMove(origin, target, false, promotion)) == null) {
				System.out.println("Illegal move, try again");
				readMove();
			}

		} while (m.engineBoard.matchCanContinue());

		String input = "";
		m.setToZerothMove();

		while (!input.equals("5")) {
			System.out.println("1. First");
			System.out.println("2. Prev");
			System.out.println("3. Next");
			System.out.println("4. Last");
			System.out.println("5. Quit");
			try {
				input = reader.readLine();
			} catch (IOException e) {
				continue;
			}
			Move move = null;
			if (input.equals("1")) {
				m.setToZerothMove();
				printPiecesFromDisplayBoard(m.getCurrentDisplayBoard());
				System.out.println();
				continue;
			} else if (input.equals("2")) {
				move = m.getPrevMove();
			} else if (input.equals("3")) {
				move = m.getNextMove();
			} else if (input.equals("4")) {
				move = m.getLastMove();
			}

			if (move == null) {
				System.out.println("Null move");
				if (input.equals("2")) {
					printPiecesFromDisplayBoard(m.getCurrentDisplayBoard());
					System.out.println();
				}
			} else {
				System.out.println(move.toString());
				printPiecesFromDisplayBoard(m.getCurrentDisplayBoard());
				System.out.println();
			}

		}
	}

	private static void printPiecesFromDisplayBoard(String[][] board) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (!board[j][i].equals("")) {
					System.out.println(board[j][i] + " @ " + (char) ('a' + j) + (char) ('0' + i + 1));
				}
			}
		}
	}

	/**
	 * Consumes input from console and updates static data fields accordingly in
	 * an attempt to prepare for move execution
	 *
	 */
	private static void readMove() {
		String input = "";

		System.out.print((m.engineBoard.getTurn() == 'w' ? "White's move: " : "Black's move: ")); // change
		// tempTurn
		// to
		// b.getTurn()

		while (true) {
			try {
				input = reader.readLine();
				break;
			} catch (IOException e) {
			}
		}

		if (input.equalsIgnoreCase("undo")) {
			undo = true;
			return;
		} else if (input.equalsIgnoreCase("AI")) {
			AI = true;
			return;
		}

		if (input.equals("resign")) {
			System.out.println((m.engineBoard.getTurn() == 'w' ? "Black wins" : "White wins")); // change
			// tempTurn
			// to
			// b.getTurn()
			System.exit(0); // end match
		} else if (input.equals("draw") && pendingDraw) {
			System.out.println("Draw");
			System.exit(0); // draw was waiting and is now confirmed
		}

		String[] parts = input.split(" ");

		// (x,y)
		origin = new Point(parts[0].charAt(0) - 'a', parts[0].charAt(1) - '1');
		target = new Point(parts[1].charAt(0) - 'a', parts[1].charAt(1) - '1');

		promotion = 'Q';
		end = "";

		if (parts.length > 2) {
			end = parts[parts.length - 1];
			promotion = parts[2].charAt(0);
		}

		pendingDraw = end.equals("draw?");
	}
}
