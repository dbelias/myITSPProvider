package splibraries;

public class Configuration {

public int sipPort;
public String name;
public String userID;
public int audioPort;
public int videoPort;
public int audioCodec;
public int videoCodec;
public String transport;

  public Configuration() {
    sipPort=5060;
    name="Dimitrios Belias";
    userID="2109567472";
    audioPort=40000;
    videoPort=-1;
    audioCodec=8; //G711A
    videoCodec=26;
    transport="udp";
  }
  
  public void setSIPPort(String s){
	  sipPort=Integer.parseInt(s);
  }
  public void setDisplayPart(String s){
	  name=s;
  }
  public void setUserPart(String s){
	  userID=s;
  }
  public void setAudioPort(String s){
	  audioPort=Integer.parseInt(s);
  }
  public void setVideoPort(String s){
	  videoPort=Integer.parseInt(s);
  }
  public void setAudioCodec(String s){
	  audioCodec=Integer.parseInt(s);
  }
  public void setAudioCodec(int i){
	  audioCodec=i;
  }
  public void setVideoCodec(String s){
	  videoCodec=Integer.parseInt(s);
  }
  public void setVideoCodec(int i){
	  videoCodec=i;
  }
  public void setTransportUDP(){
	  transport="udp";
  }
  public void setTransportTCP(){
	  transport="tcp";
  }
  
  public String getTransport(){
	  return transport;
  }
}
