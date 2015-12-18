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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.cards.Card.Rank;
import ca.mcgill.cs.stg.solitaire.cards.Card.Suit;

public class TestSuitStackManager
{
	private SuitStackManager aSuitStackManager;
	private static final Card CAC = Card.get(Rank.ACE, Suit.CLUBS);
	private static final Card CAD = Card.get(Rank.ACE, Suit.DIAMONDS);
	private static final Card C3D = Card.get(Rank.THREE, Suit.DIAMONDS);
	
	@Before
	public void setup()
	{
		aSuitStackManager = new SuitStackManager();
	}
	
	@Test
	public void testInitialize()
	{
		assertTrue( aSuitStackManager.isEmpty(Suit.CLUBS));
		assertTrue( aSuitStackManager.isEmpty(Suit.DIAMONDS));
		assertTrue( aSuitStackManager.isEmpty(Suit.HEARTS));
		assertTrue( aSuitStackManager.isEmpty(Suit.SPADES));
	}
	
	@Test
	public void testPushPop()
	{
		aSuitStackManager.push(CAC);
		assertTrue( aSuitStackManager.isEmpty(Suit.DIAMONDS));
		assertTrue( aSuitStackManager.isEmpty(Suit.HEARTS));
		assertTrue( aSuitStackManager.isEmpty(Suit.SPADES));
		aSuitStackManager.push(CAD);
		assertEquals(CAD, aSuitStackManager.peek(Suit.DIAMONDS));
		aSuitStackManager.push(C3D);
		assertEquals(C3D, aSuitStackManager.peek(Suit.DIAMONDS));
		aSuitStackManager.pop(Suit.DIAMONDS);
		assertEquals(CAD, aSuitStackManager.peek(Suit.DIAMONDS));
		aSuitStackManager.pop(Suit.DIAMONDS);
		assertTrue( aSuitStackManager.isEmpty(Suit.DIAMONDS));
	}
}
