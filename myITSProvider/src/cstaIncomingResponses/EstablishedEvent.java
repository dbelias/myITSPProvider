package cstaIncomingResponses;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EstablishedEvent extends AbstractResponse {
	private static Logger logger=Logger.getLogger("EstablishedEvent");
	public EstablishedEvent(Document doc) {
		super(doc);
		//myNodeList = doc.getElementsByTagName(CstaMessages.EstablishedEvent.getTag());
		//myNode=myNodeList.item(0);
		// TODO Auto-generated constructor stub
	}
	
	public String getMonitorCrossRefID(){
		myNodeList = doc.getElementsByTagName("mCRI");
		myNode=myNodeList.item(0);
		String mcRI=myNode.getTextContent();
		logger.info("mCRI is recognised from EstablishedEvent, cID="+mcRI);
		return mcRI;
	}
	
	public String getCause(){
		myNodeList = doc.getElementsByTagName("cs");
		myNode=myNodeList.item(0);
		String cause=myNode.getTextContent();
		logger.info("cause is recognised from EstablishedEvent, cID="+cause);
		return cause;
	}
	public String getCallId(){
		myNodeList = doc.getElementsByTagName("eCn");
		myNode=myNodeList.item(0);
		Element myElement=(Element) myNode;
		String callID=myElement.getElementsByTagName("cID").item(0).getTextContent();
		logger.info("callID is recognised from EstablishedEvent, cID="+callID);
		return callID;
	}
	public String getdeviceID(){
		myNodeList = doc.getElementsByTagName("eCn");
		myNode=myNodeList.item(0);
		Element myElement=(Element) myNode;
		String deviceID=myElement.getElementsByTagName("dID").item(0).getTextContent();
		m=p.matcher(deviceID);
		if (m.find()){
			deviceID=m.group(1);
		}
		logger.info("deviceID is recognised from EstablishedEvent, dID="+deviceID);
		return deviceID;
	}

}
