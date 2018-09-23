package cstaIncomingResponses;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import circuit.CstaMessages;

public class MonitorStartResponse extends AbstractResponse {
	private static Logger logger=Logger.getLogger("MonitorStartResponse");
	public MonitorStartResponse(Document doc) {
		super(doc);
		// TODO Auto-generated constructor stub
	}
	
	public String getMonitorCrossRefID(){
		myNodeList = doc.getElementsByTagName("mCRI");
		myNode=myNodeList.item(0);
		logger.info("MonitorCrossRefID is recognized mCRI="+myNode.getTextContent());
		return myNode.getTextContent();
	}

}
