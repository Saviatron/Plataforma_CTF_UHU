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

public interface Config {

	/**
	 * VERSION
	 */
	public static final String VERSION = "Captura la bandera 2019 - v6.1";
	public static final String PSW = "CTF_2019";

	/**
	 * TABLERO
	 */
	public static final char PARED = '#';
	public static final char VACIO = ' ';
	public static String[] Equipos = { "Rojo", "Azul", "Amarillo", "Verde", "Magenta", "Cian", "Rosa", "Gris" };

	/**
	 * CONFIGURACION INTERNA
	 */
	public static final String MapaPath = "mapas" + System.getProperty("file.separator");
	public static final String MapaDefecto = "blanco.txt";
	public static final String ContenidoDefecto = "blanco_2.ini";
	public static final String Mapa = Propiedades.getPropiedad("MAPA");
	public static final String Contenido = Propiedades.getPropiedad("CONTENIDO");

	public static final String NOMBRE_SERVICIO = Propiedades.getPropiedad("NOMBRE_SERVICIO");
	public static final String TIPO_SERVICIO = Propiedades.getPropiedad("TIPO_SERVICIO");

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
	public static final String ADELANTE = "ADELANTE";
	public static final String ATRAS = "ATRAS";
	public static final String ROTAR = "ROTAR";
	public static final String ABANDONAR = "ABANDONAR";
	public static final String NULA = "NULA";
	public static final int GAME_OVER = 9;

	/**
	 * CONFIGURACION EXTERNA
	 */
	// TICKS PARA LA DESCONEXION
	public static final int TICKS = Integer.parseInt(Propiedades.getPropiedad("TICKS"));
	public static final boolean DESCONEXION = Boolean.valueOf((Propiedades.getPropiedad("DESCONEXION")));

	// NUM MAXIMO JUGADORES POR EQUIPO
	public static final int MAX_JUGADORES_EQ = Integer.parseInt(Propiedades.getPropiedad("MAX_JUGADORES_EQ"));

	// NUM MAXIMO DE EQUIPOS
	public static final int NUM_EQUIPOS = Integer.parseInt(Propiedades.getPropiedad("NUM_EQUIPOS"));

	// TIEMPO DE TICK (en ms)
	public static final int TiempoTick = Integer.parseInt(Propiedades.getPropiedad("TiempoTick"));

	public static double TASA_RECHAZO = Double.parseDouble(Propiedades.getPropiedad("TASA_RECHAZO"));

	// ORIENTACION RELATIVA
	public static boolean ORIENTACION_RELATIVA = Boolean.valueOf(Propiedades.getPropiedad("ORIENTACION_RELATIVA"));

	// VISION PARCIAL
	public static boolean VISION_PARCIAL = Boolean.valueOf(Propiedades.getPropiedad("VISION_PARCIAL"));
	public static int ALCANCE = Integer.parseInt(Propiedades.getPropiedad("ALCANCE"));
	public static int ANCHO = Integer.parseInt(Propiedades.getPropiedad("ANCHO"));

}
