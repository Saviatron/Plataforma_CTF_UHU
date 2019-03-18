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

import config.Config;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import juegoCTF.Jugador;

/**
 * COMPORTAMIENTO QUE ENVIA LOS JUGADORES ACTUALES AL MONITOR
 */
@SuppressWarnings("serial")
public class Enviar_Jugadores_Monitor extends OneShotBehaviour {

	String MENSAJE;
	int EQUIPO;
	List<AID> MONITORES;

	public Enviar_Jugadores_Monitor(int equipo, List<Jugador> lista, List<AID> monitores) {
		super();
		MENSAJE = analizar(equipo,lista);
		EQUIPO = equipo;
		MONITORES = monitores;
	}

	@Override
	public void action() {
		ACLMessage m = new ACLMessage(ACLMessage.INFORM);
		m.setContent(MENSAJE);
		Iterator<AID> i = MONITORES.iterator();
		while (i.hasNext()) {
			m.addReceiver(i.next());
		}
		m.setConversationId("jugadores" + EQUIPO);
		myAgent.send(m);
		// System.out.println("Envio Jugadores a Monitor:\n" + MENSAJE);
	}

	private String analizar(int equipo,List<Jugador> js) {
		StringBuffer sb = new StringBuffer();
		sb.append(Config.Equipos[equipo] + "\n");
		Iterator<Jugador> it = js.iterator();
		while (it.hasNext()) {
			Jugador j = it.next();
			sb.append(j.toString() + "\n");
		}

		return sb.toString();
	}

}
