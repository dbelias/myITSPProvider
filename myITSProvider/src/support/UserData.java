package support;

import java.text.ParseException;

import gov.nist.javax.sip.address.SipUri;

public class UserData {
	private String displayPart;
	private String userPart;
	private String host;
	private int port;
	private SipUri uri;
	
	public UserData(){
		this.displayPart=null;
		this.userPart=null;
		this.host=null;
		this.port=5060;
		this.uri=null;
	}
	
	public UserData(String user){
		this.displayPart=null;
		this.userPart=user;
		this.host=null;
		this.port=5060;
		this.uri=null;
	}
	
	public UserData(SipUri uri){
		this.displayPart=null;
		this.userPart=uri.getUser();
		this.host=uri.getHost();
		this.port=uri.getPort();
		if (this.port==-1){
			this.port=5060;
		}
		this.uri=uri;
	}

	/**
	 * @return the displayPart
	 */
	public String getDisplayPart() {
		return displayPart;
	}

	/**
	 * @param displayPart the displayPart to set
	 */
	public void setDisplayPart(String displayPart) {
		this.displayPart = displayPart;
	}

	/**
	 * @return the userPart
	 */
	public String getUserPart() {
		return userPart;
	}

	/**
	 * @param userPart the userPart to set
	 */
	public void setUserPart(String userPart) {
		this.userPart = userPart;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return String.valueOf(port);
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = Integer.valueOf(port);
	}
	
	public SipUri getUri(){
		if (uri==null){
			SipUri temp=new SipUri();
			temp.setUser(userPart);
			try {
				temp.setHost(host);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			temp.setPort(port);
			return temp;
		}else return uri;
	}

}
