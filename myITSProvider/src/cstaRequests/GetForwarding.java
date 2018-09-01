package cstaRequests;

public class GetForwarding extends AbstractRequest {
	public GetForwarding(String device){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<GF>\n");
		body.append("<dvc>").append(device).append("</dvc>\n");
		body.append("</GF>");
	}
}
