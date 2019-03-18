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

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames.InteractionProtocol;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.*;
import juegoCTF.Cerebro;

/**
 * COMPORTAMIENTO QUE RECIBE LOS MONITORES
 */
@SuppressWarnings("serial")
public class RecibirMonitores extends SimpleAchieveREResponder {

	Cerebro CEREBRO;
	AID monitor;

	public RecibirMonitores(Agent a, Cerebro _cer) {
		super(a, MessageTemplate.and(MessageTemplate.MatchProtocol(InteractionProtocol.FIPA_REQUEST),
				MessageTemplate.MatchContent("Monitor")));
		CEREBRO = _cer;
		System.out.println("A la espera de Monitores.");
	}

	protected ACLMessage prepareResponse(ACLMessage msg) {
		ACLMessage respuesta = msg.createReply();
		monitor = msg.getSender();
		respuesta.setPerformative(ACLMessage.AGREE);
		return respuesta;
	}

	protected ACLMessage prepareResultNotification(ACLMessage inMsg, ACLMessage outMsg) {

		CEREBRO.addMonitor(monitor);
		System.out.println("Monitor " + monitor.getName() + " unido a la plataforma");

		ACLMessage msg = inMsg.createReply();
		msg.setPerformative(ACLMessage.INFORM);
		msg.setContent(CEREBRO.EnviarTableroaMonitor());

		return msg;
	}

}