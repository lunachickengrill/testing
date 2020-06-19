package at.kapschcs.testing.messages;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "testRequest", namespace = "at.kapschcs")
@XmlAccessorType(XmlAccessType.NONE)
public class TestRequest extends AbstractMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "firstName")
	private String firstName;

	@XmlElement(name = "lastName")
	private String lastName;

	public TestRequest() {
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "TestRequest [firstName=" + firstName + ", lastName=" + lastName + ", getId()=" + getId()
				+ ", getService()=" + getService() + ", getMethod()=" + getMethod() + "]";
	}

}
