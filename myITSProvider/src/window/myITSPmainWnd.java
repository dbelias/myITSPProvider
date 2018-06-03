package window;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JMenu;

import core.ITSPListener;

import splibraries.Configuration;
import splibraries.RequestInvite;
import splibraries.Response180;
import splibraries.Response183;
import splibraries.Response200;
import support.BackupSettings;
import support.CallFeatures;
import support.FailoverMode;
import support.Feature;
import support.GStreamerLocation;
import support.SIPHeadersTxt;
import support.SIPRequestsInfo;
import support.SIPResponsesInfo;
import support.SystemIPs;
import support.WAVLocation;
import support.XMLBackup;
import support.XMLRestore;
import support.voiceConfiguration;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.event.CaretListener;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.swing.event.CaretEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.event.ChangeEvent;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.DefaultComboBoxModel;
import support.HoldMode;
import support.ReInviteMode;
import support.RegisteredDevice;

import javax.swing.JToggleButton;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.SystemColor;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFormattedTextField;
import javax.swing.JSeparator;

import circuit.CSTAMessageHandler;
import circuit.CircuitHandler;
import circuit.CstaMessages;

public class myITSPmainWnd {
	private static Logger logger=Logger.getLogger("myITSPmainWnd");
	private final String Version="V1.18.3 Registration & Failover Enhancements &uaCSTA enhancements";
	ITSPListener list;
	private JFrame frmMyItspSimulator;
	private JTextField txtPassword;
	private JTextField txtMyIp;
	private JTextField txtCalledPartyNumber;
	private JTextField txtRemoteIp;
	private JTextField txtInviteLine;
	private JTextField txtSip;
	private JTextField textFieldRemotePort;
	private JLabel lblDestination;
	private JLabel lblRequestLine;
	private JComboBox<String> comboBoxMyIPs;
	private JTextPane textArea;
	private JButton btnMakeCall;
	private JButton btnReleaseCall;
	private JLabel lblCallStatus;
	private JButton btnOnOff;
	private JButton btnOptions;
	private int state;
	private GStreamerLocation gStreamer;
	private WAVLocation wav;
	private Configuration config;
	private BackupSettings myBackupSet;
	private LinkedList<voiceConfiguration> codecsList;
	static final int STATE_ON=1;
	static final int STATE_OFF=0;
	private JTextField txtFromLine;
	private JTextField txtToLine;
	private JTextField txtContactLine;
	private JTextField txtPaiLine;
	private JTextField txtPpiLine;
	private JTextField txtDiversionLine;
	private JButton btnSend183;
	
	private Response183 my183Response;
	private Response180 my180Response;
	private Response200 my200Response;
	private RequestInvite myInviteRequest;
	public SIPRequestsInfo SIPReqInfo;
	public SIPResponsesInfo SIPRespInfo;
	public CallFeatures myCallFeaturesInfo;
	private JTextField txtFromOAD;
	private JCheckBox chckbxTelUri;
	private JButton btnReInvite;
	private JLabel lblCodec;
	private JCheckBox chckbxLateSdp;
	private boolean hasDtmfFirstOrder;
	private JRadioButtonMenuItem rdbtnmntmInfo;
	private JRadioButtonMenuItem rdbtnmntmDebug;
	private JRadioButtonMenuItem rdbtnmntmTrace;
	private boolean colorSwitch;
	static final int SEND183=2;
	static final int HOLD=3;
	static final int UNHOLD=4;
	private int btnSend183Status=2;
	private JComboBox cmbBoxReInviteMode;
	private JComboBox cmbBoxHoldMode;
	private JComboBox comboBoxUdpTcp;
	private JToggleButton tglbtnShowRegDevices;
	
