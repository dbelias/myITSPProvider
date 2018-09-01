package cstaRequests;

import circuit.CstaMessages;

public class TransferCall extends AbstractRequest {
	public TransferCall(String deviceId, String transferedDevice){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<"+CstaMessages.TransferCall.getTag()+">\n");
		body.append("<hC>\n");
		body.append("<cID/>\n");
		body.append("<dID>").append(deviceId).append("</dID>\n");
		body.append("</hC>\n");
		body.append("<atC>\n");
		body.append("<cID/>\n");
		body.append("<dID>").append(deviceId).append("</dID>\n");
		body.append("</atC>\n");
		body.append("</"+CstaMessages.TransferCall.getTag()+">");
	}

}
