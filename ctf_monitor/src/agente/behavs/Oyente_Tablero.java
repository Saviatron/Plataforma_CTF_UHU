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

import gui.GUI_Mapa;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * COMPORTAMIENTO QUE RECIBE EL CONTENIDO DE LA PARTIDA
 */
@SuppressWarnings("serial")
public class Oyente_Tablero extends CyclicBehaviour {

	GUI_Mapa sketch;

	public Oyente_Tablero(GUI_Mapa sketch) {
		this.sketch = sketch;
	}

	@Override
	public void action() {
		ACLMessage mens = myAgent.receive(MessageTemplate.MatchConversationId("tablero"));
		if (mens != null) {
			sketch.update(mens.getContent());
		} else
			block();

	}

}
