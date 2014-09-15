package eu.inn.biosign.device;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import eu.inn.biometric.signature.managed.ManagedIsoPoint;
import eu.inn.biometric.signature.renderer.ImageRenderer;
import eu.inn.biosign.ImageUtils;
import eu.inn.biosign.BioSign;
import eu.inn.biosign.BioSign.Button;
import eu.inn.biosign.device.config.DeviceConfig;
import eu.inn.biosign.util.StaticUtils;
import eu.inn.configuration.ManagedIsoPointSimulated;

public abstract class VisibleDeviceHandler extends BaseDeviceHandler {

	private boolean empty = true;
	private boolean forceAirMode = false;
	protected boolean isAirModeActive = false;
	private Point originalOffset;
	boolean isAirModeEnabled = true;
	private BufferedImage bkImage;
	private long initTime = 0;
	private boolean isDown = false;

	private Button actualButton;

	protected JLabel label;

	public VisibleDeviceHandler() {
		byte[] loading = StaticUtils.getResourceUsingFileStreams(this.getClass().getClassLoader()
				.getResourceAsStream("images/loading.gif"));
		Icon icon = new ImageIcon(loading);
		getVisibleJpanel().setLayout(null);
		label = new JLabel("", icon, JLabel.CENTER);
		getVisibleJpanel().add(label);

	}

	public final void toggleAirMode() {
		isAirModeActive = forceAirMode || (!isAirModeActive);
		if (!changeAirMode()) {
			getDeviceConfig().actualPage = getDeviceConfig().signPage - 1;
			BioSign._instance.pressNextButton(false);
		}
	}

	@Override
	public final boolean isAirModeActive() {
		return isAirModeActive;
	}

	@Override
	public final void setForceAirMode(boolean forceAirMode) {
		this.forceAirMode = forceAirMode;
		this.isAirModeActive = forceAirMode;
	}

	@Override
	public final void clear() {
		initTime = 0;
		BioSign._instance.clearScaledPoints();
		try {
			pointsForBiometric.clear();
			pointsForRenderer.clear();
			getVisibleJpanel().repaint();
		} catch (Exception e) {
			e.printStackTrace();
			BioSign.showError(e);
		}
		empty = true;
	}

