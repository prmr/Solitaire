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

import static ca.mcgill.solitaire.testutils.Cards.*;
import static ca.mcgill.solitaire.testutils.Utils.assertCardStackHas;
import static ca.mcgill.solitaire.testutils.Utils.peekAtIndex;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import ca.mcgill.solitaire.cards.Card;
import ca.mcgill.solitaire.cards.CardStack;
import ca.mcgill.solitaire.cards.Deck;
import ca.mcgill.solitaire.testutils.StubDeck;

/**
 * Tests for class Tableau. Because Tableau is a container for
 * seven piles of cards, some tests are parameterized to test
 * the logic for each pile, when applicable.
 */
public class TableauTest {
	
	private Tableau aTableau = new Tableau();

	@SuppressWarnings("unchecked")
	private Optional<Card> executeGetPreviousCard(Card pCard) {
		try {
			Method method = Tableau.class.getDeclaredMethod("getPreviousCard", Card.class);
			method.setAccessible(true);
			return (Optional<Card>) method.invoke(aTableau, pCard);
		}
		catch (ReflectiveOperationException exception) {
			throw new AssertionError(exception);
		}
	}
	
	@ParameterizedTest
	@EnumSource(TableauPile.class)
	void testNewInstance(TableauPile pPile) {
		assertTrue(aTableau.getPile(pPile).isEmpty());
	}
	
	@Test
	void testInitialize() {

		aTableau.initialize(new StubDeck());

		CardStack pile = aTableau.getPile(TableauPile.FIRST);
		assertEquals(1, pile.size());
		assertSame(CKS, pile.peekTop());

		pile = aTableau.getPile(TableauPile.SECOND);
		assertEquals(2, pile.size());
		assertSame(CJS, pile.pop());
		assertSame(CQS, pile.pop());

		pile = aTableau.getPile(TableauPile.THIRD);
		assertEquals(3, pile.size());
		assertSame(C8S, pile.pop());
		assertSame(C9S, pile.pop());
		assertSame(CTS, pile.pop());

		pile = aTableau.getPile(TableauPile.FOURTH);
		assertEquals(4, pile.size());
		assertSame(C4S, pile.pop());
		assertSame(C5S, pile.pop());
		assertSame(C6S, pile.pop());
		assertSame(C7S, pile.pop());

		pile = aTableau.getPile(TableauPile.FIFTH);
		assertEquals(5, pile.size());
		assertSame(CQH, pile.pop());
		assertSame(CKH, pile.pop());
		assertSame(CAS, pile.pop());
		assertSame(C2S, pile.pop());
		assertSame(C3S, pile.pop());

		pile = aTableau.getPile(TableauPile.SIXTH);
		assertEquals(6, pile.size());
		assertSame(C6H, pile.pop());
		assertSame(C7H, pile.pop());
		assertSame(C8H, pile.pop());
		assertSame(C9H, pile.pop());
		assertSame(CTH, pile.pop());
		assertSame(CJH, pile.pop());

		pile = aTableau.getPile(TableauPile.SEVENTH);
		assertEquals(7, pile.size());
		assertSame(CQD, pile.pop());
		assertSame(CKD, pile.pop());
		assertSame(CAH, pile.pop());
		assertSame(C2H, pile.pop());
		assertSame(C3H, pile.pop());
		assertSame(C4H, pile.pop());
		assertSame(C5H, pile.pop());
	}
	
	@ParameterizedTest
	@EnumSource(TableauPile.class)
	void testCanMoveTo_PileEmpty_False(TableauPile pPile) {
		assertFalse(aTableau.canMoveTo(CAC, pPile));
	}
	
	@ParameterizedTest
	@EnumSource(TableauPile.class)
	void testCanMoveTo_PileEmpty_True(TableauPile pPile) {
		assertTrue(aTableau.canMoveTo(CKC, pPile));
	}
	
	@ParameterizedTest
	@EnumSource(TableauPile.class)
	void testCanMoveTo_PileNotEmpty_NotConsecutive(TableauPile pPile) {
		aTableau.push(CKC, pPile);
		assertFalse(aTableau.canMoveTo(CJH, pPile));
	}
	
	@ParameterizedTest
	@EnumSource(TableauPile.class)
	void testCanMoveTo_PileNotEmpty_NotAlternatingColor(TableauPile pPile) {
		aTableau.push(CKC, pPile);
		assertFalse(aTableau.canMoveTo(CQC, pPile));
	}
	
