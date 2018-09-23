package cstaRequests;

import circuit.CstaMessages;

public class RetreiveCall extends AbstractRequest {
	public RetreiveCall(String deviceId, String callId){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<"+CstaMessages.RetreiveCall.getTag()+">\n");		
		body.append("<cTBRd>\n");
		if (callId.isEmpty()){
			body.append("<cID/>\n");
		}else{
			body.append("<cID>").append(callId).append("</cID>\n");
		}
		body.append("<dID>").append(deviceId).append("</dID>\n");
		body.append("</cTBRd>\n");
		body.append("</"+CstaMessages.RetreiveCall.getTag()+">");
	}

}
