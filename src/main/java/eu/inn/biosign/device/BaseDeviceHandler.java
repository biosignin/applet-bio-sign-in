package eu.inn.biosign.device;

/*
 * #%L
 * Java Applet for biometric trait acquisition [http://www.biosignin.org]
 * BaseDeviceHandler.java is part of BioSignIn project
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


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.inn.biometric.signature.device.CapturingComponent;
import eu.inn.biometric.signature.managed.ManagedIsoPoint;
import eu.inn.biometric.signature.renderer.ImageRenderer;
import eu.inn.biosign.device.config.DeviceConfig;
import eu.inn.configuration.ManagedIsoPointSimulated;

public abstract class BaseDeviceHandler {
	// void start();

	protected final List<ManagedIsoPoint> pointsForRenderer = new ArrayList<ManagedIsoPoint>();
	protected final ArrayList<ManagedIsoPoint> pointsForBiometric = new ArrayList<ManagedIsoPoint>();
	
	
	protected ManagedIsoPoint lastPoint = null;

	public final void resetLastPoint() {
		lastPoint = null;
	}

	public abstract void stop();

	public abstract void init();

	public abstract void setBackgroundImage(BufferedImage image);

	private DeviceConfig config;

	public final DeviceConfig getDeviceConfig() {
		if (config == null)
			config = createDeviceConfig();
		return config;
	}

	protected abstract DeviceConfig createDeviceConfig();

	public abstract void acquireNext();

	public abstract void clear();

	public abstract boolean isEmpty();

	public List<? extends ManagedIsoPoint> getPointsForRenderer(){
		return pointsForRenderer;
	}

//	protected abstract ArrayList<ManagedIsoPoint> getPointsForBiometricData();

	public final ArrayList<ManagedIsoPoint> getClearedPointsForBiometricData() {
//		ArrayList<ManagedIsoPoint> pointsForBiometric = getPointsForBiometricData();
		// get points inside buttons
		List<ManagedIsoPoint> toRemove = new ArrayList<ManagedIsoPoint>();
		for (ManagedIsoPoint penPoint : pointsForBiometric) {
			if (getDeviceConfig().getButton(penPoint) != null)
				toRemove.add(penPoint);
		}
		pointsForBiometric.removeAll(toRemove);

		// get blank points at the end
		toRemove = new ArrayList<ManagedIsoPoint>();
		Collections.reverse(pointsForBiometric);
		for (ManagedIsoPoint penPoint : pointsForBiometric) {
			if (penPoint.getPressure() > 0)
				break;
			toRemove.add(penPoint);
		}
		pointsForBiometric.removeAll(toRemove);

		// get blank points at the begin
		toRemove = new ArrayList<ManagedIsoPoint>();
		Collections.reverse(pointsForBiometric);
		for (ManagedIsoPoint penPoint : pointsForBiometric) {
			if (penPoint.getPressure() > 0)
				break;
			toRemove.add(penPoint);
		}
		pointsForBiometric.removeAll(toRemove);

		// set start-relative time
		long initTime = pointsForBiometric.iterator().next().getTime();
		for (ManagedIsoPoint p : pointsForBiometric) {
			p.setTime((int) (p.getTime() - initTime));
		}
		return pointsForBiometric;

	}

	public abstract void populateDeviceInformation(CapturingComponent dInfo);

	public abstract void penPressedSimulated(ManagedIsoPointSimulated penPoint);

	public abstract Dimension getDimension();

	public abstract int getMaxPressure();

	public void toggleAirMode() {
	};

	public void setEnableDocView(boolean enableDocView) {
	};

	public abstract void destroy();

	public abstract String getTabletDescription();

	public void setForceAirMode(boolean forceAirMode) {
	}

	public boolean getForceAirMode() {
		return false;
	}

	public void setLoading(boolean loading, boolean forceRepaint) {
	};

	public boolean isAirModeActive() {
		return false;
	}

	public BufferedImage getSignatureImage() {
		List<? extends ManagedIsoPoint> points = getPointsForRenderer();
		Rectangle rect = null;
		if (DeviceConfig.getSignatureArea() != null) {
			rect = DeviceConfig.getSignatureArea();
			// rect.translate(-dHandler.getDeviceConfig().getActiveAreaForBackground().x,-
			// dHandler.getDeviceConfig().getActiveAreaForBackground().y);
		} else {
			rect = calculateImageRect(points);
		}
		return getImage(points, rect, getDeviceConfig().getSigThickness(), getMaxPressure());
	}

	private BufferedImage getImage(List<? extends ManagedIsoPoint> points, Rectangle paramRectangle, int sigThickness,
			int maxPressure) {
		int i = paramRectangle.width;
		int j = paramRectangle.height;
		BufferedImage localBufferedImage = new BufferedImage(i, j, BufferedImage.TYPE_INT_ARGB);
		Graphics2D localGraphics2D = localBufferedImage.createGraphics();
		ImageRenderer.drawPoints(points, localGraphics2D, Color.BLACK, new Point(paramRectangle.x, paramRectangle.y),
				sigThickness, maxPressure);
		localGraphics2D.dispose();
		return localBufferedImage;
	}

	private Rectangle calculateImageRect(List<? extends ManagedIsoPoint> penPoints) {
		DeviceConfig deviceConfig = getDeviceConfig();
		int minX = Integer.MAX_VALUE;
		int maxX = 0;
		int minY = Integer.MAX_VALUE;
		int maxY = 0;
		for (ManagedIsoPoint p : penPoints) {
			if (p.getPressure() == 0)
				continue;
			if (deviceConfig.getButton(p) != null) {
				continue;
			}
			if (p.getX() < minX)
				minX = p.getX();
			if (p.getX() > maxX)
				maxX = p.getX();
			if (p.getY() < minY)
				minY = p.getY();
			if (p.getY() > maxY)
				maxY = p.getY();
		}
		return new Rectangle(minX - deviceConfig.getSigThickness(), minY - deviceConfig.getSigThickness(), maxX - minX
				+ (deviceConfig.getSigThickness() * 2), maxY - minY + (deviceConfig.getSigThickness() * 2));

	}

}
