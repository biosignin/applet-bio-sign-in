package eu.inn.tester;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import eu.inn.biometric.signature.extendeddata.AbstractExtendedData;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class NewData extends AbstractExtendedData {

	@XmlElement
	private String test = "campoTest";

}
