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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import agente.behavs.grafico.Enviar_Grafico_Monitor;
import agente.behavs.grafico.Enviar_Jugadores_Monitor;
import config.Config;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;
import juegoCTF.Accion;
import juegoCTF.Cerebro;
import juegoCTF.Jugador;
import juegoCTF.Posicion;

/**
 * COMPORTAMIENTO QUE MARCA LOS TICKS EN LA PARTIDA
 */
@SuppressWarnings("serial")
public class Tick extends TickerBehaviour {

	Cerebro CEREBRO;
	// double Time = System.currentTimeMillis();
	int ticksCount = 0;

	// private Random random = new Random(System.currentTimeMillis());

	public Tick(Agent a, long period, Cerebro _cer) {
		super(a, period);
		CEREBRO = _cer;
		System.out.println("Comienzan los Ticks.");
		CEREBRO.partidaTXT.escribirPartida(CEREBRO.EnviarTableroaMonitor());
	}

	/**
	 * CADA TICK DEL JUEGO
	 */
	protected void onTick() {
		System.out.println("Tick: " + ticksCount);
		// for (int i = 0; i < Config.NUM_EQUIPOS; i++) {
		// System.out.println("EQUIPO " + i + ": " +
		// CEREBRO.getEquipo(i).size());
		// }
		// Time = System.currentTimeMillis();

		// System.out.println("\n\nNuevo TICK:");

		// 1. Comprobar, SI SE HABILITA DESCONEXION, si hay alguno para
		// desconectar.
		if (Config.DESCONEXION)
			for (int i = 0; i < Config.NUM_EQUIPOS; i++)
				comprobarDesconexion(i);

		// 2. Recoger la lista de Jugadores que tienen acciones pendientes
		Hashtable<Jugador, Accion> lj = CEREBRO.getListaAcciones();

		if (!lj.isEmpty()) {

			Enumeration<Jugador> en = lj.keys();

			while (en.hasMoreElements()) {

				Jugador j = en.nextElement();

				if (j == null)
					continue;

				if (j.getPenalizacion() == 0) {
					Posicion posAnt = j.getPosicion();
					Posicion posFin = CEREBRO.calcularPos(j);

					// Si hay pared:
					if (CEREBRO.esPared(posFin)) {
						System.out.println(j.getLocalName() + " -> CHOCA CON PARED");
						posFin = posAnt;
					}
					// Si hay alguien de mi equipo:
					else if (CEREBRO.getJugador(j.getEquipo(), posFin) != null
							&& CEREBRO.getJugador(j.getEquipo(), posFin) != j) {
						System.out.println(j.getLocalName() + " -> CHOCA CON AMIGO");
						posFin = posAnt;
					}
					// Si hay algun enemigo:
					else if (CEREBRO.getEnemigo(j.getEquipo(), posFin) != null) {
						System.out.println(j.getLocalName() + " -> ENTRA EN PELEA CON "
								+ CEREBRO.getEnemigo(j.getEquipo(), posFin).getLocalName());
						pelea(j, CEREBRO.getEnemigo(j.getEquipo(), posFin).getEquipo(), posFin);
					}
					// Si llegamos a nuestra base:
					else if (posFin.equals(CEREBRO.getBase(j.getEquipo()))) {
						System.out.println(j.getLocalName() + " -> LLEGA A BASE PROPIA");
						llegarABasePropia(j, posFin, posAnt);
					}
					// Si llegamos a base contraria:
					// TODO:
					else if (CEREBRO.getBaseContrariaEn(j.getEquipo(), posFin) != -1) {
						System.out.println(j.getLocalName() + " -> LLEGA A BASE CONTRARIA");
						j.setTieneBandera(CEREBRO.getBaseContrariaEn(j.getEquipo(), posFin), true);
					}
					// Si cogemos nuestra bandera:
					else if (posFin.equals(CEREBRO.getBandera(j.getEquipo()))) {
						System.out.println(j.getLocalName() + " -> COGE BANDERA PROPIA");
						CEREBRO.setBandera(j.getEquipo(), CEREBRO.getBase(j.getEquipo()));
					}
					// Si cogemos bandera contraria:
					else if (CEREBRO.getBanderaContrariaEn(j.getEquipo(), posFin) != -1) {
						System.out.println(j.getLocalName() + " -> COGE BANDERA CONTRARIA");
						j.setTieneBandera(CEREBRO.getBanderaContrariaEn(j.getEquipo(), posFin), true);
					} else {
						// TODO: ESTO ES UN ELSE????
						for (int i = 0; i < Config.NUM_EQUIPOS; i++)
							if (j.isTieneBandera(i))
								CEREBRO.setBandera(i, posFin);
					}

					j.setPosicion(posFin);
					CEREBRO.estadisticas.add_tick(j.getLocalName(), j.getEquipo());
					// System.out.println("Nueva pos : " + j.getName() + "
					// acc:
					// " + j.getPosicion());

					// PENALIZACION > 0
				} else {
					//// BAJANDO PENALIZACION
					j.setPenalizacion(j.getPenalizacion() - 1);
				}

				CEREBRO.removeAccion(j);
			}
			lj.remove(en);
		}

		//// UNA VEZ TERMINADO... SE INFORMA A _TODO_ EL MUNDO DEL NUEVO
		//// CICLO
		for (int i = 0; i < Config.NUM_EQUIPOS; i++)
			Actualizacion(CEREBRO.getEquipo(i));

		//// INFORMAR A LOS MONITORES
		// Al escribir, borramos muertes para pintarlas 1 vez. Al
		//// partida.txt
		//// llegaban borradas
		String actualizacion = CEREBRO.ActualizarTableroaMonitor();
		if (!CEREBRO.getMonitores().isEmpty()) {
			myAgent.addBehaviour(new Enviar_Grafico_Monitor(actualizacion, CEREBRO.getMonitores()));
		}

		//// ESCRIBIR TICK EN FICHERO
		if (CEREBRO.hayJugadores())
			CEREBRO.partidaTXT.escribirPartida(actualizacion);
		ticksCount++;
		if (ticksCount > Config.TicksMaximos) {
			////////////////////////////////////////////////////////////////////////////////////////
			System.out.println("Juego terminado por limite de Ticks: " + Config.TicksMaximos);

			for (int i = 0; i < Config.NUM_EQUIPOS; i++)
				enviarGameOver(i, Config.PIERDE);
			enviarGameOveraMonitores("Juego terminado por limite de Ticks: " + Config.TicksMaximos);
			CEREBRO.partidaTXT.escribirPartidaFin("Juego terminado por limite de Ticks: " + Config.TicksMaximos);
			CEREBRO.estadisticas.ganador(null, -1);
			CEREBRO.estadisticas.escribirEstadisticas();

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("Empiezo a cerrar...");
			ACLMessage shutdownMessage = new ACLMessage(ACLMessage.REQUEST);
			Codec codec = new SLCodec();
			myAgent.getContentManager().registerLanguage(codec);
			myAgent.getContentManager().registerOntology(JADEManagementOntology.getInstance());
			shutdownMessage.addReceiver(myAgent.getAMS());
			shutdownMessage.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
			shutdownMessage.setOntology(JADEManagementOntology.getInstance().getName());
			try {
				myAgent.getContentManager().fillContent(shutdownMessage,
						new Action(myAgent.getAID(), new ShutdownPlatform()));
				myAgent.send(shutdownMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Cierro...");
			// try {
			// AgentContainer cont = myAgent.getContainerController();
			// PlatformController pc = cont.getPlatformController();
			// // cont.kill();
			// pc.kill();
			// myAgent.doDelete();
			// } catch (StaleProxyException e) {
			// e.printStackTrace();
			// } catch (ControllerException e) {
			// e.printStackTrace();
			// }
			// System.out.println("Kill...");
		}
	}

	/**
	 * ACTUALIZACION DEL TICK PARA LOS JUGADORES
	 */
	public void Actualizacion(List<Jugador> equipo) {
		Iterator<Jugador> it;

		it = equipo.iterator();
		while (it.hasNext()) {
			try {
				Jugador jug = it.next();
				// System.out.println("ACTUALIZO " + jug.getName());
				ACLMessage msg = jug.enviarMapa(CEREBRO.ActualizarTableroaJugador(jug));
				msg.setConversationId("actual");
				myAgent.send(msg);
				// jug.setTiempoVida(jug.getTiempoVida() + 1);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * ENVIAR FIN A LOS EQUIPOS
	 */
	private void enviarGameOver(int equipo, String FIN) {
		List<Jugador> eq = CEREBRO.getEquipo(equipo);

		Iterator<Jugador> it = eq.iterator();
		while (it.hasNext()) {
			Jugador j = it.next();
			ACLMessage msg = j.enviar(FIN);
			myAgent.send(msg);
		}

	}

	/**
	 * ENVIA EL FINAL DE LA PARTIDA A LOS MONITORES
	 */
	private void enviarGameOveraMonitores(String gameover) {
		// El servidor muere antes de realizar el comportamiento
		// myAgent.addBehaviour(new Enviar_Informacion_Monitor("Juego terminado:
		// Ganador Equipo " + equi, CEREBRO.getMonitores()));
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		Iterator<AID> i = CEREBRO.getMonitores().iterator();
		while (i.hasNext()) {
			msg.addReceiver(i.next());
		}
		msg.setContent(gameover);
		msg.setConversationId("Info");
		myAgent.send(msg);
	}

	/**
	 * COMPROBAR LA DESCONEXION DE CADA UNO DE LOS JUGADORES
	 */
	private void comprobarDesconexion(int equipo) {
		List<Jugador> eq = CEREBRO.getEquipo(equipo);
		List<Jugador> BORRAR = new ArrayList<Jugador>();

		Iterator<Jugador> it = eq.iterator();
		while (it.hasNext()) {
			Jugador j = it.next();
			if (!j.quedanTicks()) {
//				System.out.println("BORRAR POR TICKS: " + j.getName());
				BORRAR.add(j);
				ACLMessage msg = j.enviar(Config.PIERDE);
				myAgent.send(msg);
			} else
				j.decrementarTicks();
		}
		eq.removeAll(BORRAR);

		if (BORRAR.size() > 0)
			myAgent.addBehaviour(
					new Enviar_Jugadores_Monitor(equipo, CEREBRO.getEquipo(equipo), CEREBRO.getMonitores()));

	}

	/**
	 * CUANDO DOS JUGADORES CONTRARIOS COINCIDEN EN LA MISMA POSICION
	 */
	private void pelea(Jugador j, int equipo_contrario, Posicion posFin) {
		Jugador c = CEREBRO.getJugador(equipo_contrario, posFin);
		CEREBRO.removeJugador(j.getEquipo(), j);
		CEREBRO.removeJugador(equipo_contrario, c);

		// TODO para eliminar por nombre por conflictos en hashtable
		// CEREBRO.removeJugador(equipo_propio, j.getName());
		// CEREBRO.removeJugador(equipo_contrario, c.getName());

		// TODO
		CEREBRO.removeAccion(j);
		CEREBRO.removeAccion(c);

		CEREBRO.estadisticas.add_muerte(j.getLocalName(), j.getEquipo(), c.getLocalName(), c.getEquipo());

		ACLMessage msg_j = j.enviar(Config.PIERDE);
		ACLMessage msg_jc = c.enviar(Config.PIERDE);
		myAgent.send(msg_j);
		myAgent.send(msg_jc);

		myAgent.addBehaviour(
				new Enviar_Jugadores_Monitor(j.getEquipo(), CEREBRO.getEquipo(j.getEquipo()), CEREBRO.getMonitores()));
		myAgent.addBehaviour(
				new Enviar_Jugadores_Monitor(c.getEquipo(), CEREBRO.getEquipo(c.getEquipo()), CEREBRO.getMonitores()));

		// TODO: VER QUE PASA CON BANDERA
		j.setNoTieneBandera();
		c.setNoTieneBandera();

		CEREBRO.addMuerte(posFin);
		/*Jugador c = CEREBRO.getJugador(equipo_contrario, posFin);
		j.setLife(j.getLife() - random.nextInt(c.getStrength()));
		c.setLife(c.getLife() - random.nextInt(j.getStrength()));
		
		// TODO para eliminar por nombre por conflictos en hashtable
		// CEREBRO.removeJugador(equipo_propio, j.getName());
		// CEREBRO.removeJugador(equipo_contrario, c.getName());
		
		// TODO
		if (j.getLife() <= 0) {
			CEREBRO.removeJugador(j.getEquipo(), j);
			CEREBRO.removeAccion(j);
		
			ACLMessage msg_j = j.enviar(String.valueOf(Config.GAME_OVER));
			myAgent.send(msg_j);
			myAgent.addBehaviour(new Enviar_Jugadores_Monitor(j.getEquipo(), CEREBRO.getEquipo(j.getEquipo()),
					CEREBRO.getMonitores()));
		
			j.setNoTieneBandera();
			if (c.getLife() > 0)
				if (c.getPosicion().equals(CEREBRO.getBandera(c.getEquipo()))) {
					System.out.println(c.getLocalName() + " -> COGE BANDERA PROPIA");
					CEREBRO.setBandera(c.getEquipo(), CEREBRO.getBase(c.getEquipo()));
				} else if (CEREBRO.getBanderaContrariaEn(c.getEquipo(), c.getPosicion()) != -1) {
					System.out.println(c.getLocalName() + " -> COGE BANDERA CONTRARIA");
					c.setTieneBandera(CEREBRO.getBanderaContrariaEn(c.getEquipo(), c.getPosicion()), true);
				}
		}
		if (c.getLife() <= 0) {
			CEREBRO.removeJugador(equipo_contrario, c);
			CEREBRO.removeAccion(c);
		
			ACLMessage msg_jc = c.enviar(String.valueOf(Config.GAME_OVER));
			myAgent.send(msg_jc);
			myAgent.addBehaviour(new Enviar_Jugadores_Monitor(c.getEquipo(), CEREBRO.getEquipo(c.getEquipo()),
					CEREBRO.getMonitores()));
		
			c.setNoTieneBandera();
			if (j.getLife() > 0)
				if (posFin.equals(CEREBRO.getBandera(j.getEquipo()))) {
					System.out.println(j.getLocalName() + " -> COGE BANDERA PROPIA");
					CEREBRO.setBandera(j.getEquipo(), CEREBRO.getBase(j.getEquipo()));
				} else if (CEREBRO.getBanderaContrariaEn(j.getEquipo(), posFin) != -1) {
					System.out.println(j.getLocalName() + " -> COGE BANDERA CONTRARIA");
					j.setTieneBandera(CEREBRO.getBanderaContrariaEn(j.getEquipo(), posFin), true);
				}
		}
		
		CEREBRO.estadisticas.add_muerte(j.getLocalName(), j.getEquipo(), c.getLocalName(), c.getEquipo());
		CEREBRO.addMuerte(posFin);*/
	}

	/**
	 * CUANDO UN JUGADOR LLEGA A SU BASE
	 */
	private void llegarABasePropia(Jugador j, Posicion posFin, Posicion posAnt) {

		if (j.isTieneBandera(j.getEquipo())) {
			j.setTieneBandera(j.getEquipo(), false);
			CEREBRO.setBandera(j.getEquipo(), posFin);
			posFin = posAnt;

		} else if (j.isTieneAlgunaBandera()) {

			if (posFin.equals(CEREBRO.getBandera(j.getEquipo()))) {

				System.out.println("Juego terminado: Ganador Equipo " + Config.Equipos[j.getEquipo()]
						+ "\n Gracias al jugador: " + j.getLocalName());

				for (int i = 0; i < Config.NUM_EQUIPOS; i++)
					if (i != j.getEquipo())
						enviarGameOver(i, Config.PIERDE);
					else
						enviarGameOver(i, Config.GANA);

				enviarGameOveraMonitores("Juego terminado: Ganador Equipo " + Config.Equipos[j.getEquipo()]
						+ "\n Gracias al jugador: " + j.getLocalName());
				CEREBRO.partidaTXT.escribirPartidaFin("Juego terminado: Ganador Equipo " + Config.Equipos[j.getEquipo()]
						+ "\n Gracias al jugador: " + j.getLocalName());
				CEREBRO.estadisticas.ganador(j.getLocalName(), j.getEquipo());
				CEREBRO.estadisticas.escribirEstadisticas();

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("Empiezo a cerrar...");
				ACLMessage shutdownMessage = new ACLMessage(ACLMessage.REQUEST);
				Codec codec = new SLCodec();
				myAgent.getContentManager().registerLanguage(codec);
				myAgent.getContentManager().registerOntology(JADEManagementOntology.getInstance());
				shutdownMessage.addReceiver(myAgent.getAMS());
				shutdownMessage.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
				shutdownMessage.setOntology(JADEManagementOntology.getInstance().getName());
				try {
					myAgent.getContentManager().fillContent(shutdownMessage,
							new Action(myAgent.getAID(), new ShutdownPlatform()));
					myAgent.send(shutdownMessage);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("Cierro...");
				// AgentContainer cont = myAgent.getContainerController();
				// try {
				// PlatformController pc = cont.getPlatformController();
				// pc.kill();
				// } catch (StaleProxyException e) {
				// e.printStackTrace();
				// } catch (ControllerException e) {
				// e.printStackTrace();
				// }
			} else
				posFin = posAnt;

		} else {
			posFin = posAnt;

		}
	}

}
