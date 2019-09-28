import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;//PDL

import javax.swing.JButton;
import javax.swing.JPanel;

public class WordPanel extends JPanel implements Runnable {
		public static volatile boolean done;
		private WordRecord[] words;
		private int noWords;
		private int maxY;

		
		public void paintComponent(Graphics g) {
		    int width = getWidth();
		    int height = getHeight();
		    g.clearRect(0,0,width,height);
		    g.setColor(Color.red);
		    g.fillRect(0,maxY-10,width,height);

		    g.setColor(Color.black);
		    g.setFont(new Font("Arial", Font.PLAIN, 26));
		    for (int i=0;i<noWords;i++){	    	
		    	g.drawString(words[i].getWord(),words[i].getX(),words[i].getY());
		    }
		   
		  }
		
		WordPanel(WordRecord[] words, int maxY) {
			this.words=words; //James: Will this work?\nMe: Yes
			noWords = words.length;
			done=false;
			this.maxY=maxY;		
		}
		
		public void run() {
			repaint();
		}

	}


