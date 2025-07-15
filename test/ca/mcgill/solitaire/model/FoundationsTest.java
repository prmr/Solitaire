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

import static ca.mcgill.solitaire.testutils.Cards.C2C;
import static ca.mcgill.solitaire.testutils.Cards.C3C;
import static ca.mcgill.solitaire.testutils.Cards.C3D;
import static ca.mcgill.solitaire.testutils.Cards.C4C;
import static ca.mcgill.solitaire.testutils.Cards.CAC;
import static ca.mcgill.solitaire.testutils.Cards.CAD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class FoundationsTest {
	
	private Foundations aFoundationPiles = new Foundations();

	@ParameterizedTest
	@EnumSource(FoundationPile.class)
	void testInitialize(FoundationPile pFoundationPile) {
		assertTrue(aFoundationPiles.isEmpty(pFoundationPile));
	}
	
	@ParameterizedTest
	@EnumSource(FoundationPile.class)
	void testPush(FoundationPile pFoundationPile) {
		
		// From empty
		aFoundationPiles.push(CAC, pFoundationPile);
		assertSame(CAC, aFoundationPiles.peek(pFoundationPile));
		
		// From not empty
		aFoundationPiles.push(C2C, pFoundationPile);
		assertSame(C2C, aFoundationPiles.peek(pFoundationPile));
	}
	
	@ParameterizedTest
	@EnumSource(FoundationPile.class)
	void testPop(FoundationPile pFoundationFile) {
		aFoundationPiles.push(CAC, pFoundationFile);
		aFoundationPiles.push(C2C, pFoundationFile);
		
		assertSame(C2C, aFoundationPiles.pop(pFoundationFile));
		assertSame(CAC, aFoundationPiles.pop(pFoundationFile));
	}
	
	@Test
	void testGetScore_Zero() {
		assertEquals(0, aFoundationPiles.getTotalSize());
	}
	
	@Test
	void testGetScore_NonZero() {
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
		aFoundationPiles.push(CAC, FoundationPile.FIRST);
		aFoundationPiles.push(C2C, FoundationPile.FIRST);
		assertFalse(aFoundationPiles.canMoveTo(C3D, FoundationPile.FIRST));
	}

	@Test
	void testCanMoveTo_NotEmpty_SameSuit_NotInSequence() {
		aFoundationPiles.push(CAC, FoundationPile.FIRST);
		aFoundationPiles.push(C2C, FoundationPile.FIRST);
		assertFalse(aFoundationPiles.canMoveTo(C4C, FoundationPile.FIRST));
	}

	@Test
	void testCanMoveTo_NotEmpty_SameSuit_InSequence() {
		aFoundationPiles.push(CAC, FoundationPile.FIRST);
		aFoundationPiles.push(C2C, FoundationPile.FIRST);
		assertTrue(aFoundationPiles.canMoveTo(C3C, FoundationPile.FIRST));
	}
}
