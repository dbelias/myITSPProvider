package window;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import support.ComboBoxItem;
import support.LoadRepeatMode;
import support.LoadSettingsHandler;

import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JSeparator;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;

import org.apache.log4j.Logger;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.awt.Component;
import javax.swing.SwingConstants;

public class LoadSettingsWnd extends JDialog {
	private static Logger logger=Logger.getLogger("LoadSettingsWnd");

	private final JPanel contentPanel = new JPanel();
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField textFieldTimes;
	private JTextField textFieldMinutes;
	private JTextPane textField;
	private JComboBox<ComboBoxItem> cmbBxScheduledCsta;
	private JLabel lblIndex;
	private JLabel labelTotalItems;
	private JSlider slider;
	private final String KEY_INDEX="Index:";
	private int mySelectedItem;
	private JButton btnDelete;
	private JRadioButton rdbtnRepeatAfterXTimes;
	private JRadioButton rdbtnRepeatForXMinutes;
	private JRadioButton rdbtnRepeatTillTime;
	

	
	public LoadSettingsWnd(){
		logger.trace("LoadSettingsWnd is initialized");
		intializeWindow();
		setRepeatSettings();
		setScheduledCstaRequestsToComboBox();
	}

	private void setRepeatSettings() {
		// TODO Auto-generated method stub
		textFieldTimes.setText(""+LoadSettingsHandler.times);
		textFieldMinutes.setText(""+LoadSettingsHandler.minutes);
		setGUIRepeatMode();
		
		
		
	}

	private void setGUIRepeatMode() {
		// TODO Auto-generated method stub
		switch (LoadSettingsHandler.myRepeatMode){
		case TIMES:
			rdbtnRepeatAfterXTimes.setSelected(true);			
			break;
		case MINUTES:
			rdbtnRepeatForXMinutes.setSelected(true);
			break;
		case TIMESTAMP:
			rdbtnRepeatTillTime.setSelected(true);
			break;
		default:
				
		}
		
	}

	private void setScheduledCstaRequestsToComboBox() {
		logger.trace("set data to ComboBox for scheduled CSTA messages");
		Set<Integer> myKeySet=LoadSettingsHandler.myScheduledCstaRequestMap.keySet();
		for (int myKey: myKeySet){
			cmbBxScheduledCsta.addItem(new ComboBoxItem(LoadSettingsHandler.myScheduledCstaRequestMap.get(myKey).myCstaMessage.getDescription(),myKey));
		}
		labelTotalItems.setText(""+LoadSettingsHandler.myScheduledCstaRequestMap.size());
	}

