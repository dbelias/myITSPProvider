package core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionAlreadyExistsException;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentLengthHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.EventHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderAddress;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.RetryAfterHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.UserAgentHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import circuit.CSTAMessageHandler;
import circuit.CircuitGuiUpdates;
import circuit.CircuitHandler;
import circuit.CstaXmlManager;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.clientauthutils.DigestServerAuthenticationHelper;
import gov.nist.javax.sip.header.AuthenticationHeader;
import gov.nist.javax.sip.header.CallID;
import gov.nist.javax.sip.header.Event;
import gov.nist.javax.sip.header.From;
import gov.nist.javax.sip.header.WWWAuthenticate;
import splibraries.Configuration;
import splibraries.GStreamerToneTool;
import splibraries.GstreamerTool;
import splibraries.SdpInfo;
import splibraries.SdpManager;
import splibraries.TonesTool;
import splibraries.VideoTool;
import support.Feature;
import support.HeadersValuesGeneric;
import support.HoldMode;
import support.LegTransaction;
import support.ReInviteMode;
import support.RegisteredDevice;
import support.SIPHeadersTxt;
import support.UserData;
//import splibraries.VoiceTool;
import window.myITSPmainWnd;

public class ITSPListener implements SipListener{
	private static Logger logger=Logger.getLogger("ITSPListener");
	
	private SipFactory mySipFactory;
	private SipStack mySipStack;
	private ListeningPoint myListeningPoint;
	private SipProvider mySipProvider;
	private MessageFactory myMessageFactory;
	private HeaderFactory myHeaderFactory;
	private AddressFactory myAddressFactory;
	private Properties myProperties;
	private myITSPmainWnd myGUI;
	private ContactHeader myContactHeader;
	private Header myUserAgentHeader;
	private Header myCircuitUserAgentHeader;
	private Header myAdditionalHeader;
	private Header myPAIHeader;
	private ViaHeader myViaHeader;
	private Address fromAddress;
	private Dialog myDialog;
	private ClientTransaction myClientTransaction;
	private ClientTransaction myCircuitClientTransaction;
	//private ClientTransaction myClientProxyTransaction;
	private ServerTransaction myServerTransaction;
	private ServerTransaction myCircuitServerTransaction;
	//private ServerTransaction myServerProxyTransaction;
	private int status;
	private int previousStatus;
	private String myIP;
	private String toTag;
	private SdpManager mySdpManager;
	private CstaXmlManager myCstaXmlManager;
	//private VoiceTool myVoiceTool;
	private GstreamerTool myGVoiceTool;
	private GstreamerTool myGAnnouncementTool;
	private VideoTool myVideoTool;
	//private TonesTool myAlertTool;
	private GStreamerToneTool myAlertTool;
	//private TonesTool myRingTool;
	private GStreamerToneTool myRingTool;
	private boolean isOnlyAnnouncment;
	private SdpInfo answerInfo;
	private SdpInfo offerInfo;
	private boolean isSendSDP183;

	private int myPort;
	private int myAudioPort;
	private int myVideoPort;
	private int myAudioCodec;
	private int myVideoCodec;
	private int myOldAudioPort;
	private int myOldVideoPort;
	private int myOldAudioCodec;
	private int myOldVideoCodec;
	private int remoteOldAudioPort;
	private int remoteOldAudioCodec;
	private int remoteOldVideoPort;
	private int remoteOldVideoCodec;
	private ArrayList<Integer> myCodecsList;
	
	//Additional parameters for new implementation 
	HashMap<String, UserData> myRegisteredUsers= new HashMap<String,UserData>();
	HashMap<String, LegTransaction> myTransactionMap=new HashMap<String, LegTransaction>();

	private String myTransport;

	private String myDisplayName;

	private String myUserPart;
	static final int expireTime=120;
	static final int PROXY_NOTUSED=-1;
	static final int PROXY_IDLE=0;
	static final int PROXY_WAIT=1;
	static final int PROXY_ESTABLISHED=2;
	static final int INITIAL_VARIABLE=66666;
	static final int INITIAL_BASE_VALUE=1000;
	//End of additional parameters

	static final int YES=0;
	static final int NO=1;
	static final int SEND183=2;
	static final int HOLD=3;
	static final int UNHOLD=4;
	static final int SEND_OPTIONS=5;
	static final int CIRCUIT_OPTIONS=6;

	static final int IDLE=0;
	static final int WAIT_PROV=1;
	static final int WAIT_FINAL=2;
	static final int ESTABLISHED=4;
	static final int RINGING=5;
	static final int WAIT_ACK=6;
	static final int RE_INVITE_WAIT_ACK=7;
	static final int WAIT_PROV_LATE_SDP=8;
	static final int WAIT_FINAL_LATE_SDP=9;
	static final int WAIT_OPTIONS_PROV=10;
	static final int WAIT_NOTIFY_PROV=11;

	class MyTimerTask extends TimerTask {
        ITSPListener myListener;
        public MyTimerTask (ITSPListener myListener){
                this.myListener=myListener;
              }
        public void run() {
          try{
            Request myBye = myListener.myDialog.createRequest("BYE");
            myBye.addHeader(myListener.myContactHeader);
            myListener.myClientTransaction =
                myListener.mySipProvider.getNewClientTransaction(myBye);
            myListener.myDialog.sendRequest(myListener.myClientTransaction);
          }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
	
	public ITSPListener(Configuration conf,myITSPmainWnd GUI)  {
	    try{
	    logger.info("Initialize ITSP Listener");
	      myGUI = GUI;
	      myIP = myGUI.getmyIP();
	      myPort = conf.sipPort;
	      myAudioPort=conf.audioPort;
	      myVideoPort=conf.videoPort;
	      myAudioCodec=conf.audioCodec;
	      myVideoCodec=conf.videoCodec;
	      myTransport=conf.transport;
	      myDisplayName=conf.name;
	      myUserPart=conf.userID;

	      mySdpManager=new SdpManager();
	      //myVoiceTool=new VoiceTool();
	      myGVoiceTool=new GstreamerTool();
	      myGVoiceTool.setGstreamer(myGUI.getGStreamer());//Set the path of gStreamer gst-launch
	      myGAnnouncementTool=new GstreamerTool();
	      myGAnnouncementTool.setGstreamer(myGUI.getGStreamer());
	      myVideoTool=new VideoTool();
	      answerInfo=new SdpInfo();
	      offerInfo=new SdpInfo();
	      myCodecsList=new ArrayList<Integer>();
	      myCodecsList=GUI.getAvailableCodecs();
	      
	      

	      //myAlertTool=new TonesTool();
	      //myRingTool=new TonesTool();
	      
	      myAlertTool=new GStreamerToneTool();
	      myAlertTool.setGstreamer(myGUI.getGStreamer());
	      myRingTool=new GStreamerToneTool();
	      myRingTool.setGstreamer(myGUI.getGStreamer());
			
	      //myAlertTool.prepareTone("/disconnect_11.wav");
	      //myAlertTool.prepareTone("file://c:\\Users\\BeliasD\\eclipse_workspace\\SIPTry1\\wavs\\uringtone.wav");
	      //Please consider the file path "\" must become "/"
	      String s=myGUI.getWAVLocation().getRingTonePath().replace('\\', '/');
	      s=s+myGUI.getWAVLocation().getRingToneFile();     
	      myAlertTool.prepareTone(s);
	      //myAlertTool.prepareTone("c:/Users/BeliasD/eclipse_workspace/myITSProvider/wavs/uringtone.wav");
	      //myRingTool.prepareTone("/phone_ring2.wav");
	      //myRingTool.prepareTone("file://c:\\Users\\BeliasD\\eclipse_workspace\\SIPTry1\\wavs\\ringbacktone.wav");
	      s=myGUI.getWAVLocation().getRingBackTonePath().replace('\\', '/');
	      s=s+myGUI.getWAVLocation().getRingBackToneFile();
	      myRingTool.prepareTone(s);
	      String announcementFile=myGUI.getWAVLocation().getTrxPayloadPath().replace('\\', '/');
	      announcementFile=announcementFile+myGUI.getWAVLocation().getTrxPayloadFile();
	      myGAnnouncementTool.setAnnouncement(announcementFile);
	      myGAnnouncementTool.setIsAnnouncement(true);
	      isOnlyAnnouncment=!(myGUI.getWAVLocation().getIsAnnounceAsTrxSource());
	      //myRingTool.prepareTone("c:/Users/BeliasD/eclipse_workspace/myITSProvider/wavs/ringbacktone.wav");
	      isSendSDP183=false;

	      mySipFactory = SipFactory.getInstance();
	      mySipFactory.setPathName("gov.nist");
	      myProperties = new Properties();
	      myProperties.setProperty("javax.sip.STACK_NAME", "myStack");
	      if (LogManager.getRootLogger().getLevel()==Level.TRACE){
	    	  myProperties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "LOG4J");
		      myProperties.setProperty("gov.nist.javax.sip.DEBUG_LOG",
						"shootmedebug.txt");
		      myProperties.setProperty("gov.nist.javax.sip.SERVER_LOG",
						"shootmelog.txt");
	      }
	      
	      mySipStack = mySipFactory.createSipStack(myProperties);
	      

	      myMessageFactory = mySipFactory.createMessageFactory();
	      myHeaderFactory = mySipFactory.createHeaderFactory();
	      myAddressFactory = mySipFactory.createAddressFactory();
	      if (myTransport.equalsIgnoreCase("udp")){
	    	  myListeningPoint = mySipStack.createListeningPoint(myIP, myPort, "udp");
	      } else {
	    	  myListeningPoint = mySipStack.createListeningPoint(myIP, myPort, "tcp"); 
	      }
	      //myListeningPoint = mySipStack.createListeningPoint(myIP, myPort, "udp");
	      mySipProvider = mySipStack.createSipProvider(myListeningPoint);
	      mySipProvider.addSipListener(this);
	      SipURI myContactURI=myAddressFactory.createSipURI(myUserPart, myIP);
	      myContactURI.setPort(myPort);
	      myContactURI.setTransportParam(myTransport);
	      //Address contactAddress = myAddressFactory.createAddress("sip:"+myIP+":"+myPort);
	      Address contactAddress = myAddressFactory.createAddress(myContactURI);
	      myContactHeader = myHeaderFactory.createContactHeader(contactAddress);
	      myUserAgentHeader = myHeaderFactory.createHeader("User-Agent",
	              "myITSP tool V1");
	      myCircuitUserAgentHeader=myHeaderFactory.createHeader("User-Agent","Circuit Simulator Agent");
	      myPAIHeader=myHeaderFactory.createHeader("P-Asserted-Identity", " <sip:"+conf.userID+"@"+myIP+">");
	      if (myGUI.getTelURI()){
	    	  fromAddress=myAddressFactory.createAddress(conf.name+ " <tel:"+conf.userID+">");
	      }
	      else{
	    	  fromAddress=myAddressFactory.createAddress(conf.name+ " <sip:"+conf.userID+"@"+myIP+":"+myPort+">");
	      }
	      
	      status=IDLE;

	      //myGUI.jLabel5.setText("At "+myIP+", port "+myPort);
	      myGUI.showStatus("Status: IDLE");

	    }catch (Exception e) {
	    	logger.error("Exception", e);
	     e.printStackTrace();
	    }
	  }


	  public void setOff(){
	    try{

	    mySipProvider.removeSipListener(this);
	    mySipProvider.removeListeningPoint(myListeningPoint);
	    mySipStack.deleteListeningPoint(myListeningPoint);
	    mySipStack.deleteSipProvider(mySipProvider);
	    myListeningPoint=null;
	    mySipProvider=null;
	    mySipStack=null;
	    myAlertTool=null;
	    myRingTool=null;
	    myGUI.showStatus("");
	    logger.info("SetOff");
	    }
	    catch(Exception e){
	    	logger.error("Exception", e);
	    }
	  }


	public void updateConfiguration(Configuration conf) {
	  myPort = conf.sipPort;
	  myAudioPort=conf.audioPort;
	  myVideoPort=conf.videoPort;
	  myAudioCodec=conf.audioCodec;
	  myVideoCodec=conf.videoCodec;
	  

	}
	
	public void updateCodecList(ArrayList<Integer> arr){
		myCodecsList=arr;
	}
	
	public void updateFromAddress(Configuration conf){
		try {
			if (myGUI.getTelURI()){
		    	  fromAddress=myAddressFactory.createAddress(conf.name+ " <tel:"+conf.userID+">");
		      }
		      else{
		    	  fromAddress=myAddressFactory.createAddress(conf.name+ " <sip:"+conf.userID+"@"+myIP+":"+myPort+">");
		    	  myPAIHeader=myHeaderFactory.createHeader("P-Asserted-Identity", " <sip:"+conf.userID+"@"+myIP+">");
		      }
		} catch (ParseException e) {
			logger.error("ParseException", e);
			e.printStackTrace();
		}
	}
	
	
	
