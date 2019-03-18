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

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;

public class Sonido {
	static Minim minim;
	static AudioPlayer musicaPartida;
	static AudioPlayer musicaBandera;
	static AudioPlayer musicaMuerte;
	static AudioPlayer musicaFin;
	
	public Sonido(PApplet parent){
		minim = new Minim(parent);
		// TODO:
		musicaPartida = minim.loadFile("sonidos/partida.mp3");
		musicaBandera = minim.loadFile("sonidos/bandera.mp3");
		musicaMuerte = minim.loadFile("sonidos/explosion.mp3");
		musicaFin = minim.loadFile("sonidos/fin.mp3");
		musicaPartida.loop();
	}
}
