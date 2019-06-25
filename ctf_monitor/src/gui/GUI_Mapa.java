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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Iterator;
import java.util.StringTokenizer;
import config.Config_GUI;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

/**
 * PAPPLET QUE HACE DE SKETCH
 */
public class GUI_Mapa extends PApplet {

	private int camara = Config_GUI.CAMARA;
	private boolean is3D = Config_GUI.IS3D;
	private boolean ready = false;

	public int sizeSketchX = 500;
	public int sizeSketchY = 500;
	public int sizeSketchZ = 500;
	private String titulo;

	private PImage texSuelo;
	private Mapa mapa;

	private String nombreJugadorCamara = null;
	private Jugador jugadorCamara = null;

	private float eyeX, eyeY, eyeXJug, eyeYJug, eyeZ, centerX, centerY, centerZ;
	private float SPEED = 10;

	public GUI_Mapa(String msg) {

		// System.out.println(msg);
		StringTokenizer linea = new StringTokenizer(msg, "\n");

		StringTokenizer linea1 = new StringTokenizer(linea.nextToken(), ",");
		titulo = linea1.nextToken();
		int mapaX = Integer.parseInt(linea1.nextToken());
		int mapaY = Integer.parseInt(linea1.nextToken());
		Config_GUI.NUM_EQUIPOS = Integer.parseInt(linea1.nextToken());
		String mapa = linea1.nextToken();

		System.out.println("-------------------- INICIALIZAR SKETCH --------------------");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenX = screenSize.width - 220 * (int) Math.ceil((float) Config_GUI.NUM_EQUIPOS / 4); // 1116
		int screenY = screenSize.height - 60; // 708

		Config_GUI.ANCHO_CASILLA = screenX / mapaX;
		Config_GUI.ALTO_CASILLA = screenY / mapaY;

		if (Config_GUI.ANCHO_CASILLA <= Config_GUI.ALTO_CASILLA)
			Config_GUI.ALTO_CASILLA = Config_GUI.ANCHO_CASILLA;
		else
			Config_GUI.ANCHO_CASILLA = Config_GUI.ALTO_CASILLA;

		sizeSketchX = (Config_GUI.ANCHO_CASILLA * mapaX);
		sizeSketchY = (Config_GUI.ALTO_CASILLA * mapaY);

		// TODO
		// sizeSketchZ=(int) ((height / 2) / tan(PI / 7));
		sizeSketchZ = Math.max(sizeSketchX, sizeSketchY);

		System.out.println("Ventana Máxima (Pixels): " + screenX + "," + screenY);
		System.out.println("Mapa (Casillas): " + mapaX + "," + mapaY);
		System.out.println("Tamaño casillas (Pixels): " + Config_GUI.ANCHO_CASILLA + ", " + Config_GUI.ALTO_CASILLA);
		System.out.println("Tamaño del sketch (Pixels): " + sizeSketchX + ", " + sizeSketchY);

		this.mapa = new Mapa(mapaX, mapaY, mapa, msg, this);
	
	}
	public void settings() {
		size(sizeSketchX, sizeSketchY, P3D);
	}

	public void setup() {
		// TODO
		// surface.setResizable(true);
		eyeX = width / 2;
		eyeY = height / 2;
		eyeXJug = width / 2;
		eyeYJug = height / 2;
		eyeZ = (height / 2) / tan(PI / 7);
		centerX = width / 2;
		centerY = height / 2;
		centerZ = 0;

		surface.setTitle(titulo);
		surface.setLocation(220 * (int) Math.ceil((float) Config_GUI.NUM_EQUIPOS / 4), 0);
		background(0);
		mapa.cargarImagenes();
		mapa.cargarFiguras3D();
		texSuelo = Mapa.cesped2D;

		if (Config_GUI.SONIDO) {
			new Sonido(this);
		}

		ready = true;
	}

