package support;

public class voiceConfiguration {
	private codec voicecodec;
	private boolean setCodec;
	private boolean preferred;
	private int priority;
	
	public voiceConfiguration(){
		this.voicecodec=new codec();
		this.setCodec=false;
		this.preferred=false;
		this.priority=0;
		
	}
	public voiceConfiguration(String name, String payloadtype, String clockrate, String framesize, boolean s, int prio){
		this.voicecodec=new codec(name,payloadtype,clockrate, framesize);
		this.setCodec=s;
		this.preferred=false;
		this.priority=prio;
		
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

}
