package cstaRequests;

public class MonitorStop extends AbstractRequest {
	public MonitorStop(String monitorCrossRefID){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		body.append("<MSp>\n");
		body.append("<mCRI>").append(monitorCrossRefID).append("</mCRI>\n");
		body.append("</MSp>");
	}
}
