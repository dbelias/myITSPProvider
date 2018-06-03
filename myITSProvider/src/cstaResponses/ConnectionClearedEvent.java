package cstaResponses;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import circuit.CstaMessages;

public class ConnectionClearedEvent extends AbstractResponse {

	public ConnectionClearedEvent(Document doc) {
		super(doc);
		//myNodeList = doc.getElementsByTagName(CstaMessages.ConnectionClearedEvent.getTag());
		//myNode=myNodeList.item(0);
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
