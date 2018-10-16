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

import java.util.HashMap;
import java.util.Map;

import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.cards.CardStack;
import ca.mcgill.cs.stg.solitaire.cards.Rank;
import ca.mcgill.cs.stg.solitaire.model.GameModel.SuitStackIndex;

/**
 * Represents the four piles that must be completed to win the game, with the ace
 * at the bottom, face up, and all cards of the same suit on top, in sequence.
 */
class FoundationPiles
{
	private final Map<SuitStackIndex, CardStack> aPiles = new HashMap<>();
	
	/**
	 * Creates an initialized FoundationPiles object that consists of four empty piles.
	 */
	FoundationPiles()
	{
		initialize();
	}
	
	/**
	 * @return The total number of cards in all the foundation piles.
	 */
	int getTotalSize()
	{
		int total = 0;
		for( CardStack stack : aPiles.values())
		{
			total += stack.size();
		}
		return total;
	}
	
	/**
	 * Initializes the FoundationPiles object to reset it to four empty piles.
	 */
	void initialize()
	{
		for( SuitStackIndex index : SuitStackIndex.values() )
		{
			aPiles.put(index, new CardStack());
		}
	}
	
	/**
	 * @param pIndex The index of the pile to check.
	 * @return True if the pile at index pIndex is empty
	 */
	boolean isEmpty(SuitStackIndex pIndex)
	{
		return aPiles.get(pIndex).isEmpty();
	}
	
	/**
	 * @param pCard The card to test
	 * @param pIndex The suitstack to test
	 * @return True if pCard can be moved to the top of its suit stack.
	 * This is only possible if its rank is immediately superior
	 * to that of the card currently on top of the suit stack.
	 */
	boolean canMoveTo(Card pCard, SuitStackIndex pIndex )
	{
		assert pCard != null && pIndex != null;
		if( isEmpty(pIndex))
		{
			return pCard.getRank() == Rank.ACE;
		}
		else
		{
			return pCard.getSuit() == peek(pIndex).getSuit() && pCard.getRank().ordinal() == peek(pIndex).getRank().ordinal()+1;
		}
	}
	
	/**
	 * @param pIndex The index of the stack to peek
	 * @return The card on top of the stack at index pIndex
	 */
	Card peek(SuitStackIndex pIndex)
	{
		assert !aPiles.get(pIndex).isEmpty();
		return aPiles.get(pIndex).peek();
	}
	
	/**
	 * Push pCard onto the stack corresponding to its
	 * index.
	 * @param pCard The card to push.
	 * @param pIndex The index where to push the card.
	 */
	void push(Card pCard, SuitStackIndex pIndex)
	{
		aPiles.get(pIndex).push(pCard);
	}
	
	/**
	 * Pop the top card of the stack.
	 * @param pIndex the index of the stack to pop
	 * @pre !isEmpty(pSuit)
	 */
	Card pop(SuitStackIndex pIndex)
	{
		assert !isEmpty(pIndex);
		return aPiles.get(pIndex).pop();
	}
}
