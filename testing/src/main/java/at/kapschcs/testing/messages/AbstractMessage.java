package at.kapschcs.testing.messages;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public abstract class AbstractMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "id")
	private String id;

	@XmlElement(name = "service")
	private String service;

	@XmlElement(name = "method")
	private String method;

	public AbstractMessage() {

	}
	
	public AbstractMessage(final AbstractMessage message) {
		cloneMessage(message);
	}
	
	private void cloneMessage(final AbstractMessage message) {
		this.id=message.getId();
		this.service=message.getService();
		this.method=message.getMethod();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

}
