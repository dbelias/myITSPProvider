package support;

public class GStreamerLocation {
	private String path;
	private String file;
	private GStreamerLocation gStreamerLocator;
	
	public GStreamerLocation(String p, String f){
		this.path=p;
		this.file=f;
	}
	
	public String getPath(){
		return path;
	}
	public String getFile(){
		return file;
	}
	
	public GStreamerLocation getGStreamerLocation(){
		gStreamerLocator.path=path;
		gStreamerLocator.file=file;
		return gStreamerLocator;
	}
	
	public void updateGStreamerLocation(String p, String f){
		path=p;
		file=f;
	}
	public void updateGStreamerLocation(GStreamerLocation g){
		path=g.path;
		file=g.file;
	}

}
