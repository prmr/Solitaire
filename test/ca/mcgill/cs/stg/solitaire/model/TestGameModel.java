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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.cards.Rank;
import ca.mcgill.cs.stg.solitaire.cards.Suit;
import ca.mcgill.cs.stg.solitaire.model.GameModel.StackIndex;
import ca.mcgill.cs.stg.solitaire.model.GameModel.SuitStackIndex;

public class TestGameModel
{
	@Before
	public void setup() throws Exception
	{
		Field deckField = GameModel.class.getDeclaredField("aDeck");
		deckField.setAccessible(true);
		deckField.set(GameModel.instance(), new TestDeck());
		GameModel.instance().reset();
	}
	
	@Test
	public void testListeners()
	{
		final boolean[] test = {false};
		class ListenerStub implements GameModelListener
		{
			@Override
			public void gameStateChanged()
			{
				test[0] = true;
			}
		};
		// Test no crash
		GameModel.instance().getDiscardMove().perform();
		GameModel.instance().addListener(new ListenerStub());
		GameModel.instance().getDiscardMove().perform();
		assertTrue(test[0]);
	}
	
	@Test
	public void testDiscard()
	{
		assertTrue(GameModel.instance().isEmptyDiscardPile());
		assertFalse(GameModel.instance().isEmptyDeck());// 3 of hearts
		for( int i = 0; i < 24; i++ )
		{
			assertFalse(GameModel.instance().isEmptyDeck());
			GameModel.instance().getDiscardMove().perform();
			// Test a few cards
			if( i == 0 )
			{
				assertEquals(Card.get(Rank.JACK, Suit.DIAMONDS), GameModel.instance().peekDiscardPile());
			}
			if( i == 1 )
			{
				assertEquals(Card.get(Rank.TEN, Suit.DIAMONDS), GameModel.instance().peekDiscardPile());
			}
			assertFalse(GameModel.instance().isEmptyDiscardPile());
		}
		assertTrue(GameModel.instance().isEmptyDeck());
	}
	
	@Test
	public void testGetStack()
	{
		GameModel model = GameModel.instance();
		Card[] stack = model.getStack(StackIndex.FIRST);
		assertEquals(Card.get(Rank.KING, Suit.SPADES), stack[0]);
		assertEquals(1, stack.length);
		// Test that the method returns a clone
		stack[0] =Card.get(Rank.QUEEN, Suit.CLUBS);
		stack = model.getStack(StackIndex.FIRST);
		assertEquals(Card.get(Rank.KING, Suit.SPADES), stack[0]);
		assertEquals(1, stack.length);
		stack = model.getStack(StackIndex.SECOND);
		assertEquals(2, stack.length);
		assertEquals(Card.get(Rank.QUEEN, Suit.SPADES), stack[0]);
		assertEquals(Card.get(Rank.JACK, Suit.SPADES), stack[1]);
	}
	
	@Test
	public void testGetSubStack()
	{
		GameModel model = GameModel.instance();
		Card[] stack = model.getSubStack(Card.get(Rank.KING, Suit.SPADES), StackIndex.FIRST);
		assertEquals(Card.get(Rank.KING, Suit.SPADES), stack[0]);
		assertEquals(1, stack.length);
		// Test that the method returns a clone
		stack[0] = Card.get(Rank.QUEEN, Suit.CLUBS);
		stack = model.getSubStack(Card.get(Rank.KING, Suit.SPADES), StackIndex.FIRST);
		assertEquals(Card.get(Rank.KING, Suit.SPADES), stack[0]);
		assertEquals(1, stack.length);

		stack = model.getSubStack(Card.get(Rank.TEN, Suit.SPADES), StackIndex.THIRD);
		assertEquals(3, stack.length);
		stack = model.getSubStack(Card.get(Rank.NINE, Suit.SPADES), StackIndex.THIRD);
		assertEquals(2, stack.length);
		stack = model.getSubStack(Card.get(Rank.EIGHT, Suit.SPADES), StackIndex.THIRD);
		assertEquals(1, stack.length);
	}
	
