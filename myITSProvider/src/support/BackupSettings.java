package support;

public class BackupSettings {
	public String CalledIPPort;
	public String CalledIPAddress;
	public String CalledNumber;
	public String CallingNumber;
	private boolean isAvailable;
	
	public BackupSettings(){
		this.CalledIPPort="5060";
		this.CalledIPAddress="192.168.1.1";
		this.CalledNumber="022129205990";
		this.CallingNumber="2109567472";
		this.isAvailable=false;
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
	
	public boolean getIsAvailable(){
		return isAvailable;
	}

}
