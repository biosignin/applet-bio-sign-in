package eu.inn.biosign.device;

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