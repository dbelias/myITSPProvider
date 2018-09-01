package circuit;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cstaRequests.AbstractRequest;
import cstaRequests.AnswerCall;
import cstaRequests.ClearConnection;
import cstaRequests.ConsultationCall;
import cstaRequests.DeflectCall;
import cstaRequests.GenerateDigits;
import cstaRequests.GetConfigurationData;
import cstaRequests.GetDoNotDistrub;
import cstaRequests.GetForwarding;
import cstaRequests.GetLogicalDeviceInformation;
import cstaRequests.MakeCall;
import cstaRequests.MonitorStart;
import cstaRequests.MonitorStop;
import cstaRequests.ReconnectCall;
import cstaRequests.RequestSystemStatus;
import cstaRequests.SetDoNotDisturb;
import cstaRequests.SetForwarding;
import cstaRequests.SingleStepTransferCall;
import cstaRequests.SnapshotDevice;
import cstaRequests.SystemStatus;
import cstaRequests.TransferCall;
import cstaResponses.AbstractResponse;
import cstaResponses.DeliveredEvent;
import cstaResponses.MakeCallResponse;
import cstaResponses.MonitorStartResponse;

public class CircuitHandler {
	private static Logger logger=Logger.getLogger("CircuitHandler");
	private static String calledDevice;
	private static String circuitDevice2;
	public static List<CircuitGuiUpdates> myCircuitGuiUpdate;
	private static volatile CircuitHandler myCircuitHandler;
	private static volatile CSTAMessageHandler myCstaHandler;
	private static boolean sendOutgoingResponse=false;
	
	public CircuitHandler(){
		logger.info("Call CircuitHandler constructor");
		myCircuitGuiUpdate=new ArrayList<CircuitGuiUpdates>();
		myCstaHandler=CSTAMessageHandler.getInstance();
	}
	
	public static synchronized CircuitHandler getInstance(){
		if (myCircuitHandler==null){
			myCircuitHandler=new CircuitHandler();
		}
		return myCircuitHandler;
	}
	public String getCalledDevice() {
		return calledDevice;
	}
	public  void setCalledDevice(String calledDevice) {
		CircuitHandler.calledDevice = calledDevice;
	}
	public String getCircuitDevice(){
		return circuitDevice2;
	}
	public void setCircuitDevice(String circuitDevice){
		circuitDevice2=circuitDevice;
	}
	public  void buildNotifyContent(){
		boolean isImplemented=false;
		AbstractRequest message=null;
		switch (myCstaHandler.getCstaMessageOutgoing()){
		case GetConfigurationData:
			message=new GetConfigurationData(myCstaHandler.getDevice());
			isImplemented=true;
			break;
		case GetDoNotDisturb:
			message=new GetDoNotDistrub(myCstaHandler.getDevice());
			isImplemented=true;
			break;
		case GetForwarding:
			message=new GetForwarding(myCstaHandler.getDevice());
			isImplemented=true;
			break;
		case GetLogicalDeviceInformation:
			message=new GetLogicalDeviceInformation(myCstaHandler.getDevice());
			isImplemented=true;			
			break;
		case MonitorStart:
			message=new MonitorStart(myCstaHandler.getDevice());
			isImplemented=true;	
			break;
		case MonitorStop:
			message=new MonitorStop(myCstaHandler.getDevice());
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
			message=new SnapshotDevice(myCstaHandler.getDevice());
			isImplemented=true;
			break;
		case MakeCall:
			message=new MakeCall(myCstaHandler.getDevice(),calledDevice);
			isImplemented=true;
			break;
		case AnswerCall:
			message=new AnswerCall(myCstaHandler.getCallID(),myCstaHandler.getDevice());
			isImplemented=true;
			break;
		case ClearConnection:
			message=new ClearConnection(myCstaHandler.getCallID(),myCstaHandler.getDevice());
			isImplemented=true;
			break;
		case SetForwardingOn:
			message=new SetForwarding(myCstaHandler.getDevice(),calledDevice,true);
			isImplemented=true;
			break;
		case SetForwardingOff:
			message=new SetForwarding(myCstaHandler.getDevice(),calledDevice,false);
			isImplemented=true;
			break;
		case SetDoNotDisturbOn:
			message=new SetDoNotDisturb(myCstaHandler.getDevice(),true);
			isImplemented=true;
			break;
		case SetDoNotDisturbOff:
			message=new SetDoNotDisturb(myCstaHandler.getDevice(),false);
			isImplemented=true;
			break;
		case DeflectCall:
			message=new DeflectCall(myCstaHandler.getDevice(),calledDevice);
			isImplemented=true;
			break;
		case ConsultationCall:
			message=new ConsultationCall(myCstaHandler.getDevice(),calledDevice);
			isImplemented=true;
			break;
		case ReconnectCall:
			message=new ReconnectCall(myCstaHandler.getDevice(),calledDevice);
			isImplemented=true;
			break;
		case TransferCall:
			message=new TransferCall(myCstaHandler.getDevice(),calledDevice);
			isImplemented=true;
			break;
		case SingleStepTransferCall:
			message=new SingleStepTransferCall(myCstaHandler.getDevice(),calledDevice);
			isImplemented=true;
			break;
		case GenerateDigits:
			message=new GenerateDigits(myCstaHandler.getDevice(),calledDevice);
			isImplemented=true;
			break;
		default:
				
			
		}
		if (isImplemented){
			logger.trace("NOTIFY message:"+myCstaHandler.getCstaMessageOutgoing().getDescription());
			myCstaHandler.setOutgoingRequest(message.getBytes());
		}else {
			logger.warn("NOTIFY message:"+myCstaHandler.getCstaMessageOutgoing().getDescription()+" is not implemented to be sent as Request");
		}
		
	}
	
