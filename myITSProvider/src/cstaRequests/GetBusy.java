package cstaRequests;

import circuit.CstaMessages;

public class GetBusy extends AbstractRequest {
	public GetBusy(String device){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<"+CstaMessages.GetBusy.getTag()+">\n");
		body.append("<dvc>").append(device).append("</dvc>\n");
		body.append("</"+CstaMessages.GetBusy.getTag()+">");
	}

}
