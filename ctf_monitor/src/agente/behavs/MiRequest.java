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

import config.Config_GUI;
import gui.GUI_Interactuar;
import gui.GUI_Mapa;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;

/**
 * COMPORTAMIENTO QUE ESTABLECE EL PROTOCOLO FIPAREQUEST
 */
@SuppressWarnings("serial")
public class MiRequest extends SimpleAchieveREInitiator {
	private GUI_Mapa sketch;
	private GUI_Interactuar ventana;

	public MiRequest(Agent a, ACLMessage msg, GUI_Mapa sketch, GUI_Interactuar ventana) {
		super(a, msg);
		this.sketch = sketch;
		this.ventana = ventana;
	}

	public MiRequest(Agent a, ACLMessage msg) {
		super(a, msg);
	}

	public void handleAgree(ACLMessage msg) {
		System.out.println("Monitor conectado al Servidor.");
	}

	public void handleRefuse(ACLMessage msg) {
		// Reconectar
		Busqueda b = new Busqueda(sketch, ventana);
		myAgent.addBehaviour(b);
	}

	public void handleInform(ACLMessage msg) {
		// Si nos han aceptado
		sketch = new GUI_Mapa();
		sketch.initMapa(msg.getContent());
		ventana = new GUI_Interactuar("Panel de Control", msg.getContent(), sketch, false);
		sketch.run();
		ventana.setVisible(true);

		/*
		 * while (!sketch.getReady()) { System.out.println("NO ESTA LISTO"); }
		 */

		myAgent.addBehaviour(new Oyente_Tablero(sketch));
		for (int i = 0; i < Config_GUI.NUM_EQUIPOS; i++)
			myAgent.addBehaviour(new Oyente_Jugadores(i, ventana));
		myAgent.addBehaviour(new Oyente_Info(ventana));
	}

	public void handleNotUnderstood(ACLMessage msg) {
		System.out.println("handleNotUnderstood");
	}

	public void handleOutOfSequence(ACLMessage msg) {
		System.out.println("handleOutOfSequence");
	}
}