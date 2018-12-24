/**
 * GameComponent.java
 * The game component that handles all moves of the player and generates visuals
 * following the rules of spider solitaire in response. 
 * It can 
 * @author Briana Zhang & Lucy Zheng
 * Teacher: Ishman
 * Date: 2018-05-18
 * Period: 3
 */

import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.JComponent;

public class GameComponent extends JComponent
{
	/*The background shape with its colors and shape*/
	private Shape background;
	private static final Color COL_1 = new Color(41, 121, 77);
	private static final Color COL_2 = new Color(10, 70, 60);
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 800;
	private static GradientPaint grad = new GradientPaint(0, 0, COL_1, WIDTH, HEIGHT, COL_2	, false);
	
	/*Number of stacks and cards per stack*/
	private static final int NUM_STACKS = 10;
	private static final int FIRST_STACKS = 4;
	private static final int SIX_CARDS  = 6;
	private static final int FIVE_CARDS = 5;
	
	/*Normal card width, heigth, and the spacing between face up cards*/
	private static final int CARD_WIDTH = 100;
	private static final int CARD_HEIGHT = 140;
	private static final int CARD_SPACE = 30;
	
	/*Number of cards per suit, number of suits, and total number of cards*/
	private static final int SUIT_CARDS = 13;
	private static final int TOTAL_SUITS = 8;
	private static final int TOTAL_CARDS = 104;
	
	/*Numbers corresponding to card type in the setUp of cards*/
	private static final int SPADE_NUM = 0;
	private static final int HEART_NUM = 1;
	private static final int DIAMOND_NUM = 2;
	private static final int CLUB_NUM = 3;
	
	/*Strings corresponding to the card's suit type*/
	private static final String CLUB = "club";
	private static final String SPADE = "spade";
	private static final String HEART = "heart";
	private static final String DIAMOND = "diamond";
	
	/*All of the card stacks (10 of them)*/
	private ArrayList<CardStack> stacks;
	/*The stack to draw the rest of the 50 cards from*/
	private ArrayList<Card> drawStack;
	/*All of the cards*/
	private ArrayList<Card> cards;
	/*All the cards that have been removed from the playing field*/
	private ArrayList<Card> cardsDone;
	
	/*Every action of the player to in case the player wants to redo their moves*/
	private Deque<Action> actions;
	
	/*Whether or not a card was "registered"*/
	private boolean registered;
	
	/*The last registered cards and lastStack cards were from*/
	private ArrayList<Card> lastCards;
	private int lastStack;
	
	/*The last stack that cards were added to*/
	private int lastAddedTo;
	
	/*The y-coordinate of the king card of a stack that is to be removed*/
	private int YofKing;
	
	/*The last registered coordinates*/
	private int mouseX;
	private int mouseY;
	private int suits;
	
	/*Whether or not the game is to be animated*/
	private boolean animate;
	
	/*Spacing between each card in the animation*/
	private static final int SPACING = 10;
	
	/*Amount of space the drawStack is from the edges of the component*/
	private static final int SPACE  = 50;
	/*Shape that represents the drawingStack*/
	private static final Shape deckArea = new RoundRectangle2D.Double(WIDTH - CARD_WIDTH - SPACE, HEIGHT -
			CARD_HEIGHT - SPACE, CARD_WIDTH, CARD_HEIGHT, 5, 5);

	/**
	 * Generates a GameComponent that is in charge of all the graphics.
	 * @param suits the number of suits a player wants in a game
	 */
    public GameComponent(int suits)
    {
    	this.suits = suits;
    	actions = new LinkedList<>();
    	drawStack = new ArrayList<>();
    	cardsDone = new ArrayList<>();
    	background = new Rectangle2D.Double(0, 0, WIDTH, HEIGHT);
    	lastCards = null;
    	
    	setUpCards();
    }

