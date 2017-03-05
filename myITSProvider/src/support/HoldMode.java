package support;

public enum HoldMode {
	SENDONLY (0,"SendOnly"),
	INACTIVE (1,"Inactive"),
	SENDRECEIVE (2,"SendReceive"),
	RECEIVEONLY (3,"ReceiveOnly");
	
	private int index;
	private String description;
	
	private HoldMode (int i, String s){
		this.index=i;
		this.description=s;
	}
	
	public int getIndex(){
        return index;
    }
    
    public String getDescription(){
        return description;
    }
}
