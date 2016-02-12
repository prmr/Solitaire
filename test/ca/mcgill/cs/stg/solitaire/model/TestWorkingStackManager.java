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
	public void testRevealsTop()
	{
		Deck deck = new Deck();
		aWorkingStackManager.initialize(deck);
		Card[] stack = aWorkingStackManager.getStack(StackIndex.FIRST);
		assertFalse(aWorkingStackManager.revealsTop(stack[0], StackIndex.FIRST));
		aWorkingStackManager.pop(StackIndex.FIRST);
		assertFalse(aWorkingStackManager.revealsTop(stack[0], StackIndex.FIRST));
		assertFalse(aWorkingStackManager.revealsTop(deck.draw(), StackIndex.FIRST));
		assertFalse(aWorkingStackManager.revealsTop(deck.draw(), StackIndex.SECOND));
		stack = aWorkingStackManager.getStack(StackIndex.SECOND);
		assertFalse(aWorkingStackManager.revealsTop(stack[0], StackIndex.SECOND));
		assertTrue(aWorkingStackManager.revealsTop(stack[1], StackIndex.SECOND));
		
		stack = aWorkingStackManager.getStack(StackIndex.THIRD);
		assertFalse(aWorkingStackManager.revealsTop(stack[0], StackIndex.THIRD));
		assertFalse(aWorkingStackManager.revealsTop(stack[1], StackIndex.THIRD));
		assertTrue(aWorkingStackManager.revealsTop(stack[2], StackIndex.THIRD));
		
		stack = aWorkingStackManager.getStack(StackIndex.FOURTH);
		assertFalse(aWorkingStackManager.revealsTop(stack[0], StackIndex.FOURTH));
		assertFalse(aWorkingStackManager.revealsTop(stack[1], StackIndex.FOURTH));
		assertFalse(aWorkingStackManager.revealsTop(stack[2], StackIndex.FOURTH));
		assertTrue(aWorkingStackManager.revealsTop(stack[3], StackIndex.FOURTH));
		Card card = deck.draw();
		aWorkingStackManager.push(card, StackIndex.FOURTH);
		assertFalse(aWorkingStackManager.revealsTop(card, StackIndex.FOURTH));
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
	
	@Test 
	public void testMoveWithin()
	{
		Deck deck = new Deck();
		aWorkingStackManager.initialize(deck);
		Card[] stack2a = aWorkingStackManager.getStack(StackIndex.SECOND);
		Card[] stack4a = aWorkingStackManager.getStack(StackIndex.FOURTH);
		aWorkingStackManager.moveWithin(stack2a[1], StackIndex.SECOND, StackIndex.FOURTH);
		Card[] stack2b = aWorkingStackManager.getStack(StackIndex.SECOND);
		Card[] stack4b = aWorkingStackManager.getStack(StackIndex.FOURTH);
		assertEquals(1,stack2b.length);
		assertEquals(5,stack4b.length);
		assertEquals(stack2a[0], stack2b[0]);
		assertEquals(stack4a[0], stack4b[0]);
		assertEquals(stack4a[1], stack4b[1]);
		assertEquals(stack4a[2], stack4b[2]);
		assertEquals(stack4a[3], stack4b[3]);
		assertEquals(stack2a[1], stack4b[4]);
		assertTrue(aWorkingStackManager.isVisible(stack4b[4]));
		aWorkingStackManager.moveWithin(stack4b[3], StackIndex.FOURTH, StackIndex.SECOND);
		Card[] stack2c = aWorkingStackManager.getStack(StackIndex.SECOND);
		assertEquals(3,stack2c.length);
		assertEquals(stack2a[0], stack2c[0]);
		assertEquals(stack4b[3], stack2c[1]);
		assertEquals(stack4b[4], stack2c[2]);
	}
	
	@Test
	public void testContains2()
	{
		Deck deck = new Deck();
		aWorkingStackManager.initialize(deck);
		while( deck.size() > 0 )
		{
			assertFalse(aWorkingStackManager.contains(deck.draw()));
		}
		for( StackIndex index : StackIndex.values())
		{
			for( Card card : aWorkingStackManager.getStack(index) )
			{
				assertTrue( aWorkingStackManager.contains(card));
			}
		}
	}
	
	@Test
	public void testVisibility()
	{
		Deck deck = new Deck();
		aWorkingStackManager.initialize(deck);
		for( StackIndex index : StackIndex.values())
		{
			Card[] cards = aWorkingStackManager.getStack(index);
			for( int i = 0; i < cards.length-1; i++ )
			{
				assertFalse(aWorkingStackManager.isVisible(cards[i]));
			}
			assertTrue(aWorkingStackManager.isVisible(cards[cards.length-1]));
		}
		aWorkingStackManager.push(deck.draw(), StackIndex.SECOND);
		Card[] stack = aWorkingStackManager.getStack(StackIndex.SECOND);
		assertFalse(aWorkingStackManager.isVisible(stack[0]));
		assertTrue(aWorkingStackManager.isVisible(stack[1]));
		assertTrue(aWorkingStackManager.isVisible(stack[2]));
	}
	
	@Test
	public void testShowHideTop()
	{
		aWorkingStackManager.initialize(new Deck());
		Card[] one = aWorkingStackManager.getStack(StackIndex.FIRST); 
		Card[] two = aWorkingStackManager.getStack(StackIndex.SECOND); 
		assertTrue(aWorkingStackManager.isVisible(one[0]));
		aWorkingStackManager.hideTop(StackIndex.FIRST);
		assertFalse(aWorkingStackManager.isVisible(one[0]));
		aWorkingStackManager.showTop(StackIndex.FIRST);
		assertTrue(aWorkingStackManager.isVisible(one[0]));
		
		assertTrue(aWorkingStackManager.isVisible(two[1]));
		aWorkingStackManager.hideTop(StackIndex.SECOND);
		assertFalse(aWorkingStackManager.isVisible(two[1]));
		aWorkingStackManager.showTop(StackIndex.SECOND);
		assertTrue(aWorkingStackManager.isVisible(two[1]));
	}
}
