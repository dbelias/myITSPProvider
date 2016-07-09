package window;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

import core.ITSPListener;

import splibraries.Configuration;
import splibraries.Response180;
import splibraries.Response183;
import support.GStreamerLocation;
import support.SIPHeadersTxt;
import support.SIPRequestsInfo;
import support.SIPResponsesInfo;
import support.SystemIPs;
import support.WAVLocation;
import support.voiceConfiguration;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.util.ArrayList;
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

import org.apache.log4j.Logger;

import javax.swing.event.CaretEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class myITSPmainWnd {
	private static Logger logger=Logger.getLogger("myITSPmainWnd");
	ITSPListener list;
	private JFrame frmMyItspSimulator;
	private JTextField txtDomain;
	private JTextField txtMyIp;
	private JTextField txtCalledPartyNumber;
	private JTextField txtRemoteIp;
	private JTextField txtInviteLine;
	private JTextField txtSip;
	private JTextField textFieldRemotePort;
	private JLabel lblDestination;
	private JLabel lblRequestLine;
	private JComboBox<String> comboBoxMyIPs;
	private JTextArea textArea;
	private JButton btnMakeCall;
	private JButton btnReleaseCall;
	private JLabel lblCallStatus;
	private JButton btnOnOff;
	private int state;
	private GStreamerLocation gStreamer;
	private WAVLocation wav;
	private Configuration config;
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
	private final String Version="V1.2";
	private Response183 my183Response;
	private Response180 my180Response;
	public SIPRequestsInfo SIPReqInfo;
	public SIPResponsesInfo SIPRespInfo;
	private JTextField txtFromOAD;
	private JCheckBox chckbxTelUri;
	private JButton btnReInvite;

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
		frmMyItspSimulator.setBounds(100, 100, 800, 448);
		frmMyItspSimulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 120, 80, 200, 61, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 54, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
		
		txtDomain = new JTextField();
		txtDomain.setText("domain");
		GridBagConstraints gbc_txtDomain = new GridBagConstraints();
		gbc_txtDomain.gridwidth = 3;
		gbc_txtDomain.insets = new Insets(0, 0, 5, 5);
		gbc_txtDomain.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDomain.gridx = 8;
		gbc_txtDomain.gridy = 1;
		frmMyItspSimulator.getContentPane().add(txtDomain, gbc_txtDomain);
		txtDomain.setColumns(10);
		
		JLabel lblDomain = new JLabel("domain");
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
		
		textArea = new JTextArea();
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
				config.setUserPart(getFromOAD());
				list.updateFromAddress(config);
				list.updateCodecList(getAvailableCodecs());
				list.userInput(0,getDestination());
			}
		});
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
				list.updateCodecList(getAvailableCodecs());
				list.userInput(2,null);
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
				list.updateCodecList(getAvailableCodecs());
				list.userInput(0,null);
			}
		});
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
		GridBagConstraints gbc_btnClearText = new GridBagConstraints();
		gbc_btnClearText.insets = new Insets(0, 0, 5, 5);
		gbc_btnClearText.gridx = 11;
		gbc_btnClearText.gridy = 13;
		frmMyItspSimulator.getContentPane().add(btnClearText, gbc_btnClearText);
		
		JMenuBar menuBar = new JMenuBar();
		frmMyItspSimulator.setJMenuBar(menuBar);
		
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
			}
		});
		mnImportHeaders.add(mntmSipRequestHeaders);
		
		JMenuItem mntmSdpHeaders = new JMenuItem("SDP Headers");
		mntmSdpHeaders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		mnImportHeaders.add(mntmSdpHeaders);
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
		codecsList=new LinkedList<voiceConfiguration>();
		setDefaultCodecSettings();
		SIPReqInfo=new SIPRequestsInfo();
		SIPRespInfo=new SIPResponsesInfo();
		my183Response= new Response183();
		my180Response=new Response180();
		SIPRespInfo.Resp183=my183Response;
		SIPRespInfo.Resp180=my180Response;
		
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
		
		comboBoxMyIPs.setEnabled(false);
		setTxtLine(SIPHeadersTxt.ResetLines,"");
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
		btnReInvite.setEnabled(false);
		comboBoxMyIPs.setEnabled(true);
	}
	public void showStatus(String s){
		lblCallStatus.setText(s);
	}
	
	public void display(String s){
		textArea.append(s);
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
		textArea.append("GStreamer Settings:"+gStreamer.getPath()+gStreamer.getFile()+"\n");
	}
	private void showWAVSettings(){
		textArea.append("WAV Settings:"+"\n");
		textArea.append("1)RingBackTone:"+wav.getRingBackTonePath()+wav.getRingBackToneFile()+"\n");
		textArea.append("2)RingTone:"+wav.getRingTonePath()+wav.getRingToneFile()+"\n");
		textArea.append("3)TrxPayload:"+"\n");
		textArea.append("4)RcvPayload"+"\n");
	}
	private void showConfiguration(){
		textArea.append("Configuration Settings:"+"\n");
		textArea.append("SIP Server Port:"+config.sipPort+"\n");
		textArea.append("Audio Base Port:"+config.audioPort+"\n");
		textArea.append("Video Base Port:"+config.videoPort+"\n");
		textArea.append("Audio Codec:"+config.audioCodec+"\n");
		textArea.append("Audio Codec:"+config.videoCodec+"\n");
	}
	public void setButtonStatusIdle(){
		btnMakeCall.setEnabled(true);
		btnMakeCall.setText("Call");
		btnReleaseCall.setEnabled(false);
		btnReleaseCall.setText("Reject");
		btnSend183.setEnabled(false);
		btnReInvite.setEnabled(false);
	}
	public void setButtonStatusMakeCall(){
		btnMakeCall.setEnabled(false);
		//btnMakeCall.setText("Call");
		btnReleaseCall.setEnabled(true);
		btnReleaseCall.setText("Release");
	}
	public void setButtonStatusEstablishedCall(){
		btnMakeCall.setEnabled(false);
		//btnMakeCall.setText("Call");
		btnSend183.setEnabled(false);
		btnReleaseCall.setEnabled(true);
		btnReleaseCall.setText("Release");
		btnReInvite.setEnabled(true);
	}
	public void setButtonStatusAnswerCall(){
		btnMakeCall.setEnabled(true);
		btnMakeCall.setText("Answer");
		btnReleaseCall.setEnabled(true);
		btnReleaseCall.setText("Reject");
	}
	public void setButtonStatusSend183(){
		btnSend183.setEnabled(true);
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
		ali.add(codecsList.getLast().getVoiceConfig().getPayloadType());
		return ali;				
	}
}
    
