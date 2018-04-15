package support;

import gov.nist.javax.sip.message.SIPResponse;

public class FailoverMode {
	public boolean hasRetryAfter;
	public String retryAfter;
	public int failoverHeader;
	
	
	public FailoverMode (int h, String s, boolean b){
		this.failoverHeader=h;
		this.retryAfter=s;
		this.hasRetryAfter=b;
	}
	public FailoverMode(){
		this(SIPResponse.REQUEST_TIMEOUT,"120",true);
	}
	
	
	
	public FailoverMode(int h){
		this(h, "120", true);
	}
	
	public void setRetryAfter(String v){
		retryAfter=v;
	}
	
	public void setFailoverHeader(int h){
		failoverHeader=h;
	}
	
	public void setHasRetryAfter(boolean b){
		hasRetryAfter=b;
	}

}
