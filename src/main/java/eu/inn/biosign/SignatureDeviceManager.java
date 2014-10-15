package eu.inn.biosign;

/*
 * #%L
 * Java Applet for biometric trait acquisition [http://www.biosignin.org]
 * SignatureDeviceManager.java is part of BioSignIn project
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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JPanel;

import netscape.javascript.JSObject;

import org.bouncycastle.util.encoders.Base64;

import eu.inn.biosign.device.SignatureBean;
import eu.inn.biosign.listener.IDeviceListener;
import eu.inn.biosign.util.FileUtils;
import eu.inn.biosign.util.MemoryWarningSystem;
import eu.inn.configuration.BindingData;
import eu.inn.configuration.Device;

//import org.apache.pdfbox.pdmodel.PDDocument;

public class SignatureDeviceManager extends JApplet {

//	private JPanel renderingPanel=null;
	
	public IExternalImageRenderer externalImageRenderer = null;

	public void setImageRenderer(IExternalImageRenderer renderer, boolean isMixed) {
		Device.useAppletRendering = isMixed;
		externalImageRenderer = renderer;
		if (BioSign._instance != null && BioSign._instance.tablet != null
				&& BioSign._instance.tablet.getDeviceConfig() != null) {
			BioSign._instance.tablet.getDeviceConfig().clearAllCache();
		// Device.cachePureImageFromPDF.
		setPdfBase64Image("", -1, 1, -1);
		}
	}

	List<IDeviceListener> listeners = new ArrayList<IDeviceListener>();

	public void addListener(IDeviceListener listener) {
		listeners.add(listener);
	}

	public void removeListener(IDeviceListener listener) {
		try {
			listeners.remove(listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final long serialVersionUID = 1L;
	public static SignatureDeviceManager _instance = null;
	protected BioSign webSigning = null;

	public void setServerKeyLength(int value) {
		CmsEncryptor.ServerKeyLength = value;

	}

	@Override
	public void setPreferredSize(Dimension preferredSize) {
		// TODO Auto-generated method stub
		super.setPreferredSize(preferredSize);
	}

	public final static float DEFAULT_EXPORT_SCALE = 1f / (3f / 4f) / 2f;// 1f/(793f/1190f)/2;

//	public SignatureDeviceManager() {
//		this(null);
//	}
	
	
	public SignatureDeviceManager() {
//		this.renderingPanel=panel;
		// TODO Auto-generated constructor stub
		_instance = this;
		setFocusable(true);
		setExportScale(DEFAULT_EXPORT_SCALE);
		MemoryWarningSystem.setPercentageUsageThreshold(0.8);

		MemoryWarningSystem mws = new MemoryWarningSystem();
		mws.addListener(new MemoryWarningSystem.Listener() {
			public void memoryUsageLow(long usedMemory, long maxMemory) {
				System.out.println("Memory usage low!!!");
				double percentageUsed = ((double) usedMemory) / maxMemory;
				System.out.println("percentageUsed = " + percentageUsed);
				if (BioSign._instance != null && BioSign._instance.tablet != null
						&& BioSign._instance.tablet.getDeviceConfig() != null)
					BioSign._instance.tablet.getDeviceConfig().didReceiveMemoryWarning();
			}
		});

	}
	

	// public static URL codeBase;

	public void init() {
		try {
			// codeBase=thisgetpgetProtectionDomain().getCodeSource();
			// _instance.validate();

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private float exportScale;// =1f/(793f/1190f);

	public void setExportScale(float scale) {
		exportScale = scale;
	}

	private void debugOnTablet() {

		Device.isDEBUG = true;
		System.out.println("in debug mode");

		setSignRectangle(62, 682, 151, 75);
		Device.TAG = " seq=45,displayName=Firma correntista,w=250,h=75,description=ciao sono la descrizione della firma";

		// TabletManager.IS_MOUSE_ALLOWED=true;
		String imageString = null;
		try {
			imageString = new String(Base64.encode(FileUtils.readFileToByteArray(new File("c:\\getImage.png"))));
			;
			String urlString = "http://192.168.1.161:8081/bio-sign-in/signManagement";
			String uuid = "";
			// setPdfFile(new String(Base64.encode(FileUtils.readFileToByteArray(new File("c:\\getImage.png")))));
			setPdfBase64Image(imageString, 595, 1, 3);
		} catch (Exception ioe) {
			System.out.println("Exception while reading the Image " + ioe);
		}
		// setEnableDocView(true+"");
		// Device.setBase64PdfImage(b64);
		boolean forceShowDocument = false;
		while (!startCapture(forceShowDocument))
			if (!Device.IS_MOUSE_ALLOWED)
				Device.IS_MOUSE_ALLOWED = true;
			else
				setPdfBase64Image(imageString, 595, 1, 3);

		// forceShowDocument=!forceShowDocument;
		long start = System.nanoTime();
		// getPageB64(1);
		long last = System.nanoTime();
		System.out.println("tablet getPageB64 in " + ((last - start) / 1000000) + "ms");
		// this.add
	}

	public void start() {
		if (webSigning == null) {
			try {
				webSigning = new BioSign();
			} catch (Exception ex) {
				ex.printStackTrace();
				// JSObjectWrapper.call("noDevice", null);
				return;
			}

			// Frame[] frames = Frame.getFrames();
			// for (Frame frame : frames) {
			// frame.setMenuBar(null);
			// frame.set
			// frame.pack();
			// }
			_instance.add(webSigning);
		}

		if (!System.getProperty("debugApp", "0").equals("0")) {
			System.out.println("debugging");
			debugOnTablet();
		}
		// JSObjectWrapper.call("initCompleted", null);
		// JSObjectWrapper.call("log", new Object[] { "Inizializzazione applet terminatata" });
	}

	public void stop() {
		try {
			System.out.println("Applet in Stop");
			if (BioSign._instance != null)
				BioSign._instance.close();
			// JSTUTablet.clearScreen();
			// JSTUTablet.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			// JSObjectWrapper.call("log", new Object[] { exToStr(e) });
		}
	}

	public String getDeviceInfo() {
		try {
			return BioSign._instance.tablet.getTabletDescription();
		} catch (Exception ex) {
			return "NO_DEVICE";
		}
	}

	public void destroy(boolean exit) {
		clearPanel();
		System.out.println("Applet in destroy " + exit);
		if (webSigning != null)
			webSigning.destroy();

		if (exit) {

			System.exit(0);
		}
	}

	public void destroy() {
		destroy(true);
		// System.out.println("Applet in destroy");
		// System.exit(0);
	}

	protected String exToStr(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return sw.toString(); // stack trace as a string
	}

	public void setPdfB64Image(final String imageDataString, final int pdfPointWidth, final int actualPage,
			final int totalPage, final boolean refreshCache) {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				try {

					if (refreshCache)
						webSigning.tablet.getDeviceConfig().clearCache(actualPage);
					// WebSigning._instance.tablet.getDeviceConfig().setSignPage(actualPage);
					BioSign._instance.tablet.getDeviceConfig().setBase64PdfImage(imageDataString, pdfPointWidth,
							actualPage, totalPage);

					// int
					// i=Integer.parseInt(imageDataString.substring(imageDataString.lastIndexOf("&page=")+6,imageDataString.lastIndexOf("&random")));

				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return null;
			}
		});
	}

	public void setPdfBase64Image(String imageDataString, int pdfPointWidth, int actualPage, int totalPage) {

		BioSign._instance.tablet.getDeviceConfig().setSignPage(actualPage);
		setPdfB64Image(imageDataString, pdfPointWidth, actualPage, totalPage, true);
	}

	public void setSignRectangleAndScale(int x, int y, int width, int height, float scale) {
		BioSign._instance.tablet.getDeviceConfig()
				.setSignRectangle(x / scale, y / scale, width / scale, height / scale);
	}

	public void setSignRectangle(int x, int y, int width, int height) {
		setSignRectangleAndScale(x, y, width, height, exportScale);
	}

	public static Dimension getDimension() {
		return _instance.getSize();
	}

	// public Boolean startCapture() {
	// return startCapture(false);
	// }

	public Boolean showDocument() {
		return startCapture(true);
	}

	public Boolean startCapture(final boolean forceAirMode) {

		return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
			public Boolean run() {
				try {
					// JSObjectWrapper.call("log", new
					// Object[]{"Inizializzazione Pagina di firma"});
					if (webSigning == null) {
						webSigning = new BioSign();
						_instance.add(webSigning);
					}
					webSigning.acquireNext(forceAirMode);

					return true;
				} catch (Throwable e) {
					e.printStackTrace();
					// JSObjectWrapper.call("log", new Object[] { exToStr(e) });
				}
				return false;
			}
		});
	}

	/* Metodi di configurazione del Device */

	public void allowMouse(String asBoolean) {
		try {
			Device.IS_MOUSE_ALLOWED = Boolean.parseBoolean(asBoolean);
		} catch (Exception ex) {
			Device.IS_MOUSE_ALLOWED = false;
		}

	}

	public void setBindingData(String hashDocument, String digestAlgoritm, int offset, int count) {

		BindingData.hashDocument = hashDocument;
		BindingData.digestAlgoritm = digestAlgoritm;
		BindingData.offset = offset;
		BindingData.count = count;

	}

	public void clearPanel() {
		try {
			webSigning.tablet.clear();
		} catch (Exception ex) {

		}
	}

	public void setSigTag(String value) {
		Device.TAG = value;
	}

	public void setPdfFile(final String base64) {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			@Override
			public Object run() {

				ByteArrayInputStream bais = null;
				try {
					byte[] b = Base64.decode(base64);
					bais = new ByteArrayInputStream(b);
					// Device.PDFDOCUMENT = PDDocument.load(bais);
					if (BioSign._instance != null && BioSign._instance.tablet != null
							&& BioSign._instance.tablet.getDeviceConfig() != null)
						BioSign._instance.tablet.getDeviceConfig().clearAllCache();
					// Device.cachePureImageFromPDF.
					Device.useAppletRendering = true;
					setPdfBase64Image("", -1, 1, -1);
				} catch (Exception ex) {
					ex.printStackTrace();
					try {
						if (bais != null)
							bais.close();
					} catch (Exception e) {
					}
				}
				return null;
			}
		});
	}

	public void setEnablePdfJS() {
		final SignatureDeviceManager self = this;
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			@Override
			public Object run() {
				try {
					// Device.useExternalRenderer = true;
					self.setImageRenderer(new IExternalImageRenderer() {

						@Override
						public PdfPageInfo getPageInfo(int index) {
							JSObject window = JSObject.getWindow(self);
							JSObject pdfInfo = (JSObject) window.eval("getPdfPage(" + index + ")");
							System.out.println("pdfInfo : " + pdfInfo.toString());
							return new PdfPageInfo(((Number) pdfInfo.getMember("totalPages")).intValue(),
									((Number) pdfInfo.getMember("pointWidth")).intValue(), (String) pdfInfo
											.getMember("imgB64"));
						}
					},true);

					
				} catch (Exception ex) {
					ex.printStackTrace();

				}
				return null;
			}
		});
	}

	// public JSObject getPageInfoFromWeb(int index) {
	// JSObject window = JSObject.getWindow(this);
	// return (JSObject) window.eval("getPdfPage("+index+")");
	// }

	public void setEnableDocView(String asBoolean) {
		System.out.println("EnableDocView " + asBoolean);
		boolean enableDocView = false;
		try {
			enableDocView = Boolean.parseBoolean(asBoolean);
		} catch (Exception ex) {
			ex.printStackTrace();
			enableDocView = false;
		}
		webSigning.tablet.setEnableDocView(enableDocView);
	}

	public void removeLastSignature() {
		SignatureBean sBean = Device.signatureBeans.removeLast();
		webSigning.tablet.getDeviceConfig().clearCache(sBean.getPage());
	}

	public int getAllPages() {
		if (!Device.useAppletRendering)
			throw new IllegalStateException("Internal Rendering is disabled");
		if (webSigning == null)
			throw new IllegalStateException("DeviceManager is not initialized");
		return webSigning.totalPage;
	}

	public String getPageB64(int page) {

		return getPageB64(page, exportScale);
	}

	public String getPageB64(final int page, final float scale) {
		return AccessController.doPrivileged(new PrivilegedAction<String>() {
			@Override
			public String run() {
				try {
					if (!Device.useAppletRendering)
						throw new IllegalStateException("Internal Rendering is disabled");
					if (webSigning == null)
						throw new IllegalStateException("DeviceManager is not initialized");
					setPdfB64Image("", 0, page, 0, false);
					return webSigning.tablet.getDeviceConfig().getPageImageB64Cached(page, scale);
				} catch (IllegalStateException t) {
					t.printStackTrace();
					throw t;
				}

			}
		});

	}

	public Image getPageBufferedImage(int page) {
		return getPageBufferedImage(page, exportScale);
	}

	public Image getPageBufferedImage(int page, float scale) {
		if (!Device.useAppletRendering)
			throw new IllegalStateException("Internal Rendering is disabled");
		if (webSigning == null)
			throw new IllegalStateException("DeviceManager is not initialized");
		setPdfB64Image("", 0, page, 0, false);
		return webSigning.tablet.getDeviceConfig().getPageImageCached(page, scale);
	}

}
