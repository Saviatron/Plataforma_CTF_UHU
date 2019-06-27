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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EscribirFichero {
	BufferedWriter bw;
	String newFileName;
	Boolean fin = false;

	public EscribirFichero(String filename) {
//		File directionTemp = new File("./Partidas");
//		// Make Folder
//		if (!directionTemp.exists()) {
//			directionTemp.mkdirs();
//		}
		
		File aFile = new File(filename + ".txt");
		int fileNo = 0;
		newFileName = "";
		if (aFile.exists() && !aFile.isDirectory()) {

			// newFileName = filename.replaceAll(getFileExtension(filename), "("
			// + fileNo + ")" + getFileExtension(filename));

			while (aFile.exists()) {
				fileNo++;
				aFile = new File(filename + " (" + fileNo + ").txt");
				newFileName = filename + " (" + fileNo + ").txt";
			}

		} else if (!aFile.exists()) {
			try {
				aFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			newFileName = filename + ".txt";
		}

		try {
			this.bw = new BufferedWriter(new FileWriter(new File(newFileName)));
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void escribirPartida(String cadena) {
		if (!fin) {
			try {
				this.bw = new BufferedWriter(new FileWriter(new File(newFileName), true));
				bw.write(cadena + ";\n\n");
				bw.close();
			} catch (IOException e) {
				// e.printStackTrace();
				// System.err.println("No se ha escrito.");
			}
		}
	}

	public void escribirPartidaFin(String cadena) {
		fin = true;
		try {
			this.bw = new BufferedWriter(new FileWriter(new File(newFileName), true));
			bw.write(cadena + ".\n");
			bw.close();
		} catch (IOException e) {
			// e.printStackTrace();
			// System.err.println("No se ha escrito.");
		}
	}

	public void escribirEstadistica(String cadena) {
		try {
			this.bw = new BufferedWriter(new FileWriter(new File(newFileName), true));
			bw.write(cadena);
			bw.close();
		} catch (IOException e) {
			// e.printStackTrace();
			// System.err.println("No se ha escrito.");
		}
	}

	// public void cerrar() {
	// try {
	// bw.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

}