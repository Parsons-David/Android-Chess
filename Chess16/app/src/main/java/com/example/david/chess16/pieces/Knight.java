package com.example.david.chess16.pieces;

import com.example.david.chess16.control.Point;

/**
 * 
 * Knight Implementation of the Abstract Piece Object
 * 
 * @author David Parsons
 * @author Phil Plucinski
 */
public class Knight extends Piece {

	/**
	 * Constructor.
	 * 
	 * @param color
	 *            piece color
	 * @param location
	 *            intial location
	 */
	public Knight(char color, Point location) {
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

		/*
		 * note that this currently ignore array out of bounds exceptions
		 */

		int mobility[][] = new int[8][8];

		// King can move/capture one in any direction

		int currX = this.location.x;
		int currY = this.location.y;

		if (currX + 2 <= 7) {
			if (currY + 1 <= 7) {
				mobility[currX + 2][currY + 1] = 2;
			}
			if (currY - 1 >= 0) {
				mobility[currX + 2][currY - 1] = 2;
			}
		}

		if (currX + 1 <= 7) {
			if (currY + 2 <= 7) {
				mobility[currX + 1][currY + 2] = 2;
			}
			if (currY - 2 >= 0) {
				mobility[currX + 1][currY - 2] = 2;
			}
		}

		if (currX - 1 >= 0) {
			if (currY + 2 <= 7) {
				mobility[currX - 1][currY + 2] = 2;
			}
			if (currY - 2 >= 0) {
				mobility[currX - 1][currY - 2] = 2;
			}
		}

		if (currX - 2 >= 0) {
			if (currY + 1 <= 7) {
				mobility[currX - 2][currY + 1] = 2;
			}
			if (currY - 1 >= 0) {
				mobility[currX - 2][currY - 1] = 2;
			}
		}

		return mobility;
	}

	/**
	 * Returns the string representation of a knight to be used in the ascii
	 * board representation.
	 *
	 * @return the ascii representation of a knight.
	 */
	public String toString() {
		return super.toString() + 'N';
	}

	/**
	 * Updates knight location to the given location
	 *
	 * @param p
	 *            new knight location
	 */
	public void move(Point p) {
		super.move(p);
	}

	/**
	 * Creates and return a copy of the current knight instance
	 *
	 * @return new knight copy
	 */
	public Piece copy() {
		return new Knight(color, new Point(location.x, location.y));
	}
}
