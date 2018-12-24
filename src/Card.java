/**
 * Card.java
 * Representation of a physical playing card. Is able to have a front and back side.
 * Front side consists of a red card with white borders. Each Card has a suit 
 * (club, spade, diamond, heart) and number ranging from 1 to 13. 
 * The card is able to be drawn from Graphics. 
 *
 * @author Briana Zhang & Lucy Zheng
 * Teacher: Ishman
 * Date: 2018-05-18
 * Period: 3
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

public class Card implements Comparable
{
	/*Self generated color red*/
	private static Color RED = new Color(222, 34, 38);
	
	/*Width and height of the card*/
	private static final int H = 140;
	private static final int W = 100;
	/*Amount of arc*/
	private static final int ARC = 5;
	/*Diameter of the large circles*/
	private static final int DIAM = W / 3;
	/*Space from the top of the card for the smaller graphics*/
	private static final int SPACE = 13;
	/*Ratio of the diamond's height to width*/
	private static final double RATIO = 5.0 / 4;
	
	/*String representation of type of suit*/
	private static final String CLUB = "club";
	private static final String SPADE = "spade";
	private static final String HEART = "heart";
	private static final String DIAMOND = "diamond";
	
	/*Represents the x and y coordinate of the top left of the card*/
	private int x;
	private int y;

	/*Represents the number of the card and type of suit*/
	private int num;
	private String suit;
	
	/*Color of the card based on suit*/
	private Color color;
	
	/*Whether or not the card is face down*/
	private boolean ifBack;

	/** Creates a representation of a card using the given coordinates and 
	 * 	information about its suit and number, and whether or not it is facing up
	 * @param x the x-coordinate of the top left corner of the card
	 * @param y the y-coordinate of the top left corner of the card
	 * @param suit the type of card (club, spade, heart, diamond)
	 * @param num the number of the card
	 * @param ifBack if the card is faced down or not
	 */
	public Card(int x, int y, String suit, int num, boolean ifBack)
    {
    	this.ifBack = ifBack;
    	this.x = x;
    	this.y = y;
    	this.suit = suit;
    	this.num = num;
    	if(suit.equalsIgnoreCase(CLUB) || suit.equalsIgnoreCase(SPADE))
    		color = Color.BLACK;
    	else
    		color = RED;
    }

	/**
	 * @return if the card is facing down or not
	 */
	public boolean ifBack()
	{
		return ifBack;
	}
	
	/**
	 * "flips" the card by changing whether or not 
	 * 	the card is facing down
	 */
	public void flip()
	{
		ifBack = !ifBack;
	}
	
	/**
	 * @return the suit of the card
	 */
	public String getSuit()
	{
		return suit;
	}
	
	/**
	 * @return the number of the card
	 */
	public int getNum()
	{
		return num;
	}
	
	/**
	 * @return the x-coordinate of the top left corner of the card
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * @return the y-coordinate of the top left corner of the card
	 */
	public int getY()
	{
		return y;
	}
	
	/**
	 * Changes whether or not the card is face up
	 * @param ifBack
	 */
	public void setBack(boolean ifBack)
	{
		this.ifBack = ifBack;
	}
	
	/**
	 * Changes the coordinates of the top left corner of the card 
	 * with the given x and y values
	 * @param newX the new x-coordinate of the top left corner of the card
	 * @param newY the new x-coordinate of the top left corner of the card
	 */
	public void changeCoord(int newX, int newY)
	{
		x = newX;
		y = newY;
	}
	
	/**
	 * Moves the card with the given change in the x direction and y direction
	 * @param dx amount of change in the x direction
	 * @param dy amount of change in the y direction
	 */
	public void move(int dx, int dy)
	{
		x += dx;
		y += dy;
	}
	
	/**
	 * Gives a physical representation of the area the card occupies
	 * @return the RoundRectangle that takes up the area of the card
	 */
	public RoundRectangle2D cardShape()
	{
		RoundRectangle2D temp = new RoundRectangle2D.Double(x, y, W, H, ARC, ARC);
		return temp;
	}

	/**
	 * Draws the card based off of its suit and number
	 * @param gr the graphics component used to draw the card
	 */
	public void draw(Graphics2D gr)
	{
		RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, W, H, ARC, ARC);
    	gr.setColor(Color.WHITE);
		gr.fill(rect);
		RoundRectangle2D innerRect = new RoundRectangle2D.Double(x + ARC, y + ARC, 
				W - 2 * ARC, H - 2* ARC, ARC, ARC);
		if(ifBack)
		{
    		gr.setColor(RED);
    		gr.fill(innerRect);
		}
		else
		{
			gr.setColor(Color.BLACK);
			gr.draw(innerRect);
			gr.setColor(color);
			if(suit.equalsIgnoreCase(CLUB))
				drawClub(gr);
			else if(suit.equalsIgnoreCase(SPADE))
				drawSpade(gr);
			else if(suit.equalsIgnoreCase(HEART))
				drawHeart(gr);
			else
				drawDiamond(gr);
			drawBigNum(gr);
			drawSmallNum(gr);
		}
	}

	/**
	 * Draws the club design, three circles and one triangle, twice.
	 * A small version in the top left corner and a big version in the middle of the card. 
	 * @param gr the graphics component used to draw the card
	 */
	private void drawClub(Graphics2D gr)
	{
		//small club
		y -= SPACE;
		gr.fillOval(x + W/4 - DIAM/4, y + H/4 - DIAM/2, DIAM/2, DIAM/2);
		gr.fillOval(x + W/4 - DIAM/2, y + H/4 - DIAM/4, DIAM/2, DIAM/2);
		gr.fillOval(x + W/4, y + H/4 - DIAM/4, DIAM/2, DIAM/2);
		int[] X = {x + W/4, x + W/4 - DIAM/4, x + W/4 + DIAM/4};
		int[] Y = {y + H/4, y + H/4 + DIAM/2, y + H/4 + DIAM/2};
		gr.fillPolygon(X, Y, X.length);
		y += SPACE;
		//big club
		gr.fillOval(x + W/2 - DIAM/2, y + H/2 - DIAM, DIAM, DIAM);
		gr.fillOval(x + W/2 - DIAM, y + H/2 - DIAM/2, DIAM, DIAM);
		gr.fillOval(x + W/2, y + H/2 - DIAM/2, DIAM, DIAM);
		int[] Xs = {x + W/2, x + W/2 - DIAM/2, x + W/2 + DIAM/2};
		int[] Ys = {y + H/2, y + H/2 + DIAM, y + H/2 + DIAM};
		gr.fillPolygon(Xs, Ys, Xs.length);
	}
	
	/**
	 * Draws the spade design, two circles and two triangles, twice.
	 * A small version in the top left corner and a big version in the middle of the card. 
	 * @param gr the graphics component used to draw the card
	 */
	private void drawSpade(Graphics2D gr)
	{
		//tiny spade
		y -= SPACE;
		gr.fillOval(x + W/4 - DIAM/2, y + H/4 - DIAM/4, DIAM/2, DIAM/2);
		gr.fillOval(x + W/4, y + H/4 - DIAM/4, DIAM/2, DIAM/2);
		int[] X1s = {x + W/4, x + W/4 - DIAM/4, x + W/4 + DIAM/4};
		int[] Y1s = {y + H/4, y + H/4 + DIAM/2, y + H/4 + DIAM/2};
		gr.fillPolygon(X1s, Y1s, X1s.length);
		int[] X2s = {x + W/4, x + W/4 - DIAM/2, x + W/4 + DIAM/2};
		int[] Y2s = {y + H/4 - DIAM/2, y + H/4, y + H/4};
		gr.fillPolygon(X2s, Y2s, X2s.length);
		y += SPACE;
		//big spade
		gr.fillOval(x + W/2 - DIAM, y + H/2 - DIAM/2, DIAM, DIAM);
		gr.fillOval(x + W/2, y + H/2 - DIAM/2, DIAM, DIAM);
		int[] Xs = {x + W/2, x + W/2 - DIAM/2, x + W/2 + DIAM/2};
		int[] Ys = {y + H/2, y + H/2 + DIAM, y + H/2 + DIAM};
		gr.fillPolygon(Xs, Ys, Xs.length);
		int[] X = {x + W/2, x + W/2 - DIAM, x + W/2 + DIAM};
		int[] Y = {y + H/2 - DIAM, y + H/2, y + H/2};
		gr.fillPolygon(X, Y, Xs.length);
	}
	
	/**
	 * Draws the heart design, two circles and a triangle, twice.
	 * A small version in the top left corner and a big version in the middle of the card. 
	 * @param gr the graphics component used to draw the card
	 */
	private void drawHeart(Graphics2D gr)
	{
		gr.fillOval(x + W/2 - DIAM, y + H/2 - DIAM, DIAM, DIAM);
		gr.fillOval(x + W/2, y + H/2 - DIAM, DIAM, DIAM);
		int[] Xs = {x + W/2 - DIAM, x + W/2 + DIAM, x + W/2};
		int[] Ys = {y + H/2 - DIAM/2, y + H/2 - DIAM/2, y + H/2 + DIAM};
		gr.fillPolygon(Xs, Ys, Xs.length);
		//little heart
		y -= SPACE;
		gr.fillOval(x + W/4 - DIAM/2, y + H/4 - DIAM/2, DIAM/2, DIAM/2);
		gr.fillOval(x + W/4, y + H/4 - DIAM/2, DIAM/2, DIAM/2);
		int[] X = {x + W/4 - DIAM/2, x + W/4 + DIAM/2, x + W/4};
		int[] Y = {y + H/4 - DIAM/4, y + H/4 - DIAM/4, y + H/4 + DIAM/2};
		gr.fillPolygon(X, Y, X.length);
		y += SPACE;
	}
	
	/**
	 * Draws the diamond design twice.
	 * A small version in the top left corner and a big version in the middle of the card. 
	 * @param gr the graphics component used to draw the card
	 */
	private void drawDiamond(Graphics2D gr)
	{
		int[] Xs = {x + W/2, x + W/2 - DIAM, x + W/2, x + W/2 + DIAM};
		int[] Ys = {y + H/2 - (int)(DIAM*RATIO),
				y + H/2, y + H/2 + (int)(DIAM*RATIO), y + H/2};
		gr.fillPolygon(Xs, Ys, Xs.length);
		//smaller diamond
		y -= SPACE;
		int[] X = {x + W/4, x + W/4 - DIAM/2, x + W/4, x + W/4 + DIAM/2};
		int[] Y = {y + H/4 - (int)(DIAM*RATIO)/2 + ARC, y + H/4 + ARC, 
				y + H/4 + (int)(DIAM*RATIO)/2 + ARC, y + H/4 + ARC};
		gr.fillPolygon(X, Y, X.length);
		y += SPACE;
	}
	
	/**
	 * Draws the large number of the card in the center 
	 * @param gr the graphics component used to draw the card
	 */
	private void drawBigNum(Graphics2D gr)
	{
		Font f = new Font("Century Gothic", Font.BOLD, 40);
		gr.setFont(f);
		gr.setColor(Color.WHITE);
		String number = num + "";
		if(num == 1)
			number = "A";
		else if(num == 11)
			number = "J";
		else if(num == 12)
			number = "Q";
		else if(num == 13)
			number = "K";
		int X = x + W/2 - ARC*2;
		int Y = y + H/2 + ARC*2;
		if(num == 10)
			X -= ARC/2;
		if(num == 10 || num == 12)
			X -= ARC * 2;
		else if(num == 1)
			X -= ARC;
		gr.drawString(number, X, Y);
	}
	
	/**
	 * Draws the smaller number in the top left corner
	 * @param gr the graphics component used to draw the card
	 */
	private void drawSmallNum(Graphics2D gr)
	{
		Font f = new Font("Century Gothic", Font.BOLD, 20);
		gr.setFont(f);
		gr.setColor(Color.WHITE);
		String number = num + "";
		if(num == 1)
			number = "A";
		else if(num == 11)
			number = "J";
		else if(num == 12)
			number = "Q";
		else if(num == 13)
			number = "K";
		int X = x + W/4 - ARC;
		int Y = y + H/4 - ARC;
		if(num == 10 || num == 12)
			X -= ARC;
		else if(num == 1)
			X -= ARC/2;				
		gr.drawString(number, X, Y);
	}
	
	/**
	 * @return the difference between the 
	 * number of this card and another card
	 */
	@Override
	public int compareTo(Object other)
    {
    	if (other instanceof Card)
    	{
    		Card temp = (Card)(other);
    		return this.num - temp.num;
    	}
    	return 0;
    }
    
	/**
	 * @return the String representation of the card
	 */
	@Override
	public String toString()
	{
		return suit + " " + num;
   	}

}