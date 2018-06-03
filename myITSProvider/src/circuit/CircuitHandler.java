package circuit;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cstaRequests.AbstractRequest;
import cstaRequests.AnswerCall;
import cstaRequests.ClearConnection;
import cstaRequests.GetConfigurationData;
import cstaRequests.GetDoNotDistrub;
import cstaRequests.GetForwarding;
import cstaRequests.GetLogicalDeviceInformation;
import cstaRequests.MakeCall;
import cstaRequests.MonitorStart;
import cstaRequests.MonitorStop;
import cstaRequests.RequestSystemStatus;
import cstaRequests.SnapshotDevice;
import cstaRequests.SystemStatus;
import cstaResponses.AbstractResponse;
import cstaResponses.DeliveredEvent;
import cstaResponses.MakeCallResponse;
import cstaResponses.MonitorStartResponse;

public class CircuitHandler {
	private static Logger logger=Logger.getLogger("CircuitHandler");
	private static String calledDevice;
	public static List<CircuitGuiUpdates> myCircuitGuiUpdate;
	
	public CircuitHandler(){
		myCircuitGuiUpdate=new ArrayList<CircuitGuiUpdates>();
	}
	public static String getCalledDevice() {
		return calledDevice;
	}
	public static void setCalledDevice(String calledDevice) {
		CircuitHandler.calledDevice = calledDevice;
	}
	public static void buildNotifyContent(){
		boolean isImplemented=false;
		AbstractRequest message=null;
		switch (CSTAMessageHandler.getCstaMessageOutgoing()){
		case GetConfigurationData:
			message=new GetConfigurationData(CSTAMessageHandler.getDevice());
			isImplemented=true;
			break;
		case GetDoNotDisturb:
			message=new GetDoNotDistrub(CSTAMessageHandler.getDevice());
			isImplemented=true;
			break;
		case GetForwarding:
			message=new GetForwarding(CSTAMessageHandler.getDevice());
			break;
		case GetLogicalDeviceInformation:
			message=new GetLogicalDeviceInformation(CSTAMessageHandler.getDevice());
			isImplemented=true;			
			break;
		case MonitorStart:
			message=new MonitorStart(CSTAMessageHandler.getDevice());
			isImplemented=true;	
			break;
		case MonitorStop:
			message=new MonitorStop(CSTAMessageHandler.getDevice());
			isImplemented=true;
			break;
		case RequestSystemStatus:
			message=new RequestSystemStatus();
			isImplemented=true;
			break;
		case SystemStatus:
			message=new SystemStatus();
			isImplemented=true;
			break;
		case SnapshotDevice:
			message=new SnapshotDevice(CSTAMessageHandler.getDevice());
			isImplemented=true;
			break;
		case MakeCall:
			message=new MakeCall(CSTAMessageHandler.getDevice(),calledDevice);
			isImplemented=true;
			break;
		case AnswerCall:
			message=new AnswerCall(CSTAMessageHandler.getCallID(),CSTAMessageHandler.getDevice());
			isImplemented=true;
			break;
		case ClearConnection:
			message=new ClearConnection(CSTAMessageHandler.getCallID(),CSTAMessageHandler.getDevice());
			isImplemented=true;
			break;
		default:
				
			
		}
		if (isImplemented){
			CSTAMessageHandler.setOutgoingRequest(message.getBytes());
		}
		
	}
	
	public static void analyzeNotifyRequestContent(){
		byte[] data=CSTAMessageHandler.getCstaIncomingRequest();
		XmlDecoder myXmlDecoder=new XmlDecoder(data);
		CstaMessages myCstaMessage=myXmlDecoder.getCstaMessage();
		switch(myCstaMessage){
		case DeliveredEvent:
			DeliveredEvent de= (DeliveredEvent) myXmlDecoder.myObject;
			CSTAMessageHandler.setCallID(de.getCallId());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.CallId);
		default:
			break;	
		}
		
		
		//TODO:further actions to analyze the data (e.g:save callRefId)
	}
	
	public static void analyzeNotifyResponseContent(){
		byte[] data=CSTAMessageHandler.getCstaOutgoingResponse();
		XmlDecoder myXmlDecoder=new XmlDecoder(data);
		CstaMessages myCstaMessage=myXmlDecoder.getCstaMessage();
		switch(myCstaMessage){
		case MonitorStartResponse:
			MonitorStartResponse msr=(MonitorStartResponse) myXmlDecoder.myObject;
			CSTAMessageHandler.setMonitorCrossRefID(msr.getMonitorCrossRefID());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.MonitorCrossRefId);
			break;
		case MakeCallResponse:
			MakeCallResponse mcr=(MakeCallResponse) myXmlDecoder.myObject;
			CSTAMessageHandler.setCallID(mcr.getCallID());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.CallId);
			break;
		default:
			break;	
		}
		
	}
	
	public static String getMonitorCrossRefId(){
		return CSTAMessageHandler.getMonitorCrossRefID();
	}
	
	public static String getCallId(){
		return CSTAMessageHandler.getCallID();
	}

}
