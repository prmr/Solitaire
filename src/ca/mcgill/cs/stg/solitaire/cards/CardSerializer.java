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
package ca.mcgill.cs.stg.solitaire.cards;

import java.util.StringJoiner;

/**
 * A utility class to serialize and deserialize cards.
 * 
 * In serialized form, a card is represented as an integer between 0 and 51. A
 * stack of cards is represented as a semicolon-separated sequence of integers,
 * going in sequence from bottom to top.
 */
public final class CardSerializer
{
	private static final String SEPARATOR = ";";
	
	private CardSerializer() {}
	
	private static String cardToInt(Card pCard)
	{
		assert pCard != null;
		return Integer.toString(pCard.getSuit().ordinal() * 
				Rank.values().length + pCard.getRank().ordinal());
	}
	
	private static Card intToCard(int pNumber)
	{
		assert pNumber >=0 && pNumber < Rank.values().length * 
				Suit.values().length;
		return Card.get(Rank.values()[pNumber % Rank.values().length], 
				Suit.values()[pNumber / Rank.values().length]);
	}
	
	/**
	 * @param pCard The card to serialize.
	 * @return A serialized version of this card.
	 */
	public static String serialize(Card pCard)
	{
		assert pCard != null;
		return cardToInt(pCard);
	}
	
	/**
	 * @param pCards A card stack to serialize.
	 * @return A serialized version of this stack.
	 */
	public static String serialize(CardStack pCards)
	{
		assert pCards != null;
		StringJoiner serialized = new StringJoiner(SEPARATOR);
		for( Card card : pCards )
		{
			serialized.add(cardToInt(card));
		}
		return serialized.toString();
	}
	
	/**
	 * @param pCards A card stack to deserialize.
	 * @return A deserialized version of this stack.
	 */
	public static CardStack deserialize(String pCards)
	{
		assert pCards != null;
		CardStack result = new CardStack();
		for( String card : pCards.split(SEPARATOR))
		{
			result.push(intToCard(Integer.parseInt(card)));
		}
		return result;
	}
	
	/**
	 * Convenience method to deserialize a sequence of cards 
	 * and obtain the top of the deserialized stack in one 
	 * operation.
	 * 
	 * @param pCards A card stack to deserialize.
	 * @return The cards at the top of the stack.
	 * @pre !pCards.isEmpty()
	 */
	public static Card deserializeBottomCard(String pCards)
	{
		assert !pCards.isEmpty();
		return deserialize(pCards).peek(0);
	}
}
