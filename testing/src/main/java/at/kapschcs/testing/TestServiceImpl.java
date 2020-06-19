package at.kapschcs.testing;

import org.springframework.stereotype.Service;

import at.kapschcs.testing.messages.TestRequest;
import at.kapschcs.testing.messages.TestResponse;

@Service("testService")
public class TestServiceImpl implements TestService{
	
	public TestServiceImpl() {
		
	}

	@Override
	public TestResponse testMethod(TestRequest req) {
		TestResponse resp = new TestResponse(req);
		resp.setStatus("SUCCESS");
		return resp;
	}
	
	

}