    /**
     * "Paints" the current situation of the game.
     * 	If it is animating, it only paints the animated done cards;
     * 	Normally, it draws the cards of each CardStack, 
     * 	the cards in the drawing stack,
     * 	and the finished cards (although it only displays the last 13 cards added). 
     * 	@param g the graphics component used to draw everything
     */
    @Override
    public void paintComponent(Graphics g)
    {
    	Graphics2D gr = (Graphics2D) g;
    	gr.setPaint(grad);
    	gr.fill(background);
    	
    	if(animate)
    	{
    		for(Card card: cardsDone)
    			card.draw(gr);
    		return;
    	}
    	
    	for (CardStack temp : stacks)
    		temp.drawStack(gr);
    	
    	for (Card card : drawStack)
    		card.draw(gr);
    	
    	if(!cardsDone.isEmpty())
    	{
    		int index = cardsDone.size() - 1;
        	for(int k = 0; k < SUIT_CARDS; k++)
        	{
        		Card card = cardsDone.get(index);
        		card.changeCoord(SPACE * (k + 1), HEIGHT - CARD_HEIGHT - SPACE);
        		card.draw(gr);
        		index--;
        	}
    	}    	
    	
    	if(registered)
    		register(gr);
    }
    
    /**
     * Sets up the original set-up of the cards according to the rules of spider solitaire
     */
    private void setUpCards()
    {
    	stacks = new ArrayList<>();
    	cardsDone = new ArrayList<>();
    	animate = false;
    	while (stacks.size() < NUM_STACKS)
    		stacks.add(new CardStack(stacks.size()));
    	cards = new ArrayList<>();
    	generateCards();
    	fillStacks();
    }
    
    /**
     * Generates randomly all 104 cards used based off of 
     * the number of suits a player wants and the rules of spider solitaire.
     */
    private void generateCards()
    {
    	//randomization of cards
    	int K = TOTAL_SUITS / suits;
    	for(int k = 0; k < K; k++)
    	{
    		for(int i = 0; i < suits; i++)
        	{
        		for(int j = 1; j <= 13; j++)
        		{
        			String suit = "";
        			if(i == SPADE_NUM)
        				suit = SPADE;
        			else if(i == HEART_NUM)
        				suit = HEART;
        			else if(i == DIAMOND_NUM)
        				suit = DIAMOND;
        			else
        				suit = CLUB;
        			Card card = new Card(0, 0, suit, j, true);
        			cards.add(card);
        		}
        	}
    	}
    	Collections.shuffle(cards);
    }
    
    /**
     * Fills all the stacks of the game with the generated cards 
     * in accordance to the rules of spider solitaire.
     * This means the first 4 stacks are each filled with 6 cards 
     * with only the top card face up,
     * and the last 6 stacks are each filled with 5 cards
     * with only the top card face up.
     */
    private void fillStacks()
    {
    	int index = 0;
    	for(int i = 0; i < FIRST_STACKS; i++)
    	{
    		CardStack stack = stacks.get(i);
    		for(int j = 1; j <= SIX_CARDS; j++)
    		{
    			Card card = cards.get(index);
    			if(j == SIX_CARDS)
    				card.setBack(false);
    			stack.add(card, false);
    			index++;
    		}
    	}
    	for(int i = FIRST_STACKS; i < stacks.size(); i++)
    	{
    		CardStack stack = stacks.get(i);
    		for(int j = 1; j <= FIVE_CARDS; j++)
    		{
    			Card card = cards.get(index);
    			if(j == FIVE_CARDS)
    				card.setBack(false);
    			stack.add(card, false);
    			index++;
    		}
    	}
    	while(index < cards.size())
    	{
    		cards.get(index).changeCoord(WIDTH - CARD_WIDTH - SPACE, HEIGHT - CARD_HEIGHT - SPACE);
    		drawStack.add(cards.get(index));
    		index++;
    	}
    }
    
