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

import java.util.ArrayList;
import config.Config_GUI;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

/**
 * CLASE QUE ADMINISTRA EL CONTENIDO DE LAS CASILLAS DEL MAPA
 */
public class Casilla {

	private PImage tex;
	private boolean suelo;
	private boolean pared;
	private int jugador;
	private String orientacion;
	private int bandera;
	private int base;
	private int entrada;
	private boolean muerte;
	PVector v1;
	PVector v2;
	PVector v3;
	PVector v4;
	private GUI_Mapa parent;
	private ArrayList<Particula> particList = new ArrayList<Particula>();

	public Casilla(boolean pared, int x, int y, int ancho, int alto, GUI_Mapa parent) {
		this.parent = parent;
		this.pared = pared;
		this.suelo = !pared;
		this.jugador = -1;
		this.base = -1;
		this.bandera = -1;
		this.entrada = -1;
		this.orientacion = "";
		this.muerte = false;

		v1 = new PVector(x, y);
		v2 = new PVector(x + ancho, y);
		v3 = new PVector(x + ancho, y + alto);
		v4 = new PVector(x, y + alto);

		pintaCasilla3D();
	}

	public void display3D() {
		pintaCasilla3D();
		parent.lights();

		if (pared) {
			parent.strokeWeight(1);
			parent.stroke(0);
			parent.beginShape(PConstants.QUADS);
			parent.texture(tex);

			// +Z "front" face
			parent.vertex(v2.x, v2.y, Config_GUI.ANCHO_CASILLA, 0, 0);
			parent.vertex(v1.x, v1.y, Config_GUI.ANCHO_CASILLA, 20, 0);
			parent.vertex(v4.x, v4.y, Config_GUI.ANCHO_CASILLA, 20, 20);
			parent.vertex(v3.x, v3.y, Config_GUI.ANCHO_CASILLA, 0, 20);

			// -Z "back" face
			parent.vertex(v1.x, v1.y, 0, 0, 0);
			parent.vertex(v2.x, v2.y, 0, 20, 0);
			parent.vertex(v3.x, v3.y, 0, 20, 20);
			parent.vertex(v4.x, v4.y, 0, 0, 20);

			// +Y "bottom" face
			parent.vertex(v3.x, v3.y, Config_GUI.ANCHO_CASILLA, 0, 0);
			parent.vertex(v4.x, v4.y, Config_GUI.ANCHO_CASILLA, 20, 0);
			parent.vertex(v4.x, v4.y, 0, 20, 20);
			parent.vertex(v3.x, v3.y, 0, 0, 20);

			// -Y "top" face
			parent.vertex(v2.x, v2.y, 0, 0, 0);
			parent.vertex(v1.x, v1.y, 0, 20, 0);
			parent.vertex(v1.x, v1.y, Config_GUI.ANCHO_CASILLA, 20, 20);
			parent.vertex(v2.x, v2.y, Config_GUI.ANCHO_CASILLA, 0, 20);

			// +X "right" face
			parent.vertex(v1.x, v1.y, Config_GUI.ANCHO_CASILLA, 0, 0);
			parent.vertex(v1.x, v1.y, 0, 20, 0);
			parent.vertex(v4.x, v4.y, 0, 20, 20);
			parent.vertex(v4.x, v4.y, Config_GUI.ANCHO_CASILLA, 0, 20);

			// -X "left" face
			parent.vertex(v2.x, v2.y, 0, 0, 0);
			parent.vertex(v2.x, v2.y, Config_GUI.ANCHO_CASILLA, 20, 0);
			parent.vertex(v3.x, v3.y, Config_GUI.ANCHO_CASILLA, 20, 20);
			parent.vertex(v3.x, v3.y, 0, 0, 20);

			parent.endShape();
			parent.noStroke();
		} else {
			parent.strokeWeight(1);
			parent.stroke(0);
			parent.beginShape();
			parent.texture(tex);
			parent.vertex(v1.x, v1.y, 0, 0, 0);
			parent.vertex(v2.x, v2.y, 0, 20, 0);
			parent.vertex(v3.x, v3.y, 0, 20, 20);
			parent.vertex(v4.x, v4.y, 0, 0, 20);
			parent.endShape();
			parent.noStroke();
			if (!suelo) {
				if (jugador != -1)
					displayJugador(jugador, bandera);
				else if (base != -1)
					displayBase(base, bandera);
				else if (bandera != -1)
					displayBandera(bandera, 0);
				else if (entrada != -1)
					displayEntrada(entrada);
				if (muerte)
					for (int i = 0; i < 25; i++)
						particList.add(new Particula(v1.x + Config_GUI.ANCHO_CASILLA / 2,
								v1.y + Config_GUI.ALTO_CASILLA / 2, 0, parent));

			}
		}
		if (!particList.isEmpty())

		{
			for (int i = 0; i < particList.size(); i++) {
				particList.get(i).run();
				particList.get(i).update();
				particList.get(i).gravity();
				if (particList.get(i).getPos().z < -10) {
					particList.remove(i);
				}
			}
			parent.fill(255);
		}

	}

