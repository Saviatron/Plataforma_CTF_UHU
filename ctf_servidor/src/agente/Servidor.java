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


package agente;

import agente.behavs.Inicializar;
import jade.core.Agent;

/**
 * AGENTE QUE ARRANCA LA PLATAFORMA
 */
@SuppressWarnings("serial")
public class Servidor extends Agent {

	public void setup() {
		addBehaviour(new Inicializar(this));
	}

}
