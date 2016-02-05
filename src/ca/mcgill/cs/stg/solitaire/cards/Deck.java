/*******************************************************************************
 * Solitaire
 *
 * Copyright (C) 2016 by Martin P. Robillard
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
                aCards.add( Card.get( lRank, lSuit ));
            }
		}
	}

	/**
	 * Shuffles the deck.
	 */
	public void shuffle()
	{
		reset();
		Collections.shuffle( aCards );
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
