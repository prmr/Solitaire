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
import ca.mcgill.cs.stg.solitaire.cards.Deck;
import ca.mcgill.cs.stg.solitaire.cards.Rank;
import ca.mcgill.cs.stg.solitaire.cards.Suit;

public class TestWorkingStackManager
{
	private Tableau aWorkingStackManager = new Tableau();
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
		for( TableauPile index : TableauPile.values())
		{
			assertEquals(0, aWorkingStackManager.getStack(index).length);
		}
		Deck deck = new Deck();
		aWorkingStackManager.initialize(deck);
		assertEquals(1,aWorkingStackManager.getStack(TableauPile.FIRST).length);
		assertEquals(2,aWorkingStackManager.getStack(TableauPile.SECOND).length);
		assertEquals(3,aWorkingStackManager.getStack(TableauPile.THIRD).length);
		assertEquals(4,aWorkingStackManager.getStack(TableauPile.FOURTH).length);
		assertEquals(5,aWorkingStackManager.getStack(TableauPile.FIFTH).length);
		assertEquals(6,aWorkingStackManager.getStack(TableauPile.SIXTH).length);
		assertEquals(7,aWorkingStackManager.getStack(TableauPile.SEVENTH).length);
		deck.shuffle();
		aWorkingStackManager.initialize(deck);
		assertEquals(1,aWorkingStackManager.getStack(TableauPile.FIRST).length);
		assertEquals(2,aWorkingStackManager.getStack(TableauPile.SECOND).length);
		assertEquals(3,aWorkingStackManager.getStack(TableauPile.THIRD).length);
		assertEquals(4,aWorkingStackManager.getStack(TableauPile.FOURTH).length);
		assertEquals(5,aWorkingStackManager.getStack(TableauPile.FIFTH).length);
		assertEquals(6,aWorkingStackManager.getStack(TableauPile.SIXTH).length);
		assertEquals(7,aWorkingStackManager.getStack(TableauPile.SEVENTH).length);
	}
	
	@Test
	public void testRevealsTop()
	{
		Deck deck = new Deck();
		aWorkingStackManager.initialize(deck);
		Card[] stack = aWorkingStackManager.getStack(TableauPile.FIRST);
		assertFalse(aWorkingStackManager.revealsTop(stack[0], TableauPile.FIRST));
		aWorkingStackManager.pop(TableauPile.FIRST);
		assertFalse(aWorkingStackManager.revealsTop(stack[0], TableauPile.FIRST));
		assertFalse(aWorkingStackManager.revealsTop(deck.draw(), TableauPile.FIRST));
		assertFalse(aWorkingStackManager.revealsTop(deck.draw(), TableauPile.SECOND));
		stack = aWorkingStackManager.getStack(TableauPile.SECOND);
		assertFalse(aWorkingStackManager.revealsTop(stack[0], TableauPile.SECOND));
		assertTrue(aWorkingStackManager.revealsTop(stack[1], TableauPile.SECOND));
		
		stack = aWorkingStackManager.getStack(TableauPile.THIRD);
		assertFalse(aWorkingStackManager.revealsTop(stack[0], TableauPile.THIRD));
		assertFalse(aWorkingStackManager.revealsTop(stack[1], TableauPile.THIRD));
		assertTrue(aWorkingStackManager.revealsTop(stack[2], TableauPile.THIRD));
		
		stack = aWorkingStackManager.getStack(TableauPile.FOURTH);
		assertFalse(aWorkingStackManager.revealsTop(stack[0], TableauPile.FOURTH));
		assertFalse(aWorkingStackManager.revealsTop(stack[1], TableauPile.FOURTH));
		assertFalse(aWorkingStackManager.revealsTop(stack[2], TableauPile.FOURTH));
		assertTrue(aWorkingStackManager.revealsTop(stack[3], TableauPile.FOURTH));
		Card card = deck.draw();
		aWorkingStackManager.push(card, TableauPile.FOURTH);
		assertFalse(aWorkingStackManager.revealsTop(card, TableauPile.FOURTH));
	}
	
	@Test
	public void testContains()
	{
		assertFalse(aWorkingStackManager.contains(CAC, TableauPile.FIRST));
		aWorkingStackManager.push(CAC, TableauPile.FIRST);
		assertTrue(aWorkingStackManager.contains(CAC, TableauPile.FIRST));
		aWorkingStackManager.push(C3H, TableauPile.FIRST);
		aWorkingStackManager.push(C5D, TableauPile.FIRST);
		assertTrue(aWorkingStackManager.contains(C5D, TableauPile.FIRST));
	}
	
	@Test
	public void testCanMoveTo()
	{
		assertFalse(aWorkingStackManager.canMoveTo(CAC, TableauPile.FIRST)); 
		assertTrue(aWorkingStackManager.canMoveTo(CKC, TableauPile.FIRST)); 
		aWorkingStackManager.push(C5D, TableauPile.FIRST);
		assertFalse(aWorkingStackManager.canMoveTo(CAC, TableauPile.FIRST));
		assertFalse(aWorkingStackManager.canMoveTo(C4D, TableauPile.FIRST));
		assertFalse(aWorkingStackManager.canMoveTo(C4H, TableauPile.FIRST));
		assertTrue(aWorkingStackManager.canMoveTo(C4C, TableauPile.FIRST));
		assertTrue(aWorkingStackManager.canMoveTo(C4S, TableauPile.FIRST));
	}
	
	@Test
	public void testGetSequence()
	{
		aWorkingStackManager.push(C5D, TableauPile.SECOND);
		Card[] sequence = aWorkingStackManager.getSequence(C5D, TableauPile.SECOND);
		assertEquals(1, sequence.length);
		assertEquals(C5D, sequence[0]);
		aWorkingStackManager.push(C4C, TableauPile.SECOND);
		sequence = aWorkingStackManager.getSequence(C5D, TableauPile.SECOND);
		assertEquals(2, sequence.length);
		assertEquals(C5D, sequence[0]);
		assertEquals(C4C, sequence[1]);
		sequence = aWorkingStackManager.getSequence(C4C, TableauPile.SECOND);
		assertEquals(1, sequence.length);
		assertEquals(C4C, sequence[0]);
	}
	
	@Test 
	public void testMoveWithin()
	{
		Deck deck = new Deck();
		aWorkingStackManager.initialize(deck);
		Card[] stack2a = aWorkingStackManager.getStack(TableauPile.SECOND);
		Card[] stack4a = aWorkingStackManager.getStack(TableauPile.FOURTH);
		aWorkingStackManager.moveWithin(stack2a[1], TableauPile.SECOND, TableauPile.FOURTH);
		Card[] stack2b = aWorkingStackManager.getStack(TableauPile.SECOND);
		Card[] stack4b = aWorkingStackManager.getStack(TableauPile.FOURTH);
		assertEquals(1,stack2b.length);
		assertEquals(5,stack4b.length);
		assertEquals(stack2a[0], stack2b[0]);
		assertEquals(stack4a[0], stack4b[0]);
		assertEquals(stack4a[1], stack4b[1]);
		assertEquals(stack4a[2], stack4b[2]);
		assertEquals(stack4a[3], stack4b[3]);
		assertEquals(stack2a[1], stack4b[4]);
		assertTrue(aWorkingStackManager.isVisible(stack4b[4]));
		aWorkingStackManager.moveWithin(stack4b[3], TableauPile.FOURTH, TableauPile.SECOND);
		Card[] stack2c = aWorkingStackManager.getStack(TableauPile.SECOND);
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
		while( !deck.isEmpty() )
		{
			assertFalse(aWorkingStackManager.contains(deck.draw()));
		}
		for( TableauPile index : TableauPile.values())
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
		for( TableauPile index : TableauPile.values())
		{
			Card[] cards = aWorkingStackManager.getStack(index);
			for( int i = 0; i < cards.length-1; i++ )
			{
				assertFalse(aWorkingStackManager.isVisible(cards[i]));
			}
			assertTrue(aWorkingStackManager.isVisible(cards[cards.length-1]));
		}
		aWorkingStackManager.push(deck.draw(), TableauPile.SECOND);
		Card[] stack = aWorkingStackManager.getStack(TableauPile.SECOND);
		assertFalse(aWorkingStackManager.isVisible(stack[0]));
		assertTrue(aWorkingStackManager.isVisible(stack[1]));
		assertTrue(aWorkingStackManager.isVisible(stack[2]));
	}
	
	@Test
	public void testShowHideTop()
	{
		aWorkingStackManager.initialize(new Deck());
		Card[] one = aWorkingStackManager.getStack(TableauPile.FIRST); 
		Card[] two = aWorkingStackManager.getStack(TableauPile.SECOND); 
		assertTrue(aWorkingStackManager.isVisible(one[0]));
		aWorkingStackManager.hideTop(TableauPile.FIRST);
		assertFalse(aWorkingStackManager.isVisible(one[0]));
		aWorkingStackManager.showTop(TableauPile.FIRST);
		assertTrue(aWorkingStackManager.isVisible(one[0]));
		
		assertTrue(aWorkingStackManager.isVisible(two[1]));
		aWorkingStackManager.hideTop(TableauPile.SECOND);
		assertFalse(aWorkingStackManager.isVisible(two[1]));
		aWorkingStackManager.showTop(TableauPile.SECOND);
		assertTrue(aWorkingStackManager.isVisible(two[1]));
	}
}
