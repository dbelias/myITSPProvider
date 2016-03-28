package support;

public class codec {
	private String name;
	private int payloadType;
	private int clockRate;
	private int framesize;
	
	public codec(){
		this.name="new_codec";
		this.clockRate=8000;
		this.payloadType=8;
		this.framesize=20;
	}
	
	public codec(String n, String pt, String cr, String fr){
		this.name=n;
		this.clockRate=Integer.parseInt(pt);
		this.payloadType=Integer.parseInt(cr);
		this.framesize=Integer.parseInt(fr);
	}
	
	public void setCodec(String n, String pt, String cr, String fr){
		name=n;
		payloadType=Integer.parseInt(pt);
		clockRate=Integer.parseInt(cr);
		framesize=Integer.parseInt(fr);
		
	}
	
	public int getPayloadType(){
		
		return payloadType;
	}
	
	public int getClockRate(){
		return clockRate;
	}
	
	public String getName(){
		return name;
	}
	
	public int getFrameSize(){
		return framesize;
	}

}
