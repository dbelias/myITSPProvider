package window;



import javax.swing.JDialog;
import net.miginfocom.swing.MigLayout;
import support.RegisteredDeviceGUIObject;

import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Map;
import java.util.TreeMap;

public class RegisteredDevicesWnd extends JDialog {
	private JLabel lblNumber;
	private JLabel myNewRegisteredStatusLbl;
	private JLabel myNewUserStatusLbl;
	private JLabel myNewDIDLbl;
	private RegisteredDeviceGUIObject myNewRegisteredDeviceGUIObject;
	private TreeMap<Integer, RegisteredDeviceGUIObject> myGUIObjectMap;

	

	/**
	 * Create the dialog.
	 */
	public RegisteredDevicesWnd(Rectangle v ) {
		setSize(new Dimension(350, 300));
		setMinimumSize(new Dimension(350, 200));
		setMaximumSize(new Dimension(450, 500));
		setTitle("Registered Devices");
		setBounds((int)(v.getX()+v.getWidth()), (int) v.getY(), 350, 300);
		
		getContentPane().setLayout(new MigLayout("", "[][][][][][][][]", "[]"));
		
		JLabel lblRegisteredStatus = new JLabel("Registered Status");
		getContentPane().add(lblRegisteredStatus, "cell 1 0");
		
		lblNumber = new JLabel("Number");
		getContentPane().add(lblNumber, "cell 3 0 3 1,alignx center");
		
		JLabel lblUsedStatus = new JLabel("Used Status");
		getContentPane().add(lblUsedStatus, "cell 7 0");
		myGUIObjectMap=new TreeMap<Integer, RegisteredDeviceGUIObject>();

	}
	
	private String setCoordinatesRegistrationStatus(int r){
		return "cell 1 "+r+3;
	}
	private String setCoordinatesUsedStatus(int r){
		return "cell 7 "+r+3;
	}
	private String setCoordinatesDID(int r){
		return "cell 3 "+r+3+" 3 1,alignx center";
	}
	public void createNewRegisteredDevice(int i, String s){
		initializeGUIObjects(i, s);
		myNewRegisteredDeviceGUIObject=new RegisteredDeviceGUIObject(myNewRegisteredStatusLbl, myNewUserStatusLbl, myNewDIDLbl);
		myGUIObjectMap.put(i, myNewRegisteredDeviceGUIObject);
		
	}

	private void initializeGUIObjects(int i, String s) {
		// TODO Auto-generated method stub
		//Create JLabel for Registered Status
		myNewRegisteredStatusLbl=new JLabel("Registered");
		myNewRegisteredStatusLbl.setForeground(Color.GREEN);
		getContentPane().add(myNewRegisteredStatusLbl,setCoordinatesRegistrationStatus(i));
		
		//Create JLabel for DID
		myNewDIDLbl=new JLabel(s);
		getContentPane().add(myNewDIDLbl,setCoordinatesDID(i));
		
		//Create JLabel for User Status
		myNewUserStatusLbl=new JLabel("Not Used");
		myNewUserStatusLbl.setForeground(Color.RED);
		getContentPane().add(myNewUserStatusLbl,setCoordinatesUsedStatus(i));
		
		this.revalidate();
		
		
		
	}
	
	public void setRegisteredStatus(int i, boolean b){
		JLabel myRS=myGUIObjectMap.get(i).myRegistrationStatus;
		if (b){
			myRS.setText("Registered");
			myRS.setForeground(Color.GREEN);
		} else {
			myRS.setText("Not Registered");
			myRS.setForeground(Color.RED);
		}
		
		
	}
	
	public void setUsedStatus(int i, boolean b){
		JLabel myUS=myGUIObjectMap.get(i).myUsedStatus;
		if (b){
			myUS.setText("Used");
			myUS.setForeground(Color.GREEN);
		} else {
			myUS.setText("Not Used");
			myUS.setForeground(Color.RED);
		}
	}
	
	public void refresh(){
		//TODO: refresh of 
	}

}
