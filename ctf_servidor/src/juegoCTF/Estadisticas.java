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

import java.util.ArrayList;
import java.util.Iterator;

import config.Config;

class struct_jugador {
	public String nombre;
	public int muertes;
	public int ticks;

	public struct_jugador(String nombre) {
		this.nombre = nombre;
		this.muertes = 0;
		this.ticks = 0;
	}
};

class struct_muerte {
	public String jugador1;
	public String jugador2;

	public struct_muerte(String jugador1, String jugador2) {
		this.jugador1 = jugador1;
		this.jugador2 = jugador2;
	}
};

public class Estadisticas {

	@SuppressWarnings("unchecked")
	private ArrayList<struct_jugador>[] jugadores = (ArrayList<struct_jugador>[]) new ArrayList[Config.NUM_EQUIPOS];
	ArrayList<struct_muerte> muertes = new ArrayList<struct_muerte>();
	struct_jugador ganador = null;
	int eqganador = -1;
	EscribirFichero estadisticasTXT = new EscribirFichero("/var/www/opendatalab/html/aplicaciones/ctf2019/estadisticas/Estadisticas");

	public Estadisticas() {
		for (int i = 0; i < jugadores.length; i++) {
			jugadores[i] = new ArrayList<struct_jugador>();
		}

	}

	public boolean add_jugador(String jugador, int equipo) {
		Iterator<struct_jugador> i = jugadores[equipo].iterator();
		struct_jugador j;
		while (i.hasNext()) {
			j = i.next();
			if (j.nombre.equalsIgnoreCase(jugador))
				return true;
		}
		jugadores[equipo].add(new struct_jugador(jugador));
		return true;
	}

	public void add_muerte(String jugador1, int equipo1, String jugador2, int equipo2) {
		muertes.add(new struct_muerte(jugador1, jugador2));
		Iterator<struct_jugador> i = jugadores[equipo1].iterator();
		struct_jugador j;
		while (i.hasNext()) {
			j = i.next();
			if (j.nombre.equalsIgnoreCase(jugador1))
				j.muertes++;
		}

		i = jugadores[equipo2].iterator();
		while (i.hasNext()) {
			j = i.next();
			if (j.nombre.equalsIgnoreCase(jugador2))
				j.muertes++;
		}
	}

	public void add_tick(String jugador, int equipo) {
		Iterator<struct_jugador> i = jugadores[equipo].iterator();
		struct_jugador j;
		while (i.hasNext()) {
			j = i.next();
			if (j.nombre.equalsIgnoreCase(jugador))
				j.ticks++;
		}
	}

	public void ganador(String jugador, int equipo) {
		if (jugador == null || equipo < 0) {
			ganador = null;
			eqganador = -1;
		} else {
			ganador = new struct_jugador(jugador);
			eqganador = equipo;
		}
	}

	public void escribirEstadisticas() {
		estadisticasTXT.escribirEstadistica("ESTADISTICAS DE LA PARTIDA.\n");
		estadisticasTXT.escribirEstadistica("___________________________\n\n");

		estadisticasTXT.escribirEstadistica("Han jugado " + Config.NUM_EQUIPOS + " equipos.\n");
		estadisticasTXT.escribirEstadistica("\n******************************************************************\n");

		for (int equipo = 0; equipo < Config.NUM_EQUIPOS; equipo++) {
			int bajasequipo = 0;
			estadisticasTXT.escribirEstadistica("\n- Resumen equipo " + Config.Equipos[equipo] + ":\n\n");
			Iterator<struct_jugador> j = jugadores[equipo].iterator();
			while (j.hasNext()) {
				struct_jugador jugador = j.next();
				estadisticasTXT.escribirEstadistica("\tEl jugador " + jugador.nombre + ":\n");
				estadisticasTXT.escribirEstadistica("\t\tHa jugado " + jugador.ticks + " ticks.\n");
				estadisticasTXT.escribirEstadistica("\t\tHa muerto " + jugador.muertes + " veces.\n\n");
				bajasequipo += jugador.muertes;
			}
			estadisticasTXT.escribirEstadistica("\t- MUERTES DEL EQUIPO: " + bajasequipo + ".\n\n");
		}

		estadisticasTXT.escribirEstadistica("\n******************************************************************\n");

		estadisticasTXT.escribirEstadistica("- Resumen de todas las muertes:\n");
		Iterator<struct_muerte> i = muertes.iterator();
		while (i.hasNext()) {
			struct_muerte muerte = i.next();
			estadisticasTXT.escribirEstadistica(
					"\tHan muerto los jugadores: " + muerte.jugador1 + " y " + muerte.jugador2 + ".\n");
		}

		estadisticasTXT.escribirEstadistica("\n******************************************************************\n");

		if (ganador == null || eqganador == -1) {
			estadisticasTXT.escribirEstadistica("Juego terminado por limite de Ticks: "+Config.TicksMaximos);
		} else {
			estadisticasTXT.escribirEstadistica("\nHA GANADO EL EQUIPO: " + Config.Equipos[eqganador] + ".\n");
			estadisticasTXT.escribirEstadistica("\t Gracias al jugador: " + ganador.nombre + ".\n");
			estadisticasTXT.escribirEstadistica("\n\n//////// ENHORABUENA /////////");
		} // estadisticasTXT.cerrar();
	}
}
