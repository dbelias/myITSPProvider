package support;

import splibraries.Response180;
import splibraries.Response183;
import splibraries.Response200;

public class SIPResponsesInfo {
	public Response183 Resp183;
	public Response180 Resp180;
	public Response200 Resp200;
	public SIPResponsesInfo(){
		Resp183=null;
		Resp180=null;
		Resp200=null;
	}
	public void setResponse183(Response183 r){
		this.Resp183=r;	
		}
	public void setResponse180(Response180 r){
		this.Resp180=r;
	}
	public void setResponse200(Response200 r){
		this.Resp200=r;
	}

}
