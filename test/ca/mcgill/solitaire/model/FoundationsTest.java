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

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import ca.mcgill.solitaire.cards.Card;
import ca.mcgill.solitaire.cards.Rank;
import ca.mcgill.solitaire.cards.Suit;

public class FoundationsTest {
	
	private Foundations aFoundationPiles = new Foundations();
	private static final Card ACE_OF_CLUBS = Card.get(Rank.ACE, Suit.CLUBS);
	private static final Card TWO_OF_CLUBS = Card.get(Rank.TWO, Suit.CLUBS);
	private static final Card ACE_OF_DIAMONDS = Card.get(Rank.ACE, Suit.DIAMONDS);
	private static final Card THREE_OF_DIAMONDS = Card.get(Rank.THREE, Suit.DIAMONDS);

	@ParameterizedTest
	@EnumSource(FoundationPile.class)
	void testInitialize(FoundationPile pFoundationPile) {
		assertTrue(aFoundationPiles.isEmpty(pFoundationPile));
	}
	
	@ParameterizedTest
	@EnumSource(FoundationPile.class)
	void testPush(FoundationPile pFoundationPile) {
		
		// From empty
		aFoundationPiles.push(ACE_OF_CLUBS, pFoundationPile);
		assertSame(ACE_OF_CLUBS, aFoundationPiles.peek(pFoundationPile));
		
		// From not empty
		aFoundationPiles.push(TWO_OF_CLUBS, pFoundationPile);
		assertSame(TWO_OF_CLUBS, aFoundationPiles.peek(pFoundationPile));
	}
	
	@ParameterizedTest
	@EnumSource(FoundationPile.class)
	void testPop(FoundationPile pFoundationFile) {
		aFoundationPiles.push(ACE_OF_CLUBS, pFoundationFile);
		aFoundationPiles.push(TWO_OF_CLUBS, pFoundationFile);
		
		assertSame(TWO_OF_CLUBS, aFoundationPiles.pop(pFoundationFile));
		assertSame(ACE_OF_CLUBS, aFoundationPiles.pop(pFoundationFile));
	}
	
	@Test
	void testGetScore_Zero() {
		assertEquals(0, aFoundationPiles.getTotalSize());
	}
	
	@Test
	void testGetScore_NonZero() {
		assertEquals(0, aFoundationPiles.getTotalSize());
		aFoundationPiles.push(ACE_OF_CLUBS, FoundationPile.FIRST);
		aFoundationPiles.push(ACE_OF_DIAMONDS, FoundationPile.SECOND);
		assertEquals(2, aFoundationPiles.getTotalSize());
	}

	@Test
	void testCanMoveTo_Empty() {
		assertTrue(aFoundationPiles.canMoveTo(ACE_OF_CLUBS, FoundationPile.FIRST));
		assertFalse(aFoundationPiles.canMoveTo(THREE_OF_DIAMONDS, FoundationPile.SECOND));
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