	/**
	 * Create the dialog.
	 */
	private void intializeWindow() {
		setTitle("Load Settings");
		setBounds(100, 100, 628, 459);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow][grow][][][][][][][][][][][][][][]", "[][][][][][][][][grow][][][][grow,fill][]"));
		{
			JLabel lblSelectRepeatMode = new JLabel("Select Repeat Mode");
			contentPanel.add(lblSelectRepeatMode, "cell 1 0");
		}
		{
			rdbtnRepeatAfterXTimes = new JRadioButton("Repeat after x times");
			buttonGroup.add(rdbtnRepeatAfterXTimes);
			contentPanel.add(rdbtnRepeatAfterXTimes, "cell 1 1");
		}
		{
			textFieldTimes = new JTextField();
			textFieldTimes.setText("100");
			contentPanel.add(textFieldTimes, "cell 2 1,growx");
			textFieldTimes.setColumns(10);
		}
		{
			JLabel lblAlignement = new JLabel("Alignement");
			contentPanel.add(lblAlignement, "cell 16 1");
		}
		{
			rdbtnRepeatForXMinutes = new JRadioButton("Repeat for x minutes");
			buttonGroup.add(rdbtnRepeatForXMinutes);
			contentPanel.add(rdbtnRepeatForXMinutes, "cell 1 2");
		}
		{
			textFieldMinutes = new JTextField();
			textFieldMinutes.setText("60");
			contentPanel.add(textFieldMinutes, "cell 2 2,growx");
			textFieldMinutes.setColumns(10);
		}
		{
			rdbtnRepeatTillTime = new JRadioButton("Repeat till time");
			buttonGroup.add(rdbtnRepeatTillTime);
			contentPanel.add(rdbtnRepeatTillTime, "cell 1 3");
		}
		{
			JButton btnSet = new JButton("Set");
			btnSet.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					logger.trace("Set Repeat mode");
					LoadSettingsHandler.setTimes(textFieldTimes.getText());
					LoadSettingsHandler.setMinutes(textFieldMinutes.getText());
					setRepeatMode();
					
					
				}
			});
			contentPanel.add(btnSet, "cell 14 3");
		}
		{
			JSeparator separator = new JSeparator();
			contentPanel.add(separator, "cell 1 4");
		}
		{
			JLabel lblScheduledCstaMessages = new JLabel("Scheduled CSTA Messages");
			contentPanel.add(lblScheduledCstaMessages, "cell 1 5");
		}
		{
			JLabel lblItems = new JLabel("Items:");
			contentPanel.add(lblItems, "cell 2 5,alignx right");
		}
		{
			labelTotalItems = new JLabel("#");
			contentPanel.add(labelTotalItems, "cell 3 5");
		}
		{
			lblIndex = new JLabel("Index:#");
			contentPanel.add(lblIndex, "cell 0 6,alignx trailing");
		}
		{
			cmbBxScheduledCsta = new JComboBox<ComboBoxItem>();
			cmbBxScheduledCsta.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					ComboBoxItem myItem=(ComboBoxItem) cmbBxScheduledCsta.getSelectedItem();
					if (myItem!=null){
						mySelectedItem=myItem.getValue();
						lblIndex.setText(KEY_INDEX+mySelectedItem);
						textField.setText(LoadSettingsHandler.getScheduledCstaRequestToString(mySelectedItem));
						slider.setValue(LoadSettingsHandler.myScheduledCstaRequestMap.get(mySelectedItem).weight);
					}
					if (cmbBxScheduledCsta.getItemCount()>0){
						btnDelete.setEnabled(true);
					}else {
						lblIndex.setText(KEY_INDEX);
						textField.setText("");
						btnDelete.setEnabled(false);
					}
					
					
				}
			});
			contentPanel.add(cmbBxScheduledCsta, "cell 1 6,growx");
		}
		{
			btnDelete = new JButton("Delete");
			btnDelete.setEnabled(false);
			btnDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					LoadSettingsHandler.deleteScheduledCstaRequest(mySelectedItem);
					
					cmbBxScheduledCsta.removeItemAt(cmbBxScheduledCsta.getSelectedIndex());
					if (cmbBxScheduledCsta.getItemCount()>0){
						cmbBxScheduledCsta.setSelectedIndex(0);
					}else {
						btnDelete.setEnabled(false);
						lblIndex.setText(KEY_INDEX);
						textField.setText("");
					}
					
					labelTotalItems.setText(""+LoadSettingsHandler.myScheduledCstaRequestMap.size());
					
				}
			});
			contentPanel.add(btnDelete, "cell 2 6");
		}
		{
			JLabel lblWeight = new JLabel("Weight");
			contentPanel.add(lblWeight, "cell 2 7,alignx right");
		}
		{
			slider = new JSlider();
			slider.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					int weightValue=slider.getValue();
					LoadSettingsHandler.setWeightAtScheduledCstaRequest(mySelectedItem, weightValue);
				}
			});
			slider.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
			slider.setValue(1);
			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			slider.setMajorTickSpacing(1);
			slider.setMinorTickSpacing(1);
			slider.setMaximum(10);
			slider.setMinimum(1);
			contentPanel.add(slider, "cell 5 7 9 1,growx");
		}
		{
			textField = new JTextPane();
			
			textField.setEditable(false);
			textField.setAlignmentY(Component.TOP_ALIGNMENT);
			textField.setAlignmentX(Component.LEFT_ALIGNMENT);
			JScrollPane scrollPane2 = new JScrollPane(textField);
			scrollPane2.setAlignmentY(Component.TOP_ALIGNMENT);
			scrollPane2.setAlignmentX(Component.LEFT_ALIGNMENT);
			contentPanel.add(scrollPane2, "cell 2 8 13 5,grow");
			//textField.setColumns(10);
		}
		{
			JButton btnReset = new JButton("Reset");
			btnReset.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					LoadSettingsHandler.resetMap();
					cmbBxScheduledCsta.removeAllItems();
					lblIndex.setText(KEY_INDEX);
					textField.setText("");
					labelTotalItems.setText(""+LoadSettingsHandler.myScheduledCstaRequestMap.size());
					
				}
			});
			contentPanel.add(btnReset, "cell 1 13,alignx center");
		}
		
	}

	protected void setRepeatMode() {
		// TODO Auto-generated method stub
		LoadRepeatMode myMode=null;
		if (rdbtnRepeatForXMinutes.isSelected()){
			myMode=LoadRepeatMode.MINUTES;
		}else if(rdbtnRepeatAfterXTimes.isSelected()){
			myMode=LoadRepeatMode.TIMES;
		}else {//default value
			myMode=LoadRepeatMode.TIMES;
		}
		LoadSettingsHandler.setRepeatMode(myMode);
		
	}

}
