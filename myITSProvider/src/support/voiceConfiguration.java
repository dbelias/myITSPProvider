package support;

public class voiceConfiguration {
	private codec voicecodec;
	private boolean setCodec;
	private boolean preferred;
	private int priority;
	private boolean hasOverrideOrder;
	
	public voiceConfiguration(){
		this.voicecodec=new codec();
		this.setCodec=false;
		this.preferred=false;
		this.priority=0;
		this.hasOverrideOrder=false;
		
	}
	public voiceConfiguration(String name, String payloadtype, String clockrate, String framesize, boolean s, int prio){
		this.voicecodec=new codec(name,payloadtype,clockrate, framesize);
		this.setCodec=s;
		this.preferred=false;
		this.priority=prio;
		this.hasOverrideOrder=false;
		
	}
	
	public void setVoiceConfig(String name, String payloadtype, String clockrate, String framesize,  boolean s, int prio){
		voicecodec.setCodec(name,payloadtype,clockrate, framesize);
		setCodec=s;
		priority=prio;
	}
	
	public void setPreferred(boolean p){
		preferred=p;
	}
	public void setPriority(int i){
		priority=i;
	}
	
	public void setSetCodec(boolean s){
		setCodec=s;
	}
	
	public codec getVoiceConfig(){
		return voicecodec;
	}
	
	public boolean getSetCodec(){
		return setCodec;
	}
	
	public boolean getPreferred(){
		return preferred;
	}
	
	public int getPriority(){
		return priority;
	}
	
	public void setHasOverrideOrder(boolean b){
		hasOverrideOrder=b;
	}
	
	public boolean getHasOverrideOrder(){
		return hasOverrideOrder;
	}

}
