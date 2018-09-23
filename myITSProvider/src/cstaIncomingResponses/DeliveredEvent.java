package cstaIncomingResponses;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class DeliveredEvent extends AbstractResponse {
	private static Logger logger=Logger.getLogger("DeliveredEvent");
	public DeliveredEvent(Document doc) {
		super(doc);
		//myNodeList = doc.getElementsByTagName(CstaMessages.DeliveredEvent.getTag());
		//myNode=myNodeList.item(0);
		// TODO Auto-generated constructor stub
	}
	
	public String getMonitorCrossRefID(){
		myNodeList = doc.getElementsByTagName("mCRI");
		myNode=myNodeList.item(0);
		Element myElement=(Element) myNode;
		String monitorCrossRefID=myElement.getTextContent();
		logger.info("MonitorCrossRefID is recognised from DeliveredEvent, mCRI="+monitorCrossRefID);
		return monitorCrossRefID;
	}
	
	public String getCause(){
		myNodeList = doc.getElementsByTagName("cs");
		myNode=myNodeList.item(0);
		Element myElement=(Element) myNode;
		String cause=myElement.getTextContent();
		logger.info("Cause is recognised from DeliveredEvent, cs="+cause);
		return cause;
	}
	public String getCallId(){
		myNodeList = doc.getElementsByTagName("cnncn");
		myNode=myNodeList.item(0);
		Element myElement=(Element) myNode;
		String callID=myElement.getElementsByTagName("cID").item(0).getTextContent();
		logger.info("callID is recognised from DeliveredEvent, cID="+callID);
		return callID;
	}
	public String getdeviceID(){
		myNodeList = doc.getElementsByTagName("cnncn");
		myNode=myNodeList.item(0);
		Element myElement=(Element) myNode;
		String deviceID=myElement.getElementsByTagName("dID").item(0).getTextContent();
		m=p.matcher(deviceID);
		if (m.find()){
			deviceID=m.group(1);
		}
		logger.info("deviceID is recognised from DeliveredEvent, dID="+deviceID);
		return deviceID;
	}
	

}
