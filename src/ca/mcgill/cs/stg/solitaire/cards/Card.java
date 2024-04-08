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

/**
 * An immutable description of a playing card. This abstraction is designed to
 * be independent of game logic, so it does not provide any service that relies
 * on the knowledge of the rules of any particular game.
 * 
 * This class implements the Flyweight design pattern: there can only ever be
 * one instance of a card that represents a specific real-world playing card
 * (such as Ace of Spades). In the absence of serialization and reflection,
 * this ensures that the behavior of the == operator is identical to that of
 * the equals method when two card arguments are provided.
 */
public final class Card
{
	// Indexed by suit, then rank.
	private static final Card[][] CARDS = new Card[Suit.values().length][];
	
	// Create the flyweight objects.
	static
	{
		for( Suit suit : Suit.values() )
		{
			CARDS[suit.ordinal()] = new Card[Rank.values().length];
			for( Rank rank : Rank.values() )
			{
				CARDS[suit.ordinal()][rank.ordinal()] = new Card(rank, suit);
			}
		}
	}
	
	private final Rank aRank;
	private final Suit aSuit;
	
	private Card(Rank pRank, Suit pSuit )
	{
		aRank = pRank;
		aSuit = pSuit;
	}
	
	/**
	 * Get a flyweight Card object.
	 * 
	 * @param pRank The rank of the card (from Ace to King).
	 * @param pSuit The suit of the card (Clubs, Diamond, Spades, Hearts).
	 * @return The card object representing the card with pRank and pSuit.
	 */
	public static Card get(Rank pRank, Suit pSuit)
	{
		assert pRank != null && pSuit != null;
		return CARDS[pSuit.ordinal()][pRank.ordinal()];
	}
	
	/**
	 * Obtain the rank of the card.
	 * @return An object representing the rank of the card.
	 */
	public Rank getRank()
	{
		return aRank;
	}
	
	/**
	 * Obtain the suit of the card.
	 * @return An object representing the suit of the card 
	 */
	public Suit getSuit()
	{
		return aSuit;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return aRank + " of " + aSuit;
	}
}
