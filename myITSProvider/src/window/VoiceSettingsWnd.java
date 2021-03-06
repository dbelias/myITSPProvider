package window;



import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import javax.swing.JLabel;
import javax.swing.JTextField;



import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

import javax.swing.JComboBox;

import splibraries.Configuration;
import support.voiceConfiguration;

public class VoiceSettingsWnd extends JFrame {
	private static Logger logger=Logger.getLogger("VoiceSettingsWnd");
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldG711A_PT;
	private JTextField textFieldG711U_PT;
	private JTextField textFieldG729_PT;
	private JTextField textFieldG722_PT;
	private JTextField textFieldG711A_FS;
	private JTextField textFieldG711U_FS;
	private JTextField textFieldG729_FS;
	private JTextField textFieldG722_FS;
	private JTextField textFieldDTMF_PT;
	private JComboBox<Integer> cmbBoxG711APrio;
	private JComboBox<Integer> cmbBoxG711UPrio;
	private JComboBox<Integer> cmbBoxG722Prio;
	private JComboBox<Integer> cmbBoxG729Prio;
	static  final int MAX_PRIORITY=4; 
	private Configuration config;
	private LinkedList<voiceConfiguration> codecsList;
	private JCheckBox chckbxG711A;
	private JCheckBox chckbxG711U;
	private JCheckBox chckbxG729;
	private JCheckBox chckbxG722;
	

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the frame.
	 */
	public VoiceSettingsWnd(Configuration c, LinkedList<voiceConfiguration> ll) {
		config=c;
		codecsList= ll;
		setTitle("Voice & DTMF Settings");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 569, 344);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblPriority = new JLabel("Priority");
		GridBagConstraints gbc_lblPriority = new GridBagConstraints();
		gbc_lblPriority.insets = new Insets(0, 0, 5, 5);
		gbc_lblPriority.gridx = 0;
		gbc_lblPriority.gridy = 0;
		contentPane.add(lblPriority, gbc_lblPriority);
		
		JLabel lblCodec = new JLabel("Codec");
		GridBagConstraints gbc_lblCodec = new GridBagConstraints();
		gbc_lblCodec.insets = new Insets(0, 0, 5, 5);
		gbc_lblCodec.gridx = 1;
		gbc_lblCodec.gridy = 0;
		contentPane.add(lblCodec, gbc_lblCodec);
		
		JLabel lblPayloadType = new JLabel("Payload Type");
		GridBagConstraints gbc_lblPayloadType = new GridBagConstraints();
		gbc_lblPayloadType.insets = new Insets(0, 0, 5, 5);
		gbc_lblPayloadType.gridx = 2;
		gbc_lblPayloadType.gridy = 0;
		contentPane.add(lblPayloadType, gbc_lblPayloadType);
		
		JLabel lblClockRate = new JLabel("Clock Rate");
		GridBagConstraints gbc_lblClockRate = new GridBagConstraints();
		gbc_lblClockRate.insets = new Insets(0, 0, 5, 5);
		gbc_lblClockRate.gridx = 3;
		gbc_lblClockRate.gridy = 0;
		contentPane.add(lblClockRate, gbc_lblClockRate);
		
		JLabel lblNewLabel = new JLabel("Frame Size");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 4;
		gbc_lblNewLabel.gridy = 0;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel lblPayloadType_1 = new JLabel("Payload Type");
		GridBagConstraints gbc_lblPayloadType_1 = new GridBagConstraints();
		gbc_lblPayloadType_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblPayloadType_1.gridx = 6;
		gbc_lblPayloadType_1.gridy = 0;
		contentPane.add(lblPayloadType_1, gbc_lblPayloadType_1);
		
		cmbBoxG711APrio = new JComboBox<Integer>();
		GridBagConstraints gbc_cmbBoxG711APrio = new GridBagConstraints();
		gbc_cmbBoxG711APrio.insets = new Insets(0, 0, 5, 5);
		gbc_cmbBoxG711APrio.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbBoxG711APrio.gridx = 0;
		gbc_cmbBoxG711APrio.gridy = 1;
		contentPane.add(cmbBoxG711APrio, gbc_cmbBoxG711APrio);
		fillCmbBoxPriorities(cmbBoxG711APrio);
		cmbBoxG711APrio.setSelectedIndex(1);
		
