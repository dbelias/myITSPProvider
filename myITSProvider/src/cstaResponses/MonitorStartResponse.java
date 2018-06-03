package cstaResponses;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import circuit.CstaMessages;

public class MonitorStartResponse extends AbstractResponse {

	public MonitorStartResponse(Document doc) {
		super(doc);
		// TODO Auto-generated constructor stub
	}
	
	public String getMonitorCrossRefID(){
		myNodeList = doc.getElementsByTagName("mCRI");
		myNode=myNodeList.item(0);
		return myNode.getTextContent();
	}

}
