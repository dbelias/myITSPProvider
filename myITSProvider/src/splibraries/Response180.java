package splibraries;

import java.util.LinkedList;
import java.util.ListIterator;

import support.HeadersValuesGeneric;

public class Response180 {
	private boolean sendSDP;
	private LinkedList<HeadersValuesGeneric> my180HeadersList;
	
	
	public Response180(){
		this.sendSDP=false;
		this.my180HeadersList=new LinkedList<HeadersValuesGeneric>();
		
	}
	public void setSendSDP(boolean b){
		sendSDP=b;
	}
	public boolean getSendSDP(){
		return sendSDP;
	}
	public void addHeader(String h, String v){
		HeadersValuesGeneric temp=new HeadersValuesGeneric(h,v);
		my180HeadersList.add(temp);
	}
	public void addHeader(HeadersValuesGeneric c){
		my180HeadersList.add(c);
	}
	public void removeHeader(int i){
		my180HeadersList.remove(i);
	}
	public String getHeaderValue(int i){
		String s=null;
		s=my180HeadersList.get(i).getHeaderValue();
		return s;
	}
	public LinkedList<String> getHeaderValueStringList(){
		LinkedList<String> stringList=new LinkedList<String>();
		ListIterator<HeadersValuesGeneric> listIterator = my180HeadersList.listIterator();
		while (listIterator.hasNext()) {
			stringList.add(listIterator.next().getHeaderValue());
        }
		return stringList;
		
	}
	public LinkedList<HeadersValuesGeneric> getHeaderValuesList(){
		return my180HeadersList;
	}
	public Response180 copyData(){
		Response180 copyObject=new Response180();
		copyObject.sendSDP=sendSDP;
		copyObject.my180HeadersList=new LinkedList<HeadersValuesGeneric>();
		copyObject.my180HeadersList.addAll(my180HeadersList);
		return copyObject;
	}

}
