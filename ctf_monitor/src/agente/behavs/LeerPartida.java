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

import config.Config_GUI;
import gui.GUI_Interactuar;
import gui.GUI_Mapa;
import gui.LeerFichero;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

/**
 * CLASE QUE VISUALIZA LA PARTIDA DESDE UN TXT
 */
@SuppressWarnings("serial")
public class LeerPartida extends TickerBehaviour {
	private GUI_Mapa sketch;
	private GUI_Interactuar ventana;
	private LeerFichero lector;
	private String partida;
	private StringTokenizer tick;

	public LeerPartida(Agent a, long period, GUI_Mapa sketch, GUI_Interactuar ventana) {
		super(a, period);
		this.sketch = new GUI_Mapa();
		lector = new LeerFichero(Config_GUI.partida);
		partida = lector.leerContenido();
		tick = new StringTokenizer(partida, ";");
		String primerMSG = tick.nextToken();
		this.sketch.initMapa(primerMSG);
		this.ventana = new GUI_Interactuar("Panel de Control", primerMSG, this.sketch, true);
		this.sketch.run();
		this.ventana.setVisible(true);
	}

	@Override
	protected void onTick() {
		// TODO Auto-generated method stub
		if (tick.hasMoreTokens() && ventana.getPlay())
			sketch.update(tick.nextToken());
	}
}