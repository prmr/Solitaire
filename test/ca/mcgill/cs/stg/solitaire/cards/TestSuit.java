/*******************************************************************************
 * Solitaire
 *  
 *  Copyright (C) 2016-2024 by Martin P. Robillard
 *  
 *  See: https://github.com/prmr/Solitaire
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/.
 *******************************************************************************/
package ca.mcgill.cs.stg.solitaire.cards;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TestSuit
{
	@Test
	void testSameColor()
	{
		assertTrue(Suit.CLUBS.sameColorAs(Suit.CLUBS));
		assertFalse(Suit.CLUBS.sameColorAs(Suit.DIAMONDS));
		assertFalse(Suit.CLUBS.sameColorAs(Suit.HEARTS));
		assertTrue(Suit.CLUBS.sameColorAs(Suit.SPADES));
		assertFalse(Suit.DIAMONDS.sameColorAs(Suit.CLUBS));
		assertTrue(Suit.DIAMONDS.sameColorAs(Suit.DIAMONDS));
		assertTrue(Suit.DIAMONDS.sameColorAs(Suit.HEARTS));
		assertFalse(Suit.DIAMONDS.sameColorAs(Suit.SPADES));
		assertFalse(Suit.HEARTS.sameColorAs(Suit.CLUBS));
		assertTrue(Suit.HEARTS.sameColorAs(Suit.DIAMONDS));
		assertTrue(Suit.HEARTS.sameColorAs(Suit.HEARTS));
		assertFalse(Suit.HEARTS.sameColorAs(Suit.SPADES));
		assertTrue(Suit.SPADES.sameColorAs(Suit.CLUBS));
		assertFalse(Suit.SPADES.sameColorAs(Suit.DIAMONDS));
		assertFalse(Suit.SPADES.sameColorAs(Suit.HEARTS));
		assertTrue(Suit.SPADES.sameColorAs(Suit.SPADES));

	}
}
