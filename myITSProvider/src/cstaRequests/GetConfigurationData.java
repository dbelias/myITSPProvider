package cstaRequests;

public class GetConfigurationData extends AbstractRequest {
	public GetConfigurationData(String device){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<zGCD>\n");
		body.append("<zcDG>").append(device).append("</zcDG>\n");
		body.append("</zGCD>");
	}
}
