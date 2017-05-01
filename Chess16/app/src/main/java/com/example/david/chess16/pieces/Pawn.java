package pieces;

import java.awt.Point;

/**
 * 
 * Pawn Implementation of the Abstract Piece Object
 * 
 * @author      David Parsons
 * @author      Phil Plucinski
 */
public class Pawn extends Piece {

	/**
	 * Turns since special move for enpassant
	 */
	public int turnsSinceSpecialMove = 0;

	/**
	 * Constructor.
	 * 
	 * @param color piece color
	 * @param location intial location
	 */
	public Pawn(char color, Point location) {
		super(color, location);
	}

	/**
	 * Returns a int array that representing the mobility of the piece
	 * or positions that the piece could move
	 * 0s, 1s, 2s, in each position
	 * 0 - not accessible
	 * 1 - only for move
	 * 2 - capture (implies move)
	 *
	 * @return mobility of game piece.
	 */
	@Override
	public int[][] getMobility() {

		/*
		 * note that this currently ignore array out of bounds exceptions
		 */

		int mobility[][] = new int[8][8];

		// Pawn can move two down/up if not moved yet
		// One down/up all the time
		// And can capture in any down/up diagonal
		// down/up is dependent upon color
		int orientation = (this.getColor() == 'w' ? 1 : -1); // sets up down
																// mobility 1
																// for up, -1
																// for down

		int currX = this.location.x;
		int currY = this.location.y;

		// sets two possible caputures
		if (currX + 1 < 8) {
			mobility[currX + 1][currY + orientation] = 2;
		}
		if (currX - 1 >= 0) {
			mobility[currX - 1][currY + orientation] = 2;
		}

		// Two possible forward moves (1 fwd or 2 fwd)

		mobility[currX][currY + orientation] = 1;
		// is piece still in starting row
		if (getColor() == 'w') {
			if (currY < 6) {
				mobility[currX][currY + orientation * 2] = (currY == 1) ? 1 : 0;
			}
		} else {
			if (currY > 1) {
				mobility[currX][currY + orientation * 2] = (currY == 6) ? 1 : 0;
			}
		}

		return mobility;
	}

	/**
	 * Returns the string representation of a pawn to be used in the ascii board
	 * representation.
	 *
	 * @return the ascii representation of a pawn.
	 */
	public String toString() {
		return super.toString() + 'p';
	}

	/**
	 * Updates pawn location to the given location
	 *
	 * @param p new pawn location
	 */
	public void move(Point p) {
		super.move(p);
	}

	/**
	 * Creates and return a copy of the current pawn instance
	 *
	 * @return new pawn copy
	 */
	public Piece copy() {
		return new Pawn(color, new Point(location.x, location.y));
	}

	/**
	 * Pawn equality check.
	 * 
	 * @param o object to be compared to current instance
	 * @return equality of object o and instance
	 */
	public boolean equals(Object o) {
		if (o instanceof Pawn) {
			if (((Pawn) o).location.equals(location) && ((Pawn) o).color == color) {
				return true;
			}
		}
		return false;
	}
}
