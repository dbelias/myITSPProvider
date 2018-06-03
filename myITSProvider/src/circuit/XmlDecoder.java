package circuit;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.xml.sax.SAXException;

import cstaResponses.ConnectionClearedEvent;
import cstaResponses.DeliveredEvent;
import cstaResponses.EstablishedEvent;
import cstaResponses.GetDoNotDisturbResponse;
import cstaResponses.GetForwardingResponse;
import cstaResponses.GetLogicalDeviceInformationResponse;
import cstaResponses.MakeCallResponse;
import cstaResponses.MonitorStartResponse;
import cstaResponses.OriginatedEvent;
import cstaResponses.RequestSystemStatusResponse;
import cstaResponses.ServiceInitiatedEvent;
import cstaResponses.SnapshotDeviceResponse;

public class XmlDecoder {
	private InputStream xmlInputStream;
	public Document doc;
	public Element rootElement;
	public CstaMessages myCstaMessage;
	public Object myObject;
	
	
	XmlDecoder(byte[] is){
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			xmlInputStream = new ByteArrayInputStream(is);
			 doc = dBuilder.parse(xmlInputStream);
			 doc.getDocumentElement().normalize();
			 rootElement=doc.getDocumentElement();
			 String response=rootElement.getNodeName();
			 myCstaMessage=null;
			 myObject=null;
			 for (CstaMessages m: CstaMessages.values()){
				 if (m.getTag().equals(response)){
					 myCstaMessage=m;
					 
					 break;
				 }
			 }
			 
		}catch (ParserConfigurationException | SAXException | IOException e){
			
		}
	}
	
	public CstaMessages getCstaMessage()throws NullPointerException{
		if (myCstaMessage!=null){
			return myCstaMessage;
		}else {
			throw new NullPointerException();
		}
	}
	
	public Document getDocument(){
		return doc;
	}
	
	public Element getRootElement(){
		return rootElement;
	}
	
	public Object returnObject(){
		if (myCstaMessage!=null){
			switch (myCstaMessage){
			case RequestSystemStatusResponse:
				myObject=new RequestSystemStatusResponse(doc);
				break;
			case GetLogicalDeviceInformationResponse:
				myObject=new GetLogicalDeviceInformationResponse(doc);
				break;
			case MonitorStartResponse:
				myObject=new MonitorStartResponse(doc);
				break;
			case SnapshotDeviceResponse:
				myObject=new SnapshotDeviceResponse(doc);
				break;
			case GetForwardingResponse:
				myObject=new GetForwardingResponse(doc);
				break;
			case GetDoNotDisturbResponse:
				myObject=new GetDoNotDisturbResponse(doc);
				break;
			case DeliveredEvent:
				myObject=new DeliveredEvent(doc);
				break;
			case EstablishedEvent:
				myObject=new EstablishedEvent(doc);
				break;
			case ConnectionClearedEvent:
				myObject=new ConnectionClearedEvent(doc);
				break;
			case MakeCallResponse:
				myObject=new MakeCallResponse(doc);
				break;
			case ServiceInitiatedEvent:
				myObject=new ServiceInitiatedEvent(doc);
				break;
			case OriginatedEvent:
				myObject=new OriginatedEvent(doc);
				break;
			default:
				
			}
		}
		return myObject;
	}
	
	
	

}
