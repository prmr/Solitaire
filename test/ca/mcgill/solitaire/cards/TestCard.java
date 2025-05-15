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
package ca.mcgill.solitaire.cards;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestCard
{	
	@Test
	void testToString()
	{
		assertEquals("Ace of Clubs", Card.get(Rank.ACE, Suit.CLUBS).toString());
		assertEquals("Two of Clubs", Card.get(Rank.TWO, Suit.CLUBS).toString());
		assertEquals("Three of Clubs", Card.get(Rank.THREE, Suit.CLUBS).toString());
		assertEquals("Four of Diamonds", Card.get(Rank.FOUR, Suit.DIAMONDS).toString());
		assertEquals("Five of Diamonds", Card.get(Rank.FIVE, Suit.DIAMONDS).toString());
		assertEquals("Six of Diamonds", Card.get(Rank.SIX, Suit.DIAMONDS).toString());
		assertEquals("Seven of Hearts", Card.get(Rank.SEVEN, Suit.HEARTS).toString());
		assertEquals("Eight of Hearts", Card.get(Rank.EIGHT, Suit.HEARTS).toString());
		assertEquals("Nine of Hearts", Card.get(Rank.NINE, Suit.HEARTS).toString());
		assertEquals("Ten of Spades", Card.get(Rank.TEN, Suit.SPADES).toString());
		assertEquals("Jack of Spades", Card.get(Rank.JACK, Suit.SPADES).toString());
		assertEquals("Queen of Spades", Card.get(Rank.QUEEN, Suit.SPADES).toString());
		assertEquals("King of Spades", Card.get(Rank.KING, Suit.SPADES).toString());
	}
}
