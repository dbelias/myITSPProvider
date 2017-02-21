package core;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Properties;
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
import javax.sip.TransactionTerminatedEvent;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.UserAgentHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.apache.log4j.Logger;

import splibraries.Configuration;
import splibraries.GStreamerToneTool;
import splibraries.GstreamerTool;
import splibraries.SdpInfo;
import splibraries.SdpManager;
import splibraries.TonesTool;
import splibraries.VideoTool;
import support.HeadersValuesGeneric;
import support.SIPHeadersTxt;
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
	private UserAgentHeader myUserAgentHeader;
	private Header myExtensionHeader;
	private Header myAdditionalHeader;
	private Header myPAIHeader;
	private ViaHeader myViaHeader;
	private Address fromAddress;
	private Dialog myDialog;
	private ClientTransaction myClientTransaction;
	private ServerTransaction myServerTransaction;
	private int status;
	private String myIP;
	private String toTag;
	private SdpManager mySdpManager;
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
	

	static final int YES=0;
	static final int NO=1;
	static final int SEND183=2;

	static final int IDLE=0;
	static final int WAIT_PROV=1;
	static final int WAIT_FINAL=2;
	static final int ESTABLISHED=4;
	static final int RINGING=5;
	static final int WAIT_ACK=6;
	static final int RE_INVITE_WAIT_ACK=7;
	static final int WAIT_PROV_LATE_SDP=8;
	static final int WAIT_FINAL_LATE_SDP=9;

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
	      mySipStack = mySipFactory.createSipStack(myProperties);
	      myMessageFactory = mySipFactory.createMessageFactory();
	      myHeaderFactory = mySipFactory.createHeaderFactory();
	      myAddressFactory = mySipFactory.createAddressFactory();
	      myListeningPoint = mySipStack.createListeningPoint(myIP, myPort, "udp");
	      mySipProvider = mySipStack.createSipProvider(myListeningPoint);
	      mySipProvider.addSipListener(this);

	      Address contactAddress = myAddressFactory.createAddress("sip:"+myIP+":"+myPort);
	      myContactHeader = myHeaderFactory.createContactHeader(contactAddress);
	      myExtensionHeader = myHeaderFactory.createHeader("User-Agent",
	              "myITSP tool V1");
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

             myViaHeader = myHeaderFactory.createViaHeader(myIP, myPort,"udp", null);
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
             myRequest.addHeader(myExtensionHeader);
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
           if (type==YES){
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


public void processRequest(RequestEvent requestReceivedEvent) {
  Request myRequest=requestReceivedEvent.getRequest();
  String method=myRequest.getMethod();
  myGUI.display("<<<<<<<<<< "+myRequest.toString());
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

        Response myResponse=myMessageFactory.createResponse(180,myRequest);
        myResponse.addHeader(myContactHeader);
        //TODO:Perhaps this control is wrong. 180 Ringing doesn't need PAI, does it?
        if (myGUI.SIPRespInfo.Resp200.getCOLP()){
       	 myResponse.addHeader(myPAIHeader);
        }
        ToHeader myToHeader = (ToHeader) myResponse.getHeader("To");
        myToHeader.setTag("454326");
        setAdditionalHeadersResponse(myResponse, myGUI.SIPRespInfo.Resp180.getHeaderValuesList());
        myServerTransaction.sendResponse(myResponse);
        myDialog=myServerTransaction.getDialog();
        myGUI.display(">>> "+myResponse.toString());
        status=RINGING;
        myGUI.showStatus("Status: RINGING");
        myGUI.setButtonStatusAnswerCall();
        myGUI.setButtonStatusSend183();
        setTxtLines(myRequest);
        
      }
     break;
    case ESTABLISHED:
      if (method.equals("BYE")) {
        Response myResponse=myMessageFactory.createResponse(200,myRequest);
        myResponse.addHeader(myContactHeader);
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
      }
      if (method.equals("INVITE")){ //Re-Invite handling
    	  // it may occur that my myServerTransaction is not created yet (e.g call is initiated by tool first)
    	  if (myServerTransaction == null) {
              myServerTransaction = mySipProvider.getNewServerTransaction(myRequest);
    	  }
    	  answerToReInvite(myRequest) ;
    	  status=RE_INVITE_WAIT_ACK;
    	  myGUI.showStatus("Status: RE_INVITE_WAIT_ACK");
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

        myAlertTool.stopTone();

        myGUI.display(">>> "+myResponse.toString());
        myGUI.display(">>> "+myCancelResponse.toString());

        status=IDLE;
        myGUI.showStatus("Status: IDLE");
        myGUI.setButtonStatusIdle();
        myGUI.setTxtLine(SIPHeadersTxt.ResetLines, "");
      }
      break;

      case WAIT_ACK:
        if (method.equals("ACK")) {
        status=ESTABLISHED;
        myGUI.showStatus("Status: ESTABLISHED");
        myGUI.setButtonStatusEstablishedCall();
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
           
          
              
        }  

  }

  }catch (Exception e) {
	  logger.warn("Exception", e);
    e.printStackTrace();
  }
}


public void processResponse(ResponseEvent responseReceivedEvent) {
  try{
  Response myResponse=responseReceivedEvent.getResponse();
  myGUI.display("<<<<<<<<<< "+myResponse.toString());
  ClientTransaction thisClientTransaction=responseReceivedEvent.getClientTransaction();
  if (!thisClientTransaction.equals(myClientTransaction)) {
	  logger.warn("Not similar Client Transactions");
	  return;}
  int myStatusCode=myResponse.getStatusCode();
  CSeqHeader originalCSeq=(CSeqHeader) myClientTransaction.getRequest().getHeader(CSeqHeader.NAME);
  long numseq=originalCSeq.getSeqNumber();
  logger.info("processResponse: Status="+status+" Response="+myStatusCode);
switch(status){

  case WAIT_PROV_LATE_SDP:
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
	      myAck.addHeader(myContactHeader);
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

	      status=IDLE;
	      Request myAck = myDialog.createAck(numseq);
	      myAck.addHeader(myContactHeader);
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
      myAck.addHeader(myContactHeader);
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

    }
    else {

      status=IDLE;
      Request myAck = myDialog.createAck(numseq);
      myAck.addHeader(myContactHeader);
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
	      myAck.addHeader(myContactHeader);
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
      myAck.addHeader(myContactHeader);
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
	      myAck.addHeader(myContactHeader);
	      
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
		  break;   
	  }
	  
}
  }catch(Exception excep){
	  logger.error("Exception", excep);
    excep.printStackTrace();
  }
}

	public void processTimeout(TimeoutEvent arg0) {
		// TODO Auto-generated method stub
		logger.warn("processTimeout --> no handling");
		
	}

	public void processTransactionTerminated(TransactionTerminatedEvent arg0) {
		// TODO Auto-generated method stub
		logger.warn("processTransactionTerminated --> no handling");
		
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

}