	public void draw() {
		// camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ)
		switch (camara) {
		case 1:
			camera(width / 2, height + height / 2, (height / 2) / tan(PI / 5), width / 2, height / 2, 0, 0, 1, 0);
			break;
		case 2:
			camera(width / 2, height / 2, (height / 2) / tan(PI / 7), width / 2, height / 2, 0, 0, 1, 0);
			break;
		case 3:
			camera(mouseX, mouseY, (height / 2) / tan(PI / 7), width / 2, height / 2, 0, 0, 1, 0);
			break;
		case 4:
			camera(mouseX * 3 - width, mouseY * 3 - height, (height / 2) / tan(PI / 7), width / 2, height / 2, 0, 0, 1,
					0);
			break;
		case 5:
			camera(mouseX * 2 - width / 2, mouseY * 2 - height / 2,
					((height / 2) / tan(PI / 7)) - abs(mouseY - height / 2) / 2, width / 2, height / 2, 0, 0, 1, 0);
			break;
		case 6:
			if (keyPressed) {
				if (key == 'q') {
					eyeZ -= SPEED;
					centerZ -= SPEED;
				}
				if (key == 'a') {
					eyeZ += SPEED;
					centerZ += SPEED;
				}
				if (keyCode == UP) {
					eyeY -= SPEED;
					centerY -= SPEED;
				}
				if (keyCode == DOWN) {
					eyeY += SPEED;
					centerY += SPEED;
				}
				if (keyCode == LEFT) {
					eyeX -= SPEED;
					centerX -= SPEED;
				}
				if (keyCode == RIGHT) {
					eyeX += SPEED;
					centerX += SPEED;
				}
			}
			camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, 0, 1, 0);
			break;
		case 7:
			for (Iterator<Jugador> iterator = mapa.getJugadores().iterator(); iterator.hasNext();) {
				Jugador j = (Jugador) iterator.next();
				if (j.getName().equals(nombreJugadorCamara)) {
					jugadorCamara = j;
				}
			}
			if (jugadorCamara != null) {
				switch (jugadorCamara.getOrientacion()) {
				case Config_GUI.NORTE:
					eyeXJug = jugadorCamara.getPosX() * Config_GUI.ANCHO_CASILLA;
					eyeYJug = (jugadorCamara.getPosY() + 2) * Config_GUI.ALTO_CASILLA;
					break;
				case Config_GUI.NE:
					eyeXJug = (jugadorCamara.getPosX() - 2) * Config_GUI.ANCHO_CASILLA;
					eyeYJug = (jugadorCamara.getPosY() + 2) * Config_GUI.ALTO_CASILLA;
					break;
				case Config_GUI.ESTE:
					eyeXJug = (jugadorCamara.getPosX() - 2) * Config_GUI.ANCHO_CASILLA;
					eyeYJug = jugadorCamara.getPosY() * Config_GUI.ALTO_CASILLA;
					break;
				case Config_GUI.SE:
					eyeXJug = (jugadorCamara.getPosX() - 2) * Config_GUI.ANCHO_CASILLA;
					eyeYJug = (jugadorCamara.getPosY() - 2) * Config_GUI.ALTO_CASILLA;
					break;
				case Config_GUI.SUR:
					eyeXJug = jugadorCamara.getPosX() * Config_GUI.ANCHO_CASILLA;
					eyeYJug = (jugadorCamara.getPosY() - 2) * Config_GUI.ALTO_CASILLA;
					break;
				case Config_GUI.SW:
					eyeXJug = (jugadorCamara.getPosX() + 2) * Config_GUI.ANCHO_CASILLA;
					eyeYJug = (jugadorCamara.getPosY() - 2) * Config_GUI.ALTO_CASILLA;
					break;
				case Config_GUI.OESTE:
					eyeXJug = (jugadorCamara.getPosX() + 2) * Config_GUI.ANCHO_CASILLA;
					eyeYJug = jugadorCamara.getPosY() * Config_GUI.ALTO_CASILLA;
					break;
				case Config_GUI.NW:
					eyeXJug = (jugadorCamara.getPosX() + 2) * Config_GUI.ANCHO_CASILLA;
					eyeYJug = (jugadorCamara.getPosY() - 2) * Config_GUI.ALTO_CASILLA;
					break;
				default:
					break;
				}
				// camera(jugadorCamara.getPosX() * Config_GUI.ANCHO_CASILLA,
				// jugadorCamara.getPosY() * Config_GUI.ALTO_CASILLA,
				// (height / 2) / tan(PI / 5), jugadorCamara.getPosX() *
				// Config_GUI.ANCHO_CASILLA,
				// jugadorCamara.getPosY() * Config_GUI.ALTO_CASILLA, (float)
				// 0.2, 0, 1, 0);
				camera(eyeXJug, eyeYJug, Config_GUI.ALTO_CASILLA * 2,
						jugadorCamara.getPosX() * Config_GUI.ANCHO_CASILLA,
						jugadorCamara.getPosY() * Config_GUI.ALTO_CASILLA, Config_GUI.ALTO_CASILLA * 2, 0, 0, -1);
			}
			break;
		}

