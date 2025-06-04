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
package ca.mcgill.solitaire.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a general-purpose stack of cards.
 */
public class CardStack implements Iterable<Card> {
	
	private final List<Card> aCards = new ArrayList<>();
	
	/**
	 * Creates an empty CardStack.
	 */
	public CardStack() {}
	
	/**
	 * Creates a CardStack that contains all the cards in pCards, in the
	 * iteration order, from bottom to top.
	 * 
	 * @param pCards The cards to initialize the stack with.
	 */
	public CardStack(Iterable<Card> pCards) {
		for (Card card : pCards) {
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
	public void push(Card pCard) {
		assert pCard != null && !aCards.contains(pCard);
		aCards.add(pCard);
	}
	
	/**
	 * Removes the card on top of the stack and returns it.
	 * 
	 * @return The card on top of the stack.
	 * @pre !isEmpty()
	 */
	public Card pop() {
		assert !isEmpty();
		return aCards.removeLast();
	}
	
	/**
	 * Returns the card at the top of the stack, without 
	 * removing it.
	 * 
	 * @return The card at the top of the stack.
	 * @pre !isEmpty();
	 */
	public Card peekTop() {
		assert !isEmpty();
		return aCards.getLast();
	}
	
	/**
	 * Returns the card at the bottom of the stack, without
	 * removing it.
	 * 
	 * @return The card at the bottom of the stack.
	 * @pre !isEmpty()
	 */
	public Card peekBottom() {
		assert !isEmpty();
		return aCards.getFirst();
	}
	
	/**
	 * @return The number of cards in the stack.
	 */
	public int size() {
		return aCards.size();
	}
	
	/**
	 * Removes all the cards in the stack.
	 */
	public void clear() {
		aCards.clear();
	}
	
	/**
	 * @return True if and only if the stack has no cards in it.
	 */
	public boolean isEmpty() {
		return aCards.isEmpty();
	}
	
	@Override
	public String toString() {
		return aCards.toString();
	}

	@Override
	public Iterator<Card> iterator() {
		return Collections.unmodifiableList(aCards).iterator();
	}
}
