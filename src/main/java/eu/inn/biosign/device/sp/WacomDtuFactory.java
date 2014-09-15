package eu.inn.biosign.device.sp;

import eu.inn.biosign.device.BaseDeviceHandler;
import eu.inn.biosign.device.impl.WacomDtuMouseHandlerImpl;
import eu.inn.biosign.device.impl.WacomDtuWinTabHandlerImpl;

public class WacomDtuFactory implements IDeviceHandlerFactory{

	@Override
	public BaseDeviceHandler getConnecteDeviceHandler() {
		if (WacomDtuWinTabHandlerImpl.isConnected())
			return new WacomDtuWinTabHandlerImpl();
		if (WacomDtuMouseHandlerImpl.isConnected())
			return new WacomDtuMouseHandlerImpl();
		return null;
	}

}
