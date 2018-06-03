package cstaRequests;

public class RequestSystemStatus extends AbstractRequest {
	
	public RequestSystemStatus(){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		body.append("<zGCD>\n");
		body.append("<zcCA>Internet</zcCA>\n");
		body.append("<zdMO>Chrome</zdMO>\n");
		body.append("<zdOS>WebRTC</zdOS>\n");
		body.append("<zcDG/>\n");
		body.append("<zrTC>true</zrTC>\n");
		body.append("</zGCD>");
	}
	
	

}
