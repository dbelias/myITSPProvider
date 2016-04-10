package window;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;

public class SIPRequestSettingsDialog extends JDialog {

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the dialog.
	 */
	public SIPRequestSettingsDialog() {
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
			tabbedPane.setBounds(0, 11, 434, 212);
			

			getContentPane().add(tabbedPane);
			{
				JPanel panelINVITE = new JPanel();
				tabbedPane.addTab("INVITE", null, panelINVITE, null);
			}
			{
				JPanel panelACK = new JPanel();
				tabbedPane.addTab("ACK", null, panelACK, null);
			}
			{
				JPanel panelBYE = new JPanel();
				tabbedPane.addTab("BYE", null, panelBYE, null);
			}
			{
				JPanel panelCANCEL = new JPanel();
				tabbedPane.addTab("CANCEL", null, panelCANCEL, null);
			}
			{
				JPanel panelOPTIONS = new JPanel();
				tabbedPane.addTab("OPTIONS", null, panelOPTIONS, null);
			}
			{
				JPanel panelNOTIFY = new JPanel();
				tabbedPane.addTab("NOTIFY", null, panelNOTIFY, null);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 228, 434, 33);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	


}
