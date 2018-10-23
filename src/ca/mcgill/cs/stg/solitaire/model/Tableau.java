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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;

import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.cards.CardStack;
import ca.mcgill.cs.stg.solitaire.cards.Deck;
import ca.mcgill.cs.stg.solitaire.cards.Rank;

/**
 * Represents seven piles of cards that fan downwards, where cards
 * must be stacked in alternating suit colors, and where cards can 
 * be moved from pile to pile.
 */
class Tableau
{
	private final Map<TableauPile, CardStack> aPiles = new HashMap<>();
	private final Set<Card> aVisible = new HashSet<>();
	
	/**
	 * Creates an empty tableau.
	 */
	Tableau()
	{
		for( TableauPile index : TableauPile.values() )
		{
			aPiles.put(index, new CardStack());
		}
	}
	
	/**
	 * Fills the tableau by drawing cards from the deck.
	 * @param pDeck a deck of card to use to fill the piles initially.
	 * @pre pDeck != null
	 * 
	 */
	void initialize(Deck pDeck)
	{   
		assert pDeck != null; 
		aVisible.clear();
		for( int i = 0; i < TableauPile.values().length; i++ )
		{
			aPiles.get(TableauPile.values()[i]).clear();
			for( int j = 0; j < i+1; j++ )
			{
				Card card = pDeck.draw();
				aPiles.get(TableauPile.values()[i]).push(card);
				if( j == i )
				{
					aVisible.add(card);
				}
			}
		}
	}
	
	
	/**
	 * Determines if it is legal to move pCard on top of pPile, 
	 * i.e. if a king is moved to an empty pile or any other rank on 
	 * a card of immediately greater rank but of a different color.
	 * @param pCard The card we wish to move
	 * @param pPile The desired destination pile
	 * @return True if the move is legal
	 * @pre pCard != null && pPile != null
	 */
	boolean canMoveTo(Card pCard, TableauPile pPile )
	{
		assert pCard != null && pPile != null;
		CardStack pile = aPiles.get(pPile);
		if( pile.isEmpty() )
		{
			return pCard.getRank() == Rank.KING;
		}
		else
		{ 
			return pCard.getRank().ordinal() == pile.peek().getRank().ordinal()-1 && 
					!pCard.getSuit().sameColorAs(pile.peek().getSuit());
		}
	}
	
	/**
	 * @param pCard The card to check.
	 * @return True if pCard is a visible king located at the bottom
	 * of the pile.
	 * @pre pCard != null && contains(pCard);
	 */
	public boolean isBottomKing(Card pCard)
	{
		assert pCard != null && contains(pCard);
		return pCard.getRank() == Rank.KING && aPiles.get(getPile(pCard)).peek(0) == pCard;
	}

	
	/**
	 * Returns a copy of the entire pile at the specified position in the tableau.
	 * 
	 * @param pPile The pile to obtain.
	 * @return A copy of the at pPile.
	 * @pre pPile != null
	 */
	CardStack getPile(TableauPile pPile)
	{
		assert pPile != null;
		return new CardStack(aPiles.get(pPile));
	}
	
	/**
	 * Returns true if moving pCard away reveals the top of the card.
	 * @param pCard The card to test
	 * @return true if the card above pCard is not visible and pCard
	 * is visible.
	 * @pre pCard != null && contains(pCard)
	 */
	boolean revealsTop(Card pCard)
	{
		assert pCard != null && contains(pCard);
		Optional<Card> previous = getPreviousCard(pCard);
		if( !previous.isPresent() )
		{
			return false;
		}
		return aVisible.contains(pCard) && !aVisible.contains(previous.get());
	}
	
	private TableauPile getPile(Card pCard)
	{
		assert contains(pCard);
		for( TableauPile pile : TableauPile.values() )
		{
			if( contains(pCard, pile))
			{
				return pile;
			}
		}
		assert false;
		return null;
	}
	
