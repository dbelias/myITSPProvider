package cstaRequests;

public class GetDoNotDistrub extends AbstractRequest {
	public GetDoNotDistrub(String device){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		body.append("<GDND>\n");
		body.append("<dvc>").append(device).append("</dvc>\n");
		body.append("</GDND>");
	}

}
