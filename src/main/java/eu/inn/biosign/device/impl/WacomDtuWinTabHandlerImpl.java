package eu.inn.biosign.device.impl;

/*
 * #%L
 * Java Applet for biometric trait acquisition [http://www.biosignin.org]
 * WacomDtuWinTabHandlerImpl.java is part of BioSignIn project
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



import java.awt.GraphicsDevice;
import java.security.AccessController;
import java.security.PrivilegedAction;

import eu.inn.biometric.signature.device.CapturingComponent;
import eu.inn.biometric.signature.device.Dimension;
import eu.inn.biometric.signature.device.MetricUnits;
import eu.inn.biometric.signature.device.Point;
import eu.inn.biometric.signature.device.RealSize;
import eu.inn.biometric.signature.device.SignArea;
import eu.inn.biosign.util.StaticUtils;
import eu.inn.configuration.ManagedIsoPointSimulated;
import eu.inn.wintab.Jwintab;

public class WacomDtuWinTabHandlerImpl extends WacomDtuBaseHandlerImpl {

	public WacomDtuWinTabHandlerImpl() {
		super();
		capturingThread.start();
	}

	protected static boolean isInitialized = false;
	static {
		try {
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				public Object run() {
					try {
						if (StaticUtils.loadNative("Inno Software", "jwintab.dll", "native/jwintab.dll", true))
							isInitialized = true;
						return null;
					} catch (final Exception e) {
						e.printStackTrace();
						return null;
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getMaxPressure() {
		return 1023;
	}

	Thread capturingThread = new Thread() {
		public void run() {
			try {
				int arg[] = new int[6];
				int lastPressure = 0;
				while (true) {
					int res = Jwintab.getPacket(arg);
					if (res > 0) {
						if (!isCapturing) {
							Thread.sleep(1);
							continue;
						}
						ManagedIsoPointSimulated point = new ManagedIsoPointSimulated(arg[0], 800 - arg[1], arg[5],
								false, 1, 1);
						if (!isAirModeActive)
							penPressedSimulated(point);
						if (isAirModeActive) {
							int pressure = arg[5];
							if (lastPressure == 0 && pressure > 0)
								lastPoint = null;
							if (lastPressure > 0 || pressure > 0)
								penPressedSimulated(point);
							lastPressure = pressure;
						}
					} else if (res < 0) {
						System.out.println("error " + res);
						break;
					} else if (res == 0)
						Thread.sleep(1);
				}
			} catch (Exception ex) {
				return;
			}

		};
	};

	@Override
	public void populateDeviceInformation(CapturingComponent dInfo) {
		
		java.awt.Dimension d = getDimension();
		eu.inn.biometric.signature.device.Dimension safe = new eu.inn.biometric.signature.device.Dimension(d.width, d.height);
		dInfo.setRealSize(new RealSize(MetricUnits.Points, new Dimension(615, 384)));
		dInfo.setSignArea(new SignArea(new Point(0, 0), safe));
		

		dInfo.getTimeInfo().setSupported(true);
		dInfo.getTimeInfo().setTimeSupportDuringAirMoves(true);
		dInfo.getTimeInfo().setFixedSamplingRate(false);
		dInfo.getTimeInfo().setSamplingRatePointsPerSecond(0);
		dInfo.getPressure().setPressureSupported(true);
		dInfo.getPressure().setAirmovesSupported(true);
		dInfo.getPressure().setMaximum(getMaxPressure());
		dInfo.getPressure().setMinimum(0);
	}

	public static boolean isConnected() {
		if (!isInitialized)
			return false;
		try {
			GraphicsDevice device = getGraphicsDevice();
			if (device != null && Jwintab.open(1280, 800) == 1) {
				return true;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void destroy() {
		try {
			Jwintab.close();
		} catch (Exception ex) {
		}
	}

	@Override
	public String getTabletDescription() {
		return "WACOM DTU-1031";
	}

}
