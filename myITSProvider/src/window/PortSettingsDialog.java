package window;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import splibraries.Configuration;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PortSettingsDialog extends JDialog {
	private static Logger logger=Logger.getLogger("PortSettingsDialog");
	private final JPanel contentPanel = new JPanel();
	private JTextField txtFieldSIPPortServer;
	private JTextField txtFieldAudioBasePort;
	private JTextField txtFieldVideoBasePort;
	private Configuration config;

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the dialog.
	 */
	public PortSettingsDialog(Configuration c) {
		config=c;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][][grow][][][][][][][]", "[][][]"));
		{
			JLabel lblSipPortServer = new JLabel("SIP Port Server");
			contentPanel.add(lblSipPortServer, "cell 1 0,alignx trailing");
		}
		{
			txtFieldSIPPortServer = new JTextField();
			txtFieldSIPPortServer.setText("5060");
			contentPanel.add(txtFieldSIPPortServer, "cell 2 0,growx");
			txtFieldSIPPortServer.setColumns(10);
		}
		{
			JLabel lblSteering = new JLabel("Steering");
			contentPanel.add(lblSteering, "cell 9 0");
		}
		{
			JLabel lblAudioBasePort = new JLabel("Audio Base Port");
			contentPanel.add(lblAudioBasePort, "cell 1 1,alignx trailing");
		}
		{
			txtFieldAudioBasePort = new JTextField();
			txtFieldAudioBasePort.setText("4000");
			contentPanel.add(txtFieldAudioBasePort, "cell 2 1,growx");
			txtFieldAudioBasePort.setColumns(10);
		}
		{
			JLabel lblVideoBasePort = new JLabel("Video Base Port");
			contentPanel.add(lblVideoBasePort, "cell 1 2,alignx trailing");
		}
		{
			txtFieldVideoBasePort = new JTextField();
			txtFieldVideoBasePort.setEnabled(false);
			txtFieldVideoBasePort.setText("-1");
			contentPanel.add(txtFieldVideoBasePort, "cell 2 2,growx");
			txtFieldVideoBasePort.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						logger.debug("Audio Port/Video Port/SIP Port="+txtFieldAudioBasePort.getText()+"/"+txtFieldVideoBasePort.getText()+"/"+txtFieldSIPPortServer.getText());
						config.setAudioPort(txtFieldAudioBasePort.getText());
						config.setVideoPort(txtFieldVideoBasePort.getText());
						config.setSIPPort(txtFieldSIPPortServer.getText());
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
