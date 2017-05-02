package com.example.david.chess16.control;
import com.example.david.chess16.pieces.*;

import java.util.ArrayList;


/**
 * This class stores all of the Piece objects on the board. It keeps track of
 * which player has which pieces. The class can move pieces via its executeMove
 * method, as well as allow the user to toggle turns, check for checkmate and
 * stalemate, and print out a visual representation of the board.
 * 
 * @author David Parsons
 * @author Phil Plucinski
 */
public class Board {

	Piece[][] board = new Piece[8][8];
	ArrayList<Piece> whitePieces = new ArrayList<Piece>(16);
	ArrayList<Piece> blackPieces = new ArrayList<Piece>(16);

	ArrayList<Pawn> enpassantPawns = new ArrayList<Pawn>();

	Piece[][] prevBoard = new Piece[8][8];
	ArrayList<Piece> prevWhitePieces = new ArrayList<Piece>(16);
	ArrayList<Piece> prevBlackPieces = new ArrayList<Piece>(16);
	ArrayList<Pawn> prevEnpassantPawns = new ArrayList<Pawn>();

	private char turn = 'w';

	/**
	 * Creates a new Board object with pieces initialized to the default chess
	 * board layout.
	 */
	Board() {
		initializeBoard();
	}

	public void undo() {
		turn = turn == 'w' ? 'b' : 'w';
		board = prevBoard;
		whitePieces = prevWhitePieces;
		blackPieces = prevBlackPieces;
		enpassantPawns = prevEnpassantPawns;
	}

	/**
	 * Prints out the chess board. Empty spaces of the same parity are black
	 * (##), white ( ) otherwise. Non-empty space are represented by aA, where a
	 * is the color (w/b) and A is the piece (p = pawn, K = king, Q = queen, R =
	 * rook, B = bishop, N = knight).
	 *
	 * @return String representation of the chess board.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('\n');
		for (int row = 7; row >= 0; row--) {
			for (int col = 0; col < 8; col++) {
				// No Piece
				if (board[col][row] == null) {
					if ((row % 2) == 0 ^ (col % 2) == 0) { // Different parities
						sb.append("  ");
					} else { // Same parity
						sb.append("##");
					}
					// Piece
				} else {
					sb.append(board[col][row]);
				}
				sb.append(' ');
			}
			sb.append(row + 1);
			sb.append('\n');
		}
		for (int col = 0; col < 8; col++) {
			sb.append(" " + (char) ('a' + col) + " ");
		}
		sb.append('\n');
		return sb.toString();
	}

	/**
	 * Checks to see if there are no pieces on the board between two points on
	 * the board.
	 *
	 * @param board
	 *            2D array of Pieces to be examined
	 * @param origin
	 *            Origin point
	 * @param target
	 *            Target point
	 * @return true if there are no pieces between origin and target; false
	 *         otherwise.
	 */
	private boolean isPathClear(Piece[][] board, Point origin, Point target) {
		if (origin.equals(target)) {
			return false;
		}

		if (origin.x == target.x) { // check vertical path

			int orientation = ((origin.y > target.y) ? -1 : 1);
			int currY = origin.y + orientation;
			while (currY != target.y) {
				if (board[origin.x][currY] != null) { // every piece
														// between must be
														// clear
					return false;
				}
				currY += orientation;
			}

		} else if (origin.y == target.y) { // check horizontal path

			int orientation = ((origin.x > target.x) ? -1 : 1);
			int currX = origin.x + orientation;
			while (currX != target.x) {
				if (board[currX][origin.y] != null) { // every piece
														// between must be
														// clear
					return false;
				}
				currX += orientation;
			}

		} else if (Math.abs((origin.x - target.x)) == Math.abs((origin.y - target.y))) { // check
			// diagonal
			// of
			// slope
			// 1

			int orientationX = ((origin.x > target.x) ? -1 : 1);
			int currX = origin.x + orientationX;
			int orientationY = ((origin.y > target.y) ? -1 : 1);
			int currY = origin.y + orientationY;
			while (currX != target.x && currY != target.y) {
				if (board[currX][currY] != null) { // every piece between
													// must be clear
					return false;
				}
				currX += orientationX;
				currY += orientationY;
			}

		} else {
			if (!(board[origin.x][origin.y] instanceof Knight)) {
				return false;
			}
		}

		if (board[target.x][target.y] != null
				&& board[origin.x][origin.y].getColor() == board[target.x][target.y].getColor()) {
			return false;
		}

		return true;
	}

