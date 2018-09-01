package cstaRequests;

import circuit.CstaMessages;

public class SetDoNotDisturb extends AbstractRequest {
	public SetDoNotDisturb(String device, boolean hasDND){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<"+CstaMessages.SetDoNotDisturbOn.getTag()+">\n");
		body.append("<dvc>").append(device).append("</dvc>\n");
		if (hasDND){
			body.append("<dNDO>").append("true").append("</dNDO>\n");
		}else{
			body.append("<dNDO>").append("false").append("</dNDO>\n");
		}
		body.append("</"+CstaMessages.SetDoNotDisturbOn.getTag()+">");
	}

}
