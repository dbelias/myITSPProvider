package cstaRequests;

public class SystemStatus extends AbstractRequest {
	public SystemStatus(){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<SS>\n");
		body.append("<ssS>normal</ssS>\n");
		body.append("</SS>");
	}

}
