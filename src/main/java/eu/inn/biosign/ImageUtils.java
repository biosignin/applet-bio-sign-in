package eu.inn.biosign;

/*
 * #%L
 * Java Applet for biometric trait acquisition [http://www.biosignin.org]
 * ImageUtils.java is part of BioSignIn project
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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

public class ImageUtils {

	
	public static BufferedImage toBufferedImage(Image img){
		long start = System.nanoTime();
		
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        long last = System.nanoTime();
		System.out.println("tablet toBufferedImage in " + ((last - start)/1000000) + "ms");
        // Return the buffered image
        return bimage;
    }
	
	public static BufferedImage scaleImage (BufferedImage before, double scale) {
		int w = before.getWidth();
		int h = before.getHeight();
		BufferedImage after = new BufferedImage((int)Math.round(w*scale), (int)Math.round(h*scale), BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		AffineTransformOp scaleOp = 
		   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		return scaleOp.filter(before, after);
	}
	
	public static Image getImageFromText(String text, Dimension rect, Color backgroundColor) {
		Font font = new Font(Font.SERIF, Font.PLAIN, 256);
        FontRenderContext frc = new FontRenderContext(null, true, true);
        TextLayout layout = new TextLayout(text, font, frc);
        Rectangle r = layout.getPixelBounds(null, 0, 0);
//        System.out.println(r);
        BufferedImage bi = new BufferedImage(
            r.width + 1, r.height + 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d2 = (Graphics2D) bi.getGraphics();
        g2d2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        if (backgroundColor!=null) {
		        g2d2.setColor(backgroundColor);
		        g2d2.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        }
        g2d2.setColor(Color.black);
        layout.draw(g2d2, 0, -r.y);
        g2d2.dispose();
       return bi.getScaledInstance(rect.width ,rect.height, BufferedImage.SCALE_SMOOTH);		        
//			        return bi;
	}

    /**
     * Splits an image into a number of rows and columns
     * 
     * @param img The image to be split
     * @param rows The number of rows
     * @param cols The number of columns
     * @return The array of split images in the vertical order
     */
    public static BufferedImage[] splitImage(Image img, int rows, int cols){
        // Determine the width of each part
        int w = img.getWidth(null) / cols;
        // Determine the height of each part
        int h = img.getHeight(null) / rows;
        // Determine the number of BufferedImages to be created
        int num = rows * cols;
        // The count of images we'll use in looping
        int count = 0;
        // Create the BufferedImage array
        BufferedImage[] imgs = new BufferedImage[num];
        // Start looping and creating images [splitting]
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                // The BITMASK type allows us to use bmp images with coloured
                // text and any background
                imgs[count] = new BufferedImage(w, h, BufferedImage.BITMASK);
                // Get the Graphics2D object of the split part of the image
                Graphics2D g = imgs[count++].createGraphics();
                // Draw only the required portion of the main image on to the
                // split image
                g.drawImage(img, 0, 0, w, h, w * y, h * x, w * y + w, h * x + h, null);
                // Now Dispose the Graphics2D class
                g.dispose();
            }
        }
        return imgs;
    }

    /**
     * Converts a given BufferedImage into an Image
     * 
     * @param bimage The BufferedImage to be converted
     * @return The converted Image
     */
    public static Image toImage(BufferedImage bimage){
        // Casting is enough to convert from BufferedImage to Image
        Image img = (Image) bimage;
        return img;
    }

    /**
     * Resizes a given image to given width and height
     * 
     * @param img The image to be resized
     * @param width The new width
     * @param height The new height
     * @return The resized image
     */
    public static Image resize(Image img, int width, int height){
        // Create a null image
        Image image = null;
        // Resize into a BufferedImage
        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimg.createGraphics();
        bGr.drawImage(img, 0, 0, width, height, null);
        bGr.dispose();
        // Convert to Image and return it
        image = toImage(bimg);
        return image;
    }

    /**
     * Creates a tiled image with an image upto given width and height
     * 
     * @param img The source image
     * @param width The width of image to be created
     * @param height The height of the image to be created
     * @return The created image
     */
    public static Image createTiledImage(Image img, int width, int height){
        // Create a null image
        Image image = null;
        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // The width and height of the given image
        int imageWidth = img.getWidth(null);
        int imageHeight = img.getHeight(null);
        // Start the counting
        int numX = (width / imageWidth) + 2;
        int numY = (height / imageHeight) + 2;
        // Create the graphics context
        Graphics2D bGr = bimg.createGraphics();
        for (int y = 0; y < numY; y++) {
            for (int x = 0; x < numX; x++) {
                bGr.drawImage(img, x * imageWidth, y * imageHeight, null);
            }
        }
        // Convert and return the image
        image = toImage(bimg);
        return image;
    }

    /**
     * Creates an empty image with transparency
     * 
     * @param width The width of required image
     * @param height The height of required image
     * @return The created image
     */
    public static BufferedImage getEmptyImage(int width, int height){
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        return (img);
    }

    /**
     * Creates a colored image with a specified color
     * 
     * @param color The color to be filled with
     * @param width The width of the required image
     * @param height The height of the required image
     * @return The created image
     */
    public static Image getColoredImage(Color color, int width, int height){
        BufferedImage img = toBufferedImage(getEmptyImage(width, height));
        Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.dispose();
        return img;
    }

    /**
     * Flips an image horizontally. (Mirrors it)
     * 
     * @param img The source image
     * @return The image after flip
     */
    public static Image flipImageHorizontally(Image img){
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        BufferedImage bimg = toBufferedImage(getEmptyImage(w, h));
        Graphics2D g = bimg.createGraphics();
        g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
        g.dispose();
        return toImage(bimg);
    }

    /**
     * Flips an image vertically. (Mirrors it)
     * 
     * @param img The source image
     * @return The image after flip
     */
    public static Image flipImageVertically(Image img){
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        BufferedImage bimg = toBufferedImage(getEmptyImage(w, h));
        Graphics2D g = bimg.createGraphics();
        g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
        g.dispose();
        return toImage(bimg);
    }

    /**
     * Clones an image. After cloning, a copy of the image is returned.
     * 
     * @param img The image to be cloned
     * @return The clone of the given image
     */
    public static BufferedImage clone(Image img){
        BufferedImage bimg = (getEmptyImage(img.getWidth(null), img.getHeight(null)));
        Graphics2D g = bimg.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return (bimg);
    }

    /**
     * Rotates an image. Actually rotates a new copy of the image.
     * 
     * @param img The image to be rotated
     * @param angle The angle in degrees
     * @return The rotated image
     */
    public static Image rotate(Image img, double angle){
        double sin = Math.abs(Math.sin(Math.toRadians(angle))), cos = Math.abs(Math.cos(Math.toRadians(angle)));
        int w = img.getWidth(null), h = img.getHeight(null);
        int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math.floor(h
                * cos + w * sin);
        BufferedImage bimg = toBufferedImage(getEmptyImage(neww, newh));
        Graphics2D g = bimg.createGraphics();
        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(Math.toRadians(angle), w / 2, h / 2);
        g.drawRenderedImage(toBufferedImage(img), null);
        g.dispose();
        return toImage(bimg);
    }
    
    /**
     * Makes a color in an Image transparent.
     */
    public static Image mask(Image img, Color color){
        BufferedImage bimg = toBufferedImage(getEmptyImage(img.getWidth(null), img.getHeight(null)));
        Graphics2D g = bimg.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        for (int y=0; y<bimg.getHeight(); y++){
            for (int x=0; x<bimg.getWidth(); x++){
                int col = bimg.getRGB(x, y);
                if (col==color.getRGB()){
                    bimg.setRGB(x, y, col & 0x00ffffff);
                }
            }
        }
        return toImage(bimg);
    }


	public static BufferedImage convertRenderedImage(RenderedImage img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		ColorModel cm = img.getColorModel();
		int width = img.getWidth();
		int height = img.getHeight();
		WritableRaster raster = cm.createCompatibleWritableRaster(width,
				height);
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		Hashtable<String, Object> properties = new Hashtable<String, Object>();
		String[] keys = img.getPropertyNames();
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				properties.put(keys[i], img.getProperty(keys[i]));
			}
		}
		BufferedImage result = new BufferedImage(cm, raster,
				isAlphaPremultiplied, properties);
		img.copyData(raster);
		return result;
	}
	
	
	 public static Image makeColorTransparent(final BufferedImage im, final Color color)
	   {
	      final ImageFilter filter = new RGBImageFilter()
	      {
	         // the color we are looking for (white)... Alpha bits are set to opaque
	         public int markerRGB = color.getRGB() | 0xFFFFFFFF;

	         public final int filterRGB(final int x, final int y, final int rgb)
	         {
	            if ((rgb | 0xFF000000) == markerRGB)
	            {
	               // Mark the alpha bits as zero - transparent
	               return 0x00FFFFFF & rgb;
	            }
	            else
	            {
	               // nothing to do
	               return rgb;
	            }
	         }
	      };

	      final ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
	      return Toolkit.getDefaultToolkit().createImage(ip);
	   }
	
}
