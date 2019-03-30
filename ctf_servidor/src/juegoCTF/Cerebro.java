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

import jade.core.AID;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import config.Config;
import juegoCTF.Posicion;

/**
 * CLASE QUE GUARDA LA INFORMACION DE LA PARTIDA
 */
public class Cerebro implements Config {

	public EscribirFichero partidaTXT = new EscribirFichero("Partida");
	public Estadisticas estadisticas = new Estadisticas();

	private Tablero_Lineal tablero;

	private Posicion[] banderas = new Posicion[NUM_EQUIPOS];
	private Posicion[] bases = new Posicion[NUM_EQUIPOS];
	private Posicion[] entradas = new Posicion[NUM_EQUIPOS];
	private ArrayList<Posicion> muertes = new ArrayList<Posicion>();

	@SuppressWarnings("unchecked")
	private ArrayList<Jugador>[] jugadores = (ArrayList<Jugador>[]) new ArrayList[NUM_EQUIPOS];

	private List<AID> monitores = new ArrayList<AID>();

	private Hashtable<Jugador, Accion> listaAcciones = new Hashtable<Jugador, Accion>();

	/************************************************************
	 ****************************************** COSTRUCTOR
	 ***********************************************************/
	public Cerebro() {
		for (int i = 0; i < jugadores.length; i++) {
			jugadores[i] = new ArrayList<Jugador>();
		}

	}

	/************************************************************
	 ****************************************** METODOS PARSER
	 ***********************************************************/

	public void leerFicheroCTF(String mapa) {
		try {

			File fr = new File(mapa);
			if (fr.canRead()) {
				// TODO: No seria necesario?
				tablero = new Tablero_Lineal(new FileReader(mapa));

			} else {
				System.out.println("El mapa no se puede leer");
			}

		} catch (Exception e) {
			System.out.println("Error al leer el mapa");
			e.printStackTrace();

		}
	}

