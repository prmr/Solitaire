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

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import ca.mcgill.solitaire.auto.GreedyPlayingStrategy;
import ca.mcgill.solitaire.cards.Card;
import ca.mcgill.solitaire.cards.CardStack;
import ca.mcgill.solitaire.cards.Rank;
import ca.mcgill.solitaire.cards.Suit;

/**
 * Test class for the GameModel. The constructor uses reflection to inject
 * a special deck that is not shuffled into the GameModel, so that the order
 * of cards can be predicted and used as oracle. JUnit5 reinstantiates the 
 * test class before each test, so the order of cards in the deck can always be
 * predicted. The cards are ordered by suit, then rank. See the Suit and 
 * Rank enumerated type for the order.
 * 
 * Because 28 cards are used to initialize the tableau from the deck, after
 * the GameModel is initialized, the card at the top of the deck is the 29th
 * card, so the Jack of Diamonds.
 * 
 * Tableau Piles (bottom -> top):
 * 1: KS
 * 2: QS JS
 * 3: TS 9S 8S
 * 4: 7S 6S 5S 4S
 * 5: 3S 2S AS KH QH
 * 6: JH TH 9H 8H 7H 6H 
 * 7: 5H 4H 3H 2H AH KD QD
 */
public class GameModelTest {
	
	private final GameModel aModel = new GameModel(new GreedyPlayingStrategy());

	/**
	 * Initialize the test class so that the instance of GameModel
	 * stored in aModel is a special subclass TestDeck.
	 */
	GameModelTest() throws ReflectiveOperationException {
		Field deckField = GameModel.class.getDeclaredField("aDeck");
		deckField.setAccessible(true);
		deckField.set(aModel, new DeckTest());
		aModel.reset();
	}
	
	@Test
	void testGetScore_Zero() {
		assertEquals(0, aModel.getScore());
	}
	
	@Test
	void testGetScore_NonZero() {
		aModel.getCardMove(Card.get(Rank.QUEEN, Suit.DIAMONDS), TableauPile.FIRST).perform();
		aModel.getCardMove(Card.get(Rank.JACK, Suit.SPADES), TableauPile.FIRST).perform();
		aModel.getCardMove(Card.get(Rank.QUEEN, Suit.SPADES), TableauPile.SEVENTH).perform();
		aModel.getCardMove(Card.get(Rank.KING, Suit.DIAMONDS), TableauPile.SECOND).perform();
		aModel.getCardMove(Card.get(Rank.ACE, Suit.HEARTS), FoundationPile.FIRST).perform();
		assertEquals(1, aModel.getScore());
		aModel.getCardMove(Card.get(Rank.TWO, Suit.HEARTS), FoundationPile.FIRST).perform();
		assertEquals(2, aModel.getScore());
	}
	
	@Test
	void testListeners_OneListener() {
		final boolean[] test = { false };

		// The statement below can be replaced with: 
		// aModel.addListener(() -> test[0] = true);
		aModel.addListener(new GameModelListener() {
			@Override
			public void gameStateChanged() {
				test[0] = true;
			}
		});
		aModel.getDiscardMove().perform();
		assertTrue(test[0]);
	}
	
	@Test
	void testListeners_None() {
		// There is no assert here. The test
		// verifies that there is no exception when
		// there is no listener to notify
		aModel.getDiscardMove().perform();
	}

	/*
	 * Verifies that the cards at the top are returned by asserting
	 * the card at the top of the discard pile.
	 */
	@Test
	void testDiscard_Discard() {
		aModel.getDiscardMove().perform();
		assertSame(Card.get(Rank.JACK, Suit.DIAMONDS), aModel.peekDiscardPile());
		aModel.getDiscardMove().perform();
		assertSame(Card.get(Rank.TEN, Suit.DIAMONDS), aModel.peekDiscardPile());
	}

	@Test
	void testGetTableauPile_First() {
		CardStack pile = aModel.getTableauPile(TableauPile.FIRST);
		assertEquals(1, pile.size());
		assertSame(Card.get(Rank.KING, Suit.SPADES), pile.peekTop());
	}
	
	@Test
	void testGetTableauPile_Second() {
		CardStack pile = aModel.getTableauPile(TableauPile.SECOND);
		assertEquals(2, pile.size());
		assertSame(Card.get(Rank.JACK, Suit.SPADES), pile.pop());
		assertSame(Card.get(Rank.QUEEN, Suit.SPADES), pile.pop());
	}
	
	@Test
	void testGetTableauPile_Third() {
		CardStack pile = aModel.getTableauPile(TableauPile.THIRD);
		assertEquals(3, pile.size());
		assertSame(Card.get(Rank.EIGHT, Suit.SPADES), pile.pop());
		assertSame(Card.get(Rank.NINE, Suit.SPADES), pile.pop());
		assertSame(Card.get(Rank.TEN, Suit.SPADES), pile.pop());
	}
	
