package circuit;

public class CSTAMessageHandler {
	private static  byte[] CstaOutgoingRequest="Any String you want".getBytes();
	private static  byte[] CstaIncomingResponse="Any String you want".getBytes();
	private static  byte[] CstaIncomingRequest="Any String you want".getBytes();
	private static  byte[] CstaOutgoingResponse="Any String you want".getBytes();
	private static String device;
	private static String monitorCrossRefID;
	private static String callID;
	private static CstaMessages messageOutgoing;
	private static CstaMessages messageIncoming;
	
	
	public CSTAMessageHandler(){
		
		
	}
	
	public static void setOutgoingRequest(byte[] req){
		CstaOutgoingRequest=req;
	}
	public static void setIncomingResponse(byte[] res){
		CstaIncomingResponse=res;
	}
	
	
	public static byte[] getCstaOutgoingRequest(){
		return CstaOutgoingRequest;
	}
	public static byte[] getCstaIncomingResponse(){
		return CstaIncomingResponse;
	}

	public static String getDevice() {
		return device;
	}

	public static void setDevice(String device) {
		CSTAMessageHandler.device = device;
	}
	
	public static String getMonitorCrossRefID() {
		return monitorCrossRefID;
	}

	public static void setMonitorCrossRefID(String monitorCrossRefID) {
		CSTAMessageHandler.monitorCrossRefID = monitorCrossRefID;
	}

	public static String getCallID() {
		return callID;
	}

	public static void setCallID(String callID) {
		CSTAMessageHandler.callID = callID;
	}

	public static void setCstaMessageOutgoing(CstaMessages mes){
		messageOutgoing=mes;
	}
	public static CstaMessages getCstaMessageOutgoing(){
		return messageOutgoing;
	}

	public static byte[] getCstaIncomingRequest() {
		return CstaIncomingRequest;
	}

	public static void setCstaIncomingRequest(byte[] cstaIncomingRequest) {
		CstaIncomingRequest = cstaIncomingRequest;
	}

	public static byte[] getCstaOutgoingResponse() {
		return CstaOutgoingResponse;
	}

	public static void setCstaOutgoingResponse(byte[] cstaOutgoingResponse) {
		CstaOutgoingResponse = cstaOutgoingResponse;
	}

	public static CstaMessages getCSTAMessageIncoming() {
		return messageIncoming;
	}

	public static void setCstaMessageIncoming(CstaMessages messageIncoming) {
		CSTAMessageHandler.messageIncoming = messageIncoming;
	}
	

}
