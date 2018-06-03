package cstaRequests;

public abstract class AbstractRequest {
	StringBuilder body;
	public byte[] getBytes(){
		return body.toString().getBytes();
	}
}