		// TODO: Aqui se ejecuta siempre, probar en setup solo 1 vez

		background(100);
		mapa.displaySkyBox();
		// displaySuelo();

		if (is3D)
			mapa.display3D();
		else
			mapa.display2D();

	}

	public void setJugadorCamara(String jugador) {
		if (jugador != null)
			nombreJugadorCamara = jugador;
		else
			System.out.println("JUAGDOR NULL");
		System.out.println("SIGO AL JUGADOR: " + nombreJugadorCamara);
	}

	public void setCamara(int camara) {
		this.camara = camara;
	}

	public void set3D(boolean is3D) {
		this.is3D = is3D;
	}

//	public void initMapa(String msg) {
//
//		// System.out.println(msg);
//		StringTokenizer linea = new StringTokenizer(msg, "\n");
//
//		StringTokenizer linea1 = new StringTokenizer(linea.nextToken(), ",");
//		titulo = linea1.nextToken();
//		int mapaX = Integer.parseInt(linea1.nextToken());
//		int mapaY = Integer.parseInt(linea1.nextToken());
//		Config_GUI.NUM_EQUIPOS = Integer.parseInt(linea1.nextToken());
//		String mapa = linea1.nextToken();
//
//		System.out.println("-------------------- INICIALIZAR SKETCH --------------------");
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		int screenX = screenSize.width - 220 * (int) Math.ceil((float) Config_GUI.NUM_EQUIPOS / 4); // 1116
//		int screenY = screenSize.height - 60; // 708
//
//		Config_GUI.ANCHO_CASILLA = screenX / mapaX;
//		Config_GUI.ALTO_CASILLA = screenY / mapaY;
//
//		if (Config_GUI.ANCHO_CASILLA <= Config_GUI.ALTO_CASILLA)
//			Config_GUI.ALTO_CASILLA = Config_GUI.ANCHO_CASILLA;
//		else
//			Config_GUI.ANCHO_CASILLA = Config_GUI.ALTO_CASILLA;
//
//		sizeSketchX = (Config_GUI.ANCHO_CASILLA * mapaX);
//		sizeSketchY = (Config_GUI.ALTO_CASILLA * mapaY);
//
//		// TODO
//		// sizeSketchZ=(int) ((height / 2) / tan(PI / 7));
//		sizeSketchZ = Math.max(sizeSketchX, sizeSketchY);
//
//		System.out.println("Ventana Máxima (Pixels): " + screenX + "," + screenY);
//		System.out.println("Mapa (Casillas): " + mapaX + "," + mapaY);
//		System.out.println("Tamaño casillas (Pixels): " + Config_GUI.ANCHO_CASILLA + ", " + Config_GUI.ALTO_CASILLA);
//		System.out.println("Tamaño del sketch (Pixels): " + sizeSketchX + ", " + sizeSketchY);
//
//		this.mapa = new Mapa(mapaX, mapaY, mapa, msg, this);
//	}

	public void update(String mapa) {

		// TODO:
		if (mapa.startsWith("\n\nJuego")) {
			System.out.println(mapa);
			if (Config_GUI.SONIDO) {
				if (Sonido.musicaPartida.isPlaying()) {
					Sonido.musicaPartida.pause();
					Sonido.musicaFin.play();
				}
			}
		} else
			this.mapa.update(mapa);
		// this.mapa.imprimeMapa();

	}

	public void displaySuelo() {

		beginShape();
		texture(texSuelo);
		textureMode(PConstants.NORMAL);
		vertex(-500, -500, -1, 0, 0);
		vertex(sizeSketchX + 500, -500, -1, 1, 0);
		vertex(sizeSketchX + 500, sizeSketchY + 500, -1, 1, 1);
		vertex(-500, sizeSketchY + 500, -1, 0, 1);
		endShape();
		textureMode(PConstants.IMAGE);
	}

	public boolean getReady() {
		return ready;
	}

	// public void mouseClicked() {
	// int X = mouseX / Config_GUI.ANCHO_CASILLA;
	// int Y = mouseY / Config_GUI.ALTO_CASILLA;
	// jugadorCamara = mapa.getJugador(X, Y);
	// }

	// public void

	public void run() {
		String[] processingArgs = { GUI_Mapa.class.getName() };
		PApplet.runSketch(processingArgs, this);
	}

}
