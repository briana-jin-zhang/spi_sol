/**
 * Action.java
 * Stores the action of a player,
 * keeping track of the cards moved,
 * including the x and y coordinates of the cards,
 * whether or not they were drawn from a stack,
 * moved from one stack to another and if the card below was flipped,
 * or if a card was removed from the playing field.
 * @author Briana Zhang & Lucy Zheng
 * Teacher: Ishman
 * Date: 2018-05-18
 * Period: 3
 */

import java.util.*;

public class Action {
	
	/*Cards moved*/
	private ArrayList<Card> cards;
	
	/*previous x and y coordinates of the cards moved*/
	private int oldX;
	private List<Integer> oldYs;
	
	/*Whether or not the action was drawing from the draw stack*/
	private boolean fromDrawStack;
	
	/*The indexes of the CardStack the card was drawn from and placed on*/
	private int oldStack;
	private int newStack;
	
	/*Whether or not a card was flipped below when moving from one CardStack to another*/
	private boolean ifCardBelowFlipped;
	
	/*Whether or not the action completed a stack*/
	private boolean completedStack;

	/**
	 * Representation of the cards moved, their previous positions, 
	 * which stacks it was moved from and to, and whether or not a 
	 * card below was flipped in the oldStack.
	 * If the newStack is -1, that means it was removed from the playing area
	 * @param cards the cards moved
	 * @param oldX the x-coordinate of the cards moved
	 * @param oldYs the y-coordinate of the cards moved
	 * @param oldStack the index of the CardStack the cards were moved from
	 * @param newStack the index of the CardStack the cards were added to
	 * @param ifCardBelowFlipped if the card below the cards was flipped in the oldStack
	 */
	public Action(ArrayList<Card> cards, int oldX, List<Integer> oldYs, int oldStack, int newStack, boolean ifCardBelowFlipped)
	{
		this.cards = cards;
		this.oldX = oldX;
		this.oldYs = oldYs;
		this.oldStack = oldStack;
		this.newStack = newStack;
		this.ifCardBelowFlipped = ifCardBelowFlipped;
		this.completedStack = newStack < 0;
	}
	
	/**
	 * Keeps track of the cards removed from the drawStack
	 * @param cards the cards that were moved from the drawStack
	 */
	public Action(ArrayList<Card> cards)
	{
		this.cards = cards;
		fromDrawStack = true;
	}
	
	/**
	 * @return the cards that were moved
	 */
	public ArrayList<Card> getCards()
	{
		return cards;
	}
	
	/**
	 * @return the oldX coordinate of the cards moved
	 */
	public int getOldX()
	{
		return oldX;
	}
	
	/**
	 * @return the oldY coordinates of the cards moved
	 */
	public List<Integer> getOldYs()
	{
		return oldYs;
	}
	
	/**
	 * @return whether or not the cards were drawn from the drawStack
	 */
	public boolean fromDrawStack()
	{
		return fromDrawStack;
	}
	
	/**
	 * @return the index of the CardStack the cards were moved FROM
	 */
	public int oldStack()
	{
		return oldStack;
	}
	
	/**
	 * @return the index of the CardStack the cards were moved TO
	 */
	public int newStack()
	{
		return newStack;
	}
	
	/**
	 * @return whether or not the top card was flipped in the oldStack
	 */
	public boolean ifCardBelowFlipped()
	{
		return ifCardBelowFlipped;
	}
	
	/**
	 * @return whether or not the action was completing a stack
	 */
	public boolean completedStack()
	{
		return completedStack;
	}
	
	/**
	 * @return the String representation of the action
	 */
	@Override
	public String toString()
	{
		if(fromDrawStack)
			return "fromDrawStack";
		return "from oldStack: " + oldStack + " newStack: " + newStack + " oldX: " 
			+ oldX + " oldY: " + oldYs.toString() 
			+ "\nif oldStack flippedTopCard: " + ifCardBelowFlipped;
   	}
}
