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

import java.io.Serializable;
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
class WorkingStackManager implements Serializable
{
	private static final int NUMBER_OF_CARDS_NEEDED = 24;
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
	 * @param pDeck a deck of card to use to fill the stacks initially.
	 * @pre pDeck != null && pDeck.size() >= 24
	 */
	void initialize(Deck pDeck)
	{   
		assert pDeck != null && pDeck.size() >= NUMBER_OF_CARDS_NEEDED; 
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
	
	
	/**
	 * Determines if it is legal to move pCard on top of
	 * stack pIndex, i.e. if a king is moved to an empty
	 * stack or any other rank on a card of immediately greater
	 * rank but of a different color.
	 * @param pCard The card to move
	 * @param pIndex The destination stack.
	 * @return True if the move is legal.
	 * @pre pCard != null, pIndex != null
	 */
	boolean canMoveTo(Card pCard, StackIndex pIndex )
	{
		assert pCard != null && pIndex != null;
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
	
	/**
	 * @param pIndex The index of the stack to obtain.
	 * @return An array of cards in the stack at pIndex, where element [0] is the 
	 * bottom of the stack. Modifying the array has no impact on the state of the 
	 * object.
	 * @pre pIndex != null
	 */
	Card[] getStack(StackIndex pIndex)
	{
		assert pIndex != null;
		return aStacks.get(pIndex).toArray(new Card[aStacks.get(pIndex).size()]);
	}
	
	/**
	 * Returns true if moving pCard away reveals the top of the card.
	 * @param pCard The card to test
	 * @param pIndex The index of the stack.
	 * @return true if the card above pCard is not visible and pCard
	 * is visible.
	 * @pre pCard != null && pIndex != null
	 */
	boolean revealsTop(Card pCard, StackIndex pIndex)
	{
		assert pCard != null && pIndex != null;
		int indexOf = aStacks.get(pIndex).indexOf(pCard);
		if( indexOf < 1 )
		{
			return false;
		}
		return aVisible.contains(pCard) && !aVisible.contains(aStacks.get(pIndex).get(indexOf-1));
	}
	
	/**
	 * Move pCard and all the cards below to pDestination.
	 * @param pCard The card to move, possibly including all the cards on top of it.
	 * @param pOrigin The location of the card before the move.
	 * @param pDestination The intended destination of the card.
     * @pre this is a legal move
	 */
	void moveWithin(Card pCard, StackIndex pOrigin, StackIndex pDestination )
	{
		assert pCard != null && pOrigin != null && pDestination != null;
		assert contains(pCard, pOrigin);
		assert isVisible(pCard);
		Stack<Card> temp = new Stack<>();
		Card card = aStacks.get(pOrigin).pop();
		temp.push(card);
		while( !card.equals(pCard ))
		{
			card = aStacks.get(pOrigin).pop();
			temp.push(card);
		}
		while( !temp.isEmpty() )
		{
			aStacks.get(pDestination).push(temp.pop());
		}
		
	}
	
	/**
	 * Returns a sequence of cards starting at pCard and including
	 * all cards on top of it.
	 * @param pCard The bottom card in the stack
	 * @param pIndex The index of the stack.
	 * @return An array of cards in the stack. Element 0 is the bottom.
	 * @pre pCard != null && pIndex != null
	 */
	public Card[] getSequence(Card pCard, StackIndex pIndex)
	{
		Stack<Card> stack = aStacks.get(pIndex);
		List<Card> lReturn = new ArrayList<>();
		boolean aSeen = false;
		for( Card card : stack )
		{
			if( card.equals(pCard ))
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
	 * Make the top card of a stack visible.
	 * @param pIndex The index of the requested stack.
	 * @pre pIndex != null && !isEmpty(pIndex)
	 */
	void showTop(StackIndex pIndex)
	{
		assert !aStacks.get(pIndex).isEmpty();
		aVisible.add(aStacks.get(pIndex).peek());
	}
	
	/**
	 * Make the top card of a stack not visible.
	 * @param pIndex The index of the requested stack.
	 * @pre pIndex != null && !isEmpty(pIndex)
	 */
	void hideTop(StackIndex pIndex)
	{
		assert !aStacks.get(pIndex).isEmpty();
		aVisible.remove(aStacks.get(pIndex).peek());
	}
	
	/**
	 * @param pCard The card to check
	 * @param pIndex The index of the stack to check
	 * @return True if pIndex contains pCard
	 * @pre pCard != null && pIndex != null
	 */
	boolean contains(Card pCard, StackIndex pIndex)
	{
		assert pCard != null && pIndex != null;
		for( Card card : aStacks.get(pIndex))
		{
			if( card.equals(pCard ))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param pCard The card to check.
	 * @return Whether pCard is contains in any stack.
	 * @pre pCard != null;
	 */
	boolean contains(Card pCard)
	{
		assert pCard != null;
		for( StackIndex index : StackIndex.values())
		{
			if( contains(pCard, index))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param pCard The card to check.
	 * @return true if pCard is visible in the stacks.
	 * @pre contains(pCard)
	 */
	boolean isVisible(Card pCard)
	{
		assert contains(pCard);
		return aVisible.contains(pCard);
	}
	
	/**
	 * Removes the top card from the stack at pIndex.
	 * @param pIndex The index of the stack to pop.
	 * @pre !isEmpty(pIndex)
	 */
	void pop(StackIndex pIndex)
	{
		assert !aStacks.get(pIndex).isEmpty();
		aVisible.remove(aStacks.get(pIndex).pop());
	}
	
	/**
	 * Places a card on top of the stack at pIndex. The
	 * card will be visible by default.
	 * @param pCard The card to push.
	 * @param pIndex The index of the destination stack.
	 * @pre pCard != null && pIndex != null;
	 */
	void push(Card pCard, StackIndex pIndex)
	{
		assert pCard != null && pIndex != null;
		aStacks.get(pIndex).push(pCard);
		aVisible.add(pCard);
	}
}