	public void display2D() {
		pintaCasilla2D();
		parent.lights();
		parent.strokeWeight(1);
		parent.stroke(0);
		parent.beginShape();
		parent.texture(tex);
		parent.vertex(v1.x, v1.y, 0, 0, 0);
		parent.vertex(v2.x, v2.y, 0, 20, 0);
		parent.vertex(v3.x, v3.y, 0, 20, 20);
		parent.vertex(v4.x, v4.y, 0, 0, 20);
		parent.endShape();
		parent.noStroke();
		parent.fill(255);

	}

	public void setPared() {
		this.pared = true;
		this.suelo = false;
		this.jugador = -1;
		this.base = -1;
		this.bandera = -1;
		this.entrada = -1;
		this.muerte = false;
	}

	public void setSuelo() {
		this.pared = false;
		this.suelo = true;
		this.jugador = -1;
		this.base = -1;
		this.bandera = -1;
		this.entrada = -1;
		this.muerte = false;
	}

	public void setJugador(int jugador, String orientacion) {
		this.pared = false;
		this.suelo = false;
		this.jugador = jugador;
		this.orientacion = orientacion;
	}

	public void setBase(int base) {
		this.pared = false;
		this.suelo = false;
		this.base = base;
	}

	public void setBandera(int bandera) {
		this.pared = false;
		this.suelo = false;
		this.bandera = bandera;
	}

	public void setEntrada(int entrada) {
		this.pared = false;
		this.suelo = false;
		this.entrada = entrada;
	}

	public void setMuerte() {
		this.pared = false;
		this.suelo = false;
		this.muerte = true;
		// GUI_Mapa.musicaMuerte =
		// GUI_Mapa.minim.loadFile("sonidos/explosion.mp3");
		if (Config_GUI.SONIDO)
			Sonido.musicaMuerte.play(500);
	}

	public boolean isPared() {
		return pared;
	}

	public void displayJugador(int jugador, int bandera) {
		parent.pushMatrix();
		parent.noStroke();
		parent.translate(v1.x + (Config_GUI.ANCHO_CASILLA / 2), v1.y + (Config_GUI.ALTO_CASILLA / 2), 0);
		Mapa.jugador3D.setFill(parent.color(Config_GUI.Colores[jugador].getRed(),
				Config_GUI.Colores[jugador].getGreen(), Config_GUI.Colores[jugador].getBlue()));
		Mapa.jugador3D.resetMatrix();
		Mapa.jugador3D.scale(Config_GUI.ESCALADO);
		switch (orientacion) {
		case Config_GUI.NE:
			Mapa.jugador3D.rotateZ(PApplet.radians(45));
			break;
		case Config_GUI.ESTE:
			Mapa.jugador3D.rotateZ(PApplet.radians(90));
			break;
		case Config_GUI.SE:
			Mapa.jugador3D.rotateZ(PApplet.radians(135));
			break;
		case Config_GUI.SUR:
			Mapa.jugador3D.rotateZ(PApplet.radians(180));
			break;
		case Config_GUI.SW:
			Mapa.jugador3D.rotateZ(PApplet.radians(225));
			break;
		case Config_GUI.OESTE:
			Mapa.jugador3D.rotateZ(PApplet.radians(270));
			break;
		case Config_GUI.NW:
			Mapa.jugador3D.rotateZ(PApplet.radians(315));
			break;
		default:
			break;
		}
		parent.shape(Mapa.jugador3D, 0, 0);
		parent.popMatrix();
		if (bandera != -1)
			displayBandera(bandera, Mapa.jugador3D.getDepth());

	}

	public void displayBase(int base, int bandera) {
		parent.pushMatrix();
		parent.translate(v1.x + Config_GUI.ANCHO_CASILLA / 2, v1.y + Config_GUI.ALTO_CASILLA / 2, 0);
		Mapa.base3D.setFill(parent.color(Config_GUI.Colores[base].getRed(), Config_GUI.Colores[base].getGreen(),
				Config_GUI.Colores[base].getBlue()));
		parent.shape(Mapa.base3D, 0, 0);
		parent.fill(255);
		parent.popMatrix();
		if (bandera != -1)
			displayBandera(bandera, Mapa.jugador3D.getDepth());

	}

