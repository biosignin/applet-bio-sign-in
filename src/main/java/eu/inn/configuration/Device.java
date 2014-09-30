package eu.inn.configuration;

import java.util.LinkedList;

//import org.apache.pdfbox.pdmodel.PDDocument;

import eu.inn.biosign.device.SignatureBean;

public class Device {

	public static boolean IS_MOUSE_ALLOWED = true;

	public static boolean isDEBUG = false;

//	public static PDDocument PDFDOCUMENT = null;

	public static boolean useAppletRendering = true;

	public static String TAG;
	public static double clientRatio = 1;

	public static LinkedList<SignatureBean> signatureBeans = new LinkedList<SignatureBean>();

//	public static boolean useExternalRenderer = false;

}
