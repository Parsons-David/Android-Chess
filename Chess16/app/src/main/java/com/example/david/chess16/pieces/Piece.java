package com.example.david.chess16.pieces;

import com.example.david.chess16.control.Point;

/**
 * 
 * Abstract Piece Object
 * 
 * @author David Parsons
 * @author Phil Plucinski
 */
public abstract class Piece {

	/**
	 * color of piece
	 */
	protected char color;

	/**
	 * Point (x,y) location of piece
	 */
	public Point location;

	/**
	 * Constructor.
	 * 
	 * @param color
	 *            piece color
	 * @param location
	 *            initial location
	 */
	Piece(char color, Point location) {
		this.color = color;
		this.location = location;
	}

	/**
	 * Returns a int array that representing the mobility of the piece or
	 * positions that the piece could move 0s, 1s, 2s, in each position 0 - not
	 * accessible 1 - only for move 2 - capture (implies move)
	 *
	 * @return mobility of game piece.
	 */
	public abstract int[][] getMobility();

	/**
	 * Updates pieces location to the given location
	 *
	 * @param p
	 *            new piece location
	 */
	public void move(Point p) {
		location = p;
	}

	/**
	 * Creates and return a copy of the current piece instance
	 *
	 * @return new piece copy
	 */
	public abstract Piece copy();

	/**
	 * Returns the string representation of the chess game piece to be used in
	 * the ascii board representation.
	 *
	 * @return the ascii representation of the game piece
	 */
	public String toString() {
		return color + "";
	}

	/**
	 * Gets the color of the current piece instance
	 *
	 * @return color of the piece
	 */
	public char getColor() {
		return this.color;
	}
}
