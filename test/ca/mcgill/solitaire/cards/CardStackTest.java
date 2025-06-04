/*******************************************************************************
 * Solitaire
 * 
 * Copyright (C) 2016-2024 by Martin P. Robillard
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
package ca.mcgill.solitaire.cards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class CardStackTest {
	private final static Card ACE_OF_CLUBS = Card.get(Rank.ACE, Suit.CLUBS);
	private final static Card TWO_OF_CLUBS = Card.get(Rank.TWO, Suit.CLUBS);

	private final CardStack aStack = new CardStack();

	@Test
	void testConstructor_WithInput_Empty()
	{
		CardStack stack = new CardStack(List.of());
		assertTrue(stack.isEmpty());
	}
	
	@Test
	void testConstructor_WithInput_SingleElement()
	{
		CardStack stack = new CardStack(List.of(ACE_OF_CLUBS));
		assertSame(ACE_OF_CLUBS, stack.peekTop());
		assertEquals(1, stack.size());
	}
	
	@Test
	void testConstructor_WithInput_MultipleElement()
	{
		CardStack stack = new CardStack(List.of(ACE_OF_CLUBS, TWO_OF_CLUBS));
		assertSame(TWO_OF_CLUBS, stack.peekTop());
		assertSame(ACE_OF_CLUBS, stack.peekBottom());
		assertEquals(2, stack.size());
	}
	
	@Test
	void testClear() {
		aStack.clear();
		assertTrue(aStack.isEmpty());

		aStack.push(ACE_OF_CLUBS);
		aStack.push(TWO_OF_CLUBS);
		assertFalse(aStack.isEmpty());
		aStack.clear();
		assertTrue(aStack.isEmpty());
	}

	@Test
	void testPeekTop() {
		aStack.push(ACE_OF_CLUBS);
		aStack.push(TWO_OF_CLUBS);
		assertSame(TWO_OF_CLUBS, aStack.peekTop());
	}

	@Test
	void testPeekBottom() {
		aStack.push(ACE_OF_CLUBS);
		aStack.push(TWO_OF_CLUBS);
		assertSame(ACE_OF_CLUBS, aStack.peekBottom());
	}

	@Test
	void testPop() {
		aStack.push(ACE_OF_CLUBS);
		aStack.push(TWO_OF_CLUBS);
		assertSame(TWO_OF_CLUBS, aStack.pop());
		assertEquals(1, aStack.size());
	}

	@Test
	void testPush() {
		aStack.push(ACE_OF_CLUBS);
		assertSame(ACE_OF_CLUBS, aStack.peekTop());
		assertEquals(1, aStack.size());
		aStack.push(TWO_OF_CLUBS);
		assertSame(TWO_OF_CLUBS, aStack.peekTop());
		assertEquals(2, aStack.size());
	}
}
