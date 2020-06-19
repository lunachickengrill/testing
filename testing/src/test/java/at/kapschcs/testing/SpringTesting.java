package at.kapschcs.testing;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.UnmarshalException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.util.Assert;
import org.springframework.util.MethodInvoker;
import org.springframework.validation.DataBinder;

import at.kapschcs.testing.messages.AbstractMessage;
import at.kapschcs.testing.messages.TestRequest;
import at.kapschcs.testing.messages.TestResponse;

public class SpringTesting extends AbstractBaseTest {

	private TestRequest req;

	@BeforeEach
	public void createDummyData() {

		req = new TestRequest();
		req.setId("12345");
		req.setFirstName("First");
		req.setLastName("Last");
		req.setService("testService");
		req.setMethod("testMethod");

	}

	@Autowired
	private TestService testService;

	@Autowired
	private ApplicationContext ctx;
	
	@Autowired
	private XmlTransformationService transformationService;

	@Test
	public void dataBinderTest() {
		TestRequest req = new TestRequest();
		DataBinder dataBinder = new DataBinder(req);

		Map<String, String> map = new HashMap<>();
		map.put("id", "741");
		map.put("firstName", "Data");
		map.put("lastName", "Binder");
		map.put("service", "testService");
		map.put("method", "testMethod");
		map.put("gibtsNet", "asdf");
		MutablePropertyValues mpv = new MutablePropertyValues(map);

		dataBinder.bind(mpv);

		System.out.println(req.toString());
	}

	@Test
	public void ctxLookupTest() {

		String serviceName = StringUtils.uncapitalize(req.getService());
		String methodName = req.getMethod();

		assertTrue(ctx.containsBean(serviceName), "No bean with name " + serviceName + " in ctx");

		TestService serviceClass = getService(serviceName);

		Method m = getMethod(methodName, serviceClass);
		assertTrue(m != null, "No Method found with name " + methodName);
		
		

	}

	@Test
	public void testLookupAndInvoke() {
		String serviceName = StringUtils.uncapitalize(req.getService());
		String methodName = req.getMethod();

		TestService serviceClass = getService(serviceName);
		assertTrue(serviceClass!=null, "Service not found with name " + serviceName);
		Method method = getMethod(methodName, serviceClass);
		assertTrue(method!=null, "Method not found with name " + methodName);

		TestResponse resp = invoke(serviceClass, method, req);
		assertNotNull(resp, "No response from service invocation");
		
		System.out.println(resp);

	}
	
	@Test
	public void testLookupAndInvokeAndXml() {
		String serviceName = StringUtils.uncapitalize(req.getService());
		String methodName = req.getMethod();

		TestService serviceClass = getService(serviceName);
		assertTrue(serviceClass!=null, "Service not found with name " + serviceName);
		Method method = getMethod(methodName, serviceClass);
		assertTrue(method!=null, "Method not found with name " + methodName);

		TestResponse resp = invoke(serviceClass, method, req);
		assertNotNull(resp, "No response from service invocation");
		
		try {
		String respXml = transformationService.toXml(resp);
		assertTrue(respXml!=null, "Response not transformed");
		
		System.out.println(respXml);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	@Test
	public void testService() {
		TestResponse resp = testService.testMethod(req);
		assertNotNull(resp, "Response is null");
		
		System.out.println(resp);
	}
	
	

	private TestService getService(final String serviceName) {
		Assert.isTrue(ctx.containsBean(serviceName), "No bean found with name " + serviceName);
		return ctx.getBean(serviceName, TestService.class);
	}

	private Method getMethod(final String methodName, final TestService service) {

		for (Method m : service.getClass().getDeclaredMethods()) {
			if (m.getName().equals(methodName)) {
				return m;
			}
		}
		return null;
	}

	private TestResponse invoke(TestService service, Method method, TestRequest req) {

		MethodInvoker invoker = new MethodInvoker();
		invoker.setTargetClass(service.getClass());
		invoker.setTargetMethod(method.getName());
		invoker.setTargetObject(service);
		invoker.setArguments(req);
		

		TestResponse resp = new TestResponse();

		try {
			invoker.prepare();
			return (TestResponse) invoker.invoke();
		} catch (ClassNotFoundException| NoSuchMethodException| InvocationTargetException | IllegalAccessException ex) {
			ex.printStackTrace();
			resp.setStatus("ERROR");
		}

		return resp;
	}

}
