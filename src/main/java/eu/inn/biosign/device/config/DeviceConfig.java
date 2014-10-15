package eu.inn.biosign.device.config;

/*
 * #%L
 * Java Applet for biometric trait acquisition [http://www.biosignin.org]
 * DeviceConfig.java is part of BioSignIn project
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


import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.bouncycastle.util.encoders.Base64;
import org.w3c.dom.Element;

import eu.inn.biometric.signature.managed.ManagedIsoPoint;
import eu.inn.biosign.BioSign;
import eu.inn.biosign.BioSign.Button;
import eu.inn.biosign.DeviceManager;
import eu.inn.biosign.ImageUtils;
import eu.inn.biosign.PdfPageInfo;
import eu.inn.biosign.device.SignatureBean;
import eu.inn.biosign.util.FileUtils;
import eu.inn.biosign.util.StaticUtils;
import eu.inn.biosign.util.StringUtils;
import eu.inn.configuration.Device;
import eu.inn.configuration.ManagedIsoPointSimulated;
import eu.inn.signature.Signature;
import gui.ava.html.Html2Image;
import gui.ava.html.imagemap.ElementBox;
//import org.apache.pdfbox.pdmodel.PDPage;

public abstract class DeviceConfig {

	private ImageBean bean = null;

	public final ImageBean getImageBean() {
		return bean;
	}

	public final void setImageBean(ImageBean bean) {
		this.bean = bean;
	}

	public Rectangle pagesRectangle = null;

	public abstract void setNextButton(Rectangle rect);

	public abstract Rectangle getNextButton();

	public abstract void setPrevButton(Rectangle rect);

	public abstract Rectangle getPrevButton();

	public abstract Rectangle getOkButton();

	public abstract int getSigThickness();

	public abstract Rectangle getCancelButton();

	public abstract boolean isAirModeSupported();

	public abstract Rectangle getAirModeButton();

	public abstract void setAirModeButton(Rectangle area);

	public abstract Rectangle getRetryButton();

	public abstract Rectangle getActiveAreaForBackground();

	public abstract Image getButtonsImage() throws IOException;

	protected abstract void setButtonsImage(Image img);

	public abstract boolean isButtonImageFixed();

	// abstract void setImageOnDevice(BufferedImage image);

	public abstract double getDpiWidth();

	protected abstract void setCancelButton(Rectangle rect);

	protected abstract void setOkButton(Rectangle rect);

	protected abstract void setRetryButton(Rectangle rect);

	protected abstract void setActiveAreaForBackground(Rectangle rect);

	protected abstract String getTemplatePath();

	protected String getDefaultTemplate() {
		return "default";
	}

	// public BufferedImage image;

	private static final String NO_IMAGE_ERROR = "No pdf image. Usign blank background";
	private BufferedImage pureImageFromPDF;

	private double getScaledRatio(BufferedImage image, double dpiWidth) {
		double pdfInchWidth = pdfPointWidth / 72d;

		double pngInchWidth = image.getWidth() / dpiWidth;

		return pdfInchWidth / pngInchWidth;
	}

	private double pdfPointWidth;

	public double getPdfPointWidth() {
		return pdfPointWidth;
	}

	private Rectangle pngRectangle;
	public Rectangle scaledSignRectangle;

	// private static double dpiWidth = 800 / 4.09;

	public void setSignRectangle(float x, float y, float width, float height) {
		// TODO Auto-generated method stub
		pngRectangle = new Rectangle2D.Float(x, y, width, height).getBounds();
	}

	public static Rectangle signatureArea;

	public static Rectangle getSignatureArea() {
		return signatureArea;
	}

	BufferedImage signImage = null;
	public int signPointWidth = -1;
	public int signPage = -1;
	public int actualPage = -1;
	public String baseImageUrl = null;

	public void clearCache(int page) {
		System.err.println("claering cache pag " + page);
		try {
			cache.remove(new Integer(page));
		} catch (Exception ex) {

		}
		try {
			if (!Device.useAppletRendering) {
				cachePureImageFromPDF.remove(new Integer(page));
			}
		} catch (Exception ex) {

		}
		signImage = null;
		signatureArea = null;
	}

	HashMap<Integer, BufferedImage> cachePureImageFromPDF = new HashMap<Integer, BufferedImage>();

	public void setBase64PdfImage(String b64, int pointWidth, int page, int totalPage) {

		try {
			System.out.println("setBase64PdfImage");
			long start = System.nanoTime();
			if (!cachePureImageFromPDF.containsKey(page)) {
//				if (Device.PDFDOCUMENT != null) {
//					
//					System.out.println("Read image of page " + page + "from file");
//					
//					pureImageFromPDF = ((PDPage) Device.PDFDOCUMENT.getDocumentCatalog().getAllPages().get(page - 1))
//							.convertToImage();
//					long last = System.nanoTime();
//					System.out.println("tablet convertToImage in " + ((last - start)/1000000) + "ms");
//					// System.out.println(pageImage.getWidth() +
//					// " - "+pageImage.getHeight());
//					pointWidth = Math.round(((PDPage) Device.PDFDOCUMENT.getDocumentCatalog().getAllPages()
//							.get(page - 1)).getMediaBox().getWidth());
//					totalPage = Device.PDFDOCUMENT.getDocumentCatalog().getAllPages().size();
//				} else 
					if(DeviceManager._instance.externalImageRenderer!=null) {
					System.out.println("Read image of page " + page + "from external renderer");
					PdfPageInfo pdfInfo = DeviceManager._instance.externalImageRenderer.getPageInfo(page -1);
					System.out.println("pdfInfo : " + pdfInfo.toString());
					totalPage = pdfInfo.getTotalPage();
					pointWidth = pdfInfo.getPointWidth();
					System.out.println("totalPage = " + totalPage + " , pointWidth = " + pointWidth);
					byte[] imageByte = Base64.decode(pdfInfo.getImgB64());
					System.out.println("imageByte.length = "+ imageByte.length);
					ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
					pureImageFromPDF = ImageIO.read(bis);
					bis.close();
					long last = System.nanoTime();
					System.out.println("tablet use external renderer in " + ((last - start)/1000000) + "ms");
					
				}else if (b64.startsWith("http")) {
					System.out.println("read from " + b64);
					pureImageFromPDF = ImageIO.read(new URL(b64));
					// System.out.println(pageImage.getWidth() +
					// " - "+pageImage.getHeight());
				} else {
					System.out.println("read from b64");
					byte[] imageByte = Base64.decode(b64);
					ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
					pureImageFromPDF = ImageIO.read(bis);

					bis.close();
				}
				BioSign._instance.totalPage = totalPage;
				pdfPointWidth = pointWidth;
				if (signImage == null) {
					signImage = pureImageFromPDF;
					signPointWidth = pointWidth;
					try {
						baseImageUrl = b64.substring(0, b64.lastIndexOf("&page=") + 6);
					} catch (Exception ex) {
					}
				}
				cachePureImageFromPDF.put(page, pureImageFromPDF);

			}

			actualPage = page;
			pureImageFromPDF = cachePureImageFromPDF.get(page);
			System.out.println("setting pointWidth " + pointWidth);
			long last = System.nanoTime();
			System.out.println("tablet setBase64Img in " + ((last - start)/1000000) + "ms");

		} catch (Throwable e) {
			e.printStackTrace();
		}
		// cache
	}

	public class ImageBean {
		private BufferedImage image;
		private BufferedImage originalDimensionImage;
		private Point offset;

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

		public ImageBean(BufferedImage image, Point offset, Image originalDimensionImage) {
			this.image = image;
			this.originalOffset = offset;
			this.offset = offset;

			this.originalDimensionImage = (originalDimensionImage instanceof BufferedImage) ? (BufferedImage) originalDimensionImage
					: ImageUtils.toBufferedImage(originalDimensionImage);
			this.originalScaledImages.put(1f, this.originalDimensionImage);
			this.scaledImages.put(1f, this.image);
		}

		public void setOffset(Point offset) {
			this.offset = offset;
		}

		// Image scaledImage=null;
		Map<Float, Image> scaledImages = new HashMap<Float, Image>();
		Map<Float, Image> originalScaledImages = new HashMap<Float, Image>();

		protected Image getScaledImage(float scale) {
			// TODO Auto-generated method stub
			if (!scaledImages.containsKey(scale)) {
				scaledImages.put(
						scale,ImageUtils.scaleImage(image, scale));
			}

			return scaledImages.get(scale);
			// return null;
		}

		public Map<Float, String> b64 = new HashMap<Float, String>();

		public String getOriginalImageInBase64Cached(float scale) {
			long start = System.nanoTime();
			if (!b64.containsKey(scale)) {
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(ImageUtils.toBufferedImage(getOriginalImageCached(scale)), "png", baos);
					long last = System.nanoTime();
					System.out.println("-- tablet ImageIO.write in " + ((last - start)/1000000) + "ms");
					baos.flush();
					byte[] imageInByte = baos.toByteArray();
					b64.put(scale, new String(Base64.encode(imageInByte)));
					last = System.nanoTime();
					System.out.println("-- tablet Base64.encode in " + ((last - start)/1000000) + "ms");
					baos.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			long last = System.nanoTime();
			System.out.println("tablet getOriginalImageInBase64Cached in " + ((last - start)/1000000) + "ms");
			return b64.get(scale);
		}

		public Image getOriginalImageCached(float scale) {
			if (!originalScaledImages.containsKey(scale)) {
				originalScaledImages.put(scale, ImageUtils.scaleImage(originalDimensionImage,scale));
			}

			return originalScaledImages.get(scale);
		}

	}

	Map<Integer, ImageBean> cache = new HashMap<Integer, DeviceConfig.ImageBean>();

	public ImageBean getPdfCroppedImage(int page) {
		System.out.println("loading page " + page);
		long start = System.nanoTime();
		if (!cache.containsKey(page)) {
			Rectangle rect = getActiveAreaForBackground();
			try {
				if (pureImageFromPDF == null)
					throw new Exception(NO_IMAGE_ERROR);
				// System.out.println("getPdfCroppedImage");
				double ratio = getScaledRatio(pureImageFromPDF, getDpiWidth());

				Image originalDimensionImage = ImageUtils.clone(pureImageFromPDF);
				long last = System.nanoTime();
				System.out.println("tablet ImageUtils.clone in " + ((last - start)/1000000) + "ms");

				Graphics gOriginal = originalDimensionImage.getGraphics();
				BufferedImage scaledImage = ImageUtils.scaleImage(pureImageFromPDF, ratio);
			
				last = System.nanoTime();
				System.out.println("tablet ImageUtils.clone + scale in " + ((last - start)/1000000) + "ms");
				if (pngRectangle == null) {
					cache.put(
							page, //TODO: remove the getSubImage
							new ImageBean(scaledImage.getSubimage(rect.x, rect.y,
									Math.min(rect.width, scaledImage.getWidth(null)),
									Math.min(rect.height, scaledImage.getHeight(null))), rect.getLocation(),
									originalDimensionImage));
					
					last = System.nanoTime();
					System.out.println("tablet scaledImage.getSubimage in " + ((last - start)/1000000) + "ms");
					
					return cache.get(page);

				}
				scaledSignRectangle = new Rectangle((int) Math.round(pngRectangle.getX() * ratio),
						(int) Math.round(pngRectangle.getY() * ratio),
						(int) Math.round(pngRectangle.getWidth() * ratio), (int) Math.round(pngRectangle.getHeight()
								* ratio));
				Point scaledCropStartPoint = new Point();
				scaledCropStartPoint.x = scaledSignRectangle.x
						- ((rect.width - scaledSignRectangle.width) / 2);
				if ((scaledCropStartPoint.x + rect.width) > scaledImage.getWidth())
					scaledCropStartPoint.x = scaledImage.getWidth() - rect.width;
				if (scaledCropStartPoint.x < 0)
					scaledCropStartPoint.x = 0;
				scaledCropStartPoint.y = scaledSignRectangle.y
						- Math.round((rect.height - scaledSignRectangle.height) / 2);
				if (scaledCropStartPoint.y < 0)
					scaledCropStartPoint.y = 0;

				if ((scaledCropStartPoint.y + rect.height) > scaledImage.getHeight())
					scaledCropStartPoint.y = scaledImage.getHeight() - rect.height;
				Graphics2D g = (Graphics2D) scaledImage.getGraphics();
				g.setColor(Color.BLACK);
				g.setStroke(new BasicStroke(2));
				if (Device.useAppletRendering) {
					for (SignatureBean sb : Device.signatureBeans) {
						if (sb.getPage() != page)
							continue;
						g.drawImage(sb.getImage(), sb.getOffset().x, sb.getOffset().y, null);
						gOriginal.drawImage(sb.getScaledImage((float) (1f / ratio)),
								((int) Math.round(sb.getOffset().x / ratio)),
								((int) Math.round(sb.getOffset().y / ratio)), null);
					}
				}
				g.dispose();
				gOriginal.dispose();
				if (scaledSignRectangle.x < 0)
					scaledSignRectangle.x = 0;
				if (signatureArea == null) {
					signatureArea = new Rectangle(scaledSignRectangle.x - scaledCropStartPoint.x, scaledSignRectangle.y
							- scaledCropStartPoint.y, scaledSignRectangle.width, scaledSignRectangle.height);
					System.err.println("translating");
					signatureArea.translate(getActiveAreaForBackground().x, getActiveAreaForBackground().y);
				}
				ImageBean b = null;
				b = new ImageBean(scaledImage, signPage == page ? scaledCropStartPoint : new Point(),
						originalDimensionImage);
				cache.put(page, b);
			} catch (Exception ex) {
				if (ex.getMessage() != null && ex.getMessage().equals(NO_IMAGE_ERROR)) {
					System.err.println(NO_IMAGE_ERROR);
				} else {
					System.out.println("Error getPdfCroppedImage");
					ex.printStackTrace();
				}
				BufferedImage b = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_RGB);

				Graphics g = b.getGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, rect.width, rect.height);
				g.dispose();
				cache.put(page, new ImageBean(b, new Point(), b));
			}
		}
		ImageBean bean = cache.get(page);
		bean.resetOffset();
		long last = System.nanoTime();
		System.out.println("tablet getPdfCroppedImage in " + ((last - start)/1000000) + "ms");
		return bean;
	}

	public final void loadTemplate(String TAG) {

		Map<String, String> values = new HashMap<String, String>();
		if (StringUtils.isNotBlank(TAG)) {
			TAG = TAG.trim();
			String[] entries = StringUtils.split(TAG, ",");
			for (String entry : entries) {
				try {
					String[] kv = StringUtils.split(entry, "=");
					values.put(kv[0].trim(), kv[1].trim());
				} catch (Exception ex) {
				}
			}
		}

		String templateName = null;
		if (values.containsKey("template"))
			templateName = values.get("template");
		Signature.isBio = true;
		try {
			if (values.containsKey("bio"))
				Signature.isBio = !values.get("bio").equals("0");
		} catch (Exception ex) {

		}
		String html = null;
		try {
			html = new String(StaticUtils.getResourceUsingFileStreams(this.getClass().getClassLoader()
					.getResourceAsStream(getTemplatePath() + templateName + ".html")));
		} catch (Exception ex) {
			System.out.println("cannot find template \"" + templateName + "\". Trying default");

		}
		if (StringUtils.isBlank(html))
			try {
				templateName = getDefaultTemplate();
				html = new String(StaticUtils.getResourceUsingFileStreams(this.getClass().getClassLoader()
						.getResourceAsStream(getTemplatePath() + templateName + ".html")));

			} catch (Exception ex) {
				System.out.println("cannot find default template \"" + templateName + "\".");
			}
		if (StringUtils.isBlank(html)) {
			System.out.println("template \"" + templateName + "\" is blank");
			return;
		}

		for (String key : values.keySet()) {
			try {
				html = html.replace("$" + key + "$", values.get(key));
			} catch (Exception ex) {
			}
		}

		File fin = null;
		try {

			for (String s : StaticUtils.getResourceListing(this.getClass(), getTemplatePath())) {
				if (s.equals(templateName + ".html"))
					continue;
				StaticUtils.copy("Inno Templates", s, getTemplatePath() + s, true);
			}
			fin = new File(System.getProperty("user.home") + "\\Inno Templates\\template.html");
			if (fin.exists()) {
				fin.delete();
				Thread.sleep(100);
			}
			try {
				FileUtils.writeStringToFile(fin, html);
			}catch(Exception e) {
				fin = new File("/tmp/Inno Templates/template.html");
				if (fin.exists()) {
					fin.delete();
					Thread.sleep(100);
				}
				FileUtils.writeStringToFile(fin, html);
			}
			Thread.sleep(100);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (fin == null) {
			System.out.println("fin is null");
			return;
		}

		if (!fin.exists()) {
			System.out.println("fin does not exist " + fin.getAbsolutePath());
			return;
		}

		final Html2Image html2Image = Html2Image.fromFile(fin, values);
		html2Image.getImageRenderer().setWidth(100);
		// final Html2Image html2Image =
		// Html2Image.fromURL(ResourceUtils.getURL("classpath:emanuele.html"));
		// html2Image.getImageRenderer().saveImage("c:\\nuovotest1.png");
		// html2Image.getHtmlImageMap().getClickableBoxes().entrySet();

		BufferedImage img = html2Image.getImageRenderer().getBufferedImage();
		for (Entry<Element, Collection<ElementBox>> e : html2Image.getHtmlImageMap().getClickableBoxes().entrySet()) {
			// System.out.println("------------");
			System.out.println(e.getKey());
			for (ElementBox box : e.getValue()) {
				String href = box.getElement().getAttribute("href");
				System.out.println(href);
				;
				Rectangle rect = new Rectangle(box.getLeft(), box.getTop(), box.getWidth(), box.getHeight());
				if (href.equalsIgnoreCase("#PdfArea")) {
					setActiveAreaForBackground(rect);
					BufferedImage img2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics2D g2 = img2.createGraphics();
					g2.drawImage(img, 0, 0, null);

					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
					g2.fillRect(rect.x, rect.y, rect.width, rect.height);

					// g2.clearRect(rect.x, rect.y, rect.width, rect.height);
					g2.dispose();
					img = img2;
				}

				if (href.equalsIgnoreCase("#ButtonRetry")) {
					setRetryButton(rect);
				}

				if (href.equalsIgnoreCase("#ButtonOK")) {
					setOkButton(rect);
				}

				if (href.equalsIgnoreCase("#ButtonCancel")) {
					setCancelButton(rect);
				}

				if (href.equalsIgnoreCase("#ButtonAirMode")) {
					setAirModeButton(rect);
				}
				if (href.equalsIgnoreCase("#ButtonNext")) {
					setNextButton(rect);
				}
				if (href.equalsIgnoreCase("#ButtonPrev")) {
					setPrevButton(rect);
				}
				if (href.equalsIgnoreCase("#Pages")) {
					pagesRectangle = rect;
					// setPrevButton(rect);
				}

				System.out.println(rect);
			}
		}
		setButtonsImage(img);
		// setCancelButton(null);
		// html2Image.getImageRenderer().setImageType(imageType)
		// html2Image.getHtmlImageMap().saveImageMapDocument("test1.html",
		// "test1.png");
	}

	public boolean isAlphaSupported() {
		// TODO Auto-generated method stub
		return true;
	}

	public void setSignPage(int parseInt) {
		signPage = parseInt;
		actualPage = parseInt;
		// TODO Auto-generated method stub

	}

	public void clearAllCache() {
		cache.clear();
		cachePureImageFromPDF.clear();
		Device.signatureBeans.clear();
		// TODO Auto-generated method stub

	}
	
	public void didReceiveMemoryWarning() {
		cache.clear();
		cachePureImageFromPDF.clear();
	}

	public String getPageImageB64Cached(int page, float scale) {
		getPdfCroppedImage(page);
		if (!cache.containsKey(page))
			throw new IllegalStateException("Page #" + page + " is not loaded yet");

		return cache.get(page).getOriginalImageInBase64Cached(scale);
		// TODO Auto-generated method stub

	}

	public Image getPageImageCached(int page, float scale) {
		// int origActualPage=actualPage;

		// actualPage=page;
		getPdfCroppedImage(page);
		// actualPage=origActualPage;
		if (!cache.containsKey(page))
			throw new IllegalStateException("Page #" + page + " is not loaded yet");

		return cache.get(page).getOriginalImageCached(scale);

		// TODO Auto-generated method stub

	}

	public Image getScaledImage(float scale) {
		// TODO Auto-generated method stub
		if (getImageBean() != null)
			return getImageBean().getScaledImage(scale);

		return null;
	}

	public Button getButton(ManagedIsoPoint point) {
		boolean simulated = false;
		if (point instanceof ManagedIsoPointSimulated)
			simulated = ((ManagedIsoPointSimulated) point).simulated;
		Point standardPoint = new Point(point.getX(), point.getY());
		if (!simulated && getOkButton() != null && getOkButton().contains(standardPoint)) {
			return Button.OK;
		}
		if (getRetryButton() != null && getRetryButton().contains(standardPoint)) {
			return Button.CLEAR;
		}
		if (getCancelButton() != null && getCancelButton().contains(standardPoint)) {
			return Button.CANCEL;
		}
		if (isAirModeSupported()) {
			if (getAirModeButton() != null && getAirModeButton().contains(standardPoint)) {
				return Button.AIRMODE;
			}
			if (getNextButton() != null && getNextButton().contains(standardPoint)) {
				return Button.NEXT;
			}
			if (getPrevButton() != null && getPrevButton().contains(standardPoint)) {
				return Button.PREV;
			}

		}
		return null;
	}

}
