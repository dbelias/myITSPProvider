package support;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XMLRestore {
	private static Logger logger=Logger.getLogger("XMLRestore");
	
	public XMLRestore(GStreamerLocation g, WAVLocation w, BackupSettings b){
		
		try {
			File inputFile=new File("ITSPConfiguration.xml");
			SAXBuilder saxBuilder=new SAXBuilder();
			Document doc=saxBuilder.build(inputFile);
			Element configElement=doc.getRootElement();
			List<Element> myElemetList=configElement.getChildren();
			for (Element temp: myElemetList){
				switch(temp.getName()){
				case "gstreamer":
					setGstreamerSettings(temp,g);
					break;
				case "audio_files":
					setWAVSettings(temp, w);
					break;
				case "settings":
					setOtherSettings(temp,b);
				}
			}
		} catch (JDOMException e) {
			logger.error("JDOMException", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("IOException", e);
			e.printStackTrace();
		}
		
		
	}
	
	private void setOtherSettings(Element e, BackupSettings b){
		b.setIsAvailable();
		b.setCallingSettings(e.getChildText("caller"));
		logger.debug("OAD set with:"+e.getChildText("caller"));
		b.setCalledSettings(e.getChildText("called"), e.getChildText("calledIPAddress"), e.getChildText("calledIPPort"));
		logger.debug("DAD set with "+e.getChildText("called")+"@"+e.getChildText("calledIPAddress")+":"+e.getChildText("calledIPPort"));
		String transport=null;
		transport=e.getChildText("transport");
		if (transport!=null){
			b.setTransport(transport);
			logger.debug("Transport protocol set with:"+transport);
		} else {
			logger.debug("Transport protocol Not Found!. Default value(UDP) is set.");
		}
		String pswd=null;
		pswd=e.getChildText("password");
		if (pswd!=null){
			b.setPassword(pswd);
			logger.debug("Registration Password set with:"+pswd);
		} else {
			pswd="a11111111!";
			b.setPassword(pswd);
			logger.debug("Registration Password set with default value:"+pswd);
		}
		String listeningPort=null;
		listeningPort=e.getChildText("listeningPort");
		if (listeningPort!=null){
			b.setListeningPort(listeningPort);
			logger.debug("Listening Port set with:"+listeningPort);
		} else {
			listeningPort="5060!";
			b.setListeningPort(listeningPort);
			logger.debug("Listening Port set with default value:"+listeningPort);
		}
		
		
		
		
		
	}
	
	private void setGstreamerSettings(Element e, GStreamerLocation g){
		g.updateGStreamerLocation(e.getChildText("path"), e.getChildText("file"));
		logger.debug("gstreamer set with:"+e.getChildText("path")+e.getChildText("file"));
	}
	private void setWAVSettings(Element e, WAVLocation w){
		List<Element> myWAVList=e.getChildren();
		for (Element temp : myWAVList){
			if (temp.getName().equals("flag")){
				w.setIsAnnounceAsTrxSource(Boolean.valueOf(temp.getText()));
				logger.debug(temp.getName()+" is set with:"+temp.getText());
			}
			else {
			w.setPathName(temp.getChildText("path"), temp.getChildText("file"), temp.getName());
			logger.debug(temp.getName()+" is set with:"+temp.getChildText("path")+temp.getChildText("file"));
			}
		}
		
	}
	
	
}
