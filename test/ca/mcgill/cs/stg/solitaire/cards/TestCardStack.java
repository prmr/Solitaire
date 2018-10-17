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
package ca.mcgill.cs.stg.solitaire.cards;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

public class TestCardStack
{
	private final static Card ACE_OF_CLUBS = Card.get(Rank.ACE, Suit.CLUBS);
	private final static Card TWO_OF_CLUBS = Card.get(Rank.TWO, Suit.CLUBS);
	
	private final CardStack aStack = new CardStack();
	
	@Before
	public void setUp()
	{
		aStack.clear();
	}
	
	@Test
	public void testClear()
	{
		aStack.clear();
		assertTrue(aStack.isEmpty());
		
		aStack.push(ACE_OF_CLUBS);
		aStack.push(TWO_OF_CLUBS);
		assertFalse(aStack.isEmpty());
		aStack.clear();
		assertTrue(aStack.isEmpty());
	}
	
	@Test
	public void testPeek()
	{
		aStack.push(ACE_OF_CLUBS);
		aStack.push(TWO_OF_CLUBS);
		assertSame(TWO_OF_CLUBS, aStack.peek());
		assertEquals(2, aStack.size());
	}
	
	@Test
	public void testPeek_int()
	{
		aStack.push(ACE_OF_CLUBS);
		aStack.push(TWO_OF_CLUBS);
		assertSame(ACE_OF_CLUBS, aStack.peek(0));
		assertSame(TWO_OF_CLUBS, aStack.peek(1));
	}
	
	@Test
	public void testPop()
	{
		aStack.push(ACE_OF_CLUBS);
		aStack.push(TWO_OF_CLUBS);
		assertSame(TWO_OF_CLUBS, aStack.pop());
		assertEquals(1, aStack.size());
	}
	
	@Test
	public void testPush()
	{
		aStack.push(ACE_OF_CLUBS);
		assertSame(ACE_OF_CLUBS, aStack.peek());
		assertEquals(1, aStack.size());
		aStack.push(TWO_OF_CLUBS);
		assertSame(TWO_OF_CLUBS, aStack.peek());
		assertEquals(2, aStack.size());
	}
}
