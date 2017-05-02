package com.example.david.chess16.pieces;

import com.example.david.chess16.control.Point;

/**
 * 
 * Queen Implementation of the Abstract Piece Object
 * 
 * @author David Parsons
 * @author Phil Plucinski
 */
public class Queen extends Piece {

	/**
	 * Constructor.
	 * 
	 * @param color
	 *            piece color
	 * @param location
	 *            intial location
	 */
	public Queen(char color, Point location) {
		super(color, location);
	}

	/**
	 * Returns a int array that representing the mobility of the piece or
	 * positions that the piece could move 0s, 1s, 2s, in each position 0 - not
	 * accessible 1 - only for move 2 - capture (implies move)
	 *
	 * @return mobility of game piece.
	 */
	@Override
	public int[][] getMobility() {
		int mobility[][] = new int[8][8];

		int currX = this.location.x;
		int currY = this.location.y;

		for (int y = 0; y < 8; y++) {
			mobility[currX][y] = 2;
		}
		for (int x = 0; x < 8; x++) {
			mobility[x][currY] = 2;
		}
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
	 * Returns the string representation of a queen to be used in the ascii
	 * board representation.
	 *
	 * @return the ascii representation of a queen.
	 */
	public String toString() {
		return super.toString() + 'Q';
	}

	/**
	 * Updates queen location to the given location
	 *
	 * @param p
	 *            new queen location
	 */
	public void move(Point p) {
		super.move(p);
	}

	/**
	 * Creates and return a copy of the current queen instance
	 *
	 * @return new queen copy
	 */
	public Piece copy() {
		return new Queen(color, new Point(location.x, location.y));
	}

}
