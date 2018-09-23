package cstaOutgoingResponses;

public class SystemStatusResponse extends AbstractOutgoingResponse {
	public SystemStatusResponse(){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<SSR/>\n");
	}

}
