package circuit;

public enum CstaMessages {
	Unknown("Unknown","Unknown",false),
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
	MonitorStop("MonitorStop","MSp",true,"mCRI"),
	MonitorStopResponse("MonitorStopResponse","MSRe",false),
	SnapshotDevice("SnapshotDevice","SDe",true),
	SnapshotDeviceResponse("SnapshotDeviceResponse","SnDR",false),
	SnapshotCall("SnapshotCall","SC",true),
	SnapshotCallResponse("SnapshotCallResponse","SCR",false),
	DeliveredEvent("DeliveredEvent","DE",false),
	EstablishedEvent("EstablishedEvent","EEt",false),
	ServiceInitiatedEvent("ServiceInitiatedEvent","SIE",false),
	OriginatedEvent("OriginatedEvent","OEt",false),
	HeldEvent("HeldEvent","HE",false),
	RetrievedEvent("RetrievedEvent","ReE",false),	
	FailedEvent("FailedEvent","FE",false),
	AnswerCall("AnswerCall","AnC",true),
	AnswerCallResponse("AnswerCallResponse","AnCR",false),
	MakeCall("MakeCall","MC",true,"cDN"),
	MakeCallResponse("MakeCallResponse","MCR",false),
	ClearConnection("ClearConnection","CCn",true),
	ClearConnectionResponse("ClearConnectionResponse","ClCR",false),
	ConnectionClearedEvent("ConnectionClearedEvent","CCEt",false),
	DeflectCall("DeflectCall","DCl",true,"nD"),
	ConsultationCall("ConsultationCall","CnC",true,"cnD"),
	ReconnectCall("ReconnectCall","RC",true,"dID"),
	TransferCall("TransferCall","TC",true,"dID"),
	SingleStepTransferCall("SingleStepTransferCall","SSTC",true,"tTo"),
	AlternateCall("AlternateCall","ACl",true),
	HoldCall("HoldCall","HC",true),
	RetreiveCall("RetreiveCall","RCl",true),
	GenerateDigits("GenerateDigits","GD",true,"cTS"),
	GetForwarding("GetForwarding","GF",true),
	GetForwardingResponse("GetForwardingResponse","GFR",false),
	SetForwardingOn("SetForwardingOn","SF",true,"fDNN"),
	SetForwardingOff("SetForwardingOff","SF",true,"fDNN"),
	SetForwardingResponse("SetForwardingResponse","SFR",false),
	GetDoNotDisturb("GetDND","GDND",true),
	GetDoNotDisturbResponse("GetDoNotDisturbResponse","GDNDe",false),
	SetDoNotDisturbOn("SetDoNotDisturbOn","SDND",true),
	SetDoNotDisturbOff("SetDoNotDisturbOn","SDND",true),
	SetBusyOn("SetBusyOn","zSBN",true),
	SetBusyOff("SetBusyOff","zSBN",true),
	GetBusy("GetBusy","zGBN",true),
	SetDoNotDisturbResponse("SetDoNotDisturbResponse","SDNDe",false),
	MyCSTARequest("MyyCSTARequest","",true);
	private String description;
	private String tag;
	private boolean isRequest;
	private String parameter1Tag;
	private String parameter2Tag;
	private CstaMessages(String s, String t, boolean r){
		this.description=s;
		this.tag=t;
		this.isRequest=r;
	}
	private CstaMessages(String s, String t, boolean r, String p){
		this.description=s;
		this.tag=t;
		this.isRequest=r;
		this.parameter1Tag=p;
	}
	private CstaMessages(String s, String t, boolean r, String p1, String p2){
		this.description=s;
		this.tag=t;
		this.isRequest=r;
		this.parameter1Tag=p1;
		this.parameter2Tag=p2;
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
	
	public String getParameter1Tag(){
		return parameter1Tag;
	}
	public String getParameter2Tag(){
		return parameter2Tag;
	}

}
