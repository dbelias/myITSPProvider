package support;

public enum SIPHeadersTxt {
	RequestLine (0,"Request Line", 0),
	FromLine(1,"From:",1),
	ToLine(2,"To:",2),
	ContactLine(3,"Contact:",3),
	PAILine(4,"PAI:",4),
	PPILine(5,"PPI:",5),
	DiversionLine(6,"Diversion:",6),
	ResetLines(7,"REset All Lines",7);
	
	private final int index;
    private final String name;
    private final int value;
    
    private SIPHeadersTxt(int i, String s, int v){
        this.index=i;
        this.name=s;
        this.value=v;
    }
    
    public int getIndex(){
        return index;
    }
    
    public String getName(){
        return name;
    }
    
    public int getValue(){
        return value;
    }

}
