package support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import splibraries.GstreamerTool;

public class GStreamerCmdThread extends Thread{
	private static Logger logger=Logger.getLogger("GStreamerCmdThread");
	private GstreamerTool obj;
	private String command;
	private Process pid;
	private String name;
	private int callID;
	public GStreamerCmdThread(String s,GstreamerTool o, String n, int ID  ){
		this.command=s;
		this.obj=o;
		this.name=n;
		this.callID=ID;
		start();
	}
	public void run(){
		//System.out.println(command);
		logger.info(command);
			try {
				
				pid=Runtime.getRuntime().exec(command);
				//pid.waitFor();
				
				/*
				BufferedReader in = new BufferedReader(new InputStreamReader(pid.getInputStream()));
				String line = null;
				while ((line = in.readLine()) != null) {
					System.out.println(name+":"+line);
				}

				//pid.waitFor();
				 * Don't use the while above because it never goes out of it. Consequently the while below 
				 * is never taken into account!!!!
				 */
				while (!obj.getStopFlag(callID)){
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
				logger.info(name+" stopped hopefully with callID:"+callID);
				pid.destroy();
				
			}
			
		
	}

}