    /**
     * Registers clicks based on if they are the first or second click of card(s). 
     * In response to the first click, if the cards are moveable 
     * according to the rules of spider solitaire, they are highlighted in magenta.
     * In response to the second click, if the lastCards registered are able to be
     * moved onto the second stack clicked on, then they will be moved on. Otherwise,
     * all clicks are unregistered.
     * @param x the x-coordinate the mouse clicked
     * @param y the y-coordinate the mouse clicked
     */
    public void mouseClicked(int x, int y)
    {
    	if(registered)
    		secondMouseClick(x);
    	else
    	{
    		registered = true;
    		this.mouseX = x;
    		this.mouseY = y;
    		repaint();
    	}
    }
    
    /**
     * Registers the "second" mouse click. Checks to see if the lastCards registered
     * can be moved onto the newly clicked card. If moveable according to the rules of solitaire,
     * the cards are moved. Otherwise, the lastCards are unregistered.
     * @param x the x-coordinate clicked on
     */
    private void secondMouseClick(int x)
    {
		registered = false;
		int i = 0;
		while(x > (i + 1) * CARD_WIDTH)
			i++;
		CardStack newStack = stacks.get(i);
		if(newStack.allowMovementOnto(lastCards.get(0)))
			moveCardOnto(i);
    	if (checkIfRemoveCards())
    		removeCards();
    	repaint();
    }
    
    /**
     * "Registers" the cards that have been selected. This means calculating
     *  stack the mouse clicked based off of the x-coordinate the mouse clicked, 
     *  then calculating the cards selected based off of the y-coordinate the mouse clicked.
     * @param gr used to highlight the selected cards in magenta.
     */
    private void register(Graphics2D gr)
    {
		int i = 0;
		while(mouseX > (i + 1) * CARD_WIDTH)
			i++;
		CardStack stack = stacks.get(i);
		lastStack = i;
		if(stack.canMoveFrom(mouseY, gr))
			lastCards = stack.getFromY(mouseY);
		else
			registered = false;
    }
    
    /**
     * "Unregisters" the lastCards clicked so that cards must be re-registered
     */
    public void unregister()
    {
    	registered = false;
    }
    
    /**
     * "Moves" the lastCards registered into the newly clicked card.
     * Registers each old coordinate just in case user wants to "undo" their last action.
     * @param i the index of the stack that the lastCards are being moved onto
     */
    private void moveCardOnto(int i)
    {    	
    	CardStack oldStack = stacks.get(lastStack);
    	CardStack newStack = stacks.get(i);
		oldStack.remove(mouseY);
		List<Integer> oldYs = new ArrayList<>();
		for(int k = 0; k < lastCards.size(); k++)
			oldYs.add(lastCards.get(k).getY());
		Action action = new Action(lastCards, lastCards.get(0).getX(), oldYs, lastStack, i, oldStack.ifTopFlipped());
		actions.push(action);
		newStack.add(lastCards);
		lastAddedTo = i;
    }
    
    /**
     * Removes 10 cards from the drawStack to place onto the CardStacks.
     * Each card's old position is stored into an action in case the player wants to undo.
     * Then, it checks if cards can be removed from the board aka having 13 cards from
     * King to Ace of the same suit.
     */
    public void clickDeck()
    {
    	if(drawStack.isEmpty())
    		return;
    	ArrayList<Card> removed = new ArrayList<>();
    	for (int k = 0; k < stacks.size(); k++)
    	{
    		CardStack stack = stacks.get(k);
    		Card added = drawStack.remove(drawStack.size() - 1);
    		added.flip();
    		removed.add(added);
    		ArrayList<Card> card = new ArrayList<>();
    		card.add(added);
    		stack.add(card);
    	}
    	Action action = new Action(removed);
    	actions.push(action);
    	for(int i = 0; i < NUM_STACKS; i++)
    	{
    		lastAddedTo = i;
    		if(checkIfRemoveCards())
    			removeCards();
    	}
    	repaint();
    }
    
    /**
     * Checks to see if a mouse click will draw from the stack
     * and whether or not there are cards in the stack to be drawn
     * @param mouseX the x-coordinate of the mouse click
     * @param mouseY the y-coordinate of the mouse click
     * @return whether or not the drawStack can be "drawn" from
     */
    public boolean hasDrawCard(int mouseX, int mouseY)
    {
    	if (deckArea.contains(mouseX, mouseY) && !drawStack.isEmpty())
    		return true;
    	return false;
    }

