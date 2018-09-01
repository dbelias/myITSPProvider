package cstaRequests;

import circuit.CstaMessages;

public class DeflectCall extends AbstractRequest {
	public DeflectCall(String deviceId, String newDestination){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<"+CstaMessages.DeflectCall.getTag()+">\n");
		body.append("<cTBD>\n");
		body.append("<cID/>\n");
		body.append("<dID>").append(deviceId).append("</dID>\n");
		body.append("</cTBD>\n");
		body.append("<nD>").append(newDestination).append("</nD>\n");
		body.append("</"+CstaMessages.DeflectCall.getTag()+">");
	}

}
