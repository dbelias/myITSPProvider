package circuit;

import org.apache.log4j.Logger;

public class CSTAMessageHandler {
	private static Logger logger=Logger.getLogger("CircuitHandler");
	private static  byte[] CstaOutgoingRequest="Any String you want".getBytes();
	private static  byte[] CstaIncomingResponse="Any String you want".getBytes();
	private static  byte[] CstaIncomingRequest="Any String you want".getBytes();
	private static  byte[] CstaOutgoingResponse="Any String you want".getBytes();
	private static String device;
	private static String monitorCrossRefID;
	private static String callID;
	private static CstaMessages messageOutgoing;
	private static CstaMessages messageIncoming;
	private static volatile CSTAMessageHandler myCstaMessageHandler;
	
	
	public CSTAMessageHandler(){
		logger.info("Call CSTAMessageHandler constructor");
		
	}
	
	public static synchronized CSTAMessageHandler getInstance(){
		if (myCstaMessageHandler==null){
			myCstaMessageHandler=new CSTAMessageHandler();
		}
		return myCstaMessageHandler;
	}
	
	public void setOutgoingRequest(byte[] req){
		CstaOutgoingRequest=req;
	}
	public void setIncomingResponse(byte[] res){
		CstaIncomingResponse=res;
	}
	
	
	public byte[] getCstaOutgoingRequest(){
		return CstaOutgoingRequest;
	}
	public byte[] getCstaIncomingResponse(){
		return CstaIncomingResponse;
	}

	public String getDevice() {
		return device;
	}

	public  void setDevice(String device) {
		CSTAMessageHandler.device = device;
	}
	
	public  String getMonitorCrossRefID() {
		return monitorCrossRefID;
	}

	public void setMonitorCrossRefID(String monitorCrossRefID) {
		CSTAMessageHandler.monitorCrossRefID = monitorCrossRefID;
	}

	public  String getCallID() {
		return callID;
	}

	public  void setCallID(String callID) {
		CSTAMessageHandler.callID = callID;
	}

	public void setCstaMessageOutgoing(CstaMessages mes){
		messageOutgoing=mes;
	}
	public CstaMessages getCstaMessageOutgoing(){
		return messageOutgoing;
	}

	public byte[] getCstaIncomingRequest() {
		return CstaIncomingRequest;
	}

	public void setCstaIncomingRequest(byte[] cstaIncomingRequest) {
		CstaIncomingRequest = cstaIncomingRequest;
	}

	public  byte[] getCstaOutgoingResponse() {
		return CstaOutgoingResponse;
	}

	public  void setCstaOutgoingResponse(byte[] cstaOutgoingResponse) {
		CstaOutgoingResponse = cstaOutgoingResponse;
	}

	public  CstaMessages getCSTAMessageIncoming() {
		return messageIncoming;
	}

	public  void setCstaMessageIncoming(CstaMessages messageIncoming) {
		CSTAMessageHandler.messageIncoming = messageIncoming;
	}
	

}