    /**
     * Looks at the CardStack that had cards last added to in order to check
     * if there exists face up cards from king to Ace in decreasing order
     * @return whether or not cards should be removed from a stack and onto cardsDone
     */
    public boolean checkIfRemoveCards()
    {
    	CardStack lastAdded = stacks.get(lastAddedTo);
    	ArrayList<Card> pile = lastAdded.getPile();
    	if(pile.isEmpty())
    		return false;
    	Card card = pile.get(pile.size() - 1);
    	int index = lastAdded.size() - 1;
    	String suit = card.getSuit();
    	for(int k = 1; k < SUIT_CARDS; k++)
    	{
    		index--;
    		Card nextCard = null;
    		if(index >= 0)
    			nextCard = pile.get(index);
    		if (nextCard == null && card.compareTo(nextCard) != -1 || nextCard.ifBack() || !suit.equals(nextCard.getSuit()))
    			return false;
    		card = nextCard;
    	}
    	YofKing = card.getY() + CARD_SPACE/2;
    	return true;
    }
    
    /**
     * Removes cards so that they are out of play. This happens after 
     * a player connects 13 cards of decreasing order of the same suit.
     * The old position of the cards are recorded just in case the player
     * wants to undo his or her last move.
     */
    public void removeCards()
    {
    	CardStack temp = stacks.get(lastAddedTo);
		ArrayList<Card> removed = temp.remove(YofKing);
		List<Integer> oldYs = new ArrayList<>();
		for (Card card : removed)
		{
			oldYs.add(card.getY());
			cardsDone.add(card);
		}
		Action action = new Action(removed, temp.getStackX(), 
				oldYs, lastAddedTo, -1, temp.ifTopFlipped());
		actions.push(action);
    }
    
    /**
     * @return if any of the CardStacks are empty because in the rules of spider solitaire,
     * one cannot draw cards from the drawStack if one of the 10 CardStacks is empty
     */
    public boolean anyStackEmpty()
    {
    	for (CardStack temp : stacks)
    	{
    		if (temp.isEmpty())
    			return true;
    	}
    	return false;
    }
    
    /**
     * @return whether or not all the cards have been removed
     */
    public boolean isOver()
    {
    	return cardsDone.size() == TOTAL_CARDS;
    }
    
    /**
     *	Resets the original setUp of the cards and clears the previous actions
     */
    public void reset()
    {
    	setUpCards();
    	actions = new LinkedList<>();
    	repaint();
    }
    
    /**
     * Undoes an action
     */
    public void undo()
    {
    	if(actions.isEmpty())
    		return;
    	Action action = actions.pop();
    	if(action.fromDrawStack())
    		undoDrawStack(action);
    	else if(action.completedStack())
    		undoCompletedStack(action);
    	else
    		undoMovingStack(action);
    	repaint();
    }
    
    /**
     * Undoes a previous action of drawing from the stack.
     * Removes every card added to the CardStacks by the last action
     * and places them back onto the drawing stack
     * @param the previous action that had drawn from the stack.
     */
    private void undoDrawStack(Action action)
    {
    	ArrayList<Card> cards = action.getCards();
    	for(int i = cards.size() - 1; i >= 0; i--)
    	{
    		Card card = cards.get(i);
    		card.changeCoord(WIDTH - CARD_WIDTH - SPACE, HEIGHT -
    				CARD_HEIGHT - SPACE);
    		card.flip();
			drawStack.add(card);
			CardStack stack = stacks.get(i);
			stack.removeTopCard();  			
    	}
    }
    
