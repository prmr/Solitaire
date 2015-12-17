package ca.mcgill.cs.stg.solitaire.cards;

import java.util.Collections;
import java.util.Stack;

import ca.mcgill.cs.stg.solitaire.cards.Card.Rank;
import ca.mcgill.cs.stg.solitaire.cards.Card.Suit;

/**
 * Models a deck of 52 cards.
 */
public class Deck 
{
	private Stack<Card> aCards;
	
	/**
	 * Creates a new deck of 52 cards, shuffled.
	 */
	public Deck()
	{
		aCards = new Stack<Card>();
		reset();
		shuffle();
	}
	
	private void reset()
	{
		aCards.clear();
		for( Suit lSuit : Suit.values() )
		{
            for( Rank lRank : Rank.values() )
            {
                aCards.add( new Card( lRank, lSuit ));
            }
		}
	}

	/**
	 * Shuffle the deck.
	 */
	public void shuffle()
	{
		reset();
		Collections.shuffle( aCards );
	}
	
	/**
	 * Draws a card from the deck and removes the card from the deck.
	 * @return The card drawn.
	 * @pre initial.size() > 0
	 * @post final.size() == initial.size() - 1
	 */
	public Card draw()
	{
		assert size() > 0;
		return aCards.pop();
	}
	
	/**
	 * @return The number of cards in the deck.
	 */
	public int size()
	{
		return aCards.size();
	}
}
