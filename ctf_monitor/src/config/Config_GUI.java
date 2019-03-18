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

import java.awt.Color;

public class Config_GUI {

	/////////////////////////////////////////
	// TABLERO
	/////////////////////////////////////////

	public static final char PARED = '#';
	public static final char VACIO = ' ';

	public static Color[] Colores = { new Color(255, 0, 0), new Color(0, 0, 255), new Color(255, 255, 0),
			new Color(0, 255, 0), Color.MAGENTA, Color.CYAN, Color.PINK, Color.DARK_GRAY };

	public static String[] Equipos = { "Rojo", "Azul", "Amarillo", "Verde", "Magenta", "Cian", "Rosa", "Gris" };

	/**
	 * ACCIONES
	 */
	public static final String ESTE = "E";
	public static final String OESTE = "W";
	public static final String NORTE = "N";
	public static final String SUR = "S";
	public static final String NE = "NE";
	public static final String NW = "NW";
	public static final String SE = "SE";
	public static final String SW = "SW";
	public static final String ABANDONAR = "ABANDONAR";
	public static final int GAME_OVER = 9;
	/////////////////////////////////////////
	// MONITOR
	/////////////////////////////////////////

	public static int NUM_EQUIPOS = 2;

	public static int ANCHO_CASILLA = 20;
	public static int ALTO_CASILLA = 20;

	public static float ESCALADO = 1;

	public static boolean SERVIDOR = Boolean.valueOf((Propiedades_GUI.getPropiedad("SERVIDOR")));
	public static boolean IS3D = Boolean.valueOf((Propiedades_GUI.getPropiedad("IS3D")));
	public static boolean SONIDO = Boolean.valueOf((Propiedades_GUI.getPropiedad("SOUND")));
	public static String partida = Propiedades_GUI.getPropiedad("PARTIDA");

	public static int CAMARA = IS3D ? 1 : 2;

}
