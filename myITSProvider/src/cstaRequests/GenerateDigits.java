package cstaRequests;

import circuit.CstaMessages;

public class GenerateDigits extends AbstractRequest {
	public GenerateDigits(String deviceId, String charactersToSend, String callID){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<"+CstaMessages.GenerateDigits.getTag()+">\n");
		body.append("<cTSD>\n");
		if (callID.isEmpty()){
			body.append("<cID/>\n");
		}else{
			body.append("<cID>").append(callID).append("</cID>\n");
		}
		body.append("<dID>").append(deviceId).append("</dID>\n");
		body.append("</cTSD>\n");
		body.append("<cTS>").append(charactersToSend).append("</cTS>\n");
		body.append("</"+CstaMessages.GenerateDigits.getTag()+">");
	}

}
