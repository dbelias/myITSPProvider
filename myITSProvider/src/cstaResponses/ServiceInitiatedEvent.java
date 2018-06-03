package cstaResponses;

import org.w3c.dom.Document;

public class ServiceInitiatedEvent extends AbstractResponse {

	public ServiceInitiatedEvent(Document doc) {
		super(doc);
		// TODO Auto-generated constructor stub
	}
	public String getMonitorCrossRefID(){
		myNodeList = doc.getElementsByTagName("mCRI");
		myNode=myNodeList.item(0);
		
		return myNode.getTextContent();
	}
	public String getCause(){
		myNodeList = doc.getElementsByTagName("cs");
		myNode=myNodeList.item(0);
		return myNode.getTextContent();
	}

}
