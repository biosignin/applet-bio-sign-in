package eu.inn.biosign;

/*
 * #%L
 * Java Applet for biometric trait acquisition [http://www.biosignin.org]
 * JSObjectWrapper.java is part of BioSignIn project
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