	protected void _paintComponent(Graphics g1) {
		try {
//			 panel.paintComponent(g1);

			try {
				BioSign._instance.repaint();
			} catch (Exception ex) {
			}
			Graphics2D g = (Graphics2D) g1;
			System.out.println("before getDevice");
			if (getDeviceConfig() == null) {
				System.err.println("returning");
				return;
			}
			System.out.println("after getDevice");
			System.out.println("ckImage null ? "+(bkImage==null));
			if (bkImage != null) {
				if (getDeviceConfig().getImageBean() != null)
					g.drawImage(getDeviceConfig().getImageBean().getImage(), -getDeviceConfig().getImageBean()
							.getOffset().x + getDeviceConfig().getActiveAreaForBackground().x, -getDeviceConfig()
							.getImageBean().getOffset().y + getDeviceConfig().getActiveAreaForBackground().y, null);
				g.drawImage(bkImage, 0, 0, null);
				if (getDeviceConfig().pagesRectangle != null) {
					String testo = "Pagina " + BioSign._instance.tablet.getDeviceConfig().actualPage + "/"
							+ BioSign._instance.totalPage;
					g.drawImage(ImageUtils.getImageFromText(testo, getDeviceConfig().pagesRectangle.getSize(), null),
							getDeviceConfig().pagesRectangle.x,
							getDeviceConfig().pagesRectangle.y, null);
				}

			}
			ImageRenderer.drawPoints(pointsForRenderer, (Graphics2D) g, Color.BLACK, new Point(0, 0),
					Math.max(1, getDeviceConfig().getSigThickness()), getMaxPressure());
			g.setStroke(new BasicStroke(2));
			if (bkImage != null) {
				g.setColor(new Color(88, 88, 88, 130));
				if (isAirModeEnabled && isAirModeActive) {
					if (getDeviceConfig().getOkButton() != null)
						g.fillRect(getDeviceConfig().getOkButton().x, getDeviceConfig().getOkButton().y,
								getDeviceConfig().getOkButton().width, getDeviceConfig().getOkButton().height);

					if (getDeviceConfig().getRetryButton() != null)
						g.fillRect(getDeviceConfig().getRetryButton().x, getDeviceConfig().getRetryButton().y,
								getDeviceConfig().getRetryButton().width, getDeviceConfig().getRetryButton().height);

				} else {
					g.setColor(Color.black);
					Rectangle sigArea = DeviceConfig.getSignatureArea();
					g.drawRect(sigArea.x, sigArea.y, sigArea.width, sigArea.height);
					// PAINT THE RECT FOR SIGNATURE
				}
				
				// if (config.getCancelButton()!=null)
				// g.fillRect(config.getCancelButton().x,
				// config.getCancelButton().y, config.getCancelButton().width,
				// config.getCancelButton().height);
				g.setColor(new Color(88, 88, 88, 130));
				if (getDeviceConfig().getNextButton() != null
						&& (!isAirModeActive || getDeviceConfig().actualPage == BioSign._instance.totalPage))
					g.fillRect(getDeviceConfig().getNextButton().x, getDeviceConfig().getNextButton().y,
							getDeviceConfig().getNextButton().width, getDeviceConfig().getNextButton().height);
				if (getDeviceConfig().getPrevButton() != null
						&& (!isAirModeActive || getDeviceConfig().actualPage == 1))
					g.fillRect(getDeviceConfig().getPrevButton().x, getDeviceConfig().getPrevButton().y,
							getDeviceConfig().getPrevButton().width, getDeviceConfig().getPrevButton().height);

				if (!isAirModeEnabled && getDeviceConfig().getAirModeButton() != null) {
					g.fillRect(getDeviceConfig().getAirModeButton().x, getDeviceConfig().getAirModeButton().y,
							getDeviceConfig().getAirModeButton().width, getDeviceConfig().getAirModeButton().height);
				}
				if (isAirModeActive) {
					g.setStroke(new BasicStroke(2));
					g.setColor(Color.GREEN);
					g.drawRect(getDeviceConfig().getAirModeButton().x, getDeviceConfig().getAirModeButton().y,
							getDeviceConfig().getAirModeButton().width, getDeviceConfig().getAirModeButton().height);
				}
			}

			// if (loadingImage!=null) {
			// System.out.println("printing loading image");
			// // g.drawImage(loadingImage, 0,0,null);
			// g.drawImage(loadingImage,
			// config.getActiveAreaForBackground().x,config.getActiveAreaForBackground().y,null);
			// }
			// else
			// System.out.println("no loading image");
		} catch (Throwable t) {
		}
	}

	@Override
	public final boolean getForceAirMode() {
		// TODO Auto-generated method stub
		return forceAirMode;
	}

	private boolean changeAirMode() {
		if (getDeviceConfig().getImageBean() != null) {
			if (originalOffset == null)
				originalOffset = getDeviceConfig().getImageBean().getOffset();
			if (forceAirMode || (isAirModeEnabled && isAirModeActive)) {
				lastPoint = null;
				originalOffset = (Point) getDeviceConfig().getImageBean().getOffset().clone();
				// repaint();

				clear();
				return true;
			} else {

				getDeviceConfig().getImageBean().setOffset((Point) originalOffset.clone());
				return false;
				// repaint();
				// clear();
				// repaint();
			}
		}
		return false;
		// TODO Auto-generated method stub
	}

