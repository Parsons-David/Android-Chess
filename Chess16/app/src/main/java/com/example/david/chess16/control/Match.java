package com.example.david.chess16.control;
import com.example.david.chess16.pieces.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;


/**
 * This class is the representation of a chess match. A caller can execute
 * moves, make AI moves, undo moves, and get a String[][] representation of the
 * board at any moment. After the match is concluded, the user can iterate
 * through match states to be able to display the match, move by move.
 * 
 * @author Phil Plucinski
 *
 */
public class Match {
	Board engineBoard;
	private String[][] displayBoard = new String[8][8];
	private ArrayList<Move> moves = new ArrayList<Move>();
	private String title;
	private int currentMoveIndex = 0;
	private String endStatus;

	/**
	 * No-arg constructor that simply initializes various variables in the
	 * class.
	 */
	public Match() {
		engineBoard = new Board();
		displayBoard = populateDisplayBoard();
		moves.add(new Move(populateDisplayBoard()));
	}

	public void endMatch() {
		Engine.saveMatch();
	}

	/**
	 * Performs a move randomly selected from the set of valid moves for the
	 * current player.
	 * 
	 * @return Move representation of the performed move
	 */
	public Move makeAIMove(boolean draw) {
		char turn = engineBoard.getTurn();
		Move ogMove = moves.get(currentMoveIndex);
		Move move = null;
		do {
			ArrayList<Piece> pieces = (turn == 'w') ? engineBoard.whitePieces : engineBoard.blackPieces;
			int pieceNo = new Random().nextInt(pieces.size());
			ArrayList<Point> moves = engineBoard.getValidMoves(pieces.get(pieceNo));
			Collections.shuffle(moves);
			for (Point point : moves) {
				Point pOrigin = pieces.get(pieceNo).location;
				String origin = String.valueOf(pOrigin.getY() + 1) + String.valueOf((char) ('a' + pOrigin.getX()));
				String target = String.valueOf(point.getY() + 1) + String.valueOf((char) ('a' + point.getX()));
				move = executeMove(origin, target, draw, 'Q');
				if (move != null && move != ogMove) {
					break;
				}
			}
			// Fix this.
		} while (move == null || move == ogMove);

		return move;
	}

	/**
	 * Reverses the last move in both the engine and display boards.
	 * 
	 * @return true if successful, false otherwise
	 */
	public Move undo() {
		if (currentMoveIndex == 0) {
			return moves.get(currentMoveIndex);
		}
		engineBoard.undo();
		displayBoard = moves.get(--currentMoveIndex).displayBoard;
		moves.remove(moves.size() - 1);
		return moves.get(currentMoveIndex);
	}

	/**
	 * Calls the executeMove within Board and updates the display board within
	 * Match.
	 * 
	 * @param origin
	 *            Point containing the piece to be moved.
	 * @param target
	 *            Destination point
	 * @param promotion
	 *            char indicating the desired promotion, if applicable
	 * @return
	 */
	public Move executeMove(String o, String t, boolean draw, char promotion) {
		Point origin = getPointFromString(o);
		Point target = getPointFromString(t);
		Move move = engineBoard.executeMove(engineBoard.board, engineBoard.whitePieces, engineBoard.blackPieces, origin,
				target, promotion);

		if (move == null) {
			return moves.get(currentMoveIndex);
		}

		engineBoard.toggleTurn();

		if (engineBoard.inCheck(engineBoard.getTurn(), engineBoard.board, engineBoard.whitePieces,
				engineBoard.blackPieces)) {
			move.check = engineBoard.getTurn();
		}

		move.updateDisplayBoard(displayBoard);
		displayBoard = move.displayBoard;
		moves.add(move);
		currentMoveIndex++;

		System.out.println(move.toString());

		move.pendingDraw = draw;

		if (!isOngoing()) {
			if (engineBoard.inStalemate()) {
				endStatus = "Stalemate on " + ((engineBoard.getTurn() == 'w') ? "white" : "black") + "to move.";
			} else if (engineBoard.checkMate()) {
				endStatus = "Checkmate. " + ((engineBoard.getTurn() == 'w') ? "Black" : "White") + " wins!";
			}
		}

		return move;
	}

	public String resignation() {
		endStatus = (engineBoard.getTurn() == 'w' ? "White" : "Black") + " resigns.";
		return endStatus;
	}

	public String acceptDraw() {
		endStatus = "Match is a draw";
		return endStatus;
	}

	/**
	 * Returns the display board based on its current state. While ongoing, the
	 * board represents the currennt turn. When the match is not ongoing, the
	 * display board corresponds to that of the move that is currently being
	 * examined.
	 * 
	 * @return String[][] representation of the board in the form of
	 *         board[col][row]. ie. board [a][4] == "wq"
	 */
	public String[][] getCurrentDisplayBoard() {
		return displayBoard;
	}

	public String getEndMessage() {
		return endStatus;
	}

	/**
	 * @return Title of the Match
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the Title of the Match
	 * 
	 * @param title
	 *            title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean isOngoing() {
		return engineBoard.matchCanContinue();
	}

	public WatchableMatch getWatchableMatch() {
		return new WatchableMatch(title, moves, endStatus, new Date());
	}

	/**
	 * Sets the displayBoard to the initial setup of a chess board.
	 */
	private String[][] populateDisplayBoard() {

		String[][] displayBoard;

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

		return displayBoard;
	}

	private Point getPointFromString(String p) {
		p = p.toLowerCase();
		int y = p.charAt(0) - '1';
		int x = p.charAt(1) - 'a';
		return new Point(x, y);
	}
}
