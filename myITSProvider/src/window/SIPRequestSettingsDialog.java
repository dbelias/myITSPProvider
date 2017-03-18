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

import org.apache.log4j.Logger;

import support.HeadersValuesGeneric;
import support.SIPRequestsInfo;

import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.ListIterator;
import java.awt.event.ActionEvent;

public class SIPRequestSettingsDialog extends JDialog {
	private static Logger logger=Logger.getLogger("SIPRequestSettingsDialog");
	private JCheckBox chckbxUserAgentHeader;
	private JTextField txtHeaderInvite;
	private JTextField textValueInvite;
	private JComboBox<String> comboBoxInvite;
	
	private SIPRequestsInfo r;
	private LinkedList<HeadersValuesGeneric> extraHeadersInvite;
	private boolean isModifiedInvite;

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the dialog.
	 */
	public SIPRequestSettingsDialog(SIPRequestsInfo SIPReqInfo) {
		r=SIPReqInfo;
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
				GridBagLayout gbl_panelINVITE = new GridBagLayout();
				gbl_panelINVITE.columnWidths = new int[]{0, 0, 95, 89, 0};
				gbl_panelINVITE.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
				gbl_panelINVITE.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				gbl_panelINVITE.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				panelINVITE.setLayout(gbl_panelINVITE);
				{
					chckbxUserAgentHeader = new JCheckBox("User Agent Header");
					chckbxUserAgentHeader.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							isModifiedInvite=true;
						}
					});
					chckbxUserAgentHeader.setSelected(true);
					GridBagConstraints gbc_chckbxUserAgentHeader = new GridBagConstraints();
					gbc_chckbxUserAgentHeader.insets = new Insets(0, 0, 5, 5);
					gbc_chckbxUserAgentHeader.gridx = 1;
					gbc_chckbxUserAgentHeader.gridy = 1;
					panelINVITE.add(chckbxUserAgentHeader, gbc_chckbxUserAgentHeader);
				}
				{
					JLabel lblAddHeadervalue = new JLabel("Add Additional Header:Value");
					GridBagConstraints gbc_lblAddHeadervalue = new GridBagConstraints();
					gbc_lblAddHeadervalue.insets = new Insets(0, 0, 5, 5);
					gbc_lblAddHeadervalue.anchor = GridBagConstraints.EAST;
					gbc_lblAddHeadervalue.gridx = 1;
					gbc_lblAddHeadervalue.gridy = 2;
					panelINVITE.add(lblAddHeadervalue, gbc_lblAddHeadervalue);
				}
				{
					txtHeaderInvite = new JTextField();
					txtHeaderInvite.setText("Header");
					GridBagConstraints gbc_txtHeader = new GridBagConstraints();
					gbc_txtHeader.insets = new Insets(0, 0, 5, 5);
					gbc_txtHeader.fill = GridBagConstraints.HORIZONTAL;
					gbc_txtHeader.gridx = 2;
					gbc_txtHeader.gridy = 2;
					panelINVITE.add(txtHeaderInvite, gbc_txtHeader);
					txtHeaderInvite.setColumns(10);
				}
				
				textValueInvite = new JTextField();
				textValueInvite.setText("Value");
				GridBagConstraints gbc_textValue = new GridBagConstraints();
				gbc_textValue.insets = new Insets(0, 0, 5, 0);
				gbc_textValue.fill = GridBagConstraints.HORIZONTAL;
				gbc_textValue.gridx = 3;
				gbc_textValue.gridy = 2;
				panelINVITE.add(textValueInvite, gbc_textValue);
				textValueInvite.setColumns(10);
				
				JButton btnAddHeader = new JButton("Add Header");
				btnAddHeader.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						isModifiedInvite=true;
						addComboBoxItem(comboBoxInvite,extraHeadersInvite,txtHeaderInvite.getText(),textValueInvite.getText());
						txtHeaderInvite.setText("");
						textValueInvite.setText("");
					}
				});
				GridBagConstraints gbc_btnAddHeader = new GridBagConstraints();
				gbc_btnAddHeader.insets = new Insets(0, 0, 5, 5);
				gbc_btnAddHeader.gridx = 1;
				gbc_btnAddHeader.gridy = 3;
				panelINVITE.add(btnAddHeader, gbc_btnAddHeader);
				
				comboBoxInvite = new JComboBox();
				GridBagConstraints gbc_comboBox = new GridBagConstraints();
				gbc_comboBox.gridwidth = 2;
				gbc_comboBox.insets = new Insets(0, 0, 5, 5);
				gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
				gbc_comboBox.gridx = 2;
				gbc_comboBox.gridy = 3;
				panelINVITE.add(comboBoxInvite, gbc_comboBox);
				
				JButton btnRemoveHeader = new JButton("Remove Header");
				btnRemoveHeader.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						isModifiedInvite=true;
						removeComboBoxItem(comboBoxInvite,extraHeadersInvite);
						txtHeaderInvite.setText("");
						textValueInvite.setText("");
					}
				});
				GridBagConstraints gbc_btnRemoveHeader = new GridBagConstraints();
				gbc_btnRemoveHeader.insets = new Insets(0, 0, 0, 5);
				gbc_btnRemoveHeader.gridx = 1;
				gbc_btnRemoveHeader.gridy = 4;
				panelINVITE.add(btnRemoveHeader, gbc_btnRemoveHeader);
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
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						saveAll();
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
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		initializeGUIObjectsData();
	}

	private void initializeGUIObjectsData() {
		// TODO Auto-generated method stub
		logger.info("Initialize variables");
		updateInviteTab();
	}

	private void updateInviteTab() {
		// TODO Auto-generated method stub
		isModifiedInvite=false;
		chckbxUserAgentHeader.setSelected(r.ReqInvite.getHasUserAgent());
		extraHeadersInvite=r.ReqInvite.getHeaderValuesList();
		logger.trace("update Invite Tab");
		updateComboBox(comboBoxInvite,extraHeadersInvite);
	}

	private void updateComboBox(JComboBox<String> cmbBx, LinkedList<HeadersValuesGeneric> ll) {
		// TODO Auto-generated method stub
		ListIterator<HeadersValuesGeneric> listIterator= ll.listIterator();
		while (listIterator.hasNext()) {
			Object obj=listIterator.next();
			HeadersValuesGeneric temp=(HeadersValuesGeneric)obj;
			String s=temp.getHeaderValue();
			cmbBx.addItem(s);
			logger.debug(cmbBx.getName()+" update ComboBox");
			
        }
		
	}
	
	private void addComboBoxItem(JComboBox<String> cmbBx, LinkedList<HeadersValuesGeneric> ll, String header, String value){
		HeadersValuesGeneric temp=new HeadersValuesGeneric(header,value);
		ll.add(temp);
		String s=ll.getLast().getHeaderValue(); 
		cmbBx.addItem(s);
		logger.debug(cmbBx.getName()+" add new item:"+s);
		
	}
	private void removeComboBoxItem(JComboBox<String> cmbBx, LinkedList<HeadersValuesGeneric> ll){
		int i=0;
		i=cmbBx.getSelectedIndex();
		ll.remove(i);
		cmbBx.removeItemAt(i);
		logger.debug(cmbBx.getName()+" remove item with index:"+i+" -->"+cmbBx.getItemAt(i));
		
	}

	protected void saveAll() {
		// TODO Auto-generated method stub
		logger.trace("Save All");
		saveSettingsInvite();
		
	}

	private void saveSettingsInvite() {
		// TODO Auto-generated method stub
		if (isModifiedInvite){
			r.ReqInvite.setHasUserAgent(chckbxUserAgentHeader.isSelected());
			
			logger.trace("Save Invite settings");
		}else {
			logger.trace("No changes for INVITE settings to save");
		}
		
	}

	
}
