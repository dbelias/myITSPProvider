package splibraries;

import java.util.ArrayList;


public class SdpInfo {
  public String IpAddress;
  public int aport;
  public int aformat;
  public int vport;
  public int vformat;
  public ArrayList<Integer> audioFormatList;
  public ArrayList<Integer> videoFormatList;

 public SdpInfo() {
   IpAddress="";
   aport=0;
   aformat=0;
   vport=0;
   vformat=0;
   audioFormatList=new ArrayList<Integer>();
   videoFormatList=new ArrayList<Integer>();
   
 }
 public boolean isAudioCodecAvailable(int c){
	 return audioFormatList.contains(c);
 }
 public boolean isVideoCodecAvailable(int c){
	 return videoFormatList.contains(c);
 }
}
/*

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
