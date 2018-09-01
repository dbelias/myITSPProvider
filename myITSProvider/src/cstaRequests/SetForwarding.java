package cstaRequests;

import circuit.CstaMessages;

public class SetForwarding extends AbstractRequest {
	public SetForwarding(String device, String forwardDN,boolean hasForwarded){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<"+CstaMessages.SetForwardingOn.getTag()+">\n");
		body.append("<dvc>").append(device).append("</dvc>\n");
		body.append("<fTe>").append("forwardImmediate").append("</fTe>\n");
		if (hasForwarded){
			body.append("<aF>").append("true").append("</aF>\n");
			body.append("<fDNN>").append(forwardDN).append("</fDNN>\n");
		}else{
			body.append("<aF>").append("false").append("</aF>\n");
			body.append("<fDNN/>\n");
		}
		body.append("</"+CstaMessages.SetForwardingOn.getTag()+">");
	}

}
