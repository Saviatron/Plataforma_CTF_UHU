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


package agente.behavs.grafico;

import java.util.Iterator;
import java.util.List;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;

/**
 * COMPORTAMIENTO QUE ENVIA EL MAPA AL MONITOR
 */
@SuppressWarnings("serial")
public class Enviar_Grafico_Monitor extends OneShotBehaviour {

	String MENSAJE;
	List<AID> MONITORES;

	public Enviar_Grafico_Monitor(String _mensaje, List<AID> _mon) {
		super();
		MENSAJE = _mensaje;
		MONITORES = _mon;
	}

	@Override
	public void action() {
		ACLMessage m = new ACLMessage(ACLMessage.INFORM);
		m.setContent(MENSAJE);

		Iterator<AID> i = MONITORES.iterator();
		while (i.hasNext()) {
			m.addReceiver(i.next());
		}

		m.setConversationId("tablero");
		myAgent.send(m);

	}
}
