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
		int DTMFCodec=s.audioFormatList.get(s.audioFormatList.size()-1);
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
	 int[] myAudioCodecArray=new int[size];
	 int i=0;
	 for (int temp : audioFormatList ){
		 myAudioCodecArray[i]=temp;
		 i++;
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
}




/*
 * 

public class SdpInfo {
  private String IpAddress;
  private int aport;
  private int aformat;
  private int vport;
  private int vformat;

 public SdpInfo() {
   IpAddress="";
   aport=0;
   aformat=0;
   vport=0;
   vformat=0;
 }

public void setIPAddress(String IP) { IpAddress=IP;}
public void setAPort(int AP) { aport=AP;}
public void setAFormat(int AF) { aformat=AF;}
public void setVPort(int VP) { vport=VP;}
public void setVFormat(int VF) { vformat=VF;}

public String getIPAddress() { return IpAddress;}
public int getAPort() { return aport;}
public int getAFormat() { return aformat;}
public int getVPort() { return vport;}
public int getVFormat() { return vformat;}


}
*/
