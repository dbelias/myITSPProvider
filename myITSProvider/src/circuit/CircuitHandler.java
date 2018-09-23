package circuit;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cstaIncomingResponses.AbstractResponse;
import cstaIncomingResponses.ConnectionClearedEvent;
import cstaIncomingResponses.DeliveredEvent;
import cstaIncomingResponses.EstablishedEvent;
import cstaIncomingResponses.HeldEvent;
import cstaIncomingResponses.MakeCallResponse;
import cstaIncomingResponses.MonitorStartResponse;
import cstaIncomingResponses.OriginatedEvent;
import cstaIncomingResponses.RetrievedEvent;
import cstaIncomingResponses.ServiceInitiatedEvent;
import cstaOutgoingResponses.SystemStatusResponse;
import cstaRequests.AbstractRequest;
import cstaRequests.AlternateCall;
import cstaRequests.AnswerCall;
import cstaRequests.ClearConnection;
import cstaRequests.ConsultationCall;
import cstaRequests.DeflectCall;
import cstaRequests.GenerateDigits;
import cstaRequests.GetConfigurationData;
import cstaRequests.GetDoNotDistrub;
import cstaRequests.GetForwarding;
import cstaRequests.GetLogicalDeviceInformation;
import cstaRequests.HoldCall;
import cstaRequests.MakeCall;
import cstaRequests.MonitorStart;
import cstaRequests.MonitorStop;
import cstaRequests.ReconnectCall;
import cstaRequests.RequestSystemStatus;
import cstaRequests.RetreiveCall;
import cstaRequests.SetDoNotDisturb;
import cstaRequests.SetForwarding;
import cstaRequests.SingleStepTransferCall;
import cstaRequests.SnapshotCall;
import cstaRequests.SnapshotDevice;
import cstaRequests.SystemStatus;
import cstaRequests.TransferCall;
import cstaRequests.MyCstaRequest;
import cstaRequests.SetBusy;
import cstaRequests.GetBusy;

public class CircuitHandler {
	private static Logger logger=Logger.getLogger("CircuitHandler");
	private static String device;
	private static String parameter1;
	private static String parameter2;
	private static String freeParameter;
	

