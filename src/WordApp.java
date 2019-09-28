import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;


import java.util.Scanner;
import java.util.concurrent.*;
//model is separate from the view.

public class WordApp {
//shared variables
	static int noWords=4;
	static int totalWords;

   	static int frameX=1000;
	static int frameY=600;
	static int yLimit=480;

	static WordDictionary dict = new WordDictionary(); //use default dictionary, to read from file eventually

	static WordRecord[] words;
	static volatile boolean done = true;  //must be volatile
	static 	Score score = new Score();

	static WordPanel w;
	
	static JLabel missed;
	
	
	public static void setupGUI(int frameX,int frameY,int yLimit) {
		// Frame init and dimensions
    	JFrame frame = new JFrame("WordGame"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(frameX, frameY);
    	
      	JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
      	g.setSize(frameX,frameY);
 
    	
		w = new WordPanel(words,yLimit);
		w.setSize(frameX,yLimit+100);
	    g.add(w);
	    
	    
	    JPanel txt = new JPanel();
	    txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS)); 
	    JLabel caught =new JLabel("Caught: " + score.getCaught() + "    ");
	    missed =new JLabel("Missed:" + score.getMissed()+ "    ");
	    JLabel scr =new JLabel("Score:" + score.getScore()+ "    ");    
	    txt.add(caught);
	    txt.add(missed);
	    txt.add(scr);
    
	    //[snip]
  
	   final JTextField textEntry = new JTextField("",20);
	   textEntry.addActionListener(new ActionListener()
	    {
	      public void actionPerformed(ActionEvent evt) {
	        String text = textEntry.getText();
	        for (int i=0; i<words.length; i++)
	        {
	          	if(words[i].matchWord(text))
	          	{
	          		words[i].resetWord();
	          		score.caughtWord(text.length());
	          		caught.setText("Caught: " + score.getCaught() + "    ");
	          		scr.setText("Score:" + score.getScore()+ "    ");
	          		if(score.getCaught()>=totalWords)
	          		{
	          			done=true;
	          			JFrame victoryFrame = new JFrame();
	          			victoryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	          			victoryFrame.setSize(frameX, frameY);
	          			JPanel victoryPanel = new JPanel(){
	          				
	          				public void paintComponent(Graphics g)
	          				{
	          					g.setColor(Color.black);
	          					g.setFont(new Font("Arial", Font.PLAIN, 26));
	          					g.drawString("Victory", frameX/2-40, frameY/2);
	          					g.setColor(Color.green);
	          					g.drawString("FireWorks.gif", frameX/2-70, frameY/2+40);
	          				}
	          			};
        				victoryPanel.setLayout(new BoxLayout(victoryPanel, BoxLayout.PAGE_AXIS)); 
      					victoryPanel.setSize(frameX,frameY);
      					victoryFrame.add(victoryPanel);
      					victoryFrame.setLocationRelativeTo(null);
      					victoryFrame.setContentPane(victoryPanel);
      					victoryFrame.setVisible(true);
      					
      					//try{Thread.sleep(2000);}catch(Exception ex){System.out.println("Yeet");}
      					//System.exit(0);
          			}
	          		w.run();
          		}
      		}
	          textEntry.setText("");
	          textEntry.requestFocus();
	      }
	    });
	   
	   
	   txt.add(textEntry);
	   txt.setMaximumSize( txt.getPreferredSize() );
	   g.add(txt);
	    
	    JPanel b = new JPanel();
        b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS)); 
	   	JButton startB = new JButton("Start");;
		
			// add the listener to the jbutton to handle the "pressed" event
			startB.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {
		    	  //[snip]
		    	  textEntry.requestFocus();  //return focus to the text entry field
		    	  done = false;
		    	  
		      }
		    });
		JButton endB = new JButton("End");;
			
				// add the listener to the jbutton to handle the "pressed" event
				endB.addActionListener(new ActionListener()
			    {
			      public void actionPerformed(ActionEvent e)
			      {
			    	  done=true;
			    	for (int i=0; i<words.length; i++)
	        		{
	          			words[i].resetWord();
      				}
			    	score.resetScore();
			    	scr.setText("Score:" + score.getScore()+ "    ");
			    	missed.setText("Missed:" + score.getMissed()+ "    ");
			    	caught.setText("Caught: " + score.getCaught() + "    ");
			    	w.run();
			      }
			    });
			    
	    JButton quitB = new JButton("Quit");;
			
				// add the listener to the jbutton to handle the "pressed" event
				quitB.addActionListener(new ActionListener()
			    {
			      public void actionPerformed(ActionEvent e)
			      {
			    	  System.exit(0);
			      }
			    });
		
		b.add(startB);
		b.add(endB);
		b.add(quitB);
		
		g.add(b);
    	
      	frame.setLocationRelativeTo(null);  // Center window on screen.
      	frame.add(g); //add contents to window
        frame.setContentPane(g);     
       	//frame.pack();  // don't do this - packs it into small space
        frame.setVisible(true);

		
	}

	
public static String[] getDictFromFile(String filename) {
		String [] dictStr = null;
		try {
			Scanner dictReader = new Scanner(new FileInputStream(filename));
			int dictLength = dictReader.nextInt();
			//System.out.println("read '" + dictLength+"'");

			dictStr=new String[dictLength];
			for (int i=0;i<dictLength;i++) {
				dictStr[i]=new String(dictReader.next());
				//System.out.println(i+ " read '" + dictStr[i]+"'"); //for checking
			}
			dictReader.close();
		} catch (IOException e) {
	        System.err.println("Problem reading file " + filename + " default dictionary will be used");
	    }
		return dictStr;

	}

	public static void main(String[] args) {
    	
		//deal with command line arguments
		totalWords=Integer.parseInt(args[0]);  //total words to fall
		noWords=Integer.parseInt(args[1]); // total words falling at any point
		assert(totalWords>=noWords); // this could be done more neatly
		String[] tmpDict=getDictFromFile(args[2]); //file of words
		if (tmpDict!=null)
			dict= new WordDictionary(tmpDict);
		
		WordRecord.dict=dict; //set the class dictionary for the words.
		
		words = new WordRecord[noWords];  //shared array of current words
		
		//[snip]
		
		setupGUI(frameX, frameY, yLimit);  
    	//Start WordPanel thread - for redrawing animation

		int x_inc=(int)frameX/noWords;
	  	//initialize shared array of current words

		for (int i=0;i<noWords;i++) {
			words[i]=new WordRecord(dict.getNewWord(),i*x_inc,yLimit);
		}
		
	(new Thread(new Runnable()
	{
		@Override
		public void run()
		{
			while (true)
			{
				if(!done)
				{
					for (int i=0;i<words.length;i++)
					{
						words[i].drop();
						if(words[i].dropped()){words[i].resetWord();score.missedWord();missed.setText("Missed:" + score.getMissed()+ "    ");}
					}
					w.repaint();
					try{Thread.sleep(500);}catch(Exception ex){System.out.println("Yeet");}
				}
			}
		}
	})).start();
	}
	
	
	
	

}
