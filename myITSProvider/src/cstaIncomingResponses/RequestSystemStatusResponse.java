package cstaIncomingResponses;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import circuit.CstaMessages;

public class RequestSystemStatusResponse extends AbstractResponse {

	public RequestSystemStatusResponse(Document doc) {
		super(doc);
		myNodeList = doc.getElementsByTagName("ssS");
		myNode=myNodeList.item(0);
		// TODO Auto-generated constructor stub
	}
	
	public String getSystemStatus(){
		return myNode.getTextContent();
	}

}
