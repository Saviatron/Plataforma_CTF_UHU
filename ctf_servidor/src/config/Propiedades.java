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

package config;

import java.util.Properties;
import java.util.HashMap;
import java.io.FileInputStream;

/**
 * CLASE QUE LEE LA CONFIGURACION DEL FICHERO CONFIG
 */
public class Propiedades {

	private static final String CONFIGURATION_FILE = "./servidorConfig.cfg";

	private static HashMap<Object, Object> propiedades;

	static {
		try {
			FileInputStream f = new FileInputStream(CONFIGURATION_FILE);
			Properties propiedadesTemporales = new Properties();
			propiedadesTemporales.load(f);
			f.close();
			propiedades = new HashMap<Object, Object>(propiedadesTemporales);
		} catch (Exception e) {
		}
	}

	private Propiedades() {
	}

	public static String getPropiedad(String nombre) {
		String valor = (String) propiedades.get(nombre);
		if (valor == null)
			System.err.println("No existe la propiedad: "+ nombre);
		return valor;
	}

}
