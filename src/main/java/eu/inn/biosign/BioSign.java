package eu.inn.biosign;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.bouncycastle.util.encoders.Base64;

import eu.inn.biometric.signature.managed.ManagedIsoPoint;
import eu.inn.biometric.signature.renderer.ImageRenderer;
import eu.inn.biosign.device.BaseDeviceHandler;
import eu.inn.biosign.device.SignatureBean;
import eu.inn.biosign.device.config.DeviceConfig;
import eu.inn.biosign.device.config.DeviceConfig.ImageBean;
import eu.inn.biosign.device.impl.DummyHandlerImpl;
import eu.inn.biosign.device.impl.MouseHandlerImpl;
import eu.inn.biosign.device.sp.DeviceHandlerServiceProvider;
import eu.inn.biosign.listener.IDeviceListener;
import eu.inn.configuration.Device;
import eu.inn.configuration.ManagedIsoPointSimulated;
import eu.inn.signature.Signature;


public class BioSign extends JPanel implements MouseListener, MouseMotionListener {


	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("clicked");
		genericMouseEvent(e);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("pressed");
	genericMouseEvent(e);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("released");
		genericMouseEvent(e);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("entered");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println("exited");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
//		System.out.println("dragged");
		genericMouseEvent(e);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
//		System.out.println("moved");
		genericMouseEvent(e);
	
		
	}
	
	int lastPressure=0;
	
	private void genericMouseEvent(MouseEvent e)
	{
		if (tablet.getDeviceConfig().isAirModeSupported()){
			int pressure=(e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) == 0?0:1;
//			System.out.println("Pressure: "+pressure);
			if (lastPressure==0 && pressure>0)
				tablet.resetLastPoint();
//				t.lastPoint=null;//new PointWithPressureAndTime(Math.round(e.getX()/scale), Math.round(e.getY()/scale),pressure );
//				t.PenPressed(new PointWithPressureAndTime(Math.round(e.getX()/scale), Math.round(e.getY()/scale),0 ), false);
			
			if (lastPressure>0 || pressure>0)
				tablet.penPressedSimulated(new ManagedIsoPointSimulated(Math.round(e.getX()/scale), Math.round(e.getY()/scale),pressure , true,1,1));
			lastPressure=pressure;
		}
	}
	
	public static BioSign _instance = null;

	private static final long serialVersionUID = 1L;

	private static float scale = 0.5f;

	// size of the tablets
	// private static final int STU520_WIDTH = 800;
	// private static final int STU520_HEIGHT = 480;

	public enum Button {
		OK, CLEAR, CANCEL, AIRMODE, NEXT, PREV
	}


	
	private ArrayList<ManagedIsoPoint> scalePoints = new ArrayList<ManagedIsoPoint>();

	public void clearScaledPoints() {
		scalePoints.clear();
	}

	public boolean addScaledPoint(ManagedIsoPoint p) {
		return addScaledPoint(p, false);
	}
	
	boolean lastInside=false;

	public boolean addScaledPoint(ManagedIsoPoint pOrig, boolean excludeScale) {
		ManagedIsoPoint p = (ManagedIsoPoint)pOrig.clone();		
		Point point =  new Point((int)Math.round(p.x), (int) Math.round(p.y));
		Rectangle rect = null;
		if (DeviceConfig.getSignatureArea()==null) {
			rect=tablet.getDeviceConfig().getActiveAreaForBackground();
		}
		else {
			
//			point.translate(-tablet.getDeviceConfig().getActiveAreaForBackground().x, -tablet.getDeviceConfig().getActiveAreaForBackground().y);
			rect=DeviceConfig.getSignatureArea();
		}
		if (rect==null  || rect.contains(point)) {
			lastInside=true;
		//		new Point(Math.round(p.x), (int) Math.round(p.y)))) {
			if (!excludeScale) {
				p.setLocation(p.getX()*scale, p.getY()*scale);
				
				
			}
			scalePoints.add(p);
			repaint();
			return true;
		}else {
			if (lastInside)
			{
				scalePoints.add(new ManagedIsoPoint(0, 0, 0));
				pOrig.pressure=0;
				lastInside=false;
				return true;
			}
			
			lastInside = false;}
		return false;
	}

	
	
	@Override
	public void paint(Graphics g) {
		System.out.println("BioSign Paint");
		super.paint(g);
//		bkImage = justButtons.getScaledInstance(
//				Math.round(allInOne.getWidth() * scale),
//				Math.round(allInOne.getHeight() * scale),
//				Image.SCALE_SMOOTH);
		
		if (bkImage != null)
			
		{
			if (tablet!=null && tablet.getDeviceConfig()!=null) {
				DeviceConfig config=tablet.getDeviceConfig();
				Image scaledImage = config.getScaledImage(scale);
				if (scaledImage!=null)
				{
//					BufferedImage image = config.getImageBean().getScaledImage(scale);
//					Image bi = image.getScaledInstance(Math.round(image.getWidth()*scale), Math.round(image.getHeight()*scale), BufferedImage.SCALE_DEFAULT);
					g.drawImage(scaledImage, Math.round((-config.getImageBean().getOffset().x+config.getActiveAreaForBackground().x)*scale), Math.round((-config.getImageBean().getOffset().y+config.getActiveAreaForBackground().y)*scale), null);
					
					
				}
				
				
			}
			g.drawImage(bkImage, 0, 0, null);
			if (tablet!=null && tablet.getDeviceConfig()!=null) {
				DeviceConfig config=tablet.getDeviceConfig();
				Graphics2D g2d=(Graphics2D)g;
				
				boolean isAirModeEnabled=true;
				boolean isAirModeActive=tablet.getDeviceConfig().isAirModeSupported() && tablet.isAirModeActive();
				g.setColor(new Color(88, 88, 88, 130));
				if (isAirModeEnabled&&isAirModeActive) {
					if (config.getOkButton()!=null) 							
						g2d.fill(new Rectangle2D.Float(config.getOkButton().x*scale, config.getOkButton().y*scale, config.getOkButton().width*scale, config.getOkButton().height*scale));
					
					if (config.getRetryButton()!=null)
						g2d.fill(new Rectangle2D.Float(config.getRetryButton().x*scale, config.getRetryButton().y*scale, config.getRetryButton().width*scale, config.getRetryButton().height*scale));
	
					
				}
				else {
					g.setColor(Color.black);
					Stroke orig= g2d.getStroke();
					g2d.setStroke(new BasicStroke(1.5f));//
					Rectangle sigArea = DeviceConfig.getSignatureArea();
					if (sigArea!=null)
						g2d.draw(new Rectangle2D.Float(sigArea.x*scale, sigArea.y*scale, sigArea.width*scale, sigArea.height*scale));
					g.setColor(new Color(88, 88, 88, 130));
					g2d.setStroke(orig);
				}
//				if (config.getCancelButton()!=null)
//					g2d.fill(new Rectangle2D.Float(config.getCancelButton().x*scale, config.getCancelButton().y*scale, config.getCancelButton().width*scale, config.getCancelButton().height*scale));

//				else {
					if (config.getNextButton()!=null && (!isAirModeActive || tablet.getDeviceConfig().actualPage==totalPage))
						g2d.fill(new Rectangle2D.Float(config.getNextButton().x*scale, config.getNextButton().y*scale, config.getNextButton().width*scale, config.getNextButton().height*scale));
					if (config.getPrevButton()!=null && (!isAirModeActive || tablet.getDeviceConfig().actualPage==1))
						g2d.fill(new Rectangle2D.Float(config.getPrevButton().x*scale, config.getPrevButton().y*scale, config.getPrevButton().width*scale, config.getPrevButton().height*scale));
//				}
				
				if (!isAirModeEnabled && config.getAirModeButton()!=null)
				{
					g2d.fill(new Rectangle2D.Float(config.getAirModeButton().x*scale, config.getAirModeButton().y*scale, config.getAirModeButton().width*scale, config.getAirModeButton().height*scale));
				}
				if (isAirModeActive)
				{
					g2d.setStroke(new BasicStroke(2));
					g.setColor(Color.GREEN);
					g2d.draw(new Rectangle2D.Float(config.getAirModeButton().x*scale, config.getAirModeButton().y*scale, config.getAirModeButton().width*scale, config.getAirModeButton().height*scale));
				}
				if (config.pagesRectangle!=null)
				{				
				    String testo="Pagina "+BioSign._instance.tablet.getDeviceConfig().actualPage+"/"+  BioSign._instance.totalPage;
				    g.drawImage(ImageUtils.getImageFromText(testo, new Dimension(Math.round(config.pagesRectangle.width*scale) ,Math.round(config.pagesRectangle.height*scale)),null), Math.round(config.pagesRectangle.x*scale) ,Math.round( config.pagesRectangle.y*scale) , null);
				}
				
			}
			
		
		}
		if (!scalePoints.isEmpty()) {
//		if (tablet.getDeviceConfig().getImageBean() != null)
//			g.drawImage(tablet.getDeviceConfig().getImageBean().getImage(), tablet.getDeviceConfig().getImageBean().getOffset().x, tablet.getDeviceConfig().getImageBean().getOffset().y, null);
		ImageRenderer.drawPoints(scalePoints, (Graphics2D) g, Color.BLACK, new Point(
				0, 0), Math.max(1, (3 * scale)), tablet.getMaxPressure()*scale);
		
		}
		
		
		
		
	}

	public static boolean IS_DUAL_SCREEN=false;
	
	public BioSign() throws IOException {

		JSObjectWrapper.call("log", new Object[] { "intiTablet" });

		_instance = this;
		
		if (!initTablet()) {
			if (Device.isDEBUG)
				showError(new Exception("Cannot connect to any device"));
			_instance = null;
//			return;
			throw new IOException("Cannot connect to any Device");
		}
		
		
		
		if (tablet.getDeviceConfig().isAirModeSupported()) {
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
		}
		this.setFocusable(true);
		this.setRequestFocusEnabled(true);		
		scale = DeviceManager._instance.getWidth()
				/ (float) tablet.getDimension().getWidth();

		this.setLayout(new BorderLayout());

	}

	private boolean initTablet() {

		tablet = checkTablet();
		if (tablet == null)
			return false;
		System.out.println("Using " + tablet.getTabletDescription());

		tablet.init();
		return true;

	}

	private BaseDeviceHandler checkTablet() {
		BaseDeviceHandler ret = DeviceHandlerServiceProvider.getInstance().getConnecteDeviceHandler();
		if (ret!=null)
			return ret;
		if (Device.IS_MOUSE_ALLOWED)
			return new MouseHandlerImpl();
		else {
			return new DummyHandlerImpl();
		}

	}

	double vp = 0;

	
	public void pressAirModeButton() {
		
		tablet.toggleAirMode();
	}
	
	public void pressOkButton() {
		if (!save()) return;
	
		
		tablet.getDeviceConfig().clearCache(tablet.getDeviceConfig().actualPage);
		tablet.clear();

		close();
		for (IDeviceListener list :DeviceManager._instance.listeners) {
			list.accept(Signature.outputSdi, Signature.image, DeviceManager._instance);
		}
		if (Device.isDEBUG) {
			DeviceManager._instance.startCapture(false);
		}
	}

	public void pressClearButton() {
		System.out.println("pressClearButton");
		long start = System.nanoTime();
		tablet.clear();
		long cleared = System.nanoTime();
		System.out.println("tablet clear in " + ((cleared - start)/1000000) + "ms");
//		loadImage();
		repaint();
		long loadedImage = System.nanoTime();
		System.out.println("tablet reloadedImage in " + ((loadedImage - cleared)/1000000) + "ms");
	}

	public void pressCancelButton() {
//		System.out.println("cancelSign");
		tablet.clear();
		close();

		for (IDeviceListener list :DeviceManager._instance.listeners) {
			list.cancel(DeviceManager._instance);
			}
		JSObjectWrapper.call("cancelSign", null);
		if (Device.isDEBUG) {
			
			DeviceManager._instance.startCapture(!tablet.getForceAirMode());
		}
	}
	
	

	// private static byte[] transformIntTo4Bytes(int int_0) {
	// return new byte[] { (byte) ((int_0 & -16777216) >> 24),
	// (byte) ((int_0 & 16711680) >> 16),
	// (byte) ((int_0 & 65280) >> 8), (byte) int_0 };
	// }
	//
	// private byte[] transformIntTo2Bytes(short short_0) {
	// return new byte[] { (byte) (((int) short_0 & 65280) >> 8),
	// (byte) short_0 };
	// }

	public BaseDeviceHandler tablet=null;// = new WacomStuHandlerImpl();

	private Boolean save() {

		return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
			public Boolean run() {
				try {
					JSObjectWrapper.call("log", new Object[] { "save sign : "
							+ tablet.isEmpty() });

					if (tablet.isEmpty()) return false;
					if (!tablet.isEmpty()) {

						if (Signature.isBio)
							Signature.outputSdi = CmsEncryptor
								.getBiometricData();
						else
							Signature.outputSdi="";

//						BufferedImage bufferedImage = tablet
//								.getSignatureImage();
						
						BufferedImage bufferedImage = tablet.getSignatureImage();
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						ImageIO.write(bufferedImage, "png", out);
						if (tablet.getDeviceConfig().scaledSignRectangle!=null) {
							Point r = ((Point) tablet.getDeviceConfig().scaledSignRectangle.getLocation().clone());
//									r.translate(-tablet.getDeviceConfig().getActiveAreaForBackground().x,-tablet.getDeviceConfig().getActiveAreaForBackground().y);
							Device.signatureBeans.add(new SignatureBean(bufferedImage,r, tablet.getDeviceConfig().actualPage));
						}
						byte[] image = out.toByteArray();
						byte[] base64Image = Base64.encode(image);
						Signature.image = new String(base64Image);

						JSObjectWrapper.call("log",
								new Object[] { "SDI : "
										+ Signature.outputSdi });
//						IOUtils.write(image, new FileOutputStream(new File(
//								"c:\\imageFromTablet.png")));
						JSObjectWrapper.call("signAcquired", new Object[] {
								Signature.outputSdi,
								Signature.image });

					}
					tablet.getDeviceConfig().clearCache(tablet.getDeviceConfig().actualPage);
//					tablet.clear();
					return true;

				} catch (Throwable e) {

					JSObjectWrapper.call("log", new Object[] { exToStr(e) });
					return false;
				}
			}
		});

	}

	private String exToStr(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return sw.toString(); // stack trace as a string
	}

	private Image bkImage = null;

	public int totalPage=-1;
	
