package splibraries;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sdp.*;

import org.apache.log4j.Logger;

import gov.nist.javax.sdp.fields.AttributeField;

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
    sdpinfo.setDTMFPT();
    if (sdpinfo.isDtmfFirst){
    	 aaf[0]=sdpinfo.DTMF_PT;
    	 aaf[1]=sdpinfo.aformat;  	  	   
    }else {
    	 aaf[0]=sdpinfo.aformat;  	 
    	 aaf[1]=sdpinfo.DTMF_PT;
    }
    
    
    
    MediaDescription myAudioDescription;
    //aaf[0]=sdpinfo.getAFormat();
    if (b){
    	 myAudioDescription = mySdpFactory.createMediaDescription("audio", sdpinfo.aport, 1, "RTP/AVP", aaf);
    }else{
    	 myAudioDescription = mySdpFactory.createMediaDescription("audio", sdpinfo.aport, 1, "RTP/AVP", sdpinfo.getAudioFormatList());
        
    }
    if (sdpinfo.getDTMFAvailable()){
    	String rtpmap=String.valueOf(sdpinfo.DTMF_PT)+" telephone-event/8000";
        String fmtp=String.valueOf(sdpinfo.DTMF_PT)+" 0-15";
        myAudioDescription.setAttribute("rtpmap", rtpmap);
        myAudioDescription.setAttribute("fmtp", fmtp);
    }
    
    //TODO: this create exception and causes problem with java 1.8.101
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
        
        logger.debug("Received AudioFormatsVector size="+audioFormats.size());
        
        int myAudioMediaFormat = Integer.parseInt(audioFormats.elementAt(0).toString());
        logger.debug("Audio Codec in first position="+myAudioMediaFormat);
        if (audioFormats.size()>1){
      	  setSDPinfoArrayList(audioFormats,mySdpInfo.audioFormatList);
        }
        Vector attributeVector=new Vector();
        attributeVector=myAudioDescription.getAttributes(true);
        
        String directionAttribute="sendrecv";
        int size=attributeVector.size();
        if (size>0){
        	findDirections(attributeVector);
        	findDTMFpt(attributeVector);
        }else {
        	mySdpInfo.setDirection(directionAttribute);
        }
        
        
        
        
        
        int myVideoPort=-1;
        int myVideoMediaFormat=-1;    
        //System.out.println(recMediaDescriptionVector.size());
        if (recMediaDescriptionVector.size()>1) {
          MediaDescription myVideoDescription = (MediaDescription)
              recMediaDescriptionVector.elementAt(1);
          Media myVideo = myVideoDescription.getMedia();
          myVideoPort = myVideo.getMediaPort();
          Vector videoFormats = myVideo.getMediaFormats(false);
          logger.debug("Received VideoFormatsVector size="+audioFormats.size());
          myVideoMediaFormat =  Integer.parseInt(videoFormats.elementAt(0).toString());
          logger.debug("Video Codec in first position="+myVideoMediaFormat);
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
  private void findDirections(Vector v) throws SdpParseException{
	// TODO Auto-generated method stub
	  Attribute a;//=(Attribute) attributeVector.get(size-1);
	  Pattern pattern=Pattern.compile("\\s*(sendrecv|recvonly|sendonly|inactive)\\s*$");
	  String temp=null;
  	  boolean isDirectionAvailable=false;
  	  for (int i=0;i<v.size();i++){
		a=(Attribute) v.get(i);
		temp=a.getName(); //if attribute does not have name, returns null
		if (temp!=null){
			 Matcher matcher=pattern.matcher(temp);
			 if (matcher.find()){
				 mySdpInfo.setDirection(matcher.group(1));
		    	logger.debug("Direction is found: a="+matcher.group(1));	
		    	isDirectionAvailable=true;
		    	}
		}
	}
	if (!isDirectionAvailable){
		logger.warn("Direction 'a=' is not found in SDP. 'sendrecv' is assumed");
		mySdpInfo.setDirection("sendrecv");
	}
	
}

private void findDTMFpt(Vector v) throws SdpParseException {
	  Attribute a;//=(Attribute) attributeVector.get(size-1);
	  Pattern pattern=Pattern.compile("\\s*(\\d+)\\s*telephone-event[/]\\d+");
	  String temp=null;
  	 boolean isDTMFAvalailable=false;
	for (int i=0;i<v.size();i++){
		a=(Attribute) v.get(i);
		temp=a.getValue(); //if attribute does not have value, returns null
		if (temp!=null){
			 Matcher matcher=pattern.matcher(temp);
			 if (matcher.find()){
				 mySdpInfo.setDTMFPT(Integer.valueOf(matcher.group(1)));
		    	logger.debug("DTMF rtpmap is found:"+matcher.group(1));	
		    	isDTMFAvalailable=true;
		    	}
		}
	}
	if (!isDTMFAvalailable){
		logger.warn("DTMF rtpmap is Not Found");
	}
	
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
