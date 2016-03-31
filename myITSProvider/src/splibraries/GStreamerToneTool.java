package splibraries;


import org.apache.log4j.Logger;

import support.GStreamerLocation;
import support.GStreamerToneCmdThread;

public class GStreamerToneTool {
	private static Logger logger=Logger.getLogger("GStreamerToneTool");
	private boolean stopFlag;
	private String audioPath;
	private String path;
	private String audioPlayCmd;
	private String audioPipelineBase;
	
	
	public GStreamerToneTool(){
		this.stopFlag=false;
		this.audioPath=null;
		this.path=null;
		this.audioPipelineBase=" ! wavparse ! audioconvert ! autoaudiosink";
		}
	public void prepareTone(String f){
		audioPath=f;
	}
	public void playTone(){
		stopFlag=false;
		audioPlayCmd=path+" "+"filesrc location="+audioPath+audioPipelineBase;
		GStreamerToneCmdThread toneThread=new GStreamerToneCmdThread(audioPlayCmd,GStreamerToneTool.this,"PlayTone");
	}
	public boolean getStopFlag(){
		return stopFlag;
	}
	public void stopTone(){
		logger.info("Stop Tone ordered");
		//System.out.println("gStreamerTone.StopTone ordered"); 
		stopFlag=true;
	}
	public void setGstreamer(GStreamerLocation gsl){
		path=gsl.getPath()+gsl.getFile();
	}

}
