package circuit;

public enum CstaMessages {
	RequestSystemStatus ("RequestSystemStatus","RSS"),
	RequestSystemStatusResponse("RequestSystemStatusResponse","RSSR"),
	SystemStatus("SystemStatus","SS"),
	SystemStatusResponse("SystemStatusResponse","SSR"),
	GetConfigurationData ("GetConfigurationData","GCD"),
	GetConfigurationDataResponse("GetConfigurationDataResponse","GCDR"),
	GetLogicalDeviceInformation ("GetLogicalDeviceInformation","GLDI"),
	GetLogicalDeviceInformationResponse("GetLogicalDeviceInformation","GLDIe"),
	MonitorStart("MonitorStart","MS"),
	MonitorStartResponse("MonitorStartResponse","MSR"),
	MonitorStop("MonitorStop","MSp"),
	MonitorStopResponse("MonitorStopResponse","MSRe"),
	SnapshotDevice("SnapshotDevice","SDe"),
	SnapshotDeviceResponse("SnapshotDeviceResponse","SnDR"),
	SnapshotCall("SnapshotCall","SC"),
	SnapshotCallResponse("SnapshotCallResponse","SCR"),
	DeliveredEvent("DeliveredEvent","DE"),
	EstablishedEvent("EstablishedEvent","EEt"),
	ServiceInitiatedEvent("ServiceInitiatedEvent","SIE"),
	OriginatedEvent("OriginatedEvent","OEt"),
	AnswerCall("AnswerCall","AnC"),
	AnswerCallResponse("AnswerCallResponse","AnCR"),
	MakeCall("MakeCall","MC"),
	MakeCallResponse("MakeCallResponse","MCR"),
	ClearConnection("ClearConnection","CCn"),
	ClearConnectionResponse("ClearConnectionResponse","ClCR"),
	ConnectionClearedEvent("ConnectionClearedEvent","CCEt"),
	GetForwarding("GetForwarding","GF"),
	GetForwardingResponse("GetForwardingResponse","GFR"),
	SetForwarding("SetForwarding","SF"),
	SetForwardingResponse("SetForwardingResponse","SFR"),
	GetDoNotDisturb("GetDND","GDND"),
	GetDoNotDisturbResponse("GetDoNotDisturbResponse","GDNDe"),
	SetDoNotDisturb("SetDoNotDisturb","SDND"),
	SetDoNotDisturbResponse("SetDoNotDisturbResponse","SDNDe");
	private String description;
	private String tag;
	private CstaMessages(String s, String t){
		this.description=s;
		this.tag=t;
	}
	public String getDescription(){
        return description;
    }
	
	public String getTag(){
		return tag;
	}

}
