/** 
 * Copyright (C) 2019  Javier Martín Moreno
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package juegoCTF;

/**
 * CLASE QUE GUARDA LA INFORMACION DE LA POSICION
 */
public class Posicion {

	private int X, Y;

	public Posicion(int _x, int _y) {
		X = _x;
		Y = _y;
	}

	public Posicion() {
		X = -1;
		Y = -1;
	}

	public int getX() {
		return X;
	}

	public int getY() {
		return Y;
	}

	public void setXY(int _x, int _y) {
		X = _x;
		Y = _y;
	}

	@Override
	public boolean equals(Object obj) {
		Posicion o = (Posicion) obj;
		return this.X == o.X && this.Y == o.Y;
	}

	public String toString() {
		return "(" + X + "," + Y + ")";
	}

}
