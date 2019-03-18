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
import gui.GUI_Mapa;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPANames.InteractionProtocol;
import jade.lang.acl.ACLMessage;

/**
 * COMPORTAMIENTO QUE BUSCA AL SERVIDOR
 */
@SuppressWarnings("serial")
public class Busqueda extends Behaviour {

	private AID plataforma = null;
	private GUI_Mapa sketch;
	private GUI_Interactuar ventana;

	public Busqueda(GUI_Mapa sketch, GUI_Interactuar ventana) {
		super();
		this.sketch = sketch;
		this.ventana = ventana;
	}

	public void action() {
		// ----------------------------------------------------------------//!
		// Busqueda de Servicio en el DF
		// ----------------------------------------------------------------//!
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("CTF");
		sd.setName("SERVIDOR_2019");
		template.addServices(sd);

		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			plataforma = new AID();
			for (int i = 0; i < result.length; i++) {
				plataforma = result[i].getName();
			}
		} catch (FIPAException fe) {
			plataforma = null;
			fe.printStackTrace();
		}

	}

	public boolean done() {
		if ((plataforma != null)) {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);

			msg.addReceiver(plataforma);
			msg.setContent("Monitor");
			msg.setProtocol(InteractionProtocol.FIPA_REQUEST);

			myAgent.addBehaviour(new MiRequest(myAgent, msg, sketch, ventana));

			return true;
		} else
			return false;
	}
}