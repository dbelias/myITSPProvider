package splibraries;

import java.util.ArrayList;
import org.apache.log4j.Logger;


public class SdpInfo {
  private static Logger logger=Logger.getLogger("SdpInfo");
  public String IpAddress;
  public int aport;
  public int aformat;
  public int vport;
  public int vformat;
  public ArrayList<Integer> audioFormatList;
  public ArrayList<Integer> videoFormatList;
  public boolean OnlyOneCodec;
  public int DTMF_PT;
  public String direction;
  public boolean isDtmfFirst;
  

 public SdpInfo() {
   IpAddress="";
   aport=0;
   aformat=0;
   vport=0;
   vformat=0;
   audioFormatList=new ArrayList<Integer>();
   videoFormatList=new ArrayList<Integer>();
   OnlyOneCodec=true;
   DTMF_PT=98;
   direction="sendrecv";
   isDtmfFirst=false;
   
 }
 public boolean isAudioCodecAvailable(int c){
	 return audioFormatList.contains(c);
 }
 public boolean isVideoCodecAvailable(int c){
	 return videoFormatList.contains(c);
 }
 
 public boolean isOnlyOneCodec(){
	 return OnlyOneCodec;
 }
 
 public void setOnlyOneCodec(boolean b){
	 OnlyOneCodec=b;
 }
 public void setAudioFormatList(ArrayList<Integer> ar){
	 audioFormatList=ar;
	 aformat=audioFormatList.get(0);
 }
 
 public void setDTMFPT(int c){
	 DTMF_PT=c;
 }
 
 public void setDTMFPT(){
	 DTMF_PT=audioFormatList.get(audioFormatList.size()-1);
	}
 
 public void  findProperCodec(SdpInfo s, boolean b ){
		int myCodec=999;
		//int DTMFCodec=s.audioFormatList.get(s.audioFormatList.size()-1);
		int DTMFCodec=s.DTMF_PT;  //supposed to be found before when handling incoming offer
		boolean bingo=false;
		logger.info("SDP 1:"+ getAudioFormatListToString());
		logger.info("SDP 2:"+s.getAudioFormatListToString());
		// find the DTMF payload from answer
			if (b){ //Select from the first match from offer list
				for (int temp : audioFormatList){
					if (s.isAudioCodecAvailable(temp)&& temp!=DTMFCodec){
						//codec is found
						myCodec=temp;
						bingo=true;
						logger.info("First Codec match searching in Offer list :"+temp);
						break;
					}
				}
			}else { //select from the first match from my list
				for (int temp : s.audioFormatList){
					if (isAudioCodecAvailable(temp) && temp!=DTMFCodec){
						//codec is found
						myCodec=temp;
						bingo=true;
						logger.info("First Codec match searching in my codec list:"+temp);
						break;
					}
				}
			}
			if (!bingo){
				logger.error("No codec match");
			}
		
			aformat= myCodec;
	}
 
 public void findProperCodec(SdpInfo s){
	 findProperCodec(s, false); 
 }
 public int[] getAudioFormatList(){
	 int size=audioFormatList.size();
	 int i=0;
	 int[] myAudioCodecArray=new int[size];
	 if (isDtmfFirst){ //DTMF must be set first
		 int last=audioFormatList.get(size-1);
		 int first=audioFormatList.get(0);
		 ArrayList<Integer> tempArray=new ArrayList<Integer>();
		 tempArray.add(last);
		 tempArray.add(first);
		 tempArray.addAll(audioFormatList.subList(1,size-2));
		 for (int temp : tempArray ){
			 myAudioCodecArray[i]=temp;
			 i++;
		 }
		 
	 }else {
		 for (int temp : audioFormatList ){
			 myAudioCodecArray[i]=temp;
			 i++;
		 }
	 }	 
	 return myAudioCodecArray;
 }
 public String getAudioFormatListToString(){
	 String  myAudioCodecString="";
	 
	 for (int temp : audioFormatList ){
		 myAudioCodecString+=String.valueOf(temp)+" ";
		 
	 }
	 return myAudioCodecString;
 }
 
 public String getAudioCodecString(){
	 String s="Codec:N.A";
	 if (aformat==0){
		 s="Codec:G711U";
	 }
	 else if (aformat==8){
		 s="Codec:G711A";
	 }
	 else if (aformat==9){
		 s="Codec:G722";
	 }
	 else if (aformat==18){
		 s="Codec:G729";
	 }
	 else if (aformat==3){
		 s="Codec:GSM";
	 }
	 return s;
 }
 
 public String getDirection(){
	 return direction;
 }
 
 public void setDirection(String s){
	 direction=s;
 }
}
