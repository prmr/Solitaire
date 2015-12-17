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
package ca.mcgill.cs.stg.solitaire.model;

import java.util.Stack;

import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.cards.Deck;

/**
 * Manages the state of the deck and its 
 * discard pile.
 */
class DeckManager
{
	private Deck aDeck = new Deck();
	private Stack<Card> aDiscard = new Stack<>();

	/**
	 * Creates an initialized deck manager.
	 */
	DeckManager()
	{
		initialize();
	}
	
	/**
	 * Fills and shuffles the deck and empty
	 * the discard pile.
	 */
	void initialize()
	{
		aDeck.shuffle();
		aDiscard.clear();
	}
	
	/**
	 * @return A card drawn from the deck.
	 * @pre !deckEmpty()
	 */
	Card getCardFromDeck()
	{
		assert !deckEmpty();
		return aDeck.draw();
	}
	
	/**
	 * @return true if and only if there are no more
	 * cards in the deck.
	 */
	boolean deckEmpty()
	{
		return aDeck.size() == 0;
	}
	
	/**
	 * @return true if and only if there are no 
	 * cards in the discard pile.
	 */
	boolean discardPileEmpty()
	{
		return aDiscard.size() == 0;
	}
	
	Card getDiscardPileTop()
	{
		assert !discardPileEmpty();
		return aDiscard.peek();
	}
	
	/**
	 * @param pCard
	 * @return True if pCard is the card on top of the discard pile
	 */
	boolean isOnTopOfDiscardPile(Card pCard)
	{
		return !aDiscard.isEmpty() && aDiscard.peek() == pCard;
	}
	
	/**
	 * Removes the card on top of the discard pile and 
	 * discards it.
	 */
	void popDiscardPile()
	{
		assert !aDiscard.isEmpty();
		aDiscard.pop();
	}
	
	/**
	 * Removes a card from the deck and puts it on top
	 * of the discard pile.
	 * @pre !deckEmpty()
	 */
	void discardFromDeck()
	{
		assert !deckEmpty();
		aDiscard.push(aDeck.draw());
	}
}
