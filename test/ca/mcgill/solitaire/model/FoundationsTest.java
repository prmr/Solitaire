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
package ca.mcgill.solitaire.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ca.mcgill.solitaire.cards.Card;
import ca.mcgill.solitaire.cards.Rank;
import ca.mcgill.solitaire.cards.Suit;

public class FoundationsTest {
	private Foundations aFoundationPiles = new Foundations();
	private static final Card CAC = Card.get(Rank.ACE, Suit.CLUBS);
	private static final Card CAD = Card.get(Rank.ACE, Suit.DIAMONDS);
	private static final Card C3D = Card.get(Rank.THREE, Suit.DIAMONDS);

	@Test
	void testInitialize() {
		assertTrue(aFoundationPiles.isEmpty(FoundationPile.FIRST));
		assertTrue(aFoundationPiles.isEmpty(FoundationPile.SECOND));
		assertTrue(aFoundationPiles.isEmpty(FoundationPile.THIRD));
		assertTrue(aFoundationPiles.isEmpty(FoundationPile.FOURTH));
	}

	@Test
	void testPushPop() {
		aFoundationPiles.push(CAC, FoundationPile.FIRST);
		assertTrue(aFoundationPiles.isEmpty(FoundationPile.SECOND));
		assertTrue(aFoundationPiles.isEmpty(FoundationPile.THIRD));
		assertTrue(aFoundationPiles.isEmpty(FoundationPile.FOURTH));
		aFoundationPiles.push(CAD, FoundationPile.SECOND);
		assertEquals(CAD, aFoundationPiles.peek(FoundationPile.SECOND));
		aFoundationPiles.push(C3D, FoundationPile.SECOND);
		assertEquals(C3D, aFoundationPiles.peek(FoundationPile.SECOND));
		aFoundationPiles.pop(FoundationPile.SECOND);
		assertEquals(CAD, aFoundationPiles.peek(FoundationPile.SECOND));
		aFoundationPiles.pop(FoundationPile.SECOND);
		assertTrue(aFoundationPiles.isEmpty(FoundationPile.SECOND));
	}

	@Test
	void testGetScore() {
		assertEquals(0, aFoundationPiles.getTotalSize());
		aFoundationPiles.push(CAC, FoundationPile.FIRST);
		aFoundationPiles.push(CAD, FoundationPile.SECOND);
		assertEquals(2, aFoundationPiles.getTotalSize());
	}

	@Test
	void testCanMoveTo_Empty() {
		assertTrue(aFoundationPiles.canMoveTo(CAC, FoundationPile.FIRST));
		assertFalse(aFoundationPiles.canMoveTo(C3D, FoundationPile.SECOND));
	}

	@Test
	void testCanMoveTo_NotEmpty_NotSameSuit() {
		aFoundationPiles.push(Card.get(Rank.ACE, Suit.CLUBS), FoundationPile.FIRST);
		aFoundationPiles.push(Card.get(Rank.TWO, Suit.CLUBS), FoundationPile.FIRST);
		assertFalse(aFoundationPiles.canMoveTo(Card.get(Rank.THREE, Suit.DIAMONDS), FoundationPile.FIRST));
	}

	@Test
	void testCanMoveTo_NotEmpty_SameSuit_NotInSequence() {
		aFoundationPiles.push(Card.get(Rank.ACE, Suit.CLUBS), FoundationPile.FIRST);
		aFoundationPiles.push(Card.get(Rank.TWO, Suit.CLUBS), FoundationPile.FIRST);
		assertFalse(aFoundationPiles.canMoveTo(Card.get(Rank.FOUR, Suit.CLUBS), FoundationPile.FIRST));
	}

	@Test
	void testCanMoveTo_NotEmpty_SameSuit_InSequence() {
		aFoundationPiles.push(Card.get(Rank.ACE, Suit.CLUBS), FoundationPile.FIRST);
		aFoundationPiles.push(Card.get(Rank.TWO, Suit.CLUBS), FoundationPile.FIRST);
		assertTrue(aFoundationPiles.canMoveTo(Card.get(Rank.THREE, Suit.CLUBS), FoundationPile.FIRST));
	}
}
