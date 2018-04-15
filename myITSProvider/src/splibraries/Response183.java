package splibraries;

import java.util.LinkedList;
import java.util.ListIterator;

import support.HeadersValuesGeneric;

public class Response183 {
	private boolean sendSDP;
	private boolean isAvoided;
	private LinkedList<HeadersValuesGeneric> my183HeadersList;
	
	
	public Response183(){
		this.sendSDP=false;
		this.isAvoided=true;
		this.my183HeadersList=new LinkedList<HeadersValuesGeneric>();
		
	}
	public void setSendSDP(boolean b){
		sendSDP=b;
	}
	public boolean getSendSDP(){
		return sendSDP;
	}
	
	public void setIsAvoided(boolean b){
		isAvoided=b;
	}
	
	public boolean getIsAvoided(){
		return isAvoided;
	}
	
	public void addHeader(String h, String v){
		HeadersValuesGeneric temp=new HeadersValuesGeneric(h,v);
		my183HeadersList.add(temp);
	}
	public void addHeader(HeadersValuesGeneric c){
		my183HeadersList.add(c);
	}
	public void removeHeader(int i){
		my183HeadersList.remove(i);
	}
	public String getHeaderValue(int i){
		String s=null;
		s=my183HeadersList.get(i).getHeaderValue();
		return s;
	}
	public LinkedList<String> getHeaderValueStringList(){
		LinkedList<String> stringList=new LinkedList<String>();
		ListIterator<HeadersValuesGeneric> listIterator = my183HeadersList.listIterator();
		while (listIterator.hasNext()) {
			stringList.add(listIterator.next().getHeaderValue());
        }
		return stringList;
		
	}
	public LinkedList<HeadersValuesGeneric> getHeaderValuesList(){
		return my183HeadersList;
	}
	public Response183 copyData(){
		Response183 copyObject=new Response183();
		copyObject.sendSDP=sendSDP;
		copyObject.isAvoided=isAvoided;
		copyObject.my183HeadersList=new LinkedList<HeadersValuesGeneric>();
		copyObject.my183HeadersList.addAll(my183HeadersList);
		return copyObject;
	}

}
