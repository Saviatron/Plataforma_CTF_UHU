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


package gui;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PVector;

public class Particula {
	private float r = 2;
	private PVector pos, speed, grav;
	private ArrayList<PVector> tail;
	private float splash = 4;
	private int margin = 2;
	private int taillength = 4;
	private PApplet parent;

	public Particula(float tempx, float tempy, float tempz, PApplet parent) {
		this.parent = parent;
		float startx = tempx + parent.random(-splash, splash);
		float starty = tempy + parent.random(-splash, splash);
		float startz = tempz + parent.random(0, splash * 3);
		startx = PApplet.constrain(startx, 0, parent.width);
		starty = PApplet.constrain(starty, 0, parent.height);
		float xspeed = parent.random(-3, 3);
		float yspeed = parent.random(-3, 3);
		float zspeed = parent.random(7, 10);

		pos = new PVector(startx, starty, startz);
		speed = new PVector(xspeed, yspeed, zspeed);
		grav = new PVector(0, 0, (float) -0.5);

		tail = new ArrayList<PVector>();
	}

	public void run() {
		pos.add(speed);

		tail.add(new PVector(pos.x, pos.y, pos.z));
		if (tail.size() > taillength) {
			tail.remove(0);
		}

		float damping = parent.random((float) -0.5, (float) -0.6);
		if (pos.x > parent.width - margin || pos.x < margin) {
			speed.x *= damping;
		}
		if (pos.y > parent.height - margin) {
			speed.y *= damping;
		}
		// speed.z *= damping;
	}

	public void gravity() {
		speed.add(grav);
	}

	public void update() {
		for (int i = 0; i < tail.size(); i++) {
			PVector tempv = (PVector) tail.get(i);
			parent.noStroke();
			parent.fill(255, parent.random(0, 255), 0);
			parent.pushMatrix();
			parent.sphereDetail(0);
			parent.translate(tempv.x, tempv.y, tempv.z);
			parent.sphere(r);
			parent.popMatrix();
		}
	}

	public PVector getPos() {
		return pos;
	}
}