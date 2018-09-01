package cstaRequests;

public class SnapshotDevice extends AbstractRequest{
	public SnapshotDevice(String device){
		body = new StringBuilder();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
		body.append("<SDe>\n");
		body.append("<sO>").append(device).append("</sO>\n");
		body.append("</SDe>");
	}
}
