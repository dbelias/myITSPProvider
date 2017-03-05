package support;

public class CallFeatures {
	public HoldMode holdMode;
	public ReInviteMode reInviteMode;
	public Feature activeFeature;
	
	public CallFeatures (){
		holdMode=HoldMode.SENDONLY;
		reInviteMode=ReInviteMode.WithSDP;
		activeFeature=Feature.NormalCall;
	}
	
	public void setHoldMode(HoldMode hm){
		holdMode=hm;
	}
	
	public void setReInviteMode(ReInviteMode rm){
		reInviteMode=rm;
	}
	
	public void setActiveFeature(Feature ft){
		activeFeature=ft;
	}
	
	public HoldMode getHoldMode(){
		return holdMode;
	}
	
	public int getHoldModeToInt(){
		return holdMode.getIndex();
	}
	
	public ReInviteMode getReInviteMode(){
		return reInviteMode;
	}
	
	public int getReInviteModeToInt(){
		return reInviteMode.getIndex();
	}
	
	public Feature getActiveFeature(){
		return activeFeature;
	}
	
	public int getActiveFeatureToInt(){
		return activeFeature.getIndex();
	}

}
