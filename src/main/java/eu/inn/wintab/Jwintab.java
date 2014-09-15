package eu.inn.wintab;


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

