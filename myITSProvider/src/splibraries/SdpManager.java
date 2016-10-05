package splibraries;


import java.util.*;
import javax.sdp.*;

import org.apache.log4j.Logger;

public class SdpManager {
	private static Logger logger=Logger.getLogger("SdpManager");

  SdpFactory mySdpFactory;
  SdpInfo mySdpInfo;
  byte[] mySdpContent;


  public SdpManager() {
    mySdpFactory = SdpFactory.getInstance();
  }

  public byte[] createSdp(SdpInfo sdpinfo, boolean b) {

    try{
    //Session-level description lines
    Version myVersion = mySdpFactory.createVersion(0);
    long ss=mySdpFactory.getNtpTime(new Date());
    Origin myOrigin = mySdpFactory.createOrigin("-",ss,ss,"IN","IP4",sdpinfo.IpAddress);
    SessionName mySessionName = mySdpFactory.createSessionName("-Belias Dimitris "+String.valueOf(ss));
    Connection myConnection = mySdpFactory.createConnection("IN","IP4", sdpinfo.IpAddress);

    //Time description lines
    Time myTime=mySdpFactory.createTime();
    Vector myTimeVector=new Vector();
    myTimeVector.add(myTime);

    //Media description lines
    int[] aaf=new int[2];
    aaf[0]=sdpinfo.aformat;
    sdpinfo.setDTMFPT();
    aaf[1]=sdpinfo.DTMF_PT;
    
    MediaDescription myAudioDescription;
    //aaf[0]=sdpinfo.getAFormat();
    if (b){
    	 myAudioDescription = mySdpFactory.createMediaDescription("audio", sdpinfo.aport, 1, "RTP/AVP", aaf);
    }else{
    	 myAudioDescription = mySdpFactory.createMediaDescription("audio", sdpinfo.aport, 1, "RTP/AVP", sdpinfo.getAudioFormatList());
        
    }
    String rtpmap=String.valueOf(sdpinfo.DTMF_PT)+" telephone-event/8000";
    String fmtp=String.valueOf(sdpinfo.DTMF_PT)+" 0-15";
    myAudioDescription.setAttribute("rtpmap", rtpmap);
    myAudioDescription.setAttribute("fmtp", fmtp);
    myAudioDescription.setAttribute(sdpinfo.getDirection(), null);
    Vector myMediaDescriptionVector=new Vector();
    myMediaDescriptionVector.add(myAudioDescription);

    if (sdpinfo.vport!=-1) {
     int[] avf=new int[1];
     avf[0]=sdpinfo.vformat;
     MediaDescription myVideoDescription = mySdpFactory.createMediaDescription("video", sdpinfo.vport, 1, "RTP/AVP", avf);
     myMediaDescriptionVector.add(myVideoDescription);
    }

    SessionDescription mySdp = mySdpFactory.createSessionDescription();

    mySdp.setVersion(myVersion);
    mySdp.setOrigin(myOrigin);
    mySdp.setSessionName(mySessionName);
    mySdp.setConnection(myConnection);
    mySdp.setTimeDescriptions(myTimeVector);
    mySdp.setMediaDescriptions(myMediaDescriptionVector);

    mySdpContent=mySdp.toString().getBytes();

    }catch(Exception e){
    	logger.error("Exception", e);
      e.printStackTrace();
    }

    return mySdpContent;
  }
  public byte[] createSdp(SdpInfo sdpinfo) {
	  return createSdp(sdpinfo, false);
  }

  public SdpInfo getSdp(byte[] content) {
    try{
      mySdpInfo=new SdpInfo();
      String s = new String(content);
      SessionDescription recSdp = mySdpFactory.createSessionDescription(s);

      String myPeerIp=recSdp.getConnection().getAddress();

      String myPeerName=recSdp.getOrigin().getUsername();
      Vector recMediaDescriptionVector=recSdp.getMediaDescriptions(false);
      logger.info("Received MediaDescriptionVector size="+recMediaDescriptionVector.size());
        MediaDescription myAudioDescription = (MediaDescription) recMediaDescriptionVector.elementAt(0);
        Media myAudio = myAudioDescription.getMedia();
        int myAudioPort = myAudio.getMediaPort();
        Vector audioFormats = myAudio.getMediaFormats(false);
        
        logger.info("Received AudioFormatsVector size="+audioFormats.size());
        
        int myAudioMediaFormat = Integer.parseInt(audioFormats.elementAt(0).toString());
        logger.info("Audio Codec in first position="+myAudioMediaFormat);
        if (audioFormats.size()>1){
      	  setSDPinfoArrayList(audioFormats,mySdpInfo.audioFormatList);
        }
        Vector attributeVector=new Vector();
        attributeVector=myAudioDescription.getAttributes(true);
        
        String directionAttribute="sendrecv";
        int size=attributeVector.size();
        if (size>0){
        	Attribute a=(Attribute) attributeVector.get(size-1);
        	directionAttribute=a.getName();
        	logger.info("SDP with a="+directionAttribute);
        }
        
        mySdpInfo.setDirection(directionAttribute);
        
        
        int myVideoPort=-1;
        int myVideoMediaFormat=-1;    
        //System.out.println(recMediaDescriptionVector.size());
        if (recMediaDescriptionVector.size()>1) {
          MediaDescription myVideoDescription = (MediaDescription)
              recMediaDescriptionVector.elementAt(1);
          Media myVideo = myVideoDescription.getMedia();
          myVideoPort = myVideo.getMediaPort();
          Vector videoFormats = myVideo.getMediaFormats(false);
          logger.info("Received VideoFormatsVector size="+audioFormats.size());
          myVideoMediaFormat =  Integer.parseInt(videoFormats.elementAt(0).toString());
          logger.info("Video Codec in first position="+myVideoMediaFormat);
          if (videoFormats.size()>1){
        	  setSDPinfoArrayList(videoFormats,mySdpInfo.videoFormatList);
          }
        }

      


    //  mySdpInfo.setIPAddress(myPeerIp);
      mySdpInfo.IpAddress=myPeerIp;
      mySdpInfo.aport=myAudioPort;
      mySdpInfo.aformat=myAudioMediaFormat;
      mySdpInfo.vport=myVideoPort;
      mySdpInfo.vformat=myVideoMediaFormat;
      
      

    }catch(Exception e){
    	logger.error("Exception", e);
      e.printStackTrace();
    }

    return mySdpInfo;
  }
  private void setSDPinfoArrayList(Vector<?> v, ArrayList<Integer> al){
	  int i=v.size();
	  int temp;
	  for (int k=0;k<i; k++){
		  temp=Integer.parseInt(v.elementAt(k).toString());
		  al.add(temp);
	  }
  }
}
