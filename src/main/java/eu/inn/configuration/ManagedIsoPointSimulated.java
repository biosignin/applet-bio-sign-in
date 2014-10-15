package eu.inn.configuration;

/*
 * #%L
 * Java Applet for biometric trait acquisition [http://www.biosignin.org]
 * ManagedIsoPointSimulated.java is part of BioSignIn project
 * %%
 * Copyright (C) 2014 Innovery SpA
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import java.awt.geom.Point2D;

import eu.inn.biometric.signature.managed.ManagedIsoPoint;

public class ManagedIsoPointSimulated extends ManagedIsoPoint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean simulated;

	private Point2D.Double origLocation;

	public ManagedIsoPointSimulated(double x, double y, int pressure, boolean simulated, double ratioX,
			double ratioY) {
		super((int) Math.round(x * ratioX), (int) Math.round(y * ratioY), pressure);
		origLocation = new Point2D.Double(x, y);
		this.simulated = simulated;
	}

	public Point2D.Double getOrigLocation() {
		return origLocation;
	}

}
