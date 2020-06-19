package at.kapschcs.testing;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

import at.kapschcs.testing.messages.TestRequest;

@Service
public class XmlTransformationServiceImpl implements XmlTransformationService {
	
		
	@Autowired
	@Qualifier("marshaller")
	private Marshaller marshaller;
	
	@Autowired
	@Qualifier("unmarshaller")
	private Unmarshaller unmarshaller;
	
	
	public XmlTransformationServiceImpl() {
		
	}

	@Override
	public String toXml(Object obj) throws Exception{
		System.out.println("toXml");
		StringResult result = new StringResult();
		marshaller.marshal(obj, result);
		return result.toString();
	
	}

	@Override
	public TestRequest fromXml(String xml) throws Exception {
		System.out.println("fromXml");
		
		StringSource src = new StringSource(xml);
		return (TestRequest) unmarshaller.unmarshal(src);
		
	}
	
	

}