		chckbxG711A = new JCheckBox("G771A");
		chckbxG711A.setSelected(true);
		chckbxG711A.setHorizontalTextPosition(SwingConstants.LEFT);
		GridBagConstraints gbc_chckbxG711A = new GridBagConstraints();
		gbc_chckbxG711A.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxG711A.gridx = 1;
		gbc_chckbxG711A.gridy = 1;
		contentPane.add(chckbxG711A, gbc_chckbxG711A);
		
		textFieldG711A_PT = new JTextField();
		textFieldG711A_PT.setText("8");
		GridBagConstraints gbc_textFieldG711A_PT = new GridBagConstraints();
		gbc_textFieldG711A_PT.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldG711A_PT.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldG711A_PT.gridx = 2;
		gbc_textFieldG711A_PT.gridy = 1;
		contentPane.add(textFieldG711A_PT, gbc_textFieldG711A_PT);
		textFieldG711A_PT.setColumns(10);
		
		textFieldG711A_FS = new JTextField();
		textFieldG711A_FS.setText("20");
		GridBagConstraints gbc_textFieldG711A_FS = new GridBagConstraints();
		gbc_textFieldG711A_FS.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldG711A_FS.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldG711A_FS.gridx = 4;
		gbc_textFieldG711A_FS.gridy = 1;
		contentPane.add(textFieldG711A_FS, gbc_textFieldG711A_FS);
		textFieldG711A_FS.setColumns(10);
		
		JLabel lblDtmfrfc = new JLabel("DTMF-RFC2833");
		GridBagConstraints gbc_lblDtmfrfc = new GridBagConstraints();
		gbc_lblDtmfrfc.anchor = GridBagConstraints.EAST;
		gbc_lblDtmfrfc.insets = new Insets(0, 0, 5, 5);
		gbc_lblDtmfrfc.gridx = 5;
		gbc_lblDtmfrfc.gridy = 1;
		contentPane.add(lblDtmfrfc, gbc_lblDtmfrfc);
		
		textFieldDTMF_PT = new JTextField();
		textFieldDTMF_PT.setText("98");
		GridBagConstraints gbc_textFieldDTMF_PT = new GridBagConstraints();
		gbc_textFieldDTMF_PT.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldDTMF_PT.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldDTMF_PT.gridx = 6;
		gbc_textFieldDTMF_PT.gridy = 1;
		contentPane.add(textFieldDTMF_PT, gbc_textFieldDTMF_PT);
		textFieldDTMF_PT.setColumns(10);
		
		cmbBoxG711UPrio = new JComboBox<Integer>();
		GridBagConstraints gbc_cmbBoxG711UPrio = new GridBagConstraints();
		gbc_cmbBoxG711UPrio.insets = new Insets(0, 0, 5, 5);
		gbc_cmbBoxG711UPrio.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbBoxG711UPrio.gridx = 0;
		gbc_cmbBoxG711UPrio.gridy = 2;
		contentPane.add(cmbBoxG711UPrio, gbc_cmbBoxG711UPrio);
		fillCmbBoxPriorities(cmbBoxG711UPrio);
		cmbBoxG711UPrio.setSelectedIndex(1);
		
		chckbxG711U = new JCheckBox("G711U");
		chckbxG711U.setHorizontalTextPosition(SwingConstants.LEFT);
		GridBagConstraints gbc_chckbxG711U = new GridBagConstraints();
		gbc_chckbxG711U.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxG711U.gridx = 1;
		gbc_chckbxG711U.gridy = 2;
		contentPane.add(chckbxG711U, gbc_chckbxG711U);
		
