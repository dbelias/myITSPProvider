package cstaRequests;

public class MonitorStart extends AbstractRequest {
	public MonitorStart(String deviceObject){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		body.append("<MS>\n");
		body.append("<mO>\n");
		body.append("<dO>").append(deviceObject).append("</dO>\n");
		body.append("</mO>\n");
		body.append("</MS>");
	}

}
