package cstaRequests;

import circuit.CstaMessages;

public class AlternateCall extends AbstractRequest {
	public AlternateCall(String deviceId, String callIDActive, String callIDHold){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<"+CstaMessages.AlternateCall.getTag()+">\n");		
		body.append("<atC>\n");
		if (callIDActive.isEmpty()){
			body.append("<cID/>\n");
		}else{
			body.append("<cID>").append(callIDActive).append("</cID>\n");
		}
		body.append("<dID>").append(deviceId).append("</dID>\n");
		body.append("</atC>\n");
		body.append("<hC>\n");
		if (callIDHold.isEmpty()){
			body.append("<cID/>\n");
		}else{
			body.append("<cID>").append(callIDHold).append("</cID>\n");
		}
		body.append("<dID>").append(deviceId).append("</dID>\n");
		body.append("</hC>\n");
		body.append("</"+CstaMessages.AlternateCall.getTag()+">");
	}

}