	public void displayEntrada(int entrada) {
		parent.pushMatrix();
		parent.translate(v1.x + Config_GUI.ANCHO_CASILLA / 2, v1.y + Config_GUI.ALTO_CASILLA / 2, 0);
		Mapa.entrada3D.setFill(parent.color(Config_GUI.Colores[entrada].getRed(),
				Config_GUI.Colores[entrada].getGreen(), Config_GUI.Colores[entrada].getBlue()));
		parent.shape(Mapa.entrada3D, 0, 0);
		parent.fill(255);
		parent.popMatrix();
	}

	public void displayBandera(int bandera, float posZ) {
		parent.pushMatrix();
		parent.translate(v1.x + Config_GUI.ANCHO_CASILLA / 2, v1.y + Config_GUI.ALTO_CASILLA / 2, posZ);
		Mapa.bandera3D.setFill(parent.color(Config_GUI.Colores[bandera].getRed(),
				Config_GUI.Colores[bandera].getGreen(), Config_GUI.Colores[bandera].getBlue()));
		parent.shape(Mapa.bandera3D, 0, 0);
		parent.fill(255);
		parent.popMatrix();
	}

	private void pintaCasilla2D() {
		if (pared)
			tex = Mapa.pared2D;
		else if (suelo)
			tex = Mapa.suelo2D;
		else {
			if (jugador != -1) {
				if (bandera == -1)
					switch (orientacion) {
					case Config_GUI.NORTE:
						tex = Mapa.jugadorN2D;
						break;
					case Config_GUI.NE:
						tex = Mapa.jugadorNE2D;
						break;
					case Config_GUI.ESTE:
						tex = Mapa.jugadorE2D;
						break;
					case Config_GUI.SE:
						tex = Mapa.jugadorSE2D;
						break;
					case Config_GUI.SUR:
						tex = Mapa.jugadorS2D;
						break;
					case Config_GUI.SW:
						tex = Mapa.jugadorSW2D;
						break;
					case Config_GUI.OESTE:
						tex = Mapa.jugadorW2D;
						break;
					case Config_GUI.NW:
						tex = Mapa.jugadorNW2D;
						break;
					default:
						break;
					}
				else
					switch (orientacion) {
					case Config_GUI.NORTE:
						tex = Mapa.jugador_banderaN2D;
						break;
					case Config_GUI.NE:
						tex = Mapa.jugador_banderaNE2D;
						break;
					case Config_GUI.ESTE:
						tex = Mapa.jugador_banderaE2D;
						break;
					case Config_GUI.SE:
						tex = Mapa.jugador_banderaSE2D;
						break;
					case Config_GUI.SUR:
						tex = Mapa.jugador_banderaS2D;
						break;
					case Config_GUI.SW:
						tex = Mapa.jugador_banderaSW2D;
						break;
					case Config_GUI.OESTE:
						tex = Mapa.jugador_banderaW2D;
						break;
					case Config_GUI.NW:
						tex = Mapa.jugador_banderaNW2D;
						break;
					default:
						break;
					}
				parent.fill(parent.color(Config_GUI.Colores[jugador].getRed(), Config_GUI.Colores[jugador].getGreen(),
						Config_GUI.Colores[jugador].getBlue()));

			} else if (base != -1) {
				if (bandera == -1)
					tex = Mapa.base2D;
				else
					tex = Mapa.base_bandera2D;
				parent.fill(parent.color(Config_GUI.Colores[base].getRed(), Config_GUI.Colores[base].getGreen(),
						Config_GUI.Colores[base].getBlue()));

			} else if (bandera != -1) {
				tex = Mapa.bandera2D;
				parent.fill(parent.color(Config_GUI.Colores[bandera].getRed(), Config_GUI.Colores[bandera].getGreen(),
						Config_GUI.Colores[bandera].getBlue()));
			} else if (entrada != -1) {
				tex = Mapa.entrada2D;
				parent.fill(parent.color(Config_GUI.Colores[entrada].getRed(), Config_GUI.Colores[entrada].getGreen(),
						Config_GUI.Colores[entrada].getBlue(), 1));
			} else if (muerte) {
				tex = Mapa.muerte2D;
			}
		}
	}

	private void pintaCasilla3D() {
		if (pared)
			tex = Mapa.pared2D;
		else
			tex = Mapa.suelo2D;
	}
}
