package support;

public enum Feature {
	NormalCall (0,"Normal Call"),
	NormalCallLateSDP (1,"Late SDP Call"),
	ReInvite (3,"ReInvite"),
	Hold (4,"Hold");
	private int index;
	private String description;
	private Feature(int i, String s){
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
