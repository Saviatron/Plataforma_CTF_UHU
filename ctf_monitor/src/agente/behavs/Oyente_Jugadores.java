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

import gui.GUI_Interactuar;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * COMPORTAMIENTO QUE RECIBE LOS NOMBRES DE LOS JUGADORES ACTUALES
 */
@SuppressWarnings("serial")
public class Oyente_Jugadores extends CyclicBehaviour {

	GUI_Interactuar ventana;
	int equipo;

	public Oyente_Jugadores(int equipo, GUI_Interactuar ventana) {
		this.ventana = ventana;
		this.equipo = equipo;
	}

	@Override
	public void action() {
		ACLMessage mens = myAgent.receive(MessageTemplate.MatchConversationId("jugadores" + equipo));
		if (mens != null)
			ventana.rePintarJugadores(equipo, mens.getContent());

		else
			block();

	}

}