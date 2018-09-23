package cstaOutgoingResponses;

import org.apache.log4j.Logger;

public abstract class AbstractOutgoingResponse {
	private static Logger logger=Logger.getLogger("AbstractOutgoingResponse");
	StringBuilder body;
	public byte[] getBytes(){
		logger.info("CSTA-XML="+body.toString());
		return body.toString().getBytes();
	}

}