	public RegisteredDevicesWnd myRegDevicesWnd;
	public HashMap<String,RegisteredDevice> myRegisteredDevices;
	private JButton btnFailoverMode;
	public JCheckBox chckbxEnableFaileover;
	public FailoverMode myFailoverMode;
	private JTextField panelScroll;
	private JCheckBox chckbxCircuitMode;
	private JSeparator separator;
	private JLabel lblUser;
	private JTextField circuitUserTxt;
	private JButton btnNotify;
	private JTextPane textPaneCsta;
	private JComboBox comboBoxCstaRequest;
	private JLabel lblMonCrossRefId;
	private JLabel lblCallid;
	

	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					logger.info("main process is started");
					myITSPmainWnd window = new myITSPmainWnd();
					window.frmMyItspSimulator.setVisible(true);
				} catch (Exception e) {
					logger.error("Exception", e);
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public myITSPmainWnd() {
		initialize();
		setSystemIPToComboBox();
		initializeGUIObjects();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMyItspSimulator = new JFrame();
		frmMyItspSimulator.getContentPane().setBackground(SystemColor.menu);
		/*
		frmMyItspSimulator.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				System.out.println("Height="+frmMyItspSimulator.getHeight());
				System.out.println("Width="+frmMyItspSimulator.getWidth());
			
			}
		});
		*/
		frmMyItspSimulator.setPreferredSize(new Dimension(900, 550));
		frmMyItspSimulator.setMinimumSize(new Dimension(800, 400));
		frmMyItspSimulator.setTitle("my ITSP Simulator @Powered by Belias Dimitrios "+Version);
		frmMyItspSimulator.setBounds(100, 100, 800, 703);
		frmMyItspSimulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 120, 80, 200, 61, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 28, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		frmMyItspSimulator.getContentPane().setLayout(gridBagLayout);
		
		
		JLabel lblProtocol = new JLabel("Protocol");
		GridBagConstraints gbc_lblProtocol = new GridBagConstraints();
		gbc_lblProtocol.anchor = GridBagConstraints.EAST;
		gbc_lblProtocol.insets = new Insets(0, 0, 5, 5);
		gbc_lblProtocol.gridx = 1;
		gbc_lblProtocol.gridy = 0;
		frmMyItspSimulator.getContentPane().add(lblProtocol, gbc_lblProtocol);
		
		txtSip = new JTextField();
		txtSip.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent arg0) {
				setDestinationLbl();
			}
		});
		txtSip.setText("sip");
		GridBagConstraints gbc_txtSip = new GridBagConstraints();
		gbc_txtSip.gridwidth = 2;
		gbc_txtSip.insets = new Insets(0, 0, 5, 5);
		gbc_txtSip.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSip.gridx = 2;
		gbc_txtSip.gridy = 0;
		frmMyItspSimulator.getContentPane().add(txtSip, gbc_txtSip);
		txtSip.setColumns(10);
		
		JLabel lblItspConfiguration = new JLabel("ITSP configuration");
		GridBagConstraints gbc_lblItspConfiguration = new GridBagConstraints();
		gbc_lblItspConfiguration.insets = new Insets(0, 0, 5, 5);
		gbc_lblItspConfiguration.gridx = 5;
		gbc_lblItspConfiguration.gridy = 0;
		frmMyItspSimulator.getContentPane().add(lblItspConfiguration, gbc_lblItspConfiguration);
		
		JLabel lblCall = new JLabel("Call (To:)");
		GridBagConstraints gbc_lblCall = new GridBagConstraints();
		gbc_lblCall.anchor = GridBagConstraints.WEST;
		gbc_lblCall.insets = new Insets(0, 0, 5, 5);
		gbc_lblCall.gridx = 1;
		gbc_lblCall.gridy = 1;
		frmMyItspSimulator.getContentPane().add(lblCall, gbc_lblCall);
		
		txtCalledPartyNumber = new JTextField();
		txtCalledPartyNumber.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				setDestinationLbl();
			}
		});
		
		
		
		
		txtCalledPartyNumber.setText("2106197313");
		GridBagConstraints gbc_txtTypeTheCdpn = new GridBagConstraints();
		gbc_txtTypeTheCdpn.gridwidth = 2;
		gbc_txtTypeTheCdpn.insets = new Insets(0, 0, 5, 5);
		gbc_txtTypeTheCdpn.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTypeTheCdpn.gridx = 2;
		gbc_txtTypeTheCdpn.gridy = 1;
		frmMyItspSimulator.getContentPane().add(txtCalledPartyNumber, gbc_txtTypeTheCdpn);
		txtCalledPartyNumber.setColumns(10);
		
		chckbxTelUri = new JCheckBox("Tel URI");
		chckbxTelUri.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				setTelUriDestinationLbl();
			}
		});
		GridBagConstraints gbc_chckbxTelUri = new GridBagConstraints();
		gbc_chckbxTelUri.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxTelUri.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxTelUri.gridx = 7;
		gbc_chckbxTelUri.gridy = 1;
		frmMyItspSimulator.getContentPane().add(chckbxTelUri, gbc_chckbxTelUri);
		
		txtPassword = new JTextField();
		txtPassword.setText("a11111111!");
		GridBagConstraints gbc_txtPassword = new GridBagConstraints();
		gbc_txtPassword.gridwidth = 3;
		gbc_txtPassword.insets = new Insets(0, 0, 5, 5);
		gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPassword.gridx = 8;
		gbc_txtPassword.gridy = 1;
		frmMyItspSimulator.getContentPane().add(txtPassword, gbc_txtPassword);
		txtPassword.setColumns(10);
		
		JLabel lblDomain = new JLabel("Password");
		GridBagConstraints gbc_lblDomain = new GridBagConstraints();
		gbc_lblDomain.insets = new Insets(0, 0, 5, 5);
		gbc_lblDomain.gridx = 11;
		gbc_lblDomain.gridy = 1;
		frmMyItspSimulator.getContentPane().add(lblDomain, gbc_lblDomain);
		
		JLabel lblRemoteIp = new JLabel("Remote IP");
		GridBagConstraints gbc_lblRemoteIp = new GridBagConstraints();
		gbc_lblRemoteIp.anchor = GridBagConstraints.WEST;
		gbc_lblRemoteIp.insets = new Insets(0, 0, 5, 5);
		gbc_lblRemoteIp.gridx = 1;
		gbc_lblRemoteIp.gridy = 2;
		frmMyItspSimulator.getContentPane().add(lblRemoteIp, gbc_lblRemoteIp);
		
		txtRemoteIp = new JTextField();
		txtRemoteIp.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent arg0) {
				setDestinationLbl();
			}
		});
		txtRemoteIp.setText("192.168.1.121");
		GridBagConstraints gbc_txtRemoteIp = new GridBagConstraints();
		gbc_txtRemoteIp.gridwidth = 2;
		gbc_txtRemoteIp.insets = new Insets(0, 0, 5, 5);
		gbc_txtRemoteIp.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtRemoteIp.gridx = 2;
		gbc_txtRemoteIp.gridy = 2;
		frmMyItspSimulator.getContentPane().add(txtRemoteIp, gbc_txtRemoteIp);
		txtRemoteIp.setColumns(10);
		
		btnOnOff = new JButton("On/Off");
		btnOnOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (state==STATE_ON){
					logger.info("Button Off is pressed");
					state=STATE_OFF;
					list.setOff();
				    list=null;
				    setGUIoff();
				    
				}
				else {//state==STATE_OFF
					logger.info("Button On is pressed");
					state=STATE_ON;
					setGUIon();
					showITSPSettings();
					
				    //TODO: set configuration according to my settings
					list = new ITSPListener(config, myITSPmainWnd.this);
				}
			}
		});
		GridBagConstraints gbc_btnOnoff = new GridBagConstraints();
		gbc_btnOnoff.insets = new Insets(0, 0, 5, 5);
		gbc_btnOnoff.gridx = 5;
		gbc_btnOnoff.gridy = 2;
		frmMyItspSimulator.getContentPane().add(btnOnOff, gbc_btnOnoff);
		
		comboBoxMyIPs = new JComboBox<String>();
		GridBagConstraints gbc_comboBoxMyIPs = new GridBagConstraints();
		gbc_comboBoxMyIPs.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxMyIPs.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxMyIPs.gridx = 7;
		gbc_comboBoxMyIPs.gridy = 2;
		frmMyItspSimulator.getContentPane().add(comboBoxMyIPs, gbc_comboBoxMyIPs);
		
		txtMyIp = new JTextField();
		txtMyIp.setText("my IP");
		GridBagConstraints gbc_txtMyIp = new GridBagConstraints();
		gbc_txtMyIp.gridwidth = 3;
		gbc_txtMyIp.insets = new Insets(0, 0, 5, 5);
		gbc_txtMyIp.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMyIp.gridx = 8;
		gbc_txtMyIp.gridy = 2;
		frmMyItspSimulator.getContentPane().add(txtMyIp, gbc_txtMyIp);
		txtMyIp.setColumns(10);
		
		JLabel lblLocalIp = new JLabel("local IP");
		GridBagConstraints gbc_lblLocalIp = new GridBagConstraints();
		gbc_lblLocalIp.insets = new Insets(0, 0, 5, 5);
		gbc_lblLocalIp.gridx = 11;
		gbc_lblLocalIp.gridy = 2;
		frmMyItspSimulator.getContentPane().add(lblLocalIp, gbc_lblLocalIp);
		
		JLabel lblPort = new JLabel("Port");
		GridBagConstraints gbc_lblPort = new GridBagConstraints();
		gbc_lblPort.anchor = GridBagConstraints.EAST;
		gbc_lblPort.insets = new Insets(0, 0, 5, 5);
		gbc_lblPort.gridx = 1;
		gbc_lblPort.gridy = 3;
		frmMyItspSimulator.getContentPane().add(lblPort, gbc_lblPort);
		
		textFieldRemotePort = new JTextField();
		textFieldRemotePort.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent arg0) {
				setDestinationLbl();
			}
		});
		textFieldRemotePort.setText("5060");
		GridBagConstraints gbc_textFieldRemotePort = new GridBagConstraints();
		gbc_textFieldRemotePort.gridwidth = 2;
		gbc_textFieldRemotePort.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldRemotePort.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldRemotePort.gridx = 2;
		gbc_textFieldRemotePort.gridy = 3;
		frmMyItspSimulator.getContentPane().add(textFieldRemotePort, gbc_textFieldRemotePort);
		textFieldRemotePort.setColumns(10);
		
		comboBoxUdpTcp = new JComboBox();
		comboBoxUdpTcp.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (comboBoxUdpTcp.getSelectedIndex()==0){
					config.setTransportUDP();
				} else {
					config.setTransportTCP();
				}
			}
		});
		comboBoxUdpTcp.setModel(new DefaultComboBoxModel(new String[] {"UDP", "TCP"}));
		comboBoxUdpTcp.setSelectedIndex(0);//Index=0 equals to UDP, Index=1 equals to TCP
		comboBoxUdpTcp.setEnabled(true);
		GridBagConstraints gbc_comboBoxUdpTcp = new GridBagConstraints();
		gbc_comboBoxUdpTcp.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxUdpTcp.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxUdpTcp.gridx = 4;
		gbc_comboBoxUdpTcp.gridy = 3;
		frmMyItspSimulator.getContentPane().add(comboBoxUdpTcp, gbc_comboBoxUdpTcp);
		
		chckbxCircuitMode = new JCheckBox("Circuit Mode");
		GridBagConstraints gbc_chckbxCircuitMode = new GridBagConstraints();
		gbc_chckbxCircuitMode.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxCircuitMode.gridx = 5;
		gbc_chckbxCircuitMode.gridy = 3;
		frmMyItspSimulator.getContentPane().add(chckbxCircuitMode, gbc_chckbxCircuitMode);
		
		txtFromOAD = new JTextField();
		GridBagConstraints gbc_txtFromOAD = new GridBagConstraints();
		gbc_txtFromOAD.gridwidth = 4;
		gbc_txtFromOAD.insets = new Insets(0, 0, 5, 5);
		gbc_txtFromOAD.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFromOAD.gridx = 7;
		gbc_txtFromOAD.gridy = 3;
		frmMyItspSimulator.getContentPane().add(txtFromOAD, gbc_txtFromOAD);
		txtFromOAD.setColumns(10);
		txtFromOAD.setText("2109567472");
		
		JLabel lblFromcaller = new JLabel("From (Caller)");
		GridBagConstraints gbc_lblFromcaller = new GridBagConstraints();
		gbc_lblFromcaller.insets = new Insets(0, 0, 5, 5);
		gbc_lblFromcaller.gridx = 11;
		gbc_lblFromcaller.gridy = 3;
		frmMyItspSimulator.getContentPane().add(lblFromcaller, gbc_lblFromcaller);
		
		lblDestination = new JLabel("Destination");
		GridBagConstraints gbc_lblDestination = new GridBagConstraints();
		gbc_lblDestination.insets = new Insets(0, 0, 5, 5);
		gbc_lblDestination.gridx = 1;
		gbc_lblDestination.gridy = 4;
		frmMyItspSimulator.getContentPane().add(lblDestination, gbc_lblDestination);
		
		lblRequestLine = new JLabel("Request Line");
		GridBagConstraints gbc_lblRequestLine = new GridBagConstraints();
		gbc_lblRequestLine.gridwidth = 2;
		gbc_lblRequestLine.insets = new Insets(0, 0, 5, 5);
		gbc_lblRequestLine.gridx = 2;
		gbc_lblRequestLine.gridy = 4;
		frmMyItspSimulator.getContentPane().add(lblRequestLine, gbc_lblRequestLine);
		
		cmbBoxHoldMode = new JComboBox();
		cmbBoxHoldMode.setEnabled(false);
		cmbBoxHoldMode.setEditable(false);
		cmbBoxHoldMode.setModel(new DefaultComboBoxModel(HoldMode.values()));
		cmbBoxHoldMode.setSelectedIndex(0);
		GridBagConstraints gbc_cmbBoxHoldMode = new GridBagConstraints();
		gbc_cmbBoxHoldMode.insets = new Insets(0, 0, 5, 5);
		gbc_cmbBoxHoldMode.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbBoxHoldMode.gridx = 4;
		gbc_cmbBoxHoldMode.gridy = 4;
		frmMyItspSimulator.getContentPane().add(cmbBoxHoldMode, gbc_cmbBoxHoldMode);
		
		btnOptions = new JButton("OPTIONS");
		btnOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				config.setUserPart(getFromOAD());
				list.updateFromAddress(config);
				list.updateCodecList(getAvailableCodecs());
				list.userInput(5,getDestination());
			}
		});
		btnOptions.setEnabled(false);
		GridBagConstraints gbc_btnOptions = new GridBagConstraints();
		gbc_btnOptions.insets = new Insets(0, 0, 5, 5);
		gbc_btnOptions.gridx = 5;
		gbc_btnOptions.gridy = 4;
		frmMyItspSimulator.getContentPane().add(btnOptions, gbc_btnOptions);
		
		textArea = new JTextPane();
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.gridheight = 9;
		gbc_textArea.gridwidth = 5;
		gbc_textArea.insets = new Insets(0, 0, 5, 5);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 7;
		gbc_textArea.gridy = 4;
		JScrollPane scrollPane = new JScrollPane(textArea);
		frmMyItspSimulator.getContentPane().add(scrollPane, gbc_textArea);
		
		btnMakeCall = new JButton("Call");
		btnMakeCall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//update config (display and user part)
				myCallFeaturesInfo.setActiveFeature(Feature.Options);
				config.setUserPart(getFromOAD());
				list.updateFromAddress(config);
				list.updateCodecList(getAvailableCodecs());
				list.userInput(0,getDestination());
			}
		});
		
		chckbxLateSdp = new JCheckBox("Late SDP");
		chckbxLateSdp.setEnabled(false);
		GridBagConstraints gbc_chckbxLateSdp = new GridBagConstraints();
		gbc_chckbxLateSdp.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxLateSdp.gridx = 1;
		gbc_chckbxLateSdp.gridy = 5;
		frmMyItspSimulator.getContentPane().add(chckbxLateSdp, gbc_chckbxLateSdp);
		GridBagConstraints gbc_btnMakeCall = new GridBagConstraints();
		gbc_btnMakeCall.insets = new Insets(0, 0, 5, 5);
		gbc_btnMakeCall.gridx = 3;
		gbc_btnMakeCall.gridy = 5;
		frmMyItspSimulator.getContentPane().add(btnMakeCall, gbc_btnMakeCall);
		
		btnReleaseCall = new JButton("Release");
		btnReleaseCall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				list.userInput(1,null);
			}
		});
		
		btnSend183 = new JButton("Send 183");
		btnSend183.setEnabled(false);
		btnSend183.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myCallFeaturesInfo.setHoldMode((HoldMode)cmbBoxHoldMode.getSelectedItem());
				list.updateCodecList(getAvailableCodecs());
				list.userInput(btnSend183Status,null);
				if (btnSend183Status==HOLD){				
					setButton183Status(UNHOLD);
				} else if (btnSend183Status==UNHOLD){
					setButton183Status(HOLD);
				} else {
					//setButton183Status(SEND183);
				}
				
			}
		});
		GridBagConstraints gbc_btnSend183 = new GridBagConstraints();
		gbc_btnSend183.insets = new Insets(0, 0, 5, 5);
		gbc_btnSend183.gridx = 4;
		gbc_btnSend183.gridy = 5;
		frmMyItspSimulator.getContentPane().add(btnSend183, gbc_btnSend183);
		GridBagConstraints gbc_btnReleaseCall = new GridBagConstraints();
		gbc_btnReleaseCall.insets = new Insets(0, 0, 5, 5);
		gbc_btnReleaseCall.gridx = 5;
		gbc_btnReleaseCall.gridy = 5;
		frmMyItspSimulator.getContentPane().add(btnReleaseCall, gbc_btnReleaseCall);
		
		JLabel lblStatus = new JLabel("Status");
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.insets = new Insets(0, 0, 5, 5);
		gbc_lblStatus.gridx = 1;
		gbc_lblStatus.gridy = 6;
		frmMyItspSimulator.getContentPane().add(lblStatus, gbc_lblStatus);
		
		lblCallStatus = new JLabel("Unknown");
		GridBagConstraints gbc_lblStatus_1 = new GridBagConstraints();
		gbc_lblStatus_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblStatus_1.gridx = 3;
		gbc_lblStatus_1.gridy = 6;
		frmMyItspSimulator.getContentPane().add(lblCallStatus, gbc_lblStatus_1);
		
		btnReInvite = new JButton("Re-Invite");
		btnReInvite.setEnabled(false);
		btnReInvite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myCallFeaturesInfo.setActiveFeature(Feature.ReInvite);
				myCallFeaturesInfo.setReInviteMode((ReInviteMode)cmbBoxReInviteMode.getSelectedItem());
				list.updateCodecList(getAvailableCodecs());
				list.userInput(0,null);
			}
		});
		
		lblCodec = new JLabel("Codec:N.A");
		GridBagConstraints gbc_lblCodec = new GridBagConstraints();
		gbc_lblCodec.insets = new Insets(0, 0, 5, 5);
		gbc_lblCodec.gridx = 4;
		gbc_lblCodec.gridy = 6;
		frmMyItspSimulator.getContentPane().add(lblCodec, gbc_lblCodec);
		GridBagConstraints gbc_btnReInvite = new GridBagConstraints();
		gbc_btnReInvite.insets = new Insets(0, 0, 5, 5);
		gbc_btnReInvite.gridx = 5;
		gbc_btnReInvite.gridy = 6;
		frmMyItspSimulator.getContentPane().add(btnReInvite, gbc_btnReInvite);
		//frame.getContentPane().add(textArea, gbc_textArea);
		
		JLabel lblInvite = new JLabel("Invite");
		GridBagConstraints gbc_lblInvite = new GridBagConstraints();
		gbc_lblInvite.anchor = GridBagConstraints.EAST;
		gbc_lblInvite.insets = new Insets(0, 0, 5, 5);
		gbc_lblInvite.gridx = 1;
		gbc_lblInvite.gridy = 7;
		frmMyItspSimulator.getContentPane().add(lblInvite, gbc_lblInvite);
		
		txtInviteLine = new JTextField();
		txtInviteLine.setEditable(false);
		txtInviteLine.setText("call from xyz");
		GridBagConstraints gbc_txtInviteLine = new GridBagConstraints();
		gbc_txtInviteLine.gridwidth = 3;
		gbc_txtInviteLine.insets = new Insets(0, 0, 5, 5);
		gbc_txtInviteLine.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtInviteLine.gridx = 2;
		gbc_txtInviteLine.gridy = 7;
		frmMyItspSimulator.getContentPane().add(txtInviteLine, gbc_txtInviteLine);
		txtInviteLine.setColumns(10);
		
		cmbBoxReInviteMode = new JComboBox();
		cmbBoxReInviteMode.setModel(new DefaultComboBoxModel(ReInviteMode.values()));
		cmbBoxReInviteMode.setSelectedIndex(0);
		cmbBoxReInviteMode.setMaximumRowCount(3);
		cmbBoxReInviteMode.setEnabled(false);
		GridBagConstraints gbc_cmbBoxReInviteMode = new GridBagConstraints();
		gbc_cmbBoxReInviteMode.insets = new Insets(0, 0, 5, 5);
		gbc_cmbBoxReInviteMode.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbBoxReInviteMode.gridx = 5;
		gbc_cmbBoxReInviteMode.gridy = 7;
		frmMyItspSimulator.getContentPane().add(cmbBoxReInviteMode, gbc_cmbBoxReInviteMode);
		
		JLabel lblFrom = new JLabel("From:");
		lblFrom.setHorizontalTextPosition(SwingConstants.LEFT);
		GridBagConstraints gbc_lblFrom = new GridBagConstraints();
		gbc_lblFrom.anchor = GridBagConstraints.EAST;
		gbc_lblFrom.insets = new Insets(0, 0, 5, 5);
		gbc_lblFrom.gridx = 1;
		gbc_lblFrom.gridy = 8;
		frmMyItspSimulator.getContentPane().add(lblFrom, gbc_lblFrom);
		
		txtFromLine = new JTextField();
		txtFromLine.setEditable(false);
		txtFromLine.setText("From Line");
		GridBagConstraints gbc_txtFromLine = new GridBagConstraints();
		gbc_txtFromLine.gridwidth = 3;
		gbc_txtFromLine.insets = new Insets(0, 0, 5, 5);
		gbc_txtFromLine.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFromLine.gridx = 2;
		gbc_txtFromLine.gridy = 8;
		frmMyItspSimulator.getContentPane().add(txtFromLine, gbc_txtFromLine);
		txtFromLine.setColumns(10);
		
		JLabel lblTo = new JLabel("To:");
		GridBagConstraints gbc_lblTo = new GridBagConstraints();
		gbc_lblTo.anchor = GridBagConstraints.EAST;
		gbc_lblTo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTo.gridx = 1;
		gbc_lblTo.gridy = 9;
		frmMyItspSimulator.getContentPane().add(lblTo, gbc_lblTo);
		
		txtToLine = new JTextField();
		txtToLine.setEditable(false);
		txtToLine.setText("To Line");
		GridBagConstraints gbc_txtToLine = new GridBagConstraints();
		gbc_txtToLine.gridwidth = 3;
		gbc_txtToLine.insets = new Insets(0, 0, 5, 5);
		gbc_txtToLine.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtToLine.gridx = 2;
		gbc_txtToLine.gridy = 9;
		frmMyItspSimulator.getContentPane().add(txtToLine, gbc_txtToLine);
		txtToLine.setColumns(10);
		
		JLabel lblContact = new JLabel("Contact:");
		GridBagConstraints gbc_lblContact = new GridBagConstraints();
		gbc_lblContact.anchor = GridBagConstraints.EAST;
		gbc_lblContact.insets = new Insets(0, 0, 5, 5);
		gbc_lblContact.gridx = 1;
		gbc_lblContact.gridy = 10;
		frmMyItspSimulator.getContentPane().add(lblContact, gbc_lblContact);
		
		txtContactLine = new JTextField();
		txtContactLine.setEditable(false);
		txtContactLine.setText("Contact Line");
		GridBagConstraints gbc_txtContactLine = new GridBagConstraints();
		gbc_txtContactLine.gridwidth = 3;
		gbc_txtContactLine.insets = new Insets(0, 0, 5, 5);
		gbc_txtContactLine.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtContactLine.gridx = 2;
		gbc_txtContactLine.gridy = 10;
		frmMyItspSimulator.getContentPane().add(txtContactLine, gbc_txtContactLine);
		txtContactLine.setColumns(10);
		
		JLabel lblPai = new JLabel("PAI:");
		GridBagConstraints gbc_lblPai = new GridBagConstraints();
		gbc_lblPai.anchor = GridBagConstraints.EAST;
		gbc_lblPai.insets = new Insets(0, 0, 5, 5);
		gbc_lblPai.gridx = 1;
		gbc_lblPai.gridy = 11;
		frmMyItspSimulator.getContentPane().add(lblPai, gbc_lblPai);
		
		txtPaiLine = new JTextField();
		txtPaiLine.setEditable(false);
		txtPaiLine.setText("PAI Line");
		GridBagConstraints gbc_txtPaiLine = new GridBagConstraints();
		gbc_txtPaiLine.gridwidth = 3;
		gbc_txtPaiLine.insets = new Insets(0, 0, 5, 5);
		gbc_txtPaiLine.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPaiLine.gridx = 2;
		gbc_txtPaiLine.gridy = 11;
		frmMyItspSimulator.getContentPane().add(txtPaiLine, gbc_txtPaiLine);
		txtPaiLine.setColumns(10);
		
		JLabel lblPpi = new JLabel("PPI:");
		GridBagConstraints gbc_lblPpi = new GridBagConstraints();
		gbc_lblPpi.anchor = GridBagConstraints.EAST;
		gbc_lblPpi.insets = new Insets(0, 0, 5, 5);
		gbc_lblPpi.gridx = 1;
		gbc_lblPpi.gridy = 12;
		frmMyItspSimulator.getContentPane().add(lblPpi, gbc_lblPpi);
		
		txtPpiLine = new JTextField();
		txtPpiLine.setEditable(false);
		txtPpiLine.setText("PPI Line");
		GridBagConstraints gbc_txtPpiLine = new GridBagConstraints();
		gbc_txtPpiLine.gridwidth = 3;
		gbc_txtPpiLine.insets = new Insets(0, 0, 5, 5);
		gbc_txtPpiLine.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPpiLine.gridx = 2;
		gbc_txtPpiLine.gridy = 12;
		frmMyItspSimulator.getContentPane().add(txtPpiLine, gbc_txtPpiLine);
		txtPpiLine.setColumns(10);
		
		JLabel lblDiversion = new JLabel("Diversion:");
		GridBagConstraints gbc_lblDiversion = new GridBagConstraints();
		gbc_lblDiversion.anchor = GridBagConstraints.EAST;
		gbc_lblDiversion.insets = new Insets(0, 0, 5, 5);
		gbc_lblDiversion.gridx = 1;
		gbc_lblDiversion.gridy = 13;
		frmMyItspSimulator.getContentPane().add(lblDiversion, gbc_lblDiversion);
		
		txtDiversionLine = new JTextField();
		txtDiversionLine.setEditable(false);
		txtDiversionLine.setText("Diversion Line");
		GridBagConstraints gbc_txtDiversionLine = new GridBagConstraints();
		gbc_txtDiversionLine.gridwidth = 3;
		gbc_txtDiversionLine.insets = new Insets(0, 0, 5, 5);
		gbc_txtDiversionLine.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDiversionLine.gridx = 2;
		gbc_txtDiversionLine.gridy = 13;
		frmMyItspSimulator.getContentPane().add(txtDiversionLine, gbc_txtDiversionLine);
		txtDiversionLine.setColumns(10);
		
		JButton btnClearText = new JButton("Clear Text");
		btnClearText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				textArea.setText(null);
			}
		});
		
		tglbtnShowRegDevices = new JToggleButton("Show Reg. Devices");
		tglbtnShowRegDevices.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if (tglbtnShowRegDevices.isSelected()){
					//TODO: create new Registered Device Window
					tglbtnShowRegDevices.setText("Hide Reg. Devices");
					Rectangle originalFrm= frmMyItspSimulator.getBounds();
					myRegDevicesWnd=new RegisteredDevicesWnd(originalFrm);
					updateRegisteredDeviceDialogGUI();
					myRegDevicesWnd.setVisible(true);
				} else {
					
					tglbtnShowRegDevices.setText("Show Reg. Devices");
					myRegDevicesWnd.setVisible(false);
				}
			}
		});
		GridBagConstraints gbc_tglbtnShowRegDevices = new GridBagConstraints();
		gbc_tglbtnShowRegDevices.insets = new Insets(0, 0, 5, 5);
		gbc_tglbtnShowRegDevices.gridx = 7;
		gbc_tglbtnShowRegDevices.gridy = 13;
		frmMyItspSimulator.getContentPane().add(tglbtnShowRegDevices, gbc_tglbtnShowRegDevices);
		GridBagConstraints gbc_btnClearText = new GridBagConstraints();
		gbc_btnClearText.insets = new Insets(0, 0, 5, 5);
		gbc_btnClearText.gridx = 11;
		gbc_btnClearText.gridy = 13;
		frmMyItspSimulator.getContentPane().add(btnClearText, gbc_btnClearText);
		
		btnFailoverMode = new JButton("Failover Mode Selection");
		btnFailoverMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FailoverWnd myFailoverWnd=new FailoverWnd(myFailoverMode);
				myFailoverWnd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				myFailoverWnd.setVisible(true);
			}
		});
		
		chckbxEnableFaileover = new JCheckBox("Enable Faileover");
		chckbxEnableFaileover.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (chckbxEnableFaileover.isSelected()){//Failover Mode is enabled
					panelScroll.setText("!!!Warning --Failover Mode Enabled:"+myFailoverMode.failoverHeader+"-- Warning!!!");
					panelScroll.setBackground(Color.RED);
					Font myFont=new Font("Courier", Font.BOLD,12);
					panelScroll.setFont(myFont);
					
				}else {//Failover Mode is disabled
					panelScroll.setText("Normal Mode Running");
					Font myFont=new Font("Tahoma", Font.PLAIN,11);
					panelScroll.setFont(myFont);
					panelScroll.setBackground(Color.GREEN);
				}
			}
		});
		
		GridBagConstraints gbc_chckbxEnableFaileover = new GridBagConstraints();
		gbc_chckbxEnableFaileover.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxEnableFaileover.gridx = 5;
		gbc_chckbxEnableFaileover.gridy = 14;
		frmMyItspSimulator.getContentPane().add(chckbxEnableFaileover, gbc_chckbxEnableFaileover);
		GridBagConstraints gbc_btnFailoverMode = new GridBagConstraints();
		gbc_btnFailoverMode.insets = new Insets(0, 0, 5, 5);
		gbc_btnFailoverMode.gridx = 7;
		gbc_btnFailoverMode.gridy = 14;
		frmMyItspSimulator.getContentPane().add(btnFailoverMode, gbc_btnFailoverMode);
		
		panelScroll = new JTextField();
		panelScroll.setHorizontalAlignment(SwingConstants.CENTER);
		panelScroll.setText("Normal Mode Running");
		panelScroll.setBackground(Color.GREEN);
		panelScroll.setEditable(false);
		GridBagConstraints gbc_panelScroll = new GridBagConstraints();
		gbc_panelScroll.gridwidth = 8;
		gbc_panelScroll.insets = new Insets(0, 0, 5, 5);
		gbc_panelScroll.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelScroll.gridx = 3;
		gbc_panelScroll.gridy = 15;
		frmMyItspSimulator.getContentPane().add(panelScroll, gbc_panelScroll);
		panelScroll.setColumns(10);
		
		separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 0, 5, 5);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 16;
		frmMyItspSimulator.getContentPane().add(separator, gbc_separator);
		
		lblUser = new JLabel("User");
		GridBagConstraints gbc_lblUser = new GridBagConstraints();
		gbc_lblUser.insets = new Insets(0, 0, 5, 5);
		gbc_lblUser.gridx = 1;
		gbc_lblUser.gridy = 17;
		frmMyItspSimulator.getContentPane().add(lblUser, gbc_lblUser);
		
		circuitUserTxt = new JTextField();
		GridBagConstraints gbc_circuitUserTxt = new GridBagConstraints();
		gbc_circuitUserTxt.insets = new Insets(0, 0, 5, 5);
		gbc_circuitUserTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_circuitUserTxt.gridx = 3;
		gbc_circuitUserTxt.gridy = 17;
		frmMyItspSimulator.getContentPane().add(circuitUserTxt, gbc_circuitUserTxt);
		circuitUserTxt.setColumns(10);
		
		btnNotify = new JButton("NOTIFY");
		btnNotify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CSTAMessageHandler.setDevice(circuitUserTxt.getText());
				CSTAMessageHandler.setCstaMessageOutgoing((CstaMessages) comboBoxCstaRequest.getSelectedItem());
				CircuitHandler.setCalledDevice(txtCalledPartyNumber.getText());
				sendCstaNotify();
				config.setUserPart(getFromOAD());
				list.updateFromAddress(config);
				list.updateCodecList(getAvailableCodecs());
				list.userInput(6,getDestination());
				
				
			}
		});
		GridBagConstraints gbc_btnNotify = new GridBagConstraints();
		gbc_btnNotify.insets = new Insets(0, 0, 5, 5);
		gbc_btnNotify.gridx = 4;
		gbc_btnNotify.gridy = 17;
		frmMyItspSimulator.getContentPane().add(btnNotify, gbc_btnNotify);
		
		textPaneCsta = new JTextPane();
		textPaneCsta.setEditable(false);
		GridBagConstraints gbc_textPaneCsta = new GridBagConstraints();
		gbc_textPaneCsta.gridheight = 6;
		gbc_textPaneCsta.gridwidth = 5;
		gbc_textPaneCsta.insets = new Insets(0, 0, 0, 5);
		gbc_textPaneCsta.fill = GridBagConstraints.BOTH;
		gbc_textPaneCsta.gridx = 7;
		gbc_textPaneCsta.gridy = 17;
		JScrollPane scrollPane2 = new JScrollPane(textPaneCsta);
		frmMyItspSimulator.getContentPane().add(scrollPane2, gbc_textPaneCsta);
		//frmMyItspSimulator.getContentPane().add(textPaneCsta, gbc_textPaneCsta);
		
		comboBoxCstaRequest = new JComboBox();
		comboBoxCstaRequest.setModel(new DefaultComboBoxModel(CstaMessages.values()));
		GridBagConstraints gbc_comboBoxCstaRequest = new GridBagConstraints();
		gbc_comboBoxCstaRequest.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxCstaRequest.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxCstaRequest.gridx = 3;
		gbc_comboBoxCstaRequest.gridy = 18;
		frmMyItspSimulator.getContentPane().add(comboBoxCstaRequest, gbc_comboBoxCstaRequest);
		
		JLabel lblMonitorcrossreferenceid = new JLabel("monitorCrossRefId");
		GridBagConstraints gbc_lblMonitorcrossreferenceid = new GridBagConstraints();
		gbc_lblMonitorcrossreferenceid.insets = new Insets(0, 0, 5, 5);
		gbc_lblMonitorcrossreferenceid.gridx = 4;
		gbc_lblMonitorcrossreferenceid.gridy = 18;
		frmMyItspSimulator.getContentPane().add(lblMonitorcrossreferenceid, gbc_lblMonitorcrossreferenceid);
		
		lblMonCrossRefId = new JLabel("CrossRefId");
		GridBagConstraints gbc_lblMonCrossRefId = new GridBagConstraints();
		gbc_lblMonCrossRefId.insets = new Insets(0, 0, 5, 5);
		gbc_lblMonCrossRefId.gridx = 5;
		gbc_lblMonCrossRefId.gridy = 18;
		frmMyItspSimulator.getContentPane().add(lblMonCrossRefId, gbc_lblMonCrossRefId);
		
		JLabel CallIdLabel = new JLabel("call ID");
		GridBagConstraints gbc_CallIdLabel = new GridBagConstraints();
		gbc_CallIdLabel.insets = new Insets(0, 0, 5, 5);
		gbc_CallIdLabel.gridx = 4;
		gbc_CallIdLabel.gridy = 19;
		frmMyItspSimulator.getContentPane().add(CallIdLabel, gbc_CallIdLabel);
		
		lblCallid = new JLabel("callID");
		GridBagConstraints gbc_lblCallid = new GridBagConstraints();
		gbc_lblCallid.insets = new Insets(0, 0, 5, 5);
		gbc_lblCallid.gridx = 5;
		gbc_lblCallid.gridy = 19;
		frmMyItspSimulator.getContentPane().add(lblCallid, gbc_lblCallid);
		
		JMenuBar menuBar = new JMenuBar();
		frmMyItspSimulator.setJMenuBar(menuBar);
		
		JMenu mnBr = new JMenu("B&R");
		menuBar.add(mnBr);
		
		JMenuItem mntmBackup = new JMenuItem("Backup");
		mntmBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myBackupSet.setCallingSettings(txtFromOAD.getText());
				myBackupSet.setCalledSettings(txtCalledPartyNumber.getText(), txtRemoteIp.getText(), textFieldRemotePort.getText());
				myBackupSet.setTransport(config.getTransport());
				myBackupSet.setPassword(txtPassword.getText());
				XMLBackup xmlb=new XMLBackup(gStreamer, wav, myBackupSet);
			}
		});
		mnBr.add(mntmBackup);
		
		JMenuItem mntmRestore = new JMenuItem("Restore");
		mntmRestore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				XMLRestore xmlr=new XMLRestore(gStreamer, wav, myBackupSet);
				if (myBackupSet.getIsAvailable()){ //to be compatible with old backups
					updateGUISettings(myBackupSet);
				}
				
			}
		});
		mnBr.add(mntmRestore);
		
		JMenu mnSystemSettings = new JMenu("System Settings");
		menuBar.add(mnSystemSettings);
		
		JMenuItem mntmGstreamerSettings = new JMenuItem("GStreamer Settings");
		mntmGstreamerSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GStreamerSettingsDialog gssd= new GStreamerSettingsDialog(gStreamer);
				gssd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				gssd.setVisible(true);
				
			}
		});
		mnSystemSettings.add(mntmGstreamerSettings);
		
		JMenuItem mntmWavSettings = new JMenuItem("WAV Settings");
		mntmWavSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WAVSettingsDialog dialog=new WAVSettingsDialog(wav);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		mnSystemSettings.add(mntmWavSettings);
		
		JMenu mnItspSettings = new JMenu("ITSP Settings");
		menuBar.add(mnItspSettings);
		
		JMenuItem mntmPortSettings = new JMenuItem("Port Settings");
		mntmPortSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PortSettingsDialog dialog = new PortSettingsDialog(config);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		mnItspSettings.add(mntmPortSettings);
		
		JMenu mnCodecSettings = new JMenu("Codec Settings");
		menuBar.add(mnCodecSettings);
		
		JMenuItem mntmVoicedtmfSettings = new JMenuItem("Voice -DTMF Settings");
		mntmVoicedtmfSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VoiceSettingsWnd dialog=new VoiceSettingsWnd(config,codecsList);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		mnCodecSettings.add(mntmVoicedtmfSettings);
		
		JMenu mnImportHeaders = new JMenu("Import Headers");
		menuBar.add(mnImportHeaders);
		
		JMenuItem mntmSipHeaders = new JMenuItem("SIP Respone Headers");
		mntmSipHeaders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SIPResponseSettingsDialog dialog=new SIPResponseSettingsDialog(SIPRespInfo);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		mnImportHeaders.add(mntmSipHeaders);
		
		JMenuItem mntmSipRequestHeaders = new JMenuItem("SIP Request Headers");
		mntmSipRequestHeaders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SIPRequestSettingsDialog dialog=new SIPRequestSettingsDialog(SIPReqInfo);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
			}
		});
		mnImportHeaders.add(mntmSipRequestHeaders);
		
		JMenuItem mntmSdpHeaders = new JMenuItem("SDP Headers");
		mntmSdpHeaders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		mnImportHeaders.add(mntmSdpHeaders);
		
		JMenu mnTraces = new JMenu("Traces");
		
		ButtonGroup traceGroup = new ButtonGroup();

		rdbtnmntmInfo = new JRadioButtonMenuItem("INFO");
		rdbtnmntmInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setTraceLevel();
			}
		});
		rdbtnmntmInfo.setSelected(true);
		rdbtnmntmDebug = new JRadioButtonMenuItem("DEBUG");
		rdbtnmntmDebug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTraceLevel();
			}
		});
		rdbtnmntmTrace = new JRadioButtonMenuItem("TRACE");
		rdbtnmntmTrace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTraceLevel();
			}
		});
		traceGroup.add(rdbtnmntmInfo);
		traceGroup.add(rdbtnmntmDebug);
		traceGroup.add(rdbtnmntmTrace);
		
		mnTraces.add(rdbtnmntmInfo);
		mnTraces.add(rdbtnmntmDebug);
		mnTraces.add(rdbtnmntmTrace);
		menuBar.add(mnTraces);
	}
	protected void sendCstaNotify() {
		// TODO Auto-generated method stub
		CircuitHandler.buildNotifyContent();
		
	}

	protected void updateRegisteredDeviceDialogGUI() {
		// TODO Auto-generated method stub
		int i=0;
		for (HashMap.Entry<String,RegisteredDevice> entry : myRegisteredDevices.entrySet()){
			i++;
			myRegDevicesWnd.createNewRegisteredDevice(i,entry.getValue().getDID());
			myRegDevicesWnd.setRegisteredStatus(i, entry.getValue().getUsedStatus());
		}
		
		//myRegDevicesWnd.createNewRegisteredDevice(1,"+302108196543");
	}

	protected void setTraceLevel() {
		// TODO Auto-generated method stub
		if (rdbtnmntmInfo.isSelected()){
			LogManager.getRootLogger().setLevel(Level.INFO);
			logger.trace("Trace Level:INFO is set");
		}
		if (rdbtnmntmDebug.isSelected()){
			LogManager.getRootLogger().setLevel(Level.DEBUG);
			logger.trace("Trace Level:DEBUG is set");
		}
		if (rdbtnmntmTrace.isSelected()){
			LogManager.getRootLogger().setLevel(Level.TRACE);
			logger.trace("Trace Level:TRACE is set");
		}
	}

	private void updateGUISettings(BackupSettings b) {
		// TODO Auto-generated method stub
		txtCalledPartyNumber.setText(b.CalledNumber);
		txtRemoteIp.setText(b.CalledIPAddress);
		textFieldRemotePort.setText(b.CalledIPPort);
		txtFromOAD.setText(b.CallingNumber);
		if ((b.transport).equalsIgnoreCase("udp")){
			comboBoxUdpTcp.setSelectedIndex(0);
		} else {
			comboBoxUdpTcp.setSelectedIndex(1);
		}
		txtPassword.setText(b.getPassword());
		
		
		
	}

	private void setDestinationLbl(){
		String destination;
		if (chckbxTelUri.isSelected()){
			//Handle Tel URI case
			destination="tel:"+txtCalledPartyNumber.getText()+";phone-context="+txtRemoteIp.getText();
			 
		}else {
			destination=txtSip.getText()+":"+txtCalledPartyNumber.getText()+"@"+txtRemoteIp.getText()+":"+textFieldRemotePort.getText();
			 
		}
		lblRequestLine.setText(destination);
		
	}
	
	private void setTelUriDestinationLbl(){
		setDestinationLbl();
	}
	private String getDestination(){
		return  lblRequestLine.getText();
	}
	
	public String getRemoteIP(){
		return txtRemoteIp.getText();
	}
	public String getRemotePort(){
		return textFieldRemotePort.getText();
	}
	public String getRemotePartyNumber(){
		return txtCalledPartyNumber.getText();
	}
	private void setSystemIPToComboBox(){
		SystemIPs myIPObject=new SystemIPs();
		LinkedList<String> myIPs;
		myIPs=myIPObject.getMyIPsToString();
		Iterator<String> itr=myIPs.iterator();
		while (itr.hasNext()){
			Object obj=itr.next();
			comboBoxMyIPs.addItem((String)obj);
			
		}
		comboBoxMyIPs.setSelectedIndex(0);
		
		
	}
	private void initializeGUIObjects(){
		logger.info("initialize GUI objects started");
		state=STATE_OFF;
		gStreamer=new GStreamerLocation(null,null);
		wav=new WAVLocation();
		config=new Configuration();
		myBackupSet=new BackupSettings();
		codecsList=new LinkedList<voiceConfiguration>();
		setDefaultCodecSettings();
		SIPReqInfo=new SIPRequestsInfo();
		SIPRespInfo=new SIPResponsesInfo();
		my183Response= new Response183();
		my180Response=new Response180();
		my200Response=new Response200();
		myInviteRequest=new RequestInvite();
		myCallFeaturesInfo=new CallFeatures();
		SIPRespInfo.Resp183=my183Response;
		SIPRespInfo.Resp180=my180Response;
		SIPRespInfo.Resp200=my200Response;
		SIPReqInfo.ReqInvite=myInviteRequest;
		myRegisteredDevices=new HashMap<String,RegisteredDevice>();
		myFailoverMode=new FailoverMode();
		
		btnOnOff.setText("ON");
		btnOnOff.setBackground(Color.RED);
		txtCalledPartyNumber.setEnabled(false);
		txtRemoteIp.setEnabled(false);
		txtInviteLine.setEnabled(false);
		txtSip.setEnabled(false);
		textFieldRemotePort.setEnabled(false);
		textArea.setEnabled(false);
		btnMakeCall.setEnabled(false);
		btnReleaseCall.setEnabled(false);
		btnSend183.setEnabled(false);
		btnReInvite.setEnabled(false);
		cmbBoxHoldMode.setEnabled(false);
		cmbBoxReInviteMode.setEnabled(false);
		btnOptions.setEnabled(false);
		showCodec("Codec:N.A");
		chckbxLateSdp.setEnabled(false);
		hasDtmfFirstOrder=false;
		colorSwitch=false;
		chckbxEnableFaileover.setEnabled(false);
		logger.info("initialize GUI objects finished");
	}
	private void setGUIon(){
		state=STATE_ON;
		btnOnOff.setText("OFF");
		btnOnOff.setBackground(Color.GREEN);
		setDestinationLbl();
		txtCalledPartyNumber.setEnabled(true);
		txtRemoteIp.setEnabled(true);
		txtInviteLine.setEnabled(true);
		txtSip.setEnabled(true);
		textFieldRemotePort.setEnabled(true);
		textArea.setEnabled(true);
		setButtonStatusIdle();
		chckbxLateSdp.setEnabled(true);
		chckbxEnableFaileover.setEnabled(true);
		showCodec("");
		
		comboBoxMyIPs.setEnabled(false);
		cmbBoxHoldMode.setEnabled(false);
		cmbBoxReInviteMode.setEnabled(false);
		setTxtLine(SIPHeadersTxt.ResetLines,"");
		comboBoxUdpTcp.setEnabled(false);
	}
	private void setGUIoff(){
		state=STATE_OFF;
		btnOnOff.setText("ON");
		btnOnOff.setBackground(Color.RED);
		txtCalledPartyNumber.setEnabled(false);
		txtRemoteIp.setEnabled(false);
		txtInviteLine.setEnabled(false);
		txtSip.setEnabled(false);
		textFieldRemotePort.setEnabled(false);
		textArea.setEnabled(false);
		btnMakeCall.setEnabled(false);
		btnReleaseCall.setEnabled(false);
		btnSend183.setEnabled(false);
		setButton183Status(SEND183);
		btnReInvite.setEnabled(false);
		btnOptions.setEnabled(false);
		comboBoxMyIPs.setEnabled(true);
		chckbxLateSdp.setEnabled(false);
		cmbBoxHoldMode.setEnabled(false);
		cmbBoxReInviteMode.setEnabled(false);
		comboBoxUdpTcp.setEnabled(true);
		chckbxEnableFaileover.setEnabled(false);
		showCodec("Codec:N.A");
	}
	public void showStatus(String s){
		lblCallStatus.setText(s);
	}
	
	public void showCodec(String s){
		lblCodec.setText(s);
	}
	
	private void append(String s)  {
		StyledDocument document = (StyledDocument) textArea.getDocument();
	     try {
			document.insertString(document.getLength(), s, null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			logger.error("Bad Location Exception when appending text", e);
			e.printStackTrace();
		}

	}
	
	public void display(String s, JTextPane myJText){
		if (colorSwitch){
			displayWithStyle (s, Color.BLACK, myJText);
		} else {
			displayWithStyle (s, Color.GRAY, myJText);
		}
		colorSwitch=!colorSwitch;
	}
	public void display(String s) {
		display(s,textArea);
	}
	
	public void displayCsta(String s){
		display(s,textPaneCsta);
	}
	
	
	
	public void displayWithStyle (String s, Color c, JTextPane myJText){
		StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        StyledDocument document = (StyledDocument) myJText.getDocument();
	     try {
			document.insertString(document.getLength(), s, aset);
			
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			logger.error("Bad Location Exception when appending text", e);
			e.printStackTrace();
		}

	}
	public void displayWithStyle (String s, Color c){
		displayWithStyle(s,c,textArea);
	}
	public String getmyIP(){
		String s;
		s=(String) comboBoxMyIPs.getSelectedItem();
		return s;
		
	}
	
	public GStreamerLocation getGStreamer(){
		return gStreamer;
	}
	public WAVLocation getWAVLocation(){
		return wav;
	}
	public void showITSPSettings(){
		showGStreamerSettings();
		showWAVSettings();
		showConfiguration();
	}
	private void showGStreamerSettings(){
		displayWithStyle("GStreamer Settings:"+gStreamer.getPath()+gStreamer.getFile()+"\n", Color.RED);
	}
	private void showWAVSettings(){
		displayWithStyle("WAV Settings:"+"\n", Color.BLUE);
		displayWithStyle("1)RingBackTone:"+wav.getRingBackTonePath()+wav.getRingBackToneFile()+"\n", Color.BLUE);
		displayWithStyle("2)RingTone:"+wav.getRingTonePath()+wav.getRingToneFile()+"\n", Color.BLUE);
		displayWithStyle("3)TrxPayload:"+"\n", Color.BLUE);
		displayWithStyle("4)RcvPayload"+"\n", Color.BLUE);
	}
	private void showConfiguration(){
		displayWithStyle("Configuration Settings:"+"\n", Color.GREEN);
		displayWithStyle("SIP Server Port:"+config.sipPort+"\n", Color.GREEN);
		displayWithStyle("SIP Server Transport Protocol:"+config.transport+"\n", Color.GREEN);
		displayWithStyle("Audio Base Port:"+config.audioPort+"\n", Color.GREEN);
		displayWithStyle("Video Base Port:"+config.videoPort+"\n", Color.GREEN);
		displayWithStyle("Audio Codec:"+config.audioCodec+"\n", Color.GREEN);
		displayWithStyle("Audio Codec:"+config.videoCodec+"\n", Color.GREEN);
	}
	public void setButtonStatusIdle(){
		btnMakeCall.setEnabled(true);
		btnMakeCall.setText("Call");
		btnReleaseCall.setEnabled(false);
		btnReleaseCall.setText("Reject");
		btnSend183.setEnabled(false);
		setButton183Status(SEND183);
		btnReInvite.setEnabled(false);
		btnOptions.setEnabled(true);
		chckbxLateSdp.setEnabled(true);
		cmbBoxHoldMode.setEnabled(false);
		cmbBoxReInviteMode.setEnabled(false);
		
	}
	public void setButtonStatusMakeCall(){
		btnMakeCall.setEnabled(false);
		//btnMakeCall.setText("Call");
		btnReleaseCall.setEnabled(true);
		btnReleaseCall.setText("Release");
		chckbxLateSdp.setEnabled(false);
		btnOptions.setEnabled(false);
	}
	public void setButtonStatusEstablishedCall(){
		btnMakeCall.setEnabled(false);
		//btnMakeCall.setText("Call");
		btnSend183.setEnabled(true);
		setButton183Status(HOLD);
		btnReleaseCall.setEnabled(true);
		btnReleaseCall.setText("Release");
		btnReInvite.setEnabled(true);
		chckbxLateSdp.setEnabled(false);
		cmbBoxHoldMode.setEnabled(true);
		cmbBoxReInviteMode.setEnabled(true);
		btnOptions.setEnabled(false);
	}
	public void setButtonStatusAnswerCall(){
		btnMakeCall.setEnabled(true);
		btnMakeCall.setText("Answer");
		btnReleaseCall.setEnabled(true);
		btnReleaseCall.setText("Reject");
		chckbxLateSdp.setEnabled(false);
		btnOptions.setEnabled(false);
	}
	public void setButtonStatusSend183(){
		btnSend183.setEnabled(true);
		chckbxLateSdp.setEnabled(false);
	}
	public void setTxtLine(SIPHeadersTxt h,String s){
		String temp="Not Available";
		switch (h){
		case RequestLine:
			txtInviteLine.setText(s);
			break;
		case FromLine:
			txtFromLine.setText(s);
			break;
		case ToLine:
			txtToLine.setText(s);
			break;
		case ContactLine:
			txtContactLine.setText(s);
			break;
		case PAILine:
			txtPaiLine.setText(s);
			break;
		case PPILine:
			txtPpiLine.setText(s);
			break;
		case DiversionLine:
			txtDiversionLine.setText(s);
			break;
		case ResetLines:
			txtInviteLine.setText(temp);
			txtFromLine.setText(temp);
			txtToLine.setText(temp);
			txtContactLine.setText(temp);
			txtPaiLine.setText(temp);
			txtPaiLine.setText(temp);
			txtPpiLine.setText(temp);
			txtDiversionLine.setText(temp);
			break;
		}
			
		
	}
	private String getFromOAD(){
    	String s;
    	s=txtFromOAD.getText();
    	return s;
    }
	
	public Boolean getLateSDP(){
		Boolean status;
		status=chckbxLateSdp.isSelected();
		return status;
	}
	
	public Boolean getTelURI(){
		Boolean status;
		status=chckbxTelUri.isSelected();
		return  status;
	}
	
	private void setDefaultCodecSettings(){
		voiceConfiguration g711A=new voiceConfiguration();
		voiceConfiguration g711U=new voiceConfiguration();
		voiceConfiguration g729=new voiceConfiguration();
		voiceConfiguration g722=new voiceConfiguration();
		voiceConfiguration gsm=new voiceConfiguration();
		voiceConfiguration DTMF=new voiceConfiguration();
		g711A.setVoiceConfig("G711A", "8", "8000", "20",true, 1);
		g711U.setVoiceConfig("G711U", "0", "8000", "20",true, 2);
		g729.setVoiceConfig("G729", "18", "8000", "20",false, 0);
		g722.setVoiceConfig("G722", "9", "8000", "20", true, 3);
		gsm.setVoiceConfig("GSM", "3", "8000", "20",true, 4);
		DTMF.setVoiceConfig("DTMF", "98", "8000", "0", true, 0);
		codecsList.add(g711A);
		codecsList.add(g711U);
		codecsList.add(g729);
		codecsList.add(g722);
		codecsList.add(gsm);
		codecsList.add(DTMF);
		
	}
	
	public boolean getDtmfFirstOrder(){
		return hasDtmfFirstOrder;
	}
	
	public ArrayList<Integer> getAvailableCodecs(){
		//manipulate codecsList and find the supported codecs with the right priority
		ArrayList<Integer> ali=new ArrayList<Integer>();
		TreeMap<Integer, Integer> myMap= new TreeMap<Integer,Integer>();
		Iterator<voiceConfiguration> itr=codecsList.iterator();
		while (itr.hasNext()){
			Object obj=itr.next();
			voiceConfiguration vc=(voiceConfiguration) obj;
			
			if (!(vc.getSetCodec()) ||  vc.getPriority()==0){
				
			}else {
				myMap.put(vc.getPriority(), vc.getVoiceConfig().getPayloadType());
			}
			
		}
		
		for (Map.Entry<Integer, Integer> e : myMap.entrySet()){
			ali.add(e.getValue());
		}
		ali.add(codecsList.getLast().getVoiceConfig().getPayloadType()); //It's always DTMF
		hasDtmfFirstOrder=codecsList.getLast().getHasOverrideOrder();
		return ali;				
	}
	
	private void setButton183Status(int i){
		switch (i){
		case SEND183:
			btnSend183.setText("Send 183");
			btnSend183Status=SEND183;
			break;
		case HOLD:
			btnSend183.setText("Hold");
			btnSend183Status=HOLD;
			break;
		case UNHOLD:
			btnSend183.setText("UnHold");
			btnSend183Status=UNHOLD;
			break;
		}
			
	}
	
	public void refreshRegisteredDeviceWnd(){
		//TODO:RegisteredDevicesWnd
	}

	public String getMyPassword() {
		// TODO Auto-generated method stub
		return txtPassword.getText();
	}
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	
	public void setMonitorCrossRefId(String s){
		lblMonCrossRefId.setText(s);
	}
	
	public void setCallId(String s){
		lblCallid.setText(s);
	}
	
	
}
    
