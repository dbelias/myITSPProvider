package circuit;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.xml.sax.SAXException;

import cstaIncomingResponses.ConnectionClearedEvent;
import cstaIncomingResponses.DeliveredEvent;
import cstaIncomingResponses.EstablishedEvent;
import cstaIncomingResponses.GetDoNotDisturbResponse;
import cstaIncomingResponses.GetForwardingResponse;
import cstaIncomingResponses.GetLogicalDeviceInformationResponse;
import cstaIncomingResponses.HeldEvent;
import cstaIncomingResponses.MakeCallResponse;
import cstaIncomingResponses.MonitorStartResponse;
import cstaIncomingResponses.OriginatedEvent;
import cstaIncomingResponses.RequestSystemStatusResponse;
import cstaIncomingResponses.RetrievedEvent;
import cstaIncomingResponses.ServiceInitiatedEvent;
import cstaIncomingResponses.SnapshotDeviceResponse;

public class XmlDecoder {
	private static Logger logger=Logger.getLogger("XmlDecoder");
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
					 logger.info("XML Decoder recognized XML message as "+m.getDescription());
					 break;
				 }
			 }
			 
		}catch (ParserConfigurationException | SAXException | IOException e){
			logger.error("Exception in XML Decoder",e);
		}
	}
	
	public CstaMessages getCstaMessage()throws NullPointerException{
		if (myCstaMessage!=null){
			return myCstaMessage;
		}else {
			logger.error("XML Decoder didn't recognize the XML message. Unknown or not implemented yet");
			return CstaMessages.Unknown;
			//throw new NullPointerException();
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
			case HeldEvent:
				myObject=new HeldEvent(doc);
				break;
			case RetrievedEvent:
				myObject=new RetrievedEvent(doc);
				break;
				
			default:
				
			}
		}
		return myObject;
	}
	
	
	

}
