package splibraries;

import java.util.LinkedList;
import java.util.ListIterator;

import support.HeadersValuesGeneric;

public class RequestInvite {
	private boolean hasUserAgent;
	private LinkedList<HeadersValuesGeneric> myInviteHeadersList;
	
	public RequestInvite(){
		this.hasUserAgent=true;
		this.myInviteHeadersList=new LinkedList<HeadersValuesGeneric>();
	}
	
	public void setHasUserAgent(boolean b){
		this.hasUserAgent=b;
	}
	
	public boolean getHasUserAgent(){
		return hasUserAgent;
	}
	
	public void addHeader(String h, String v){
		HeadersValuesGeneric temp=new HeadersValuesGeneric(h,v);
		myInviteHeadersList.add(temp);
	}
	public void addHeader(HeadersValuesGeneric c){
		myInviteHeadersList.add(c);
	}
	public void removeHeader(int i){
		myInviteHeadersList.remove(i);
	}
	public String getHeaderValue(int i){
		String s=null;
		s=myInviteHeadersList.get(i).getHeaderValue();
		return s;
	}
	public LinkedList<String> getHeaderValueStringList(){
		LinkedList<String> stringList=new LinkedList<String>();
		ListIterator<HeadersValuesGeneric> listIterator = myInviteHeadersList.listIterator();
		while (listIterator.hasNext()) {
			stringList.add(listIterator.next().getHeaderValue());
        }
		return stringList;
		
	}
	public LinkedList<HeadersValuesGeneric> getHeaderValuesList(){
		return myInviteHeadersList;
	}
}
