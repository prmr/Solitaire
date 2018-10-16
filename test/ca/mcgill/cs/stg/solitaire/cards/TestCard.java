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
package ca.mcgill.cs.stg.solitaire.cards;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestCard
{
	@Test
	public void testToIDString()
	{
		assertEquals("0", Card.get(Rank.ACE, Suit.CLUBS).getIDString());
		assertEquals("9", Card.get(Rank.TEN, Suit.CLUBS).getIDString());
		assertEquals("12", Card.get(Rank.KING, Suit.CLUBS).getIDString());
		assertEquals("13", Card.get(Rank.ACE, Suit.DIAMONDS).getIDString());
		assertEquals("26", Card.get(Rank.ACE, Suit.HEARTS).getIDString());
		assertEquals("39", Card.get(Rank.ACE, Suit.SPADES).getIDString());
	}
	
	@Test
	public void testFromIDString()
	{
		assertEquals(Card.get(Rank.ACE, Suit.CLUBS), Card.get("0"));
		assertEquals(Card.get(Rank.TEN, Suit.CLUBS), Card.get("9"));
		assertEquals(Card.get(Rank.KING, Suit.CLUBS), Card.get("12"));
		assertEquals(Card.get(Rank.ACE, Suit.DIAMONDS), Card.get("13"));
		assertEquals(Card.get(Rank.ACE, Suit.HEARTS), Card.get("26"));
		assertEquals(Card.get(Rank.ACE, Suit.SPADES), Card.get("39"));
	}
	
	@Test
	public void testToString()
	{
		assertEquals("ACE of CLUBS", Card.get(Rank.ACE, Suit.CLUBS).toString());
		assertEquals("TWO of CLUBS", Card.get(Rank.TWO, Suit.CLUBS).toString());
		assertEquals("KING of CLUBS", Card.get(Rank.KING, Suit.CLUBS).toString());
		assertEquals("ACE of DIAMONDS", Card.get(Rank.ACE, Suit.DIAMONDS).toString());
		assertEquals("TWO of DIAMONDS", Card.get(Rank.TWO, Suit.DIAMONDS).toString());
		assertEquals("KING of DIAMONDS", Card.get(Rank.KING, Suit.DIAMONDS).toString());
		assertEquals("ACE of HEARTS", Card.get(Rank.ACE, Suit.HEARTS).toString());
		assertEquals("TWO of HEARTS", Card.get(Rank.TWO, Suit.HEARTS).toString());
		assertEquals("KING of HEARTS", Card.get(Rank.KING, Suit.HEARTS).toString());
		assertEquals("ACE of SPADES", Card.get(Rank.ACE, Suit.SPADES).toString());
		assertEquals("TWO of SPADES", Card.get(Rank.TWO, Suit.SPADES).toString());
		assertEquals("KING of SPADES", Card.get(Rank.KING, Suit.SPADES).toString());
	}
}
