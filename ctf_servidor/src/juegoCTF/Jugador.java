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

import juegoCTF.Posicion;

import java.util.Random;

import config.Config;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

/**
 * CLASE QUE MANEJA LA INFORMACION DE LOS JUGADORES
 */
public class Jugador {

	private AID id = null;
	private Posicion pos;
	private String orientacion = Config.NORTE;
	private int grados = 0;
	private boolean tieneBandera[] = new boolean[Config.NUM_EQUIPOS];
	private Accion accion = new Accion(0, Config.NORTE);
	private int equipo;
	private int Life;
	private int Strength;
	private int penalizacion = 0;
	private long tiempoVida = 0;
	private Random r = new Random(System.currentTimeMillis());

	// Se utiliza para desconectar al jugador en caso de inactividad
	private int ticksSinEnviarAccion = Config.TICKS;

	public Jugador(AID aid, int eq) {
		pos = new Posicion();
		id = aid;
		equipo = eq;
		Life = 100;
		Strength = r.nextInt(51) + 50;
	}

	public String getName() {
		return id.getName();
	}

	public String getLocalName() {
		return id.getLocalName();
	}

	public Posicion getPosicion() {
		return this.pos;
	}

	public void setPosicion(Posicion p) {
		// System.out.println("Se asigna posicion: "+p);
		this.pos = p;
	}

	public boolean isTieneBandera(int equipo) {
		return tieneBandera[equipo];
	}

	public boolean isTieneAlgunaBandera() {
		for (int i = 0; i < tieneBandera.length; i++) {
			if (tieneBandera[i])
				return true;
		}
		return false;
	}

	public void setTieneBandera(int equipo, boolean tieneBandera) {
		this.tieneBandera[equipo] = tieneBandera;
	}

	public void setNoTieneBandera() {
		tieneBandera = new boolean[Config.NUM_EQUIPOS];
	}

	public Accion getAccion() {
		return accion;
	}

	public void setAccion(Accion accion) {
		this.accion = accion;
		if (!accion.isIncorrecta() && !accion.isAbandonar() )
			if (config.Config.ORIENTACION_RELATIVA) {
				grados += accion.getGrados();
				while (grados < 0)
					grados = 360 - grados;
				while (grados >= 360)
					grados = grados % 360;
				switch (grados) {
				case 0:
					orientacion = Config.NORTE;
					break;
				case 45:
					orientacion = Config.NE;
					break;
				case 90:
					orientacion = Config.ESTE;
					break;
				case 135:
					orientacion = Config.SE;
					break;
				case 180:
					orientacion = Config.SUR;
					break;
				case 225:
					orientacion = Config.SW;
					break;
				case 270:
					orientacion = Config.OESTE;
					break;
				case 315:
					orientacion = Config.NW;
					break;
				default:
					System.err.println("NO SE RECONOCE ORIENTACION: " + grados);
					break;
				}
			} else {
				orientacion = accion.getOrientacion();
				grados = accion.getGrados();
			}
	}

	public String getOrientacion() {
		return orientacion;
	}

	public void setOrientacion(String orientacion) {
		this.orientacion = orientacion;
	}

	public int getEquipo() {
		return equipo;
	}

	public int getPenalizacion() {
		return penalizacion;
	}

	public void setPenalizacion(int penalizacion) {
		this.penalizacion = penalizacion;
	}

	public ACLMessage enviarMapa(String c) {
		ACLMessage m = new ACLMessage(ACLMessage.PROPOSE);
		m.setContent(c);
		m.addReceiver(this.id);

		return m;
	}

	public ACLMessage enviar(String c) {
		ACLMessage m = new ACLMessage(ACLMessage.INFORM);
		m.setContent(c);
		m.addReceiver(this.id);

		return m;
	}

	public void setTiempoVida(long tiempoVida) {
		this.tiempoVida = tiempoVida;
	}

	public long getTiempoVida() {
		return tiempoVida;
	}

	public boolean quedanTicks() {
		return ticksSinEnviarAccion > 0;
	}

	public void resetTicks() {
		ticksSinEnviarAccion = Config.TICKS;
	}

	public void decrementarTicks() {
		ticksSinEnviarAccion -= 1;
	}

	public void vaciarTicks() {
		ticksSinEnviarAccion -= 1;
	}

	public String toString() {
		return id.getLocalName();
	}

	public int getLife() {
		return Life;
	}

	public void setLife(int life) {
		Life = life;
	}

	public int getStrength() {
		return Strength;
	}

	public void setStrength(int strength) {
		Strength = strength;
	}

}