	public void analyzeNotifyRequestContent(){
		byte[] data=myCstaHandler.getCstaIncomingRequest();
		XmlDecoder myXmlDecoder=new XmlDecoder(data);
		CstaMessages myCstaMessage=myXmlDecoder.getCstaMessage();
		switch(myCstaMessage){
		case DeliveredEvent:
			DeliveredEvent de= (DeliveredEvent) myXmlDecoder.myObject;
			myCstaHandler.setCallID(de.getCallId());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.CallId);
			break;
		case SystemStatus:
			RequestSystemStatus myRSS=new RequestSystemStatus();
			myCstaHandler.setCstaOutgoingResponse(myRSS.getBytes());
			sendOutgoingResponse=true;
		default:
			break;	
		}
		
		
		//TODO:further actions to analyze the data (e.g:save callRefId)
	}
	
	public void analyzeNotifyResponseContent(){
		byte[] data=myCstaHandler.getCstaOutgoingResponse();
		XmlDecoder myXmlDecoder=new XmlDecoder(data);
		CstaMessages myCstaMessage=myXmlDecoder.getCstaMessage();
		switch(myCstaMessage){
		case MonitorStartResponse:
			MonitorStartResponse msr=(MonitorStartResponse) myXmlDecoder.myObject;
			myCstaHandler.setMonitorCrossRefID(msr.getMonitorCrossRefID());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.MonitorCrossRefId);
			break;
		case MakeCallResponse:
			MakeCallResponse mcr=(MakeCallResponse) myXmlDecoder.myObject;
			myCstaHandler.setCallID(mcr.getCallID());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.CallId);
			break;
		default:
			break;	
		}
		
	}
	
	public String getMonitorCrossRefId(){
		return myCstaHandler.getMonitorCrossRefID();
	}
	
	public String getCallId(){
		return myCstaHandler.getCallID();
	}
	
	public boolean getSendOutgoingResponse(){
		return sendOutgoingResponse;
	}
	public void setSendOutgoingResponse(){
		sendOutgoingResponse=true;
	}
	public void resetSendOutgoingResponse(){
		sendOutgoingResponse=false;
	}

}
