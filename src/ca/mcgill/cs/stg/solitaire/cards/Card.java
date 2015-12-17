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
package ca.mcgill.cs.stg.solitaire.cards;

/**
 * An immutable description of a playing card.
 */
public final class Card implements Comparable<Card>
{
	/**
	 * Represents the rank of the card.
	 */
	public enum Rank 
	{ ACE, TWO, THREE, FOUR, FIVE, SIX,
		SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING }
	
	/**
	 * Represents the suit of the card.
	 */
	public enum Suit 
	{ CLUBS, DIAMONDS, HEARTS, SPADES }
	
	/**
	 * A card color, red or black.
	 */
	public enum Color
	{
		RED, BLACK;
	}
	
	private final Rank aRank;
	private final Suit aSuit;
	
	/**
	 * Create a new card object. 
	 * @param pRank The rank of the card.
	 * @param pSuit The suit of the card.
	 */
	public Card(Rank pRank, Suit pSuit )
	{
		aRank = pRank;
		aSuit = pSuit;
	}
	
	/**
	 * Create a new card from a card description string.
	 * @param pString The description string.
	 */
	public Card(String pString)
	{
		String[] tokens = pString.split(" ");
		aRank = Rank.valueOf(tokens[0]);
		aSuit = Suit.valueOf(tokens[2]);
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
	 * @return The color of the card.
	 */
	public Color getColor()
	{
		if( aSuit == Suit.DIAMONDS || aSuit == Suit.HEARTS )
		{
			return Color.RED;
		}
		else
		{
			return Color.BLACK;
		}
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
	 * @return See above.
	 */
	public String toString()
	{
		return aRank + " of " + aSuit;
	}
	
	/**
	 * @return A compact representation of this card.
	 */
	public String toShortString()
	{
		StringBuilder lReturn = new StringBuilder();
		if( aRank.ordinal() == Rank.ACE.ordinal() || aRank.ordinal() >= Rank.TEN.ordinal())
		{
			lReturn.append(aRank.toString().charAt(0));
		}
		else
		{
			lReturn.append(aRank.ordinal() + 1);
		}
		lReturn.append(aSuit.toString().charAt(0));
		return lReturn.toString();
	}
	
	/**
	 * Compares two cards according to gin rules (ace is low, suits 
	 * run as Spade, Hearts, Diamonds, Clubs (high to low)).
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * @param pCard The card to compare to
	 * @return Returns a negative integer, zero, or a positive integer 
	 * as this object is less than, equal to, or greater than pCard
	 */
	public int compareTo(Card pCard)
	{
		int lRankDifference = getRank().ordinal() - pCard.getRank().ordinal();
		if( lRankDifference != 0 )
		{
			return lRankDifference;
		}
		else
		{
			return getSuit().ordinal() - pCard.getSuit().ordinal();
		}
	}

	/**
	 * Two cards are equal if they have the same suit and rank.
	 * @param pCard The card to test.
	 * @return true if the two cards are equal
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object pCard ) 
	{
		if( pCard == null )
		{
			return false;
		}
		if( pCard == this )
		{
			return true;
		}
		if( pCard.getClass() != getClass() )
		{
			return false;
		}
		return (((Card)pCard).getRank() == getRank()) && (((Card)pCard).getSuit() == getSuit());
	}

	/** 
	 * The hashcode for a card is the suit*13 + that of the rank (perfect hash).
	 * @return the hashcode
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() 
	{
		return getSuit().ordinal() * Rank.values().length + getRank().ordinal();
	}
}
