package support;

import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import cstaRequests.AbstractRequest;

public class LoadSettingsHandler {
	private static Logger logger=Logger.getLogger("LoadSettingsHandler");
	public static int times=100;
	public static int minutes=60;
	//public static timestamp;
	public static LoadRepeatMode myRepeatMode=LoadRepeatMode.TIMES;
	public static TreeMap<Integer, ScheduledCstaRequest> myScheduledCstaRequestMap=new TreeMap<Integer,ScheduledCstaRequest>();
	private static SortedMap<Integer,Integer> myWeightedSortedMap=new TreeMap<Integer,Integer>();
	private static int counter=0;
	private static Random myRnd;
	private static int totalWeight=0;
	
	public static void setScheduledCstaRequest(ScheduledCstaRequest scr){
		counter++;
		myScheduledCstaRequestMap.put(counter, scr);
		scr.index=counter;
	}
	
	public static void deleteScheduledCstaRequest(int i){
		myScheduledCstaRequestMap.remove(i);
	}
	
	public static void setWeightAtScheduledCstaRequest(int i, int w){
		myScheduledCstaRequestMap.get(i).weight=w;
	}
	
	public static void setRepeatMode(LoadRepeatMode lrm){
		myRepeatMode=lrm;
	}
	
	public static String getScheduledCstaRequestToString(int i){
		return new String(myScheduledCstaRequestMap.get(i).myRequest.getBytes());
	}
	
	public static void resetMap(){
		myScheduledCstaRequestMap.clear();
		counter=0;
	}
	
	public static void setTimes(String s){
		times=Integer.parseInt(s);
		logger.trace("Times is set as:"+times);
	}
	
	public static void setMinutes(String s){
		minutes=Integer.parseInt(s);
		logger.trace("Minutes is set as:"+minutes);
	}
	
	public static void createWeightedMap(){
		logger.trace("myWeightedSortedMap is going to be initialized");
		Set<Integer> myKeySet=myScheduledCstaRequestMap.keySet();
		int counter=0;
		for (int myKey: myKeySet){
			int tempWeight=myScheduledCstaRequestMap.get(myKey).weight;
			totalWeight+=tempWeight;
			for (int i=0;i<tempWeight;i++){
				myWeightedSortedMap.put(counter, myKey);
				counter++;
			}
		}
		myRnd=new Random();
	}
	
	public static AbstractRequest getRandomlySelectedCstaMessage(){
		if ((totalWeight-1)<1){//only one CSTA message is scheduled to send
			return myScheduledCstaRequestMap.get(myWeightedSortedMap.get(0)).myRequest;
		}else{
			int rnd=myRnd.nextInt(totalWeight-1);
			return myScheduledCstaRequestMap.get(myWeightedSortedMap.get(rnd)).myRequest;
		}
		
	}

}
