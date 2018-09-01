package cstaRequests;

import circuit.CstaMessages;

public class GenerateDigits extends AbstractRequest {
	public GenerateDigits(String deviceId, String charactersToSend){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<"+CstaMessages.GenerateDigits.getTag()+">\n");
		body.append("<cTSD>\n");
		body.append("<cID/>\n");
		body.append("<dID>").append(deviceId).append("</dID>\n");
		body.append("</cTSD>\n");
		body.append("<cTS>").append(charactersToSend).append("</cTS>\n");
		body.append("</"+CstaMessages.GenerateDigits.getTag()+">");
	}

}
