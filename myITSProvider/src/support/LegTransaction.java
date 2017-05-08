package support;

import javax.sip.ClientTransaction;
import javax.sip.ServerTransaction;
import javax.sip.header.CallIdHeader;

public class LegTransaction {
	private String ownCallID;
	public LegTransaction partnerLeg;
	public String number;
	private String ipSignaling;
	private String portSignaling;
	private int id;
	private int state;
	private ClientTransaction clientTransactionProxy;
	private ServerTransaction serverTransactionProxy;
	private boolean actsAsServer;
	private long numSeq;
	private int seq;
	private static int i=0;
	private CallIdHeader callIdHeader;
	
	public LegTransaction(){
		this.ownCallID=null;
		this.number=null;
		this.ipSignaling=null;
		this.portSignaling=null;
		this.partnerLeg=null;
		this.setState(0);
		i++;
		this.id=i;
		this.clientTransactionProxy=null;
		this.serverTransactionProxy=null;
		this.setActingAsServer(true);
		this.setNumSeq(0L);
		this.seq=i;
		this.setCallIdHeader(null);
		
	}
	
	public LegTransaction(String callID){
		this.ownCallID=callID;
		this.number=null;
		this.ipSignaling=null;
		this.portSignaling=null;
		this.partnerLeg=null;
		i++;
		this.id=i;
		this.clientTransactionProxy=null;
		this.serverTransactionProxy=null;
		this.setActingAsServer(true);
		this.setNumSeq(0L);
		this.seq=i;
		this.setCallIdHeader(null);
	}

	/**
	 * @return the ownCallID
	 */
	public String getOwnCallID() {
		return ownCallID;
	}

	/**
	 * @param ownCallID the ownCallID to set
	 */
	public void setOwnCallID(String ownCallID) {
		this.ownCallID = ownCallID;
	}

	/**
	 * @return the partnerLeg
	 */
	public LegTransaction getPartnerLeg() {
		return partnerLeg;
	}

	/**
	 * @param partnerLeg the partnerLeg to set
	 */
	public void setPartnerLeg(LegTransaction partnerLeg) {
		this.partnerLeg = partnerLeg;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the ipRTP
	 */
	public String getIpSignaling() {
		return ipSignaling;
	}

	/**
	 * @param ipRTP the ipRTP to set
	 */
	public void setIpSignaling(String ipRTP) {
		this.ipSignaling = ipRTP;
	}

	/**
	 * @return the portRTP
	 */
	public String getPortSignaling() {
		return portSignaling;
	}

	/**
	 * @param portRTP the portRTP to set
	 */
	public void setPortSignaling(String portRTP) {
		this.portSignaling = portRTP;
	}
	
	public int getID(){
		return id;
	}
	
	public String getIDtoString(){
		return String.valueOf(id);
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public void setUserData(UserData user){
		
		this.number=user.getUserPart();
		this.ipSignaling=user.getHost();
		
	}

	public ClientTransaction getClientTransaction() {
		return clientTransactionProxy;
	}

	public void setClientTransaction(ClientTransaction clientTransaction) {
		this.clientTransactionProxy = clientTransaction;
		this.setActingAsServer(false);
	}

	public ServerTransaction getServerTransaction() {
		return serverTransactionProxy;
	}

	public void setServerTransaction(ServerTransaction serverTransaction) {
		this.serverTransactionProxy = serverTransaction;
		this.setActingAsServer(true);
	}

	public boolean isActingAsServer() {
		return actsAsServer;
	}

	public void setActingAsServer(boolean actsAsServer) {
		this.actsAsServer = actsAsServer;
	}
	
	public String getUserHostPort(){
		return number+"@"+ipSignaling+":"+portSignaling;
	}

	public long getNumSeq() {
		return numSeq;
	}

	public void setNumSeq(long numSeq) {
		this.numSeq = numSeq;
	}
	
	public void setSeq(){
		seq++;
	}
	
	public int getSeq(){
		return ++seq;
	}

	public CallIdHeader getCallIdHeader() {
		return callIdHeader;
	}

	public void setCallIdHeader(CallIdHeader callIdHeader) {
		this.callIdHeader = callIdHeader;
	}
	

}
