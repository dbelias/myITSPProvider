package cstaRequests;

public class GetLogicalDeviceInformation extends AbstractRequest {
	public GetLogicalDeviceInformation(String device){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		body.append("<GLDI>\n");
		body.append("<dvc>").append(device).append("</dvc>\n");
		body.append("</GLDI>");
	}
}
