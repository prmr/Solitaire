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

import static ca.mcgill.solitaire.testutils.Utils.peekAtIndex;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import ca.mcgill.solitaire.cards.Card;
import ca.mcgill.solitaire.cards.CardStack;
import ca.mcgill.solitaire.cards.Deck;
import ca.mcgill.solitaire.cards.Rank;
import ca.mcgill.solitaire.cards.Suit;

public class TableauTest {
	private Tableau aTableau = new Tableau();
	private static final Card CKC = Card.get(Rank.KING, Suit.CLUBS);
	private static final Card CAC = Card.get(Rank.ACE, Suit.CLUBS);
	private static final Card C5D = Card.get(Rank.FIVE, Suit.DIAMONDS);
	private static final Card C4D = Card.get(Rank.FOUR, Suit.DIAMONDS);
	private static final Card C4C = Card.get(Rank.FOUR, Suit.CLUBS);
	private static final Card C4S = Card.get(Rank.FOUR, Suit.SPADES);
	private static final Card C4H = Card.get(Rank.FOUR, Suit.HEARTS);
	private static final Card C3H = Card.get(Rank.THREE, Suit.HEARTS);

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
	void testInitialize() {
		for (TableauPile index : TableauPile.values()) {
			assertEquals(0, aTableau.getPile(index).size());
		}
		Deck deck = new Deck();
		aTableau.initialize(deck);
		assertEquals(1, aTableau.getPile(TableauPile.FIRST).size());
		assertEquals(2, aTableau.getPile(TableauPile.SECOND).size());
		assertEquals(3, aTableau.getPile(TableauPile.THIRD).size());
		assertEquals(4, aTableau.getPile(TableauPile.FOURTH).size());
		assertEquals(5, aTableau.getPile(TableauPile.FIFTH).size());
		assertEquals(6, aTableau.getPile(TableauPile.SIXTH).size());
		assertEquals(7, aTableau.getPile(TableauPile.SEVENTH).size());
		deck.shuffle();
		aTableau.initialize(deck);
		assertEquals(1, aTableau.getPile(TableauPile.FIRST).size());
		assertEquals(2, aTableau.getPile(TableauPile.SECOND).size());
		assertEquals(3, aTableau.getPile(TableauPile.THIRD).size());
		assertEquals(4, aTableau.getPile(TableauPile.FOURTH).size());
		assertEquals(5, aTableau.getPile(TableauPile.FIFTH).size());
		assertEquals(6, aTableau.getPile(TableauPile.SIXTH).size());
		assertEquals(7, aTableau.getPile(TableauPile.SEVENTH).size());
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
	void testCanMoveTo() {
		assertFalse(aTableau.canMoveTo(CAC, TableauPile.FIRST));
		assertTrue(aTableau.canMoveTo(CKC, TableauPile.FIRST));
		aTableau.push(C5D, TableauPile.FIRST);
		assertFalse(aTableau.canMoveTo(CAC, TableauPile.FIRST));
		assertFalse(aTableau.canMoveTo(C4D, TableauPile.FIRST));
		assertFalse(aTableau.canMoveTo(C4H, TableauPile.FIRST));
		assertTrue(aTableau.canMoveTo(C4C, TableauPile.FIRST));
		assertTrue(aTableau.canMoveTo(C4S, TableauPile.FIRST));
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
	void testMoveWithin() {
		Deck deck = new Deck();
		aTableau.initialize(deck);
		CardStack stack2a = aTableau.getPile(TableauPile.SECOND);
		CardStack stack4a = aTableau.getPile(TableauPile.FOURTH);
		aTableau.moveWithin(peekAtIndex(stack2a, 1), TableauPile.SECOND, TableauPile.FOURTH);
		CardStack stack2b = aTableau.getPile(TableauPile.SECOND);
		CardStack stack4b = aTableau.getPile(TableauPile.FOURTH);
		assertEquals(1, stack2b.size());
		assertEquals(5, stack4b.size());
		assertEquals(stack2a.peekBottom(), stack2b.peekBottom());
		assertEquals(stack4a.peekBottom(), stack4b.peekBottom());
		assertEquals(peekAtIndex(stack4a, 1), peekAtIndex(stack4b, 1));
		assertEquals(peekAtIndex(stack4a, 2), peekAtIndex(stack4b, 2));
		assertEquals(peekAtIndex(stack4a, 3), peekAtIndex(stack4b, 3));
		assertEquals(peekAtIndex(stack2a, 1), peekAtIndex(stack4b, 4));
		assertTrue(aTableau.isVisible(peekAtIndex(stack4b, 4)));
		aTableau.moveWithin(peekAtIndex(stack4b, 3), TableauPile.FOURTH, TableauPile.SECOND);
		CardStack stack2c = aTableau.getPile(TableauPile.SECOND);
		assertEquals(3, stack2c.size());
		assertEquals(stack2a.peekBottom(), stack2c.peekBottom());
		assertEquals(peekAtIndex(stack4b, 3), peekAtIndex(stack2c, 1));
		assertEquals(peekAtIndex(stack4b, 4), peekAtIndex(stack2c, 2));
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
