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
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import ca.mcgill.solitaire.auto.GreedyPlayingStrategy;
import ca.mcgill.solitaire.cards.CardStack;
import ca.mcgill.solitaire.testutils.StubDeck;

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
		deckField.set(aModel, new StubDeck());
		aModel.reset();
	}
	
	@Test
	void testGetScore_Zero() {
		assertEquals(0, aModel.getScore());
	}
	
	@Test
	void testGetScore_NonZero() {
		aModel.getCardMove(CQD, TableauPile.FIRST).perform();
		aModel.getCardMove(CJS, TableauPile.FIRST).perform();
		aModel.getCardMove(CQS, TableauPile.SEVENTH).perform();
		aModel.getCardMove(CKD, TableauPile.SECOND).perform();
		aModel.getCardMove(CAH, FoundationPile.FIRST).perform();
		assertEquals(1, aModel.getScore());
		aModel.getCardMove(C2H, FoundationPile.FIRST).perform();
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
	void testDiscard() {
		aModel.getDiscardMove().perform();
		assertSame(CJD, aModel.peekDiscardPile());
		aModel.getDiscardMove().perform();
		assertSame(CTD, aModel.peekDiscardPile());
	}

	@Test
	void testGetTableauPile_First() {
		CardStack pile = aModel.getTableauPile(TableauPile.FIRST);
		assertEquals(1, pile.size());
		assertSame(CKS, pile.peekTop());
	}
	
	@Test
	void testGetTableauPile_Second() {
		CardStack pile = aModel.getTableauPile(TableauPile.SECOND);
		assertEquals(2, pile.size());
		assertSame(CJS, pile.pop());
		assertSame(CQS, pile.pop());
	}
	
	@Test
	void testGetTableauPile_Third() {
		CardStack pile = aModel.getTableauPile(TableauPile.THIRD);
		assertEquals(3, pile.size());
		assertSame(C8S, pile.pop());
		assertSame(C9S, pile.pop());
		assertSame(CTS, pile.pop());
	}
	
	@Test
	void testGetTableauPile_Fourth() {
		CardStack pile = aModel.getTableauPile(TableauPile.FOURTH);
		assertEquals(4, pile.size());
		assertSame(C4S, pile.pop());
		assertSame(C5S, pile.pop());
		assertSame(C6S, pile.pop());
		assertSame(C7S, pile.pop());
	}
	
	@Test
	void testGetTableauPile_Fifth() {
		CardStack pile = aModel.getTableauPile(TableauPile.FIFTH);
		assertEquals(5, pile.size());
		assertSame(CQH, pile.pop());
		assertSame(CKH, pile.pop());
		assertSame(CAS, pile.pop());
		assertSame(C2S, pile.pop());
		assertSame(C3S, pile.pop());
	}
	
	@Test
	void testGetTableauPile_Sixth() {
		CardStack pile = aModel.getTableauPile(TableauPile.SIXTH);
		assertEquals(6, pile.size());
		assertSame(C6H, pile.pop());
		assertSame(C7H, pile.pop());
		assertSame(C8H, pile.pop());
		assertSame(C9H, pile.pop());
		assertSame(CTH, pile.pop());
		assertSame(CJH, pile.pop());
	}
	
	@Test
	void testGetTableauPile_Seventh() {
		CardStack pile = aModel.getTableauPile(TableauPile.SEVENTH);
		assertEquals(7, pile.size());
		assertSame(CQD, pile.pop());
		assertSame(CKD, pile.pop());
		assertSame(CAH, pile.pop());
		assertSame(C2H, pile.pop());
		assertSame(C3H, pile.pop());
		assertSame(C4H, pile.pop());
		assertSame(C5H, pile.pop());
	}
	
	@Test
	void testGetSubStack_SingleCard() {
		CardStack stack = aModel.getSubpile(CKS);
		assertEquals(1, stack.size());
		assertSame(CKS, stack.peekTop());
	}
	
	@Test
	void testGetSubStack_AllCards() {
		CardStack stack = aModel.getSubpile(CTS);
		assertEquals(3, stack.size());
		assertSame(C8S, stack.pop());
		assertSame(C9S, stack.pop());
		assertSame(CTS, stack.pop());
	}
	
	@Test
	void testGetSubStack_TopTwo() {
		CardStack stack = aModel.getSubpile(C9S);
		assertEquals(2, stack.size());
		assertSame(C8S, stack.pop());
		assertSame(C9S, stack.pop());
	}
	
	@Test
	void testGetSubStack_TopOnly() {
		CardStack stack = aModel.getSubpile(C8S);
		assertEquals(1, stack.size());
		assertSame(C8S, stack.pop());
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
