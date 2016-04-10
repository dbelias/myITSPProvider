package support;

import splibraries.Response180;
import splibraries.Response183;

public class SIPResponsesInfo {
	public Response183 Resp183;
	public Response180 Resp180;
	public SIPResponsesInfo(){
		Resp183=null;
		Resp180=null;
	}
	public void setResponse183(Response183 r){
		this.Resp183=r;	
		}
	public void setResponse180(Response180 r){
		this.Resp180=r;
	}

}
