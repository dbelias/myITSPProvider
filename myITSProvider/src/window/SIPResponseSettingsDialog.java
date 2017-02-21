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

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import support.HeadersValuesGeneric;
import support.SIPResponsesInfo;

import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SIPResponseSettingsDialog extends JDialog {
	private static Logger logger=Logger.getLogger("SIPResponseSettingsDialog");
	private SIPResponsesInfo SIPRespInf;
	
	private LinkedList<HeadersValuesGeneric> extraHeaders183;
	private boolean isModified183;
	private JCheckBox chckbxSendSdp183;	
	private JTextField txtHeader183;
	private JTextField txtValue183;
	private JComboBox<String> comboBox183;
	
	private LinkedList<HeadersValuesGeneric> extraHeaders180;
	private boolean isModified180;
	private JCheckBox chckbxSendSdp180;
	private JTextField txtHeader180;
	private JTextField txtValue180;
	private JComboBox<String> comboBox180;
	
	private boolean isModified200;
	private JCheckBox chckbxColpSupport;
	

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the dialog.
	 */
	public SIPResponseSettingsDialog(SIPResponsesInfo SIPRespInfo) {
		initializeVariables(SIPRespInfo);
		
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		{
			
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
			tabbedPane.setBounds(0, 11, 434, 212);
			

			getContentPane().add(tabbedPane);
			{
				JPanel panel180 = new JPanel();
				tabbedPane.addTab("180", null, panel180, null);
				GridBagLayout gbl_panel180 = new GridBagLayout();
				gbl_panel180.columnWidths = new int[]{0, 0, 128, 0, 0};
				gbl_panel180.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
				gbl_panel180.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
				gbl_panel180.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				panel180.setLayout(gbl_panel180);
				{
					chckbxSendSdp180 = new JCheckBox("Send SDP");
					chckbxSendSdp180.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							isModified180=true;
						}
					});
					GridBagConstraints gbc_chckbxSendSdp180 = new GridBagConstraints();
					gbc_chckbxSendSdp180.insets = new Insets(0, 0, 5, 5);
					gbc_chckbxSendSdp180.gridx = 1;
					gbc_chckbxSendSdp180.gridy = 0;
					panel180.add(chckbxSendSdp180, gbc_chckbxSendSdp180);
				}
				{
					JLabel lblAddHeadervalue = new JLabel("Add Additional Header:Value");
					GridBagConstraints gbc_lblAddHeadervalue = new GridBagConstraints();
					gbc_lblAddHeadervalue.anchor = GridBagConstraints.EAST;
					gbc_lblAddHeadervalue.insets = new Insets(0, 0, 5, 5);
					gbc_lblAddHeadervalue.gridx = 1;
					gbc_lblAddHeadervalue.gridy = 2;
					panel180.add(lblAddHeadervalue, gbc_lblAddHeadervalue);
				}
				{
					txtHeader180 = new JTextField();
					txtHeader180.setText("Header");
					GridBagConstraints gbc_txtHeader180 = new GridBagConstraints();
					gbc_txtHeader180.insets = new Insets(0, 0, 5, 5);
					gbc_txtHeader180.fill = GridBagConstraints.HORIZONTAL;
					gbc_txtHeader180.gridx = 2;
					gbc_txtHeader180.gridy = 2;
					panel180.add(txtHeader180, gbc_txtHeader180);
					txtHeader180.setColumns(10);
				}
				{
					txtValue180 = new JTextField();
					txtValue180.setText("Value");
					GridBagConstraints gbc_txtValue180 = new GridBagConstraints();
					gbc_txtValue180.insets = new Insets(0, 0, 5, 0);
					gbc_txtValue180.fill = GridBagConstraints.HORIZONTAL;
					gbc_txtValue180.gridx = 3;
					gbc_txtValue180.gridy = 2;
					panel180.add(txtValue180, gbc_txtValue180);
					txtValue180.setColumns(10);
				}
				{
					JButton btnAddHeader_1 = new JButton("Add Header");
					btnAddHeader_1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							isModified180=true;
							addComboBoxItem(comboBox180,extraHeaders180,txtHeader180.getText(),txtValue180.getText());
							txtHeader180.setText("");
							txtValue180.setText("");
						}
					});
					GridBagConstraints gbc_btnAddHeader_1 = new GridBagConstraints();
					gbc_btnAddHeader_1.insets = new Insets(0, 0, 5, 5);
					gbc_btnAddHeader_1.gridx = 1;
					gbc_btnAddHeader_1.gridy = 3;
					panel180.add(btnAddHeader_1, gbc_btnAddHeader_1);
				}
				{
					comboBox180 = new JComboBox<String>();
					comboBox180.setName("cmbBx180");
					GridBagConstraints gbc_comboBox180 = new GridBagConstraints();
					gbc_comboBox180.gridwidth = 2;
					gbc_comboBox180.insets = new Insets(0, 0, 5, 5);
					gbc_comboBox180.fill = GridBagConstraints.HORIZONTAL;
					gbc_comboBox180.gridx = 2;
					gbc_comboBox180.gridy = 3;
					panel180.add(comboBox180, gbc_comboBox180);
				}
				{
					JButton btnRemoveHeader_1 = new JButton("Remove Header");
					btnRemoveHeader_1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							isModified180=true;
							removeComboBoxItem(comboBox180,extraHeaders180);
							txtHeader180.setText("");
							txtValue180.setText("");
						}
					});
					GridBagConstraints gbc_btnRemoveHeader_1 = new GridBagConstraints();
					gbc_btnRemoveHeader_1.insets = new Insets(0, 0, 0, 5);
					gbc_btnRemoveHeader_1.gridx = 1;
					gbc_btnRemoveHeader_1.gridy = 4;
					panel180.add(btnRemoveHeader_1, gbc_btnRemoveHeader_1);
				}
			}
			{
				JPanel panel183 = new JPanel();
				tabbedPane.addTab("183", null, panel183, null);
				GridBagLayout gbl_panel183 = new GridBagLayout();
				gbl_panel183.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
				gbl_panel183.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
				gbl_panel183.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
				gbl_panel183.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				panel183.setLayout(gbl_panel183);
				{
					chckbxSendSdp183 = new JCheckBox("Send SDP");
					chckbxSendSdp183.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							isModified183=true;
						}
					});
					GridBagConstraints gbc_chckbxSendSdp = new GridBagConstraints();
					gbc_chckbxSendSdp.insets = new Insets(0, 0, 5, 5);
					gbc_chckbxSendSdp.gridx = 1;
					gbc_chckbxSendSdp.gridy = 0;
					panel183.add(chckbxSendSdp183, gbc_chckbxSendSdp);
				}
				{
					JLabel lblAddAdditionalHeader = new JLabel("Add Additional Header: Value");
					GridBagConstraints gbc_lblAddAdditionalHeader = new GridBagConstraints();
					gbc_lblAddAdditionalHeader.anchor = GridBagConstraints.EAST;
					gbc_lblAddAdditionalHeader.insets = new Insets(0, 0, 5, 5);
					gbc_lblAddAdditionalHeader.gridx = 1;
					gbc_lblAddAdditionalHeader.gridy = 3;
					panel183.add(lblAddAdditionalHeader, gbc_lblAddAdditionalHeader);
				}
				
				txtHeader183 = new JTextField();
				txtHeader183.setText("Header");
				GridBagConstraints gbc_txtHeader183 = new GridBagConstraints();
				gbc_txtHeader183.insets = new Insets(0, 0, 5, 5);
				gbc_txtHeader183.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtHeader183.gridx = 2;
				gbc_txtHeader183.gridy = 3;
				panel183.add(txtHeader183, gbc_txtHeader183);
				txtHeader183.setColumns(10);
				
				txtValue183 = new JTextField();
				txtValue183.setText("Value");
				GridBagConstraints gbc_txtValue183 = new GridBagConstraints();
				gbc_txtValue183.insets = new Insets(0, 0, 5, 0);
				gbc_txtValue183.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtValue183.gridx = 4;
				gbc_txtValue183.gridy = 3;
				panel183.add(txtValue183, gbc_txtValue183);
				txtValue183.setColumns(10);
				
				JButton btnAddHeader = new JButton("Add Header");
				btnAddHeader.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						isModified183=true;
						addComboBoxItem(comboBox183,extraHeaders183,txtHeader183.getText(),txtValue183.getText());
						txtHeader183.setText("");
						txtValue183.setText("");
					}
				});
				GridBagConstraints gbc_btnAddHeader = new GridBagConstraints();
				gbc_btnAddHeader.insets = new Insets(0, 0, 5, 5);
				gbc_btnAddHeader.gridx = 1;
				gbc_btnAddHeader.gridy = 4;
				panel183.add(btnAddHeader, gbc_btnAddHeader);
				
				comboBox183 = new JComboBox<String>();
				comboBox183.setName("cmbBx183");
				GridBagConstraints gbc_comboBox183 = new GridBagConstraints();
				gbc_comboBox183.insets = new Insets(0, 0, 5, 0);
				gbc_comboBox183.gridwidth = 3;
				gbc_comboBox183.fill = GridBagConstraints.HORIZONTAL;
				gbc_comboBox183.gridx = 2;
				gbc_comboBox183.gridy = 4;
				panel183.add(comboBox183, gbc_comboBox183);
				
				
				JButton btnRemoveHeader = new JButton("Remove Header");
				btnRemoveHeader.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						isModified183=true;
						removeComboBoxItem(comboBox183,extraHeaders183);
						txtHeader183.setText("");
						txtValue183.setText("");
						
					}
				});
				GridBagConstraints gbc_btnRemoveHeader = new GridBagConstraints();
				gbc_btnRemoveHeader.insets = new Insets(0, 0, 0, 5);
				gbc_btnRemoveHeader.gridx = 1;
				gbc_btnRemoveHeader.gridy = 5;
				panel183.add(btnRemoveHeader, gbc_btnRemoveHeader);
				
				
				
				
			}
			{
				JPanel panel200 = new JPanel();
				tabbedPane.addTab("200", null, panel200, null);
				GridBagLayout gbl_panel200 = new GridBagLayout();
				gbl_panel200.columnWidths = new int[]{0, 0, 0};
				gbl_panel200.rowHeights = new int[]{0, 0, 0};
				gbl_panel200.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
				gbl_panel200.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
				panel200.setLayout(gbl_panel200);
				{
					chckbxColpSupport = new JCheckBox("COLP Support");
					chckbxColpSupport.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							isModified200=true;
						}
					});
					GridBagConstraints gbc_chckbxColpSupport = new GridBagConstraints();
					gbc_chckbxColpSupport.gridx = 1;
					gbc_chckbxColpSupport.gridy = 1;
					panel200.add(chckbxColpSupport, gbc_chckbxColpSupport);
				}
			}
			{
				JPanel panel486 = new JPanel();
				tabbedPane.addTab("486 Busy Here", null, panel486, null);
			}
			{
				JPanel panel404 = new JPanel();
				tabbedPane.addTab("404 Not Found", null, panel404, null);
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
					public void actionPerformed(ActionEvent e) {
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
	private void initializeVariables(SIPResponsesInfo r){
		
		SIPRespInf=r;
		
		
	}
	private void initializeGUIObjectsData(){
		logger.info("Initialize variables");
		update180Tab(SIPRespInf);
		update183Tab(SIPRespInf);
		update200Tab(SIPRespInf);
	}
	private void update200Tab(SIPResponsesInfo r) {
		// TODO Auto-generated method stub
		isModified200=false;
		chckbxColpSupport.setSelected(r.Resp200.getCOLP());
		logger.trace("update 200 Tab");
		
	}
	private void update183Tab(SIPResponsesInfo r){
		
		isModified183=false;
		chckbxSendSdp183.setSelected(r.Resp183.getSendSDP());
		extraHeaders183=r.Resp183.getHeaderValuesList();
		logger.trace("update 183 Tab");
		updateComboBox(comboBox183,extraHeaders183);
	}
private void update180Tab(SIPResponsesInfo r){
		
		isModified180=false;
		chckbxSendSdp180.setSelected(r.Resp180.getSendSDP());
		extraHeaders180=r.Resp180.getHeaderValuesList();
		logger.trace("update 180 Tab");
		updateComboBox(comboBox180,extraHeaders180);
	}
	
	private void updateComboBox(JComboBox<String> cmbBx, LinkedList<HeadersValuesGeneric> ll){
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
		
		private void saveAll(){
			logger.trace("Save All");
			saveSettings183();
			saveSettings180();
			saveSettings200();
			
		}
		private void saveSettings200() {
			if (isModified200){
				SIPRespInf.Resp200.setCOLP(chckbxColpSupport.isSelected());
				logger.trace("Save 200 settings");
			}else {
				logger.trace("No changes for 200 settings to save");
			}
			
		}
		private void saveSettings183(){
			if (isModified183){
				SIPRespInf.Resp183.setSendSDP(chckbxSendSdp183.isSelected());
				logger.trace("Save 183 settings");
			}else {
				logger.trace("No changes for 183 settings to save");
			}
		}
		private void saveSettings180(){
			if (isModified180){
				SIPRespInf.Resp180.setSendSDP(chckbxSendSdp180.isSelected());
				logger.trace("Save 180 settings");
			}else {
				logger.trace("No changes for 180 settings to save");
			}
		}
		
	
}
