package cstaIncomingResponses;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ServiceInitiatedEvent extends AbstractResponse {
	private static Logger logger=Logger.getLogger("ServiceInitiatedEvent");
	public ServiceInitiatedEvent(Document doc) {
		super(doc);
		// TODO Auto-generated constructor stub
	}
	public String getMonitorCrossRefID(){
		myNodeList = doc.getElementsByTagName("mCRI");
		myNode=myNodeList.item(0);
		String mcRI=myNode.getTextContent();
		logger.info("mCRI is recognised from ServiceInitiatedEvent, cID="+mcRI);
		return mcRI;
	}
	public String getCause(){
		myNodeList = doc.getElementsByTagName("cs");
		myNode=myNodeList.item(0);
		String cause=myNode.getTextContent();
		logger.info("cause is recognised from ServiceInitiatedEvent, cID="+cause);
		return cause;
	}
	public String getCallId(){
		myNodeList = doc.getElementsByTagName("iCn");
		myNode=myNodeList.item(0);
		Element myElement=(Element) myNode;
		String callID=myElement.getElementsByTagName("cID").item(0).getTextContent();
		logger.info("callID is recognised from ServiceInitiatedEvent, cID="+callID);
		return callID;
	}
	public String getdeviceID(){
		myNodeList = doc.getElementsByTagName("iCn");
		myNode=myNodeList.item(0);
		Element myElement=(Element) myNode;
		String deviceID=myElement.getElementsByTagName("dID").item(0).getTextContent();
		m=p.matcher(deviceID);
		if (m.find()){
			deviceID=m.group(1);
		}
		logger.info("deviceID is recognised from ServiceInitiatedEvent, dID="+deviceID);
		return deviceID;
	}

}
