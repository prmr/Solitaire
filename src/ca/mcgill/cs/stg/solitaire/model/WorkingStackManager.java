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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.cards.Card.Rank;
import ca.mcgill.cs.stg.solitaire.cards.Deck;
import ca.mcgill.cs.stg.solitaire.model.GameModel.StackIndex;

/**
 * Manages the state of the bottom stacks where partial
 * suits are accumulated.
 */
class WorkingStackManager
{
	private Map<StackIndex, Stack<Card>> aStacks = new HashMap<>();
	private Set<Card> aVisible = new HashSet<>();
	
	/**
	 * Creates working stacks with no cards in them.
	 */
	WorkingStackManager()
	{
		for( StackIndex index : StackIndex.values() )
		{
			aStacks.put(index, new Stack<Card>());
		}
	}
	
	/**
	 * Fills the working stacks by drawing cards from the deck.
	 */
	void initialize(Deck pDeck)
	{
		aVisible.clear();
		for( int i = 0; i < StackIndex.values().length; i++ )
		{
			aStacks.get(StackIndex.values()[i]).clear();
			for( int j = 0; j < i+1; j++ )
			{
				Card card = pDeck.draw();
				aStacks.get(StackIndex.values()[i]).push(card);
				if( j == i )
				{
					aVisible.add(card);
				}
			}
		}
	}
	
	boolean canMoveTo(Card pCard, StackIndex pIndex )
	{
		Stack<Card> stack = aStacks.get(pIndex);
		if( stack.isEmpty() )
		{
			return pCard.getRank() == Rank.KING;
		}
		else
		{ 
			return pCard.getRank().ordinal() == stack.peek().getRank().ordinal()-1 && 
					!pCard.sameColorAs(stack.peek());
		}
	}
	
	Card[] getStack(StackIndex pIndex)
	{
		return aStacks.get(pIndex).toArray(new Card[aStacks.get(pIndex).size()]);
	}
	
	public Card[] getSequence(Card pCard, StackIndex pIndex)
	{
		Stack<Card> stack = aStacks.get(pIndex);
		List<Card> lReturn = new ArrayList<>();
		boolean aSeen = false;
		for( Card card : stack )
		{
			if( card == pCard )
			{
				aSeen = true;
			}
			if( aSeen )
			{
				lReturn.add(card);
			}
		}
		return lReturn.toArray(new Card[lReturn.size()]);
	}
	
	/**
	 * Removes and returns a sequence of cards starting at 
	 * pCard and running until the top of the stack.
	 * @param pCard The card to start the sequence at.
	 * @param pIndex The stack index.
	 * @return The first card was further from the bottom
	 * of the stack.
	 */
	Card[] removeSequence(Card pCard, StackIndex pIndex)
	{
		Stack<Card> stack = aStacks.get(pIndex);
		List<Card> lReturn = new ArrayList<>();
		boolean aSeen = false;
		for( Card card : stack )
		{
			if( card == pCard )
			{
				aSeen = true;
			}
			if( aSeen )
			{
				lReturn.add(card);
			}
		}
		for( int i = 0; i < lReturn.size(); i++ )
		{
			pop(pIndex);
		}
		return lReturn.toArray(new Card[lReturn.size()]);
	}
	
	boolean contains(Card pCard, StackIndex pIndex)
	{
		for( Card card : aStacks.get(pIndex))
		{
			if( card == pCard )
			{
				return true;
			}
		}
		return false;
	}
	
	boolean contains(Card pCard)
	{
		for( StackIndex index : StackIndex.values())
		{
			if( contains(pCard, index))
			{
				return true;
			}
		}
		return false;
	}
	
	boolean isVisible(Card pCard)
	{
		assert contains(pCard);
		return aVisible.contains(pCard);
	}
	
	void pop(StackIndex pIndex)
	{
		assert !aStacks.get(pIndex).isEmpty();
		aVisible.remove(aStacks.get(pIndex).pop());
		if( !aStacks.get(pIndex).isEmpty())
		{
			aVisible.add(aStacks.get(pIndex).peek());
		}
	}
	
	void push(Card pCard, StackIndex pIndex)
	{
		aStacks.get(pIndex).push(pCard);
		aVisible.add(pCard);
	}
}