	public void userInput(int type, String destination){
     try {
    	 logger.info("User Input:Type="+type+" Status="+status+" Destination="+destination);
       switch (status) {
         case IDLE:
           if (type == YES) {
        	 
             Address toAddress = myAddressFactory.createAddress(destination);
             ToHeader myToHeader = myHeaderFactory.createToHeader(toAddress, null);

             FromHeader myFromHeader = myHeaderFactory.createFromHeader(
                 fromAddress, "56438");

             //myViaHeader = myHeaderFactory.createViaHeader(myIP, myPort,"udp", null);
             myViaHeader = myHeaderFactory.createViaHeader(myIP, myPort,myTransport, null);
             ArrayList<ViaHeader> myViaHeaders = new ArrayList<ViaHeader>();
             myViaHeaders.add(myViaHeader);
             MaxForwardsHeader myMaxForwardsHeader = myHeaderFactory.
                 createMaxForwardsHeader(70);
             CSeqHeader myCSeqHeader = myHeaderFactory.createCSeqHeader(1L,
                 "INVITE");
             CallIdHeader myCallIDHeader = mySipProvider.getNewCallId();
             javax.sip.address.URI myRequestURI = toAddress.getURI();
             
             Request myRequest = myMessageFactory.createRequest(myRequestURI,
                 "INVITE",
                 myCallIDHeader, myCSeqHeader, myFromHeader, myToHeader,
                 myViaHeaders, myMaxForwardsHeader);
             myRequest.addHeader(myContactHeader);
             if (myGUI.SIPReqInfo.ReqInvite.getHasUserAgent()){
            	 myRequest.addHeader(myUserAgentHeader);
             }
             setAdditionalHeaderRequest(myRequest,myGUI.SIPReqInfo.ReqInvite.getHeaderValuesList());
             
             if (myGUI.getLateSDP()){
            	 logger.trace("Send Invite w/o SDP");
            	//TODO:Send Invite w/o SDP 
            	 myClientTransaction = mySipProvider.getNewClientTransaction(myRequest);
                 //String bid=myClientTransaction.getBranchId();

                 myClientTransaction.sendRequest();
                 myDialog = myClientTransaction.getDialog();
                 myGUI.display(">>> " + myRequest.toString());
                 status = WAIT_PROV_LATE_SDP;
                 myGUI.showStatus("Status: WAIT_PROV_LATE_SDP");
             }
             else { //Normal case: Send Invite with SDP

             offerInfo=new SdpInfo();
             offerInfo.IpAddress=myIP;
             offerInfo.aport=myAudioPort;
             myOldAudioPort=myAudioPort;
             myAudioPort=myAudioPort+2;
             //offerInfo.aformat=myAudioCodec;
             //myOldAudioCodec=myAudioCodec;
             offerInfo.vport=myVideoPort;
             offerInfo.vformat=myVideoCodec;
             offerInfo.setAudioFormatList(myCodecsList);
             offerInfo.isDtmfFirst=myGUI.getDtmfFirstOrder();
             

             ContentTypeHeader contentTypeHeader=myHeaderFactory.createContentTypeHeader("application","sdp");
             byte[] content=mySdpManager.createSdp(offerInfo);
             myRequest.setContent(content,contentTypeHeader);

             myClientTransaction = mySipProvider.getNewClientTransaction(myRequest);
             String bid=myClientTransaction.getBranchId();

             myClientTransaction.sendRequest();
             myDialog = myClientTransaction.getDialog();
             myGUI.display(">>> " + myRequest.toString());
             status = WAIT_PROV;
             myGUI.showStatus("Status: WAIT_PROV");
             
             
             }
             myGUI.setButtonStatusMakeCall();
             break;
           } else if (type==SEND_OPTIONS){
        	   //TODO:Implementation for Send OPTIONS (similar to Invite w/o SDP
        	   previousStatus=status;
        	   status=WAIT_OPTIONS_PROV;
        	   myGUI.showStatus("Status: WAIT_OPTIONS_PROV");
        	  break; 
           } else if (type==CIRCUIT_OPTIONS){
        	 //TODO:Implementation for Send CIRCUIT OPTIONS 
        	   sendCircuitNotifyRequest(destination);
        	   previousStatus=status;
        	   status=WAIT_NOTIFY_PROV;
        	   myGUI.showStatus("Status: WAIT_NOTIFY_PROV");
        	  break; 
           }
           

         case WAIT_FINAL:
         case WAIT_FINAL_LATE_SDP:
           if (type == NO) {
             Request myCancelRequest = myClientTransaction.createCancel();
             ClientTransaction myCancelClientTransaction = mySipProvider.
                 getNewClientTransaction(myCancelRequest);
             myCancelClientTransaction.sendRequest();
             myGUI.display(">>> " + myCancelRequest.toString());
             myRingTool.stopTone();

             status = IDLE;
             myGUI.showStatus("Status: IDLE");
             myGUI.setButtonStatusIdle();
             break;

           }
           else if (type==CIRCUIT_OPTIONS){
        	   sendCircuitNotifyRequest(destination);
        	   previousStatus=status;
        	   status=WAIT_NOTIFY_PROV;
        	   myGUI.showStatus("Status: WAIT_NOTIFY_PROV");
        	  break; 
           }

         case ESTABLISHED:
           if (type == NO) {
             Request myBye = myDialog.createRequest("BYE");
             myBye.addHeader(myContactHeader);
             myClientTransaction= mySipProvider.getNewClientTransaction(myBye);
             myDialog.sendRequest(myClientTransaction);
             myGUI.display(">>> " + myBye.toString());
             //myVoiceTool.stopMedia();
             if (isOnlyAnnouncment){
             	myGVoiceTool.stopMedia();
             }else {
             	myGAnnouncementTool.stopMedia();
             }

             if (answerInfo.vport>0) {
               myVideoTool.stopMedia();
             }

             status = IDLE;

             myGUI.showStatus("Status: IDLE");
             myGUI.showCodec("");
             myGUI.setButtonStatusIdle();
             
             break;
           }
           if (type==HOLD || type==UNHOLD){
        	  //TODO: Send Re-Invite from ITSP
        	   Request myReInvite = myDialog.createRequest("INVITE");
        	   //myReInvite.addHeader(myContactHeader);
        	   offerInfo=new SdpInfo();
               offerInfo.IpAddress=myIP;
               offerInfo.aport=myAudioPort;
               myOldAudioPort=myAudioPort;
               myAudioPort=myAudioPort+2;
               offerInfo.vport=myVideoPort;
               offerInfo.vformat=myVideoCodec;
               offerInfo.setAudioFormatList(myCodecsList);
               offerInfo.isDtmfFirst=myGUI.getDtmfFirstOrder();
               if (type==HOLD){
            	   if (myGUI.myCallFeaturesInfo.getHoldMode()==HoldMode.INACTIVE){
            		   offerInfo.setDirection("inactive");
                	   offerInfo.setDtmfAvailable(false); 
            		   
            	   } else {
            		   //Handle RecvOnly,SendOnly and SendReceive as SendOnly state
            		   offerInfo.setDirection("sendonly");
                	   offerInfo.setDtmfAvailable(false); 
            	   }            	  
               }
               ContentTypeHeader contentTypeHeader=myHeaderFactory.createContentTypeHeader("application","sdp");
               byte[] content=mySdpManager.createSdp(offerInfo);
               myReInvite.setContent(content,contentTypeHeader);
               myClientTransaction= mySipProvider.getNewClientTransaction(myReInvite);
               myDialog.sendRequest(myClientTransaction);
               myGUI.display(">>> " + myReInvite.toString());
        	   status=RE_INVITE_WAIT_ACK;
        	   myGUI.showStatus("Status: RE_INVITE_WAIT_ACK");
        	   break;
           }
           
           if (type==YES){
        	 //TODO: Send Re-Invite from ITSP
        	   Request myReInvite = myDialog.createRequest("INVITE");
        	   //myReInvite.addHeader(myContactHeader);
        	   if (myGUI.myCallFeaturesInfo.reInviteMode==ReInviteMode.WithSDP){
        		   offerInfo=new SdpInfo();
                   offerInfo.IpAddress=myIP;
                   offerInfo.aport=myAudioPort;
                   myOldAudioPort=myAudioPort;
                   myAudioPort=myAudioPort+2;
                   offerInfo.vport=myVideoPort;
                   offerInfo.vformat=myVideoCodec;
                   offerInfo.setAudioFormatList(myCodecsList);
                   offerInfo.isDtmfFirst=myGUI.getDtmfFirstOrder();
                   ContentTypeHeader contentTypeHeader=myHeaderFactory.createContentTypeHeader("application","sdp");
                   byte[] content=mySdpManager.createSdp(offerInfo);
                   myReInvite.setContent(content,contentTypeHeader);
                   myClientTransaction= mySipProvider.getNewClientTransaction(myReInvite);
                   myDialog.sendRequest(myClientTransaction);
                   myGUI.display(">>> " + myReInvite.toString());
            	   status=RE_INVITE_WAIT_ACK;
            	   myGUI.showStatus("Status: RE_INVITE_WAIT_ACK");
        	   } else {
        		   //ReInvite w/o SDP
        		   myClientTransaction= mySipProvider.getNewClientTransaction(myReInvite);
                   myDialog.sendRequest(myClientTransaction);
        		   myGUI.display(">>> " + myReInvite.toString());
            	   status=WAIT_PROV_LATE_SDP;
            	   myGUI.showStatus("Status: WAIT_PROV_LATE_SDP");
        	   }
        	   
        	   break;
        	   
           }
           if (type==CIRCUIT_OPTIONS){
        	   sendCircuitNotifyRequest(destination);
        	   previousStatus=status;
        	   status=WAIT_NOTIFY_PROV;;
        	   myGUI.showStatus("Status: WAIT_NOTIFY_PROV");
        	  break; 
           }
           
           

         case RINGING:
           if (type == NO) {
             Request originalRequest = myServerTransaction.getRequest();
             Response myResponse = myMessageFactory.createResponse(486,
                 originalRequest);
             myServerTransaction.sendResponse(myResponse);
             myGUI.display(">>> " + myResponse.toString());
              myAlertTool.stopTone();
              if (isSendSDP183){
            	  myGAnnouncementTool.stopMedia();           	   
              }

             status = IDLE;
             myGUI.showStatus("Status: IDLE");
             myGUI.showCodec("");
             myGUI.setButtonStatusIdle();
             break;
           }
           else if (type == YES) {

             Request originalRequest = myServerTransaction.getRequest();
             Response myResponse = myMessageFactory.createResponse(200,
                 originalRequest);
             ToHeader myToHeader = (ToHeader) myResponse.getHeader("To");
             myToHeader.setTag("454326");
             if (myGUI.SIPRespInfo.Resp200.getCOLP()){
            	 myResponse.addHeader(myPAIHeader);
             }
             
             myResponse.addHeader(myContactHeader);

             myAlertTool.stopTone();
             if (isSendSDP183){
           	  myGAnnouncementTool.stopMedia(); 
           	  myGUI.showCodec("");
             }


             ContentTypeHeader contentTypeHeader=myHeaderFactory.createContentTypeHeader("application","sdp");
             answerInfo.setAudioFormatList(myCodecsList);
             answerInfo.isDtmfFirst=myGUI.getDtmfFirstOrder();
             answerInfo.findProperCodec(offerInfo,true);
             //TODO: special handle in case codec is not existing =999
             //myAudioCodec=answerInfo.aformat;
             myOldAudioCodec=answerInfo.aformat;
             remoteOldAudioCodec=myOldAudioCodec;
             remoteOldAudioPort=offerInfo.aport;
             byte[] content=mySdpManager.createSdp(answerInfo,true);//answer only to the matched codec
             myResponse.setContent(content,contentTypeHeader);
             //handle what is the best audio codec to use
             
             //myVoiceTool.startMedia(offerInfo.IpAddress,offerInfo.aport,answerInfo.aport,offerInfo.aformat);
             if (isOnlyAnnouncment){
            	 myGVoiceTool.startMedia(offerInfo.IpAddress, offerInfo.aport, answerInfo.aport,answerInfo.aformat );
             } else {
            	 myGAnnouncementTool.startMedia(offerInfo.IpAddress, offerInfo.aport, answerInfo.aport,answerInfo.aformat );
             }
             

            if (answerInfo.vport>0) {
              myVideoTool.startMedia(offerInfo.IpAddress,offerInfo.vport,answerInfo.vport,myVideoCodec);
            }

             myServerTransaction.sendResponse(myResponse);
             myDialog = myServerTransaction.getDialog();

             //new Timer().schedule(new MyTimerTask(this),500000);
             myGUI.display(">>> " + myResponse.toString());
             status = WAIT_ACK;
             myGUI.showStatus("Status: WAIT_ACK");
             myGUI.showCodec(answerInfo.getAudioCodecString());
             myGUI.setButtonStatusEstablishedCall();
             break;
           }
           else if (type == SEND183){
        	   Request originalRequest = myServerTransaction.getRequest();
        	   Response myResponse=myMessageFactory.createResponse(183,originalRequest);
               myResponse.addHeader(myContactHeader);
               ToHeader myToHeader = (ToHeader) myResponse.getHeader("To");
               myToHeader.setTag("454326");
               setAdditionalHeadersResponse(myResponse, myGUI.SIPRespInfo.Resp183.getHeaderValuesList());
              
               isSendSDP183=myGUI.SIPRespInfo.Resp183.getSendSDP();
               if (isSendSDP183){
            	 
            	   createSDPResponse(myResponse);
               }
               myServerTransaction.sendResponse(myResponse);
               myDialog=myServerTransaction.getDialog();
               myGUI.display(">>> "+myResponse.toString());
               //TODO:handle SDP if exist, like 200 OK
               status=RINGING;
        	   
        	   break;
           }
           else if (type==CIRCUIT_OPTIONS){
        	   sendCircuitNotifyRequest(destination);
        	   previousStatus=status;
        	   status=WAIT_NOTIFY_PROV;
        	   myGUI.showStatus("Status: WAIT_NOTIFY_PROV");
        	  break; 
           }
       }
     }
     catch (Exception e){
    	 logger.error("Exception", e);
     e.printStackTrace();
   }

   }
	


