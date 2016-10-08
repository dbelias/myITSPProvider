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
	
	public XMLRestore(GStreamerLocation g, WAVLocation w){
		
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
	private void setGstreamerSettings(Element e, GStreamerLocation g){
		g.updateGStreamerLocation(e.getChildText("path"), e.getChildText("file"));
		logger.info("gstreamer set with:"+e.getChildText("path")+e.getChildText("file"));
	}
	private void setWAVSettings(Element e, WAVLocation w){
		List<Element> myWAVList=e.getChildren();
		for (Element temp : myWAVList){
			if (temp.getName().equals("flag")){
				w.setIsAnnounceAsTrxSource(Boolean.valueOf(temp.getText()));
				logger.info(temp.getName()+" is set with:"+temp.getText());
			}
			else {
			w.setPathName(temp.getChildText("path"), temp.getChildText("file"), temp.getName());
			logger.info(temp.getName()+" is set with:"+temp.getChildText("path")+temp.getChildText("file"));
			}
		}
		
	}
	
	
}
