package eu.inn.wintab;

/*
 * #%L
 * Java Applet for biometric trait acquisition [http://www.biosignin.org]
 * Jwintab.java is part of BioSignIn project
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



/**
 *  Java interface to Wintab
 *
 *  @version $Id: Jwintab.java,v 1.1 
 *  @author 
 *
 **/   
public class Jwintab {
	
	

    Jwintab(){}

    /** returns current version number 
     */
    public static native int getVersion();
    
    public static native int open(int maxX, int maxY);

	/** call this before closing the application, otherwise tablet does not
	    work.
	**/    
    public static native int close();
    
    /** val[0..5]  = {x, y, button, orientation, angle, pressure}
     */
    public static native int getPacket(int val[]);
    

    public static native int getInfoAxis(int val[], int deviceId);
    
    
//    public static native int MonitorEnumProc(IntPtr hDesktop, IntPtr hdc, ref Rect pRect, int dwData);

}

