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

package juegoCTF;

import config.Config;

public class Accion {

	private int desplazamiento;
	private String orientacion;
	private int grados;
	private boolean incorrecta;
	private boolean nula;
	private boolean abandonar;

	public Accion(int desplazamiento, String orientacion) {
		this.desplazamiento = desplazamiento > 1 ? 1 : desplazamiento;
		this.orientacion = orientacion;
		this.incorrecta = false;
		this.abandonar = false;
		switch (orientacion) {
		case Config.NORTE:
			grados = 0;
			break;
		case Config.NE:
			grados = 45;
			break;
		case Config.ESTE:
			grados = 90;
			break;
		case Config.SE:
			grados = 135;
			break;
		case Config.SUR:
			grados = 180;
			break;
		case Config.SW:
			grados = 225;
			break;
		case Config.OESTE:
			grados = 270;
			break;
		case Config.NW:
			grados = 315;
			break;
		case Config.ABANDONAR:
			grados = 0;
			this.abandonar = true;
			break;
		default:
			grados = 0;
			incorrecta = true;
		}
		this.nula = desplazamiento == 0 ? true : false;
	}

	public Accion(int desplazamiento, int grados) {
		this.desplazamiento = desplazamiento > 1 ? 1 : desplazamiento;
		this.orientacion = null;
		this.grados = grados;
		this.incorrecta = grados % 45 == 0 ? false : true;
		this.nula = desplazamiento == 0 ? true : false;
		this.abandonar = false;
	}

	public int getDesplazamiento() {
		return desplazamiento;
	}

	public void setDesplazamiento(int desplazamiento) {
		this.desplazamiento = desplazamiento;
	}

	public String getOrientacion() {
		return orientacion;
	}

	public void setOrientacion(String orientacion) {
		this.orientacion = orientacion;
	}

	public int getGrados() {
		return grados;
	}

	public void setGrados(int grados) {
		this.grados = grados;
	}

	public boolean isIncorrecta() {
		return incorrecta;
	}

	public void setIncorrecta(boolean incorrecta) {
		this.incorrecta = incorrecta;
	}

	public boolean isNula() {
		return nula;
	}

	public void setNula(boolean nula) {
		this.nula = nula;
	}

	public boolean isAbandonar() {
		return abandonar;
	}

	public void setAbandonar(boolean abandonar) {
		this.abandonar = abandonar;
	}

	@Override
	public String toString() {
		String ret;
		if (incorrecta)
			ret = "Accion Incorrecta";
		else if (nula)
			ret = "Accion Nula";
		else if (abandonar)
			ret = "Accion Abandonar";
		else if (orientacion == null)
			ret = "Accion Relativa: " + desplazamiento + " , " + grados;
		else
			ret = "Accion Absoluta: " + desplazamiento + " , " + orientacion + ":" + grados;
		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Accion other = (Accion) obj;
		if (abandonar != other.abandonar)
			return false;
		if (desplazamiento != other.desplazamiento)
			return false;
		if (grados != other.grados)
			return false;
		if (incorrecta != other.incorrecta)
			return false;
		if (nula != other.nula)
			return false;
		if (orientacion == null) {
			if (other.orientacion != null)
				return false;
		} else if (!orientacion.equals(other.orientacion))
			return false;
		return true;
	}
}
