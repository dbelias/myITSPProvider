package support;

import javax.swing.JLabel;

public class RegisteredDeviceGUIObject {
	public JLabel myRegistrationStatus;
	public JLabel myUsedStatus;
	public JLabel myDID;
	
	public RegisteredDeviceGUIObject(JLabel r, JLabel u, JLabel d){
		this.myRegistrationStatus=r;
		this.myUsedStatus=u;
		this.myDID=d;
	}

}
