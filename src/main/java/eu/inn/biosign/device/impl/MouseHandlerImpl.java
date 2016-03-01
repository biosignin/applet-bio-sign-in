package eu.inn.biosign.device.impl;

/*
 * #%L
 * Java Applet for biometric trait acquisition [http://www.biosignin.org]
 * MouseHandlerImpl.java is part of BioSignIn project
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


import java.awt.Dimension;
//import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import eu.inn.biometric.signature.device.CapturingComponent;
import eu.inn.biometric.signature.device.MetricUnits;
import eu.inn.biometric.signature.device.Point;
import eu.inn.biometric.signature.device.RealSize;
import eu.inn.biometric.signature.device.SignArea;
import eu.inn.biometric.signature.managed.ManagedIsoPoint;
import eu.inn.biosign.BioSign;
import eu.inn.biosign.BioSign.Button;
import eu.inn.biosign.DeviceManager;
import eu.inn.biosign.device.BaseDeviceHandler;
import eu.inn.biosign.device.config.DeviceConfig;
import eu.inn.biosign.device.config.impl.MouseDeviceConfigImpl;
import eu.inn.configuration.ManagedIsoPointSimulated;

public class MouseHandlerImpl extends BaseDeviceHandler implements MouseListener, MouseMotionListener {
	private static long initTime = 0;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void penPressedSimulated(ManagedIsoPointSimulated penPoint) {
		pointsForBiometric.add(penPoint);
		// TODO Auto-generated method stub

	}

	private boolean empty = true;

	@Override
	public int getMaxPressure() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Dimension getDimension() {
		// TODO Auto-generated method stub
		// return TabletManager.getDimension();
		return DeviceManager._instance.getSize();
		// return new Dimension(800,480);

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		try {
			// JSTUTablet.StopCapture();
			isCapturing = false;
			clear();
			// JSTUTablet.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void clear() {
		initTime = 0;
		BioSign._instance.clearScaledPoints();
		try {
			pointsForRenderer.clear();
			pointsForBiometric.clear();
			BioSign._instance.repaint();

			// JSTUTablet.clearScreen();
		} catch (Exception e) {
			BioSign.showError(e);
		}
		empty = true;
	}

	@Override
	public void init() {

		try {
			BioSign._instance.addMouseListener(this);
			BioSign._instance.addMouseMotionListener(this);

		} catch (Throwable e) {
			e.printStackTrace();
			BioSign.showError(e);
		}
		// TODO Auto-generated method stub

	}

	boolean isCapturing = false;

	@Override
	public void acquireNext() {
		// TODO Auto-generated method stub
		try {
			// JSTUTablet.EnableTimecount();
			isCapturing = true;
			// JSTUTablet.StartCapture(true, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			BioSign.showError(e);
		}

	}

	@Override
	public void setBackgroundImage(BufferedImage combined) {

	}

	public Rectangle getActiveAreaForBackground() {
		return new Rectangle(0, 0,
		// getDimension().width, Math.round(getDimension().height/12f*10f)
				800, 399);
	}

	// ArrayList<ManagedIsoPoint> pointsForRendering = new
	// ArrayList<ManagedIsoPoint>();

	// @Override
	// public BufferedImage getSignatureImage() {
	// // TODO Auto-generated method stub
	// // ArrayList<PenPoint> points = JSTUTablet.getPenPoints();
	//
	// Rectangle rect = Device.getSignatureArea() == null ?
	// calculateImageRect(points)
	// : Device.getSignatureArea();
	// return getImage(points, rect, 3);
	// }
	// private static BufferedImage getImage(List<PointWithPressureAndTime>
	// points,
	// Rectangle paramRectangle, int paramInt)
	// {
	//
	// int i = paramRectangle.width;
	// int j = paramRectangle.height;
	//
	// BufferedImage localBufferedImage = new BufferedImage(i, j,
	// BufferedImage.TYPE_INT_ARGB);
	//
	// Graphics2D localGraphics2D = localBufferedImage.createGraphics();
	//
	// Device.DrawPoints(points, localGraphics2D, Color.BLACK, new Point(
	// paramRectangle.x, paramRectangle.y), paramInt);
	//
	// localGraphics2D.dispose();
	//
	// return localBufferedImage;
	// }
	//
	//
	//
	// private Rectangle calculateImageRect(List<PointWithPressureAndTime>
	// penPoints) {
	// int minX = Integer.MAX_VALUE;
	// int maxX = 0;
	// int minY = Integer.MAX_VALUE;
	// int maxY = 0;
	// for (PointWithPressureAndTime p : penPoints) {
	// if (p.pressure == 0)
	// continue;
	// if (DeviceUtil.getButton(p, getDeviceConfig()) != null) {
	// // System.out.println(getButton(p.getX(), p.getY()).name());
	// continue;
	// }
	// if (p.x < minX)
	// minX = p.x;
	// if (p.x > maxX)
	// maxX = p.x;
	// if (p.y < minY)
	// minY = p.y;
	// if (p.y > maxY)
	// maxY = p.y;
	// }
	// return new Rectangle(minX - 5, minY - 5, maxX - minX + 5, maxY - minY
	// + 5);
	//
	// }

	@Override
	public void populateDeviceInformation(CapturingComponent dInfo) {
		Dimension d = BioSign._instance.getSize();
		// Caps caps = JstuTableProxy.getCapabilities();
		eu.inn.biometric.signature.device.Dimension safe = new eu.inn.biometric.signature.device.Dimension(d.width, d.height);
		dInfo.setRealSize(new RealSize(MetricUnits.Pixels, safe));
		dInfo.setSignArea(new SignArea(new Point(0, 0), safe));
		dInfo.getTimeInfo().setSupported(true);
		dInfo.getTimeInfo().setTimeSupportDuringAirMoves(true);
		dInfo.getTimeInfo().setFixedSamplingRate(false);
		dInfo.getTimeInfo().setSamplingRatePointsPerSecond(0);
		dInfo.getPressure().setMaximum(getMaxPressure());
		dInfo.getPressure().setMinimum(0);
	}

	public void PenPressed(ManagedIsoPoint penPoint) {
		long actualTime = System.currentTimeMillis();
		if (initTime == 0 && penPoint.getPressure() == 0)
			return;
		if (initTime == 0) {
			if (penPoint.getTime() == 0)
				initTime = actualTime;
			else {
				isTimeSupported = true;
				initTime = penPoint.getTime();
			}
		}

		penPoint.setTime(((int) ((isTimeSupported ? penPoint.getTime() : actualTime) - initTime)));
		pointsForBiometric.add(penPoint);
		if (BioSign._instance.addScaledPoint(penPoint)) {			
			pointsForRenderer.add(penPoint);
			empty = false;
		}

		if ((penPoint.getPressure() > 0) && (!isDown)) {
			isDown = true;
			penDown(penPoint);
		} else if ((penPoint.getPressure() == 0) && (isDown)) {
			isDown = false;
			penUp(penPoint);
		} else {
			if (actualButton == null && penPoint.getPressure() > 0)
				empty = false;

		}

		// if (lastPoint != null) {
		// double d = Math
		// .sqrt(Math.pow(penPoint.getX() - lastPoint.getX(), 2)
		// + Math.pow(penPoint.getY() - lastPoint.getY(), 2));
		// int t = penPoint.getTime() - lastPoint.getTime();
		// double v = d / t;
		// if (v > vp)
		// vp = v;
		//
		// }
		lastPoint = penPoint;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return empty;
	}

	private void penDown(ManagedIsoPoint penPoint) {
		actualButton = getDeviceConfig().getButton(penPoint);
	}

	private boolean isDown = false;

	// private List<ManagedIsoPoint> buttonPoints = new
	// ArrayList<ManagedIsoPoint>();
	private Button actualButton;

	@SuppressWarnings("incomplete-switch")
	private void penUp(ManagedIsoPoint penPoint) {
		System.out.println("UP: "+actualButton);
		if ((actualButton != null) && (actualButton == getDeviceConfig().getButton(penPoint))) {
			if (actualButton == Button.AIRMODE) {
				BioSign._instance.pressAirModeButton();
				return;
			}
			// pointsForRendering.removeAll(buttonPoints);
			// buttonPoints.clear();
			try {
				switch (actualButton) {
				case OK:
					BioSign._instance.pressOkButton();
					break;
				case CLEAR:
					BioSign._instance.pressClearButton();
					break;
				case CANCEL:
					BioSign._instance.pressCancelButton();
					break;
				}
			} catch (Exception e) {
				BioSign.showError(e);
			}
		}
	}

	private boolean isTimeSupported = false;

	@Override
	public void mouseClicked(MouseEvent e) {		
		// System.out.println("clicked");
		genericMouseEvent(e);
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// System.out.println("pressed");
		genericMouseEvent(e);
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// System.out.println("released");
		genericMouseEvent(e);
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// System.out.println("entered");
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// System.out.println("exited");
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// System.out.println("dragged");
		genericMouseEvent(e);
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// System.out.println("moved");
		genericMouseEvent(e);

		// TODO Auto-generated method stub

	}

	private void genericMouseEvent(MouseEvent e) {
		if (!isCapturing)
			return;

		PenPressed(new ManagedIsoPoint(e.getX(), e.getY(), (e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) == 0 ? 0
				: 1));
	}

	// MouseDeviceConfigImpl config;

	@Override
	public DeviceConfig createDeviceConfig() {
		return new MouseDeviceConfigImpl();
	}

	@Override
	public String getTabletDescription() {
		// TODO Auto-generated method stub
		return "MOUSE";
	}

}
