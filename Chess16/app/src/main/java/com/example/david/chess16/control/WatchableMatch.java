package com.example.david.chess16.control;
import com.example.david.chess16.pieces.*;

import java.util.ArrayList;
import java.util.Date;

public class WatchableMatch {

	private String[][] displayBoard = new String[8][8];
	private ArrayList<Move> moves = new ArrayList<Move>();
	int currentMoveIndex = 0;
	String title;
	String endMessage;
	Date date;

	WatchableMatch(String title, ArrayList<Move> moves, String endMessage, Date date) {
		this.title = title;
		this.moves = moves;
		this.endMessage = endMessage;
		this.date = date;
	}

	/**
	 * Sets the display board to the initial setup, to reflect move 'zero'.
	 */
	public void setToZerothMove() {
		populateDisplayBoard();
		currentMoveIndex = 0;
	}

	public String[][] start() {
		currentMoveIndex = 0;
		return moves.get(0).displayBoard;
	}

	/**
	 * Returns the next Move in the match and sets the displayBoard to the
	 * displayBoard of that move.
	 * 
	 * @return Next Move
	 */
	public String[][] getNextMove() {
		if (currentMoveIndex > moves.size() - 1) {
			return null;
		}
		if (currentMoveIndex == moves.size() - 1) {
			displayBoard = moves.get(currentMoveIndex).displayBoard;
			return displayBoard;
		}
		displayBoard = moves.get(++currentMoveIndex).displayBoard;
		return displayBoard;
	}

	/**
	 * Returns the previous Move in the match and sets the displayBoard to the
	 * displayBoard of that move.
	 * 
	 * @return Previous Move
	 */
	public String[][] getPrevMove() {
		if (currentMoveIndex < 0) {
			return null;
		}
		if (currentMoveIndex == 0) {
			displayBoard = moves.get(currentMoveIndex).displayBoard;
			return displayBoard;
		}
		displayBoard = moves.get(--currentMoveIndex).displayBoard;
		return displayBoard;
	}

	public int getTotalNumberOfMoves() {
		return moves.size();
	}

	public int getCurrentMove() {
		return currentMoveIndex;
	}

	/**
	 * Returns the last Move of the match and sets the displayBoard to that of
	 * the move.
	 * 
	 * @return Last Move
	 */
	public String[][] getLastMove() {
		if (moves.isEmpty()) {
			return null;
		}
		displayBoard = moves.get(moves.size() - 1).displayBoard;
		currentMoveIndex = moves.size() - 1;
		return displayBoard;
	}

	public String getEndMessage() {
		return endMessage;
	}

	public String getTitle() {
		return title;
	}

	public Date getDate() {
		return date;
	}

	/**
	 * Sets the displayBoard to the initial setup of a chess board.
	 */
	private void populateDisplayBoard() {

		displayBoard = new String[8][8];

		displayBoard[0][7] = "br";
		displayBoard[1][7] = "bn";
		displayBoard[2][7] = "bb";
		displayBoard[3][7] = "bq";
		displayBoard[4][7] = "bk";
		displayBoard[5][7] = "bb";
		displayBoard[6][7] = "bn";
		displayBoard[7][7] = "br";

		for (int i = 0; i < 8; i++) {
			displayBoard[i][6] = "bp";
			displayBoard[i][1] = "wp";
		}

		displayBoard[0][0] = "wr";
		displayBoard[1][0] = "wn";
		displayBoard[2][0] = "wb";
		displayBoard[3][0] = "wq";
		displayBoard[4][0] = "wk";
		displayBoard[5][0] = "wb";
		displayBoard[6][0] = "wn";
		displayBoard[7][0] = "wr";

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (displayBoard[i][j] == null) {
					displayBoard[i][j] = "";
				}
			}
		}
	}
}