	@Override
	public final void setEnableDocView(boolean enableDocView) {
		boolean oldAirMode = isAirModeActive;
		isAirModeEnabled = enableDocView && getDeviceConfig().isAirModeSupported()
				&& getDeviceConfig().getAirModeButton() != null;
		isAirModeActive = isAirModeActive && isAirModeEnabled;
		if (isAirModeActive != oldAirMode) {

			if (!changeAirMode()) {

				getDeviceConfig().actualPage = getDeviceConfig().signPage - 1;
				BioSign._instance.pressNextButton(false);
				// WebSigning._instance.setpdf
				// WebSigning._instance.loadImage();
			}
		} else
			getVisibleJpanel().repaint();
		// TODO Auto-generated method stub

	}

	@Override
	public final Dimension getDimension() {
		return getVisibleJpanel().getSize();
	}

	protected void paintComponent(java.awt.Graphics g) {
		_paintComponent(g);
	}

	private void createVisibleJPanel() {
		final VisibleDeviceHandler v = this;
		visiblePanel = new JPanel() {

			private static final long serialVersionUID = -235217477730320434L;

			@Override
			protected void paintComponent(java.awt.Graphics g) {
				super.paintComponent(g);
				System.out.println("in paint compo");
				v.paintComponent(g);
			}
		};
	}

	private JPanel visiblePanel;

	protected synchronized JPanel getVisibleJpanel() {
		if (visiblePanel == null) {
			System.out.println("creating visiblePanel");
			createVisibleJPanel();
		}
		return visiblePanel;
	}

	SlidingThread slidingThread;

	class SlidingThread extends Thread {
		boolean running = false;

		@Override
		public void interrupt() {
			running = false;
		}

		public void run() {
			running = true;
			try {
				while (running) {
					Thread.sleep(1);
					boolean repaint = false;
					boolean xMoved = false;
					boolean yMoved = false;
					if (getDeviceConfig().getImageBean().getOffset().x < 0) {
						getDeviceConfig().getImageBean().getOffset().x++;
						repaint = true;
						xMoved = true;
					}

					if (getDeviceConfig().getImageBean().getOffset().y < 0) {
						getDeviceConfig().getImageBean().getOffset().y++;
						repaint = true;
						yMoved = true;
					}

					if (!xMoved
							&& getDeviceConfig().getImageBean().getOffset().x > 0
							&& getDeviceConfig().getImageBean().getImage().getWidth()
									- getDeviceConfig().getImageBean().getOffset().x < getDeviceConfig()
									.getActiveAreaForBackground().width)

					{
						getDeviceConfig().getImageBean().getOffset().x--;
						repaint = true;
					}
					if (!yMoved
							&& getDeviceConfig().getImageBean().getOffset().y > 0
							&& getDeviceConfig().getImageBean().getImage().getHeight()
									- getDeviceConfig().getImageBean().getOffset().y < getDeviceConfig()
									.getActiveAreaForBackground().height) {
						getDeviceConfig().getImageBean().getOffset().y--;
						repaint = true;
					}
					if (repaint)
						getVisibleJpanel().repaint();
					else {
						return;
					}
				}
			} catch (Exception ex) {

			}

		};
	}