	/**
	 * Gets the current value of 'turn' in the current instance.
	 *
	 * @return current instance 'turn' field
	 */
	public char getTurn() {
		return turn;
	}

	/**
	 * Toggles the current player color. Sets turn to w if b, and vice versa
	 */
	public void toggleTurn() {
		turn = (turn == 'w' ? 'b' : 'w');
		ArrayList<Pawn> pawnsToRemove = new ArrayList<Pawn>();
		for (Pawn p : enpassantPawns) {
			p.turnsSinceSpecialMove++;
			if (p.turnsSinceSpecialMove >= 2) {
				pawnsToRemove.add(p);
			}
		}
		for (Pawn p : pawnsToRemove) {
			enpassantPawns.remove(p);
		}
	}

	/**
	 * Iterates over every opposing color piece and determines if the king is in
	 * any of their mobilities with a clear path. If the king is capturable
	 * returns true.
	 * 
	 * @param color
	 *            Color of the player whose pieces are to be checked.
	 * @param board
	 *            2D array of Pieces to be examined
	 * @param whitePieces
	 *            white Piece arrayList to be examined
	 * @param blackPieces
	 *            black Piece arrayList to be examined
	 * @return true if the King is in check; false otherwise
	 */
	public boolean inCheck(char color, Piece[][] board, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {

		Piece king = getKing((color == 'w' ? whitePieces : blackPieces));
		int kingX = (int) king.location.getX();
		int kingY = (int) king.location.getY();
		// Want to see if opponent pieces can attack king
		ArrayList<Piece> searchList = (color == 'w' ? blackPieces : whitePieces);

		for (Piece p : searchList) {
			if ((p.getMobility()[kingX][kingY] == 2) && (isPathClear(board, p.location, king.location))) {

				return true;
			}
		}

		return false;
	}

	/**
	 * Finds the king in a list of pieces, return null if no king is found.
	 * 
	 * @param searchList
	 *            ArrayList of pieces where king is located
	 * @return King object if found; null otherwise
	 */
	private Piece getKing(ArrayList<Piece> searchList) {
		for (Piece p : searchList) {
			if (p instanceof King) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Checks to see if the player of the specified color has any valid moves to
	 * be made that would also not put them in check.
	 * 
	 * @param color
	 *            color of player to be examined.
	 * @return true if player has valid moves that would not put them in check;
	 *         false otherwise
	 */
	private boolean existValidMoves(char color) {
		ArrayList<Piece> searchList = (color == 'b' ? blackPieces : whitePieces);

		for (Piece p : searchList) {
			int[][] mobility = p.getMobility();
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					if (mobility[x][y] != 0 && isPathClear(board, p.location, new Point(x, y))) {
						// Check if said move will cause check.
						Piece[][] vBoard = new Piece[8][8];
						ArrayList<Piece> vWhitePieces = new ArrayList<Piece>(16);
						ArrayList<Piece> vBlackPieces = new ArrayList<Piece>(16);

						saveStateToVirtualStorage(vBoard, vWhitePieces, vBlackPieces);
						if (executeMove(vBoard, vWhitePieces, vBlackPieces, p.location, new Point(x, y), ' ') != null) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public ArrayList<Point> getValidMoves(Piece p) {
		ArrayList<Point> validMoves = new ArrayList<Point>();

		int[][] mobility = p.getMobility();
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (mobility[x][y] != 0 && isPathClear(board, p.location, new Point(x, y))) {
					// Check if said move will cause check.
					Piece[][] vBoard = new Piece[8][8];
					ArrayList<Piece> vWhitePieces = new ArrayList<Piece>(16);
					ArrayList<Piece> vBlackPieces = new ArrayList<Piece>(16);

					saveStateToVirtualStorage(vBoard, vWhitePieces, vBlackPieces);
					if (executeMove(vBoard, vWhitePieces, vBlackPieces, p.location, new Point(x, y), ' ') != null) {
						validMoves.add(new Point(x, y));
					}
				}
			}
		}
		return validMoves;
	}

	/**
	 * Checks to see if the argument Pawn is contained in the list of
	 * enpassant-eligible pawns.
	 * 
	 * @param p
	 *            Pawn to be examined.
	 * @return true if Pawn p is eligible to perform enpassant; false otherwise.
	 */
	private boolean pawnIsEnpassant(Pawn p) {
		for (Pawn q : enpassantPawns) {
			if (p.equals(q)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks to see if origin and target points are within the 8x8 chess board.
	 * 
	 * @param origin
	 *            1st Point to be examined.
	 * @param target
	 *            2nd Point to be examined.
	 * @return true if points are within bounds; false otherwise
	 */
	private boolean verifyBounds(Point origin, Point target) {
		if (origin.x < 0 || origin.y < 0 || origin.x > 7 || origin.y > 7) {
			return false;
		}
		if (target.x < 0 || target.y < 0 || target.x > 7 || target.y > 7) {
			return false;
		}
		return true;
	}

	/**
	 * Performs move on piece located at origin based on its target. Checks to
	 * see if the move is valid according to the capabilities of each piece and
	 * whether the move would put the player in check.
	 * 
	 * @param board
	 *            2D Piece array to be used as the reference board.
	 * @param whitePieces
	 *            ArrayList of white pieces to be referenced.
	 * @param blackPieces
	 *            ArrayList of black pieces to be referenced.
	 * @param origin
	 *            Point containing the piece to be moved.
	 * @param target
	 *            Destination Point
	 * @param promotion
	 *            char indicating the desired promotion, if applicable; ' '
	 *            otherwise
	 * @return Move representation of the executed move. Null if move was
	 *         invalid.
	 */
	public Move executeMove(Piece[][] board, ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces, Point origin,
			Point target, char promotion) {

		Piece[][] vBoard = new Piece[8][8];
		ArrayList<Piece> vWhitePieces = new ArrayList<Piece>(16);
		ArrayList<Piece> vBlackPieces = new ArrayList<Piece>(16);

		// Check bounds of origin and target
		if (!verifyBounds(origin, target)) {
			return null;
		}

		// Check that piece exists and that it belongs to the current player
		if (board[origin.x][origin.y] == null || board[origin.x][origin.y].getColor() != turn) {
			return null;
		}

		// Check that piece is not trying to move onto a friendly piece.
		if (board[target.x][target.y] != null
				&& board[origin.x][origin.y].getColor() == board[target.x][target.y].getColor()) {
			return null;
		}

		saveStateToVirtualStorage(vBoard, vWhitePieces, vBlackPieces);

		Piece p = vBoard[origin.x][origin.y];
		char color = p.getColor();

		// Check that piece is not trying to move onto another piece. (Ignore
		// special moves)
		if (p.getMobility()[target.x][target.y] == 1 && board[target.x][target.y] != null) {
			return null;
		}

		// Checking for/performing special moves first

		// Castling
		if (p instanceof King) {
			if (color == 'w' && origin.x == 4 && origin.y == 0 && target.x == 2 && target.y == 0) {
				// White Queen-side Castle attempt detected
				Piece rook = vBoard[0][0];
				if (!((King) p).hasMoved && !inCheck(color, board, whitePieces, blackPieces) && rook instanceof Rook
						&& !((Rook) rook).hasMoved && board[1][0] == null && board[2][0] == null
						&& board[3][0] == null) {
					// Check if king is to pass through attacked squares.
					for (Piece q : blackPieces) {
						for (int i = 1; i <= 3; i++) {
							if (q.getMobility()[i][0] == 2 && isPathClear(board, q.location, new Point(i, 0))) {
								return null;
							}
						}
					}
					// Castle eligible
					vBoard[2][0] = p;
					vBoard[3][0] = rook;
					vBoard[4][0] = null;
					vBoard[0][0] = null;
					p.move(new Point(2, 0));
					rook.move(new Point(3, 0));

					if (inCheck(color, vBoard, vWhitePieces, vBlackPieces)) {
						return null;
					}

					if (board == this.board) {
						savePrevious();
					}

					whitePieces.remove(board[origin.x][origin.y]);
					whitePieces.remove(board[0][0]);
					board[2][0] = p;
					board[3][0] = rook;
					board[4][0] = null;
					board[0][0] = null;
					whitePieces.add(p);
					whitePieces.add(rook);

					Move m = new Move();
					m.setMove(origin, target, new Point(0, 0), new Point(3, 0), turn);

					return m;

				} else {
					return null;
				}
			} else if (color == 'w' && origin.x == 4 && origin.y == 0 && target.x == 6 && target.y == 0) {
				// White King-side Castle attempt detected
				Piece rook = board[7][0];
				if (!((King) p).hasMoved && !inCheck(color, board, whitePieces, blackPieces) && rook instanceof Rook
						&& !((Rook) rook).hasMoved && board[5][0] == null && board[6][0] == null) {
					// Check if king is to pass through attacked squares.
					for (Piece q : blackPieces) {
						for (int i = 5; i <= 6; i++) {
							if (q.getMobility()[i][0] == 2 && isPathClear(board, q.location, new Point(i, 0))) {
								return null;
							}
						}
					}
					// Castle eligible
					vBoard[6][0] = p;
					vBoard[5][0] = rook;
					vBoard[4][0] = null;
					vBoard[7][0] = null;
					p.move(new Point(6, 0));
					rook.move(new Point(5, 0));

					if (inCheck(color, vBoard, vWhitePieces, vBlackPieces)) {
						return null;
					}

					if (board == this.board) {
						savePrevious();
					}

					whitePieces.remove(board[origin.x][origin.y]);
					whitePieces.remove(board[7][0]);
					board[6][0] = p;
					board[5][0] = rook;
					board[4][0] = null;
					board[7][0] = null;
					whitePieces.add(p);
					whitePieces.add(rook);

					Move m = new Move();
					m.setMove(origin, target, new Point(7, 0), new Point(5, 0), turn);

					return m;
				} else {
					return null;
				}
			} else if (color == 'b' && origin.x == 4 && origin.y == 7 && target.x == 2 && target.y == 7) {
				// Black Queen-side Castle attempt detected
				Piece rook = board[0][7];
				if (!((King) p).hasMoved && !inCheck(color, board, whitePieces, blackPieces) && rook instanceof Rook
						&& !((Rook) rook).hasMoved && board[1][7] == null && board[2][7] == null
						&& board[3][7] == null) {
					// Check if king is to pass through attacked squares.
					for (Piece q : whitePieces) {
						for (int i = 1; i <= 3; i++) {
							if (q.getMobility()[i][7] == 2 && isPathClear(board, q.location, new Point(i, 7))) {
								return null;
							}
						}
					}
					// Castle eligible
					vBoard[2][7] = p;
					vBoard[3][7] = rook;
					vBoard[4][7] = null;
					vBoard[0][7] = null;
					p.move(new Point(2, 7));
					rook.move(new Point(3, 7));

					if (inCheck(color, vBoard, vWhitePieces, vBlackPieces)) {
						return null;
					}

					if (board == this.board) {
						savePrevious();
					}

					blackPieces.remove(board[origin.x][origin.y]);
					blackPieces.remove(board[0][7]);
					board[2][7] = p;
					board[3][7] = rook;
					board[4][7] = null;
					board[0][7] = null;
					blackPieces.add(p);
					blackPieces.add(rook);

					Move m = new Move();
					m.setMove(origin, target, new Point(0, 7), new Point(3, 7), turn);

					return m;
				} else {
					return null;
				}
			} else if (color == 'b' && origin.x == 4 && origin.y == 7 && target.x == 6 && target.y == 7) {
				// Black King-side Castle attempt detected
				Piece rook = board[7][7];
				if (!((King) p).hasMoved && !inCheck(color, board, whitePieces, blackPieces) && rook instanceof Rook
						&& !((Rook) rook).hasMoved && board[5][7] == null && board[6][7] == null) {
					// Check if king is to pass through attacked squares.
					for (Piece q : whitePieces) {
						for (int i = 5; i <= 6; i++) {
							if (q.getMobility()[i][7] == 2 && isPathClear(board, q.location, new Point(i, 7))) {
								return null;
							}
						}
					}
					// Castle eligible
					vBoard[6][7] = p;
					vBoard[5][7] = rook;
					vBoard[4][7] = null;
					vBoard[7][7] = null;
					p.move(new Point(6, 7));
					rook.move(new Point(5, 7));

					if (inCheck(color, vBoard, vWhitePieces, vBlackPieces)) {
						return null;
					}

					if (board == this.board) {
						savePrevious();
					}

					blackPieces.remove(board[origin.x][origin.y]);
					blackPieces.remove(board[7][7]);
					board[6][7] = p;
					board[5][7] = rook;
					board[4][7] = null;
					board[7][7] = null;
					blackPieces.add(p);
					blackPieces.add(rook);

					Move m = new Move();
					m.setMove(origin, target, new Point(7, 7), new Point(5, 7), turn);

					return m;
				} else {
					return null;
				}
			}
		}

		// MAY NEED TO CONFIRM PROMOTION CHAR
		// HAVE TO CONFIRM PERSISTENCE OF PAWNS WITH EN PASSANT
		// Enpassant/Pawn Special Move
		if (p instanceof Pawn) {
			// EP
			if (color == 'w' && p.location.y == 4 && origin.x != target.x && board[target.x][target.y] == null) {
				if (board[target.x][4] instanceof Pawn && pawnIsEnpassant(new Pawn('b', new Point(target.x, 4)))) {

					vBoard[origin.x][origin.y] = null;
					vBoard[target.x][target.y] = p;
					p.move(new Point(target.x, target.y));

					if (inCheck(color, vBoard, vWhitePieces, vBlackPieces)) {
						return null;
					}

					if (board == this.board) {
						savePrevious();
					}

					blackPieces.remove(board[target.x][4]);
					whitePieces.remove(board[origin.x][origin.y]);
					board[origin.x][origin.y] = null;
					board[target.x][4] = null;
					board[target.x][target.y] = p;
					whitePieces.add(p);

					Move m = new Move();
					m.setMove(origin, target, new Point(target.x, 4), null, turn);

					return m;
				} else {
					return null;
				}
			} else if (color == 'b' && origin.x != target.x && p.location.y == 3 && board[target.x][target.y] == null) {
				if (board[target.x][3] instanceof Pawn && pawnIsEnpassant(new Pawn('w', new Point(target.x, 3)))) {
					vBoard[origin.x][origin.y] = null;
					vBoard[target.x][target.y] = p;
					p.move(new Point(target.x, target.y));

					if (inCheck(color, vBoard, vWhitePieces, vBlackPieces)) {
						return null;
					}

					if (board == this.board) {
						savePrevious();
					}

					whitePieces.remove(board[target.x][3]);
					blackPieces.remove(board[origin.x][origin.y]);
					board[origin.x][origin.y] = null;
					board[target.x][3] = null;
					board[target.x][target.y] = p;
					blackPieces.add(p);

					Move m = new Move();
					m.setMove(origin, target, new Point(target.x, 3), null, turn);

					return m;
				} else {
					return null;
				}
				// Special Move
			} else if (Math.abs(origin.y - target.y) == 2) {
				if ((color == 'w' && origin.y == 1)
						|| (color == 'b' && origin.y == 6) && isPathClear(board, origin, target)) {
					enpassantPawns.add((Pawn) p);
				} else {
					return null;
				}
				// Promotion
			} else if (color == 'w' && target.y == 7) {
				Piece temp = p;
				vWhitePieces.remove(p);
				p = promote(promotion, temp);
				vWhitePieces.add(p);

				vBoard[origin.x][origin.y] = null;
				vBoard[target.x][target.y] = p;
				/** Null pointer **/
				p.move(new Point(target.x, target.y));

				if (inCheck(color, vBoard, vWhitePieces, vBlackPieces)) {
					return null;
				}

				if (board == this.board) {
					savePrevious();
				}

				whitePieces.remove(board[origin.x][origin.y]);
				board[origin.x][origin.y] = null;
				board[target.x][target.y] = p;
				whitePieces.add(p);

				Move m = new Move();
				m.setMove(origin, target, null, null, turn);
				m.promotionType = Character.toLowerCase(promotion);

				return m;

			} else if (color == 'b' && target.y == 0) {
				Piece temp = p;
				vBlackPieces.remove(p);
				p = promote(promotion, temp);
				vBlackPieces.add(p);

				vBoard[origin.x][origin.y] = null;
				vBoard[target.x][target.y] = p;
				p.move(new Point(target.x, target.y));

				if (inCheck(color, vBoard, vWhitePieces, vBlackPieces)) {
					return null;
				}

				if (board == this.board) {
					savePrevious();
				}

				blackPieces.remove(board[origin.x][origin.y]);
				board[origin.x][origin.y] = null;
				board[target.x][target.y] = p;
				blackPieces.add(p);

				Move m = new Move();
				m.setMove(origin, target, null, null, turn);
				m.promotionType = Character.toLowerCase(promotion);

				return m;
				// Check to make sure diagonal move is actually an attack.
			} else if (p.getMobility()[target.x][target.y] == 2 && vBoard[target.x][target.y] == null) {
				return null;
			}

		}

		// All Special Cases handled.

		// Piece must be allowed to move to target.
		if (p.getMobility()[target.x][target.y] == 0) {
			return null;
		}

		// Path must be clear.
		if (!isPathClear(board, origin, target)) {
			return null;
		}

		vBoard[origin.x][origin.y] = null;
		vBoard[target.x][target.y] = p;
		p.move(new Point(target.x, target.y));

		if (inCheck(color, vBoard, vWhitePieces, vBlackPieces)) {
			if (p instanceof Pawn && enpassantPawns.contains((Pawn) p)) {
				enpassantPawns.remove((Pawn) p);
			}
			return null;
		}

		if (board == this.board) {
			savePrevious();
		}

		if (board[target.x][target.y] != null) {
			(color == 'b' ? whitePieces : blackPieces).remove(board[target.x][target.y]);
		}

		(color == 'w' ? whitePieces : blackPieces).remove(board[origin.x][origin.y]);
		(color == 'w' ? whitePieces : blackPieces).add(p);
		board[origin.x][origin.y] = null;
		board[target.x][target.y] = p;

		Move m = new Move();
		m.setMove(origin, target, null, null, turn);

		// EnPassant pawn was added that did not exist in previous state.
		if (board == this.board && p instanceof Pawn && Math.abs(origin.y - target.y) == 2) {
			prevEnpassantPawns.remove(enpassantPawns.size() - 1);
		}

		return m;
	}

	/**
	 * Creates and returns a new Piece based on desired promotion with color and
	 * location attributes of original pawn.
	 * 
	 * @param promotion
	 *            char representing desired Piece subclass {Q(ueen), (K)N(ight),
	 *            R(ook), B(ishop)}
	 * @param pawn
	 *            Pawn to be promoted
	 * @return
	 */
	private Piece promote(char promotion, Piece pawn) {
		Piece promotedPiece = null;
		switch (promotion) {
		case 'Q':
			promotedPiece = new Queen(pawn.getColor(), pawn.location);
			break;
		case 'N':
			promotedPiece = new Knight(pawn.getColor(), pawn.location);
			break;
		case 'R':
			promotedPiece = new Rook(pawn.getColor(), pawn.location);
			break;
		case 'B':
			promotedPiece = new Bishop(pawn.getColor(), pawn.location);
			break;
		default:
			promotedPiece = new Queen(pawn.getColor(), pawn.location);
			break;
		}
		return promotedPiece;
	}

	/**
	 * Saves the current state of the board over to placeholders for a previous
	 * state.
	 */
	private void savePrevious() {
		prevBoard = new Piece[8][8];
		prevWhitePieces = new ArrayList<Piece>(16);
		prevBlackPieces = new ArrayList<Piece>(16);
		prevEnpassantPawns = new ArrayList<Pawn>();

		saveStateToVirtualStorage(prevBoard, prevWhitePieces, prevBlackPieces);
		for (Pawn p : enpassantPawns) {
			prevEnpassantPawns.add(p);
		}
	}

	/**
	 * Creates a copy of the board and piece lists and saves them to the
	 * argument lists.
	 * 
	 * @param vBoard
	 *            2D Piece array copy destination
	 * @param vWhitePieces
	 *            ArrayList of white pieces copy destination
	 * @param vBlackPieces
	 *            ArrayList of black pieces copy destination
	 */
	private void saveStateToVirtualStorage(Piece[][] vBoard, ArrayList<Piece> vWhitePieces,
			ArrayList<Piece> vBlackPieces) {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (board[x][y] != null) {
					vBoard[x][y] = board[x][y].copy();
					if (vBoard[x][y].getColor() == 'w') {
						vWhitePieces.add(vBoard[x][y]);
					} else {
						vBlackPieces.add(vBoard[x][y]);
					}
				}
			}
		}
	}

	/**
	 * Checks to see if either player is in checkmate
	 * 
	 * @return true if a player is checkmated; false otherwise
	 */
	public boolean checkMate() {
		if (turn == 'w' && inCheck('w', board, whitePieces, blackPieces) && !existValidMoves('w')) {
			System.out.println(this);
			System.out.println("Checkmate");
			System.out.println("Black wins");
			return true;
		} else if (turn == 'b' && inCheck('b', board, whitePieces, blackPieces) && !existValidMoves('b')) {
			System.out.println(this);
			System.out.println("Checkmate");
			System.out.println("White wins");
			return true;
		}
		return false;
	}

	/**
	 * Checks to see if a stalemate has occurred.
	 * 
	 * @return true if statemate is detected; false otherwise
	 */
	public boolean inStalemate() {
		if (turn == 'w' && !inCheck('w', board, whitePieces, blackPieces) && !existValidMoves('w')) {
			System.out.println(this);
			System.out.println("Stalemate");
			System.out.println("Draw");
			return true;
		} else if (turn == 'b' && !inCheck('b', board, whitePieces, blackPieces) && !existValidMoves('b')) {
			System.out.println(this);
			System.out.println("Stalemate");
			System.out.println("Draw");
			return true;
		}
		return false;
	}

	/**
	 * Checks to see if a checkmate or stalemate has occurred.
	 * 
	 * @return true if checkmate or stalemate has been detected; false otherwise
	 */
	public boolean matchCanContinue() {
		return !(checkMate() || inStalemate());
	}

	/**
	 * Prints "The Match is over"
	 * 
	 */
	public void printEndState() {
		System.out.println("The Match is over");
	}

	/**
	 * Populates the board with all 32 chess pieces in their default layout.
	 */
	private void initializeBoard() {
		board[0][7] = new Rook('b', new Point(0, 7));
		board[1][7] = new Knight('b', new Point(1, 7));
		board[2][7] = new Bishop('b', new Point(2, 7));
		board[3][7] = new Queen('b', new Point(3, 7));
		board[4][7] = new King('b', new Point(4, 7));
		board[5][7] = new Bishop('b', new Point(5, 7));
		board[6][7] = new Knight('b', new Point(6, 7));
		board[7][7] = new Rook('b', new Point(7, 7));

		for (int i = 0; i < 8; i++) {
			board[i][6] = new Pawn('b', new Point(i, 6));
		}

		for (int y = 7; y >= 6; y--) {
			for (int x = 0; x < 8; x++) {
				blackPieces.add(board[x][y]);
			}
		}

		for (int i = 0; i < 8; i++) {
			board[i][1] = new Pawn('w', new Point(i, 1));
		}

		board[0][0] = new Rook('w', new Point(0, 0));
		board[1][0] = new Knight('w', new Point(1, 0));
		board[2][0] = new Bishop('w', new Point(2, 0));
		board[3][0] = new Queen('w', new Point(3, 0));
		board[4][0] = new King('w', new Point(4, 0));
		board[5][0] = new Bishop('w', new Point(5, 0));
		board[6][0] = new Knight('w', new Point(6, 0));
		board[7][0] = new Rook('w', new Point(7, 0));

		for (int y = 0; y < 2; y++) {
			for (int x = 0; x < 8; x++) {
				whitePieces.add(board[x][y]);
			}
		}
	}

}
