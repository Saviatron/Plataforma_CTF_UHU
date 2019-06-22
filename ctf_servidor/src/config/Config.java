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

	public Propiedades prop=new Propiedades();
	
	/**
	 * VERSION
	 */
	public static final String VERSION = "Captura la bandera 2019 - v9.0";
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
	public final String Mapa = prop.getPropiedad("MAPA");
	public final String Contenido = prop.getPropiedad("CONTENIDO");

	public final String NOMBRE_SERVICIO = prop.getPropiedad("NOMBRE_SERVICIO");
	public final String TIPO_SERVICIO = prop.getPropiedad("TIPO_SERVICIO");

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
	public static final String PIERDE = "PIERDE";
	public static final String GANA = "GANA";

	/**
	 * CONFIGURACION EXTERNA
	 */
	// TICKS PARA LA DESCONEXION
	public final int TICKS = Integer.parseInt(prop.getPropiedad("TICKS"));
	public final boolean DESCONEXION = Boolean.valueOf((prop.getPropiedad("DESCONEXION")));

	// NUM MAXIMO JUGADORES POR EQUIPO
	public final int MAX_JUGADORES_EQ = Integer.parseInt(prop.getPropiedad("MAX_JUGADORES_EQ"));

	// NUM MAXIMO DE EQUIPOS
	public final int NUM_EQUIPOS = Integer.parseInt(prop.getPropiedad("NUM_EQUIPOS"));

	// TIEMPO DE TICK (en ms)
	public final int TiempoTick = Integer.parseInt(prop.getPropiedad("TiempoTick"));
	
	// TICKS MAXIMOS
	public final int TicksMaximos = Integer.parseInt(prop.getPropiedad("TicksMaximos"));

	public double TASA_RECHAZO = Double.parseDouble(prop.getPropiedad("TASA_RECHAZO"));

	// ORIENTACION RELATIVA
	public boolean ORIENTACION_RELATIVA = Boolean.valueOf(prop.getPropiedad("ORIENTACION_RELATIVA"));

	// VISION PARCIAL
	public boolean VISION_PARCIAL = Boolean.valueOf(prop.getPropiedad("VISION_PARCIAL"));
	public int ALCANCE = Integer.parseInt(prop.getPropiedad("ALCANCE"));
	public int ANCHO = Integer.parseInt(prop.getPropiedad("ANCHO"));

}
