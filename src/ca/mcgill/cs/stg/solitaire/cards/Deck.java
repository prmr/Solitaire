/*******************************************************************************
 * Solitaire
 *
 * Copyright (C) 2016-2018 by Martin P. Robillard
 *
 * See: https://github.com/prmr/Solitaire
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package ca.mcgill.cs.stg.solitaire.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Models a deck of 52 cards.
 */
public class Deck 
{
	private CardStack aCards;
	
	/**
	 * Creates a new deck of 52 cards, shuffled.
	 */
	public Deck()
	{
		shuffle();
	}
	
	/**
	 * Reinitializes the deck with all 52 cards, and shuffles them.
	 */
	public void shuffle()
	{
		List<Card> cards = new ArrayList<>();
		for( Suit suit : Suit.values() )
		{
            for( Rank rank : Rank.values() )
            {
                cards.add( Card.get( rank, suit ));
            }
		}
		Collections.shuffle(cards);
		aCards = new CardStack(cards);
	}
	
	/**
	 * Places pCard on top of the deck.
	 * @param pCard The card to place on top
	 * of the deck.
	 * @pre pCard !=null
	 */
	public void push(Card pCard)
	{
		assert pCard != null;
		aCards.push(pCard);
	}
	
	/**
	 * Draws a card from the deck and removes the card from the deck.
	 * @return The card drawn.
	 * @pre !isEmpty()
	 */
	public Card draw()
	{
		assert !isEmpty();
		return aCards.pop();
	}
	
	/**
	 * @return True iff there are no cards in the deck.
	 */
	public boolean isEmpty()
	{
		return aCards.isEmpty();
	}
}
