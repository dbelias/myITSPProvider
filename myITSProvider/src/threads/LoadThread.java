package threads;

import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;

import circuit.CSTAMessageHandler;
import core.ITSPListener;
import cstaRequests.AbstractRequest;
import support.LoadRepeatMode;
import support.LoadSettingsHandler;
import window.myITSPmainWnd;

public class LoadThread implements Runnable {
	private static Logger logger=Logger.getLogger("LoadThread");
	private final long THREAD_SLEEP=2*1000L;
	private boolean continueFlag;
	private boolean shouldRunFlag;
	private ITSPListener itspListener;
	private myITSPmainWnd mainWnd;
	private LoadRepeatMode myRepeatMode;
	private int loops;
	private int scheduledRepeats;
	private Date now;
	private long durationOfLoad;
	private CSTAMessageHandler myCSTAMessageHandler;
	private boolean sentSuccesfully;
	private AbstractRequest tempMessage; 
	
	public LoadThread(ITSPListener itspListener, myITSPmainWnd mainWnd ){
		this.itspListener=itspListener;
		this.mainWnd=mainWnd;
		this.myRepeatMode=LoadSettingsHandler.myRepeatMode;
		this.continueFlag=true;
		this.shouldRunFlag=true;
		this.scheduledRepeats=LoadSettingsHandler.times;
		this.durationOfLoad=(LoadSettingsHandler.minutes)*60*1000L;
		this.myCSTAMessageHandler=CSTAMessageHandler.getInstance();
		this.sentSuccesfully=true;
		logger.trace("LoadThread is initialized");
		
	}
	

	@Override
	public void run() {
		logger.trace("LoadThread is started");
		this.now=new Date();
		loops=0;
		if (LoadSettingsHandler.myScheduledCstaRequestMap.size()>0){			
			LoadSettingsHandler.createWeightedMap();			
			while (isRunning()){
				loops++;
				
				try {
					Thread.sleep(THREAD_SLEEP);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AbstractRequest myRandomlySelectedRequest=getRandomlySelectedRequest();
				synchronized(this){
					if (itspListener.getCoreState()==0){
						myCSTAMessageHandler.setOutgoingRequest(myRandomlySelectedRequest.getBytes());
						itspListener.userInput(6,mainWnd.getDestination());
						sentSuccesfully=true;
					}else{
						sentSuccesfully=false;
					}
					
				}
				
				
			}
		}else {
			//No scheduled CSTA messages to send
			logger.warn("LoadThread has no Scheduled message to send");
		}
		logger.info("LoadThread is stopping");

	}
	
	private AbstractRequest getRandomlySelectedRequest() {
		// TODO Auto-generated method stub
		if (sentSuccesfully){
			tempMessage=LoadSettingsHandler.getRandomlySelectedCstaMessage();
			return tempMessage;
		}else{
			logger.warn("Last CSTA message"+tempMessage.toString()+ " seems that it's not send. Try again");
			return tempMessage;
		}
		
	}


	private boolean isRunning(){
		switch (myRepeatMode){
		case TIMES:
			if (loops>scheduledRepeats){
				continueFlag=false;
			}
			
			break;
		case MINUTES:
			Date currentTime=new Date();
			long duration=currentTime.getTime()-now.getTime();
			if (duration>durationOfLoad){
				continueFlag=false;
			}
			break;
		case TIMESTAMP:
			continueFlag=false;
			break;
		}
		return (continueFlag & shouldRunFlag);
		
	}
	
	public void setStop(){
		shouldRunFlag=false;
	}
	

}
