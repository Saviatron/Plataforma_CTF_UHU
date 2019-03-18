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

import config.Config;

/**
 * CLASE QUE GUARDA LA INFORMACION DE LAS CASILLAS DEL MAPA
 */
public class Casilla implements Config {

	private char Contenido;
	
	public Casilla() {
		Contenido = VACIO;
	}
	
	public Casilla(char _c) {
		Contenido = _c;
	}
	
	public void setContenido(char e) {
		Contenido = e;
	}
	
	public char getContenido() {
		return Contenido;
	}
	
	public String toString() {
		return Contenido+"";
	}
	
	public boolean contiene(char e) {
		return Contenido==e;
	}
	
	public boolean esVacio() {
		return Contenido == VACIO;
	}
	
	public boolean esPared() {
		return Contenido == PARED;
	}
	
}
