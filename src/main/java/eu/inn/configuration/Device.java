package eu.inn.configuration;

/*
 * #%L
 * Java Applet for biometric trait acquisition [http://www.biosignin.org]
 * Device.java is part of BioSignIn project
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
