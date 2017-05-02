package com.example.david.chess16.control;
import com.example.david.chess16.pieces.*;

public class Point {
	public int x;
	public int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean equals(Object o) {
		if (o == null || !(o instanceof Point)) {
			return false;
		}
		if (this.x == ((Point) o).x && this.y == ((Point) o).y) {
			return true;
		}
		return false;
	}

	public String toString() {
		return "[x = " + x + ", y = " + y + "]";
	}
}
