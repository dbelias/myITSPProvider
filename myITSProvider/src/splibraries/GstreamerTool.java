package splibraries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Random;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import support.GStreamerCmdThread;
import support.GStreamerLocation;

public class GstreamerTool {
	private static Logger logger=Logger.getLogger("GstreamerTool");
	private boolean stopFlag;
	private String peerIP;
	private int peerPort;
	private int localPort;
	private GStreamerCmdThread trx;
	private GStreamerCmdThread rcv;
	private String cmdTrx;
	private String cmdRcv;
	private String path;
	private String execcmdTrx;
	private String execcmdTrxBase;
	private String execcmdRcv;
	private String execcmdRcvBase;
	private String codecEncoding;
	private String codecDecoding;
	private String rtpPay;
	private String rtpDepay;
	private String PayloadType;
	private String MTU;
	private String frame;
	private String ssrc;
	private String announcement;
	private boolean isAnnouncement;
	private static int callID=0;
	private TreeMap<Integer, Boolean> tm;
	
	

	
	public GstreamerTool(){
		this.tm=new TreeMap<Integer,Boolean>();
		this.peerIP=null;
		this.peerPort=0;
		this.localPort=0;
		trx=null;
		rcv=null;
		this.stopFlag=false;
		this.isAnnouncement=false;
		this.MTU="172";
		this.frame="20000000";
		
		this.ssrc=Integer.toString(19751978);
		//path="C:\\gstreamer\\1.0\\x86\\bin\\gst-launch-1.0.exe ";
		//execcmdTrx="autoaudiosrc do-timestamp=true  !  audioconvert ! audioresample ! alawenc ! rtppcmapay pt=8 mtu=172  min-ptime=20000000 max-ptime=20000000 ssrc=1468797040 ! udpsink host=";
		//execcmdTrx="autoaudiosrc do-timestamp=true  !  audioconvert ! audioresample ! avenc_g722 ! rtpg722pay pt=9 mtu=172  min-ptime=20000000 max-ptime=20000000 ssrc=1468797040 ! udpsink host=";
		//execcmdTrx="autoaudiosrc do-timestamp=true  !  audioconvert ! audioresample ! avenc_g722 ! rtpg722pay pt=9 mtu=172  min-ptime=20000000 max-ptime=20000000 ssrc=1468797040 ! udpsink host=";
		//execcmdTrxBase="autoaudiosrc do-timestamp=true  !  audioconvert ! audioresample ! ";
		execcmdTrxBase="autoaudiosrc ! audioconvert ! audioresample ! ";
		//String announcement="C:\\wavs\\holdmusic.wav".replace('\\', '/');
		//execcmdTrxBase="filesrc location="+announcement+" ! wavparse ! audioconvert ! audioresample ! ";
		
		//execcmdRcv=" caps=\"application/x-rtp\" ! queue ! rtppcmadepay ! alawdec ! audioconvert ! autoaudiosink";
		//execcmdRcv=" caps=\"application/x-rtp\" ! queue ! rtpg722depay ! avdec_g722 ! autoaudiosink";
		execcmdRcvBase=" caps=\"application/x-rtp\" ! queue ! ";
		
	}
	
	public void setIsAnnouncement(boolean ia){
		if (ia){
			isAnnouncement=true;
			execcmdTrxBase="filesrc location="+announcement+" ! wavparse ! audioconvert ! audioresample ! ";
		}
	}
	public void startMedia(String destIP, int destPort, int sourcePort, int codec) throws InterruptedException{
		logger.info("Start media ordered. Dst:"+destIP+":"+destPort+" from SrcPort:"+sourcePort+" with Codec:"+codec);
		++callID;
		tm.put(callID, false);
		stopFlag=false;
		peerIP=destIP;
		peerPort=destPort;
		localPort=sourcePort;
		setCodecAttributes(codec);
		Calendar cal = Calendar.getInstance();
		long t=cal.getTimeInMillis();
		long a=t & 0x000000007fffffffL;
		ssrc=Integer.toString((int)a);
		execcmdTrx=execcmdTrxBase+codecEncoding+" ! "+rtpPay+" pt="+PayloadType+" mtu="+MTU
		+" min-ptime="+frame+" max-ptime="+frame+" ssrc="+ssrc+" ! udpsink host="+peerIP
		+" port="+peerPort+" bind-port="+localPort;
		execcmdRcv="udpsrc port="+localPort+execcmdRcvBase+rtpDepay+" ! "+codecDecoding+" ! audioconvert ! autoaudiosink";
		cmdTrx=path+" "+execcmdTrx;
		cmdRcv=path+" "+execcmdRcv;
		/*
		String newcmd=path+" "+"rtpbin name=rtpbin "+execcmdTrxBase+codecEncoding
		+" ! "+rtpPay+" pt="+PayloadType+" mtu="+MTU
		+" min-ptime="+frame+" max-ptime="+frame+" ssrc="+ssrc
		+" ! rtpbin.send_rtp_sink_0 rtpbin.send_rtp_src_0 ! udpsink host="
		+peerIP+" port="+peerPort+" bind-port="+localPort+" sync=false async=false "
		+"udpsrc port="+localPort+execcmdRcvBase+"rtpbin.recv_rtp_sink_0 rtpbin. ! "
		+rtpDepay+" ! "+codecDecoding+" ! audioconvert ! autoaudiosink sync=false async=false";
		*/
		//start();
		/*
		System.out.println("-------Trx Command--------");
		System.out.println(cmdTrx);
		System.out.println("-------Rcv Command--------");
		System.out.println(cmdRcv);
		*/
		//Thread.sleep(300);
		rcv=new GStreamerCmdThread(cmdRcv,GstreamerTool.this,"Rcv",callID);
		Thread.sleep(300);
		if (isAnnouncement){
			trx=new GStreamerCmdThread(cmdTrx,GstreamerTool.this,"Trx-Announcement",callID);
		} else{
			trx=new GStreamerCmdThread(cmdTrx,GstreamerTool.this,"Trx",callID);
		}
		
		
	}
	
	public void stopMedia(){
		//System.out.println("gStreamer.StopMedia ordered"); 
		logger.info("Stop Media ordered for CallID:"+callID);
		stopFlag=true;
		tm.put(callID, true);
		//trx.destroy();
		//rcv.destroy();
	}
	public boolean getStopFlag(int i){
		return tm.get(callID);
		//return stopFlag;
	}
	public void setGstreamer(GStreamerLocation gsl){
		path=gsl.getPath()+gsl.getFile();
	}
	public void setAnnouncement(String a){
		announcement=a;
	}
	private void setCodecAttributes(int c){
		if (c==0){//G711U-ulaw
			codecEncoding="mulawenc";
			codecDecoding="mulawdec";
			rtpPay="rtppcmupay";
			rtpDepay="rtppcmudepay";
			PayloadType="0";
			
		}
		else if (c==8){//G711A-alaw
			codecEncoding="alawenc";
			codecDecoding="alawdec";
			rtpPay="rtppcmapay";
			rtpDepay="rtppcmadepay";
			PayloadType="8";
		}
		else if (c==9){//G722
			codecEncoding="avenc_g722";
			codecDecoding="avdec_g722";
			rtpPay="rtpg722pay";
			rtpDepay="rtpg722depay";
			PayloadType="9";
		}
		else if (c==18){//G729
			codecEncoding="avenc_g729";//not exist!!!!!DO NOT USE IT!!!!!!
			codecDecoding="avdec_g729";
			rtpPay="rtpg729pay";
			rtpDepay="rtpg729depay";
			PayloadType="18";
		}
	}
}