	@Override
	public final void stop() {
		try {
			stopCapturing();
			bkImage = null;
			getVisibleJpanel().repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract void stopCapturing();

	@Override
	public final void setBackgroundImage(BufferedImage combined) {
		bkImage = combined;
		getVisibleJpanel().validate();
		getVisibleJpanel().repaint();
	}

	@Override
	public final void setLoading(boolean loading, boolean forceRepaint) {
		label.setBounds(0, 0, getVisibleJpanel().getWidth(), getVisibleJpanel().getHeight());
		label.setVisible(loading);
		getVisibleJpanel().paintImmediately(getVisibleJpanel().getBounds());
		if (forceRepaint) {
			getVisibleJpanel().paintImmediately(getVisibleJpanel().getBounds());
		}
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public final void penPressedSimulated(ManagedIsoPointSimulated penPoint) {
		if (isAirModeActive && penPoint.getPressure() > 0) {
			if (penPoint.getPressure() > 0) {
				if (slidingThread != null) {
					slidingThread.interrupt();
				}
			}
			if (getDeviceConfig().getActiveAreaForBackground().contains(penPoint)) {
//				System.out.println("IN MOVE");
				if (penPoint.getPressure() > 0) {
					if (lastPoint == null) {
						lastPoint = penPoint;
					}
					getDeviceConfig().getImageBean().getOffset()
							.translate(lastPoint.x - penPoint.x, lastPoint.y - penPoint.y);
					getVisibleJpanel().repaint();
				}
			} else {
				if (lastPoint != null) {
					lastPoint = null;
				}
			}
		}
		if (!isAirModeActive) {
			if (!penPoint.simulated) {
				long actualTime = System.currentTimeMillis();
				if (initTime == 0 && penPoint.getPressure() == 0)
					return;
				if (initTime == 0) {
					if (penPoint.getTime() == 0)
						initTime = actualTime;
					else {
						isTimeSupported = true;
						initTime = penPoint.getTime();
					}
				}
				penPoint.setTime((int) ((isTimeSupported ? penPoint.getTime() : actualTime) - initTime));
				pointsForBiometric.add(penPoint);
				getVisibleJpanel().repaint();
				if (BioSign._instance.addScaledPoint(penPoint)) {
					empty = false;
					pointsForRenderer.add(penPoint);
				}
			}
		}
		if ((penPoint.getPressure() > 0) && (!isDown)) {
			isDown = true;
			penDown(penPoint);
		} else if ((penPoint.getPressure() == 0) && (isDown)) {
			isDown = false;
			penUp(penPoint);
		}
		lastPoint = penPoint;
	}

	@Override
	public final boolean isEmpty() {
		return empty;
	}

	private final void penDown(ManagedIsoPoint penPoint) {
		actualButton = getDeviceConfig().getButton(penPoint);
		if (isAirModeActive)
			lastPoint = null;
	}

	private final void penUp(ManagedIsoPointSimulated penPoint) {
		System.out.println("penUp");
		if (isAirModeActive) {
			DeviceConfig config = getDeviceConfig();
			boolean needSliding = false;
			if (config.getImageBean().getImage().getWidth() - config.getImageBean().getOffset().x < config
					.getActiveAreaForBackground().width) {
				needSliding = true;
			}
			if (config.getImageBean().getImage().getHeight() - config.getImageBean().getOffset().y < config
					.getActiveAreaForBackground().height) {
				needSliding = true;
			}
			if (config.getImageBean().getOffset().x < 0) {
				needSliding = true;
			}
			if (config.getImageBean().getOffset().y < 0) {
				needSliding = true;
			}
			lastPoint = null;
			if (needSliding) {
				slidingThread = new SlidingThread();
				slidingThread.start();
			}
		}
		if ((actualButton != null) && (actualButton == getDeviceConfig().getButton(penPoint))) {
			try {
				switch (actualButton) {
				case OK:
					if (!isAirModeActive)
						BioSign._instance.pressOkButton();
					break;
				case CLEAR:
					if (!isAirModeActive)
						BioSign._instance.pressClearButton();
					break;
				case CANCEL:
					BioSign._instance.pressCancelButton();
					break;
				case AIRMODE:
				case NEXT:
				case PREV:
					if (isAirModeEnabled) {
						if (actualButton == Button.AIRMODE) {
							BioSign._instance.pressAirModeButton();
						} else if (isAirModeActive) {
							if (actualButton == Button.NEXT) {
								BioSign._instance.pressNextButton(penPoint.simulated);
							}
							if (actualButton == Button.PREV) {
								BioSign._instance.pressPrevButton(penPoint.simulated);
							}
						}
					}
				default:
					break;
				}
			} catch (Exception e) {
				BioSign.showError(e);
				e.printStackTrace();
			}
		}
	}

	/** FIX THIS TO USE IN PARENT OBJECT */
	@Deprecated
	private boolean isTimeSupported = false;

}
