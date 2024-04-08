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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestCard
{
	@Test
	void testToString()
	{
		assertEquals("Ace of Clubs", Card.get(Rank.ACE, Suit.CLUBS).toString());
		assertEquals("Two of Clubs", Card.get(Rank.TWO, Suit.CLUBS).toString());
		assertEquals("King of Clubs", Card.get(Rank.KING, Suit.CLUBS).toString());
		assertEquals("Ace of Diamonds", Card.get(Rank.ACE, Suit.DIAMONDS).toString());
		assertEquals("Two of Diamonds", Card.get(Rank.TWO, Suit.DIAMONDS).toString());
		assertEquals("King of Diamonds", Card.get(Rank.KING, Suit.DIAMONDS).toString());
		assertEquals("Ace of Hearts", Card.get(Rank.ACE, Suit.HEARTS).toString());
		assertEquals("Two of Hearts", Card.get(Rank.TWO, Suit.HEARTS).toString());
		assertEquals("King of Hearts", Card.get(Rank.KING, Suit.HEARTS).toString());
		assertEquals("Ace of Spades", Card.get(Rank.ACE, Suit.SPADES).toString());
		assertEquals("Two of Spades", Card.get(Rank.TWO, Suit.SPADES).toString());
		assertEquals("King of Spades", Card.get(Rank.KING, Suit.SPADES).toString());
	}
}
