package support;

public enum ReInviteMode {
	WithSDP (0,"With SDP"),
	WithoutSDP (1,"Without SDP");
	
	private int index;
	private String description;
	
	private ReInviteMode (int i, String s){
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