//	Image loadingImage=null;
	
	public void loadImage() {
		
		try {

			tablet.setLoading(true, true);
//			System.out.println("repaint");
////			repaint();
//			JSObjectWrapper.call("log",
//					new Object[] { "WebSigning: loadImage on path "
//							+ Device.signWithButtons });

			Image overlay = tablet.getDeviceConfig().getButtonsImage();
//			overlay.getGraphics().clearRect(tablet.get, y, width, height);
//			Image overlay = tablet.getDeviceConfig().image;
//			ImageIO.write((RenderedImage)overlay, "png", new File("/tmp/nuovoTest.png"));
			ImageBean image = tablet
					.getDeviceConfig().getPdfCroppedImage(tablet.getDeviceConfig().actualPage);
//					.getDeviceConfig());
			int w = Math.max(image == null ? 0 : image.getImage().getWidth(),
					overlay.getWidth(null));
			int h = Math.max(image == null ? 0 : image.getImage().getHeight(),
					overlay.getHeight(null));
			BufferedImage justButtons = new BufferedImage(w, h,
					tablet.getDeviceConfig().isAlphaSupported()?BufferedImage.TYPE_INT_ARGB:
					BufferedImage.TYPE_INT_ARGB);

			Graphics gJustButton = justButtons.getGraphics();
			BufferedImage allInOne = new BufferedImage(w, h,
					BufferedImage.TYPE_INT_ARGB);

			Graphics gAllInOne = allInOne.getGraphics();
			
			tablet.getDeviceConfig().setImageBean(image);
//			g.clearRect(tablet.getDeviceConfig().get, y, width, height);
			if (image != null) //usare la pdfArea
				gAllInOne.drawImage(image.getImage(), -image.getOffset().x+tablet.getDeviceConfig().getActiveAreaForBackground().x, -image.getOffset().y+tablet.getDeviceConfig().getActiveAreaForBackground().y, null);

			gAllInOne.drawImage(overlay, 0, 0, null);
			
			if (!tablet.getDeviceConfig().isButtonImageFixed()) {

				if (DeviceManager._instance.getSize().width != overlay
						.getWidth(null)
						|| DeviceManager._instance.getSize().height != overlay
								.getHeight(null)) {
					overlay = overlay.getScaledInstance(
							DeviceManager._instance.getSize().width,
							DeviceManager._instance.getSize().height,
							BufferedImage.SCALE_SMOOTH);
					System.out.println("Resizing buttons image");
				}
			}
			
			
			gJustButton.drawImage(overlay, 0, 0, null);
//			if (tablet.getDeviceConfig().isAirModeSupported())
				tablet.setBackgroundImage(justButtons);
//			else
//				tablet.setBackgroundImage(allInOne);
			
			scale = DeviceManager._instance.getWidth()
					/ (float) tablet.getDimension().getWidth();

			bkImage = ImageUtils.scaleImage(justButtons,scale);
			

		} catch (Exception e) {
			e.printStackTrace();
			// throw new InvalidOperationException(e.getMessage());
		}
		finally {
			
			tablet.setLoading(false, false);
			this.repaint();
			
		}

	}

	public void close() {
		if (tablet!=null)
		tablet.stop();

		setVisible(false);
	}

	public static void showError(Throwable e) {
		e.printStackTrace();
		if (_instance != null)
			JOptionPane.showMessageDialog(_instance, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
	}

	public void acquireNext(boolean forceAirMode) {
		try {
			if (tablet==null || tablet instanceof DummyHandlerImpl)
			{
				JSObjectWrapper.call("noDevice", null);
				return;
			}
			tablet.clear();
			tablet.setForceAirMode(forceAirMode);
//			if (forceAirMode)
//				tablet.toggleAirMode();
//			if (!forceAirMode)
//				tablet.toggleAirMode();
//			tablet.toggleAirMode();
			
			tablet.getDeviceConfig().loadTemplate(Device.TAG);
			loadImage();

			
			setVisible(true);

			if (Device.isDEBUG)
				tablet.setEnableDocView(true);
			tablet.acquireNext();

			_instance.repaint();
		} catch (Exception ex) {
			if (ex.getMessage()!=null && ex.getMessage().equals("Device is not connected"))
				showError(ex);
			else
				ex.printStackTrace();
		}

	}

	public void destroy() {
	
		
		// TODO Auto-generated method stub
		if (tablet!=null)
			tablet.destroy();
	}

	public void pressNextButton(boolean forceRepaint) {
		
			
		tablet.getDeviceConfig().actualPage++;
		if (tablet.getDeviceConfig().actualPage>totalPage) {
			tablet.getDeviceConfig().actualPage--;
			return;
		}
		tablet.setLoading(true, forceRepaint);		
		DeviceManager._instance.setPdfB64Image(tablet.getDeviceConfig().baseImageUrl+tablet.getDeviceConfig().actualPage, tablet.getDeviceConfig().signPointWidth, tablet.getDeviceConfig().actualPage, totalPage, false);
		loadImage();
		// TODO Auto-generated method stub
		
	}

	public void pressPrevButton(boolean forceRepaint) {
		tablet.getDeviceConfig().actualPage--;
		if (tablet.getDeviceConfig().actualPage<1) {
			tablet.getDeviceConfig().actualPage++;
			return;
		}
		tablet.setLoading(true, forceRepaint);
		DeviceManager._instance.setPdfB64Image(tablet.getDeviceConfig().baseImageUrl+tablet.getDeviceConfig().actualPage, tablet.getDeviceConfig().signPointWidth, tablet.getDeviceConfig().actualPage, totalPage, false);
		loadImage();
		// TODO Auto-generated method stub
		
	}

}
