package window;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import support.GStreamerLocation;
import support.WAVLocation;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WAVSettingsDialog extends JDialog {
	private static Logger logger=Logger.getLogger("VoiceSettingsWnd");
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldRingBackTone;
	private JTextField textFieldRingTone;
	private JTextField textFieldVoicePayload;
	private WAVLocation wavl;
	private String RingBackTonePath;
	private String RingBackToneFile;
	private String RingTonePath;
	private String RingToneFile;
	private String VoicePayloadPath=null;
	private String VoicePayloadFile=null;

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the dialog.
	 */
	public WAVSettingsDialog(WAVLocation w) {
		setTitle("WAV Settings");
		wavl=w;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow][][][][][][]", "[][][][][][]"));
		{
			JLabel lblRingbackTone = new JLabel("RingBack Tone");
			contentPanel.add(lblRingbackTone, "cell 1 0");
		}
		{
			textFieldRingBackTone = new JTextField();
			contentPanel.add(textFieldRingBackTone, "cell 1 1 5 1,growx");
			textFieldRingBackTone.setColumns(10);
		}
		{
			JButton btn1 = new JButton("Browse");
			btn1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					FileDialog openFileDialog=new FileDialog(WAVSettingsDialog.this, "Locate the RingBack Tone  *.wav format", FileDialog.LOAD);
					openFileDialog.setFile("*.wav");
					openFileDialog.setVisible(true);
					RingBackToneFile=openFileDialog.getFile();
					RingBackTonePath=openFileDialog.getDirectory();
					if (RingBackToneFile==null){
						logger.warn("No RingBackTone file selected");
						//TODO: Show an error message
					}
					else {
						
						String path=RingBackTonePath+RingBackToneFile;
						logger.info("RingBackTone:"+path);
						textFieldRingBackTone.setText(path);
						
					}
				}
			});
			contentPanel.add(btn1, "cell 7 1");
		}
		{
			JLabel lblRingTone = new JLabel("Ring Tone");
			contentPanel.add(lblRingTone, "cell 1 2");
		}
		{
			textFieldRingTone = new JTextField();
			contentPanel.add(textFieldRingTone, "cell 1 3 5 1,growx");
			textFieldRingTone.setColumns(10);
		}
		{
			JButton btn2 = new JButton("Browse");
			btn2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					FileDialog openFileDialog=new FileDialog(WAVSettingsDialog.this, "Locate the RingBack Tone  *.wav format", FileDialog.LOAD);
					openFileDialog.setFile("*.wav");
					openFileDialog.setVisible(true);
					RingToneFile=openFileDialog.getFile();
					RingTonePath=openFileDialog.getDirectory();
					if (RingToneFile==null){
						logger.warn("No RingTone file selected");
						//TODO: Show an error message
					}
					else {
						
						String path=RingTonePath+RingToneFile;
						logger.info("RingTone:"+path);
						textFieldRingTone.setText(path);
						
					}
				}
			});
			contentPanel.add(btn2, "cell 7 3");
		}
		{
			JLabel lblVoicePayload = new JLabel("Voice Payload (Don't set if microphone is used)");
			contentPanel.add(lblVoicePayload, "cell 1 4");
		}
		{
			textFieldVoicePayload = new JTextField();
			contentPanel.add(textFieldVoicePayload, "cell 1 5 5 1,growx");
			textFieldVoicePayload.setColumns(10);
		}
		{
			JButton btn3 = new JButton("Browse");
			btn3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					FileDialog openFileDialog=new FileDialog(WAVSettingsDialog.this, "Locate the RingBack Tone  *.wav format", FileDialog.LOAD);
					openFileDialog.setFile("*.wav");
					openFileDialog.setVisible(true);
					VoicePayloadFile=openFileDialog.getFile();
					VoicePayloadPath=openFileDialog.getDirectory();
					if (VoicePayloadFile==null){
						logger.warn("No VoicePayload file selected");
						//TODO: Show an error message
					}
					else {
						
						String path=VoicePayloadPath+VoicePayloadFile;
						logger.info("VoicePayload:"+path);
						textFieldVoicePayload.setText(path);
						
					}
				}
			});
			contentPanel.add(btn3, "cell 7 5");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						wavl.updateWAVPaths(RingBackTonePath, RingBackToneFile, RingTonePath, RingToneFile, VoicePayloadPath, VoicePayloadFile);
						setVisible(false);
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
