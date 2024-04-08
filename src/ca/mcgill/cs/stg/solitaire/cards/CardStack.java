/*******************************************************************************
 * Solitaire
 *  
 *  Copyright (C) 2016-2024 by Martin P. Robillard
 *  
 *  See: https://github.com/prmr/Solitaire
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/.
 *******************************************************************************/
package ca.mcgill.cs.stg.solitaire.cards;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a general-purpose stack of cards.
 */
public class CardStack implements Iterable<Card>
{
	private final List<Card> aCards;
	
	/**
	 * Creates an empty CardStack.
	 */
	public CardStack()
	{
		aCards = new ArrayList<>();
	}
	
	/**
	 * Creates a CardStack that contains all the cards in pCard, in the
	 * iteration order, from bottom to top.
	 * 
	 * @param pCards The cards to initialize the stack with.
	 */
	public CardStack(Iterable<Card> pCards)
	{
		this();
		for( Card card : pCards )
		{
			aCards.add(card);
		}
	}
	
	/**
	 * Pushes pCard onto the stack.
	 * 
	 * @param pCard The card to push.
	 * @pre pCard != null;
	 * @pre !aCards.contains(pCard)
	 */
	public void push(Card pCard)
	{
		assert pCard != null && !aCards.contains(pCard);
		aCards.add(pCard);
	}
	
	/**
	 * Removes the card on top of the stack and returns it.
	 * 
	 * @return The card on top of the stack.
	 * @pre !isEmpty()
	 */
	public Card pop()
	{
		assert !isEmpty();
		return aCards.remove(aCards.size()-1);
	}
	
	/**
	 * @return The card at the top of the stack.
	 * @pre !isEmpty();
	 */
	public Card peek()
	{
		assert !isEmpty();
		return aCards.get(aCards.size()-1);
	}
	
	/**
	 * @param pIndex The index to peek in the stack.
	 * @return The card at the position indicated by pIndex
	 * @pre pIndex >= 0 && pIndex < size();
	 */
	public Card peek(int pIndex)
	{
		assert pIndex >= 0 && pIndex < size();
		return aCards.get(pIndex);
	}
	
	/**
	 * @return The number of cards in the stack.
	 */
	public int size()
	{
		return aCards.size();
	}
	
	/**
	 * Removes all the cards in the stack.
	 */
	public void clear()
	{
		aCards.clear();
	}
	
	/**
	 * @return True if and only if the stack has no cards in it.
	 */
	public boolean isEmpty()
	{
		return aCards.size() == 0;
	}
	
	@Override
	public String toString()
	{
		return aCards.toString();
	}

	@Override
	public Iterator<Card> iterator()
	{
		return aCards.iterator();
	}
}
