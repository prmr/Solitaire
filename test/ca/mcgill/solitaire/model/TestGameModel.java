/*******************************************************************************
 * Solitaire
 * 
 * Copyright (C) 2016-2024 by Martin P. Robillard
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import ca.mcgill.solitaire.auto.GreedyPlayingStrategy;
import ca.mcgill.solitaire.cards.Card;
import ca.mcgill.solitaire.cards.CardStack;
import ca.mcgill.solitaire.cards.Rank;
import ca.mcgill.solitaire.cards.Suit;

public class TestGameModel {
	private final GameModel aModel = new GameModel(new GreedyPlayingStrategy());

	TestGameModel() throws ReflectiveOperationException {
		Field deckField = GameModel.class.getDeclaredField("aDeck");
		deckField.setAccessible(true);
		deckField.set(aModel, new TestDeck());
		aModel.reset();
	}

	@Test
	void testListeners() {
		final boolean[] test = { false };
		class ListenerStub implements GameModelListener {
			@Override
			public void gameStateChanged() {
				test[0] = true;
			}
		}
		;
		// Test no crash
		aModel.getDiscardMove().perform();
		aModel.addListener(new ListenerStub());
		aModel.getDiscardMove().perform();
		assertTrue(test[0]);
	}

	@Test
	void testDiscard() {
		assertTrue(aModel.isDiscardPileEmpty());
		assertFalse(aModel.isDeckEmpty());// 3 of hearts
		for (int i = 0; i < 24; i++) {
			assertFalse(aModel.isDeckEmpty());
			aModel.getDiscardMove().perform();
			// Test a few cards
			if (i == 0) {
				assertEquals(Card.get(Rank.JACK, Suit.DIAMONDS), aModel.peekDiscardPile());
			}
			if (i == 1) {
				assertEquals(Card.get(Rank.TEN, Suit.DIAMONDS), aModel.peekDiscardPile());
			}
			assertFalse(aModel.isDiscardPileEmpty());
		}
		assertTrue(aModel.isDeckEmpty());
	}

	@Test
	void testGetStack() {
		GameModel model = aModel;
		CardStack stack = model.getTableauPile(TableauPile.FIRST);
		assertEquals(Card.get(Rank.KING, Suit.SPADES), stack.peek(0));
		assertEquals(1, stack.size());
		// Test that the method returns a clones
		CardStack stack2 = model.getTableauPile(TableauPile.FIRST);
		assertNotSame(stack, stack2);

		stack = model.getTableauPile(TableauPile.SECOND);
		assertEquals(2, stack.size());
		assertEquals(Card.get(Rank.QUEEN, Suit.SPADES), stack.peek(0));
		assertEquals(Card.get(Rank.JACK, Suit.SPADES), stack.peek(1));
	}

	@Test
	void testGetSubStack() {
		GameModel model = aModel;
		CardStack stack = model.getSubStack(Card.get(Rank.KING, Suit.SPADES), TableauPile.FIRST);
		assertEquals(Card.get(Rank.KING, Suit.SPADES), stack.peek(0));
		assertEquals(1, stack.size());
		CardStack stack2 = model.getSubStack(Card.get(Rank.KING, Suit.SPADES), TableauPile.FIRST);
		assertNotSame(stack, stack2);

		stack = model.getSubStack(Card.get(Rank.KING, Suit.SPADES), TableauPile.FIRST);
		assertEquals(Card.get(Rank.KING, Suit.SPADES), stack.peek(0));
		assertEquals(1, stack.size());

		stack = model.getSubStack(Card.get(Rank.TEN, Suit.SPADES), TableauPile.THIRD);
		assertEquals(3, stack.size());
		stack = model.getSubStack(Card.get(Rank.NINE, Suit.SPADES), TableauPile.THIRD);
		assertEquals(2, stack.size());
		stack = model.getSubStack(Card.get(Rank.EIGHT, Suit.SPADES), TableauPile.THIRD);
		assertEquals(1, stack.size());
	}

	@Test
	void testMoves() {
		GameModel model = aModel;
		assertTrue(model.isFoundationPileEmpty(FoundationPile.FIRST));
		assertFalse(model.isLegalMove(Card.get(Rank.THREE, Suit.CLUBS), FoundationPile.SECOND));
		assertFalse(model.isLegalMove(Card.get(Rank.THREE, Suit.CLUBS), FoundationPile.FIRST));
		assertFalse(model.isLegalMove(Card.get(Rank.TWO, Suit.CLUBS), FoundationPile.FIRST));
		assertTrue(model.isLegalMove(Card.get(Rank.ACE, Suit.CLUBS), FoundationPile.FIRST));
		model.getDiscardMove().perform(); // Jack of diamond
		model.getDiscardMove().perform(); // Ten of diamond
		assertTrue(model.isLegalMove(model.peekDiscardPile(), TableauPile.SECOND));
		model.getCardMove(model.peekDiscardPile(), TableauPile.SECOND).perform();
		model.getDiscardMove().perform();
		assertFalse(model.isLegalMove(model.peekDiscardPile(), TableauPile.SECOND));
		model.getDiscardMove().perform();
		assertEquals(Card.get(Rank.EIGHT, Suit.DIAMONDS), model.peekDiscardPile());
		model.getDiscardMove().perform();
		assertTrue(model.isLegalMove(model.peekDiscardPile(), TableauPile.THIRD));
		model.getCardMove(model.peekDiscardPile(), TableauPile.THIRD).perform();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		assertEquals(Card.get(Rank.FIVE, Suit.DIAMONDS), model.peekDiscardPile());
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		assertEquals(Card.get(Rank.ACE, Suit.DIAMONDS), model.peekDiscardPile());
		assertTrue(model.isLegalMove(model.peekDiscardPile(), FoundationPile.SECOND));
		model.getCardMove(model.peekDiscardPile(), FoundationPile.SECOND).perform();
		assertEquals(Card.get(Rank.ACE, Suit.DIAMONDS), model.peekSuitStack(FoundationPile.SECOND));
		assertEquals(Card.get(Rank.TWO, Suit.DIAMONDS), model.peekDiscardPile());
		model.getCardMove(model.peekDiscardPile(), FoundationPile.SECOND).perform();
		assertEquals(Card.get(Rank.TWO, Suit.DIAMONDS), model.peekSuitStack(FoundationPile.SECOND));
		model.getCardMove(model.peekDiscardPile(), FoundationPile.SECOND).perform();
		model.getCardMove(model.peekDiscardPile(), FoundationPile.SECOND).perform();
		model.getCardMove(model.peekDiscardPile(), FoundationPile.SECOND).perform();
		model.getCardMove(model.peekDiscardPile(), FoundationPile.SECOND).perform();
		// 8th of diamond is on top of the discard pile
		assertFalse(model.isLegalMove(model.peekDiscardPile(), FoundationPile.SECOND));
		model.getCardMove(Card.get(Rank.SEVEN, Suit.DIAMONDS), FoundationPile.SECOND).perform();
		// move the 7th back to the working stack
		model.getCardMove(Card.get(Rank.SEVEN, Suit.DIAMONDS), TableauPile.THIRD).perform();
	}

	@Test
	void testMoves2() {
		GameModel model = aModel;
		model.getDiscardMove().perform();
		model.getDiscardMove().perform(); // 10D on discard pile
		model.getCardMove(model.peekDiscardPile(), TableauPile.SECOND).perform();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform(); // 9C on discard pile
		assertEquals(Card.get(Rank.NINE, Suit.CLUBS), model.peekDiscardPile());
		model.getCardMove(model.peekDiscardPile(), TableauPile.SECOND).perform();
		// move three card sequence to pile 7
		CardStack stack = model.getSubStack(Card.get(Rank.JACK, Suit.SPADES), TableauPile.SECOND);
		assertEquals(3, stack.size());
		assertEquals(Card.get(Rank.JACK, Suit.SPADES), stack.peek(0));
		assertEquals(Card.get(Rank.TEN, Suit.DIAMONDS), stack.peek(1));
		assertEquals(Card.get(Rank.NINE, Suit.CLUBS), stack.peek(2));
		model.getCardMove(stack.peek(0), TableauPile.SEVENTH).perform();
		assertEquals(1, model.getTableauPile(TableauPile.SECOND).size());
		stack = model.getSubStack(Card.get(Rank.JACK, Suit.SPADES), TableauPile.SEVENTH);
		assertEquals(10, model.getTableauPile(TableauPile.SEVENTH).size());
		model.getCardMove(model.getSubStack(Card.get(Rank.QUEEN, Suit.DIAMONDS), TableauPile.SEVENTH).peek(0),
				TableauPile.FIRST).perform();
		CardStack stack2 = model.getTableauPile(TableauPile.FIRST);
		assertEquals(5, stack2.size());
	}

	@Test
	void testMoves3() {
		GameModel model = aModel;
		for (int i = 0; i < 14; i++) {
			model.getDiscardMove().perform();
		}
		assertEquals(Card.get(Rank.JACK, Suit.CLUBS), model.peekDiscardPile());
		model.getCardMove(model.peekDiscardPile(), TableauPile.FIFTH).perform();
		assertEquals(6, model.getTableauPile(TableauPile.FIFTH).size());
		model.getCardMove(model.getSubStack(Card.get(Rank.JACK, Suit.CLUBS), TableauPile.FIFTH).peek(0),
				TableauPile.SEVENTH).perform();
		assertEquals(5, model.getTableauPile(TableauPile.FIFTH).size());
		assertEquals(8, model.getTableauPile(TableauPile.SEVENTH).size());
	}

	@Test
	void testNullMove() {
		GameModel model = aModel;
		Move nullMove = model.getNullMove();
		assertTrue(nullMove.isNull());
		// Only really tests that nothing crashes
		nullMove.perform();
		nullMove.undo();
	}

	@Test
	void testUndo1() {
		// Tests undoing discard moves
		GameModel model = aModel;
		assertTrue(model.isDiscardPileEmpty());
		assertFalse(model.canUndo());
		Move discard = model.getDiscardMove();
		assertFalse(discard.isNull());
		discard.perform();
		assertTrue(model.canUndo());
		Card card = model.peekDiscardPile();
		model.undoLast();
		assertFalse(model.canUndo());
		assertTrue(model.isDiscardPileEmpty());
		model.getDiscardMove().perform();
		assertEquals(card, model.peekDiscardPile());
	}

	@Test
	void testGetScore() {
		GameModel model = aModel;
		assertEquals(0, model.getScore());
		assertFalse(model.isCompleted());
	}
}
