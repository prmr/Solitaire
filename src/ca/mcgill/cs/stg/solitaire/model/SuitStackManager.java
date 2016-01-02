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
import java.util.Stack;

import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.cards.Card.Rank;
import ca.mcgill.cs.stg.solitaire.model.GameModel.SuitStackIndex;

/**
 * Manages the state of the four stacks where completed
 * suits are accumulated.
 */
class SuitStackManager
{
	private Map<SuitStackIndex, Stack<Card>> aStacks = new HashMap<>();
	
	/**
	 * Creates an initialized suit stack manager.
	 */
	SuitStackManager()
	{
		initialize();
	}
	
	/**
	 * Initialize the internal data structures.
	 */
	void initialize()
	{
		for( SuitStackIndex index : SuitStackIndex.values() )
		{
			aStacks.put(index, new Stack<Card>());
		}
	}
	
	/**
	 * @param pSuit
	 * @return True if the stack at index pIndex is empty
	 */
	boolean isEmpty(SuitStackIndex pIndex)
	{
		return aStacks.get(pIndex).isEmpty();
	}
	
	/**
	 * @return True if all cards are in their proper
	 * suit stack.
	 */
	boolean isCompleted()
	{
		for( Stack<Card> stack : aStacks.values())
		{
			if( stack.size() < Rank.values().length )
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @param pIndex The index of the stack to peek
	 * @return The card on top of the stack at index pIndex
	 */
	Card peek(SuitStackIndex pIndex)
	{
		assert !aStacks.get(pIndex).isEmpty();
		return aStacks.get(pIndex).peek();
	}
	
	/**
	 * Push pCard onto the stack corresponding to its
	 * index.
	 * @param pCard The card to push.
	 * @param pIndex The index where to push the card.
	 */
	void push(Card pCard, SuitStackIndex pIndex)
	{
		aStacks.get(pIndex).push(pCard);
	}
	
	/**
	 * Pop the top card of the stack.
	 * @param pIndex the index of the stack to pop
	 * @pre !isEmpty(pSuit)
	 */
	Card pop(SuitStackIndex pIndex)
	{
		assert !isEmpty(pIndex);
		return aStacks.get(pIndex).pop();
	}
}
