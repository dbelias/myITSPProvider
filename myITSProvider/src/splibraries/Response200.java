package splibraries;

import java.util.LinkedList;
import java.util.ListIterator;

import support.HeadersValuesGeneric;

public class Response200 {
	private boolean COLPSupport;
	private LinkedList<HeadersValuesGeneric> my200HeadersList;
	
	
	public Response200(){
		this.COLPSupport=false;
		this.my200HeadersList=new LinkedList<HeadersValuesGeneric>();
		
	}
	public void setCOLP(boolean b){
		COLPSupport=b;
	}
	public boolean getCOLP(){
		return COLPSupport;
	}
	public void addHeader(String h, String v){
		HeadersValuesGeneric temp=new HeadersValuesGeneric(h,v);
		my200HeadersList.add(temp);
	}
	public void addHeader(HeadersValuesGeneric c){
		my200HeadersList.add(c);
	}
	public void removeHeader(int i){
		my200HeadersList.remove(i);
	}
	public String getHeaderValue(int i){
		String s=null;
		s=my200HeadersList.get(i).getHeaderValue();
		return s;
	}
	public LinkedList<String> getHeaderValueStringList(){
		LinkedList<String> stringList=new LinkedList<String>();
		ListIterator<HeadersValuesGeneric> listIterator = my200HeadersList.listIterator();
		while (listIterator.hasNext()) {
			stringList.add(listIterator.next().getHeaderValue());
        }
		return stringList;
		
	}
	public LinkedList<HeadersValuesGeneric> getHeaderValuesList(){
		return my200HeadersList;
	}
	public Response200 copyData(){
		Response200 copyObject=new Response200();
		copyObject.COLPSupport=COLPSupport;
		copyObject.my200HeadersList=new LinkedList<HeadersValuesGeneric>();
		copyObject.my200HeadersList.addAll(my200HeadersList);
		return copyObject;
	}

}
