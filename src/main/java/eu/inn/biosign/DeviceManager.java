package eu.inn.biosign;

import netscape.javascript.JSObject;

//import org.apache.pdfbox.pdmodel.PDDocument;

public class DeviceManager extends SignatureDeviceManager {

	
	public void start() {
		super.start();
		JSObjectWrapper.call("initCompleted", null);
		JSObjectWrapper.call("log",
				new Object[] { "Inizializzazione applet terminatata" });
	}


	public PdfPageInfo getPageInfoFromWeb(int index) {
		JSObject window = JSObject.getWindow(this);
		JSObject pdfInfo = (JSObject) window.eval("getPdfPage(" + index + ")");
		System.out.println("pdfInfo : " + pdfInfo.toString());
		return new PdfPageInfo(((Number) pdfInfo.getMember("totalPages")).intValue(), ((Number) pdfInfo.getMember("pointWidth")).intValue(), (String)pdfInfo.getMember("imgB64"));
	}

}
