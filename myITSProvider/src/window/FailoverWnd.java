package window;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import support.FailoverMode;

import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import gov.nist.javax.sip.message.SIPResponse;

import javax.swing.UIManager;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FailoverWnd extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnForbidden;
	private JRadioButton rdbtnRequestTimeout;
	private JRadioButton rdbtnServerInternal;
	private JRadioButton rdbtnServiceUnavailable;
	private JRadioButton rdbtnServerTime;
	private JRadioButton rdbtnBusyEverywhere;
	private JRadioButton rdbtnCallTransactionNotExist;
	private FailoverMode myFailoverMode;
	private JTextField textField;
	private JCheckBox lblRetryAftersecs;

	/**
	 * Create the dialog.
	 */
	public FailoverWnd(FailoverMode failMode) {
		myFailoverMode=failMode;
		setTitle("Failover Modes");
		setBounds(100, 100, 450, 350);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][][][]", "[]"));
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Modes", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
			contentPanel.add(panel, "cell 0 0,grow");
			panel.setLayout(new MigLayout("", "[]", "[][][][][][][]"));
			{
				rdbtnForbidden = new JRadioButton("403 Forbidden");
				rdbtnForbidden.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent arg0) {
						lblRetryAftersecs.setSelected(false);
					}
				});
				panel.add(rdbtnForbidden, "cell 0 0");
				buttonGroup.add(rdbtnForbidden);
			}
			{
				rdbtnRequestTimeout = new JRadioButton("408 Request Timeout");
				rdbtnRequestTimeout.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent arg0) {
						lblRetryAftersecs.setSelected(false);
					}
				});
				panel.add(rdbtnRequestTimeout, "cell 0 1");
				buttonGroup.add(rdbtnRequestTimeout);
			}
			{
				rdbtnServerInternal = new JRadioButton("500 Server Internal Error");
				rdbtnServerInternal.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent arg0) {
						lblRetryAftersecs.setSelected(true);
					}
				});
				panel.add(rdbtnServerInternal, "cell 0 2");
				buttonGroup.add(rdbtnServerInternal);
			}
			{
				rdbtnServiceUnavailable = new JRadioButton("503 Service Unavailable");
				rdbtnServiceUnavailable.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent arg0) {
						lblRetryAftersecs.setSelected(true);
					}
				});
				panel.add(rdbtnServiceUnavailable, "cell 0 3");
				buttonGroup.add(rdbtnServiceUnavailable);
			}
			{
				rdbtnServerTime = new JRadioButton("504 Server Time Out ");
				rdbtnServerTime.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent arg0) {
						lblRetryAftersecs.setSelected(false);
					}
				});
				panel.add(rdbtnServerTime, "cell 0 4");
				buttonGroup.add(rdbtnServerTime);
			}
			{
				rdbtnBusyEverywhere = new JRadioButton("600 Busy Everywhere");
				rdbtnBusyEverywhere.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent arg0) {
						lblRetryAftersecs.setSelected(true);
					}
				});
				panel.add(rdbtnBusyEverywhere, "cell 0 5");
				buttonGroup.add(rdbtnBusyEverywhere);
			}
			{
				rdbtnCallTransactionNotExist= new JRadioButton("481 Call/Transaction Not Exist");
				rdbtnCallTransactionNotExist.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent arg0) {
						lblRetryAftersecs.setSelected(false);
					}
				});
				panel.add(rdbtnCallTransactionNotExist, "cell 0 6");
				buttonGroup.add(rdbtnCallTransactionNotExist);
			}
		}
		{
			lblRetryAftersecs = new JCheckBox("Retry After (secs)");
			contentPanel.add(lblRetryAftersecs, "cell 1 0,alignx trailing");
		}
		{
			textField = new JTextField();
			textField.setText("120");
			contentPanel.add(textField, "cell 2 0,growx");
			textField.setColumns(10);
		}
		
		readFailoverModeAndUpdateGUI(myFailoverMode);
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						updateFailoverMode(myFailoverMode);
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
				setVisible(false);
				dispose();
			}
		}
	}

	protected void updateFailoverMode(FailoverMode failMode) {
		// TODO Auto-generated method stub
		failMode.setRetryAfter(textField.getText());
		failMode.setHasRetryAfter(lblRetryAftersecs.isSelected());
		if (rdbtnForbidden.isSelected()){
			failMode.setFailoverHeader(SIPResponse.FORBIDDEN);
			return;
		}
		if (rdbtnRequestTimeout.isSelected()){
			failMode.setFailoverHeader(SIPResponse.REQUEST_TIMEOUT);
			return;
		}
		if (rdbtnServerInternal.isSelected()){
			failMode.setFailoverHeader(SIPResponse.SERVER_INTERNAL_ERROR);
			return;
		}
		if (rdbtnServiceUnavailable.isSelected()){
			failMode.setFailoverHeader(SIPResponse.SERVICE_UNAVAILABLE);
			return;
		}
		if (rdbtnServerTime.isSelected()){
			failMode.setFailoverHeader(SIPResponse.SERVER_TIMEOUT);
			return;
		}
		if (rdbtnBusyEverywhere.isSelected()){
			failMode.setFailoverHeader(SIPResponse.BUSY_EVERYWHERE);
			return;
		}
		if (rdbtnCallTransactionNotExist.isSelected()){
			failMode.setFailoverHeader(SIPResponse.CALL_OR_TRANSACTION_DOES_NOT_EXIST);
			return;
		}
		
	}

	private void readFailoverModeAndUpdateGUI(FailoverMode failMode) {
		// TODO Auto-generated method stub
		textField.setText(failMode.retryAfter);
		lblRetryAftersecs.setSelected(failMode.hasRetryAfter);
		
		switch (failMode.failoverHeader) {
		case SIPResponse.FORBIDDEN:
			rdbtnForbidden.setSelected(true);
			break;
		case SIPResponse.REQUEST_TIMEOUT:
			rdbtnRequestTimeout.setSelected(true);
			break;
		case SIPResponse.SERVER_INTERNAL_ERROR:
			rdbtnServerInternal.setSelected(true);
			break;
		case SIPResponse.SERVICE_UNAVAILABLE:
			rdbtnServiceUnavailable.setSelected(true);
			break;
		case SIPResponse.SERVER_TIMEOUT:
			rdbtnServerTime.setSelected(true);
			break;
		case SIPResponse.BUSY_EVERYWHERE:
			rdbtnBusyEverywhere.setSelected(true);
			break;
		case SIPResponse.CALL_OR_TRANSACTION_DOES_NOT_EXIST:
			break;
		
		}
		
	}

}
