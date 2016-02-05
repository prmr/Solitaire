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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.cards.Card.Rank;
import ca.mcgill.cs.stg.solitaire.cards.Card.Suit;
import ca.mcgill.cs.stg.solitaire.cards.Deck;
import ca.mcgill.cs.stg.solitaire.model.GameModel.StackIndex;

public class TestWorkingStackManager
{
	private WorkingStackManager aWorkingStackManager = new WorkingStackManager();
	private static final Card CKC = Card.get(Rank.KING, Suit.CLUBS);
	private static final Card CAC = Card.get(Rank.ACE, Suit.CLUBS);
	private static final Card C5D = Card.get(Rank.FIVE, Suit.DIAMONDS);
	private static final Card C4D = Card.get(Rank.FOUR, Suit.DIAMONDS);
	private static final Card C4C = Card.get(Rank.FOUR, Suit.CLUBS);
	private static final Card C4S = Card.get(Rank.FOUR, Suit.SPADES);
	private static final Card C4H = Card.get(Rank.FOUR, Suit.HEARTS);
	private static final Card C3H = Card.get(Rank.THREE, Suit.HEARTS);
	
	@Test
	public void testInitialize()
	{
		for( StackIndex index : StackIndex.values())
		{
			assertEquals(0, aWorkingStackManager.getStack(index).length);
		}
		Deck deck = new Deck();
		aWorkingStackManager.initialize(deck);
		assertEquals(1,aWorkingStackManager.getStack(StackIndex.FIRST).length);
		assertEquals(2,aWorkingStackManager.getStack(StackIndex.SECOND).length);
		assertEquals(3,aWorkingStackManager.getStack(StackIndex.THIRD).length);
		assertEquals(4,aWorkingStackManager.getStack(StackIndex.FOURTH).length);
		assertEquals(5,aWorkingStackManager.getStack(StackIndex.FIFTH).length);
		assertEquals(6,aWorkingStackManager.getStack(StackIndex.SIXTH).length);
		assertEquals(7,aWorkingStackManager.getStack(StackIndex.SEVENTH).length);
		assertEquals(24, deck.size());
		deck.shuffle();
		aWorkingStackManager.initialize(deck);
		assertEquals(1,aWorkingStackManager.getStack(StackIndex.FIRST).length);
		assertEquals(2,aWorkingStackManager.getStack(StackIndex.SECOND).length);
		assertEquals(3,aWorkingStackManager.getStack(StackIndex.THIRD).length);
		assertEquals(4,aWorkingStackManager.getStack(StackIndex.FOURTH).length);
		assertEquals(5,aWorkingStackManager.getStack(StackIndex.FIFTH).length);
		assertEquals(6,aWorkingStackManager.getStack(StackIndex.SIXTH).length);
		assertEquals(7,aWorkingStackManager.getStack(StackIndex.SEVENTH).length);
		assertEquals(24, deck.size());
	}
	
	@Test
	public void testContains()
	{
		assertFalse(aWorkingStackManager.contains(CAC, StackIndex.FIRST));
		aWorkingStackManager.push(CAC, StackIndex.FIRST);
		assertTrue(aWorkingStackManager.contains(CAC, StackIndex.FIRST));
		aWorkingStackManager.push(C3H, StackIndex.FIRST);
		aWorkingStackManager.push(C5D, StackIndex.FIRST);
		assertTrue(aWorkingStackManager.contains(C5D, StackIndex.FIRST));
	}
	
	@Test
	public void testRemoveSequence()
	{
		aWorkingStackManager.push(Card.get(Rank.TEN, Suit.CLUBS), StackIndex.FIRST);
		aWorkingStackManager.push(Card.get(Rank.NINE, Suit.DIAMONDS), StackIndex.FIRST);
		aWorkingStackManager.push(Card.get(Rank.EIGHT, Suit.CLUBS), StackIndex.FIRST);
		aWorkingStackManager.push(Card.get(Rank.SEVEN, Suit.DIAMONDS), StackIndex.FIRST);
		aWorkingStackManager.push(Card.get(Rank.SIX, Suit.CLUBS), StackIndex.FIRST);
		aWorkingStackManager.push(Card.get(Rank.FIVE, Suit.DIAMONDS), StackIndex.FIRST);
		aWorkingStackManager.push(Card.get(Rank.FOUR, Suit.CLUBS), StackIndex.FIRST);
		aWorkingStackManager.push(Card.get(Rank.THREE, Suit.DIAMONDS), StackIndex.FIRST);
		Card[] sequence = aWorkingStackManager.removeSequence(Card.get(Rank.NINE, Suit.DIAMONDS), StackIndex.FIRST);
		Card[] stack = aWorkingStackManager.getStack(StackIndex.FIRST);
		assertEquals(1, stack.length);
		assertEquals(Card.get(Rank.TEN, Suit.CLUBS), stack[0]);
		assertEquals( 7, sequence.length);
		assertEquals( Card.get(Rank.NINE, Suit.DIAMONDS), sequence[0]);
		assertEquals( Card.get(Rank.EIGHT, Suit.CLUBS), sequence[1]);
	}
	
	@Test
	public void testCanMoveTo()
	{
		assertFalse(aWorkingStackManager.canMoveTo(CAC, StackIndex.FIRST)); 
		assertTrue(aWorkingStackManager.canMoveTo(CKC, StackIndex.FIRST)); 
		aWorkingStackManager.push(C5D, StackIndex.FIRST);
		assertFalse(aWorkingStackManager.canMoveTo(CAC, StackIndex.FIRST));
		assertFalse(aWorkingStackManager.canMoveTo(C4D, StackIndex.FIRST));
		assertFalse(aWorkingStackManager.canMoveTo(C4H, StackIndex.FIRST));
		assertTrue(aWorkingStackManager.canMoveTo(C4C, StackIndex.FIRST));
		assertTrue(aWorkingStackManager.canMoveTo(C4S, StackIndex.FIRST));
	}
	
	@Test
	public void testGetSequence()
	{
		aWorkingStackManager.push(C5D, StackIndex.SECOND);
		Card[] sequence = aWorkingStackManager.getSequence(C5D, StackIndex.SECOND);
		assertEquals(1, sequence.length);
		assertEquals(C5D, sequence[0]);
		aWorkingStackManager.push(C4C, StackIndex.SECOND);
		sequence = aWorkingStackManager.getSequence(C5D, StackIndex.SECOND);
		assertEquals(2, sequence.length);
		assertEquals(C5D, sequence[0]);
		assertEquals(C4C, sequence[1]);
		sequence = aWorkingStackManager.getSequence(C4C, StackIndex.SECOND);
		assertEquals(1, sequence.length);
		assertEquals(C4C, sequence[0]);
	}
}
