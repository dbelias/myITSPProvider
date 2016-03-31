package support;

import java.io.IOException;

import org.apache.log4j.Logger;

import splibraries.GStreamerToneTool;


public class GStreamerToneCmdThread extends Thread {
	private static Logger logger=Logger.getLogger("GStreamerToneCmdThread");
	private GStreamerToneTool obj;
	private String command;
	private Process pid;
	private String name;
	public GStreamerToneCmdThread(String s,GStreamerToneTool o, String n ){
		this.command=s;
		this.obj=o;
		this.name=n;
		start();
	}
	public void run(){
		//System.out.println(command);
		logger.info(command);
			try {
				pid=Runtime.getRuntime().exec(command);
				pid.waitFor();
				
				
				while (!obj.getStopFlag()){
					Thread.sleep(300);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("IOException", e);
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				logger.error("InterruptedException", e);
				e.printStackTrace();
			}
			finally {
				//System.out.println(name+":Stopped hopefully");
				logger.info(name+" stopped hopefully");
				pid.destroy();
				
			}
			
		
	}


}
