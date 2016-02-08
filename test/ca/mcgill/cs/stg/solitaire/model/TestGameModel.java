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
import ca.mcgill.cs.stg.solitaire.cards.Card.Rank;
import ca.mcgill.cs.stg.solitaire.cards.Card.Suit;
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
		GameModel.instance().discard();
		GameModel.instance().addListener(new ListenerStub());
		GameModel.instance().discard();
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
			GameModel.instance().discard();
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
		CardView[] stack = model.getStack(StackIndex.FIRST);
		assertEquals(Card.get(Rank.KING, Suit.SPADES), stack[0].getCard());
		assertEquals(1, stack.length);
		// Test that the method returns a clone
		stack[0] = new CardView(Card.get(Rank.QUEEN, Suit.CLUBS));
		stack = model.getStack(StackIndex.FIRST);
		assertEquals(Card.get(Rank.KING, Suit.SPADES), stack[0].getCard());
		assertEquals(1, stack.length);
		stack = model.getStack(StackIndex.SECOND);
		assertEquals(2, stack.length);
		assertEquals(Card.get(Rank.QUEEN, Suit.SPADES), stack[0].getCard());
		assertEquals(Card.get(Rank.JACK, Suit.SPADES), stack[1].getCard());
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
		assertTrue(model.isEmptySuitStack(SuitStackIndex.FIRST)); // Clusts on first
		assertFalse(model.isLegalMove(Card.get(Rank.THREE, Suit.CLUBS), SuitStackIndex.SECOND));
		assertFalse(model.isLegalMove(Card.get(Rank.THREE, Suit.CLUBS), SuitStackIndex.FIRST));
		assertFalse(model.isLegalMove(Card.get(Rank.TWO, Suit.CLUBS), SuitStackIndex.FIRST));
		assertTrue(model.isLegalMove(Card.get(Rank.ACE, Suit.CLUBS), SuitStackIndex.FIRST));
		model.discard(); // Jack of diamond
		model.discard(); // Ten of diamond
		assertTrue(model.isLegalMove(model.peekDiscardPile(), StackIndex.SECOND));
		model.move(model.peekDiscardPile(), StackIndex.SECOND);
		model.discard();
		assertFalse(model.isLegalMove(model.peekDiscardPile(), StackIndex.SECOND));
		model.discard();
		assertEquals(Card.get(Rank.EIGHT, Suit.DIAMONDS), model.peekDiscardPile());
		model.discard();
		assertTrue(model.isLegalMove(model.peekDiscardPile(), StackIndex.THIRD));
		model.move(model.peekDiscardPile(), StackIndex.THIRD);
		model.discard();
		model.discard();
		assertEquals(Card.get(Rank.FIVE, Suit.DIAMONDS), model.peekDiscardPile());
		model.discard();
		model.discard();
		model.discard();
		model.discard();
		assertEquals(Card.get(Rank.ACE, Suit.DIAMONDS), model.peekDiscardPile());
		assertTrue(model.isLegalMove(model.peekDiscardPile(), SuitStackIndex.SECOND));
		model.move(model.peekDiscardPile(), SuitStackIndex.SECOND);
		assertEquals(Card.get(Rank.ACE, Suit.DIAMONDS), model.peekSuitStack(SuitStackIndex.SECOND));
		assertEquals(Card.get(Rank.TWO, Suit.DIAMONDS), model.peekDiscardPile());
		model.move(model.peekDiscardPile(), SuitStackIndex.SECOND);
		assertEquals(Card.get(Rank.TWO, Suit.DIAMONDS), model.peekSuitStack(SuitStackIndex.SECOND));
		model.move(model.peekDiscardPile(), SuitStackIndex.SECOND);
		model.move(model.peekDiscardPile(), SuitStackIndex.SECOND);
		model.move(model.peekDiscardPile(), SuitStackIndex.SECOND);
		model.move(model.peekDiscardPile(), SuitStackIndex.SECOND); 
		// 8th of diamond is on top of the discard pile
		assertFalse(model.isLegalMove(model.peekDiscardPile(), SuitStackIndex.SECOND));
		model.move(Card.get(Rank.SEVEN, Suit.DIAMONDS), SuitStackIndex.SECOND);
		// move the 7th back to the working stack
		model.move(Card.get(Rank.SEVEN, Suit.DIAMONDS), StackIndex.THIRD);
	}
	
	@Test 
	public void testMoves2()
	{
		GameModel model = GameModel.instance();
		model.discard();
		model.discard(); // 10D on discard pile
		model.move(model.peekDiscardPile(), StackIndex.SECOND);
		model.discard();
		model.discard();
		model.discard();
		model.discard();
		model.discard();
		model.discard();
		model.discard();
		model.discard();
		model.discard();
		model.discard();
		model.discard();
		model.discard();
		model.discard();
		model.discard(); // 9C on discard pile
		assertEquals(Card.get(Rank.NINE, Suit.CLUBS), model.peekDiscardPile());
		model.move(model.peekDiscardPile(), StackIndex.SECOND);
		// move three card sequence to pile 7
		Card[] stack = model.getSubStack(Card.get(Rank.JACK, Suit.SPADES), StackIndex.SECOND);
		assertEquals(3, stack.length);
		assertEquals(Card.get(Rank.JACK, Suit.SPADES), stack[0]);
		assertEquals(Card.get(Rank.TEN, Suit.DIAMONDS), stack[1]);
		assertEquals(Card.get(Rank.NINE, Suit.CLUBS), stack[2]);
		model.move(stack[0], StackIndex.SEVENTH);
		assertEquals(1, model.getStack(StackIndex.SECOND).length);
		stack = model.getSubStack(Card.get(Rank.JACK, Suit.SPADES), StackIndex.SEVENTH);
		assertEquals(10, model.getStack(StackIndex.SEVENTH).length);
		model.move(model.getSubStack(Card.get(Rank.QUEEN, Suit.DIAMONDS), StackIndex.SEVENTH)[0], StackIndex.FIRST);
		CardView[] stack2 = model.getStack(StackIndex.FIRST);
		assertEquals(5, stack2.length);
	}
	
	@Test 
	public void testMoves3()
	{
		GameModel model = GameModel.instance();
		for( int i = 0; i < 14; i++ )
		{
			model.discard();
		}
		assertEquals(Card.get(Rank.JACK, Suit.CLUBS), model.peekDiscardPile());
		model.move(model.peekDiscardPile(), StackIndex.FIFTH);
		assertEquals(6, model.getStack(StackIndex.FIFTH).length);
		model.move(model.getSubStack(Card.get(Rank.JACK, Suit.CLUBS), StackIndex.FIFTH)[0], StackIndex.SEVENTH);
		assertEquals(5, model.getStack(StackIndex.FIFTH).length);
		assertEquals(8, model.getStack(StackIndex.SEVENTH).length);
	}
}