	@ParameterizedTest
	@EnumSource(TableauPile.class)
	void testCanMoveTo_PileNotEmpty_True(TableauPile pPile) {
		aTableau.push(CKC, pPile);
		assertTrue(aTableau.canMoveTo(CQH, pPile));
	}
	
	@ParameterizedTest
	@EnumSource(TableauPile.class)
	void testIsBottomKing_NotKing(TableauPile pPile) {
		aTableau.push(CAC, pPile);
		assertFalse(aTableau.isBottomKing(CAC));
	}
	
	@ParameterizedTest
	@EnumSource(TableauPile.class)
	void testIsBottomKing_NotBottom(TableauPile pPile) {
		aTableau.push(CAC, pPile);
		aTableau.push(CKC, pPile);
		assertFalse(aTableau.isBottomKing(CKC));
	}
	
	@ParameterizedTest
	@EnumSource(TableauPile.class)
	void testIsBottomKing_True(TableauPile pPile) {
		aTableau.push(CKC, pPile);
		aTableau.push(CAC, pPile);
		assertTrue(aTableau.isBottomKing(CKC));
	}
	
	@ParameterizedTest
	@EnumSource(TableauPile.class)
	void testgetPile(TableauPile pPile) {
		aTableau.push(CKC, pPile);
		aTableau.push(CAC, pPile);
		CardStack pile = aTableau.getPile(pPile);
		CardStack pile2 = aTableau.getPile(pPile);
		assertNotSame(pile, pile2);
		assertEquals(2, pile.size());
		assertSame(CAC, pile.pop());
		assertSame(CKC, pile.pop());
	}
	
	@Test
	void testRevealsTop_NoPreviousCard() {
		aTableau.push(CKC, TableauPile.FIRST);
		assertFalse(aTableau.revealsTop(CKC));
	}
	
	@Test
	void testRevealsTop_CardAlreadyVisible() {
		aTableau.push(C2C, TableauPile.FIRST);
		aTableau.hideTop(TableauPile.FIRST);
		aTableau.push(C3C, TableauPile.FIRST);
		aTableau.push(C4C, TableauPile.FIRST);
		assertFalse(aTableau.revealsTop(C4C));
	}
	
	@Test
	void testRevealsTop_CardNotVisible() {
		aTableau.push(C2C, TableauPile.FIRST);
		aTableau.hideTop(TableauPile.FIRST);
		aTableau.push(C3C, TableauPile.FIRST);
		aTableau.hideTop(TableauPile.FIRST);
		aTableau.push(C4C, TableauPile.FIRST);
		assertFalse(aTableau.revealsTop(C3C));
	}
	
	@Test
	void testRevealsTop_True() {
		aTableau.push(C2C, TableauPile.FIRST);
		aTableau.hideTop(TableauPile.FIRST);
		aTableau.push(C3C, TableauPile.FIRST);
		aTableau.hideTop(TableauPile.FIRST);
		aTableau.push(C4C, TableauPile.FIRST);
		assertTrue(aTableau.revealsTop(C4C));
	}
	
	@Test 
	void testMoveWithin_SingleCard() {
		aTableau.push(CAC, TableauPile.FIRST);
		aTableau.push(C2D, TableauPile.SECOND);
		aTableau.moveWithin(CAC, TableauPile.SECOND);
		assertCardStackHas(aTableau.getPile(TableauPile.FIRST));
		assertCardStackHas(aTableau.getPile(TableauPile.SECOND), C2D, CAC);
	}
	
	@Test 
	void testMoveWithin_AllCards() {
		aTableau.push(C3D, TableauPile.FIRST);
		aTableau.push(C2C, TableauPile.FIRST);
		aTableau.push(CAD, TableauPile.FIRST);
		aTableau.push(C4C, TableauPile.SECOND);
		aTableau.moveWithin(C3D, TableauPile.SECOND);
		assertCardStackHas(aTableau.getPile(TableauPile.FIRST));
		assertCardStackHas(aTableau.getPile(TableauPile.SECOND), C4C, C3D, C2C, CAD);
	}
	
	@Test 
	void testMoveWithin_PartialCards() {
		aTableau.push(C3D, TableauPile.FIRST);
		aTableau.push(C2C, TableauPile.FIRST);
		aTableau.push(CAD, TableauPile.FIRST);
		aTableau.push(C3H, TableauPile.SECOND);
		aTableau.moveWithin(C2C, TableauPile.SECOND);
		assertCardStackHas(aTableau.getPile(TableauPile.FIRST), C3D);
		assertCardStackHas(aTableau.getPile(TableauPile.SECOND), C3H, C2C, CAD);
	}
	
