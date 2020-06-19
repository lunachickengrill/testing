package at.kapschcs.testing;

import javax.xml.bind.JAXBElement;

import at.kapschcs.testing.messages.TestRequest;

public interface XmlTransformationService {
	
	public String toXml(Object obj) throws Exception;
	public TestRequest fromXml(String xml) throws Exception;

}
