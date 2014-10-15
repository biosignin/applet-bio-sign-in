package eu.inn.examples;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import eu.inn.biometric.signature.extendeddata.ExtendedData;

@Root(name="asd")
public class GpsCoordinates extends ExtendedData {	

	@Element
    public double latitude;
    
	@Element
    public double longitude;

    public GpsCoordinates() {
	}
    
    public GpsCoordinates(double latitude, double longitude) {
    	this.latitude = latitude;
        this.longitude = longitude;
    }
}