		textFieldG711U_PT = new JTextField();
		textFieldG711U_PT.setText("0");
		GridBagConstraints gbc_textFieldG711U_PT = new GridBagConstraints();
		gbc_textFieldG711U_PT.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldG711U_PT.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldG711U_PT.gridx = 2;
		gbc_textFieldG711U_PT.gridy = 2;
		contentPane.add(textFieldG711U_PT, gbc_textFieldG711U_PT);
		textFieldG711U_PT.setColumns(10);
		
		textFieldG711U_FS = new JTextField();
		textFieldG711U_FS.setText("20");
		GridBagConstraints gbc_textFieldG711U_FS = new GridBagConstraints();
		gbc_textFieldG711U_FS.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldG711U_FS.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldG711U_FS.gridx = 4;
		gbc_textFieldG711U_FS.gridy = 2;
		contentPane.add(textFieldG711U_FS, gbc_textFieldG711U_FS);
		textFieldG711U_FS.setColumns(10);
		
		cmbBoxG729Prio = new JComboBox<Integer>();
		GridBagConstraints gbc_cmbBoxG729Prio = new GridBagConstraints();
		gbc_cmbBoxG729Prio.insets = new Insets(0, 0, 5, 5);
		gbc_cmbBoxG729Prio.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbBoxG729Prio.gridx = 0;
		gbc_cmbBoxG729Prio.gridy = 3;
		contentPane.add(cmbBoxG729Prio, gbc_cmbBoxG729Prio);
		fillCmbBoxPriorities(cmbBoxG729Prio);
		cmbBoxG729Prio.setSelectedIndex(0);
		
		chckbxG729 = new JCheckBox("G729");
		chckbxG729.setHorizontalTextPosition(SwingConstants.LEFT);
		GridBagConstraints gbc_chckbxG729U = new GridBagConstraints();
		gbc_chckbxG729U.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxG729U.gridx = 1;
		gbc_chckbxG729U.gridy = 3;
		contentPane.add(chckbxG729, gbc_chckbxG729U);
		
		textFieldG729_PT = new JTextField();
		textFieldG729_PT.setText("18");
		GridBagConstraints gbc_textFieldG729_PT = new GridBagConstraints();
		gbc_textFieldG729_PT.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldG729_PT.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldG729_PT.gridx = 2;
		gbc_textFieldG729_PT.gridy = 3;
		contentPane.add(textFieldG729_PT, gbc_textFieldG729_PT);
		textFieldG729_PT.setColumns(10);
		
		textFieldG729_FS = new JTextField();
		textFieldG729_FS.setText("20");
		GridBagConstraints gbc_textFieldG729_FS = new GridBagConstraints();
		gbc_textFieldG729_FS.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldG729_FS.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldG729_FS.gridx = 4;
		gbc_textFieldG729_FS.gridy = 3;
		contentPane.add(textFieldG729_FS, gbc_textFieldG729_FS);
		textFieldG729_FS.setColumns(10);
		
		cmbBoxG722Prio = new JComboBox<Integer>();
		GridBagConstraints gbc_cmbBoxG722Prio = new GridBagConstraints();
		gbc_cmbBoxG722Prio.insets = new Insets(0, 0, 5, 5);
		gbc_cmbBoxG722Prio.fill = GridBagConstraints.HORIZONTAL;
		gbc_cmbBoxG722Prio.gridx = 0;
		gbc_cmbBoxG722Prio.gridy = 4;
		contentPane.add(cmbBoxG722Prio, gbc_cmbBoxG722Prio);
		fillCmbBoxPriorities(cmbBoxG722Prio);
		cmbBoxG722Prio.setSelectedIndex(3);
		
		chckbxG722 = new JCheckBox("G722");
		chckbxG722.setHorizontalTextPosition(SwingConstants.LEFT);
		GridBagConstraints gbc_chckbxG722 = new GridBagConstraints();
		gbc_chckbxG722.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxG722.gridx = 1;
		gbc_chckbxG722.gridy = 4;
		contentPane.add(chckbxG722, gbc_chckbxG722);
		
