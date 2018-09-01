package cstaRequests;

import circuit.CstaMessages;

public class MakeCall extends AbstractRequest {
	public MakeCall(String callingDevice, String calledDirectoryNumber){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<"+CstaMessages.MakeCall.getTag()+">\n");
		body.append("<caD>").append(callingDevice).append("</caD>\n");
		body.append("<cDN>").append(calledDirectoryNumber).append("</cDN>\n");
		body.append("<aOe>").append("prompt").append("</aOe>\n");;
		body.append("</"+CstaMessages.MakeCall.getTag()+">");
	}

}
