package support;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

import org.apache.log4j.Logger;

public class SystemIPs {
	private static Logger logger=Logger.getLogger("SystemIPs");
	private InetAddress[] allMyIps;
	private LinkedList<String> allMyIPsToString;
	private InetAddress myIP;
	public SystemIPs(){
		try {
			allMyIPsToString=new LinkedList<String>();
			myIP=InetAddress.getLocalHost();			
			InetAddress[] allMyIps = InetAddress.getAllByName(myIP.getCanonicalHostName());
			if (allMyIps != null && allMyIps.length > 1) {
				System.out.println(" Full list of IP addresses:");
			    for (int i = 0; i < allMyIps.length; i++) {
			    	
			    	allMyIPsToString.add(allMyIps[i].getHostAddress());
			    	//System.out.println("    " + allMyIps[i].getHostAddress());
			    	logger.info(allMyIps[i].getHostAddress());
			    }
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			logger.error("UnknownHostException", e);
			e.printStackTrace();
		}
	}
	public InetAddress[] getMyIPs(){
		return allMyIps;
	}
	public LinkedList<String> getMyIPsToString(){
		return allMyIPsToString;
	}

}
