/*******************************************************************************
 * Solitaire
 * 
 * Copyright (C) 2025 by Martin P. Robillard
 * 
 * See: https://github.com/prmr/Solitaire
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see http://www.gnu.org/licenses/.
 *******************************************************************************/
package ca.mcgill.solitaire.testutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import ca.mcgill.solitaire.cards.Card;
import ca.mcgill.solitaire.cards.CardStack;
import ca.mcgill.solitaire.cards.Rank;
import ca.mcgill.solitaire.cards.Suit;

/**
 * Utilities to help with testing.
 */
public final class Utils {
	
	private Utils() {}

	/**
	 * @return All 52 cards sorted by suit, then rank.
	 */
	public static List<Card> allCards() {
		List<Card> allCards = new ArrayList<>();
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				allCards.add(Card.get(rank, suit));
			}
		}
		return allCards;
	}
	
	/**
	 * Returns a card at a specified index from the stack, without permanently
	 * altering the state of the stack. Zero corresponds to the bottom of the
	 * stack, and size()-1 to the top.
	 *
	 * @param pCardStack The CardStack to peek in.
	 * @param pIndex The index to peek at. Zero corresponds to the bottom
	 *     of the stack, size()-1  to the top.
	 * @return The card at the specified index.
	 * @pre pCardStack != null
	 * @pre pIndex >= 0 && pIndex < pCardStack.size()
	 */
	public static Card peekAtIndex(CardStack pCardStack, int pIndex) {
		assert pCardStack != null;
		assert pIndex >= 0 && pIndex < pCardStack.size();
		
		CardStack temporary = new CardStack();
		Card result = null;
		while (!pCardStack.isEmpty())
		{
			temporary.push(pCardStack.pop());
		}
		int size = temporary.size();
		for (int i = 0; i < size; i++)
		{
			pCardStack.push(temporary.pop());
			if (i == pIndex)
			{
				result = pCardStack.peekTop();
			}
		}
		return result;
	}
	
	/**
	 * Asserts that pCardStack consists of exactly the cards in pCards.
	 * @param pCards Expected cards. Left to right for bottom to top.
	 */
	public static void assertCardStackHas(CardStack pCardStack, Card... pCards) {
		List<Card> cards = new ArrayList<>();
		for (Card card : pCardStack) {
			cards.add(card);
		}
		assertEquals(cards, Arrays.asList(pCards));
	}
	
	@Test
	void testPeekAtIndex_Single()
	{
		CardStack stack = new CardStack(List.of(Card.get(Rank.ACE, Suit.CLUBS)));
		assertSame(Card.get(Rank.ACE, Suit.CLUBS), peekAtIndex(stack, 0));
	}
	
	@Test
	void testPeekAtIndex_Multiple_First()
	{
		CardStack stack = new CardStack(List.of(Card.get(Rank.ACE, Suit.CLUBS),
				Card.get(Rank.TWO, Suit.CLUBS), 
				Card.get(Rank.THREE, Suit.CLUBS)));
		assertSame(Card.get(Rank.ACE, Suit.CLUBS), peekAtIndex(stack, 0));
	}
	
	@Test
	void testPeekAtIndex_Multiple_Last()
	{
		CardStack stack = new CardStack(List.of(Card.get(Rank.ACE, Suit.CLUBS),
				Card.get(Rank.TWO, Suit.CLUBS), 
				Card.get(Rank.THREE, Suit.CLUBS)));
		assertSame(Card.get(Rank.THREE, Suit.CLUBS), peekAtIndex(stack, 2));
	}
	
	@Test
	void testPeekAtIndex_Multiple_Middle()
	{
		CardStack stack = new CardStack(List.of(Card.get(Rank.ACE, Suit.CLUBS),
				Card.get(Rank.TWO, Suit.CLUBS), 
				Card.get(Rank.THREE, Suit.CLUBS)));
		assertSame(Card.get(Rank.TWO, Suit.CLUBS), peekAtIndex(stack, 1));
	}
}
