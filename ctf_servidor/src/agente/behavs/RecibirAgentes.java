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

import java.util.StringTokenizer;

import agente.behavs.grafico.Enviar_Jugadores_Monitor;
import config.Config;
import jade.core.Agent;
import jade.domain.FIPANames.InteractionProtocol;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.*;
import juegoCTF.Cerebro;
import juegoCTF.Jugador;

/**
 * COMPORTAMIENTO QUE RECIBIRA LOS JUGADORES QUE QUIEREN ENTRAR EN LA PLATAFORMA
 */
@SuppressWarnings("serial")
public class RecibirAgentes extends SimpleAchieveREResponder {

	Cerebro CEREBRO;
	Jugador jugador;
	int equipo;
	String clave = "";
	String nomEquipo = "";

	public RecibirAgentes(Agent a, Cerebro _cer) {
		// TODO: NOT MONITOR?
		super(a, MessageTemplate.and(MessageTemplate.MatchProtocol(InteractionProtocol.FIPA_REQUEST),
				MessageTemplate.not(MessageTemplate.MatchContent("Monitor"))));
		CEREBRO = _cer;
		System.out.println("A la espera de Jugadores.");
	}

	protected ACLMessage prepareResponse(ACLMessage msg) {
		ACLMessage respuesta = msg.createReply();

		if (msg != null && msg.getContent() != null) {
			if (Math.random() > Config.TASA_RECHAZO) {
				StringTokenizer st = new StringTokenizer(msg.getContent(), ",");
				equipo = Integer.parseInt(st.nextToken());
				clave = st.nextToken();
				if (st.hasMoreTokens())
					nomEquipo = st.nextToken();
				else 
					nomEquipo ="";

				if (!nomEquipo.equals(Config.Equipos[equipo])&&!nomEquipo.equals(""))
					Config.Equipos[equipo] = nomEquipo;

				jugador = new Jugador(msg.getSender(), equipo);

				if (CEREBRO.asignarPosicionEntrada(jugador) && CEREBRO.MaxJugadores(equipo, jugador)
						&& clave.equals(Config.PSW)) {
					respuesta.setPerformative(ACLMessage.AGREE);
				} else
					respuesta.setPerformative(ACLMessage.REFUSE);
			} else {
				respuesta.setPerformative(ACLMessage.REFUSE);
			}

		} else
			respuesta.setPerformative(ACLMessage.NOT_UNDERSTOOD);

		return respuesta;
	}

	protected ACLMessage prepareResultNotification(ACLMessage inMsg, ACLMessage outMsg) {

		CEREBRO.addJugador(equipo, jugador);
		CEREBRO.estadisticas.add_jugador(jugador.getLocalName(), equipo);
		System.out.println("Jugador " + jugador.getName() + " unido al equipo " + Config.Equipos[equipo]);
		myAgent.addBehaviour(new Enviar_Jugadores_Monitor(equipo, CEREBRO.getEquipo(equipo), CEREBRO.getMonitores()));

		ACLMessage msg = inMsg.createReply();
		msg.setPerformative(ACLMessage.INFORM);
		msg.setContent(CEREBRO.EnviarTableroaJugador(jugador));

		return msg;
	}

}