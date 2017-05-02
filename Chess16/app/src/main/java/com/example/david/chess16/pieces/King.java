package com.example.david.chess16.pieces;

import com.example.david.chess16.control.Point;

/**
 * 
 * King Implementation of the Abstract Piece Object
 * 
 * @author David Parsons
 * @author Phil Plucinski
 */
public class King extends Piece {

	/**
	 * States whether or not thing king has moved, used for special moves
	 */
	public boolean hasMoved = false;

	/**
	 * Constructor.
	 * 
	 * @param color
	 *            piece color
	 * @param location
	 *            intial location
	 */
	public King(char color, Point location) {
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

		// set three col right
		if (currX + 1 < 8) {
			if (currY + 1 < 8) {
				mobility[currX + 1][currY + 1] = 2;
			}
			mobility[currX + 1][currY] = 2;
			if (currY - 1 >= 0) {
				mobility[currX + 1][currY - 1] = 2;
			}
		}

		// set two in col
		if (currY + 1 < 8) {
			mobility[currX][currY + 1] = 2;
		}
		if (currY - 1 >= 0) {
			mobility[currX][currY - 1] = 2;
		}

		// set three col left
		if (currX - 1 >= 0) {
			if (currY + 1 < 8) {
				mobility[currX - 1][currY + 1] = 2;
			}
			mobility[currX - 1][currY] = 2;
			if (currY - 1 >= 0) {
				mobility[currX - 1][currY - 1] = 2;
			}
		}

		return mobility;
	}

	/**
	 * Returns the string representation of a king to be used in the ascii board
	 * representation.
	 *
	 * @return the ascii representation of a king.
	 */
	public String toString() {
		return super.toString() + 'K';
	}

	/**
	 * Updates king location to the given location
	 *
	 * @param p
	 *            new king location
	 */
	public void move(Point p) {
		super.move(p);
		if (!(hasMoved)) {
			hasMoved = true;
		}
	}

	/**
	 * Creates and return a copy of the current king instance
	 *
	 * @return new king copy
	 */
	public Piece copy() {
		King k = new King(color, new Point(location.x, location.y));
		k.hasMoved = this.hasMoved;
		return k;
	}

}
