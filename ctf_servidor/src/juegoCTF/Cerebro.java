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
public class Cerebro {

	public EscribirFichero partidaTXT = new EscribirFichero("Partida");
	public Estadisticas estadisticas = new Estadisticas();

	private Tablero_Lineal tablero;

	private Posicion[] banderas = new Posicion[Config.NUM_EQUIPOS];
	private Posicion[] bases = new Posicion[Config.NUM_EQUIPOS];
	private Posicion[] entradas = new Posicion[Config.NUM_EQUIPOS];
	private ArrayList<Posicion> muertes = new ArrayList<Posicion>();

	@SuppressWarnings("unchecked")
	private ArrayList<Jugador>[] jugadores = (ArrayList<Jugador>[]) new ArrayList[Config.NUM_EQUIPOS];

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
		if (jugadores[Equipo].size() < Config.MAX_JUGADORES_EQ)
			jugadores[Equipo].add(j);
		else
			System.out.println("Num. MAXIMO de jugadores en equipo " + Equipo);
	}

	public boolean MaxJugadores(int Equipo, Jugador j) {
		if (jugadores[Equipo].size() < Config.MAX_JUGADORES_EQ) {
			return true;
		} else {
			System.out.println("Num. MAXIMO de jugadores para: " + j.getName());
		}
		return false;
	}

	public Jugador getJugador(AID aid) {
		List<Jugador> lista = new ArrayList<Jugador>();
		for (int i = 0; i < Config.NUM_EQUIPOS; i++)
			lista.addAll(jugadores[i]);

		Iterator<Jugador> i = lista.iterator();
		while (i.hasNext()) {
			Jugador j = i.next();
			if (aid.getName().equalsIgnoreCase(j.getName()))
				return j;
		}

		return null;
	}

	/*
	 * public Jugador getJugador(int equipo, AID aid) { List<Jugador> lista =
	 * null; switch (equipo) { case Config.EQUIPO_AZUL: lista = equipoAzul;
	 * break; case Config.EQUIPO_ROJO: lista = equipoRojo; default: return null;
	 * }
	 * 
	 * Iterator<Jugador> i = lista.iterator(); while (i.hasNext()) { Jugador j =
	 * i.next(); if (aid.getName().equalsIgnoreCase(j.getName())) return j; }
	 * 
	 * return null; }
	 */

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
		for (int i = 0; i < Config.NUM_EQUIPOS; i++)
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
			case Config.NORTE:
				Y -= 1;
				break;
			case Config.NE:
				X += 1;
				Y -= 1;
				break;
			case Config.ESTE:
				X += 1;
				break;
			case Config.SE:
				X += 1;
				Y += 1;
				break;
			case Config.SUR:
				Y += 1;
				break;
			case Config.SW:
				X -= 1;
				Y += 1;
				break;
			case Config.OESTE:
				X -= 1;
				break;
			case Config.NW:
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

		/*
		 * char[] m = tablero.TableroCompleto(); int ANCHOX = tablero.getDX();
		 * 
		 * for (int i = 0; i < Config.NUM_EQUIPOS; i++)
		 * rellenarJugadores(jugadores[i], m);
		 * 
		 * StringBuffer sb = new StringBuffer(); for (int j = 0; j <
		 * tablero.getDY(); j++) { for (int i = 0; i < tablero.getDX(); i++)
		 * sb.append(m[i + ANCHOX * j]); sb.append('\n'); } return
		 * sb.toString();
		 */

	}

	/*
	 * public String TableroParcial(Posicion pos) {
	 * 
	 * int dx = Config.VENTANAX / 2; int dy = Config.VENTANAY / 2;
	 * 
	 * int esqx = pos.getX() - dx; int esqy = pos.getY() - dy;
	 * 
	 * char[] m = tablero.TableroParcial(esqx, esqy, Config.VENTANAX,
	 * Config.VENTANAY); rellenarVentana(equipoAzul, m, esqx, esqy, esqx +
	 * Config.VENTANAX, esqy + Config.VENTANAY); rellenarVentana(equipoRojo, m,
	 * esqx, esqy, esqx + Config.VENTANAX, esqy + Config.VENTANAY);
	 * 
	 * StringBuffer sb = new StringBuffer(); sb.append(pos.getX());
	 * sb.append(","); sb.append(pos.getY()); sb.append(",");
	 * 
	 * for (int i = 0; i < m.length; i++) sb.append(m[i]);
	 * 
	 * return sb.toString(); }
	 */

	/*
	 * private void rellenarVentana(List<Jugador> equipo, char[] mapa, int x1,
	 * int y1, int x2, int y2) {
	 * 
	 * // AQUI HAY QYE METERLE LOS JUGADORES Iterator<Jugador> it =
	 * equipo.iterator(); while (it.hasNext()) { Jugador j = it.next();
	 * 
	 * int jx = j.getPosicion().getX(); int jy = j.getPosicion().getY();
	 * 
	 * // SI ESTA DENTRO DE LA VENTANA if (jx >= x1 && jx <= x2 && jy >= y1 &&
	 * jy <= y2) { int indice = (jx - x1) + (jy - y1) * Config.VENTANAX; if
	 * (j.isTieneBanderaContraria()) mapa[indice] =
	 * Config.JUGADOR_AZUL_BANDERACONT; else if (j.isTieneBanderaPropia())
	 * mapa[indice] = Config.JUGADOR_AZUL_BANDERAPROP; else mapa[indice] =
	 * Config.JUGADOR_AZUL; } }
	 * 
	 * }
	 */

	/*
	 * public String TableroCompleto1() { return
	 * tablero.enviarTableroCompleto(); }
	 */

	/*
	 * public String PintarVentana(Posicion pos) {
	 * 
	 * int dx = pos.getX() - Config.VENTANAX / 2; int dy = pos.getY() -
	 * Config.VENTANAY / 2;
	 * 
	 * char[] m = tablero.enviarTableroParcial(dx, dy, Config.VENTANAX,
	 * Config.VENTANAY).toCharArray();
	 * 
	 * int ANCHOX = tablero.getDX();
	 * 
	 * m[baseB.getX() + ANCHOX * baseB.getY()] = Config.BASE_AZUL;
	 * m[baseR.getX() + ANCHOX * baseR.getY()] = Config.BASE_ROJA;
	 * m[banderaB.getX() + ANCHOX * banderaB.getY()] = Config.BANDERA_AZUL;
	 * m[banderaR.getX() + ANCHOX * banderaR.getY()] = Config.BANDERA_ROJA;
	 * 
	 * rellenarMapa(equipoAzul, m); rellenarMapa(equipoRojo, m);
	 * 
	 * StringBuffer sb = new StringBuffer(); for (int j = 0; j <
	 * Config.VENTANAY; j++) { for (int i = 0; i < Config.VENTANAX; i++)
	 * sb.append(m[(dx + i) + ANCHOX * (j + dy)]); sb.append('\n'); } return
	 * sb.toString();
	 * 
	 * }
	 */

	public String getVERSION() {
		return Config.VERSION;
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

	/*
	 * public String SoloMapa() { return tablero.TableroCompleto().toString(); }
	 */

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

	/*
	 * public String EnviarTableroaMonitor() {
	 * 
	 * char[] m = tablero.TableroCompleto();
	 * 
	 * int ANCHOX = tablero.getDX();
	 * 
	 * m[baseA.getX() + ANCHOX * baseA.getY()] = Config.BASE_AZUL;
	 * m[baseR.getX() + ANCHOX * baseR.getY()] = Config.BASE_ROJA;
	 * m[banderaA.getX() + ANCHOX * banderaA.getY()] = Config.BANDERA_AZUL;
	 * m[banderaR.getX() + ANCHOX * banderaR.getY()] = Config.BANDERA_ROJA;
	 * 
	 * 
	 * rellenarMuertes(m); rellenarBaseBandera(m); for (int i = 0; i <
	 * Config.MAX_EQUIPOS; i++) rellenarJugadores(jugadores.get(i), m);
	 * 
	 * 
	 * int fdf = 0; for (int i = 0; i < tablero.getDY(); i++) { for (int j = 0;
	 * j < tablero.getDX(); j++) { System.out.print(m[fdf]); fdf++; }
	 * System.out.println(); } System.out.println();
	 * 
	 * 
	 * StringBuffer sb = new StringBuffer(); for (int i = 0; i < m.length; i++)
	 * sb.append(m[i]);
	 * 
	 * return sb.toString(); }
	 */

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
		sb.append(Config.NUM_EQUIPOS);
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

		sb.append(tablero.getDX() + ",");
		sb.append(tablero.getDY() + ",");

		sb.append(jug.getPosicion().getX() + ",");
		sb.append(jug.getPosicion().getY() + ",");

		sb.append(Config.NUM_EQUIPOS + ",");

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

	public String ActualizarTableroaJugador(Jugador jug) {

		// "[POS_X],[POS_Y]\n[linea1]\n..."
		StringBuffer sb = new StringBuffer();
		sb.append(jug.getPosicion().getX());
		sb.append(",");
		sb.append(jug.getPosicion().getY());
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

	/*
	 * public void rellenarJugadores(List<Jugador> equipo, char[] mapa) {
	 * 
	 * Iterator<Jugador> it = equipo.iterator(); while (it.hasNext()) { Jugador
	 * j = it.next(); int indice = j.getPosicion().getX() +
	 * j.getPosicion().getY() * tablero.getDX();
	 * 
	 * switch (j.getEquipo()) { case Config.EQUIPO_AZUL: if
	 * (j.isTieneBandera(Config.EQUIPO_AZUL)) mapa[indice] =
	 * Config.JUGADOR_AZUL_BANDERA_AZUL; else if
	 * (j.isTieneBandera(Config.EQUIPO_ROJO)) mapa[indice] =
	 * Config.JUGADOR_AZUL_BANDERA_ROJA; else if
	 * (j.isTieneBandera(Config.EQUIPO_VERDE)) mapa[indice] =
	 * Config.JUGADOR_AZUL_BANDERA_VERDE; else if
	 * (j.isTieneBandera(Config.EQUIPO_AMARILLO)) mapa[indice] =
	 * Config.JUGADOR_AZUL_BANDERA_AMARILLA; else mapa[indice] =
	 * Config.JUGADOR_AZUL; break; case Config.EQUIPO_ROJO: if
	 * (j.isTieneBandera(Config.EQUIPO_AZUL)) mapa[indice] =
	 * Config.JUGADOR_ROJO_BANDERA_AZUL; else if
	 * (j.isTieneBandera(Config.EQUIPO_ROJO)) mapa[indice] =
	 * Config.JUGADOR_ROJO_BANDERA_ROJA; else if
	 * (j.isTieneBandera(Config.EQUIPO_VERDE)) mapa[indice] =
	 * Config.JUGADOR_ROJO_BANDERA_VERDE; else if
	 * (j.isTieneBandera(Config.EQUIPO_AMARILLO)) mapa[indice] =
	 * Config.JUGADOR_ROJO_BANDERA_AMARILLA; else mapa[indice] =
	 * Config.JUGADOR_ROJO; break; case Config.EQUIPO_VERDE: if
	 * (j.isTieneBandera(Config.EQUIPO_AZUL)) mapa[indice] =
	 * Config.JUGADOR_VERDE_BANDERA_AZUL; else if
	 * (j.isTieneBandera(Config.EQUIPO_ROJO)) mapa[indice] =
	 * Config.JUGADOR_VERDE_BANDERA_ROJA; else if
	 * (j.isTieneBandera(Config.EQUIPO_VERDE)) mapa[indice] =
	 * Config.JUGADOR_VERDE_BANDERA_VERDE; else if
	 * (j.isTieneBandera(Config.EQUIPO_AMARILLO)) mapa[indice] =
	 * Config.JUGADOR_VERDE_BANDERA_AMARILLA; else mapa[indice] =
	 * Config.JUGADOR_VERDE; break; case Config.EQUIPO_AMARILLO: if
	 * (j.isTieneBandera(Config.EQUIPO_AZUL)) mapa[indice] =
	 * Config.JUGADOR_AMARILLO_BANDERA_AZUL; else if
	 * (j.isTieneBandera(Config.EQUIPO_ROJO)) mapa[indice] =
	 * Config.JUGADOR_AMARILLO_BANDERA_ROJA; else if
	 * (j.isTieneBandera(Config.EQUIPO_VERDE)) mapa[indice] =
	 * Config.JUGADOR_AMARILLO_BANDERA_VERDE; else if
	 * (j.isTieneBandera(Config.EQUIPO_AMARILLO)) mapa[indice] =
	 * Config.JUGADOR_AMARILLO_BANDERA_AMARILLA; else mapa[indice] =
	 * Config.JUGADOR_AMARILLO; break; default: mapa[indice] = Config.GAME_OVER;
	 * } }
	 * 
	 * }
	 * 
	 * private void rellenarBaseBandera(char[] mm) {
	 * 
	 * int ANCHOX = tablero.getDX();
	 * 
	 * for (int i = 0; i < Config.NUM_EQUIPOS; i++) { if
	 * (bases[i].equals(banderas[i])) { mm[bases[i].getX() + ANCHOX *
	 * bases[i].getY()] = Config.BASE_BANDERA_AZUL; } else { mm[bases[i].getX()
	 * + ANCHOX * bases[i].getY()] = Config.BASE_AZUL; mm[banderas[i].getX() +
	 * ANCHOX * banderas[i].getY()] = Config.BANDERA_AZUL; } }
	 * 
	 * }
	 */

	public void rellenarMuertes(char[] mapa) {

		Iterator<Posicion> it = muertes.iterator();
		while (it.hasNext()) {
			Posicion m = it.next();
			int indice = m.getX() + m.getY() * tablero.getDX();
			mapa[indice] = '*';
		}
		muertes.clear();
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
