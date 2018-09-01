package support;

public class BackupSettings {
	public String CalledIPPort;
	public String CalledIPAddress;
	public String CalledNumber;
	public String CallingNumber;
	public String transport;
	public String password;
	public String listeningPort;
	private boolean isAvailable;
	
	public BackupSettings(){
		this.CalledIPPort="5060";
		this.CalledIPAddress="192.168.1.1";
		this.CalledNumber="022129205990";
		this.CallingNumber="2109567472";
		this.isAvailable=false;
		this.transport="udp";
		this.password="a11111111!";
		this.listeningPort="5060";
	}
	
	public void setCalledSettings(String number, String address, String port){
		CalledIPPort=port;
		CalledIPAddress=address;
		CalledNumber=number;
	}
	
	public void setCallingSettings(String number){
		CallingNumber=number;
	}
	
	public void setIsAvailable(){
		isAvailable=true;
	}
	public void setTransport(String t){
		transport=t;
	}
	
	public boolean getIsAvailable(){
		return isAvailable;
	}
	
	public void setPassword(String p){
		password=p;
	}
	public String getPassword(){
		return password;
	}

	public String getListeningPort() {
		return listeningPort;
	}

	public void setListeningPort(String listeningPort) {
		this.listeningPort = listeningPort;
	}
	
	

}
