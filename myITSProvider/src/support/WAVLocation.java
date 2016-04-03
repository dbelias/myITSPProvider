package support;

public class WAVLocation {
	private String pathRingBackTone;
	private String fileRingbackTone;
	private String pathRingTone;
	private String fileRingTone;
	private String pathTrxPayload;
	private String fileTrxPayload;
	private String pathRcvPayload;
	private String fileRcvPayload;
	private WAVLocation WAVLocObj;
	private boolean isAnnounceAsTrxSource;
	
	public WAVLocation(){
		this.pathRingBackTone=null;
		this.fileRingbackTone=null;
		this.pathRingTone=null;
		this.fileRingTone=null;
		this.pathTrxPayload=null;
		this.fileTrxPayload=null;
		this.pathRcvPayload=null;
		this.fileRcvPayload=null;
		this.isAnnounceAsTrxSource=false;
	}
	
	public WAVLocation(String p1, String f1, String p2, String f2, String p3, String f3){
		this.pathRingBackTone=p1;
		this.fileRingbackTone=f1;
		this.pathRingTone=p2;
		this.fileRingTone=f2;
		this.pathTrxPayload=p3;
		this.fileTrxPayload=f3;
		this.pathRcvPayload=null;
		this.fileRcvPayload=null;
	}
	public WAVLocation(String p1, String f1, String p2, String f2, String p3, String f3, boolean b){
		super();
		this.isAnnounceAsTrxSource=b;
	}
	
	public void updateWAVPaths(String p1, String f1, String p2, String f2, String p3, String f3){
		pathRingBackTone=p1;
		fileRingbackTone=f1;
		pathRingTone=p2;
		fileRingTone=f2;
		pathTrxPayload=p3;
		fileTrxPayload=f3;
	}
	public void updateWAVPaths(String p1, String f1, String p2, String f2, String p3, String f3, boolean b){
		updateWAVPaths(p1,f1,p2,f2,p3,f3);
		isAnnounceAsTrxSource=b;
	}
	
	public void updatePaths(WAVLocation c){
		pathRingBackTone=c.pathRingBackTone;
		fileRingbackTone=c.fileRingbackTone;
		pathRingTone=c.pathRingTone;
		fileRingTone=c.fileRingTone;
		pathTrxPayload=c.pathTrxPayload;
		fileTrxPayload=c.fileTrxPayload;
		isAnnounceAsTrxSource=c.isAnnounceAsTrxSource;
	}
	public String getRingBackTonePath(){
		return pathRingBackTone;
	}
	public String getRingBackToneFile(){
		return fileRingbackTone;
	}
	public String getRingTonePath(){
		return pathRingTone;		
	}
	public String getRingToneFile(){
		return fileRingTone;
	}
	public String getTrxPayloadPath(){
		return pathTrxPayload;
	}
	public String getTrxPayloadFile(){
		return fileTrxPayload;
	}
	public boolean getIsAnnounceAsTrxSource(){
		return isAnnounceAsTrxSource;
	}
	
	
	
	

}
