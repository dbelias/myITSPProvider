package support;

public class RegisteredDevice {
	public boolean isRegistered;
	public boolean isUsed;
	public String did;
	public int index;
	
	public RegisteredDevice(boolean r, boolean u, String s, int i){
		this.isRegistered=r;
		this.isUsed=u;
		this.did=s;
		this.index=i;
	}
	
	public RegisteredDevice(String s){
		this(false,false,s,1);
	}
	
	public RegisteredDevice(boolean r, String s){
		this(r,false,s,1);
	}
	
	public void setRegistrationStatus(boolean b){
		isRegistered=b;
	}
	
	public boolean getRegistrationStatus(){
		return isRegistered;
	}
	public void setUsedStatus(boolean b){
		isUsed=b;
	}
	
	public boolean getUsedStatus(){
		return isUsed;
	}
	public void setIndex(int i){
		index=i;
	}
	public int getIndex(){
		return index;
	}
	
	public String getDID(){
		return did;
	}

}
