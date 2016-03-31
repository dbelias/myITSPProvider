package window;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;

import support.GStreamerLocation;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GStreamerSettingsDialog extends JDialog {
	private static Logger logger=Logger.getLogger("GStreamerSettingsDialog");
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private String GStreamerPath;
	private String filename;
	private GStreamerLocation gsl;

	/**
	 * Launch the application.

	/**
	 * Create the dialog.
	 */
	public GStreamerSettingsDialog(GStreamerLocation g) {
		gsl=g;
		setTitle("GStreamer Settings");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblPathOfGstreamer = new JLabel("Path of GStreamer libraries");
			contentPanel.add(lblPathOfGstreamer, "4, 4");
		}
		{
			JButton btnSetTheGstreamer = new JButton("Set the gstreamer exe file");
			btnSetTheGstreamer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					FileDialog openFileDialog=new FileDialog(GStreamerSettingsDialog.this, "Locate the gst-launche  *.exe format", FileDialog.LOAD);
					openFileDialog.setFile("*.exe");
					openFileDialog.setVisible(true);
					filename=openFileDialog.getFile();
					GStreamerPath=openFileDialog.getDirectory();
					if (filename==null){
						//TODO: Show an error message
						logger.warn("no filename selected");
					}
					else {
						
						String path=GStreamerPath+filename;
						textField.setText(path);
						logger.info("file slected:"+path);
						
					}
				}
			});
			contentPanel.add(btnSetTheGstreamer, "4, 6");
		}
		{
			textField = new JTextField();
			contentPanel.add(textField, "4, 8, fill, default");
			textField.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						logger.info("file confirmed");
						gsl.updateGStreamerLocation(GStreamerPath, filename);
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
						logger.warn("selected file is candelled");
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
