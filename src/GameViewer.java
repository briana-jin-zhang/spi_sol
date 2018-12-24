/**
 * @(#)GameViewer.java
 * lmao jk it works
 * everything works i hope
 * 
 * @author Briana Zhang & Lucy Zheng
 * Teacher: Ishman
 * Date: 2018-05-18
 * Period: 3
 */

import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

public class GameViewer
{
	/*Height and width of frame*/
	private static int FRAME_WIDTH = 1000;
	private static int FRAME_HEIGHT = 900;
	
	/*Game component of the game, frame, panel, and buttons*/
	private static GameComponent game;
	private static JFrame frame;
	private static JPanel panel;
	private static JButton restart;
	private static JButton undo;
	
	/*Timers for when a game has ended, animation starts and ends, 
	 * and to make an option for players to play again to appear
	 */
	private static Timer gameEndedTimer;
	private static Timer animationTimer;
	private static Timer playAgainTimer;
	
	/*Delay of all the timers*/
	private static final int DELAY = 100;

	/** 
	 * Sets up the initial frame and GUI, adding all buttons and panels
	 */
	private static void setup()
	{
		frame = new JFrame();
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    	frame.setTitle("Spider Solitaire");
    	frame.setLocation(0, 0);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    	Object[] options = {"1 Suit", "2 Suit", "4 Suit"};
    	Object selectedValue = JOptionPane.showInputDialog(frame, "Pick an option",
    		"Number of Suits", JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

    	while (selectedValue == null)
    	{
    		frame.setVisible(false);
    		frame.dispose();
    		System.exit(0);
    	}

    	int numSuits = Integer.parseInt(selectedValue.toString().charAt(0) + "");

    	panel = new JPanel();

    	restart = new JButton("Restart");
    	undo = new JButton("Undo");

    	restart.setLayout(null);
    	undo.setLayout(null);

    	game = new GameComponent(numSuits);
    	panel.add(game);
		panel.validate();
    	restart.setEnabled(true);
    	undo.setEnabled(true);

    	panel.add(restart);
    	panel.add(undo);
    	frame.add(panel);

    	frame.setResizable(false);
    	frame.setVisible(true);

	}

    public static void main(String[] args)
	{
		setup();

		ActionListener reset = new RestartListener();
		restart.addActionListener(reset);
		
		ActionListener undoMove = new ButtonClick();
		undo.addActionListener(undoMove);

		MouseListener mouse = new MouseClick();
		panel.addMouseListener(mouse);
		
		ActionListener gameOver = new GameOver();
		gameEndedTimer = new Timer(DELAY, gameOver);
		gameEndedTimer.start();
		
		ActionListener animate = new Animation();
		animationTimer = new Timer(DELAY, animate);
		
		ActionListener playAgain = new PlayAgain();
		playAgainTimer = new Timer(DELAY, playAgain);

	}
    
    /**	
     * Restarts the game when the restart button is clicked and the player selects the yes option
     */
    static class RestartListener implements ActionListener
	{
    	/**	
    	 * Restarts the game when the restart button is clicked and player selects the yes option
       	 */
	  	public void actionPerformed(ActionEvent e)
		{
			Object restart_button = JOptionPane.showConfirmDialog(null, "Restart?", "",
			JOptionPane.YES_NO_OPTION);

			if (restart_button.equals(JOptionPane.YES_OPTION))
			{
				game.reset();
				frame.revalidate();
				frame.repaint();
			}
		}
	}
    
    /**
     * Undoes the last action done by the player
     */
    static class ButtonClick implements ActionListener
	{
    	/**	
    	 * Undo the last action done by the player
         */
	 	@Override
	 	public void actionPerformed(ActionEvent e)
	 	{
	 		game.undo();
	 		frame.repaint();
	 	}
	}
    
    /**
     * Keeps track of when the game is over, starting the action of animation
     */
    static class GameOver implements ActionListener
    {
    	/**
         * Keeps track of when the game is over, starting the action of animation
         */
    	@Override
    	public void actionPerformed(ActionEvent e)
    	{
    		if(game.isOver())
    		{
    			animationTimer.start();
    			game.setUpAnimation();
    			gameEndedTimer.stop();
    		}
    	}
    }
    
    /**
     * Keeps track of when the game is over, starting the action of animation
     */
    static class Animation implements ActionListener
    {
    	/**
         * Keeps track of when the game is over, starting the action of animation
         */
    	@Override
    	public void actionPerformed(ActionEvent e)
    	{
    		game.moveCards();
    		if(game.animationOver())
    		{
    			playAgainTimer.start();
    			animationTimer.stop();
    		}
    	}
    }
    
    /**
     * Restarts the game if the player selects the yes option. Otherwise closes the frame.
     */
    static class PlayAgain implements ActionListener
    {
    	/**
         * Restarts the game if the player selects the yes option. Otherwise closes the frame.
         */
    	@Override
    	public void actionPerformed(ActionEvent e)
    	{
    		Object obj = JOptionPane.showConfirmDialog(null, "Play Again?", "",
				JOptionPane.YES_NO_OPTION);
			if (obj.equals(JOptionPane.YES_OPTION))
			{
				game.reset();
				frame.revalidate();
				frame.repaint();
				playAgainTimer.stop();
			}
			else
			{
				frame.setVisible(false);
	    		frame.dispose();
	    		System.exit(0);
			}
    	}
    }
    
    /** 
     *  Registers the actions of the mouse on the JPanel
     */
    static class MouseClick implements MouseListener
	{
    	/** 
    	 * Registers where the mouse clicks on the JPanel
    	 * Checks if the mouse clicked on the drawingStack
    	 * If one of the CardStacks is empty, a message appears 
    	 * informing the player that is against the rules.
    	 * If the mouse clicks on a CardStack, that is 
    	 * registered and handled in gameComponent
         */
	 	@Override
	 	public void mouseClicked(MouseEvent e)
	 	{
	 		int mouseX = e.getX();
			int mouseY = e.getY();
			if (game.hasDrawCard(mouseX, mouseY))
	 		{
				if (game.anyStackEmpty())
	 				JOptionPane.showMessageDialog(null, "At least one stack is empty. "
	 						+ "Please make sure all stacks are filled");
	 			else
	 			{
	 				game.clickDeck();
		 			game.unregister();
	 			}		 				
	 		}
			else
			{
				game.mouseClicked(mouseX, mouseY);
			}
	 	}
	 	
	 	/** 
	 	 * Registers the action of when the mouse enters an area
	 	 */
	 	@Override
	 	public void mouseEntered(MouseEvent e)
	 	{
	 		return;
	 	}

	 	/**	
	 	 * Registers the action of when the mouse exits an area
	 	 */
	 	@Override
	 	public void mouseExited(MouseEvent e)
	 	{
	 		return;
	 	}

	 	/**	
	 	 * Registers the action of when the mouse is pressed
	 	 */
	 	@Override
	 	public void mousePressed(MouseEvent e)
	 	{
	 		return;
	 	}

	 	/**	
	 	 * Registers the action when the mouse is released
	 	 */
	 	@Override
	 	public void mouseReleased(MouseEvent e)
	 	{
	 		return;
	 	}
	} 
}