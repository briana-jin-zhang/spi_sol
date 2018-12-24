/**
 * CardStack.java
 * Representation of one of the ten stacks of the spider solitaire game
 * located at the top of the screen. Is able to remove and add cards based
 * off of the given y value.
 * The CardStack is able to be drawn with Graphics.
 *
 * @author Briana Zhang & Lucy Zheng
 * Teacher: Ishman
 * Date: 2018-05-18
 * Period: 3
 */

import java.util.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

public class CardStack
{
	private List<Card> pile;
	private int x;
	private int numBacks;
	private int stackNum;
	private final int SPACE = 30;
	private final int BACK_SPACE = 10;
	private static final int CARD_WIDTH = 100;
	private static final int CARD_HEIGHT = 140;
	private static final int ARC = 5;
	private boolean ifTopFlipped;
	
	/**
	 * Creates a new pile that represents all the cards inside the stack.
	 * Discerns x-coordinate of the left side of the pile based on the given stack number.
	 * @param stackNum, the number of the stack counted from the left from 0
	 */
    public CardStack(int stackNum)
    {
		pile = new ArrayList<Card>();
		this.stackNum = stackNum;
		x = stackNum * CARD_WIDTH;
		numBacks = 0;
    }
    
    /**
     * @return the x-coordinate of the left side of the stack
     */
    public int getStackX()
    {
    	return x; 
    }
    
    /**
     * @return the current number of cards inside the stack
     */
    public int size()
    {
    	return pile.size();
    }
    
    /**
     * @return whether or not the stack is empty
     */
    public boolean isEmpty()
    {
    	return pile.isEmpty();
    }
    
    /**
     * @return the Cards that are inside the stack
     */
    public ArrayList<Card> getPile()
    {
    	return (ArrayList<Card>) pile;
    }
    
    /**
     * Discerns whether or not a card can be added to the stack 
     * based on the rules of spider solitaire.
     * @param c the card that is tested to be moved onto this stack
     * @return whether or not the card can be moved onto the stack
     */
    public boolean allowMovementOnto(Card card)
    {
    	if (pile.isEmpty())
    		return true;
    	
    	Card top = pile.get(pile.size() - 1);
    	if (card.compareTo(top) == -1)
    		return true;

    	return false;
    }
    
    /**
     * Adds a card, changing its coordinates and information based on
     * if it is being added back or not.
     * @param card the card to be added to the stack
     * @param undo whether or not the card was being added back to the original position
     */
    public void add(Card card, boolean undo)
    {
    	if(undo)
    	{
    		pile.add(card);
    		return;
    	}
    	int bottomY = 0;
    	if(!pile.isEmpty())
    		bottomY = pile.get(pile.size() - 1).getY() + BACK_SPACE;
    	if(card.ifBack())
    		numBacks++;
    	card.changeCoord(x, bottomY);
    	pile.add(card);
    }
    
    /**
     * Adds multiple cards to the stack assuming
     * all the cards were originally face up and from another stack.
     * @param cards
     */
    public void add(ArrayList<Card> cards)
    {
    	int bottomY = 0;
    	if(!pile.isEmpty())
    		bottomY = pile.get(pile.size() - 1).getY() + SPACE;
    	for(int i = 0; i < cards.size(); i++)
    	{
    		Card card = cards.get(i);
        	card.changeCoord(x, bottomY);
        	pile.add(card);
        	bottomY += SPACE;
    	}    	
    }
    
    /**
     * Calculates the cards that are from the y-coordinate downwards
     * @param y the y-coordinate
     * @return the list of cards starting from the y-coordinate
     */
    public ArrayList<Card> getFromY(int y)
    {
    	ArrayList<Card> cards = new ArrayList<>();
    	for(int i = getIndex(y); i < pile.size(); i++)
    		cards.add(pile.get(i));
    	return cards;
    }
    
