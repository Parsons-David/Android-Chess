package control;

import java.awt.Point;
import java.util.ArrayList;

/**
 * This class represents a Move in chess. Each move can physically move up to
 * two chess pieces of the performing player's pieces. The displayBoard
 * represents the board after the move has been performed on the chess board.
 * 
 * @author Phil
 *
 */
public class Move {

	Point firstPieceOrigin = null;
	Point firstPieceTarget = null;
	Point secondPieceOrigin = null;
	Point secondPieceTarget = null;
	char promotionType = '\0';
	char check = '\0';
	String[][] displayBoard = null;
	Boolean pendingDraw = false;
	char turn = 'w';

	/**
	 * No-arg constructor.
	 */
	Move() {
	}

	/**
	 * Initializes the move based on arguments.
	 * 
	 * @param firstOrigin
	 *            Point
	 * @param firstTarget
	 *            Point
	 * @param secondOrigin
	 *            Point
	 * @param secondTarget
	 *            Point
	 */
	void setMove(Point firstOrigin, Point firstTarget, Point secondOrigin, Point secondTarget, char turn) {

		this.firstPieceOrigin = firstOrigin;
		this.firstPieceTarget = firstTarget;
		this.secondPieceOrigin = secondOrigin;
		this.secondPieceTarget = secondTarget;
		this.promotionType = '\0';
		this.check = '\0';
		this.turn = turn;
	}

	/**
	 * @return Point representation of the first piece's origin.
	 */
	public Point getFirstPieceOrigin() {
		return firstPieceOrigin;
	}

	/**
	 * @return Point representation of the first piece's target.
	 */
	public Point getFirstPieceTarget() {
		return firstPieceTarget;
	}

	/**
	 * @return Point representation of the second piece's origin.
	 */
	public Point getSecondPieceOrigin() {
		return secondPieceOrigin;
	}

	/**
	 * @return Point representation of the sceond piece's target.
	 */
	public Point getSecondPieceTarget() {
		return secondPieceTarget;
	}

	/**
	 * @return Boolean of value true if the move is a promotion. False
	 *         otherwise.
	 */
	public boolean isPromotion() {
		return promotionType != '\0';
	}

	/**
	 * @return Boolean of value true if the move caused check. False otherwise.
	 */
	public boolean isCheck() {
		return check == '\0';
	}

	/**
	 * @return char Type of promotion (Q,K,B,N)
	 */
	public char getPromotion() {
		return promotionType;
	}

	/**
	 * @return char 'w' if white is in check. 'b' if black is in check. '\0'
	 *         otherwise
	 */
	public char getCheck() {
		return check;
	}

	/**
	 * Updates the display board by executing the move on the previous display
	 * board.
	 * 
	 * @param prevBoard
	 *            Board in a state prior to the execution of the move.
	 */
	void updateDisplayBoard(String[][] prevBoard) {
		if (displayBoard != null) {
			// displayBoard was already initialized. Should not act again.
			return;
		}
		displayBoard = copyDisplayBoard(prevBoard);
		executeMoveOnDisplayBoard();
	}

	/**
	 * @return String[][] representation of the board.
	 */
	public String[][] getPieces() {
		return displayBoard;
	}

	public Boolean hasPendingDraw() {
		return pendingDraw;
	}

	public ArrayList<String> getPossiblePromoteSpaces() {
		ArrayList<String> promotables = new ArrayList<String>();
		for (int i = 0; i < 8; i++) {
			if (displayBoard[i][6].equals("wp")) {
				promotables.add("6" + ('a' + i));
			}
			if (displayBoard[i][1].equals("bp")) {
				promotables.add("1" + ('a' + i));
			}
		}
		return null;
	}

	public char getTurn() {
		return turn;
	}

	/**
	 * @param board
	 *            String[][] representation of the board to be copied.
	 * @return a copy of the String[][] representation of the argument board.
	 */
	private String[][] copyDisplayBoard(String[][] board) {

		String[][] copyBoard = new String[8][8];

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				copyBoard[i][j] = board[i][j];
			}
		}

		return copyBoard;
	}

	/**
	 * Copies over string representation in displayBoard based on piece 1 and 2
	 * origin to piece 1 and 2 destinations. Applies promotion if necessary.
	 */
	private void executeMoveOnDisplayBoard() {
		String firstPiece = displayBoard[(int) firstPieceOrigin.getX()][(int) firstPieceOrigin.getY()];
		if (isPromotion()) {
			String color = firstPiece.substring(0, 1);
			displayBoard[(int) firstPieceTarget.getX()][(int) firstPieceTarget.getY()] = color + promotionType;
		} else {
			displayBoard[(int) firstPieceTarget.getX()][(int) firstPieceTarget.getY()] = firstPiece;
		}
		displayBoard[(int) firstPieceOrigin.getX()][(int) firstPieceOrigin.getY()] = "";
		if (secondPieceOrigin != null) {
			displayBoard[(int) secondPieceTarget.getX()][(int) secondPieceTarget
					.getY()] = displayBoard[(int) firstPieceOrigin.getX()][(int) firstPieceOrigin.getY()];
			displayBoard[(int) secondPieceOrigin.getX()][(int) secondPieceOrigin.getY()] = "";
		}
	}

	/**
	 * 
	 * @return string representation of Move. Ex. "Piece 1: a7 to a8\nPromotion:
	 *         Q\nPiece 3: N/A"
	 */
	public String toString() {
		return "Piece 1: " + firstPieceOrigin.toString() + " to " + firstPieceTarget.toString() + "\nPromotion: "
				+ (promotionType == '\0' ? "N/A" : promotionType) + "\nPiece 2: "
				+ (secondPieceOrigin == null ? "N/A" : secondPieceOrigin.toString() + " to ")
				+ (secondPieceOrigin == null ? "" : secondPieceTarget.toString());
	}

}
