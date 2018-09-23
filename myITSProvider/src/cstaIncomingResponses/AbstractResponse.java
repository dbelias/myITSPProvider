package cstaIncomingResponses;




import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public abstract class AbstractResponse {
	private static String REGEX="N<(.+)\\/(.+)>";
	protected static Pattern p=Pattern.compile(REGEX);
	protected Matcher m;
	public Document doc;
	public NodeList myNodeList;
	public Node myNode;
	public Element rootElement;
	
	
	AbstractResponse(Document doc){
		this.doc=doc;
		rootElement=doc.getDocumentElement();		
	}
	
	public NodeList getNodeList()
	{
		return myNodeList;
	}
	
	public Node getNode(){
		return myNode;
	}
	
	public Element getRootElement(){
		return rootElement;
	}
	
	

}
