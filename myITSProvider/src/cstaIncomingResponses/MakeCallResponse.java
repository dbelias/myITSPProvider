package cstaIncomingResponses;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class MakeCallResponse extends AbstractResponse {
	private static Logger logger=Logger.getLogger("MakeCallResponse");
	public MakeCallResponse(Document doc) {
		super(doc);
		
		// TODO Auto-generated constructor stub
	}
	
	public String getCallID(){
		myNodeList = doc.getElementsByTagName("caD");
		myNode=myNodeList.item(0);
		Element myElement=(Element) myNode;
		String callID=myElement.getElementsByTagName("cID").item(0).getTextContent();
		logger.info("CallID from Nake CallResponse recognised ciD="+callID);
		return callID;
	}
	
	public String getdeviceID(){
		myNodeList = doc.getElementsByTagName("caD");
		myNode=myNodeList.item(0);
		Element myElement=(Element) myNode;
		String deviceID=myElement.getElementsByTagName("dID").item(0).getTextContent();
		m=p.matcher(deviceID);
		if (m.find()){
			deviceID=m.group(1);
		}
		return deviceID;
	}

}
