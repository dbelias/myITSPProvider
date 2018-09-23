package cstaRequests;

public class MyCstaRequest extends AbstractRequest {
	public MyCstaRequest(String r){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append(r);
	}

}
