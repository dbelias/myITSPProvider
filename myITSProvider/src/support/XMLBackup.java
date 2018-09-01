package support;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


public class XMLBackup {
	private static Logger logger=Logger.getLogger("XMLBackup");
	
	public XMLBackup(GStreamerLocation g, WAVLocation w, BackupSettings b){
		try{
			Element configElement=new Element("configuration");
			Document doc= new Document(configElement);
			Element gstreamerElement=new Element("gstreamer");
			Element path=new Element("path");
			Element fileName=new Element("file");
			path.setText(g.getPath());
			fileName.setText(g.getFile());
			gstreamerElement.addContent(path);
			gstreamerElement.addContent(fileName);
			doc.getRootElement().addContent(gstreamerElement);
			doc.getRootElement().addContent(wavElement(w));
			doc.getRootElement().addContent(settingsElement(b));
			XMLOutputter xmlOutput = new XMLOutputter();
	        xmlOutput.setFormat(Format.getPrettyFormat());
	        xmlOutput.output(doc, new FileOutputStream("ITSPConfiguration.xml")); 
	        

		}catch(IOException e){
			logger.error("IOException", e);
	         e.printStackTrace();
	      }		
	}
	
	private Element settingsElement(BackupSettings b){
		Element otherSettingElement=new Element("settings");
		Element callerNumber=new Element("caller");
		Element calledNumber=new Element ("called");
		Element calledIPAddr=new Element ("calledIPAddress");
		Element calledIPPort=new Element ("calledIPPort");
		Element transport=new Element ("transport");
		Element password=new Element ("password");
		Element listeningPort=new Element("listeningPort");
		callerNumber.setText(b.CallingNumber);
		calledNumber.setText(b.CalledNumber);
		calledIPAddr.setText(b.CalledIPAddress);
		calledIPPort.setText(b.CalledIPPort);
		transport.setText(b.transport);
		password.setText(b.password);
		listeningPort.setText(b.listeningPort);
		otherSettingElement.addContent(callerNumber);
		otherSettingElement.addContent(calledNumber);
		otherSettingElement.addContent(calledIPAddr);
		otherSettingElement.addContent(calledIPPort);
		otherSettingElement.addContent(transport);
		otherSettingElement.addContent(password);
		otherSettingElement.addContent(listeningPort);
		return otherSettingElement;
	}
	
	private Element wavElement(WAVLocation w){
		Element audioFilesElement=new Element("audio_files");
		Element ringbackElement=setElementFilePath("ringbacktone",w);
		Element ringElement=setElementFilePath("ringtone",w);
		Element announceElement=setElementFilePath("announcement",w);
		Element flagElement=new Element("flag");
		flagElement.setText(Boolean.toString(w.getIsAnnounceAsTrxSource()));
		audioFilesElement.addContent(ringbackElement);
		audioFilesElement.addContent(ringElement);
		audioFilesElement.addContent(announceElement);
		audioFilesElement.addContent(flagElement);
		
		return audioFilesElement;
	
	}
	
	private Element setElementFilePath(String s, WAVLocation w){
		Element temp=null;;
		Element path=new Element("path");
		Element fileName=new Element("file"); 
		switch (s){
		case "ringbacktone":
			temp=new Element(s);
			path.setText(w.getRingBackTonePath());
			fileName.setText(w.getRingBackToneFile());
			break;
		case "ringtone":
			temp=new Element(s);
			path.setText(w.getRingTonePath());
			fileName.setText(w.getRingToneFile());
			break;
		case "announcement":
			temp=new Element(s);
			path.setText(w.getTrxPayloadPath());
			fileName.setText(w.getTrxPayloadFile());
			break;
		}
		temp.addContent(path);
		temp.addContent(fileName);
		return temp;
	}
}
