package support;

import java.io.IOException;

import splibraries.GStreamerToneTool;


public class GStreamerToneCmdThread extends Thread {
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
		System.out.println(command);
			try {
				pid=Runtime.getRuntime().exec(command);
				pid.waitFor();
				
				
				while (!obj.getStopFlag()){
					Thread.sleep(300);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				System.out.println(name+":Stopped hopefully");
				pid.destroy();
				
			}
			
		
	}


}
