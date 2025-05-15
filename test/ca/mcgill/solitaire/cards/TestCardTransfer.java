/*******************************************************************************
 * Solitaire
 *  
 *  Copyright (C) 2016-2024 by Martin P. Robillard
 *  
 *  See: https://github.com/prmr/Solitaire
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/.
 *******************************************************************************/
package ca.mcgill.solitaire.cards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ca.mcgill.solitaire.testutils.TestUtils;

public class TestCardTransfer
{
	@ParameterizedTest
	@MethodSource("allCards")
	void testSerializeSingleCard(Card pCard)
	{
		assertSame(pCard, CardSerializer
				.deserialize(CardSerializer.serialize(pCard)).peek());
	}
	
	void testSerializeEmpty()
	{
		CardStack actual = CardSerializer.deserialize(
				CardSerializer.serialize(new CardStack()));
		assertTrue(actual.isEmpty());
	}
	
	@RepeatedTest(20)
	void testSerializeThreeCards()
	{
		Deck deck = new Deck();
		CardStack expected = new CardStack();
		expected.push(deck.draw());
		expected.push(deck.draw());
		expected.push(deck.draw());
		CardStack actual = CardSerializer.deserialize(CardSerializer.serialize(expected));
		assertEquals(expected.size(), actual.size());
		for( int i=0; i < expected.size(); i++ )
		{
			assertSame(expected.pop(), actual.pop());
		}
	}
	
	private static List<Card> allCards()
	{
		return TestUtils.allCards();
	}
}
