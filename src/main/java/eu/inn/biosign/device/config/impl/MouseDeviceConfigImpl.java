package eu.inn.biosign.device.config.impl;

/*
 * #%L
 * Java Applet for biometric trait acquisition [http://www.biosignin.org]
 * MouseDeviceConfigImpl.java is part of BioSignIn project
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

public class MouseDeviceConfigImpl extends DeviceConfig {
	Rectangle nextButton;
	Rectangle prevButton;
@Override
public Rectangle getNextButton() {
	// TODO Auto-generated method stub
	return nextButton;
}
@Override
public Rectangle getPrevButton() {
	// TODO Auto-generated method stub
	return prevButton;
}
@Override
public void setNextButton(Rectangle rect) {
	nextButton=rect;
	// TODO Auto-generated method stub
	
}
@Override
public void setPrevButton(Rectangle rect) {
	prevButton=rect;
	// TODO Auto-generated method stub
	
}
	@Override
	public int getSigThickness() {
		// TODO Auto-generated method stub
		return 3;
	}
@Override
public boolean isAirModeSupported() {
	// TODO Auto-generated method stub
	return false;
}

@Override
public Rectangle getAirModeButton() {
	// TODO Auto-generated method stub
	return airModeButton;
}

@Override
public void setAirModeButton(Rectangle area) {
	// TODO Auto-generated method stub
	airModeButton=area;
}



	Rectangle airModeButton = null;

	Rectangle retryButton = new Rectangle(725, 700, 220, 75);
	Rectangle okButton = new Rectangle(446, 420, 140, 40);
	Rectangle cancelButton = new Rectangle(625, 420, 140, 40);

	@Override
	protected void setCancelButton(Rectangle rect) {
		cancelButton = rect;
	}

	@Override
	protected void setOkButton(Rectangle rect) {
		okButton = rect;
	}

	@Override
	protected void setRetryButton(Rectangle rect) {
		retryButton = rect;
	}

	@Override
	public Rectangle getOkButton() {
		return okButton;
//		return new Rectangle(280 * getDimension().width / 800,
//				420 * getDimension().height / 480,
//				140 * getDimension().width / 800,
//				40 * getDimension().height / 480);
	}

	@Override
	public Rectangle getCancelButton() {
		return cancelButton;
//		return new Rectangle(625 * getDimension().width / 800,
//				420 * getDimension().height / 480,
//				140 * getDimension().width / 800,
//				40 * getDimension().height / 480);
	}

	@Override
	public Rectangle getRetryButton() {
		return retryButton;
//		
//		return new Rectangle(446 * getDimension().width / 800,
//				420 * getDimension().height / 480,
//				140 * getDimension().width / 800,
//				40 * getDimension().height / 480);
	}



	
	@Override
	public Rectangle getActiveAreaForBackground() {
		if (activeAreaForBackground==null)
			activeAreaForBackground =  new Rectangle(0, 0, 800, 399);
		return activeAreaForBackground;
	}

	Rectangle activeAreaForBackground = null;
@Override
protected void setActiveAreaForBackground(Rectangle rect) {
	activeAreaForBackground=rect;
	
}
	@Override
	public double getDpiWidth() {
		// TODO Auto-generated method stub
		return 1280 / 8.54;
	}

	@Override
	public Image getButtonsImage() throws IOException {
		if (buttonsImage==null)
			buttonsImage= ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("images/BASE/signWithButtons.png")		);
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
		return false;
	}

	@Override
	protected String getTemplatePath() {
		
		return "templates/BASE/";
		// TODO Auto-generated method stub
	}
	
	
	
}
