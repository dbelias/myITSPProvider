package cstaResponses;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class DeliveredEvent extends AbstractResponse {

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
		return myElement.getElementsByTagName("mCRI").item(0).getTextContent();
	}
	
	public String getCause(){
		myNodeList = doc.getElementsByTagName("mCRI");
		myNode=myNodeList.item(0);
		Element myElement=(Element) myNode;
		return myElement.getElementsByTagName("cs").item(0).getTextContent();
	}
	public String getCallId(){
		myNodeList = doc.getElementsByTagName("cnncn");
		myNode=myNodeList.item(0);
		Element myElement=(Element) myNode;
		return myElement.getElementsByTagName("cID").item(0).getTextContent();
	}
	public String getdeviceID(){
		myNodeList = doc.getElementsByTagName("cnncn");
		myNode=myNodeList.item(0);
		Element myElement=(Element) myNode;
		return myElement.getElementsByTagName("dID").item(0).getTextContent();
	}
	

}
