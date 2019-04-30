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

import agente.behavs.grafico.Enviar_Jugadores_Monitor;
import config.Config;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import juegoCTF.Accion;
import juegoCTF.Cerebro;
import juegoCTF.Jugador;

/**
 * COMPORTAMIENTO QUE RECIBIRA LAS ACCIONES DE LOS JUGADORES
 */
@SuppressWarnings("serial")
public class RecibirAcciones extends CyclicBehaviour {

	Cerebro CEREBRO;

	public RecibirAcciones(Cerebro _cer) {
		super();
		CEREBRO = _cer;
	}

	public void action() {

		ACLMessage accion = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
		if (accion != null) {
			Jugador j = CEREBRO.getJugador(accion.getSender());

			if (j != null) {
				Accion acc = new Accion();

				try {
					if (accion.getContent().equals(Config.ABANDONAR))
						acc.setAbandonar(true);
					else if (accion.getContent().equals(Config.NULA))
						acc.setNula(true);
					else if (Config.ORIENTACION_RELATIVA && accion.getContent().equals(Config.ADELANTE))
						acc.setDesplazamiento(1);
					else if (Config.ORIENTACION_RELATIVA && accion.getContent().equals(Config.ATRAS))
						acc.setDesplazamiento(-1);
					else if (Config.ORIENTACION_RELATIVA && accion.getContent().startsWith(Config.ROTAR))
						acc.setGrados(Integer.parseInt(accion.getContent().substring(6)));
					else if (!Config.ORIENTACION_RELATIVA)
						acc.setOrientacion(accion.getContent());
					else
						acc.setIncorrecta(true);

				} catch (Exception ee) {
					acc.setIncorrecta(true);
				}

				if (acc.isIncorrecta()) {
					j.setAccion(acc);
					j.setPenalizacion(j.getPenalizacion() + 1);
					System.out.println("\n" + j.getName() + " Penalizado\n");
				} else if (acc.isAbandonar()) {
					j.setAccion(acc);
					j.vaciarTicks();
					CEREBRO.removeJugador(j.getEquipo(), j);
					System.out.println(j.getName() + " HA ABANDONADO");
					for (int i = 0; i < Config.NUM_EQUIPOS; i++) {
						myAgent.addBehaviour(
								new Enviar_Jugadores_Monitor(i, CEREBRO.getEquipo(i), CEREBRO.getMonitores()));

					}
				} else {
					j.setAccion(acc);
					j.resetTicks();
				}
				CEREBRO.addAccion(j);
			} else {
				System.out
						.println("El jugador '" + accion.getSender().getLocalName() + "' NO pertenece a la plataforma");
			}
		} else
			block();
	}

}
