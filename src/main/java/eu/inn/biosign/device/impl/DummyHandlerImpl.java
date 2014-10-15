package eu.inn.biosign.device.impl;

/*
 * #%L
 * Java Applet for biometric trait acquisition [http://www.biosignin.org]
 * DummyHandlerImpl.java is part of BioSignIn project
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
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import eu.inn.biometric.signature.device.CapturingComponent;
import eu.inn.biometric.signature.managed.ManagedIsoPoint;
import eu.inn.biosign.device.BaseDeviceHandler;
import eu.inn.biosign.device.config.DeviceConfig;
import eu.inn.configuration.ManagedIsoPointSimulated;

public class DummyHandlerImpl extends BaseDeviceHandler {
	

	public DummyHandlerImpl() {
	}


	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<? extends ManagedIsoPoint> getPointsForRenderer() {
		// TODO Auto-generated method stub
		return new ArrayList<ManagedIsoPoint>();
	}

	@Override
	public void setBackgroundImage(BufferedImage image) {
		// TODO Auto-generated method stub
		
	}


	
	@Override
	public DeviceConfig createDeviceConfig() {
		return new DeviceConfig() {
				
				@Override
				protected void setRetryButton(Rectangle rect) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void setPrevButton(Rectangle rect) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				protected void setOkButton(Rectangle rect) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void setNextButton(Rectangle rect) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				protected void setCancelButton(Rectangle rect) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				protected void setButtonsImage(Image img) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void setAirModeButton(Rectangle area) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				protected void setActiveAreaForBackground(Rectangle rect) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public boolean isButtonImageFixed() {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public boolean isAirModeSupported() {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				protected String getTemplatePath() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public int getSigThickness() {
					// TODO Auto-generated method stub
					return 0;
				}
				
				@Override
				public Rectangle getRetryButton() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Rectangle getPrevButton() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Rectangle getOkButton() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Rectangle getNextButton() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public double getDpiWidth() {
					
					// TODO Auto-generated method stub
					return 72;
				}
				
				@Override
				public Rectangle getCancelButton() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Image getButtonsImage() throws IOException {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Rectangle getAirModeButton() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Rectangle getActiveAreaForBackground() {
					// TODO Auto-generated method stub
					return new Rectangle(1,1);
//					return null;
				}
			};
	}

	@Override
	public void acquireNext() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public void populateDeviceInformation(CapturingComponent dInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void penPressedSimulated(ManagedIsoPointSimulated penPoint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Dimension getDimension() {
		// TODO Auto-generated method stub
		return new Dimension(1, 1);
	}



	@Override
	public int getMaxPressure() {
		// TODO Auto-generated method stub
		return 0;
	}




	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTabletDescription() {
		return "NO CONNECTED DEVICE";
	}





	

}