	@Test
	void testGetTableauPile_Fourth() {
		CardStack pile = aModel.getTableauPile(TableauPile.FOURTH);
		assertEquals(4, pile.size());
		assertSame(Card.get(Rank.FOUR, Suit.SPADES), pile.pop());
		assertSame(Card.get(Rank.FIVE, Suit.SPADES), pile.pop());
		assertSame(Card.get(Rank.SIX, Suit.SPADES), pile.pop());
		assertSame(Card.get(Rank.SEVEN, Suit.SPADES), pile.pop());
	}
	
	@Test
	void testGetTableauPile_Fifth() {
		CardStack pile = aModel.getTableauPile(TableauPile.FIFTH);
		assertEquals(5, pile.size());
		assertSame(Card.get(Rank.QUEEN, Suit.HEARTS), pile.pop());
		assertSame(Card.get(Rank.KING, Suit.HEARTS), pile.pop());
		assertSame(Card.get(Rank.ACE, Suit.SPADES), pile.pop());
		assertSame(Card.get(Rank.TWO, Suit.SPADES), pile.pop());
		assertSame(Card.get(Rank.THREE, Suit.SPADES), pile.pop());
	}
	
	@Test
	void testGetTableauPile_Sixth() {
		CardStack pile = aModel.getTableauPile(TableauPile.SIXTH);
		assertEquals(6, pile.size());
		assertSame(Card.get(Rank.SIX, Suit.HEARTS), pile.pop());
		assertSame(Card.get(Rank.SEVEN, Suit.HEARTS), pile.pop());
		assertSame(Card.get(Rank.EIGHT, Suit.HEARTS), pile.pop());
		assertSame(Card.get(Rank.NINE, Suit.HEARTS), pile.pop());
		assertSame(Card.get(Rank.TEN, Suit.HEARTS), pile.pop());
		assertSame(Card.get(Rank.JACK, Suit.HEARTS), pile.pop());
	}
	
	@Test
	void testGetTableauPile_Seventh() {
		CardStack pile = aModel.getTableauPile(TableauPile.SEVENTH);
		assertEquals(7, pile.size());
		assertSame(Card.get(Rank.QUEEN, Suit.DIAMONDS), pile.pop());
		assertSame(Card.get(Rank.KING, Suit.DIAMONDS), pile.pop());
		assertSame(Card.get(Rank.ACE, Suit.HEARTS), pile.pop());
		assertSame(Card.get(Rank.TWO, Suit.HEARTS), pile.pop());
		assertSame(Card.get(Rank.THREE, Suit.HEARTS), pile.pop());
		assertSame(Card.get(Rank.FOUR, Suit.HEARTS), pile.pop());
		assertSame(Card.get(Rank.FIVE, Suit.HEARTS), pile.pop());
	}
	
	@Test
	void testGetSubStack_SingleCard() {
		CardStack stack = aModel.getSubStack(Card.get(Rank.KING, Suit.SPADES), TableauPile.FIRST);
		assertEquals(1, stack.size());
		assertSame(Card.get(Rank.KING, Suit.SPADES), stack.peekTop());
	}
	
	@Test
	void testGetSubStack_AllCards() {
		CardStack stack = aModel.getSubStack(Card.get(Rank.TEN, Suit.SPADES), TableauPile.THIRD);
		assertEquals(3, stack.size());
		assertSame(Card.get(Rank.EIGHT, Suit.SPADES), stack.pop());
		assertSame(Card.get(Rank.NINE, Suit.SPADES), stack.pop());
		assertSame(Card.get(Rank.TEN, Suit.SPADES), stack.pop());
	}
	
	@Test
	void testGetSubStack_TopTwo() {
		CardStack stack = aModel.getSubStack(Card.get(Rank.NINE, Suit.SPADES), TableauPile.THIRD);
		assertEquals(2, stack.size());
		assertSame(Card.get(Rank.EIGHT, Suit.SPADES), stack.pop());
		assertSame(Card.get(Rank.NINE, Suit.SPADES), stack.pop());
	}
	
	@Test
	void testGetSubStack_TopOnly() {
		CardStack stack = aModel.getSubStack(Card.get(Rank.EIGHT, Suit.SPADES), TableauPile.THIRD);
		assertEquals(1, stack.size());
		assertSame(Card.get(Rank.EIGHT, Suit.SPADES), stack.pop());
	}
	
	@Test
	void testNullMove() {
		Move nullMove = aModel.getNullMove();
		assertTrue(nullMove.isNull());
		// Only really tests that nothing crashes
		nullMove.perform();
		nullMove.undo();
	}
}
