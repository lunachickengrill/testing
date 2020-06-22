package at.kapschcs.testing;

import at.kapschcs.testing.messages.TestRequest;
import at.kapschcs.testing.messages.TestResponse;

public interface TestService extends AppService {
	
	public TestResponse testMethod(final TestRequest req);

}
