package cstaRequests;

import circuit.CstaMessages;

public class ClearConnection extends AbstractRequest {
	public ClearConnection(String callId, String deviceId){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<"+CstaMessages.ClearConnection.getTag()+">\n");
		body.append("<coTBC>\n");
		//body.append("<cID>").append(callId).append("</cID>\n");
		body.append("<cID/>\n");
		body.append("<dID>").append(deviceId).append("</dID>\n");
		body.append("</coTBC>\n");
		body.append("</"+CstaMessages.ClearConnection.getTag()+">");
	}

}
