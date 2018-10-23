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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.Optional;

import org.junit.Test;

import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.cards.CardStack;
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
	
	@SuppressWarnings("unchecked")
	private Optional<Card> getPreviousCard(Card pCard)
	{
		try
		{
			Method method = Tableau.class.getDeclaredMethod("getPreviousCard", Card.class);
			method.setAccessible(true);
			return (Optional<Card>) method.invoke(aTableau, pCard);
		}
		catch( ReflectiveOperationException exception )
		{
			exception.printStackTrace();
			fail();
			return Optional.empty();
		}
	}
	
	@Test
	public void testGetPreviousCard_First()
	{
		aTableau.push(CAC, TableauPile.FIRST);
		assertFalse(getPreviousCard(CAC).isPresent());
	}
	
	@Test
	public void testGetPreviousCard_Second()
	{
		aTableau.push(CAC, TableauPile.FIRST);
		aTableau.push(C5D, TableauPile.FIRST);
		assertSame(CAC, getPreviousCard(C5D).get());
	}
	
	@Test
	public void testInitialize()
	{
		for( TableauPile index : TableauPile.values())
		{
			assertEquals(0, aTableau.getPile(index).size());
		}
		Deck deck = new Deck();
		aTableau.initialize(deck);
		assertEquals(1,aTableau.getPile(TableauPile.FIRST).size());
		assertEquals(2,aTableau.getPile(TableauPile.SECOND).size());
		assertEquals(3,aTableau.getPile(TableauPile.THIRD).size());
		assertEquals(4,aTableau.getPile(TableauPile.FOURTH).size());
		assertEquals(5,aTableau.getPile(TableauPile.FIFTH).size());
		assertEquals(6,aTableau.getPile(TableauPile.SIXTH).size());
		assertEquals(7,aTableau.getPile(TableauPile.SEVENTH).size());
		deck.shuffle();
		aTableau.initialize(deck);
		assertEquals(1,aTableau.getPile(TableauPile.FIRST).size());
		assertEquals(2,aTableau.getPile(TableauPile.SECOND).size());
		assertEquals(3,aTableau.getPile(TableauPile.THIRD).size());
		assertEquals(4,aTableau.getPile(TableauPile.FOURTH).size());
		assertEquals(5,aTableau.getPile(TableauPile.FIFTH).size());
		assertEquals(6,aTableau.getPile(TableauPile.SIXTH).size());
		assertEquals(7,aTableau.getPile(TableauPile.SEVENTH).size());
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
		CardStack sequence = aTableau.getSequence(C5D, TableauPile.SECOND);
		assertEquals(1, sequence.size());
		assertEquals(C5D, sequence.peek(0));
		aTableau.push(C4C, TableauPile.SECOND);
		sequence = aTableau.getSequence(C5D, TableauPile.SECOND);
		assertEquals(2, sequence.size());
		assertEquals(C5D, sequence.peek(0));
		assertEquals(C4C, sequence.peek(1));
		sequence = aTableau.getSequence(C4C, TableauPile.SECOND);
		assertEquals(1, sequence.size());
		assertEquals(C4C, sequence.peek(0));
	}
	
	@Test 
	public void testMoveWithin()
	{
		Deck deck = new Deck();
		aTableau.initialize(deck);
		CardStack stack2a = aTableau.getPile(TableauPile.SECOND);
		CardStack stack4a = aTableau.getPile(TableauPile.FOURTH);
		aTableau.moveWithin(stack2a.peek(1), TableauPile.SECOND, TableauPile.FOURTH);
		CardStack stack2b = aTableau.getPile(TableauPile.SECOND);
		CardStack stack4b = aTableau.getPile(TableauPile.FOURTH);
		assertEquals(1,stack2b.size());
		assertEquals(5,stack4b.size());
		assertEquals(stack2a.peek(0), stack2b.peek(0));
		assertEquals(stack4a.peek(0), stack4b.peek(0));
		assertEquals(stack4a.peek(1), stack4b.peek(1));
		assertEquals(stack4a.peek(2), stack4b.peek(2));
		assertEquals(stack4a.peek(3), stack4b.peek(3));
		assertEquals(stack2a.peek(1), stack4b.peek(4));
		assertTrue(aTableau.isVisible(stack4b.peek(4)));
		aTableau.moveWithin(stack4b.peek(3), TableauPile.FOURTH, TableauPile.SECOND);
		CardStack stack2c = aTableau.getPile(TableauPile.SECOND);
		assertEquals(3,stack2c.size());
		assertEquals(stack2a.peek(0), stack2c.peek(0));
		assertEquals(stack4b.peek(3), stack2c.peek(1));
		assertEquals(stack4b.peek(4), stack2c.peek(2));
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
			for( Card card : aTableau.getPile(index) )
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
			CardStack cards = aTableau.getPile(index);
			for( int i = 0; i < cards.size()-1; i++ )
			{
				assertFalse(aTableau.isVisible(cards.peek(i)));
			}
			assertTrue(aTableau.isVisible(cards.peek()));
		}
		aTableau.push(deck.draw(), TableauPile.SECOND);
		CardStack stack = aTableau.getPile(TableauPile.SECOND);
		assertFalse(aTableau.isVisible(stack.peek(0)));
		assertTrue(aTableau.isVisible(stack.peek(1)));
		assertTrue(aTableau.isVisible(stack.peek(2)));
	}
	
	@Test
	public void testShowHideTop()
	{
		aTableau.initialize(new Deck());
		CardStack one = aTableau.getPile(TableauPile.FIRST); 
		CardStack two = aTableau.getPile(TableauPile.SECOND); 
		assertTrue(aTableau.isVisible(one.peek(0)));
		aTableau.hideTop(TableauPile.FIRST);
		assertFalse(aTableau.isVisible(one.peek(0)));
		aTableau.showTop(TableauPile.FIRST);
		assertTrue(aTableau.isVisible(one.peek(0)));
		
		assertTrue(aTableau.isVisible(two.peek(1)));
		aTableau.hideTop(TableauPile.SECOND);
		assertFalse(aTableau.isVisible(two.peek(1)));
		aTableau.showTop(TableauPile.SECOND);
		assertTrue(aTableau.isVisible(two.peek(1)));
	}
}
