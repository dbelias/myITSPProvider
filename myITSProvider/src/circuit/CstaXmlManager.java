package circuit;

import org.apache.log4j.Logger;

public class CstaXmlManager {
	private static Logger logger=Logger.getLogger("CstaXmlManager");
	byte[] myCstaXmlData;
	
	public byte[] createContentRequest(){
		
		myCstaXmlData=CSTAMessageHandler.getCstaOutgoingRequest();
		
		
		return myCstaXmlData;
		
	}
	
	public byte[] createContentResponse(){
		
		myCstaXmlData=CSTAMessageHandler.getCstaOutgoingResponse();
		
		
		return myCstaXmlData;
		
	}
	
	public void getContentResponse(byte[] content){
		CSTAMessageHandler.setIncomingResponse(content);
		
	}
	
	public void getContentRequest(byte[] content){
		CSTAMessageHandler.setCstaIncomingRequest(content);
		
	}
}
