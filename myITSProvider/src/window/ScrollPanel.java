package window;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class ScrollPanel extends JPanel implements Runnable{
	private int x;
	private int y;
	private String text;
	private boolean isEnabled;
	private Thread t;

	/**
	 * Create the panel.
	 */
	public ScrollPanel(String s) {
		this.x=50;
		this.y=150;		
		this.text=s;
		this.isEnabled=false;
		setSize(300,400);
	}
	
	


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		startGui();
	}




	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 400, 300);
		g.setColor(Color.BLACK);
		g.drawString(text, x, y);
		//super.paint(arg0);
	}
	
	public void setFlag(boolean b){
		isEnabled=b;
		if (isEnabled){
			t=new Thread(this);
			t.start();
		} else {
			
		}
	}
	
	public void startGui(){
		while (isEnabled){
			while(x<=getWidth()){
				x++;
				y=getHeight()/2;
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						
			}
			while (x>=0){
				x--;
				y=getHeight()/2;
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	
	

}
