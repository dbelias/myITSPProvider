package cstaRequests;

import circuit.CstaMessages;

public class HoldCall extends AbstractRequest {
	public HoldCall(String deviceId, String callId){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<"+CstaMessages.HoldCall.getTag()+">\n");		
		body.append("<cTBH>\n");
		if (callId.isEmpty()){
			body.append("<cID/>\n");
		}else{
			body.append("<cID>").append(callId).append("</cID>\n");
		}
		body.append("<dID>").append(deviceId).append("</dID>\n");
		body.append("</cTBH>\n");
		body.append("</"+CstaMessages.HoldCall.getTag()+">");
	}

}
