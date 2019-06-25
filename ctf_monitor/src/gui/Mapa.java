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
import java.util.Iterator;
import java.util.StringTokenizer;
import config.Config_GUI;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PShape;

/**
 * CLASE QUE CONTIENE LA INFORMACION DEL DISEÑO GRAFICO DEL MAPA
 */
public class Mapa {

	public static PImage pared2D, suelo2D, base2D, jugadorN2D, jugadorNE2D, jugadorE2D, jugadorSE2D, jugadorS2D,
			jugadorSW2D, jugadorW2D, jugadorNW2D, jugador_banderaN2D, jugador_banderaNE2D, jugador_banderaE2D,
			jugador_banderaSE2D, jugador_banderaS2D, jugador_banderaSW2D, jugador_banderaW2D, jugador_banderaNW2D,
			bandera2D, base_bandera2D, entrada2D, muerte2D, cesped2D;
	public static PImage[] skyImg = new PImage[6];
	public static PShape jugador3D, bandera3D, base3D, entrada3D;

	public Casilla[][] casillas;
	private int tamx, tamy;
	private GUI_Mapa parent;

	private ArrayList<Jugador> jugadores;

	public Mapa(int _x, int _y, String mapa, String msg, GUI_Mapa parent) {
		// System.out.println(contenido);

		// "[VERSION],[DIM_X],[DIM_Y],[NUM_EQ],[MAPA] \n [linea1] \n..."

		this.parent = parent;
		jugadores = new ArrayList<>();
		StringTokenizer linea = new StringTokenizer(msg, "\n");
		linea.nextToken();// primera linea ya analizada, lo demas es contenido
		tamx = _x;
		tamy = _y;

		// TODO: PArsear nuevo msgggg
		int xx, yy;

		int contador = 0;
		casillas = new Casilla[tamx][tamy];

		for (int j = 0; j < tamy; j++)
			for (int i = 0; i < tamx; i++) {
				xx = i * Config_GUI.ANCHO_CASILLA;
				yy = j * Config_GUI.ALTO_CASILLA;
				casillas[i][j] = new Casilla(mapa.charAt(contador++) == '#', xx, yy, Config_GUI.ANCHO_CASILLA,
						Config_GUI.ALTO_CASILLA, this.parent);
			}

		int eq;
		while (linea.hasMoreTokens()) {
			StringTokenizer st = new StringTokenizer(linea.nextToken(), ",");
			switch (st.nextToken()) {
			case "Base":
				eq = Integer.parseInt(st.nextToken());
				casillas[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())].setBase(eq);
				break;
			case "Bandera":
				eq = Integer.parseInt(st.nextToken());
				casillas[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())].setBandera(eq);
				break;
			case "Entrada":
				eq = Integer.parseInt(st.nextToken());
				casillas[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())].setEntrada(eq);
				break;
			case "Jugador":
				jugadores.add(new Jugador(st.nextToken(), Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), st.nextToken()));
				casillas[jugadores.get(jugadores.size() - 1).getPosX()][jugadores.get(jugadores.size() - 1).getPosY()]
						.setJugador(jugadores.get(jugadores.size() - 1).getEquipo(),
								jugadores.get(jugadores.size() - 1).getOrientacion());
				break;
			case "Muerte":
				casillas[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())].setMuerte();
				break;
			case "//":
				break;
			default:
				System.err.println("No se reconoce tokken");
				break;
			}
		}
	}

	public void update(String msg) {
		// "[linea1] \n..."

		// TODO: OJO CUIDAO, INEFICIENCIA!!
		for (int j = 0; j < tamy; j++)
			for (int i = 0; i < tamx; i++)
				if (!casillas[i][j].isPared())
					casillas[i][j].setSuelo();

		jugadores = new ArrayList<>();
		StringTokenizer linea = new StringTokenizer(msg, "\n");
		int eq;
		while (linea.hasMoreTokens()) {
			StringTokenizer st = new StringTokenizer(linea.nextToken(), ",");
			switch (st.nextToken()) {
			case "Base":
				eq = Integer.parseInt(st.nextToken());
				casillas[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())].setBase(eq);
				break;
			case "Bandera":
				eq = Integer.parseInt(st.nextToken());
				casillas[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())].setBandera(eq);
				break;
			case "Entrada":
				eq = Integer.parseInt(st.nextToken());
				casillas[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())].setEntrada(eq);
				break;
			case "Jugador":
				jugadores.add(new Jugador(st.nextToken(), Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), st.nextToken()));
				casillas[jugadores.get(jugadores.size() - 1).getPosX()][jugadores.get(jugadores.size() - 1).getPosY()]
						.setJugador(jugadores.get(jugadores.size() - 1).getEquipo(),
								jugadores.get(jugadores.size() - 1).getOrientacion());
				break;
			case "Muerte":
				casillas[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())].setMuerte();
				break;
			case "//":
				break;
			default:
				System.err.println("No se reconoce tokken");
				break;
			}
		}

	}

	public void display3D() {
		for (int j = 0; j < tamy; j++)
			for (int i = 0; i < tamx; i++)
				casillas[i][j].display3D();
	}

	public void display2D() {
		for (int j = 0; j < tamy; j++)
			for (int i = 0; i < tamx; i++)
				casillas[i][j].display2D();
	}

	public void displaySkyBox() {

		parent.beginShape();
		parent.texture(skyImg[0]);
		parent.textureMode(PConstants.NORMAL);
		parent.vertex(-parent.sizeSketchX, -parent.sizeSketchY, -1, 0, 0);
		parent.vertex(parent.sizeSketchX * 2, -parent.sizeSketchY, -1, 1, 0);
		parent.vertex(parent.sizeSketchX * 2, parent.sizeSketchY * 2, -1, 1, 1);
		parent.vertex(-parent.sizeSketchX, parent.sizeSketchY * 2, -1, 0, 1);
		parent.endShape();

		parent.beginShape();
		parent.texture(skyImg[1]);
		parent.textureMode(PConstants.NORMAL);
		parent.vertex(parent.sizeSketchX * 2, -parent.sizeSketchY, -1, 0, 0);
		parent.vertex(parent.sizeSketchX * 2, -parent.sizeSketchY, parent.sizeSketchZ, 1, 0);
		parent.vertex(parent.sizeSketchX * 2, parent.sizeSketchY * 2, parent.sizeSketchZ, 1, 1);
		parent.vertex(parent.sizeSketchX * 2, parent.sizeSketchY * 2, -1, 0, 1);
		parent.endShape();

		parent.beginShape();
		parent.texture(skyImg[2]);
		parent.textureMode(PConstants.NORMAL);
		parent.vertex(parent.sizeSketchX * 2, -parent.sizeSketchY, parent.sizeSketchZ, 0, 0);
		parent.vertex(-parent.sizeSketchX, -parent.sizeSketchY, parent.sizeSketchZ, 1, 0);
		parent.vertex(-parent.sizeSketchX, parent.sizeSketchY * 2, parent.sizeSketchZ, 1, 1);
		parent.vertex(parent.sizeSketchX * 2, parent.sizeSketchY * 2, parent.sizeSketchZ, 0, 1);
		parent.endShape();

		parent.beginShape();
		parent.texture(skyImg[3]);
		parent.textureMode(PConstants.NORMAL);
		parent.vertex(-parent.sizeSketchX, -parent.sizeSketchY, parent.sizeSketchZ, 0, 0);
		parent.vertex(-parent.sizeSketchX, -parent.sizeSketchY, -1, 1, 0);
		parent.vertex(-parent.sizeSketchX, parent.sizeSketchY * 2, -1, 1, 1);
		parent.vertex(-parent.sizeSketchX, parent.sizeSketchY * 2, parent.sizeSketchZ, 0, 1);
		parent.endShape();

		parent.beginShape();
		parent.texture(skyImg[4]);
		parent.textureMode(PConstants.NORMAL);
		parent.vertex(-parent.sizeSketchX, -parent.sizeSketchY, parent.sizeSketchZ, 0, 0);
		parent.vertex(parent.sizeSketchX * 2, -parent.sizeSketchY, parent.sizeSketchZ, 1, 0);
		parent.vertex(parent.sizeSketchX * 2, -parent.sizeSketchY, -1, 1, 1);
		parent.vertex(-parent.sizeSketchX, -parent.sizeSketchY, -1, 0, 1);
		parent.endShape();

		parent.beginShape();
		parent.texture(skyImg[5]);
		parent.textureMode(PConstants.NORMAL);
		parent.vertex(parent.sizeSketchX * 2, parent.sizeSketchY * 2, parent.sizeSketchZ, 0, 0);
		parent.vertex(-parent.sizeSketchX, parent.sizeSketchY * 2, parent.sizeSketchZ, 1, 0);
		parent.vertex(-parent.sizeSketchX, parent.sizeSketchY * 2, -1, 1, 1);
		parent.vertex(parent.sizeSketchX * 2, parent.sizeSketchY * 2, -1, 0, 1);
		parent.endShape();

		parent.textureMode(PConstants.IMAGE);
	}

	public Jugador getJugador(int posX, int posY) {
		for (Iterator<Jugador> iterator = jugadores.iterator(); iterator.hasNext();) {
			Jugador jugador = (Jugador) iterator.next();
			if (jugador.getPosX() == posX && jugador.getPosY() == posY)
				return jugador;
		}
		return null;
	}

	public ArrayList<Jugador> getJugadores() {
		return jugadores;
	}

	public void cargarImagenes() {
		pared2D = parent.loadImage("img/muro.jpg");
		suelo2D = parent.loadImage("img/suelo.jpg");
		base2D = parent.loadImage("img/base.jpg");
		jugadorN2D = parent.loadImage("img/jugadorNorte.jpg");
		jugadorNE2D = parent.loadImage("img/jugadorNorEste.jpg");
		jugadorE2D = parent.loadImage("img/jugadorEste.jpg");
		jugadorSE2D = parent.loadImage("img/jugadorSurEste.jpg");
		jugadorS2D = parent.loadImage("img/jugadorSur.jpg");
		jugadorSW2D = parent.loadImage("img/jugadorSurOeste.jpg");
		jugadorW2D = parent.loadImage("img/jugadorOeste.jpg");
		jugadorNW2D = parent.loadImage("img/jugadorNorOeste.jpg");
		jugador_banderaN2D = parent.loadImage("img/jugadorBanderaNorte.jpg");
		jugador_banderaNE2D = parent.loadImage("img/jugadorBanderaNorEste.jpg");
		jugador_banderaE2D = parent.loadImage("img/jugadorBanderaEste.jpg");
		jugador_banderaSE2D = parent.loadImage("img/jugadorBanderaSurEste.jpg");
		jugador_banderaS2D = parent.loadImage("img/jugadorBanderaSur.jpg");
		jugador_banderaSW2D = parent.loadImage("img/jugadorBanderaSurOeste.jpg");
		jugador_banderaW2D = parent.loadImage("img/jugadorBanderaOeste.jpg");
		jugador_banderaNW2D = parent.loadImage("img/jugadorBanderaNorOeste.jpg");
		bandera2D = parent.loadImage("img/bandera.jpg");
		base_bandera2D = parent.loadImage("img/base_bandera.jpg");
		entrada2D = parent.loadImage("img/entrada.jpg");
		muerte2D = parent.loadImage("img/explosion.png");
		cesped2D = parent.loadImage("img/cesped1.jpg");

		for (int i = 0; i < 6; i++) {
			skyImg[i] = parent.loadImage("img/sky" + i + ".jpg");
		}
	}

	public void cargarFiguras3D() {
		Config_GUI.ESCALADO = (float) Config_GUI.ANCHO_CASILLA / (float) 37;
		System.out.println("Escalado de piezas: " + Config_GUI.ESCALADO);

		long time = System.currentTimeMillis();

		jugador3D = parent.loadShape("figuras/lego.obj");
		jugador3D.scale(Config_GUI.ESCALADO);

		bandera3D = parent.loadShape("figuras/Flag.obj");
		bandera3D.scale(Config_GUI.ESCALADO * 30);
		bandera3D.rotateX(PApplet.radians(230));
		bandera3D.rotateY(PApplet.radians(180));

		base3D = parent.loadShape("figuras/castillo.obj");
		base3D.scale(Config_GUI.ESCALADO * 5);
		base3D.rotateX(PApplet.radians(180));
		base3D.rotateY(PApplet.radians(180));

		entrada3D = parent.loadShape("figuras/entrada.obj");
		entrada3D.scale(Config_GUI.ESCALADO * 6);
		entrada3D.rotateX(PApplet.radians(180));
		entrada3D.rotateY(PApplet.radians(180));

		System.out.println("Load time: " + (System.currentTimeMillis() - time) / 1000.0 + " sec");

	}
}
