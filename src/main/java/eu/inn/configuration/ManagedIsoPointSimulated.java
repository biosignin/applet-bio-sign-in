package eu.inn.configuration;

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