    /**
     * Removes all cards from a y-coordinate downwards. 
     * If this results in a card being flipped, it is recorded.
     * If not, it is also recorded.
     * @param y the y-coordinate
     * @return the cards that are from the y-coordinate down
     */
    public ArrayList<Card> remove(int y)
    {
    	int i = getIndex(y);
    	if(i < 0)
    		return null;
    	ArrayList<Card> cards = new ArrayList<>();
		for(int j = i; j < pile.size(); j++)
			cards.add(pile.get(j));
		for(int j = pile.size() - 1; j >= i; j--)
			pile.remove(j);
		if(pile.isEmpty())
			return cards;
		Card top = pile.get(pile.size() - 1);
		if(top.ifBack())
		{
			numBacks--;
			top.setBack(false);
			ifTopFlipped = true;
		}
		else
			ifTopFlipped = false;
		return cards;
    }
    
    /**
     * @return whether or not a card was flipped or not 
     * after the last removal of cards
     */
    public boolean ifTopFlipped()
    {
    	return ifTopFlipped;
    }
    
    /**
     * Gets the index of the card at the given y-coordinate
     * @param y the y-coordinate on the screen
     * @return the index of the card that is at that y-coordinate for this stack
     */
    private int getIndex(int y)
    {
    	if(pile.isEmpty())
    		return -1;
    	int bottom = pile.get(pile.size() - 1).getY() + CARD_HEIGHT;
    	if(y > bottom || y < numBacks * BACK_SPACE)
    		return -1;
    	for(int i = pile.size() - 1; i >= 0; i--)
    	{
    		Card card = pile.get(i);
    		Shape rect = card.cardShape();
    		if(rect.contains(x + CARD_WIDTH/2, y))
    				return i;
    	}
    	return -1;
    }
    
    /**
     * With the given y-coordinate, calculates whether or not a selected card
     * and the cards below it can be moved or not according to the rules of spider solitaire.
     * If a card and the cards below it can be moved, it is highlighted in magenta.
     * @param y the y-coordinate of the clicked card
     * @param g the graphics component used to highlight the selected cards
     * @return whether or not the cards be moved or not.
     */
    public boolean canMoveFrom(int y, Graphics2D g)
    {
    	int i = getIndex(y);
    	if(i < 0)
    		return false;
    	Card card = pile.get(i);
    	String suit = card.getSuit();
    	for(int j = i + 1; j < pile.size(); j++)
    	{
    		Card temp = pile.get(j);
    		if(!temp.getSuit().equals(suit) || temp.compareTo(card) != -1)
    			return false;
    		card = temp;
    	}
    	//return true;
    	if(g == null)
    		return true;
    	else
    		highlight(i, g);
    	return true;    	
    }
    
    /**
     * Creates a magenta highlight around the cards that are able to be selected
     * @param i the index of the first card in the stack that can be highlighted
     * @param g the graphics component used to paint the highlight
     */
    private void highlight(int i, Graphics2D g)
    {
    	Card topCard = pile.get(i);
    	int top = topCard.getY();
    	int bottom = pile.get(pile.size() - 1).getY() + CARD_HEIGHT;
    	top -= ARC * 2;
    	int X = x - ARC;
    	RoundRectangle2D rect = new RoundRectangle2D.Double(X, top, CARD_WIDTH + 2*ARC, bottom - top + 2*ARC, ARC, ARC);
    	g.setColor(Color.magenta);
    	g.fill(rect);
    	for(int j = i; j < pile.size(); j++)
    		pile.get(j).draw(g);
    }
    
    /**
     * Draws the CardStack on the GameComponent by drawing each card
     * @param g the graphics component used to draw the cards in the stack
     */
    public void drawStack(Graphics2D g)
    {
    	for(int i = 0; i < pile.size(); i++)
    	{
    		Card card = pile.get(i);
    		card.draw(g);
    	}
    }

    /**
     * If the top card of the stack was flipped in the process to removing 
     * a card/cards, when "undo"-ing the step, it is flipped back.
     */
    public void flipTopBack()
    {
    	Card top = pile.get(pile.size() - 1);
    	top.flip();
    }
    
    /*
     * Removes the top card of the stack
     */
    public void removeTopCard()
    {
    	pile.remove(pile.size() - 1);
    }
}