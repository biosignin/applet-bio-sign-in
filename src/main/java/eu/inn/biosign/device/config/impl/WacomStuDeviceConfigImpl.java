package eu.inn.biosign.device.config.impl;

/*
 * #%L
 * Java Applet for biometric trait acquisition [http://www.biosignin.org]
 * WacomStuDeviceConfigImpl.java is part of BioSignIn project
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


import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;

import eu.inn.biosign.device.config.DeviceConfig;

public class WacomStuDeviceConfigImpl extends DeviceConfig {

@Override
public Rectangle getNextButton() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public Rectangle getPrevButton() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public void setNextButton(Rectangle rect) {
	// TODO Auto-generated method stub
	
}
@Override
public void setPrevButton(Rectangle rect) {
	// TODO Auto-generated method stub
	
}
//	public static int buttonOkX1 = 280;
//	public static int buttonOkY1 = 420;
//	public static int buttonOkX2 = 420;
//	public static int buttonOkY2 = 460;
//	public static int buttonClearX1 = 446;
//	public static int buttonClearY1 = 420;
//	public static int buttonClearX2 = 586;
//	public static int buttonClearY2 = 462;
//	public static int buttonCancelX1 = 625;
//	public static int buttonCancelY1 = 420;
//	public static int buttonCancelX2 = 765;
//	public static int buttonCancelY2 = 460;

@Override
public boolean isAlphaSupported() {
	// TODO Auto-generated method stub
	return false;
}

	Rectangle retryButton = new Rectangle(725, 700, 220, 75);
	Rectangle okButton = new Rectangle(446, 420, 140, 40);
	Rectangle cancelButton = new Rectangle(625, 420, 140, 40);

	
	@Override
	protected void setCancelButton(Rectangle rect) {
		cancelButton=rect;	
	}
	
	@Override
	protected void setOkButton(Rectangle rect) {
		okButton=rect;
	}

	@Override
	public Rectangle getOkButton() {
		return okButton;
	}
	
	@Override
	public Rectangle getRetryButton() {
		return retryButton;
	}

	@Override
	protected void setRetryButton(Rectangle rect) {
		retryButton = rect;
	}

	@Override
	public Rectangle getCancelButton() {
		return cancelButton;
	}
	
	
	
	
	
@Override
public int getSigThickness() {
	// TODO Auto-generated method stub
	return 3;
}
	

	@Override
	public Rectangle getActiveAreaForBackground() {
		return activeAreaForBackground;
	}

	Rectangle activeAreaForBackground = new Rectangle(0, 0, 800, 399);
@Override
protected void setActiveAreaForBackground(Rectangle rect) {
	activeAreaForBackground=rect;
	
}

	@Override
	public double getDpiWidth() {
		// TODO Auto-generated method stub
		return 800 / 4.09;
	}

	
	@Override
	public Image getButtonsImage() throws IOException {
		if (buttonsImage==null)
			buttonsImage= ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("images/STU-520/signWithButtons.png")		);
		return buttonsImage;
	}

	Image buttonsImage = null;
	@Override
	protected void setButtonsImage(Image img) {
		buttonsImage=img;
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isButtonImageFixed() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected String getTemplatePath() {
		// TODO Auto-generated method stub
		return "templates/STU520/";
	}
	
	@Override
	public boolean isAirModeSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Rectangle getAirModeButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAirModeButton(Rectangle area) {
		// TODO Auto-generated method stub
		
	}
		

}
