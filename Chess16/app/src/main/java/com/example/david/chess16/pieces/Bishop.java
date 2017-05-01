package pieces;

import java.awt.Point;

/**
 * 
 * Bishop Implementation of the Abstract Piece Object
 * 
 * @author      David Parsons
 * @author      Phil Plucinski
 */
public class Bishop extends Piece {

	/**
	 * Constructor.
	 * 
	 * @param color piece color
	 * @param location intial location
	 */
	public Bishop(char color, Point location) {
		super(color, location);
	}

	/**
	 * Returns the string representation of a bishop to be used in the ascii board
	 * representation.
	 *
	 * @return the ascii representation of a bishop.
	 */
	public String toString() {
		return super.toString() + 'B';
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
		int mobility[][] = new int[8][8];

		int currX = this.location.x;
		int currY = this.location.y;

		for (int x = 0; x < 8; x++) {
			if (currY - currX + x >= 0 && currY - currX + x < 8) {
				mobility[x][currY - currX + x] = 2;
			}
			if (currY + currX - x >= 0 && currY + currX - x < 8) {
				mobility[x][currY + currX - x] = 2;
			}
		}

		mobility[currX][currY] = 0;

		return mobility;
	}

	/**
	 * Updates bishop location to the given location
	 *
	 * @param p new bishop location
	 */
	public void move(Point p) {
		super.move(p);
	}

	/**
	 * Creates and return a copy of the current bishop instance
	 *
	 * @return new bishop copy
	 */
	public Piece copy() {
		return new Bishop(color, new Point(location.x, location.y));
	}
}
