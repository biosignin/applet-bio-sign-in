package eu.inn.biosign.device.sp;

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
