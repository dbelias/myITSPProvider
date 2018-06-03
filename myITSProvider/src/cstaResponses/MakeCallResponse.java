package cstaResponses;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class MakeCallResponse extends AbstractResponse {

	public MakeCallResponse(Document doc) {
		super(doc);
		
		// TODO Auto-generated constructor stub
	}
	
	public String getCallID(){
		myNodeList = doc.getElementsByTagName("caD");
		myNode=myNodeList.item(0);
		Element myElement=(Element) myNode;
		return myElement.getElementsByTagName("cID").item(0).getTextContent();
	}
	
	public String getdeviceID(){
		myNodeList = doc.getElementsByTagName("caD");
		myNode=myNodeList.item(0);
		Element myElement=(Element) myNode;
		return myElement.getElementsByTagName("dID").item(0).getTextContent();
	}

}
