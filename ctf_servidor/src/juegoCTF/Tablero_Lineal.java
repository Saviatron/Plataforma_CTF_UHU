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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import juegoCTF.Posicion;

/**
 * CLASE QUE GUARDA LA INFORMACION DE LAS CASILLAS DEL MAPA
 */
public class Tablero_Lineal {

	Casilla[] casillas;
	int DimX, DimY;

	/**
	 * Constructor para el tablero
	 */
	public Tablero_Lineal() {
		casillas = null;
		DimX = 0;
		DimY = 0;
	}

	public Tablero_Lineal(FileReader fr) {
		BufferedReader bfs = new BufferedReader(fr);
		List<String> mapa = new LinkedList<String>();

		String cadenax;
		try {
			while ((cadenax = bfs.readLine()) != null) {
				mapa.add(cadenax);
			}
			bfs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DimX = mapa.get(0).length();
		DimY = mapa.size();

		casillas = new Casilla[mapa.get(0).length() * mapa.size()];

		int i = 0, j = 0;
		Iterator<String> mm = mapa.iterator();
		while (mm.hasNext()) {
			char[] linea = mm.next().toCharArray();
			for (i = 0; i < linea.length; i++)
				casillas[indice(i, j)] = new Casilla(linea[i]);
			j++;
		}

	}

	public Tablero_Lineal(int _x, int _y, String mapa) {
		DimX = _x;
		DimY = _y;
		char[] cc = mapa.toCharArray();
		casillas = new Casilla[mapa.length()];
		for (int i = 0; i < mapa.length(); i++)
			casillas[i] = new Casilla(cc[i]);
	}

	public boolean esPared(Posicion pos) {
		if (pos.getX() < 0 || pos.getY() < 0 || pos.getX() >= getDX() || pos.getY() >= getDY())
			return true;
		return casillas[indice(pos.getX(), pos.getY())].esPared();
	}

	public void setContenido(int _x, int _y, char cont) {
		casillas[indice(_x, _y)].setContenido(cont);
	}

	public int getDX() {
		return DimX;
	}

	public int getDY() {
		return DimY;
	}

	public boolean esVacio(Posicion pos) {
		return casillas[indice(pos.getX(), pos.getY())].esVacio();
	}

	public char[] TableroCompleto() {
		char[] sb = new char[casillas.length];
		for (int i = 0; i < casillas.length; i++)
			sb[i] = casillas[i].getContenido();
		return sb;
	}

	public char[] TableroParcial(int esqx, int esqy, int ventanax, int ventanay) {

		// TODO

		return null;
	}

	public char getContenido(int x, int y) {
		return casillas[indice(x, y)].getContenido();
	}

	private int indice(int x, int y) {
		return y * DimX + x;
	}

	/**
	 * Conversion a cadena
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < DimY; j++) {
			for (int i = 0; i < DimX; i++)
				sb.append(casillas[i + j * DimX].getContenido());
			sb.append('\n');
		}

		return sb.toString();
	}

}
