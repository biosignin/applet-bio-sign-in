package eu.inn.biosign;

import javax.swing.JApplet;

import netscape.javascript.JSObject;

public class JSObjectWrapper {
	private static JSObject object;
		
	public static void init(JApplet applet) {
		try {
			
		
		if (object == null)
			object = JSObject.getWindow(applet);
		}catch (Throwable t) {
			System.out.println("Unable to start communication with browser");
		}
	}
	
	public static JSObject getObject() {
		if (object == null)
			object = JSObject.getWindow(DeviceManager._instance);
		return object;
	}
	
	public static void call(final String method,final Object[] args) {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					if (method.equalsIgnoreCase("log")) {
						System.out.println(args[0]);
						return;
					}
					getObject().call(method, args);
					
						
				}catch (Throwable t) {
					System.out.println("Unable to communicate with browser. Method:  "+method+". Args #"+(args==null?"null":args.length));
					t.printStackTrace();
				}
				
			}
		}).start();
		
	}

}
