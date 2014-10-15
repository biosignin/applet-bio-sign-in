package eu.inn.tester;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import eu.inn.biometric.signature.extendeddata.ExtendedData;

@Root
public class NewData extends ExtendedData {

	@Element
	private String test = "campoTest";

}
