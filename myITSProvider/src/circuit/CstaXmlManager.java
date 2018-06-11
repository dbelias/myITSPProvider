package circuit;

import org.apache.log4j.Logger;

public class CstaXmlManager {
	private static Logger logger=Logger.getLogger("CstaXmlManager");
	byte[] myCstaXmlData;
	private volatile CSTAMessageHandler myCstaHandler;
	
	
	public CstaXmlManager(){
		logger.info("Call CstaXmlManager constructor");
		myCstaHandler=CSTAMessageHandler.getInstance();
	}
	
	
	
	public byte[] createContentRequest(){
		
		myCstaXmlData=myCstaHandler.getCstaOutgoingRequest();
		
		logger.trace("byte[]:"+new String(myCstaXmlData));
		return myCstaXmlData;
		
	}
	
	public byte[] createContentResponse(){
		
		myCstaXmlData=myCstaHandler.getCstaOutgoingResponse();
		
		
		return myCstaXmlData;
		
	}
	
	public void getContentResponse(byte[] content){
		myCstaHandler.setIncomingResponse(content);
		
	}
	
	public void getContentRequest(byte[] content){
		myCstaHandler.setCstaIncomingRequest(content);
		
	}
}
