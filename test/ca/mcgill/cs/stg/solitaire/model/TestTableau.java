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

public class TestTableau
{
	private Tableau aTableau = new Tableau();
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
			assertEquals(0, aTableau.getStack(index).length);
		}
		Deck deck = new Deck();
		aTableau.initialize(deck);
		assertEquals(1,aTableau.getStack(TableauPile.FIRST).length);
		assertEquals(2,aTableau.getStack(TableauPile.SECOND).length);
		assertEquals(3,aTableau.getStack(TableauPile.THIRD).length);
		assertEquals(4,aTableau.getStack(TableauPile.FOURTH).length);
		assertEquals(5,aTableau.getStack(TableauPile.FIFTH).length);
		assertEquals(6,aTableau.getStack(TableauPile.SIXTH).length);
		assertEquals(7,aTableau.getStack(TableauPile.SEVENTH).length);
		deck.shuffle();
		aTableau.initialize(deck);
		assertEquals(1,aTableau.getStack(TableauPile.FIRST).length);
		assertEquals(2,aTableau.getStack(TableauPile.SECOND).length);
		assertEquals(3,aTableau.getStack(TableauPile.THIRD).length);
		assertEquals(4,aTableau.getStack(TableauPile.FOURTH).length);
		assertEquals(5,aTableau.getStack(TableauPile.FIFTH).length);
		assertEquals(6,aTableau.getStack(TableauPile.SIXTH).length);
		assertEquals(7,aTableau.getStack(TableauPile.SEVENTH).length);
	}
	
	@Test
	public void testRevealsTop()
	{
		Deck deck = new Deck();
		aTableau.initialize(deck);
		Card[] stack = aTableau.getStack(TableauPile.FIRST);
		assertFalse(aTableau.revealsTop(stack[0], TableauPile.FIRST));
		aTableau.pop(TableauPile.FIRST);
		assertFalse(aTableau.revealsTop(stack[0], TableauPile.FIRST));
		assertFalse(aTableau.revealsTop(deck.draw(), TableauPile.FIRST));
		assertFalse(aTableau.revealsTop(deck.draw(), TableauPile.SECOND));
		stack = aTableau.getStack(TableauPile.SECOND);
		assertFalse(aTableau.revealsTop(stack[0], TableauPile.SECOND));
		assertTrue(aTableau.revealsTop(stack[1], TableauPile.SECOND));
		
		stack = aTableau.getStack(TableauPile.THIRD);
		assertFalse(aTableau.revealsTop(stack[0], TableauPile.THIRD));
		assertFalse(aTableau.revealsTop(stack[1], TableauPile.THIRD));
		assertTrue(aTableau.revealsTop(stack[2], TableauPile.THIRD));
		
		stack = aTableau.getStack(TableauPile.FOURTH);
		assertFalse(aTableau.revealsTop(stack[0], TableauPile.FOURTH));
		assertFalse(aTableau.revealsTop(stack[1], TableauPile.FOURTH));
		assertFalse(aTableau.revealsTop(stack[2], TableauPile.FOURTH));
		assertTrue(aTableau.revealsTop(stack[3], TableauPile.FOURTH));
		Card card = deck.draw();
		aTableau.push(card, TableauPile.FOURTH);
		assertFalse(aTableau.revealsTop(card, TableauPile.FOURTH));
	}
	
	@Test
	public void testContains()
	{
		assertFalse(aTableau.contains(CAC, TableauPile.FIRST));
		aTableau.push(CAC, TableauPile.FIRST);
		assertTrue(aTableau.contains(CAC, TableauPile.FIRST));
		aTableau.push(C3H, TableauPile.FIRST);
		aTableau.push(C5D, TableauPile.FIRST);
		assertTrue(aTableau.contains(C5D, TableauPile.FIRST));
	}
	
	@Test
	public void testCanMoveTo()
	{
		assertFalse(aTableau.canMoveTo(CAC, TableauPile.FIRST)); 
		assertTrue(aTableau.canMoveTo(CKC, TableauPile.FIRST)); 
		aTableau.push(C5D, TableauPile.FIRST);
		assertFalse(aTableau.canMoveTo(CAC, TableauPile.FIRST));
		assertFalse(aTableau.canMoveTo(C4D, TableauPile.FIRST));
		assertFalse(aTableau.canMoveTo(C4H, TableauPile.FIRST));
		assertTrue(aTableau.canMoveTo(C4C, TableauPile.FIRST));
		assertTrue(aTableau.canMoveTo(C4S, TableauPile.FIRST));
	}
	
	@Test
	public void testGetSequence()
	{
		aTableau.push(C5D, TableauPile.SECOND);
		Card[] sequence = aTableau.getSequence(C5D, TableauPile.SECOND);
		assertEquals(1, sequence.length);
		assertEquals(C5D, sequence[0]);
		aTableau.push(C4C, TableauPile.SECOND);
		sequence = aTableau.getSequence(C5D, TableauPile.SECOND);
		assertEquals(2, sequence.length);
		assertEquals(C5D, sequence[0]);
		assertEquals(C4C, sequence[1]);
		sequence = aTableau.getSequence(C4C, TableauPile.SECOND);
		assertEquals(1, sequence.length);
		assertEquals(C4C, sequence[0]);
	}
	
	@Test 
	public void testMoveWithin()
	{
		Deck deck = new Deck();
		aTableau.initialize(deck);
		Card[] stack2a = aTableau.getStack(TableauPile.SECOND);
		Card[] stack4a = aTableau.getStack(TableauPile.FOURTH);
		aTableau.moveWithin(stack2a[1], TableauPile.SECOND, TableauPile.FOURTH);
		Card[] stack2b = aTableau.getStack(TableauPile.SECOND);
		Card[] stack4b = aTableau.getStack(TableauPile.FOURTH);
		assertEquals(1,stack2b.length);
		assertEquals(5,stack4b.length);
		assertEquals(stack2a[0], stack2b[0]);
		assertEquals(stack4a[0], stack4b[0]);
		assertEquals(stack4a[1], stack4b[1]);
		assertEquals(stack4a[2], stack4b[2]);
		assertEquals(stack4a[3], stack4b[3]);
		assertEquals(stack2a[1], stack4b[4]);
		assertTrue(aTableau.isVisible(stack4b[4]));
		aTableau.moveWithin(stack4b[3], TableauPile.FOURTH, TableauPile.SECOND);
		Card[] stack2c = aTableau.getStack(TableauPile.SECOND);
		assertEquals(3,stack2c.length);
		assertEquals(stack2a[0], stack2c[0]);
		assertEquals(stack4b[3], stack2c[1]);
		assertEquals(stack4b[4], stack2c[2]);
	}
	
	@Test
	public void testContains2()
	{
		Deck deck = new Deck();
		aTableau.initialize(deck);
		while( !deck.isEmpty() )
		{
			assertFalse(aTableau.contains(deck.draw()));
		}
		for( TableauPile index : TableauPile.values())
		{
			for( Card card : aTableau.getStack(index) )
			{
				assertTrue( aTableau.contains(card));
			}
		}
	}
	
	@Test
	public void testVisibility()
	{
		Deck deck = new Deck();
		aTableau.initialize(deck);
		for( TableauPile index : TableauPile.values())
		{
			Card[] cards = aTableau.getStack(index);
			for( int i = 0; i < cards.length-1; i++ )
			{
				assertFalse(aTableau.isVisible(cards[i]));
			}
			assertTrue(aTableau.isVisible(cards[cards.length-1]));
		}
		aTableau.push(deck.draw(), TableauPile.SECOND);
		Card[] stack = aTableau.getStack(TableauPile.SECOND);
		assertFalse(aTableau.isVisible(stack[0]));
		assertTrue(aTableau.isVisible(stack[1]));
		assertTrue(aTableau.isVisible(stack[2]));
	}
	
	@Test
	public void testShowHideTop()
	{
		aTableau.initialize(new Deck());
		Card[] one = aTableau.getStack(TableauPile.FIRST); 
		Card[] two = aTableau.getStack(TableauPile.SECOND); 
		assertTrue(aTableau.isVisible(one[0]));
		aTableau.hideTop(TableauPile.FIRST);
		assertFalse(aTableau.isVisible(one[0]));
		aTableau.showTop(TableauPile.FIRST);
		assertTrue(aTableau.isVisible(one[0]));
		
		assertTrue(aTableau.isVisible(two[1]));
		aTableau.hideTop(TableauPile.SECOND);
		assertFalse(aTableau.isVisible(two[1]));
		aTableau.showTop(TableauPile.SECOND);
		assertTrue(aTableau.isVisible(two[1]));
	}
}
