package cstaRequests;

import circuit.CstaMessages;

public class SingleStepTransferCall extends AbstractRequest {
	public SingleStepTransferCall(String deviceId, String transferedTo){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<"+CstaMessages.SingleStepTransferCall.getTag()+">\n");
		body.append("<atC>\n");
		body.append("<cID/>\n");
		body.append("<dID>").append(deviceId).append("</dID>\n");
		body.append("</atC>\n");
		body.append("<tTo>").append(transferedTo).append("</tTo>\n");
		body.append("</"+CstaMessages.SingleStepTransferCall.getTag()+">");
	}

}
