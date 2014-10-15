package eu.inn.biosign.device.sp;

/*
 * #%L
 * Java Applet for biometric trait acquisition [http://www.biosignin.org]
 * DeviceHandlerServiceProvider.java is part of BioSignIn project
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


import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import eu.inn.biosign.device.BaseDeviceHandler;

public class DeviceHandlerServiceProvider {
	private static DeviceHandlerServiceProvider service;
	private ServiceLoader<IDeviceHandlerFactory> loader;

	private DeviceHandlerServiceProvider() {
		loader = ServiceLoader.load(IDeviceHandlerFactory.class);
	}

	public static synchronized DeviceHandlerServiceProvider getInstance() {
		if (service == null) {
			service = new DeviceHandlerServiceProvider();
		}
		return service;
	}

	public BaseDeviceHandler getConnecteDeviceHandler() {

		Iterator<IDeviceHandlerFactory> factories = loader.iterator();
		while (factories.hasNext()) {
			try {
				IDeviceHandlerFactory d = factories.next();
				BaseDeviceHandler ret = d.getConnecteDeviceHandler();
				if (ret != null)
					return ret;
			} catch (ServiceConfigurationError serviceError) {
				System.out.println("Unavailable provider, try next: " + serviceError.getMessage());
			} catch (Exception ex) {
			}
		}
		return null;
	}

}
