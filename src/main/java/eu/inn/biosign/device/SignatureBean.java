package eu.inn.biosign.device;

/*
 * #%L
 * Java Applet for biometric trait acquisition [http://www.biosignin.org]
 * SignatureBean.java is part of BioSignIn project
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
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class SignatureBean {
	private BufferedImage image;
	private Point offset;
	private int page;

	public BufferedImage getImage() {
		return image;
	}

	private Point originalOffset;

	public void resetOffset() {
		offset = (Point) originalOffset.clone();
	}

	public Point getOffset() {
		return offset;
	}

	public SignatureBean(BufferedImage image, Point offset, int page) {
		this.image = image;
		this.originalOffset = (Point) offset.clone();
		this.offset = offset;
		this.page = page;
	}

	public void setOffset(Point offset) {
		this.offset = offset;
	}

	Map<Float, Image> scaledImage = new HashMap<Float, Image>();

	public Image getScaledImage(float scale) {
		// TODO Auto-generated method stub
		if (!scaledImage.containsKey(scale)) {
			scaledImage.put(scale, image.getScaledInstance(Math.round(image.getWidth() * scale),
					Math.round(image.getHeight() * scale), BufferedImage.SCALE_SMOOTH));
		}
		return scaledImage.get(scale);
		// return null;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

}
