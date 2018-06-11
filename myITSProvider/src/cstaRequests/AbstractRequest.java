package cstaRequests;

import org.apache.log4j.Logger;

public abstract class AbstractRequest {
	private static Logger logger=Logger.getLogger("AbstractRequest");
	StringBuilder body;
	public byte[] getBytes(){
		logger.info("CSTA-XML="+body.toString());
		return body.toString().getBytes();
	}
}
