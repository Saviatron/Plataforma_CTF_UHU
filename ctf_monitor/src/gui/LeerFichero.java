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


package gui;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * CLASE QUE LEE LA PARTIDA DESDE UN TXT
 */
public class LeerFichero {

	String archivo;

	public LeerFichero(String archivo) {
		this.archivo = archivo;
		leerContenido();
	}

	public String leerContenido() {
		String cadena;
		StringBuffer sb = new StringBuffer();
		try {
			FileReader f = new FileReader(archivo);
			BufferedReader b = new BufferedReader(f);
			while ((cadena = b.readLine()) != null) {
				sb.append(cadena + "\n");
			}
			b.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(sb.toString());
		return sb.toString();
	}
}