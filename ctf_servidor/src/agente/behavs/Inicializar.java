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


package agente.behavs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import agente.behavs.grafico.RecibirMonitores;
import juegoCTF.Cerebro;
import config.Config;

/**
 * COMPORTAMIENTO QUE INICIALIZA LA PLATAFORMA
 */

@SuppressWarnings("serial")
public class Inicializar extends OneShotBehaviour {

	/**
	 * VARIABLES Y CONFIGURACION POR DEFECTO!
	 */
	String ficheroMapa = Config.MapaPath + Config.MapaDefecto;
	String ficheroContenido = Config.MapaPath + Config.ContenidoDefecto;
	int tiempo = Config.TiempoTick;
	boolean ParametroMapa = false;

	Cerebro cer;

	public Inicializar(Agent a) {
		super(a);
		System.out.println("\n\n********** INICIALIZAR: "+Config.VERSION+" ************\n");
		cer = new Cerebro();
		if (Config.Mapa != null) {
			ficheroMapa = Config.MapaPath + Config.Mapa;
			ficheroContenido = Config.MapaPath + Config.Contenido;
			ParametroMapa = true;
		}
		tiempo = Config.TiempoTick;
	}

	public void action() {
		if (!ParametroMapa) {
			try {
				BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
				System.out.print("Introduce el fichero del mapa: ");
				String as = b.readLine();
				if (!as.equals(""))
					ficheroMapa = Config.MapaPath + as;
				System.out.print("Introduce el fichero del contenido: ");
				as = b.readLine();
				if (!as.equals(""))
					ficheroContenido = Config.MapaPath + as;

			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println("Error al leer el mapa");
				myAgent.doDelete();

			}
		}
		System.out.println("Mapa: " + ficheroMapa);
		System.out.println("Contenido: " + ficheroContenido);
		System.out.println("Número de Equipos: " + Config.NUM_EQUIPOS);
		System.out.println("Número de Jugadores Máximos por equipo: " + Config.MAX_JUGADORES_EQ);
		System.out.println("Tiempo de Ticks: " + tiempo+"\n");

		cer.leerFicheroCTF(ficheroMapa);
		cer.contenido_parser(ficheroContenido);

		myAgent.addBehaviour(new Registrar());

		RecibirMonitores rmon = new RecibirMonitores(myAgent, cer);
		myAgent.addBehaviour(rmon);

		RecibirAgentes rag = new RecibirAgentes(myAgent, cer);
		myAgent.addBehaviour(rag);

		RecibirAcciones ra = new RecibirAcciones(cer);
		myAgent.addBehaviour(ra);

		myAgent.addBehaviour(new Tick(myAgent, tiempo, cer));

	}

}
