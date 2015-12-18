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
import ca.mcgill.cs.stg.solitaire.cards.Card.Suit;

/**
 * Manages the state of the four stacks where completed
 * suits are accumulated.
 */
class SuitStackManager
{
	private Map<Suit, Stack<Card>> aStacks = new HashMap<>();
	
	/**
	 * Creates an initialized suit stack manager.
	 */
	SuitStackManager()
	{
		initialize();
	}
	
	/**
	 * Initialize the inernal data structures.
	 */
	void initialize()
	{
		for( Suit suit : Suit.values() )
		{
			aStacks.put(suit, new Stack<Card>());
		}
	}
	
	/**
	 * @param pSuit
	 * @return True if the top stack for suit pSuit is empty.
	 */
	boolean isEmpty(Suit pSuit)
	{
		return aStacks.get(pSuit).isEmpty();
	}
	
	/**
	 * @param pSuit
	 * @return The card on top of the stack for suit pSuit
	 */
	Card peek(Suit pSuit)
	{
		assert !aStacks.get(pSuit).isEmpty();
		return aStacks.get(pSuit).peek();
	}
	
	/**
	 * Push pCard onto the stack corresponding to its
	 * suit.
	 * @param pCard The card to push.
	 */
	void push(Card pCard)
	{
		aStacks.get(pCard.getSuit()).push(pCard);
	}
	
	/**
	 * Pop the top card of the stack.
	 * @param pSuit The card to pop.
	 * @pre !isEmpty(pSuit)
	 */
	Card pop(Suit pSuit)
	{
		assert !isEmpty(pSuit);
		return aStacks.get(pSuit).pop();
	}
}