	@Test 
	public void testMoves()
	{
		GameModel model = GameModel.instance();
		assertTrue(model.isEmptySuitStack(SuitStackIndex.FIRST)); 
		assertFalse(model.isLegalMove(Card.get(Rank.THREE, Suit.CLUBS), SuitStackIndex.SECOND));
		assertFalse(model.isLegalMove(Card.get(Rank.THREE, Suit.CLUBS), SuitStackIndex.FIRST));
		assertFalse(model.isLegalMove(Card.get(Rank.TWO, Suit.CLUBS), SuitStackIndex.FIRST));
		assertTrue(model.isLegalMove(Card.get(Rank.ACE, Suit.CLUBS), SuitStackIndex.FIRST));
		model.getDiscardMove().perform(); // Jack of diamond
		model.getDiscardMove().perform(); // Ten of diamond
		assertTrue(model.isLegalMove(model.peekDiscardPile(), StackIndex.SECOND));
		model.getCardMove(model.peekDiscardPile(), StackIndex.SECOND).perform();
		model.getDiscardMove().perform();
		assertFalse(model.isLegalMove(model.peekDiscardPile(), StackIndex.SECOND));
		model.getDiscardMove().perform();
		assertEquals(Card.get(Rank.EIGHT, Suit.DIAMONDS), model.peekDiscardPile());
		model.getDiscardMove().perform();
		assertTrue(model.isLegalMove(model.peekDiscardPile(), StackIndex.THIRD));
		model.getCardMove(model.peekDiscardPile(), StackIndex.THIRD).perform();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		assertEquals(Card.get(Rank.FIVE, Suit.DIAMONDS), model.peekDiscardPile());
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform();
		assertEquals(Card.get(Rank.ACE, Suit.DIAMONDS), model.peekDiscardPile());
		assertTrue(model.isLegalMove(model.peekDiscardPile(), SuitStackIndex.SECOND));
		model.getCardMove(model.peekDiscardPile(), SuitStackIndex.SECOND).perform();
		assertEquals(Card.get(Rank.ACE, Suit.DIAMONDS), model.peekSuitStack(SuitStackIndex.SECOND));
		assertEquals(Card.get(Rank.TWO, Suit.DIAMONDS), model.peekDiscardPile());
		model.getCardMove(model.peekDiscardPile(), SuitStackIndex.SECOND).perform();
		assertEquals(Card.get(Rank.TWO, Suit.DIAMONDS), model.peekSuitStack(SuitStackIndex.SECOND));
		model.getCardMove(model.peekDiscardPile(), SuitStackIndex.SECOND).perform();
		model.getCardMove(model.peekDiscardPile(), SuitStackIndex.SECOND).perform();
		model.getCardMove(model.peekDiscardPile(), SuitStackIndex.SECOND).perform();
		model.getCardMove(model.peekDiscardPile(), SuitStackIndex.SECOND).perform(); 
		// 8th of diamond is on top of the discard pile
		assertFalse(model.isLegalMove(model.peekDiscardPile(), SuitStackIndex.SECOND));
		model.getCardMove(Card.get(Rank.SEVEN, Suit.DIAMONDS), SuitStackIndex.SECOND).perform();
		// move the 7th back to the working stack
		model.getCardMove(Card.get(Rank.SEVEN, Suit.DIAMONDS), StackIndex.THIRD).perform();
	}
	
	@Test 
	public void testMoves2()
	{
		GameModel model = GameModel.instance();
		model.getDiscardMove().perform();
		model.getDiscardMove().perform(); // 10D on discard pile
		model.getCardMove(model.peekDiscardPile(), StackIndex.SECOND).perform();
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
		model.getCardMove(model.peekDiscardPile(), StackIndex.SECOND).perform();
		// move three card sequence to pile 7
		Card[] stack = model.getSubStack(Card.get(Rank.JACK, Suit.SPADES), StackIndex.SECOND);
		assertEquals(3, stack.length);
		assertEquals(Card.get(Rank.JACK, Suit.SPADES), stack[0]);
		assertEquals(Card.get(Rank.TEN, Suit.DIAMONDS), stack[1]);
		assertEquals(Card.get(Rank.NINE, Suit.CLUBS), stack[2]);
		model.getCardMove(stack[0], StackIndex.SEVENTH).perform();
		assertEquals(1, model.getStack(StackIndex.SECOND).length);
		stack = model.getSubStack(Card.get(Rank.JACK, Suit.SPADES), StackIndex.SEVENTH);
		assertEquals(10, model.getStack(StackIndex.SEVENTH).length);
		model.getCardMove(model.getSubStack(Card.get(Rank.QUEEN, Suit.DIAMONDS), StackIndex.SEVENTH)[0], StackIndex.FIRST).perform();
		Card[] stack2 = model.getStack(StackIndex.FIRST);
		assertEquals(5, stack2.length);
	}
	
	@Test 
	public void testMoves3()
	{
		GameModel model = GameModel.instance();
		for( int i = 0; i < 14; i++ )
		{
			model.getDiscardMove().perform();
		}
		assertEquals(Card.get(Rank.JACK, Suit.CLUBS), model.peekDiscardPile());
		model.getCardMove(model.peekDiscardPile(), StackIndex.FIFTH).perform();
		assertEquals(6, model.getStack(StackIndex.FIFTH).length);
		model.getCardMove(model.getSubStack(Card.get(Rank.JACK, Suit.CLUBS), StackIndex.FIFTH)[0], StackIndex.SEVENTH).perform();
		assertEquals(5, model.getStack(StackIndex.FIFTH).length);
		assertEquals(8, model.getStack(StackIndex.SEVENTH).length);
	}
	
	@Test
	public void testNullMove()
	{
		GameModel model = GameModel.instance();
		Move nullMove = model.getNullMove();
		assertTrue(nullMove.isNull());
		// Only really tests that nothing crashes
		nullMove.perform();
		nullMove.undo();
	}
	
	@Test
	public void testUndo1()
	{
		// Tests undoing discard moves
		GameModel model = GameModel.instance();
		assertTrue(model.isEmptyDiscardPile());
		assertFalse(model.canUndo());
		Move discard = model.getDiscardMove();
		assertFalse(discard.isNull());
		discard.perform();
		assertTrue(model.canUndo());
		Card card = model.peekDiscardPile();
		model.undoLast();
		assertFalse(model.canUndo());
		assertTrue(model.isEmptyDiscardPile());
		model.getDiscardMove().perform();
		assertEquals(card, model.peekDiscardPile());
	}
	
	@Test
	public void testGetScore()
	{
		GameModel model = GameModel.instance();
		assertEquals( 0, model.getScore());
		assertFalse(model.isCompleted());
	}
}
