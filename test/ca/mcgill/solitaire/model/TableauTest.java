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
import static ca.mcgill.solitaire.testutils.Cards.C2D;
import static ca.mcgill.solitaire.testutils.Cards.C2H;
import static ca.mcgill.solitaire.testutils.Cards.C2S;
import static ca.mcgill.solitaire.testutils.Cards.C3C;
import static ca.mcgill.solitaire.testutils.Cards.C3D;
import static ca.mcgill.solitaire.testutils.Cards.C3H;
import static ca.mcgill.solitaire.testutils.Cards.C3S;
import static ca.mcgill.solitaire.testutils.Cards.C4C;
import static ca.mcgill.solitaire.testutils.Cards.C4H;
import static ca.mcgill.solitaire.testutils.Cards.C4S;
import static ca.mcgill.solitaire.testutils.Cards.C5D;
import static ca.mcgill.solitaire.testutils.Cards.C5H;
import static ca.mcgill.solitaire.testutils.Cards.C5S;
import static ca.mcgill.solitaire.testutils.Cards.C6H;
import static ca.mcgill.solitaire.testutils.Cards.C6S;
import static ca.mcgill.solitaire.testutils.Cards.C7H;
import static ca.mcgill.solitaire.testutils.Cards.C7S;
import static ca.mcgill.solitaire.testutils.Cards.C8H;
import static ca.mcgill.solitaire.testutils.Cards.C8S;
import static ca.mcgill.solitaire.testutils.Cards.C9H;
import static ca.mcgill.solitaire.testutils.Cards.C9S;
import static ca.mcgill.solitaire.testutils.Cards.CAC;
import static ca.mcgill.solitaire.testutils.Cards.CAD;
import static ca.mcgill.solitaire.testutils.Cards.CAH;
import static ca.mcgill.solitaire.testutils.Cards.CAS;
import static ca.mcgill.solitaire.testutils.Cards.CJC;
import static ca.mcgill.solitaire.testutils.Cards.CJH;
import static ca.mcgill.solitaire.testutils.Cards.CJS;
import static ca.mcgill.solitaire.testutils.Cards.CKC;
import static ca.mcgill.solitaire.testutils.Cards.CKD;
import static ca.mcgill.solitaire.testutils.Cards.CKH;
import static ca.mcgill.solitaire.testutils.Cards.CKS;
import static ca.mcgill.solitaire.testutils.Cards.CQC;
import static ca.mcgill.solitaire.testutils.Cards.CQD;
import static ca.mcgill.solitaire.testutils.Cards.CQH;
import static ca.mcgill.solitaire.testutils.Cards.CQS;
import static ca.mcgill.solitaire.testutils.Cards.CTH;
import static ca.mcgill.solitaire.testutils.Cards.CTS;
import static ca.mcgill.solitaire.testutils.Utils.assertCardStackHas;
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
	
	@Test
	void testGetSubpile_Copy() {
		aTableau.push(CAC, TableauPile.FIRST);
		aTableau.push(C2C, TableauPile.FIRST);
		CardStack pile = aTableau.getSubpile(CAC);
		CardStack pile2 = aTableau.getSubpile(CAC);
		assertNotSame(pile, pile2);
	}
	
	@Test
	void testGetSubpile_SingleCard() {
		aTableau.push(CAC, TableauPile.FIRST);
		CardStack pile = aTableau.getSubpile(CAC);
		assertCardStackHas(pile, CAC);
	}
	
	@Test
	void testGetSubpile_AllCards() {
		aTableau.push(CKC, TableauPile.FIRST);
		aTableau.push(CQD, TableauPile.FIRST);
		aTableau.push(CJC, TableauPile.FIRST);
		CardStack pile = aTableau.getSubpile(CKC);
		assertCardStackHas(pile, CKC, CQD, CJC);
	}
	
	@Test
	void testGetSubpile_SomeCards() {
		aTableau.push(CKC, TableauPile.FIRST);
		aTableau.push(CQD, TableauPile.FIRST);
		aTableau.push(CJC, TableauPile.FIRST);
		CardStack pile = aTableau.getSubpile(CQD);
		assertCardStackHas(pile, CQD, CJC);
	}
	
	@ParameterizedTest
	@EnumSource(TableauPile.class)
	void testShowTop(TableauPile pPile) {
		aTableau.push(C2C, pPile);
		aTableau.hideTop(pPile);
		aTableau.push(CAD, pPile);
		aTableau.hideTop(pPile);
		aTableau.showTop(pPile);
		assertTrue(aTableau.isVisible(CAD));
	}
	
	@ParameterizedTest
	@EnumSource(TableauPile.class)
	void testHideTop(TableauPile pPile) {
		aTableau.push(C2C, pPile);
		aTableau.push(CAD, pPile);
		aTableau.hideTop(pPile);
		assertFalse(aTableau.isVisible(CAD));
	}
	
	@Test
	void testIsLowestVisible_NotVisible() {
		aTableau.push(CAS, TableauPile.FIRST);
		aTableau.hideTop(TableauPile.FIRST);
		assertFalse(aTableau.isLowestVisible(CAS));
	}
	
	@Test
	void testIsLowestVisible_IsOnlyCard() {
		aTableau.push(CAS, TableauPile.FIRST);
		assertTrue(aTableau.isLowestVisible(CAS));
	}
	
	@Test
	void testIsLowestVisible_IsBottomCard() {
		aTableau.push(C2S, TableauPile.FIRST);
		aTableau.push(CAD, TableauPile.FIRST);
		assertTrue(aTableau.isLowestVisible(C2S));
	}
	
	@Test
	void testIsLowestVisible_AtTop_True() {
		aTableau.push(C2S, TableauPile.FIRST);
		aTableau.hideTop(TableauPile.FIRST);
		aTableau.push(CAD, TableauPile.FIRST);
		assertTrue(aTableau.isLowestVisible(CAD));
	}
	
	@Test
	void testIsLowestVisible_AtTop_False() {
		aTableau.push(C2S, TableauPile.FIRST);
		aTableau.push(CAD, TableauPile.FIRST);
		assertFalse(aTableau.isLowestVisible(CAD));
	}
	
	@Test
	void testIsLowestVisible_InMiddle_True() {
		aTableau.push(C3D, TableauPile.FIRST);
		aTableau.hideTop(TableauPile.FIRST);
		aTableau.push(C2S, TableauPile.FIRST);
		aTableau.push(CAD, TableauPile.FIRST);
		assertTrue(aTableau.isLowestVisible(C2S));
	}
	
	@Test
	void testIsLowestVisible_InMiddle_False() {
		aTableau.push(C3D, TableauPile.FIRST);
		aTableau.push(C2S, TableauPile.FIRST);
		aTableau.push(CAD, TableauPile.FIRST);
		assertFalse(aTableau.isLowestVisible(C2S));
	}
	
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
	
	// ***** BELOW ARE OLD TESTS ***** //
	
	
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
}
