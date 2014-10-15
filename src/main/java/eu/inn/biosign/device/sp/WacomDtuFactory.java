package eu.inn.biosign.device.sp;

/*
 * #%L
 * Java Applet for biometric trait acquisition [http://www.biosignin.org]
 * WacomDtuFactory.java is part of BioSignIn project
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
