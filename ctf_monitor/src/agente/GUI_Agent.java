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

import agente.behavs.Busqueda;
import agente.behavs.LeerPartida;
import config.Config_GUI;
import gui.GUI_Interactuar;
import gui.GUI_Mapa;
import jade.core.Agent;

/**
 * AGENTE QUE ARRANCA EL MONITOR
 */
@SuppressWarnings("serial")
public class GUI_Agent extends Agent {

	GUI_Mapa sketch = null;
	GUI_Interactuar ventana = null;

	protected void setup() {
		System.out.println("Arrancar Monitor.");
		if (Config_GUI.SERVIDOR)
			addBehaviour(new Busqueda(sketch, ventana));
		else
			addBehaviour(new LeerPartida(this, 500, sketch, ventana));
	}
}