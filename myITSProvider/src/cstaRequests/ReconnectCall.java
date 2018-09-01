package cstaRequests;

import circuit.CstaMessages;

public class ReconnectCall extends AbstractRequest {
	public ReconnectCall(String deviceId, String consultedDevice){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<"+CstaMessages.ReconnectCall.getTag()+">\n");
		body.append("<atC>\n");
		body.append("<cID/>\n");
		body.append("<dID>").append(deviceId).append("</dID>\n");
		body.append("</atC>\n");
		body.append("<hC>\n");
		body.append("<cID/>\n");
		body.append("<dID>").append(deviceId).append("</dID>\n");
		body.append("</hC>\n");
		body.append("</"+CstaMessages.ReconnectCall.getTag()+">");
	}

}