	private Optional<Card> getPreviousCard(Card pCard)
	{
		Optional<Card> previous = Optional.empty();
		for( Card card : aPiles.get(getPile(pCard)))
		{
			if( card == pCard )
			{
				return previous;
			}
			previous = Optional.of(card);
		}
		return Optional.empty();
	}
	
	
 	/**
	 * Move pCard and all the cards below to pDestination.
	 * @param pCard The card to move, possibly including all the cards on top of it.
	 * @param pOrigin The location of the card before the move.
	 * @param pDestination The intended destination of the card.
     * @pre this is a legal move
	 */
	void moveWithin(Card pCard, TableauPile pOrigin, TableauPile pDestination )
	{
		assert pCard != null && pOrigin != null && pDestination != null;
		assert contains(pCard, pOrigin);
		assert isVisible(pCard);
		Stack<Card> temp = new Stack<>();
		Card card = aPiles.get(pOrigin).pop();
		temp.push(card);
		while( card != pCard )
		{
			card = aPiles.get(pOrigin).pop();
			temp.push(card);
		}
		while( !temp.isEmpty() )
		{
			aPiles.get(pDestination).push(temp.pop());
		}
	}
	
	/**
	 * Returns a sequence of cards starting at pCard and including
	 * all cards on top of it.
	 * @param pCard The bottom card in the sequence
	 * @param pPile The target pile
	 * @return A copy of the requested sequence.
	 * @pre pCard != null && pPile != null
	 */
	CardStack getSequence(Card pCard, TableauPile pPile)
	{
		assert pCard != null && pPile != null;
		CardStack stack = aPiles.get(pPile);
		List<Card> lReturn = new ArrayList<>();
		boolean aSeen = false;
		for( Card card : stack )
		{
			if( card == pCard )
			{
				aSeen = true;
			}
			if( aSeen )
			{
				lReturn.add(card);
			}
		}
		return new CardStack(lReturn);
	}
	
	/**
	 * Make the top card of a pile visible.
	 * @param pIndex The index of the requested pile.
	 * @pre pIndex != null && !isEmpty(pIndex)
	 */
	void showTop(TableauPile pIndex)
	{
		assert !aPiles.get(pIndex).isEmpty();
		aVisible.add(aPiles.get(pIndex).peek());
	}
	
	/**
	 * Make the top card of a pile not visible.
	 * @param pIndex The index of the requested stack.
	 * @pre pIndex != null && !isEmpty(pIndex)
	 */
	void hideTop(TableauPile pIndex)
	{
		assert !aPiles.get(pIndex).isEmpty();
		aVisible.remove(aPiles.get(pIndex).peek());
	}
	
	/**
	 * @param pCard The card to check
	 * @param pIndex The index of the pile to check
	 * @return True if pIndex contains pCard
	 * @pre pCard != null && pIndex != null
	 */
	boolean contains(Card pCard, TableauPile pIndex)
	{
		assert pCard != null && pIndex != null;
		for( Card card : aPiles.get(pIndex))
		{
			if( card == pCard )
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param pCard The card to check.
	 * @return Whether pCard is contains in any stack.
	 * @pre pCard != null;
	 */
	boolean contains(Card pCard)
	{
		assert pCard != null;
		for( TableauPile index : TableauPile.values())
		{
			if( contains(pCard, index))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param pCard The card to check.
	 * @return true if pCard is visible in the piles.
	 * @pre contains(pCard)
	 */
	boolean isVisible(Card pCard)
	{
		assert contains(pCard);
		return aVisible.contains(pCard);
	}
	
	/**
	 * @param pCard The card to check.
	 * @return True if the card is visible and there is no
	 * visible card below it in its pile. This includes
	 * the case where the card is at the bottom of the pile.
	 * @pre pCard != null && contains(pCard)
	 */
	boolean isLowestVisible(Card pCard)
	{
		assert pCard != null && contains(pCard);
		if( !isVisible(pCard ))
		{
			return false;
		}
		else
		{
			Optional<Card> previousCard = getPreviousCard(pCard);
			return !previousCard.isPresent() || !isVisible(previousCard.get());
		}
	}
	
	/**
	 * Removes the top card from the pile at pIndex.
	 * @param pIndex The index of the pile to pop.
	 * @pre !isEmpty(pIndex)
	 */
	void pop(TableauPile pIndex)
	{
		assert !aPiles.get(pIndex).isEmpty();
		aVisible.remove(aPiles.get(pIndex).pop());
	}
	
	/**
	 * Places a card on top of the pile at pIndex. The
	 * card will be visible by default.
	 * @param pCard The card to push.
	 * @param pIndex The index of the destination stack.
	 * @pre pCard != null && pIndex != null;
	 */
	void push(Card pCard, TableauPile pIndex)
	{
		assert pCard != null && pIndex != null;
		aPiles.get(pIndex).push(pCard);
		aVisible.add(pCard);
	}
}
