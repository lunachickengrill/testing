package at.kapschcs.testing;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.MethodInvoker;
import org.springframework.validation.DataBinder;

import at.kapschcs.testing.messages.TestRequest;
import at.kapschcs.testing.messages.TestResponse;

public class SpringTest extends AbstractBaseTest {

	private static TestRequest req = new TestRequest();
	private static Map<String, String> params = new HashMap<>();
	private static String[] allowedFields;
	
	private static final String BIND_ID = "741";
	private static final String BIND_SERVICE = "testService";
	private static final String BIND_METHOD = "testMethod";
	private static final String BIND_FIRSTNAME = "firstName";
	private static final String BIND_LASTNAME = "lastName";
	private static final String BIND_UNKNOWN_PROPERTY = "unknownPropertyValue";

	@BeforeAll
	public static void setAllowedFieldsForDataBinding() {
		allowedFields = new String[] { "id", "firstName", "lastName", "service", "method" };
	}

	@BeforeAll
	public static void createDummyData() {
		req.setId(BIND_ID);
		req.setFirstName(BIND_FIRSTNAME);
		req.setLastName(BIND_LASTNAME);
		req.setService(BIND_SERVICE);
		req.setMethod(BIND_METHOD);
	}

	@BeforeAll
	public static void createDummyParams() {
		params.put("id", BIND_ID);
		params.put("firstName", BIND_FIRSTNAME);
		params.put("lastName", BIND_LASTNAME);
		params.put("service", BIND_SERVICE);
		params.put("method", BIND_METHOD);
		params.put("gibtsNet", BIND_UNKNOWN_PROPERTY);
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
		dataBinder.setAllowedFields(allowedFields);

		MutablePropertyValues mpv = new MutablePropertyValues(params);
		dataBinder.bind(mpv);

		assertTrue((req.getService()!=null && req.getService().equals(BIND_SERVICE)),"service not matching");
		assertTrue((req.getMethod()!=null && req.getMethod().equals(BIND_METHOD)),"method not matching");
		assertTrue((req.getId()!= null && req.getId().equals(BIND_ID)), "id not matching");
		assertTrue((req.getFirstName()!=null && req.getFirstName().equals(BIND_FIRSTNAME)), "firstName not matching");
		assertTrue((req.getLastName()!=null && req.getLastName().equals(BIND_LASTNAME)),"lastName not matching");
		
	}

	@Test
	public void ctxLookupTest() {

		String serviceName = StringUtils.uncapitalize(req.getService());
		String methodName = req.getMethod();

		assertTrue(ctx.containsBean(serviceName), "No bean with name " + serviceName + " in ctx");

		AppService serviceClass = getService(serviceName);

		Method m = getMethod(methodName, serviceClass);
		assertTrue(m != null, "No Method found with name " + methodName);

	}

	@Test
	public void testLookupAndInvoke() {
		String serviceName = StringUtils.uncapitalize(req.getService());
		String methodName = req.getMethod();

		AppService serviceClass = getService(serviceName);
		assertTrue(serviceClass != null, "Service not found with name " + serviceName);
		Method method = getMethod(methodName, serviceClass);
		assertTrue(method != null, "Method not found with name " + methodName);

		TestResponse resp = invoke(serviceClass, method, req);
		assertNotNull(resp, "No response from service invocation");

		System.out.println(resp);

	}

	@Test
	public void testLookupAndInvokeAndXml() {
		String serviceName = StringUtils.uncapitalize(req.getService());
		String methodName = req.getMethod();

		AppService serviceClass = getService(serviceName);
		assertTrue(serviceClass != null, "Service not found with name " + serviceName);
		Method method = getMethod(methodName, serviceClass);
		assertTrue(method != null, "Method not found with name " + methodName);

		TestResponse resp = invoke(serviceClass, method, req);
		assertNotNull(resp, "No response from service invocation");

		try {
			String respXml = transformationService.toXml(resp);
			assertTrue(respXml != null, "Response not transformed");

			System.out.println(respXml);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Test
	public void testService() {
		TestResponse resp = testService.testMethod(req);
		assertNotNull(resp, "Response is null");

		System.out.println(resp);
	}

	private AppService getService(final String serviceName) {
		Assert.isTrue(ctx.containsBean(serviceName), "No bean found with name " + serviceName);
		return ctx.getBean(serviceName, TestService.class);
	}

	private Method getMethod(final String methodName, final AppService service) {

		for (Method m : service.getClass().getDeclaredMethods()) {
			if (m.getName().equals(methodName)) {
				return m;
			}
		}
		return null;
	}

	private TestResponse invoke(AppService service, Method method, TestRequest req) {

		MethodInvoker invoker = new MethodInvoker();
		invoker.setTargetClass(service.getClass());
		invoker.setTargetMethod(method.getName());
		invoker.setTargetObject(service);
		invoker.setArguments(req);

		TestResponse resp = new TestResponse();

		try {
			invoker.prepare();
			return (TestResponse) invoker.invoke();
		} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException
				| IllegalAccessException ex) {
			ex.printStackTrace();
			resp.setStatus("ERROR");
		}

		return resp;
	}

}
