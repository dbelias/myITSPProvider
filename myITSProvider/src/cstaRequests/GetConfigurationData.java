package cstaRequests;

public class GetConfigurationData extends AbstractRequest {
	public GetConfigurationData(String device){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		//body.append("<SDe>\n");
		//body.append("<sO>").append(device).append("</sO>\n");
		//body.append("</SDe>");
	}
}