		textFieldG722_PT = new JTextField();
		textFieldG722_PT.setText("9");
		GridBagConstraints gbc_textFieldG722_PT = new GridBagConstraints();
		gbc_textFieldG722_PT.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldG722_PT.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldG722_PT.gridx = 2;
		gbc_textFieldG722_PT.gridy = 4;
		contentPane.add(textFieldG722_PT, gbc_textFieldG722_PT);
		textFieldG722_PT.setColumns(10);
		
		textFieldG722_FS = new JTextField();
		textFieldG722_FS.setText("20");
		GridBagConstraints gbc_textFieldG722_FS = new GridBagConstraints();
		gbc_textFieldG722_FS.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldG722_FS.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldG722_FS.gridx = 4;
		gbc_textFieldG722_FS.gridy = 4;
		contentPane.add(textFieldG722_FS, gbc_textFieldG722_FS);
		textFieldG722_FS.setColumns(10);
		
		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setCodecsSettings();
				setVisible(false);
				dispose();
			}
		});
		GridBagConstraints gbc_btnApply = new GridBagConstraints();
		gbc_btnApply.gridwidth = 2;
		gbc_btnApply.insets = new Insets(0, 0, 0, 5);
		gbc_btnApply.gridx = 2;
		gbc_btnApply.gridy = 8;
		contentPane.add(btnApply, gbc_btnApply);
		
		JButton btnSetDefault = new JButton("Set Default");
		btnSetDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		GridBagConstraints gbc_btnSetDefault = new GridBagConstraints();
		gbc_btnSetDefault.insets = new Insets(0, 0, 0, 5);
		gbc_btnSetDefault.gridx = 5;
		gbc_btnSetDefault.gridy = 8;
		contentPane.add(btnSetDefault, gbc_btnSetDefault);
	}
	private void fillCmbBoxPriorities(JComboBox<Integer> obj){
		
		for (int i=0;i<=MAX_PRIORITY;i++){
			obj.addItem(i);
		}
	}
	
	private void setCodecsSettings(){
		voiceConfiguration g711A=new voiceConfiguration();
		voiceConfiguration g711U=new voiceConfiguration();
		voiceConfiguration g729=new voiceConfiguration();
		voiceConfiguration g722=new voiceConfiguration();
		voiceConfiguration DTMF=new voiceConfiguration();
		g711A.setVoiceConfig("G711A", textFieldG711A_PT.getText(), "8000", textFieldG711A_FS.getText(),true, (int)cmbBoxG711APrio.getSelectedItem());
		g711U.setVoiceConfig("G711U", textFieldG711U_PT.getText(), "8000", textFieldG711U_FS.getText(),true, (int)cmbBoxG711UPrio.getSelectedItem());
		g729.setVoiceConfig("G729", textFieldG729_PT.getText(), "8000", textFieldG729_FS.getText(),false, (int)cmbBoxG729Prio.getSelectedItem());
		g722.setVoiceConfig("G722", textFieldG722_PT.getText(), "8000", textFieldG722_FS.getText(), true, (int)cmbBoxG722Prio.getSelectedItem());
		DTMF.setVoiceConfig("DTMF", textFieldDTMF_PT.getText(), "8000", "0", true, 0);
		if (chckbxG711A.isSelected()){
			g711A.setPreferred(true);
			config.setAudioCodec(g711A.getVoiceConfig().getPayloadType());
		}
		else if (chckbxG711U.isSelected()){
			g711U.setPreferred(true);
			config.setAudioCodec(g711U.getVoiceConfig().getPayloadType());
		}
		else if (chckbxG729.isSelected()){
			g729.setPreferred(true);
			config.setAudioCodec(g729.getVoiceConfig().getPayloadType());
		}
		else if (chckbxG722.isSelected()){
			g722.setPreferred(true);
			config.setAudioCodec(g722.getVoiceConfig().getPayloadType());
		}
		codecsList.add(g711A);
		codecsList.add(g711U);
		codecsList.add(g729);
		codecsList.add(g722);
		codecsList.add(DTMF);
		
	}
}
