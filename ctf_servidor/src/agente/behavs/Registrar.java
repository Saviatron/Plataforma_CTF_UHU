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

import config.Config;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

/**
 * COMPORTAMIENTO QUE REGISTRA AL SERVIDOR
 */
@SuppressWarnings("serial")
public class Registrar extends Behaviour {

	private boolean registrado = false;

	public void action() {
		System.out.println("El Entorno se intenta registar...");

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(myAgent.getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(Config.TIPO_SERVICIO);
		sd.setName(Config.NOMBRE_SERVICIO);
		dfd.addServices(sd);
		try {
			DFService.register(myAgent, dfd);
			System.out.println("Entorno Registrado.");
			registrado = true;
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	public boolean done() {
		return registrado;
	}
}