	@Test 
	void testMoveWithin_ToEmptyPile() {
		aTableau.push(CKD, TableauPile.FIRST);
		aTableau.push(CQC, TableauPile.FIRST);
		aTableau.moveWithin(CKD, TableauPile.SECOND);
		assertCardStackHas(aTableau.getPile(TableauPile.FIRST));
		assertCardStackHas(aTableau.getPile(TableauPile.SECOND), CKD, CQC);
	}
	
	// ***** BELOW ARE OLD TESTS ***** //
	
	@Test
	void testGetPreviousCard_First() {
		aTableau.push(CAC, TableauPile.FIRST);
		assertFalse(executeGetPreviousCard(CAC).isPresent());
	}

	@Test
	void testGetPreviousCard_Second() {
		aTableau.push(CAC, TableauPile.FIRST);
		aTableau.push(C5D, TableauPile.FIRST);
		assertSame(CAC, executeGetPreviousCard(C5D).get());
	}

	@Test
	void testContains() {
		assertFalse(aTableau.contains(CAC, TableauPile.FIRST));
		aTableau.push(CAC, TableauPile.FIRST);
		assertTrue(aTableau.contains(CAC, TableauPile.FIRST));
		aTableau.push(C3H, TableauPile.FIRST);
		aTableau.push(C5D, TableauPile.FIRST);
		assertTrue(aTableau.contains(C5D, TableauPile.FIRST));
	}

	@Test
	void testGetSequence() {
		aTableau.push(C5D, TableauPile.SECOND);
		CardStack sequence = aTableau.getSequence(C5D, TableauPile.SECOND);
		assertEquals(1, sequence.size());
		assertEquals(C5D, sequence.peekBottom());
		aTableau.push(C4C, TableauPile.SECOND);
		sequence = aTableau.getSequence(C5D, TableauPile.SECOND);
		assertEquals(2, sequence.size());
		assertEquals(C5D, sequence.peekBottom());
		assertEquals(C4C, peekAtIndex(sequence, 1));
		sequence = aTableau.getSequence(C4C, TableauPile.SECOND);
		assertEquals(1, sequence.size());
		assertEquals(C4C, sequence.peekBottom());
	}

	

	@Test
	void testContains2() {
		Deck deck = new Deck();
		aTableau.initialize(deck);
		while (!deck.isEmpty()) {
			assertFalse(aTableau.contains(deck.draw()));
		}
		for (TableauPile index : TableauPile.values()) {
			for (Card card : aTableau.getPile(index)) {
				assertTrue(aTableau.contains(card));
			}
		}
	}

	@Test
	void testVisibility() {
		Deck deck = new Deck();
		aTableau.initialize(deck);
		for (TableauPile index : TableauPile.values()) {
			CardStack cards = aTableau.getPile(index);
			for (int i = 0; i < cards.size() - 1; i++) {
				assertFalse(aTableau.isVisible(peekAtIndex(cards, i)));
			}
			assertTrue(aTableau.isVisible(cards.peekTop()));
		}
		aTableau.push(deck.draw(), TableauPile.SECOND);
		CardStack stack = aTableau.getPile(TableauPile.SECOND);
		assertFalse(aTableau.isVisible(peekAtIndex(stack,0)));
		assertTrue(aTableau.isVisible(peekAtIndex(stack, 1)));
		assertTrue(aTableau.isVisible(peekAtIndex(stack, 2)));
	}

	@Test
	void testShowHideTop() {
		aTableau.initialize(new Deck());
		CardStack one = aTableau.getPile(TableauPile.FIRST);
		CardStack two = aTableau.getPile(TableauPile.SECOND);
		assertTrue(aTableau.isVisible(one.peekBottom()));
		aTableau.hideTop(TableauPile.FIRST);
		assertFalse(aTableau.isVisible(one.peekBottom()));
		aTableau.showTop(TableauPile.FIRST);
		assertTrue(aTableau.isVisible(one.peekBottom()));

		assertTrue(aTableau.isVisible(peekAtIndex(two, 1)));
		aTableau.hideTop(TableauPile.SECOND);
		assertFalse(aTableau.isVisible(peekAtIndex(two, 1)));
		aTableau.showTop(TableauPile.SECOND);
		assertTrue(aTableau.isVisible(peekAtIndex(two, 1)));
	}
}