    /**
     * Removes the cards from the cardsDone
     * and places them back in their original positions.
     * Then calls on undo again in order to return to
     * the previous state from the player's viewpoint. 
     * @param action the previous action that had removed the "completed" cards to cardsDone
     */
    private void undoCompletedStack(Action action)
    {
    	ArrayList<Card> cards = action.getCards();
    	List<Integer> oldYs = action.getOldYs();
    	int oldX = action.getOldX();
    	CardStack oldStack = stacks.get(action.oldStack());
    	if(action.ifCardBelowFlipped())
			oldStack.flipTopBack();
    	for(int i = 0; i < cards.size(); i++)
		{
			Card card = cards.get(i);
			card.changeCoord(oldX, oldYs.get(i));
			oldStack.add(card, true);
			cardsDone.remove(cardsDone.size() - 1);
		}
    	undo();
    }
    
    /**
     * Removes the last cards added to a CardStack and places them back to their original oldStack
     * @param action the previous action that moved a stack of cards onto another CardStack
     */
    private void undoMovingStack(Action action)
    {
    	ArrayList<Card> cards = action.getCards();
    	List<Integer> oldYs = action.getOldYs();
    	int oldX = action.getOldX();
    	CardStack oldStack = stacks.get(action.oldStack());
		CardStack newStack = stacks.get(action.newStack());
		if(action.ifCardBelowFlipped())
			oldStack.flipTopBack();
		for(int i = 0; i < cards.size(); i++)
		{
			Card card = cards.get(i);
			card.changeCoord(oldX, oldYs.get(i));
			oldStack.add(card, true);
			newStack.removeTopCard();
		}
    }
    
    /**
     * Sets up the positions of the finished card for the start of the animation
     */
    public void setUpAnimation()
    {
    	animate = true;
    	int index = 0;
    	int newX = WIDTH/2 - CARD_WIDTH/2;
    	int newY = HEIGHT/2 - CARD_HEIGHT/2;
    	int dX;
    	int dY;
    	for(int i = 0; i < TOTAL_SUITS; i++)
    	{
    		if(i == 0 || i == 3 || i == 5)
    			dX = 0 - SPACING;
    		else if(i == 1 || i == 6)
    			dX = 0;
    		else
    			dX = SPACING;
    		if(i < 3)
    			dY = 0 - SPACING;
    		else if(i < 5)
    			dY = 0;
    		else
    			dY = SPACING;
    		for(int j = 0; j < SUIT_CARDS; j++)
    		{
    			Card card = cardsDone.get(index);
    			card.changeCoord(newX + dX*j, newY + dY*j);
    			index++;
    		}
    	}
    	repaint();
    }
    
    /**
     * Moves the cards with spacing in different directions
     */
    public void moveCards()
    {
    	int index = 0;
    	int dX;
    	int dY;
    	for(int i = 0; i < TOTAL_SUITS; i++)
    	{
    		if(i == 0 || i == 3 || i == 5)
    			dX = 0 - SPACING;
    		else if(i == 1 || i == 6)
    			dX = 0;
    		else
    			dX = SPACING;
    		if(i < 3)
    			dY = 0 - SPACING;
    		else if(i < 5)
    			dY = 0;
    		else
    			dY = SPACING;
    		for(int j = 0; j < SUIT_CARDS; j++)
    		{
    			Card card = cardsDone.get(index);
    			card.move(dX, dY);
    			index++;
    		}
    	}
    	repaint();
    }
    
    /**
     * Checks if any of the cards' coordinates exceed the component's height and width
     * @return whether or not the animation ends
     */
    public boolean animationOver()
    {
    	int index = 0;
    	for(int i = 0; i < TOTAL_SUITS; i++)
    	{
    		for(int j = 0; j < SUIT_CARDS; j++)
    		{
    			Card card = cardsDone.get(index);
    			if(card.getX() < 0 || card.getX() + CARD_WIDTH > WIDTH
    					|| card.getY() < 0 || card.getY() + CARD_HEIGHT > HEIGHT)
    				return true;
    			index++;
    		}
    	}
    	return false;
    }    
    
    /**
     * Forces the game to always have the same width and height so resizing is not possible
     */
    @Override
    public Dimension getPreferredSize()
    {
    	return new Dimension(WIDTH, HEIGHT);
    }

}