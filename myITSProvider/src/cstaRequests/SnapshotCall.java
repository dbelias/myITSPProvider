package cstaRequests;

import circuit.CstaMessages;

public class SnapshotCall extends AbstractRequest {
	public SnapshotCall(String callId, String deviceId){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<"+CstaMessages.SnapshotCall.getTag()+">\n");
		body.append("<sO>\n");
		if (callId.isEmpty()){
			body.append("<cID/>\n");
		}else{
			body.append("<cID>").append(callId).append("</cID>\n");
		}
		if (deviceId.isEmpty()){
			body.append("<dID/>\n");
		}else{
			body.append("<dID>").append(deviceId).append("</dID>\n");
		}
		body.append("</sO>\n");
		
		body.append("</"+CstaMessages.SnapshotCall.getTag()+">");
	}

}