	public void processDialogTerminated(DialogTerminatedEvent arg0) {
		// TODO Auto-generated method stub
		logger.warn("Process Dialog Terminated-->No handling");
		
	}

	public void processIOException(IOExceptionEvent arg0) {
		// TODO Auto-generated method stub
		logger.warn("Process IOExcpetion-->No handling");
		
	}


@SuppressWarnings("unused")
public void processRequest(RequestEvent requestReceivedEvent) {
  if (myGUI.chckbxEnableFaileover.isSelected()){
	  try {
		runFailoverMode(requestReceivedEvent);
	} catch (ParseException | SipException | InvalidArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  return;
  }
  Request myRequest=requestReceivedEvent.getRequest();
  String method=myRequest.getMethod();
  myGUI.display("<<<<<<<<<< "+myRequest.toString());
  if (method.equals("REGISTER")){
	  //TODO: Handle REGISTER method
	  boolean isOSBiz=false;
	  boolean isOSBizAuthenticated=true;
	  boolean isFirstRegistration=false;
	  ContactHeader myRegisterContactHeader=(ContactHeader)myRequest.getHeader(ContactHeader.NAME);
	  UserAgentHeader myUserAgentHeader=(UserAgentHeader)myRequest.getHeader(UserAgentHeader.NAME);
	  
	  ListIterator userAgentListIterator=myUserAgentHeader.getProduct();
	  String myUserAgentData="";
	  while (userAgentListIterator.hasNext()){
		  boolean check1=false;
		  boolean check2=false;
		  Object temp=userAgentListIterator.next();
		  String tempString=(String)temp;
		  myUserAgentData=myUserAgentData+tempString;
		  if (tempString.contains("OpenScape")){
			  check1=true;
		  }
		  if (tempString.contains("Business")){
			  check2=true;
		  }
		  if (check1 && check2){
			  isOSBiz=true;
		  }
	  }
	  logger.info("User-Agent:"+myUserAgentData);
	  SipUri uriContact= (SipUri)myRegisterContactHeader.getAddress().getURI();
	  String userPartContact=uriContact.getUser();
	  if (isOSBiz){//Registration comes from OSBiz
		  try {
			AuthorizationHeader myAuthorizationHeader=(AuthorizationHeader)myRequest.getHeader(AuthorizationHeader.NAME); 
			if (myAuthorizationHeader==null){//First Registration w/o authentication data
				isFirstRegistration=true;
				
			} else {
				
				if (!(isCorrectRegister(myRequest, myGUI.getMyPassword()))){
					//TODO:Do not send 200 OK for this REGISTER request
					isOSBizAuthenticated=false;
					logger.warn("OSBiz is not authenticated due to wrong credentials");
				}else {
					isOSBizAuthenticated=true;
					logger.info("OSBiz is authenticated due to correct credentials");
				}
			}
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
	  }else {//REGISTER comes from non-OSBiz
		  UserData myUserData =new UserData(uriContact);
		  myRegisteredUsers.put(userPartContact, myUserData);
		  myGUI.myRegisteredDevices.put(userPartContact, new RegisteredDevice(true,userPartContact));
		  myGUI.refreshRegisteredDeviceWnd();
	  }
	  
	  ViaHeader viaHeader=(ViaHeader)myRequest.getHeader(ViaHeader.NAME);
	  String receivedIP=viaHeader.getHost();
	  try {
		ServerTransaction myRegisterServerTransaction=mySipProvider.getNewServerTransaction(myRequest);
		Response myRegisterResponse;
		ExpiresHeader myPartnerExpireHeader=(ExpiresHeader)myRequest.getHeader(ExpiresHeader.NAME);
		int timerExpire=0;
		if (myPartnerExpireHeader==null){
			myRegisterContactHeader.getParameter("expires");
			timerExpire=Integer.valueOf(myRegisterContactHeader.getParameter("expires"));
		} else{
			timerExpire=myPartnerExpireHeader.getExpires();
		}
		ExpiresHeader myExpireHeader=myHeaderFactory.createExpiresHeader(expireTime);
		ContactHeader myRegisterReplyContactHeader=myRegisterContactHeader;
		if (isOSBiz){
			 logger.info("REGISTER from OSBiz: "+userPartContact+" from Host:"+viaHeader.getHost()+ " Port:"+ viaHeader.getPort());
			if (isFirstRegistration){
				myRegisterResponse=myMessageFactory.createResponse(401, myRequest);
				WWWAuthenticate myAuthHeader=new WWWAuthenticate();
				myAuthHeader.setAlgorithm("MD5");
				myAuthHeader.setRealm("Belias-Dimitrios");
				myAuthHeader.setNonce(generateNonce());
				myRegisterResponse.addHeader(myAuthHeader);
				
			}else {//is Not First Registration for OSBiz
				if (isOSBizAuthenticated){
					myRegisterResponse=myMessageFactory.createResponse(200, myRequest);
					
					if (timerExpire!=0){
						myRegisterResponse.addHeader(myExpireHeader);
						
					    myRegisterReplyContactHeader.setExpires(expireTime);
					    myRegisterResponse.addHeader(myRegisterReplyContactHeader);
					    logger.info("Response 200OK for REGISTER from OSBiz with Contact Header: "+uriContact.toString());
					} else {
						logger.info("Response 200OK for UN-REGISTER from OSBiz with Contact Header: "+uriContact.toString());
					}
					
				}else {//OSBiz is not Authenticated
					myRegisterResponse=myMessageFactory.createResponse(403, myRequest);
					logger.warn("Not authenticated OSBiz!!! Forbidden to continue");
					
				}
			}
		}else {
			logger.info("REGISTER from user: "+userPartContact+" from Host:"+viaHeader.getHost()+ " Port:"+ viaHeader.getPort());
			
			myRegisterResponse=myMessageFactory.createResponse(200, myRequest);
			if (timerExpire!=0){				
				myRegisterResponse.addHeader(myExpireHeader);
			    myRegisterReplyContactHeader.setExpires(expireTime);
			    myRegisterResponse.addHeader(myRegisterReplyContactHeader);
			    logger.info("Response 200OK for REGISTER from User with Contact Header: "+uriContact.toString());
			} else {
				logger.info("Response 200OK for UN-REGISTER from User with Contact Header: "+uriContact.toString());
				myRegisteredUsers.remove(userPartContact);
				myGUI.myRegisteredDevices.remove(userPartContact);
				myGUI.refreshRegisteredDeviceWnd();
			}
			
		}				
		((ViaHeader)myRegisterResponse.getHeader(ViaHeader.NAME)).setReceived(receivedIP);
		
		ToHeader myToHeader = (ToHeader) myRegisterResponse.getHeader("To");
        myToHeader.setTag("454326");
       
		myRegisterServerTransaction.sendResponse(myRegisterResponse);
		myGUI.display(">>> "+myRegisterResponse.toString());
		
		
	} catch (ParseException | InvalidArgumentException | SipException e) {
		// TODO Auto-generated catch block
		logger.error("Exception in REGISTER", e);
		e.printStackTrace();
	}
	  return; 
	  
  }
  
  if (method.equals("OPTIONS")){
	  ViaHeader viaHeader=(ViaHeader)myRequest.getHeader(ViaHeader.NAME);
	  String receivedIP=viaHeader.getHost();
	  try {
		ServerTransaction myOptionsServerTransaction=mySipProvider.getNewServerTransaction(myRequest);
		Response myOptionsResponse=myMessageFactory.createResponse(200, myRequest);
		((ViaHeader)myOptionsResponse.getHeader(ViaHeader.NAME)).setReceived(receivedIP);
		ToHeader myToHeader = (ToHeader) myOptionsResponse.getHeader("To");
        myToHeader.setTag("454326");
		myOptionsServerTransaction.sendResponse(myOptionsResponse);
		
		myGUI.display(">>> "+myOptionsResponse.toString());
		
	} catch (SipException | InvalidArgumentException| ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  return;
	  
  }
  String myCallID=((CallIdHeader)myRequest.getHeader(CallIdHeader.NAME)).getCallId();
  
  if (myCallIDExists(myCallID)){
	//TODO:handle as Proxy existing Transaction (Re-Invite, CANCEL, BYE, ACK)
	  logger.info("Request:"+method+" for existing CallID:"+myCallID);
	  try{
	  if (method.equals("INVITE")){ //Re-INVITE
		  sendProxyReInvite(requestReceivedEvent,myCallID);
		
		  
	  } else if (method.equals("BYE")|| method.equals("INFO") || method.equals("UPDATE") || method.equals("MESSAGE")){		  
		  sendProxyBye(requestReceivedEvent,myCallID);
		      
	  } else if (method.equals("CANCEL")){
		  sendProxyCancel(requestReceivedEvent,myCallID);
		
	  } else if (method.equals("ACK")) {
		  sendProxyACK(requestReceivedEvent,myCallID);
		
		  
	  }else {//Unimplemented Process Request
		  logger.warn("Unimplemented Request" );
	  }
	  } catch (SipException | ParseException | InvalidArgumentException e) {
			logger.error("Exception in Request:"+method+" for existing CallID:"+myCallID, e);
			e.printStackTrace();
		}
	  	  
  }else {//Handle for Not existing CallId in TransactionMap
	  logger.info("Request:"+method+" for CallID:"+myCallID+" is not existing in TransactionMap");
	  String userPartForSearch=null;
	  FromHeader fromHeader=(FromHeader)myRequest.getHeader(FromHeader.NAME);
	  Address fromAddress=fromHeader.getAddress();
	  SipUri uriFrom= (SipUri)fromHeader.getAddress().getURI();
	  String userPartFrom=uriFrom.getUser();
	  userPartForSearch=userPartFrom;
	  ContactHeader contactHeader=(ContactHeader)myRequest.getHeader(ContactHeader.NAME);
	  SipUri uriContact=null;
	  String userPartContact=null;
	  if (contactHeader!=null){//BYE and ACK message is not including Contact Header!!!!  
		  
		  uriContact= (SipUri)contactHeader.getAddress().getURI();
		  userPartContact=uriContact.getUser();
		  logger.info("Contact Header exists for CallID: "+myCallID+"   -->Contact Header:"+uriContact.toString());
		  userPartForSearch=userPartContact;
	  } else {
		  logger.warn("Contact Header Not found for CallID: "+myCallID);
	  }
	  logger.info("Read Contact Header");
	  ToHeader toHeader=(ToHeader)myRequest.getHeader(ToHeader.NAME);
	  SipUri uriTo= (SipUri)toHeader.getAddress().getURI();
	  
	  String userPartTo=uriTo.getUser();
	  logger.info("To Header: "+uriTo.toString()+" with User part: "+ userPartTo);
	  //TODO: check if userPartContact is belonging to known Registered users. If not then call from OSBiz
	  if (isRegisteredUser(userPartForSearch)){
		  //Call comes from Registered users
		  		  
		  if (isRegisteredUser(userPartTo)){
			  //Call goes to Registered user
			  //TODO:handle as Proxy call towards another Reistered user
			  logger.info("Call comes from Registered user: "+ userPartFrom+" towards other Reistered user:"+userPartTo);
			  logger.warn("Not Implemented functionality");
			  
		  }else {
			  //Call goes to OSBiz 
			  //TODO:handle as Proxy call towards OSBiz
			  logger.info("Call comes from Registered user: "+ userPartFrom+" towards OSBiz: "+userPartTo);
			  if (method.equals("INVITE")){//Don't handle other Requests sent first time like OPTIONS, NOTIFY, REFER, UPDATE
				  Object myObj=myGUI.myRegisteredDevices.get(userPartForSearch);
				  if (myObj!=null){
					  ((RegisteredDevice) myObj).setUsedStatus(true);
					  myGUI.refreshRegisteredDeviceWnd();
				  }
				  LegTransaction myServerLegTransaction=new LegTransaction(myCallID);
				  myServerLegTransaction.setNumber(userPartFrom);
				  myServerLegTransaction.setState(PROXY_IDLE);
				  myServerLegTransaction.setIpSignaling(uriContact.getHost());			
				  				  
				  myTransactionMap.put(myCallID, myServerLegTransaction);
				  
				  LegTransaction myClientLegTransaction=new LegTransaction();
				  myClientLegTransaction.setIpSignaling(myGUI.getRemoteIP());
				  myClientLegTransaction.setPortSignaling(myGUI.getRemotePort());
				  myClientLegTransaction.setNumber(userPartTo);
				  
				  ServerTransaction myServerProxyTransaction = requestReceivedEvent.getServerTransaction();
				  
				  try {
					myServerLegTransaction.setServerTransaction(myServerProxyTransaction);
					sendProxyInvite(requestReceivedEvent,myServerLegTransaction,myClientLegTransaction,false);
				} catch (ParseException | InvalidArgumentException | SipException e) {
					logger.error("Exception in Request:"+method+" for Not existing CallID in TransactionMap CallID:"+myCallID, e);
					e.printStackTrace();
				}
				  
			  }
		  }
	  } else {
		  //Call comes from OSBiz
		  if (isRegisteredUser(userPartTo)){
			  //Call goes to Registered user
			  //TODO:handle as Proxy call towards  Registered user
			  logger.info("Call comes from OSBiz user: "+ userPartFrom+" towards Registered user: "+userPartTo);
			  if (method.equals("INVITE")){
				  Object myObj=myGUI.myRegisteredDevices.get(userPartTo);
				  if (myObj!=null){
					  ((RegisteredDevice) myObj).setUsedStatus(true);
					  myGUI.refreshRegisteredDeviceWnd();
				  }
				  LegTransaction myServerLegTransaction=new LegTransaction(myCallID);
				  myServerLegTransaction.setNumber(userPartFrom);
				  myServerLegTransaction.setState(PROXY_IDLE);
				  myServerLegTransaction.setIpSignaling(uriContact.getHost());
				  myServerLegTransaction.setServerTransaction(requestReceivedEvent.getServerTransaction());
				  
				  myTransactionMap.put(myCallID, myServerLegTransaction);
				  
				  LegTransaction myClientLegTransaction=new LegTransaction();
				  UserData myPartner=myRegisteredUsers.get(userPartTo);
				  myClientLegTransaction.setUserData(myPartner);
				  try {
					sendProxyInvite(requestReceivedEvent,myServerLegTransaction,myClientLegTransaction,false);
				} catch (ParseException | InvalidArgumentException | SipException e) {
					logger.error("Exception in Request:"+method+" for Not existing CallID in TransactionMap CallID:"+myCallID, e);
					e.printStackTrace();
				}
				  
			  }
			  
			  
		  }else {
			  //Call goes to myITSP tool
			  //TODO:handle as normal call towards myITSP tool
			  logger.info("Request for myITSP Tool: "+method);
			  if (!method.equals("CANCEL")) {
				  myServerTransaction=requestReceivedEvent.getServerTransaction();
				  }

				  try{
					  logger.info("processRequest: Status="+status+" Method="+method);
				  switch (status) {

				    case IDLE:
				      if (method.equals("INVITE")) {
				        if (myServerTransaction == null) {
				                myServerTransaction = mySipProvider.getNewServerTransaction(myRequest);
				        }				        
				        
				        myAlertTool.playTone();				        
				        isSendSDP183=false;
				        byte[] cont=(byte[]) myRequest.getContent();
				        offerInfo=mySdpManager.getSdp(cont);

				        answerInfo.IpAddress=myIP;
				        answerInfo.aport=myAudioPort;
				        myOldAudioPort=myAudioPort;
				        myAudioPort=myAudioPort+2; //next call will use different ports
				        //answerInfo.findProperCodec(offerInfo,true);
				        //TODO: special handle in case codec is not existing =999
				        //myAudioCodec=answerInfo.aformat;
				        //myOldAudioCodec=answerInfo.aformat;
				        //remoteOldAudioCodec=myOldAudioCodec;
				        /*
				        if (offerInfo.isAudioCodecAvailable(myAudioCodec)){
				        	logger.info("myAudioCodec="+myAudioCodec+" exists in the list of requested INVITE");
				        } else {
				        	logger.warn("myAudioCodec="+myAudioCodec+" NOT exist in the list of requested INVITE");
				        }
				        */
				        if (offerInfo.vport==-1) {
				          answerInfo.vport=-1;
				        }
				        else if (myVideoPort==-1) {
				          answerInfo.vport=0;
				          answerInfo.vformat=offerInfo.vformat;
				        }
				        else {
				          answerInfo.vport=myVideoPort;
				          answerInfo.vformat=offerInfo.vformat;
				        }
				        
				        Response myResponse;
				        if (!(myGUI.SIPRespInfo.Resp180.getIsAvoided())){ //Send 180=yes
				        	myResponse=myMessageFactory.createResponse(180,myRequest);
				        	setAdditionalHeadersResponse(myResponse, myGUI.SIPRespInfo.Resp180.getHeaderValuesList());				        	
				        	if (myGUI.SIPRespInfo.Resp180.getSendSDP()){
				        		myAlertTool.stopTone();
					        	//createSDPResponse(myResponse);
					        	isSendSDP183=true;
					        	logger.info("Send 180 with SDP");
					        } else {
					        	
					        }logger.info("Send 180 without SDP");
					        myResponse.addHeader(myContactHeader);
					        //TODO:Perhaps this control is wrong. 180 Ringing doesn't need PAI, does it?
					        if (myGUI.SIPRespInfo.Resp180.getCOLP()){
					       	 myResponse.addHeader(myPAIHeader);
					        }
					        ToHeader myToHeader = (ToHeader) myResponse.getHeader("To");
					        myToHeader.setTag("454326");
					        if (isSendSDP183){
					        	createSDPResponse(myResponse);
					        }
					        myServerTransaction.sendResponse(myResponse);
					        myDialog=myServerTransaction.getDialog();
					        myGUI.display(">>> "+myResponse.toString());
				        	
				        } 
				        
				        status=RINGING;
				        myGUI.showStatus("Status: RINGING");
				        myGUI.setButtonStatusAnswerCall();
				        myGUI.setButtonStatusSend183();
				        setTxtLines(myRequest);
				        
				      }else if (method.equals("NOTIFY")){
				    	  if (myCircuitServerTransaction == null) {
				    		  myCircuitServerTransaction = mySipProvider.getNewServerTransaction(myRequest);
				        }	
				    	  handleNotifyRequest(myRequest);
				      }
				     break;
				    case ESTABLISHED:
				      if (method.equals("BYE")) {
				        Response myResponse=myMessageFactory.createResponse(200,myRequest);
				        //myResponse.addHeader(myContactHeader);
				        myServerTransaction.sendResponse(myResponse);
				        myGUI.display(">>> "+myResponse.toString());

				       //myVoiceTool.stopMedia();
				        if (isOnlyAnnouncment){
				        	myGVoiceTool.stopMedia();
				        }else {
				        	myGAnnouncementTool.stopMedia();
				        }
				       

				        if (answerInfo.vport>0) {
				          myVideoTool.stopMedia();
				        }

				        status=IDLE;
				        myGUI.showStatus("Status: IDLE");
				        myGUI.showCodec("");
				        myGUI.setButtonStatusIdle();
				        myGUI.setTxtLine(SIPHeadersTxt.ResetLines, "");
				      }else if (method.equals("INVITE")){ //Re-Invite handling
				    	  // it may occur that my myServerTransaction is not created yet (e.g call is initiated by tool first)
				    	  if (myServerTransaction == null) {
				              myServerTransaction = mySipProvider.getNewServerTransaction(myRequest);
				    	  }
				    	  answerToReInvite(myRequest) ;
				    	  status=RE_INVITE_WAIT_ACK;
				    	  myGUI.showStatus("Status: RE_INVITE_WAIT_ACK");
				      } else if (method.equals("NOTIFY")){
				    	  if (myCircuitServerTransaction == null) {
				    		  myCircuitServerTransaction = mySipProvider.getNewServerTransaction(myRequest);
				        }	
				    	  handleNotifyRequest(myRequest);
				      }
				      
				      
				    break;

				    case RINGING:
				      if (method.equals("CANCEL")) {
				        ServerTransaction myCancelServerTransaction=requestReceivedEvent.getServerTransaction();
				        Request originalRequest=myServerTransaction.getRequest();
				        Response myResponse=myMessageFactory.createResponse(487,originalRequest);
				        myServerTransaction.sendResponse(myResponse);
				        Response myCancelResponse=myMessageFactory.createResponse(200,myRequest);
				        myCancelServerTransaction.sendResponse(myCancelResponse);
				        
				        ///!!!!Warning!!!! The order of above messages are possibly wrong.First 200 OK (reply to CANCEL) and then 487 which is replied with ACK. 

				        //myAlertTool.stopTone();
				        if (isSendSDP183){
				           	  myGAnnouncementTool.stopMedia(); 
				           	  myGUI.showCodec("");
				        } else {
				        	myAlertTool.stopTone();
				        }

				        myGUI.display(">>> "+myResponse.toString());
				        myGUI.display(">>> "+myCancelResponse.toString());

				        status=IDLE;
				        myGUI.showStatus("Status: IDLE");
				        myGUI.setButtonStatusIdle();
				        myGUI.setTxtLine(SIPHeadersTxt.ResetLines, "");
				      }else if (method.equals("NOTIFY")){
				    	  handleNotifyRequest(myRequest);
				      }
				      break;

				      case WAIT_ACK:
				        if (method.equals("ACK")) {
				        status=ESTABLISHED;
				        myGUI.showStatus("Status: ESTABLISHED");
				        myGUI.setButtonStatusEstablishedCall();
				      }else if (method.equals("NOTIFY")){
				    	  if (myCircuitServerTransaction == null) {
				    		  myCircuitServerTransaction = mySipProvider.getNewServerTransaction(myRequest);
				        }	
				    	  handleNotifyRequest(myRequest);
				      }
				        break;
				      
				      case RE_INVITE_WAIT_ACK:
				    	if (method.equals("ACK")) { //ACK from Re-Invite sent from System Under Test
				           status=ESTABLISHED;
				           
				           byte[] cont=(byte[]) myRequest.getContent(); 
				           if (cont==null){
				        	   logger.info("ACK w/o SDP is received");  
				           }else {
				        	   logger.info("ACK with SDP is received");
				        	   answerInfo=mySdpManager.getSdp(cont);
				        	   offerInfo.findProperCodec(answerInfo);
				        	   /*
				        	   if (answerInfo.isAudioCodecAvailable(myOldAudioCodec)){
				        	        	logger.info("myAudioCodec="+myOldAudioCodec+" exists in the list of requested 200 OK");
				        	   } else {
				        	        	logger.warn("myAudioCodec="+myOldAudioCodec+" NOT exist in the list of requested 200 OK");
				        	   }
				        	   */
				        	   if (remoteOldAudioPort==answerInfo.aport && remoteOldAudioCodec==offerInfo.aformat){
				               	//Do nothing. It's not necessary to re-initiate RTP stream
				               			logger.trace("Same remote Audio Port and Audio codec for response to 200 OK. Not necessary to restart RTP");
				               }
				               else{
				               			logger.trace("Different remote Audio Port OR  Audio codec for response to 200 OK. Necessary to restart RTP");
				               			//Stop old RTP media
				               			if (isOnlyAnnouncment){
				               					myGVoiceTool.stopMedia();
				               			}else {
				               					myGAnnouncementTool.stopMedia();
				               			}
				               			myGUI.showCodec("");
				               			myAudioPort=myAudioPort+2;
				               			remoteOldAudioPort=answerInfo.aport;
				               			remoteOldAudioCodec=offerInfo.aformat;
				               			myOldAudioPort=answerInfo.aport;
				               			//Start new RTP media
				               			//handle the best codec to use 
				               			Thread.sleep(1000);//needed because the previous gstreamer threads are not terminated properly
				               			if (isOnlyAnnouncment){
				               					myGVoiceTool.startMedia(answerInfo.IpAddress, answerInfo.aport, offerInfo.aport,offerInfo.aport );
				               			} else {
				               					myGAnnouncementTool.startMedia(answerInfo.IpAddress, answerInfo.aport, offerInfo.aport,offerInfo.aport );
				               			}     

				               			if (answerInfo.vport>0) {
				               					myVideoTool.startMedia(answerInfo.IpAddress,answerInfo.vport,offerInfo.vport,myVideoCodec);
				               			}
				               			myGUI.showCodec(offerInfo.getAudioCodecString());
				               	
				               }
				         	  } 
				           myGUI.showStatus("Status: ESTABLISHED"); 
				           
				          
				              
				        }else if (method.equals("NOTIFY")){
				        	if (myCircuitServerTransaction == null) {
					    		  myCircuitServerTransaction = mySipProvider.getNewServerTransaction(myRequest);
					        }	
					    	  handleNotifyRequest(myRequest);
					      }  

				  }

				  }catch (Exception e) {
					  logger.warn("Exception: my ITSP tool handle of method: "+method+" for CallID: "+myCallID, e);
				    e.printStackTrace();
				  }
		  }
		  
	  }
	  
  }
  
}





private void runFailoverMode(RequestEvent requestReceivedEvent) throws ParseException, SipException, InvalidArgumentException {
	// TODO Auto-generated method stub
	Request myRequest=requestReceivedEvent.getRequest();
	myGUI.display("<<<<<<<<<< "+myRequest.toString());
	ViaHeader viaHeader=(ViaHeader)myRequest.getHeader(ViaHeader.NAME);
	 String receivedIP=viaHeader.getHost();
	 ServerTransaction myFailoverServerTransaction=mySipProvider.getNewServerTransaction(myRequest);
	Response myFailoverResponse=myMessageFactory.createResponse(myGUI.myFailoverMode.failoverHeader, myRequest);
	RetryAfterHeader myRetryAfterHeader=myHeaderFactory.createRetryAfterHeader(Integer.parseInt(myGUI.myFailoverMode.retryAfter));
	if (myGUI.myFailoverMode.hasRetryAfter){
		myFailoverResponse.addHeader(myRetryAfterHeader);
	}
	((ViaHeader)myFailoverResponse.getHeader(ViaHeader.NAME)).setReceived(receivedIP);
	
	ToHeader myToHeader = (ToHeader) myFailoverResponse.getHeader("To");
    myToHeader.setTag("454326");
   
    myFailoverServerTransaction.sendResponse(myFailoverResponse);
	myGUI.display(">>> "+myFailoverResponse.toString());
	
}


private void sendProxyCancel(RequestEvent requestReceivedEvent, String myCallID) throws ParseException, SipException, InvalidArgumentException {
	Request myProxyServerRequest = requestReceivedEvent.getRequest();
	ServerTransaction myServerProxyTransaction = requestReceivedEvent.getServerTransaction();   
	SipProvider sipProxyProvider = (SipProvider) requestReceivedEvent.getSource();
	Dialog myServerProxyDialog = requestReceivedEvent.getDialog();  
		  
	LegTransaction myServerLegTransaction=myTransactionMap.get(myCallID);
	
	FromHeader fromHeader=(FromHeader)myProxyServerRequest.getHeader(FromHeader.NAME);
	  SipUri uriFrom= (SipUri)fromHeader.getAddress().getURI();
	  String userPartFrom=uriFrom.getUser();
	  String senderOfBye=userPartFrom;
	  if (isRegisteredUser(senderOfBye)){
		  //CANCEL is send by Registered User
		  logger.info(myProxyServerRequest.getMethod()+" is sent by Registred User: "+senderOfBye+" towards OSBiz user: "+myServerLegTransaction.getPartnerLeg().getNumber());
	  } else {
		  //CANCEL is sent by OSBiz
		  logger.info(myProxyServerRequest.getMethod()+" is sent by OSBiz User: "+senderOfBye+" towards Registered user: "+myServerLegTransaction.getPartnerLeg().getNumber());
	  }
	  Response myProxyServerResponse=myMessageFactory.createResponse(200,myProxyServerRequest);
	  myServerProxyTransaction.sendResponse(myProxyServerResponse);
	  myGUI.display(">>> "+myProxyServerResponse.toString());
	  
	  Dialog myClientProxyDialog = (Dialog) myServerProxyDialog.getApplicationData();
	  //Request myClientProxyRequest = myClientProxyDialog.createRequest(myProxyServerRequest.getMethod());
	  //When myProxyServerRequest.getMethod() is called, its most probably null because the dialog is finalized 
	  // due to previous Transaction Response
	  Request myClientProxyRequest = myClientProxyDialog.createRequest("CANCEL");
	  ClientTransaction myClientProxyTransaction = sipProxyProvider.getNewClientTransaction(myClientProxyRequest);
	  myClientProxyTransaction.setApplicationData(myServerProxyTransaction);
	  myServerProxyTransaction.setApplicationData(myClientProxyTransaction);
	  myClientProxyDialog.sendRequest(myClientProxyTransaction);
	  myGUI.display(">>> " + myClientProxyRequest.toString());
	  
	  ServerTransaction originalServerTransaction=myServerLegTransaction.getServerTransaction();
	  Request originalServerProxyRequest=originalServerTransaction.getRequest();
	  Response my487Response=myMessageFactory.createResponse(487,originalServerProxyRequest);
      originalServerTransaction.sendResponse(my487Response);
      myGUI.display(">>> "+my487Response.toString());
	  myServerLegTransaction.setState(PROXY_IDLE);
      myTransactionMap.remove(myCallID);
	  
	  LegTransaction myPartnerLegTransaction=myServerLegTransaction.getPartnerLeg();
	  
      
      myServerLegTransaction.setServerTransaction(myServerProxyTransaction);
      //Don't remove PartnerLegTransaction, because 200OK is expected. 
      //But set the state to a correct value=PROXY_IDLE
      myPartnerLegTransaction.setState(PROXY_IDLE);
      myPartnerLegTransaction.setClientTransaction(myClientProxyTransaction);
	  
	  
	
}


private void sendProxyReInvite(RequestEvent requestReceivedEvent, String myCallID) throws SipException, ParseException, InvalidArgumentException {
	  Request myProxyServerRequest=requestReceivedEvent.getRequest();
	  LegTransaction myServerLegTransaction=myTransactionMap.get(myCallID);
	  LegTransaction myPartnerLegTransaction=myServerLegTransaction.getPartnerLeg();
	  
	  ContactHeader contactHeader=(ContactHeader)myProxyServerRequest.getHeader(ContactHeader.NAME);
	  SipUri uriContact= (SipUri)contactHeader.getAddress().getURI();
	  String userPartContact=uriContact.getUser();
	  String senderOfReInvite=userPartContact;
	  if (isRegisteredUser(senderOfReInvite)){
		  //ReInvite is send by Registered User
		  logger.info("ReInvite is sent by Registred User: "+senderOfReInvite+" towards OSBiz user: "+myServerLegTransaction.getPartnerLeg().getNumber());
	  } else {
		  //ReInvite is sent by OSBiz
		  logger.info("ReInvite is sent by OSBiz User: "+senderOfReInvite+" towards Registered user: "+myServerLegTransaction.getPartnerLeg().getNumber());
	  }
	  
	  sendProxyInvite(requestReceivedEvent,myServerLegTransaction,myPartnerLegTransaction, true);
	  
	
}


private void sendProxyACK(RequestEvent requestReceivedEvent, String myCallID) throws InvalidArgumentException, SipException, ParseException {
	ServerTransaction myServerProxyTransaction = requestReceivedEvent.getServerTransaction();  
	Request myProxyServerRequest=requestReceivedEvent.getRequest();
	LegTransaction myServerLegTransaction=myTransactionMap.get(myCallID);
	  /*Contact Header is not existing in ACK
	  ContactHeader contactHeader=(ContactHeader)myProxyServerRequest.getHeader(ContactHeader.NAME);
	  
	  SipUri uriContact= (SipUri) contactHeader.getAddress().getURI();
	  String userPartContact=uriContact.getUser();
	  */
	Dialog myServerProxyDialog = myServerProxyTransaction.getDialog();
	Dialog myClientProxyDialog = (Dialog) myServerProxyDialog.getApplicationData();
	//Warning: Dialog keeps the original CSeq (from the Original Invite)
	//In case of Re-Invite, the CSeq is changed but getLocalSeqNumber() returns always the original Cseq
	//Its better to use th Partner's Leg and for this one to get the CSeq.
	 
	/*
	 * Long myCSeq=myClientProxyDialog.getLocalSeqNumber();
	
    */
	long myCSeq=myServerLegTransaction.getPartnerLeg().getNumSeq();
	 Request myClientProxyRequest = myClientProxyDialog.createAck(myCSeq);
	
	byte[] requestSDP=(byte[]) (requestReceivedEvent.getRequest()).getContent();
	  if (requestSDP==null){
		  //ACK w/o SDP
	  }else {
		  //ACK with SDP
		  ContentTypeHeader contentTypeHeader=myHeaderFactory.createContentTypeHeader("application","sdp");;
		  myClientProxyRequest.setContent(requestSDP,contentTypeHeader);
	  }
	myClientProxyDialog.sendAck(myClientProxyRequest);
	myGUI.display(">>> "+myClientProxyRequest.toString()); 
	  FromHeader fromHeader=(FromHeader)myProxyServerRequest.getHeader(FromHeader.NAME);
	  SipUri uriFrom= (SipUri) fromHeader.getAddress().getURI();
	  String userPartFrom=uriFrom.getUser();
	  String senderOfAck=userPartFrom;
	  if (isRegisteredUser(senderOfAck)){
		  //ACK is send by Registered User
		  logger.info("ACK is sent by Registred User: "+senderOfAck+" towards OSBiz user: "+myServerLegTransaction.getPartnerLeg().getNumber());
	  } else {
		  //ACK is sent by OSBiz
		  logger.info("ACK is sent by OSBiz User: "+senderOfAck+" towards Registered user: "+myServerLegTransaction.getPartnerLeg().getNumber());
	  }

      
	
}


private void sendProxyBye(RequestEvent requestReceivedEvent, String myCallID) throws ParseException, SipException, InvalidArgumentException {
	Request myProxyServerRequest = requestReceivedEvent.getRequest();
	ServerTransaction myServerProxyTransaction = requestReceivedEvent.getServerTransaction();   
	SipProvider sipProxyProvider = (SipProvider) requestReceivedEvent.getSource();
	Dialog myServerProxyDialog = requestReceivedEvent.getDialog();  
		  
	LegTransaction myServerLegTransaction=myTransactionMap.get(myCallID);
	  //ContactHeader is not existing in BYE. Look for From Header, although quite unsafe If OSbiz uses From with Registered user content (e.g Forward
	  
	  
	  FromHeader fromHeader=(FromHeader)myProxyServerRequest.getHeader(FromHeader.NAME);
	  SipUri uriFrom= (SipUri)fromHeader.getAddress().getURI();
	  String userPartFrom=uriFrom.getUser();
	  String senderOfBye=userPartFrom;
	  if (isRegisteredUser(senderOfBye)){
		  //BYE is send by Registered User
		  logger.info(myProxyServerRequest.getMethod()+" is sent by Registred User: "+senderOfBye+" towards OSBiz user: "+myServerLegTransaction.getPartnerLeg().getNumber());
	  } else {
		  //BYE is sent by OSBiz
		  logger.info(myProxyServerRequest.getMethod()+" is sent by OSBiz User: "+senderOfBye+" towards Registered user: "+myServerLegTransaction.getPartnerLeg().getNumber());
	  }
	  Response myProxyServerResponse=myMessageFactory.createResponse(200,myProxyServerRequest);
	  myServerProxyTransaction.sendResponse(myProxyServerResponse);
	  myGUI.display(">>> "+myProxyServerResponse.toString());
	  
	  Dialog myClientProxyDialog = (Dialog) myServerProxyDialog.getApplicationData();
	  Request myClientProxyRequest = myClientProxyDialog.createRequest(myProxyServerRequest.getMethod());
	  ClientTransaction myClientProxyTransaction = sipProxyProvider.getNewClientTransaction(myClientProxyRequest);
	  myClientProxyTransaction.setApplicationData(myServerProxyTransaction);
	  myServerProxyTransaction.setApplicationData(myClientProxyTransaction);
	  myClientProxyDialog.sendRequest(myClientProxyTransaction);
	  
	  
	  LegTransaction myPartnerLegTransaction=myServerLegTransaction.getPartnerLeg();
	  
      myGUI.display(">>> " + myClientProxyRequest.toString());
      myServerLegTransaction.setServerTransaction(myServerProxyTransaction);
      myServerLegTransaction.setState(PROXY_IDLE);
      myTransactionMap.remove(myCallID);
      //Don't remove PartnerLegTransaction, because 200OK is expected. 
      //But set the state to a correct value=PROXY_IDLE
      myPartnerLegTransaction.setState(PROXY_IDLE);
      myPartnerLegTransaction.setClientTransaction(myClientProxyTransaction);
      
	
}


private void sendProxyInvite(RequestEvent requestReceivedEvent, LegTransaction myServerLegTransaction,
		LegTransaction myClientLegTransaction, boolean isReInvite) throws ParseException, InvalidArgumentException, SipException {
	// TODO Auto-generated method stub
	Request myProxyRequest=requestReceivedEvent.getRequest();
	
	/*
	ServerTransaction myInviteServerTransaction=requestReceivedEvent.getServerTransaction();
	Response myInviteResponse=myMessageFactory.createResponse(100, myProxyRequest);
	myInviteServerTransaction.sendResponse(myInviteResponse);
	myGUI.display(">>> "+myInviteResponse.toString());
	
	logger.info("Response 100 Trying for INVITE from User : "+myServerLegTransaction.getNumber());
	*/
	SipProvider sipProxyProvider=(SipProvider) requestReceivedEvent.getSource();
	ServerTransaction myProxyServerTransaction=requestReceivedEvent.getServerTransaction();
	if (myProxyServerTransaction==null){
		logger.info("New Server Transaction is created by sipProxyProvider");
		myProxyServerTransaction=sipProxyProvider.getNewServerTransaction(myProxyRequest);
		
	}
	Dialog myProxyServerDialog=myProxyServerTransaction.getDialog();
	
	byte[] requestSDP=(byte[]) (requestReceivedEvent.getRequest()).getContent();
	String partnerUri=null;
	if (isRegisteredUser(myClientLegTransaction.getNumber())){
		//Called User is one of the Registered. So get its data from UserData class
		UserData myPartner=myRegisteredUsers.get(myClientLegTransaction.getNumber());
		 partnerUri=myPartner.getUri().toString();
	}else {
		partnerUri="sip"+":"+myClientLegTransaction.getUserHostPort();
	}
	
	Address toAddress = myAddressFactory.createAddress(partnerUri);
	String toTag=null;
	if (isReInvite){
		Dialog partnerProxyDialog=(Dialog) myProxyServerDialog.getApplicationData();
		toTag=partnerProxyDialog.getRemoteTag();
		
	}
    ToHeader myToHeader = myHeaderFactory.createToHeader(toAddress, toTag);
    

    Address fromAddress=((FromHeader)myProxyRequest.getHeader(FromHeader.NAME)).getAddress();
    String fromTag= String.valueOf(INITIAL_VARIABLE+myClientLegTransaction.getID());
    FromHeader myFromHeader = myHeaderFactory.createFromHeader(
        fromAddress, fromTag);

    //myViaHeader = myHeaderFactory.createViaHeader(myIP, myPort,"udp", null);
    myViaHeader = myHeaderFactory.createViaHeader(myIP, myPort,myTransport, null);
    ArrayList<ViaHeader> myViaHeaders = new ArrayList<ViaHeader>();
    myViaHeaders.add(myViaHeader);
    MaxForwardsHeader myMaxForwardsHeader = myHeaderFactory.
        createMaxForwardsHeader(70);
    long cseq=INITIAL_BASE_VALUE+myClientLegTransaction.getID()+myClientLegTransaction.getSeq();
    CSeqHeader myCSeqHeader = myHeaderFactory.createCSeqHeader(cseq,
        "INVITE");
    CallIdHeader myCallIDHeader;
    if (isReInvite){
		myCallIDHeader=myClientLegTransaction.getCallIdHeader();
	} else {
		myCallIDHeader= mySipProvider.getNewCallId();
	}
     
    String clientTransactionCallID=myCallIDHeader.getCallId();
    
    myClientLegTransaction.setCallIdHeader(myCallIDHeader);
    myClientLegTransaction.setOwnCallID(clientTransactionCallID);
    myClientLegTransaction.setNumSeq(cseq);
    myClientLegTransaction.setPartnerLeg(myServerLegTransaction);
    myServerLegTransaction.setPartnerLeg(myClientLegTransaction); 
    
    myTransactionMap.put(clientTransactionCallID, myClientLegTransaction);
    
    javax.sip.address.URI myRequestURI = toAddress.getURI();
    
    Request myClientRequest = myMessageFactory.createRequest(myRequestURI,
        "INVITE",
        myCallIDHeader, myCSeqHeader, myFromHeader, myToHeader,
        myViaHeaders, myMaxForwardsHeader);
    if (requestSDP==null){//in case of Late SDP from originator, SDP should not be sent
    	
    }else {
    	ContentTypeHeader contentTypeHeader=myHeaderFactory.createContentTypeHeader("application","sdp");
    	myClientRequest.setContent(requestSDP, contentTypeHeader);
    }
    Address contactAddress = myAddressFactory.createAddress("sip:"+myServerLegTransaction.getNumber()+"@"+myIP+":"+myPort);
    ContactHeader myClientContactHeader = myHeaderFactory.createContactHeader(contactAddress);
    myClientRequest.addHeader(myClientContactHeader);
    if (myGUI.SIPReqInfo.ReqInvite.getHasUserAgent()){
    	myClientRequest.addHeader(myUserAgentHeader);
    }
        
    ClientTransaction myClientProxyTransaction = mySipProvider.getNewClientTransaction(myClientRequest);
    myClientLegTransaction.setClientTransaction(myClientProxyTransaction);
    myServerLegTransaction.setServerTransaction(myProxyServerTransaction);
    myServerLegTransaction.setCallIdHeader((CallIdHeader) myProxyRequest.getHeader(CallIdHeader.NAME));
    myClientProxyTransaction.sendRequest();
    myServerLegTransaction.setState(PROXY_WAIT);
    myClientLegTransaction.setState(PROXY_WAIT);
    myGUI.display(">>> " + myClientRequest.toString());	
    myClientProxyTransaction.setApplicationData(myProxyServerTransaction);
    myProxyServerTransaction.setApplicationData(myClientProxyTransaction);
    myProxyServerDialog.setApplicationData(myClientProxyTransaction.getDialog());
    myClientProxyTransaction.getDialog().setApplicationData(myProxyServerDialog);
	
}


private boolean isRegisteredUser(String userPart) {
	// TODO Auto-generated method stub
	if (myRegisteredUsers.isEmpty()) return false;
	return myRegisteredUsers.containsKey(userPart);
	
}


private boolean myCallIDExists(String myCallID) {
	// TODO Auto-generated method stub
	if (myTransactionMap.isEmpty()) return false;
	return myTransactionMap.containsKey(myCallID);
}


public void processResponse(ResponseEvent responseReceivedEvent) {
  try{
  Response myResponse=responseReceivedEvent.getResponse();
  myGUI.display("<<<<<<<<<< "+myResponse.toString());
  String myCallID=((CallIdHeader)myResponse.getHeader(CallIdHeader.NAME)).getCallId();
  int myStatusCode=myResponse.getStatusCode();
  
  if (myCallIDExists(myCallID)){
	  logger.info("Response from existing CallId: "+myCallID);
	  handleProxyResponse(responseReceivedEvent,myCallID);
	  
  } else {//Call is handled by myITSP tool
	  CSeqHeader originalCSeq=(CSeqHeader) myClientTransaction.getRequest().getHeader(CSeqHeader.NAME);
	  long numseq=originalCSeq.getSeqNumber();
	  ClientTransaction thisClientTransaction=responseReceivedEvent.getClientTransaction();
	  if (!thisClientTransaction.equals(myClientTransaction)) {
		  logger.warn("Not similar Client Transactions");
		  return;}
	  
	  logger.info("processResponse: Status="+status+" Response="+myStatusCode);
	switch(status){

	  case WAIT_PROV_LATE_SDP:
		  if (myStatusCode<200) {
		      status=WAIT_FINAL_LATE_SDP;
		      myDialog=thisClientTransaction.getDialog();
		      if (myGUI.myCallFeaturesInfo.activeFeature==Feature.ReInvite){
		    	  myGUI.showStatus("Status: Established");
		    	  myGUI.setButtonStatusEstablishedCall();
		      } else {
		    	  myRingTool.playTone();
			      myGUI.showStatus("Status: ALERTING");
			      myGUI.setButtonStatusMakeCall();
		      }
		      
		      
		    }
		  else if (myStatusCode<300) {
			  if (myGUI.myCallFeaturesInfo.activeFeature==Feature.ReInvite){
				  
			  }else {
				  myRingTool.stopTone();
			  }
			  
		      myDialog=thisClientTransaction.getDialog();
		      Request myAck = myDialog.createAck(numseq);
		      //myAck.addHeader(myContactHeader);
		      byte[] cont=(byte[]) myResponse.getContent();
		      offerInfo=mySdpManager.getSdp(cont);
		      
		      answerInfo=new SdpInfo();
		      answerInfo.IpAddress=myIP;
		      answerInfo.aport=myAudioPort;
	          myOldAudioPort=myAudioPort;
	          myAudioPort=myAudioPort+2;
	          answerInfo.vport=myVideoPort;
	          answerInfo.vformat=myVideoCodec;
		      answerInfo.setAudioFormatList(myCodecsList);
		      answerInfo.isDtmfFirst=myGUI.getDtmfFirstOrder();
		      //handle what is the best codec to use
		      answerInfo.findProperCodec(offerInfo, true);
		      myOldAudioCodec=answerInfo.aformat;
		      remoteOldAudioCodec=myOldAudioCodec;
		      remoteOldAudioPort=offerInfo.aport;
		      byte[] content=mySdpManager.createSdp(answerInfo,true);//answer only to the matched codec
		      ContentTypeHeader contentTypeHeader=myHeaderFactory.createContentTypeHeader("application","sdp");
		      myAck.setContent(content,contentTypeHeader);
		      if (isOnlyAnnouncment){
			     	 myGVoiceTool.startMedia(offerInfo.IpAddress, offerInfo.aport, answerInfo.aport,answerInfo.aformat );
			      } else {
			     	 myGAnnouncementTool.startMedia(offerInfo.IpAddress, offerInfo.aport, answerInfo.aport,answerInfo.aformat );
			      }
			      if (answerInfo.vport>0) {
			      myVideoTool.startMedia(offerInfo.IpAddress,offerInfo.vport,answerInfo.vport,myVideoCodec);
			      }
			  myGUI.showCodec(answerInfo.getAudioCodecString());
		      
		      myDialog.sendAck(myAck);
		      myGUI.display(">>> "+myAck.toString());     
		      status=ESTABLISHED;
		      myGUI.showStatus("Status: ESTABLISHED");
		      myGUI.setButtonStatusEstablishedCall();

		    } else if (myStatusCode==488){
		    	logger.info("Response: "+myStatusCode+". Re-INVITE w/o SDP will not take place. RTP negotiation remains as it was before");
				status=ESTABLISHED;
				myDialog=thisClientTransaction.getDialog();
			    Request myAckto488 = myDialog.createAck(numseq);
			    myGUI.display(">>> "+myAckto488.toString());		      
			    myGUI.showStatus("Status: ESTABLISHED");
		    } else if (myStatusCode==302){
		    	logger.info("Response: "+myStatusCode+". Moved Temporarily");
		    	status=IDLE;
		    	myRingTool.stopTone();
		    	setTxtLines(myResponse);
		    	myGUI.showStatus("Status: IDLE");
		    	myGUI.setButtonStatusIdle();
		    }
		  
		  
		    else {

		      status=IDLE;
		      Request myAck = myDialog.createAck(numseq);
		      //myAck.addHeader(myContactHeader);
		      myDialog.sendAck(myAck);
		      myRingTool.stopTone();
		      myGUI.display(">>> "+myAck.toString());
		      myGUI.showStatus("Status: IDLE");
		      myGUI.setButtonStatusIdle();

		    }
		    break;
	  
	  case WAIT_PROV:
	    if (myStatusCode<200) {
	      status=WAIT_FINAL;
	      myDialog=thisClientTransaction.getDialog();
	      myRingTool.playTone();
	      myGUI.showStatus("Status: ALERTING");
	      myGUI.setButtonStatusMakeCall();
	    }
	    else if (myStatusCode<300) {
	      myDialog=thisClientTransaction.getDialog();
	      Request myAck = myDialog.createAck(numseq);
	      //myAck.addHeader(myContactHeader);
	      myDialog.sendAck(myAck);
	      myGUI.display(">>> "+myAck.toString());
	      myRingTool.stopTone();
	      status=ESTABLISHED;
	      myGUI.showStatus("Status: ESTABLISHED");
	      myGUI.setButtonStatusEstablishedCall();

	      byte[] cont=(byte[]) myResponse.getContent();
	      answerInfo=mySdpManager.getSdp(cont);
	      remoteOldAudioPort=answerInfo.aport;
	      //handle what is the best codec to use
	      offerInfo.findProperCodec(answerInfo); 
	      myOldAudioCodec=answerInfo.aformat;
	      remoteOldAudioCodec=myOldAudioCodec;

	      //myVoiceTool.startMedia(answerInfo.IpAddress,answerInfo.aport,offerInfo.aport,answerInfo.aformat);
	      if (isOnlyAnnouncment){
	     	 myGVoiceTool.startMedia(answerInfo.IpAddress, answerInfo.aport, offerInfo.aport,offerInfo.aformat );
	      } else {
	     	 myGAnnouncementTool.startMedia(answerInfo.IpAddress, answerInfo.aport, offerInfo.aport,offerInfo.aformat );
	      }
	      if (answerInfo.vport>0) {
	      myVideoTool.startMedia(answerInfo.IpAddress,answerInfo.vport,offerInfo.vport,myVideoCodec);
	      }
	      myGUI.showCodec(offerInfo.getAudioCodecString());

	    } else if (myStatusCode==302){
	    	logger.info("Response: "+myStatusCode+". Moved Temporarily");
	    	status=IDLE;
	    	myRingTool.stopTone();
	    	setTxtLines(myResponse);
	    	myGUI.showStatus("Status: IDLE");
	    	myGUI.setButtonStatusIdle();
	    }
	    else {

	      status=IDLE;
	      Request myAck = myDialog.createAck(numseq);
	      //myAck.addHeader(myContactHeader);
	      myDialog.sendAck(myAck);
	      myRingTool.stopTone();
	      myGUI.display(">>> "+myAck.toString());
	      myGUI.showStatus("Status: IDLE");
	      myGUI.setButtonStatusIdle();

	    }
	    break;
	  case WAIT_FINAL_LATE_SDP:
		  if (myStatusCode<200) {
		      status=WAIT_FINAL_LATE_SDP;
		      myDialog=thisClientTransaction.getDialog();
		      myRingTool.playTone();
		      myGUI.showStatus("Status: ALERTING");
		      myGUI.setButtonStatusMakeCall();
		    }
		  else if (myStatusCode<300) {
			  myRingTool.stopTone();
		      myDialog=thisClientTransaction.getDialog();
		      Request myAck = myDialog.createAck(numseq);
		      //myAck.addHeader(myContactHeader);
		      byte[] cont=(byte[]) myResponse.getContent();
		      offerInfo=mySdpManager.getSdp(cont);
		      answerInfo=new SdpInfo();
		      answerInfo.IpAddress=myIP;
		      answerInfo.aport=myAudioPort;
	          myOldAudioPort=myAudioPort;
	          myAudioPort=myAudioPort+2;
	          answerInfo.vport=myVideoPort;
	          answerInfo.vformat=myVideoCodec;
		      answerInfo.setAudioFormatList(myCodecsList);
		      answerInfo.isDtmfFirst=myGUI.getDtmfFirstOrder();
		      //handle what is the best codec to use
		      answerInfo.findProperCodec(offerInfo, true);
		      myOldAudioCodec=answerInfo.aformat;
		      remoteOldAudioCodec=myOldAudioCodec;
		      remoteOldAudioPort=offerInfo.aport;
		      byte[] content=mySdpManager.createSdp(answerInfo,true);//answer only to the matched codec
		      ContentTypeHeader contentTypeHeader=myHeaderFactory.createContentTypeHeader("application","sdp");
		      myAck.setContent(content,contentTypeHeader);
		      if (isOnlyAnnouncment){
			     	 myGVoiceTool.startMedia(offerInfo.IpAddress, offerInfo.aport, answerInfo.aport,answerInfo.aformat );
			      } else {
			     	 myGAnnouncementTool.startMedia(offerInfo.IpAddress, offerInfo.aport, answerInfo.aport,answerInfo.aformat );
			      }
			      if (answerInfo.vport>0) {
			      myVideoTool.startMedia(offerInfo.IpAddress,offerInfo.vport,answerInfo.vport,myVideoCodec);
			      }
			  myGUI.showCodec(answerInfo.getAudioCodecString());
		      
		      myDialog.sendAck(myAck);
		      myGUI.display(">>> "+myAck.toString());     
		      status=ESTABLISHED;
		      myGUI.showStatus("Status: ESTABLISHED");
		      myGUI.setButtonStatusEstablishedCall();

		    }
		    else {

		      myRingTool.stopTone();
		      status=IDLE;
		      myGUI.showStatus("Status: IDLE");
		      myGUI.setButtonStatusIdle();
		    }
		    break;
		  
	  case WAIT_FINAL:
	    if (myStatusCode<200) {
	      status=WAIT_FINAL;
	      myDialog=thisClientTransaction.getDialog();
	      myRingTool.playTone();
	      myGUI.showStatus("Status: ALERTING");
	      myGUI.setButtonStatusMakeCall();
	    }
	    else if (myStatusCode<300) {
	      status=ESTABLISHED;
	      myDialog=thisClientTransaction.getDialog();
	      Request myAck = myDialog.createAck(numseq);
	      //myAck.addHeader(myContactHeader);
	      myDialog.sendAck(myAck);
	      myGUI.display(">>> "+myAck.toString());
	      myRingTool.stopTone();
	      myGUI.showStatus("Status: ESTABLISHED");
	      myGUI.setButtonStatusEstablishedCall();


	      byte[] cont=(byte[]) myResponse.getContent();
	      answerInfo=mySdpManager.getSdp(cont);
	      offerInfo.findProperCodec(answerInfo);  
	      
	      remoteOldAudioPort=answerInfo.aport;
	      //handle the best codec to use
	      myOldAudioCodec=answerInfo.aformat;
	      remoteOldAudioCodec=myOldAudioCodec;
	        //myVoiceTool.startMedia(answerInfo.IpAddress,answerInfo.aport,offerInfo.aport,answerInfo.aformat);
	      if (isOnlyAnnouncment){
	     	 myGVoiceTool.startMedia(answerInfo.IpAddress, answerInfo.aport, offerInfo.aport,offerInfo.aformat );
	      } else {
	     	 myGAnnouncementTool.startMedia(answerInfo.IpAddress, answerInfo.aport, offerInfo.aport,offerInfo.aformat );
	      }
	        logger.debug("Listen RTP at port:"+offerInfo.aport);

	        if (answerInfo.vport>0) {
	          myVideoTool.startMedia(answerInfo.IpAddress,answerInfo.vport,offerInfo.vport,myVideoCodec);
	        }
	        myGUI.showCodec(offerInfo.getAudioCodecString());

	    }
	    else {

	      myRingTool.stopTone();
	      status=IDLE;
	      myGUI.showStatus("Status: IDLE");
	      myGUI.setButtonStatusIdle();
	    }
	    break;
	  case RE_INVITE_WAIT_ACK:
		  if (myStatusCode<200) {
		      myDialog=thisClientTransaction.getDialog();
		    }else if (myStatusCode<300) {
			  status=ESTABLISHED;
			  myDialog=thisClientTransaction.getDialog();
		      Request myAck = myDialog.createAck(numseq);
		      //myAck.addHeader(myContactHeader);
		      
		      myGUI.showStatus("Status: ESTABLISHED");
		      byte[] cont=(byte[]) myResponse.getContent();
		      answerInfo=mySdpManager.getSdp(cont);
		      offerInfo.findProperCodec(answerInfo);        
		      remoteOldAudioPort=answerInfo.aport;
		      //handle the best codec to use
		      myOldAudioCodec=answerInfo.aformat;
		      remoteOldAudioCodec=myOldAudioCodec;
		      if (isOnlyAnnouncment){
	          	myGVoiceTool.stopMedia();
	          }else {
	          	myGAnnouncementTool.stopMedia();
	          }
		      myGUI.showCodec("");
		      Thread.sleep(1000);
		      if (isOnlyAnnouncment){
		      	 myGVoiceTool.startMedia(answerInfo.IpAddress, answerInfo.aport, offerInfo.aport,offerInfo.aformat );
		       } else {
		      	 myGAnnouncementTool.startMedia(answerInfo.IpAddress, answerInfo.aport, offerInfo.aport,offerInfo.aformat );
		       }
		         logger.debug("Listen RTP at port:"+offerInfo.aport);

		         if (answerInfo.vport>0) {
		           myVideoTool.startMedia(answerInfo.IpAddress,answerInfo.vport,offerInfo.vport,myVideoCodec);
		         }
		         myDialog.sendAck(myAck);
			     myGUI.display(">>> "+myAck.toString());
			     myGUI.showCodec(offerInfo.getAudioCodecString());
			   
		  } else if (myStatusCode==488){
			  logger.info("Response: "+myStatusCode+". Re-INVITE with SDP will not take place. RTP negotiation remains as it was before");
			  status=ESTABLISHED;
			  myDialog=thisClientTransaction.getDialog();
		      Request myAckto488 = myDialog.createAck(numseq);
		      myGUI.display(">>> "+myAckto488.toString());		      
		      myGUI.showStatus("Status: ESTABLISHED");
		  } else {
			  logger.info("Response: "+myStatusCode+" is  handled by myITSP Tool like response for 488");
			  status=ESTABLISHED;
			  myDialog=thisClientTransaction.getDialog();
		      Request myAckto488 = myDialog.createAck(numseq);
		      myGUI.display(">>> "+myAckto488.toString());		      
		      myGUI.showStatus("Status: ESTABLISHED");
		  }
		  break;
	  case WAIT_OPTIONS_PROV:
		  if (myStatusCode<300) {
			  logger.info("OPTIONS Response: "+myStatusCode);
			  
			  status=previousStatus;
			  myGUI.showStatus("Status: IDLE");
		  }
		  break;
	  case WAIT_NOTIFY_PROV:
		  if (myStatusCode<300) {
			  logger.info("NOTIFY Response: "+myStatusCode);
			  handleNotifyResponse(myResponse);
			  status=previousStatus;
			  myGUI.showStatus("Status: ?");
		  }
		  break;
	}
	  
  }
  }catch(Exception excep){
	  logger.error("Exception", excep);
    excep.printStackTrace();
  }
  
}



private void handleProxyResponse(ResponseEvent responseReceivedEvent, String myCallID) throws SipException, InvalidArgumentException, ParseException {
		Response myProxyClientResponse=responseReceivedEvent.getResponse();
		int myProxyClientStatusCode=myProxyClientResponse.getStatusCode();
		ClientTransaction myClientProxyTransaction = responseReceivedEvent.getClientTransaction();
		logger.info("Handle Proxy Response: "+myProxyClientStatusCode+" for CallId: "+myCallID);
		LegTransaction myProxyClientLegTransaction=myTransactionMap.get(myCallID);
		byte[] mySDPResponse=(byte[]) myProxyClientResponse.getContent();
		CSeqHeader myCseqHeaderResponse=(CSeqHeader)myProxyClientResponse.getHeader(CSeqHeader.NAME);
		
		String myCseqMethodResponse=myCseqHeaderResponse.getMethod();
		
		
		LegTransaction myProxyServerLegTransaction=myProxyClientLegTransaction.getPartnerLeg();
		if (myProxyClientLegTransaction.getState()==PROXY_IDLE){
			logger.info("Response Message: "+myProxyClientStatusCode+" from ProxyClientLegTransaction with number "+myProxyClientLegTransaction.getNumber()+" CallID: "+myCallID);
			if (myProxyServerLegTransaction.getState()==PROXY_IDLE || myProxyServerLegTransaction==null){
				//Don't need to re-direct the response to other side. Used when BYE has sent before, or Cancel, or...?
				logger.info("Response Message: "+myProxyClientStatusCode+" ...don't need to re-direct to "+myProxyServerLegTransaction.getNumber());
				
			}
			myTransactionMap.remove(myCallID);
			return;
		}
		
		ServerTransaction myProxyServerTransaction=(ServerTransaction) myClientProxyTransaction.getApplicationData();
		Request originalProxyRequest = myProxyServerTransaction.getRequest();
		Response myProxyServerResponse=null;;
		Address contactAddress = myAddressFactory.createAddress("sip:"+myProxyClientLegTransaction.getNumber()+"@"+myIP+":"+myPort);
	    ContactHeader myServerContactHeader = myHeaderFactory.createContactHeader(contactAddress);
		
		if (myProxyClientStatusCode<200){//1XX response is handled
			logger.info("Response Message: "+myProxyClientStatusCode+" from ProxyClientLegTransaction with number "+myProxyClientLegTransaction.getNumber()+" CallID: "+myCallID);
			myProxyServerResponse = myMessageFactory.createResponse(myProxyClientStatusCode,
					originalProxyRequest);
			if (myProxyClientStatusCode!=100){//100 Trying is not including Contact Header
				
				myProxyServerResponse.addHeader(myServerContactHeader);
				setAdditionalHeadersResponse(myProxyServerResponse, myGUI.SIPRespInfo.Resp180.getHeaderValuesList());
			logger.info(".....addd proper Contact Header");	
				//Need to add my ToTag
			}
		}
		else if (myProxyClientStatusCode<300){//2XX response is handled
			logger.info("Response Message: "+myProxyClientStatusCode+" from ProxyClientLegTransaction with number "+myProxyClientLegTransaction.getNumber()+" CallID: "+myCallID);
			myProxyServerResponse = myMessageFactory.createResponse(myProxyClientStatusCode,
					originalProxyRequest);
			myProxyServerResponse.addHeader(myServerContactHeader);
			myProxyClientLegTransaction.setState(PROXY_ESTABLISHED);
			myProxyServerLegTransaction.setState(PROXY_ESTABLISHED);
		}
		else if (myProxyClientStatusCode<400){//3XX response is handled
			logger.warn("Not implemented handle for Response: "+myProxyClientStatusCode);
			return;
		}
		else { //(myProxyClientStatusCode>=400)
			logger.error("Unexpected Response: "+myProxyClientStatusCode);
			return;
		}		
        
        ToHeader myToHeader = (ToHeader) myProxyServerResponse.getHeader("To");
        String toTag= String.valueOf(INITIAL_VARIABLE+myProxyServerLegTransaction.getID());
        myToHeader.setTag(toTag);
        
        if (mySDPResponse!=null){
        	ContentTypeHeader contentTypeHeader=myHeaderFactory.createContentTypeHeader("application","sdp");
        	myProxyServerResponse.setContent(mySDPResponse,contentTypeHeader);
        }
        logger.info("Send Proxy Response:");
        logger.info(myProxyServerResponse.toString());
		 
        myProxyServerTransaction.sendResponse(myProxyServerResponse);
        myGUI.display(">>> " + myProxyServerResponse.toString());
        
		
	
}


	public void processTimeout(TimeoutEvent arg0) {
		// TODO Auto-generated method stub
		logger.warn("processTimeout --> no handling");
		
	}

public void processTransactionTerminated(TransactionTerminatedEvent arg0) {
		// TODO Auto-generated method stub
		
		CallIdHeader callIDHeader=(CallIdHeader) arg0.getClientTransaction().getRequest().getHeader(CallIdHeader.NAME);
		String myCallID=callIDHeader.getCallId();
		logger.warn("processTransactionTerminated for CallId: "+myCallID+" --> no perfect handling");
				
				
	}
	
	private void setTxtLines(Request r){
		Header h;
		myGUI.setTxtLine(SIPHeadersTxt.RequestLine, r.getRequestURI().toString());
		h=r.getHeader("From");
		if (h!=null) myGUI.setTxtLine(SIPHeadersTxt.FromLine, h.toString());
		h=r.getHeader("To");
		if (h!=null) myGUI.setTxtLine(SIPHeadersTxt.ToLine, h.toString());
		h=r.getHeader("Contact");
		if (h!=null) myGUI.setTxtLine(SIPHeadersTxt.ContactLine, h.toString());
		h=r.getHeader("P-Asserted-Identity");
		if (h!=null) myGUI.setTxtLine(SIPHeadersTxt.PAILine, h.toString());
		h=r.getHeader("P-Preferred-Identity");
		if (h!=null) myGUI.setTxtLine(SIPHeadersTxt.PPILine, h.toString());
		h=r.getHeader("Diversion");
		if (h!=null) myGUI.setTxtLine(SIPHeadersTxt.DiversionLine, h.toString());
		
	}
	
	private void setTxtLines(Response r){
		Header h;
		myGUI.setTxtLine(SIPHeadersTxt.RequestLine, String.valueOf(r.getStatusCode()));
		h=r.getHeader("From");
		if (h!=null) myGUI.setTxtLine(SIPHeadersTxt.FromLine, h.toString());
		h=r.getHeader("To");
		if (h!=null) myGUI.setTxtLine(SIPHeadersTxt.ToLine, h.toString());
		h=r.getHeader("Contact");
		if (h!=null) myGUI.setTxtLine(SIPHeadersTxt.ContactLine, h.toString());
		h=r.getHeader("P-Asserted-Identity");
		if (h!=null) myGUI.setTxtLine(SIPHeadersTxt.PAILine, h.toString());
		h=r.getHeader("P-Preferred-Identity");
		if (h!=null) myGUI.setTxtLine(SIPHeadersTxt.PPILine, h.toString());
		h=r.getHeader("Diversion");
		if (h!=null) myGUI.setTxtLine(SIPHeadersTxt.DiversionLine, h.toString());
		
	}
	
	
private void setAdditionalHeadersResponse(Response r, LinkedList<HeadersValuesGeneric> ll){
		ListIterator<HeadersValuesGeneric> listIterator = ll.listIterator();
		while (listIterator.hasNext()) {
			Object obj=listIterator.next();
			HeadersValuesGeneric temp=(HeadersValuesGeneric)obj;
			try {
				myAdditionalHeader=myHeaderFactory.createHeader(temp.getHeader(), temp.getValue());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error("ParseException", e);
				e.printStackTrace();
			}
			r.addHeader(myAdditionalHeader);
			logger.debug(r.getStatusCode()+" add extra Header:Value="+temp.getHeader()+":"+temp.getValue());
        }
	}
	private void setAdditionalHeaderRequest(Request r, LinkedList<HeadersValuesGeneric> ll){
		ListIterator<HeadersValuesGeneric> listIterator = ll.listIterator();
		while (listIterator.hasNext()) {
			Object obj=listIterator.next();
			HeadersValuesGeneric temp=(HeadersValuesGeneric)obj;
			try {
				myAdditionalHeader=myHeaderFactory.createHeader(temp.getHeader(), temp.getValue());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error("ParseException", e);
				e.printStackTrace();
			}
			r.addHeader(myAdditionalHeader);
			logger.debug(r.getMethod()+" add extra Header:Value="+temp.getHeader()+":"+temp.getValue());
        }
	}
	
	private void createSDPResponse(Response r) throws ParseException, InterruptedException{
		ContentTypeHeader contentTypeHeader=myHeaderFactory.createContentTypeHeader("application","sdp");
		answerInfo.setAudioFormatList(myCodecsList);
		answerInfo.isDtmfFirst=myGUI.getDtmfFirstOrder();
		answerInfo.findProperCodec(offerInfo,true);
        //TODO: special handle in case codec is not existing =999
        myOldAudioCodec=answerInfo.aformat;
        remoteOldAudioCodec=myOldAudioCodec;
        remoteOldAudioPort=offerInfo.aport;
		byte[] content=mySdpManager.createSdp(answerInfo,true);//answer to the matched codec only
        r.setContent(content,contentTypeHeader);   
        //handle the best codec to use
       	myGAnnouncementTool.startMedia(offerInfo.IpAddress, offerInfo.aport, answerInfo.aport,answerInfo.aformat );
       	myGUI.showCodec(answerInfo.getAudioCodecString());
	}
	
private void answerToReInvite(Request r)  throws ParseException, InterruptedException, SipException, InvalidArgumentException{
		// TODO handle Re-invites with or without SDP
		byte[] content;
        Response myResponse = myMessageFactory.createResponse(200,
                r);

        myResponse.addHeader(myContactHeader);
        ContentTypeHeader contentTypeHeader=myHeaderFactory.createContentTypeHeader("application","sdp");
        
		byte[] cont=(byte[]) r.getContent();
  	  	
  	   if (cont==null){
  		  logger.trace("Re-Invite w/o SDP is received");
  		//TODO handle Re-Invite w/o SDP  
  		offerInfo.IpAddress=myIP;
        offerInfo.aport=myOldAudioPort;
        //offerInfo.aformat=myOldAudioCodec;
        offerInfo.setAudioFormatList(myCodecsList);
        offerInfo.isDtmfFirst=myGUI.getDtmfFirstOrder();
        offerInfo.vport=myVideoPort;
        offerInfo.vformat=myVideoCodec;
        content=mySdpManager.createSdp(offerInfo);
        myResponse.setContent(content,contentTypeHeader);
  		
  	  }else {
  		logger.trace("Re-Invite with SDP is received");
  		answerInfo.IpAddress=myIP;
        answerInfo.aport=myOldAudioPort; 
        answerInfo.setAudioFormatList(myCodecsList);
        answerInfo.isDtmfFirst=myGUI.getDtmfFirstOrder();
        //answerInfo.aformat=myOldAudioCodec; 
  		offerInfo=mySdpManager.getSdp(cont);
  		answerInfo.findProperCodec(offerInfo,true);
  		/*
        if (offerInfo.isAudioCodecAvailable(myOldAudioCodec)){
        	logger.info("myAudioCodec="+myOldAudioCodec+" exists in the list of requested INVITE");
        } else {
        	logger.warn("myAudioCodec="+myOldAudioCodec+" NOT exist in the list of requested INVITE");
        }
        */
        if (offerInfo.vport==-1) {
          answerInfo.vport=-1;
        }
        else if (myVideoPort==-1) {
          answerInfo.vport=0;
          answerInfo.vformat=offerInfo.vformat;
        }
        else {
          answerInfo.vport=myVideoPort;
          answerInfo.vformat=offerInfo.vformat;
        }
        
      //Check if port and codec are the same as before. 
        if (remoteOldAudioPort==offerInfo.aport && remoteOldAudioCodec==answerInfo.aformat){
        	//Do nothing. It's not necessary to re-initiate RTP stream
        	logger.trace("Same remote Audio Port and Audio codec for response to Re-Invite. Not necessary to restart RTP");
        }
        else{
        	logger.trace("Different remote Audio Port OR  Audio codec for response to Re-Invite. Necessary to restart RTP");
        	//Stop old RTP media
        	if (isOnlyAnnouncment){
            	myGVoiceTool.stopMedia();
            }else {
            	myGAnnouncementTool.stopMedia();
            }
        	myGUI.showCodec("");
        	Thread.sleep(1000);
        	myAudioPort=myAudioPort+2;
        	answerInfo.aport=myAudioPort;
        	remoteOldAudioPort=offerInfo.aport;
            remoteOldAudioCodec=answerInfo.aformat;
            myOldAudioCodec=remoteOldAudioCodec;
            myOldAudioPort=answerInfo.aport;
        	//Start new RTP media
            //handle the best codec to use
        	if (isOnlyAnnouncment){
              	 myGVoiceTool.startMedia(offerInfo.IpAddress, offerInfo.aport, answerInfo.aport,answerInfo.aformat );
               } else {
              	 myGAnnouncementTool.startMedia(offerInfo.IpAddress, offerInfo.aport, answerInfo.aport,answerInfo.aformat );
               }     

              if (answerInfo.vport>0) {
                myVideoTool.startMedia(offerInfo.IpAddress,offerInfo.vport,answerInfo.vport,myVideoCodec);
              }
              myGUI.showCodec(answerInfo.getAudioCodecString());
        	
        }
        //TODO: Handle the a:sendrecv, recvonly, etc
        String attribute=getDirectionAttribute(offerInfo);
        answerInfo.setDirection(attribute);
        content=mySdpManager.createSdp(answerInfo,true);//sent only the matched codec
        myResponse.setContent(content,contentTypeHeader);
  	  } 
        myServerTransaction.sendResponse(myResponse);
        myDialog = myServerTransaction.getDialog();

        //new Timer().schedule(new MyTimerTask(this),500000);
        myGUI.display(">>> " + myResponse.toString());
        //myGUI.setButtonStatusEstablishedCall(); State is already established
	}

private void sendCircuitNotifyRequest(String destination) {
	// TODO Auto-generated method stub
	
	try {
		Address toAddress;
		toAddress = myAddressFactory.createAddress(destination);
	
    ToHeader myToHeader = myHeaderFactory.createToHeader(toAddress, null);

    FromHeader myFromHeader = myHeaderFactory.createFromHeader(
        fromAddress, "56438");

    //myViaHeader = myHeaderFactory.createViaHeader(myIP, myPort,"udp", null);
    myViaHeader = myHeaderFactory.createViaHeader(myIP, myPort,myTransport, null);
    ArrayList<ViaHeader> myViaHeaders = new ArrayList<ViaHeader>();
    myViaHeaders.add(myViaHeader);
    MaxForwardsHeader myMaxForwardsHeader = myHeaderFactory.
        createMaxForwardsHeader(70);
    CSeqHeader myCSeqHeader = myHeaderFactory.createCSeqHeader(1L,
        "NOTIFY");
    CallIdHeader myCallIDHeader = mySipProvider.getNewCallId();
    javax.sip.address.URI myRequestURI = toAddress.getURI();
    
    Request myRequest = myMessageFactory.createRequest(myRequestURI,
        "NOTIFY",
        myCallIDHeader, myCSeqHeader, myFromHeader, myToHeader,
        myViaHeaders, myMaxForwardsHeader);
    myRequest.addHeader(myContactHeader);
    
   	 myRequest.addHeader(myCircuitUserAgentHeader);
    
    setAdditionalHeaderRequest(myRequest,myGUI.SIPReqInfo.ReqInvite.getHeaderValuesList());
    EventHeader myEventHeader=myHeaderFactory.createEventHeader("b2buaCSTA");
    myRequest.addHeader(myEventHeader);
    ContentTypeHeader contentTypeHeader=myHeaderFactory.createContentTypeHeader("application","csta+xml");
    byte[] content=myCstaXmlManager.createContentRequest();
    myGUI.displayCsta(">>> "+content.toString());
    myRequest.setContent(content,contentTypeHeader);
    myCircuitClientTransaction = mySipProvider.getNewClientTransaction(myRequest);

    myCircuitClientTransaction.sendRequest();
    //myDialog = myClientTransaction.getDialog();
    myGUI.display(">>> " + myRequest.toString());
	} catch (ParseException | InvalidArgumentException | SipException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
}

private void handleNotifyResponse(Response myResponse) {
	// TODO Auto-generated method stub
	try {
		ContentTypeHeader contentTypeHeader= (ContentTypeHeader) myResponse.getHeader(ContentTypeHeader.NAME);
		String applicationType=contentTypeHeader.getContentSubType();
		if (applicationType.contains("csta+xml")){
			ContentLengthHeader contentTypeLength=myResponse.getContentLength();
			int length=contentTypeLength.getContentLength();
			if (length>0){
				byte[] resp=(byte[]) myResponse.getContent();
				myGUI.displayCsta("<<< "+resp.toString());
				myCstaXmlManager.getContentResponse(resp);
				CircuitHandler.analyzeNotifyResponseContent();
				for (CircuitGuiUpdates cgu: CircuitHandler.myCircuitGuiUpdate){
					switch (cgu){
					case MonitorCrossRefId:
						myGUI.setMonitorCrossRefId(CircuitHandler.getMonitorCrossRefId());
						break;
					case CallId:
						myGUI.setCallId(CircuitHandler.getCallId());
						break;
					}
				}
				CircuitHandler.myCircuitGuiUpdate.clear();
				
			}else {
				logger.info("NOTIFY response without content");
			}
			
		}else {
			//TODO: Ignore it w/o doing anything?
		}
	}catch (Exception e){
		e.printStackTrace();
	}
	
	
}

private void handleNotifyRequest(Request myRequest) {
	// TODO Auto-generated method stub
	Response myResponse;
	try {
		byte[] cont=(byte[]) myRequest.getContent();
		myGUI.displayCsta("<<< "+cont.toString());
		myCstaXmlManager.getContentRequest(cont);
        CircuitHandler.analyzeNotifyRequestContent();
        
		myResponse = myMessageFactory.createResponse(200,myRequest);
	
    //myResponse.addHeader(myContactHeader);
    myCircuitServerTransaction.sendResponse(myResponse);
    myGUI.display(">>> "+myResponse.toString());
	} catch (ParseException | SipException | InvalidArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}



private String getDirectionAttribute(SdpInfo s){
	String temp=null;
	String result=null;
	temp=s.getDirection();
	switch (temp){
	case "sendrecv":
		result="sendrecv";
		break;
	case "sendonly":
		result="recvonly";
		break;
	case "recvonly":
		result="sendonly";
		break;
	case "inactive":
		result="inactive";
		break;
	}
	return result;
}

private String generateNonce() {

	MessageDigest messageDigest;
	try {
	messageDigest = MessageDigest.getInstance("MD5");
	

    Date date = new Date();
    long time = date.getTime();
    Random rand = new Random();
    long myRand = rand.nextLong();
    String nonceString = (new Long(time)).toString()+ (new Long(myRand)).toString();
    byte mdbytes[] = messageDigest.digest(nonceString.getBytes());

    // Convert the mdbytes array into a hex string.

    return toHexString(mdbytes);
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return "e721ca0e1832deca0106f99747c2f221a7471aaeC123FFEC707C";
	}

}
private static String toHexString(byte b[]) {
	final char[] toHex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    int pos = 0;
    char[] c = new char[b.length * 2];
    for (int i = 0; i < b.length; i++) {
        c[pos++] = toHex[(b[i] >> 4) & 0x0F];
        c[pos++] = toHex[b[i] & 0x0f];
    }
    return new String(c);

}

private boolean isCorrectRegister(Request r, String pass) throws NoSuchAlgorithmException{
	boolean verdict=false;
	AuthorizationHeader myAythHeader=(AuthorizationHeader) r.getHeader(AuthorizationHeader.NAME);
	if ( myAythHeader == null ) return false;
    String realm = myAythHeader.getRealm();
    String username = myAythHeader.getUsername(); 
    if ( username == null || realm == null ) {
        return false;
    }
    String nonce = myAythHeader.getNonce();
    URI uri = myAythHeader.getURI();
    if (uri == null) {
       return false;
    }
    String A1 = username + ":" + realm + ":" + pass;
    String A2 = r.getMethod().toUpperCase() + ":" + uri.toString();
    MessageDigest messageDigest;
	messageDigest = MessageDigest.getInstance(myAythHeader.getAlgorithm());
	
    byte mdbytes[] = messageDigest.digest(A1.getBytes());
    String HA1 = toHexString(mdbytes);  
    mdbytes = messageDigest.digest(A2.getBytes());
    String HA2 = toHexString(mdbytes); 
    String cnonce = myAythHeader.getCNonce();
    String KD = HA1 + ":" + nonce;
    if (cnonce != null) {
        KD += ":" + cnonce;
    }
    KD += ":" + HA2;
    mdbytes = messageDigest.digest(KD.getBytes());
    String mdString = toHexString(mdbytes);
    String response = myAythHeader.getResponse();
    verdict=mdString.equals(response);
	
	return verdict;
}

}
