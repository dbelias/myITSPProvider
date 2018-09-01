package circuit;

public enum CstaMessages {
	RequestSystemStatus ("RequestSystemStatus","RSS",true),
	RequestSystemStatusResponse("RequestSystemStatusResponse","RSSR",false),
	SystemStatus("SystemStatus","SS",false),
	SystemStatusResponse("SystemStatusResponse","SSR",false),
	GetConfigurationData ("GetConfigurationData","GCD",true),
	GetConfigurationDataResponse("GetConfigurationDataResponse","GCDR",false),
	GetLogicalDeviceInformation ("GetLogicalDeviceInformation","GLDI",true),
	GetLogicalDeviceInformationResponse("GetLogicalDeviceInformation","GLDIe",false),
	MonitorStart("MonitorStart","MS",true),
	MonitorStartResponse("MonitorStartResponse","MSR",false),
	MonitorStop("MonitorStop","MSp",true),
	MonitorStopResponse("MonitorStopResponse","MSRe",false),
	SnapshotDevice("SnapshotDevice","SDe",true),
	SnapshotDeviceResponse("SnapshotDeviceResponse","SnDR",false),
	SnapshotCall("SnapshotCall","SC",true),
	SnapshotCallResponse("SnapshotCallResponse","SCR",false),
	DeliveredEvent("DeliveredEvent","DE",false),
	EstablishedEvent("EstablishedEvent","EEt",false),
	ServiceInitiatedEvent("ServiceInitiatedEvent","SIE",false),
	OriginatedEvent("OriginatedEvent","OEt",false),
	AnswerCall("AnswerCall","AnC",true),
	AnswerCallResponse("AnswerCallResponse","AnCR",false),
	MakeCall("MakeCall","MC",true),
	MakeCallResponse("MakeCallResponse","MCR",false),
	ClearConnection("ClearConnection","CCn",true),
	ClearConnectionResponse("ClearConnectionResponse","ClCR",false),
	ConnectionClearedEvent("ConnectionClearedEvent","CCEt",false),
	DeflectCall("DeflectCall","DCl",true),
	ConsultationCall("ConsultationCall","CnC",true),
	ReconnectCall("ReconnectCall","RC",true),
	TransferCall("TransferCall","TC",true),
	SingleStepTransferCall("SingleStepTransferCall","SSTC",true),
	GenerateDigits("GenerateDigits","GD",true),
	GetForwarding("GetForwarding","GF",true),
	GetForwardingResponse("GetForwardingResponse","GFR",false),
	SetForwardingOn("SetForwardingOn","SF",true),
	SetForwardingOff("SetForwardingOff","SF",true),
	SetForwardingResponse("SetForwardingResponse","SFR",false),
	GetDoNotDisturb("GetDND","GDND",true),
	GetDoNotDisturbResponse("GetDoNotDisturbResponse","GDNDe",false),
	SetDoNotDisturbOn("SetDoNotDisturbOn","SDND",true),
	SetDoNotDisturbOff("SetDoNotDisturbOn","SDND",true),
	SetDoNotDisturbResponse("SetDoNotDisturbResponse","SDNDe",false);
	private String description;
	private String tag;
	private boolean isRequest;
	private CstaMessages(String s, String t, boolean r){
		this.description=s;
		this.tag=t;
		this.isRequest=r;
	}
	public String getDescription(){
        return description;
    }
	
	public String getTag(){
		return tag;
	}
	
	public boolean getIsRequest(){
		return isRequest;
	}

}