	public static List<CircuitGuiUpdates> myCircuitGuiUpdate;
	private static volatile CircuitHandler myCircuitHandler;
	private static volatile CSTAMessageHandler myCstaHandler;
	private static boolean sendOutgoingResponseWithXml=false;
	
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
		return parameter1;
	}
	public  void setCalledDevice(String calledDevice) {
		CircuitHandler.parameter1 = calledDevice;
	}
	public String getCircuitDevice(){
		return parameter2;
	}
	
	public void setCircuitDevice(String circuitDevice){
		parameter2=circuitDevice;
	}
	public String getParameter1() {
		return parameter1;
	}

	public void setParameter1(String paramter1) {
		CircuitHandler.parameter1 = paramter1;
	}

	public String getParameter2() {
		return parameter2;
	}

	public void setParameter2(String parameter2) {
		CircuitHandler.parameter2 = parameter2;
	}

	public AbstractRequest buildCSTAMessage(){
		AbstractRequest message;
		message=null;
		switch (myCstaHandler.getCstaMessageOutgoing()){
		case GetConfigurationData:
			message=new GetConfigurationData(myCstaHandler.getDevice());
			break;
		case GetDoNotDisturb:
			message=new GetDoNotDistrub(myCstaHandler.getDevice());
			break;
		case GetForwarding:
			message=new GetForwarding(myCstaHandler.getDevice());
			break;
		case GetLogicalDeviceInformation:
			message=new GetLogicalDeviceInformation(myCstaHandler.getDevice());		
			break;
		case MonitorStart:
			message=new MonitorStart(myCstaHandler.getDevice());	
			break;
		case MonitorStop:
			message=new MonitorStop(parameter1);
			break;
		case RequestSystemStatus:
			message=new RequestSystemStatus();
			break;
		case SystemStatus:
			message=new SystemStatus();
			break;
		case SnapshotDevice:
			message=new SnapshotDevice(myCstaHandler.getDevice());
			break;
		case MakeCall:
			message=new MakeCall(myCstaHandler.getDevice(),parameter1);
			break;
		case AnswerCall:
			message=new AnswerCall(myCstaHandler.getCallID(),myCstaHandler.getDevice());
			break;
		case ClearConnection:
			message=new ClearConnection(myCstaHandler.getCallID(),myCstaHandler.getDevice());
			break;
		case SetForwardingOn:
			message=new SetForwarding(myCstaHandler.getDevice(),parameter1,true);
			break;
		case SetForwardingOff:
			message=new SetForwarding(myCstaHandler.getDevice(),parameter1,false);
			break;
		case SetDoNotDisturbOn:
			message=new SetDoNotDisturb(myCstaHandler.getDevice(),true);
			break;
		case SetDoNotDisturbOff:
			message=new SetDoNotDisturb(myCstaHandler.getDevice(),false);
			break;
		case DeflectCall:
			message=new DeflectCall(myCstaHandler.getDevice(),parameter1,myCstaHandler.getCallID());
			break;
		case ConsultationCall:
			message=new ConsultationCall(myCstaHandler.getDevice(),parameter1,myCstaHandler.getCallID());
			break;
		case ReconnectCall:
			message=new ReconnectCall(myCstaHandler.getDevice(),parameter1,myCstaHandler.getCallID(),myCstaHandler.getCallIDSecondary());
			break;
		case TransferCall:
			message=new TransferCall(myCstaHandler.getDevice(),parameter1,myCstaHandler.getCallID(),myCstaHandler.getCallIDSecondary());
			break;
		case SingleStepTransferCall:
			message=new SingleStepTransferCall(myCstaHandler.getDevice(),parameter1,myCstaHandler.getCallID());
			break;
		case AlternateCall:
			message=new AlternateCall(myCstaHandler.getDevice(),myCstaHandler.getCallID(),myCstaHandler.getCallIDSecondary());
			break;
		case HoldCall:
			message=new HoldCall(myCstaHandler.getDevice(),myCstaHandler.getCallID());
			break;
		case RetreiveCall:
			message=new RetreiveCall(myCstaHandler.getDevice(),myCstaHandler.getCallID());
			break;	
		case GenerateDigits:
			message=new GenerateDigits(myCstaHandler.getDevice(),parameter1,myCstaHandler.getCallID());
			break;
		case SnapshotCall:
			message=new SnapshotCall(myCstaHandler.getCallID(),myCstaHandler.getDevice());
			break;
		case MyCSTARequest:
			message=new MyCstaRequest(myCstaHandler.getMyCstaXmlMessage());
			break;
		case SetBusyOn:
			message=new SetBusy(myCstaHandler.getDevice(),true);
			break;
		case SetBusyOff:
			message=new SetBusy(myCstaHandler.getDevice(),false);
			break;
		case GetBusy:
			message=new GetBusy(myCstaHandler.getDevice());
			break;
		default:
				
			
		}
		return message;
		
	}
	public  void buildNotifyContent(){
		AbstractRequest message=buildCSTAMessage();
		if (message!=null){
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
		case HeldEvent:
			HeldEvent he= (HeldEvent) myXmlDecoder.returnObject();
			logger.info("analyzeNotifyRequestContent: HeldEvent : Look for deviceID");
			myCstaHandler.setDevice(he.getdeviceID());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.DeviceID);
			logger.info("analyzeNotifyRequestContent: HeldEvent : Look for mCRId");
			myCstaHandler.setMonitorCrossRefID(he.getMonitorCrossRefID());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.MonitorCrossRefId);
			logger.info("analyzeNotifyRequestContent: HeldEvent : Look for CallID");
			myCstaHandler.setCallID(he.getCallId());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.CallId);
			logger.info("analyzeNotifyRequestContent: HeldEvent : Look for cause");
			freeParameter=he.getCause();
			myCircuitGuiUpdate.add(CircuitGuiUpdates.Cause);
			break;
		case RetrievedEvent:
			RetrievedEvent re= (RetrievedEvent) myXmlDecoder.returnObject();
			logger.info("analyzeNotifyRequestContent: RetrievedEvent : Look for deviceID");
			myCstaHandler.setDevice(re.getdeviceID());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.DeviceID);
			logger.info("analyzeNotifyRequestContent: RetrievedEvent : Look for mCRId");
			myCstaHandler.setMonitorCrossRefID(re.getMonitorCrossRefID());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.MonitorCrossRefId);
			logger.info("analyzeNotifyRequestContent: RetrievedEvent : Look for CallID");
			myCstaHandler.setCallID(re.getCallId());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.CallId);
			logger.info("analyzeNotifyRequestContent: RetrievedEvent : Look for cause");
			freeParameter=re.getCause();
			myCircuitGuiUpdate.add(CircuitGuiUpdates.Cause);
			break;
		case ConnectionClearedEvent:
			ConnectionClearedEvent cce= (ConnectionClearedEvent) myXmlDecoder.returnObject();
			logger.info("analyzeNotifyRequestContent: ConnectionClearedEvent : Look for deviceID");
			myCstaHandler.setDevice(cce.getdeviceID());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.DeviceID);
			logger.info("analyzeNotifyRequestContent: ConnectionClearedEvent : Look for mCRId");
			myCstaHandler.setMonitorCrossRefID(cce.getMonitorCrossRefID());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.MonitorCrossRefId);
			logger.info("analyzeNotifyRequestContent: ConnectionClearedEvent : Look for CallID");
			myCstaHandler.setCallID(cce.getCallId());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.CallId);
			logger.info("analyzeNotifyRequestContent: ConnectionClearedEvent : Look for cause");
			freeParameter=cce.getCause();
			myCircuitGuiUpdate.add(CircuitGuiUpdates.Cause);
			break;
		case ServiceInitiatedEvent:
			ServiceInitiatedEvent se= (ServiceInitiatedEvent) myXmlDecoder.returnObject();
			logger.info("analyzeNotifyRequestContent: ServiceInitiatedEvent : Look for deviceID");
			myCstaHandler.setDevice(se.getdeviceID());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.DeviceID);
			logger.info("analyzeNotifyRequestContent: ServiceInitiatedEvent : Look for mCRId");
			myCstaHandler.setMonitorCrossRefID(se.getMonitorCrossRefID());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.MonitorCrossRefId);
			logger.info("analyzeNotifyRequestContent: ServiceInitiatedEvent : Look for CallID");
			myCstaHandler.setCallID(se.getCallId());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.CallId);
			logger.info("analyzeNotifyRequestContent: ServiceInitiatedEvent : Look for cause");
			freeParameter=se.getCause();
			myCircuitGuiUpdate.add(CircuitGuiUpdates.Cause);
			break;
		case DeliveredEvent:
			DeliveredEvent de= (DeliveredEvent) myXmlDecoder.returnObject();
			logger.info("analyzeNotifyRequestContent: DeliverEvent : Look for deviceID");
			myCstaHandler.setDevice(de.getdeviceID());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.DeviceID);
			logger.info("analyzeNotifyRequestContent: DeliverEvent : Look for mCRId");
			myCstaHandler.setMonitorCrossRefID(de.getMonitorCrossRefID());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.MonitorCrossRefId);
			logger.info("analyzeNotifyRequestContent: DeliverEvent : Look for CallID");
			myCstaHandler.setCallID(de.getCallId());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.CallId);
			logger.info("analyzeNotifyRequestContent: DeliverEvent : Look for cause");
			freeParameter=de.getCause();
			myCircuitGuiUpdate.add(CircuitGuiUpdates.Cause);
			break;
		case EstablishedEvent:
			EstablishedEvent ee=(EstablishedEvent) myXmlDecoder.returnObject();
			logger.info("analyzeNotifyRequestContent: EstablishedEvent : Look for deviceID");
			myCstaHandler.setDevice(ee.getdeviceID());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.DeviceID);
			logger.info("analyzeNotifyRequestContent: EstablishedEvent : Look for mCRId");
			myCstaHandler.setMonitorCrossRefID(ee.getMonitorCrossRefID());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.MonitorCrossRefId);
			logger.info("analyzeNotifyRequestContent: EstablishedEvent : Look for CallID");
			myCstaHandler.setCallID(ee.getCallId());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.CallId);
			logger.info("analyzeNotifyRequestContent: EstablishedEvent : Look for cause");
			freeParameter=ee.getCause();
			myCircuitGuiUpdate.add(CircuitGuiUpdates.Cause);
		case OriginatedEvent:
			OriginatedEvent oe=(OriginatedEvent) myXmlDecoder.returnObject();
			logger.info("analyzeNotifyRequestContent: OriginatedEvent : Look for mCRId");
			myCstaHandler.setMonitorCrossRefID(oe.getMonitorCrossRefID());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.MonitorCrossRefId);
			logger.info("analyzeNotifyRequestContent: OriginatedEvent : Look for cause");
			freeParameter=oe.getCause();
			myCircuitGuiUpdate.add(CircuitGuiUpdates.Cause);
		case SystemStatus:
			//RequestSystemStatus myRSS=new RequestSystemStatus();
			SystemStatusResponse mySSR=new SystemStatusResponse();
			myCstaHandler.setCstaOutgoingResponse(mySSR.getBytes());
			sendOutgoingResponseWithXml=true;
			logger.info("analyzeNotifyRequestContent: SystemStatus : Build xml response with SystemStatusResponse");
		case Unknown:
		default:
			logger.warn("analyzeNotifyRequestContent: CSTA message:"+myCstaMessage.getDescription()+" is not further processed. Mind to enahnce the analyzeNotifyRequestContent method");
			break;	
		}
		
		
		//TODO:further actions to analyze the data (e.g:save callRefId)
	}
	
	public void analyzeNotifyResponseContent(){
		//byte[] data=myCstaHandler.getCstaOutgoingResponse();
		byte[] data=myCstaHandler.getCstaIncomingResponse();
		XmlDecoder myXmlDecoder=new XmlDecoder(data);
		CstaMessages myCstaMessage=myXmlDecoder.getCstaMessage();
		switch(myCstaMessage){
		case MonitorStartResponse:
			MonitorStartResponse msr=(MonitorStartResponse) myXmlDecoder.returnObject();
			logger.info("analyzeNotifyResponseContent: MonitorStartResponse : Look for mCRId");
			myCstaHandler.setMonitorCrossRefID(msr.getMonitorCrossRefID());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.MonitorCrossRefId);
			break;
		case MakeCallResponse:
			MakeCallResponse mcr=(MakeCallResponse) myXmlDecoder.returnObject();
			logger.info("analyzeNotifyResponseContent: MakeCallResponse : Look for CallID");
			myCstaHandler.setCallID(mcr.getCallID());
			myCircuitGuiUpdate.add(CircuitGuiUpdates.CallId);
			break;
		case Unknown:
		default:
			logger.warn("analyzeNotifyResponseContent- CSTA Message:"+myCstaMessage.getDescription()+" is not further processed. Mind to enahnce the analyzeNotifyResponseContent method");
			break;	
		}
		
	}
	
	public String getMonitorCrossRefId(){
		return myCstaHandler.getMonitorCrossRefID();
	}
	
	public String getCallId(){
		return myCstaHandler.getCallID();
	}
	
	public String getDeviceId()
	{
		return myCstaHandler.getDevice();
	}
	
	public String getFreeParamter(){
		return freeParameter
;	}
	public boolean getSendOutgoingResponseWithXml(){
		return sendOutgoingResponseWithXml;
	}
	public void setSendOutgoingResponseWithXml(){
		sendOutgoingResponseWithXml=true;
	}
	public void resetSendOutgoingResponseWithXml(){
		sendOutgoingResponseWithXml=false;
	}

}
