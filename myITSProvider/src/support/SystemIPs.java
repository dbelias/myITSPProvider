package support;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class SystemIPs {
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
			    	System.out.println("    " + allMyIps[i].getHostAddress());
			    }
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
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
