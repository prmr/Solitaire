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
package ca.mcgill.cs.stg.solitaire.gui;

import ca.mcgill.cs.stg.solitaire.cards.Card;

/**
 * An immutable utility object to facilitate the transfer of card 
 * through the drag board (drag and drop space).
 */
public class CardTransfer
{
	private static final String SEPARATOR = ";";
	
	private Card[] aCards;
	
	/**
	 * Creates a card transfer from a serialized
	 * version of the cards.
	 * @param pString The serialized version
	 */
	public CardTransfer(String pString)
	{
		assert pString != null && pString.length() > 0;
		String[] tokens = pString.split(SEPARATOR);
		aCards = new Card[tokens.length];
		for( int i = 0; i < tokens.length; i++ )
		{
			aCards[i] = Card.get(tokens[i]);
		}
		assert aCards.length > 0;
	}
	
	/**
	 * Converts an array of cards into an id string
	 * that can be deserialized by the constructor.
	 * @param pCards The array of cards with high-ranking cards first.
	 * @return The id string.
	 */
	public static String serialize(Card[] pCards)
	{
		String lReturn = pCards[0].getIDString();
		for( int i = 1; i < pCards.length; i++ )
		{
			lReturn += SEPARATOR + pCards[i].getIDString();
		}
		return lReturn;
	}
	
	/**
	 * @return The top card in the transfer
	 * (the one with the highest rank)
	 */
	public Card getTop()
	{
		return aCards[0];
	}
	
	/**
	 * @return The number of cards in the tranfer.
	 */
	public int size()
	{
		return aCards.length;
	}
}
