package cstaRequests;

public class RequestSystemStatus extends AbstractRequest {
	
	public RequestSystemStatus(){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<RSS/>\n");
	}
	
	

}
