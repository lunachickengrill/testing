package at.kapschcs.testing.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "testResponse", namespace = "at.kapschcs")
@XmlAccessorType(XmlAccessType.NONE)
public class TestResponse extends AbstractMessage {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "status")
	private String status;

	public TestResponse() {

	}
	
	public TestResponse(final AbstractMessage message) {
		super(message);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "TestResponse [status=" + status + ", getService()=" + getService() + ", getMethod()=" + getMethod()
				+ "]";
	}

}