	public void contenido_parser(String contenido) {
		// Analisis del contenido del fichro .ini
		try {
			FileReader cont = new FileReader(contenido);
			BufferedReader bfs = new BufferedReader(cont);
			String linea;
			while ((linea = bfs.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(linea, ",");
				if (st.hasMoreTokens())
					switch (st.nextToken()) {
					case "Base":
						bases[Integer.parseInt(st.nextToken())] = new Posicion(Integer.parseInt(st.nextToken()),
								Integer.parseInt(st.nextToken()));
						break;
					case "Bandera":
						banderas[Integer.parseInt(st.nextToken())] = new Posicion(Integer.parseInt(st.nextToken()),
								Integer.parseInt(st.nextToken()));
						break;
					case "Entrada":
						entradas[Integer.parseInt(st.nextToken())] = new Posicion(Integer.parseInt(st.nextToken()),
								Integer.parseInt(st.nextToken()));
						break;
					case "Jugador":
						bases[Integer.parseInt(st.nextToken())] = new Posicion(Integer.parseInt(st.nextToken()),
								Integer.parseInt(st.nextToken()));
						break;
					case "//":
						break;
					default:
						System.err.println("No se reconoce tokken");
						break;
					}
			}
			bfs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/************************************************************
	 ****************************************** METODOS JUGADORES
	 ***********************************************************/

	public void addJugador(int Equipo, Jugador j) {
		if (jugadores[Equipo].size() < MAX_JUGADORES_EQ)
			jugadores[Equipo].add(j);
		else
			System.out.println("Num. MAXIMO de jugadores en equipo " + Equipo);
	}

	public boolean MaxJugadores(int Equipo, Jugador j) {
		if (jugadores[Equipo].size() < MAX_JUGADORES_EQ) {
			return true;
		} else {
			System.out.println("Num. MAXIMO de jugadores para: " + j.getName());
		}
		return false;
	}

	public Jugador getJugador(AID aid) {
		List<Jugador> lista = new ArrayList<Jugador>();
		for (int i = 0; i < NUM_EQUIPOS; i++)
			lista.addAll(jugadores[i]);

		Iterator<Jugador> i = lista.iterator();
		while (i.hasNext()) {
			Jugador j = i.next();
			if (aid.getName().equalsIgnoreCase(j.getName()))
				return j;
		}

		return null;
	}

	public Jugador getJugador(int equipo, Posicion pos) {
		List<Jugador> lista = jugadores[equipo];
		Iterator<Jugador> i = lista.iterator();
		while (i.hasNext()) {
			Jugador j = i.next();
			if (pos.equals(j.getPosicion())) {
				return j;
			}
		}

		return null;
	}

	public Jugador getEnemigo(int equipo, Posicion pos) {
		List<Jugador> lista = new ArrayList<Jugador>();
		for (int i = 0; i < NUM_EQUIPOS; i++)
			if (i != equipo)
				lista.addAll(jugadores[i]);

		Iterator<Jugador> i = lista.iterator();
		while (i.hasNext()) {
			Jugador j = i.next();
			if (pos.equals(j.getPosicion())) {
				return j;
			}
		}
		return null;
	}

	public void removeJugador(int equipo, Jugador j) {
		jugadores[equipo].remove(j);
	}

	public void removeJugador(int equipo, String nombre) {
		jugadores[equipo].removeIf(j -> j.getName() == nombre);
	}

	public List<Jugador> getEquipo(int equipo) {
		return jugadores[equipo];
	}

	public boolean asignarPosicionEntrada(Jugador j) {
		boolean devolver = true;
		Jugador conflicto;

		if (entradas[j.getEquipo()] != null) {
			conflicto = getJugador(j.getEquipo(), entradas[j.getEquipo()]);
			devolver = conflicto == null;
			j.setPosicion(entradas[j.getEquipo()]);

		} else {
			Random r = new Random(System.currentTimeMillis());
			int x = -1;
			int y = -1;
			Posicion pos = new Posicion();
			do {
				x = r.nextInt(tablero.getDX());
				y = r.nextInt(tablero.getDY());
				pos.setXY(x, y);
			} while (!tablero.esVacio(pos));
			j.setPosicion(pos);

		}
		return devolver;
	}

	public Posicion calcularPos(Jugador j) {
		int X = j.getPosicion().getX();
		int Y = j.getPosicion().getY();
		if (!j.getAccion().isNula() && !j.getAccion().isIncorrecta() && !j.getAccion().isAbandonar())
			switch (j.getOrientacion()) {
			case NORTE:
				Y -= 1;
				break;
			case NE:
				X += 1;
				Y -= 1;
				break;
			case ESTE:
				X += 1;
				break;
			case SE:
				X += 1;
				Y += 1;
				break;
			case SUR:
				Y += 1;
				break;
			case SW:
				X -= 1;
				Y += 1;
				break;
			case OESTE:
				X -= 1;
				break;
			case NW:
				X -= 1;
				Y -= 1;
				break;
			}
		return new Posicion(X, Y);
	}

	/************************************************************
	 ****************************************** METODOS TABLERO
	 ***********************************************************/

	public void addMuerte(Posicion muerte) {
		// if(!muertes.contains(muerte))
		muertes.add(muerte);
	}

	public void removeMuerte(Posicion muerte) {
		muertes.remove(muerte);
	}

	public Posicion getBase(int equipo) {
		return bases[equipo];
	}

	public int getBaseContrariaEn(int equipo, Posicion pos) {
		for (int i = 0; i < bases.length; i++)
			if (pos.equals(bases[i]) && i != equipo)
				return i;
		return -1;
	}

	public Posicion getBandera(int equipo) {
		return banderas[equipo];
	}

	public int getBanderaContrariaEn(int equipo, Posicion pos) {
		for (int i = 0; i < banderas.length; i++)
			if (pos.equals(banderas[i]) && i != equipo)
				return i;
		return -1;
	}

	public void setBandera(int equipo, Posicion p) {
		banderas[equipo] = p;
	}

	public String toString() {
		return null;

	}

	public String getVERSION() {
		return VERSION;
	}

	public void setXXVERSION(String vERSION) {
		// VERSION = vERSION;
	}

	public int DimX() {
		return tablero.getDX();
	}

	public int DimY() {
		return tablero.getDY();
	}

	/************************************************************
	 ****************************************** METODOS ACCIONES
	 ***********************************************************/

	public void addAccion(Jugador _jug) {
		// listaAcciones.remove(_jug);
		if (!listaAcciones.containsKey(_jug)) {
			listaAcciones.put(_jug, _jug.getAccion());
		}

	}

	public void removeAccion(Jugador _jug) {
		listaAcciones.remove(_jug);

	}

	public Hashtable<Jugador, Accion> getListaAcciones() {
		return listaAcciones;
	}

	/************************************************************
	 ************************************* METODOS ENVIAR TABLERO
	 ***********************************************************/

	public String EnviarTableroaMonitor() {
		// TODO

		// "[VERSION],[DIM_X],[DIM_Y],[NUM_EQ],[MAPA] \n [linea1] \n..."

		StringBuffer sb = new StringBuffer();
		sb.append(getVERSION());
		sb.append(",");
		sb.append(DimX());
		sb.append(",");
		sb.append(DimY());
		sb.append(",");
		sb.append(NUM_EQUIPOS);
		sb.append(",");
		sb.append(tablero.TableroCompleto());
		sb.append("\n");

		for (int i = 0; i < entradas.length; i++) {
			if (entradas[i] != null) {
				sb.append("Entrada");
				sb.append(",");
				sb.append(i);
				sb.append(",");
				sb.append(entradas[i].getX());
				sb.append(",");
				sb.append(entradas[i].getY());
				sb.append("\n");
			}
		}
		for (int i = 0; i < banderas.length; i++) {
			if (banderas[i] != null) {
				sb.append("Bandera");
				sb.append(",");
				sb.append(i);
				sb.append(",");
				sb.append(banderas[i].getX());
				sb.append(",");
				sb.append(banderas[i].getY());
				sb.append("\n");
			}
		}
		for (int i = 0; i < bases.length; i++) {
			if (bases[i] != null) {
				sb.append("Base");
				sb.append(",");
				sb.append(i);
				sb.append(",");
				sb.append(bases[i].getX());
				sb.append(",");
				sb.append(bases[i].getY());
				sb.append("\n");
			}
		}
		for (int i = 0; i < jugadores.length; i++) {
			if (jugadores[i] != null) {
				for (int j = 0; j < jugadores[i].size(); j++) {
					sb.append("Jugador");
					sb.append(",");
					sb.append(i);
					sb.append(",");
					sb.append(jugadores[i].get(j).getPosicion().getX());
					sb.append(",");
					sb.append(jugadores[i].get(j).getPosicion().getY());
					// sb.append(",");
					// sb.append(jugadores[i].get(j).getAccion());
					sb.append("\n");
				}
			}
		}
		for (int i = 0; i < muertes.size(); i++) {
			sb.append("Muerte");
			sb.append(",");
			sb.append(muertes.get(i).getX());
			sb.append(",");
			sb.append(muertes.get(i).getY());
			sb.append("\n");
		}

		return sb.toString();
	}

	public String ActualizarTableroaMonitor() {

		// " [linea1] \n [linea2] \n ..."
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < entradas.length; i++) {
			if (entradas[i] != null) {
				sb.append("Entrada");
				sb.append(",");
				sb.append(i);
				sb.append(",");
				sb.append(entradas[i].getX());
				sb.append(",");
				sb.append(entradas[i].getY());
				sb.append("\n");
			}
		}
		for (int i = 0; i < banderas.length; i++) {
			if (banderas[i] != null) {
				sb.append("Bandera");
				sb.append(",");
				sb.append(i);
				sb.append(",");
				sb.append(banderas[i].getX());
				sb.append(",");
				sb.append(banderas[i].getY());
				sb.append("\n");
			}
		}
		for (int i = 0; i < bases.length; i++) {
			if (bases[i] != null) {
				sb.append("Base");
				sb.append(",");
				sb.append(i);
				sb.append(",");
				sb.append(bases[i].getX());
				sb.append(",");
				sb.append(bases[i].getY());
				sb.append("\n");
			}
		}
		for (int i = 0; i < jugadores.length; i++) {
			if (jugadores[i] != null) {
				for (int j = 0; j < jugadores[i].size(); j++) {
					sb.append("Jugador");
					sb.append(",");
					sb.append(jugadores[i].get(j).getLocalName());
					sb.append(",");
					sb.append(i);
					sb.append(",");
					sb.append(jugadores[i].get(j).getPosicion().getX());
					sb.append(",");
					sb.append(jugadores[i].get(j).getPosicion().getY());
					sb.append(",");
					sb.append(jugadores[i].get(j).getOrientacion());
					sb.append("\n");
				}
			}
		}
		for (int i = 0; i < muertes.size(); i++) {
			sb.append("Muerte");
			sb.append(",");
			sb.append(muertes.get(i).getX());
			sb.append(",");
			sb.append(muertes.get(i).getY());
			sb.append("\n");
			muertes.remove(i);
		}

		return sb.toString();
	}

	public String EnviarTableroaJugador(Jugador jug) {

		// "[ANCHO_MAPA],[ALTO_MAPA],[POS_X],[POS_Y],[NUM_EQ],[MAPA]\n[linea1]\n..."

		StringBuffer sb = new StringBuffer();

		sb.append(ORIENTACION_RELATIVA + ",");
		sb.append(VISION_PARCIAL + ",");

		sb.append(tablero.getDX() + ",");
		sb.append(tablero.getDY() + ",");

		sb.append(jug.getPosicion().getX() + ",");
		sb.append(jug.getPosicion().getY() + ",");

		sb.append(NUM_EQUIPOS + ",");
		if (!VISION_PARCIAL) {

			sb.append(tablero.TableroCompleto());

			sb.append("\n");

			for (int i = 0; i < entradas.length; i++) {
				if (entradas[i] != null) {
					sb.append("Entrada");
					sb.append(",");
					sb.append(i);
					sb.append(",");
					sb.append(entradas[i].getX());
					sb.append(",");
					sb.append(entradas[i].getY());
					sb.append("\n");
				}
			}
			for (int i = 0; i < banderas.length; i++) {
				if (banderas[i] != null) {
					sb.append("Bandera");
					sb.append(",");
					sb.append(i);
					sb.append(",");
					sb.append(banderas[i].getX());
					sb.append(",");
					sb.append(banderas[i].getY());
					sb.append("\n");
				}
			}
			for (int i = 0; i < bases.length; i++) {
				if (bases[i] != null) {
					sb.append("Base");
					sb.append(",");
					sb.append(i);
					sb.append(",");
					sb.append(bases[i].getX());
					sb.append(",");
					sb.append(bases[i].getY());
					sb.append("\n");
				}
			}
			for (int i = 0; i < jugadores.length; i++) {
				if (jugadores[i] != null) {
					for (int j = 0; j < jugadores[i].size(); j++) {
						sb.append("Jugador");
						sb.append(",");
						sb.append(i);
						sb.append(",");
						sb.append(jugadores[i].get(j).getPosicion().getX());
						sb.append(",");
						sb.append(jugadores[i].get(j).getPosicion().getY());
						// sb.append(",");
						// sb.append(jugadores[i].get(j).getAccion());
						sb.append("\n");
					}
				}
			}
			for (int i = 0; i < muertes.size(); i++) {
				sb.append("Muerte");
				sb.append(",");
				sb.append(muertes.get(i).getX());
				sb.append(",");
				sb.append(muertes.get(i).getY());
				sb.append("\n");
			}
		} else {
			sb.append(mapaParcial(jug));

			sb.append("\n");
		}

		return sb.toString();
	}

	public String ActualizarTableroaJugador(Jugador jug) {

		// "[POS_X],[POS_Y]\n[linea1]\n..."
		StringBuffer sb = new StringBuffer();
		sb.append(jug.getPosicion().getX());
		sb.append(",");
		sb.append(jug.getPosicion().getY());
		if (!VISION_PARCIAL) {
			sb.append("\n");
			for (int i = 0; i < entradas.length; i++) {
				if (entradas[i] != null) {
					sb.append("Entrada");
					sb.append(",");
					sb.append(i);
					sb.append(",");
					sb.append(entradas[i].getX());
					sb.append(",");
					sb.append(entradas[i].getY());
					sb.append("\n");
				}
			}
			for (int i = 0; i < banderas.length; i++) {
				if (banderas[i] != null) {
					sb.append("Bandera");
					sb.append(",");
					sb.append(i);
					sb.append(",");
					sb.append(banderas[i].getX());
					sb.append(",");
					sb.append(banderas[i].getY());
					sb.append("\n");
				}
			}
			for (int i = 0; i < bases.length; i++) {
				if (bases[i] != null) {
					sb.append("Base");
					sb.append(",");
					sb.append(i);
					sb.append(",");
					sb.append(bases[i].getX());
					sb.append(",");
					sb.append(bases[i].getY());
					sb.append("\n");
				}
			}
			for (int i = 0; i < jugadores.length; i++) {
				if (jugadores[i] != null) {
					for (int j = 0; j < jugadores[i].size(); j++) {
						sb.append("Jugador");
						sb.append(",");
						sb.append(i);
						sb.append(",");
						sb.append(jugadores[i].get(j).getPosicion().getX());
						sb.append(",");
						sb.append(jugadores[i].get(j).getPosicion().getY());
						// sb.append(",");
						// sb.append(jugadores[i].get(j).getAccion());
						sb.append("\n");
					}
				}
			}
			for (int i = 0; i < muertes.size(); i++) {
				sb.append("Muerte");
				sb.append(",");
				sb.append(muertes.get(i).getX());
				sb.append(",");
				sb.append(muertes.get(i).getY());
				sb.append("\n");
			}
		} else {
			sb.append("," + mapaParcial(jug));
			sb.append("\n");
		}

		return sb.toString();
	}

	private String mapaParcial(Jugador jug) {
		int ancho = (ANCHO / 2);
		int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
		switch (jug.getOrientacion()) {
		case NORTE:
			x1 = jug.getPosicion().getX() - ancho;
			x2 = jug.getPosicion().getX() + ancho;
			y1 = jug.getPosicion().getY() - ALCANCE;
			y2 = jug.getPosicion().getY();
			break;
		case NE:
			x1 = jug.getPosicion().getX();
			x2 = jug.getPosicion().getX() + ANCHO;
			y1 = jug.getPosicion().getY() - ANCHO;
			y2 = jug.getPosicion().getY();
			break;
		case ESTE:
			x1 = jug.getPosicion().getX();
			x2 = jug.getPosicion().getX() + ALCANCE;
			y1 = jug.getPosicion().getY() - ancho;
			y2 = jug.getPosicion().getY() + ancho;
			break;
		case SE:
			x1 = jug.getPosicion().getX();
			x2 = jug.getPosicion().getX() + ANCHO;
			y1 = jug.getPosicion().getY();
			y2 = jug.getPosicion().getY() + ALCANCE;
			break;
		case SUR:
			x1 = jug.getPosicion().getX() - ancho;
			x2 = jug.getPosicion().getX() + ancho;
			y1 = jug.getPosicion().getY();
			y2 = jug.getPosicion().getY() + ALCANCE;
			break;
		case SW:
			x1 = jug.getPosicion().getX() - ANCHO;
			x2 = jug.getPosicion().getX();
			y1 = jug.getPosicion().getY();
			y2 = jug.getPosicion().getY() + ALCANCE;
			break;
		case OESTE:
			x1 = jug.getPosicion().getX() - ALCANCE;
			x2 = jug.getPosicion().getX();
			y1 = jug.getPosicion().getY() - ancho;
			y2 = jug.getPosicion().getY() + ancho;
			break;
		case NW:
			x1 = jug.getPosicion().getX() - ANCHO;
			x2 = jug.getPosicion().getX();
			y1 = jug.getPosicion().getY() - ANCHO;
			y2 = jug.getPosicion().getY();
			break;
		}
		x1 = (x1 < 0) ? 0 : ((x1 > tablero.getDX() - 1) ? tablero.getDX() - 1 : x1);
		x2 = (x2 < 0) ? 0 : ((x2 > tablero.getDX() - 1) ? tablero.getDX() - 1 : x2);
		y1 = (y1 < 0) ? 0 : ((y1 > tablero.getDY() - 1) ? tablero.getDY() - 1 : y1);
		y2 = (y2 < 0) ? 0 : ((y2 > tablero.getDY() - 1) ? tablero.getDY() - 1 : y2);

		String s = "";
		s += x1 + "," + x2 + "," + y1 + "," + y2 + ",";
		for (int j = y1; j <= y2; j++)
			for (int i = x1; i <= x2; i++)
				s += tablero.getContenido(i, j);
		s += "\n";
		for (int i = 0; i < entradas.length; i++)
			if (entradas[i] != null && entradas[i].getX() >= x1 && entradas[i].getX() <= x2 && entradas[i].getY() >= y1
					&& entradas[i].getY() <= y2)
				s += "Entrada" + "," + i + "," + entradas[i].getX() + "," + entradas[i].getY() + "\n";

		for (int i = 0; i < banderas.length; i++)
			if (banderas[i] != null && banderas[i].getX() >= x1 && banderas[i].getX() <= x2 && banderas[i].getY() >= y1
					&& banderas[i].getY() <= y2)
				s += "Bandera" + "," + i + "," + banderas[i].getX() + "," + banderas[i].getY() + "\n";

		for (int i = 0; i < bases.length; i++)
			if (bases[i] != null && bases[i].getX() >= x1 && bases[i].getX() <= x2 && bases[i].getY() >= y1
					&& bases[i].getY() <= y2)
				s += "Base" + "," + i + "," + bases[i].getX() + "," + bases[i].getY() + "\n";

		for (int i = 0; i < jugadores.length; i++)
			if (jugadores[i] != null)
				for (int j = 0; j < jugadores[i].size(); j++)
					if (jugadores[i].get(j) != null && jugadores[i].get(j).getPosicion().getX() >= x1
							&& jugadores[i].get(j).getPosicion().getX() <= x2
							&& jugadores[i].get(j).getPosicion().getY() >= y1
							&& jugadores[i].get(j).getPosicion().getY() <= y2)
						s += "Jugador" + "," + i + "," + jugadores[i].get(j).getPosicion().getX() + ","
								+ jugadores[i].get(j).getPosicion().getY() + "\n";

		for (int i = 0; i < muertes.size(); i++)
			if (muertes.get(i).getX() >= x1 && muertes.get(i).getX() <= x2 && muertes.get(i).getY() >= y1
					&& muertes.get(i).getY() <= y2)
				s += "Muerte" + "," + muertes.get(i).getX() + "," + muertes.get(i).getY() + "\n";
		return s;
	}

	/************************************************************
	 ****************************************** METODOS MONITORES
	 ***********************************************************/

	public void addMonitor(AID mon) {
		monitores.add(mon);
	}

	public List<AID> getMonitores() {
		return monitores;
	}

	public AID getMonitor(AID aid) {
		Iterator<AID> i = monitores.iterator();
		while (i.hasNext()) {
			AID mon = i.next();
			if (aid.getName().equalsIgnoreCase(mon.getName()))
				return mon;
		}

		return null;
	}

	public void removeMonitor(AID mon) {
		monitores.remove(mon);
	}

	public boolean esPared(Posicion pos) {
		return tablero.esPared(pos);
	}
}
