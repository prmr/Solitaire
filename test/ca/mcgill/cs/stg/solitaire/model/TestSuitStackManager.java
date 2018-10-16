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

import org.junit.Before;
import org.junit.Test;

import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.cards.Rank;
import ca.mcgill.cs.stg.solitaire.cards.Suit;
import ca.mcgill.cs.stg.solitaire.model.GameModel.SuitStackIndex;

public class TestSuitStackManager
{
	private FoundationPiles aSuitStackManager;
	private static final Card CAC = Card.get(Rank.ACE, Suit.CLUBS);
	private static final Card CAD = Card.get(Rank.ACE, Suit.DIAMONDS);
	private static final Card C3D = Card.get(Rank.THREE, Suit.DIAMONDS);
	
	@Before
	public void setup()
	{
		aSuitStackManager = new FoundationPiles();
	}
	
	@Test
	public void testInitialize()
	{
		assertTrue( aSuitStackManager.isEmpty(SuitStackIndex.FIRST));
		assertTrue( aSuitStackManager.isEmpty(SuitStackIndex.SECOND));
		assertTrue( aSuitStackManager.isEmpty(SuitStackIndex.THIRD));
		assertTrue( aSuitStackManager.isEmpty(SuitStackIndex.FOURTH));
	}
	
	@Test
	public void testPushPop()
	{
		aSuitStackManager.push(CAC, SuitStackIndex.FIRST);
		assertTrue( aSuitStackManager.isEmpty(SuitStackIndex.SECOND));
		assertTrue( aSuitStackManager.isEmpty(SuitStackIndex.THIRD));
		assertTrue( aSuitStackManager.isEmpty(SuitStackIndex.FOURTH));
		aSuitStackManager.push(CAD, SuitStackIndex.SECOND);
		assertEquals(CAD, aSuitStackManager.peek(SuitStackIndex.SECOND));
		aSuitStackManager.push(C3D, SuitStackIndex.SECOND);
		assertEquals(C3D, aSuitStackManager.peek(SuitStackIndex.SECOND));
		aSuitStackManager.pop(SuitStackIndex.SECOND);
		assertEquals(CAD, aSuitStackManager.peek(SuitStackIndex.SECOND));
		aSuitStackManager.pop(SuitStackIndex.SECOND);
		assertTrue( aSuitStackManager.isEmpty(SuitStackIndex.SECOND));
	}
	
	@Test
	public void testGetScore()
	{
		assertEquals(0, aSuitStackManager.getTotalSize());
		aSuitStackManager.push(CAC, SuitStackIndex.FIRST);
		aSuitStackManager.push(CAD, SuitStackIndex.SECOND);
		assertEquals(2, aSuitStackManager.getTotalSize());
	}
}
