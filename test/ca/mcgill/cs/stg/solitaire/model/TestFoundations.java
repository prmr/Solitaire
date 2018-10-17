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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.cards.Rank;
import ca.mcgill.cs.stg.solitaire.cards.Suit;

public class TestFoundations
{
	private Foundations aFoundationPiles;
	private static final Card CAC = Card.get(Rank.ACE, Suit.CLUBS);
	private static final Card CAD = Card.get(Rank.ACE, Suit.DIAMONDS);
	private static final Card C3D = Card.get(Rank.THREE, Suit.DIAMONDS);
	
	@Before
	public void setup()
	{
		aFoundationPiles = new Foundations();
	}
	
	@Test
	public void testInitialize()
	{
		assertTrue( aFoundationPiles.isEmpty(FoundationPile.FIRST));
		assertTrue( aFoundationPiles.isEmpty(FoundationPile.SECOND));
		assertTrue( aFoundationPiles.isEmpty(FoundationPile.THIRD));
		assertTrue( aFoundationPiles.isEmpty(FoundationPile.FOURTH));
	}
	
	@Test
	public void testPushPop()
	{
		aFoundationPiles.push(CAC, FoundationPile.FIRST);
		assertTrue( aFoundationPiles.isEmpty(FoundationPile.SECOND));
		assertTrue( aFoundationPiles.isEmpty(FoundationPile.THIRD));
		assertTrue( aFoundationPiles.isEmpty(FoundationPile.FOURTH));
		aFoundationPiles.push(CAD, FoundationPile.SECOND);
		assertEquals(CAD, aFoundationPiles.peek(FoundationPile.SECOND));
		aFoundationPiles.push(C3D, FoundationPile.SECOND);
		assertEquals(C3D, aFoundationPiles.peek(FoundationPile.SECOND));
		aFoundationPiles.pop(FoundationPile.SECOND);
		assertEquals(CAD, aFoundationPiles.peek(FoundationPile.SECOND));
		aFoundationPiles.pop(FoundationPile.SECOND);
		assertTrue( aFoundationPiles.isEmpty(FoundationPile.SECOND));
	}
	
	@Test
	public void testGetScore()
	{
		assertEquals(0, aFoundationPiles.getTotalSize());
		aFoundationPiles.push(CAC, FoundationPile.FIRST);
		aFoundationPiles.push(CAD, FoundationPile.SECOND);
		assertEquals(2, aFoundationPiles.getTotalSize());
	}
	
	@Test
	public void testCanMoveTo_Empty()
	{
		assertTrue(aFoundationPiles.canMoveTo(CAC, FoundationPile.FIRST));
		assertFalse(aFoundationPiles.canMoveTo(C3D, FoundationPile.SECOND));
	}
	
	@Test
	public void testCanMoveTo_NotEmpty_NotSameSuit()
	{
		aFoundationPiles.push(Card.get(Rank.ACE, Suit.CLUBS), FoundationPile.FIRST);
		aFoundationPiles.push(Card.get(Rank.TWO, Suit.CLUBS), FoundationPile.FIRST);
		assertFalse(aFoundationPiles.canMoveTo(Card.get(Rank.THREE, Suit.DIAMONDS), FoundationPile.FIRST));
	}
	
	@Test
	public void testCanMoveTo_NotEmpty_SameSuit_NotInSequence()
	{
		aFoundationPiles.push(Card.get(Rank.ACE, Suit.CLUBS), FoundationPile.FIRST);
		aFoundationPiles.push(Card.get(Rank.TWO, Suit.CLUBS), FoundationPile.FIRST);
		assertFalse(aFoundationPiles.canMoveTo(Card.get(Rank.FOUR, Suit.CLUBS), FoundationPile.FIRST));
	}
	
	@Test
	public void testCanMoveTo_NotEmpty_SameSuit_InSequence()
	{
		aFoundationPiles.push(Card.get(Rank.ACE, Suit.CLUBS), FoundationPile.FIRST);
		aFoundationPiles.push(Card.get(Rank.TWO, Suit.CLUBS), FoundationPile.FIRST);
		assertTrue(aFoundationPiles.canMoveTo(Card.get(Rank.THREE, Suit.CLUBS), FoundationPile.FIRST));
	}
}
