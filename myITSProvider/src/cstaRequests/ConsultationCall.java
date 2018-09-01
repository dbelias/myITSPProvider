package cstaRequests;

import circuit.CstaMessages;

public class ConsultationCall extends AbstractRequest {
	public ConsultationCall(String deviceId, String consultedDevice){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<"+CstaMessages.ConsultationCall.getTag()+">\n");
		body.append("<eCl>\n");
		body.append("<cID/>\n");
		body.append("<dID>").append(deviceId).append("</dID>\n");
		body.append("</eCl>\n");
		body.append("<cnD>").append(consultedDevice).append("</cnD>\n");
		body.append("</"+CstaMessages.ConsultationCall.getTag()+">");
	}

}
