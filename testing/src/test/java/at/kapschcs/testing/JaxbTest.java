package at.kapschcs.testing;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import at.kapschcs.testing.messages.TestRequest;


public class JaxbTest extends AbstractBaseTest{
	
	public static final String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + 
			"<ns2:testRequest xmlns:ns2=\"at.kapschcs\">\n" + 
			"    <service>testService</service>\n" + 
			"    <method>testMethod</method>\n" + 
			"    <id>12345</id>\n" + 
			"    <firstName>First</firstName>\n" + 
			"    <lastName>Last</lastName>\n" + 
			"</ns2:testRequest>";
	
	@Autowired
	private XmlTransformationService transformationService;
	
	@Autowired
	ApplicationContext ctx;

	@Test
	public void testContext() {
		
		assertTrue(ctx.containsBean("marshaller"));
		assertTrue(ctx.containsBean("unmarshaller"));	
	}
	
	@Test
	public void testMarshaller() {
		
		TestRequest req = new TestRequest();
		req.setService("testService");
		req.setMethod("testMethod");
		req.setId("12345");
		req.setFirstName("First");
		req.setLastName("Last");
		try {
		String resp = transformationService.toXml(req);
		System.out.println(resp);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testUnmashaller() {
		
		try {
		TestRequest req = transformationService.fromXml(XML);
		System.out.println(req.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	

}
