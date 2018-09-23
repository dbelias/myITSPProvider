package cstaRequests;

import circuit.CstaMessages;

public class SetBusy extends AbstractRequest {
	public SetBusy(String device, boolean isBusy){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<"+CstaMessages.SetBusyOn.getTag()+">\n");
		body.append("<dvc>").append(device).append("</dvc>\n");
		if (isBusy){
			body.append("<zbO>").append("true").append("</zbO>\n");
		}else{
			body.append("<zbO>").append("false").append("</zbO>\n");
		}
		body.append("</"+CstaMessages.SetBusyOn.getTag()+">");
	}

}
