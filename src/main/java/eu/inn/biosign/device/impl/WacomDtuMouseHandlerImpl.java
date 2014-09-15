package eu.inn.biosign.device.impl;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import eu.inn.biometric.signature.device.CapturingComponent;
import eu.inn.biometric.signature.device.MetricUnits;
import eu.inn.biometric.signature.device.RealSize;
import eu.inn.biometric.signature.device.SignArea;
import eu.inn.configuration.ManagedIsoPointSimulated;

public class WacomDtuMouseHandlerImpl extends WacomDtuBaseHandlerImpl implements MouseListener,
		MouseMotionListener {

	public WacomDtuMouseHandlerImpl() {
		super();
	}
	@Override
	public int getMaxPressure() {
		return 1;
	}



	@Override
	public void populateDeviceInformation(CapturingComponent dInfo) {
		dInfo.setRealSize(new RealSize(MetricUnits.Points, new Dimension(615, 384)));
		dInfo.setSignArea(new SignArea(new Point(0, 0), getDimension()));
		dInfo.getTimeInfo().setSupported(true);
		dInfo.getPressure().setMaximum(getMaxPressure());
		dInfo.getPressure().setMinimum(0);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		genericMouseEvent(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		genericMouseEvent(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		genericMouseEvent(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		genericMouseEvent(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		genericMouseEvent(e);
	}

	private void genericMouseEvent(MouseEvent e) {
		if (!isCapturing)
			return;
		penPressedSimulated(new ManagedIsoPointSimulated(e.getX(), e.getY(),
				(e.getModifiers() & InputEvent.BUTTON1_MASK) == 0 ? 0 : 1, false, 1, 1));
	}

	public static boolean isConnected() {
		return getGraphicsDevice() != null;
	}

	@Override
	public String getTabletDescription() {
		return "WACOM DTU-1031 (No pressure)";
	}

}
