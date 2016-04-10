package support;

public class HeadersValuesGeneric {
	private String header;
	private String value;
	
	public HeadersValuesGeneric(){
		this.header=null;
		this.value=null;
	}
	public HeadersValuesGeneric(String h, String v){
		this.header=h;
		this.value=v;
	}
	public HeadersValuesGeneric(HeadersValuesGeneric c){
		this.header=c.header;
		this.value=c.value;
	}
	
	public void setHeader(String h){
		header=h;
	}
	public void setValue(String v){
		value=v;
	}
	public void setHeaderValue(HeadersValuesGeneric c){
		header=c.header;
		value=c.value;
	}
	public String getHeader(){
		return header;
	}
	public String getValue(){
		return value;
	}
	public String getHeaderValue(){
		return header+":"+value;
	}

}
