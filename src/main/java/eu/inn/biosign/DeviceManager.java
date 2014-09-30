package eu.inn.biosign;

import netscape.javascript.JSObject;

//import org.apache.pdfbox.pdmodel.PDDocument;

public class DeviceManager extends SignatureDeviceManager{

	
	public void start() {
		super.start();
		JSObjectWrapper.call("initCompleted", null);
		JSObjectWrapper.call("log",
				new Object[] { "Inizializzazione applet terminatata" });
	}

	

}
