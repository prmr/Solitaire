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
import java.util.List;
import java.util.Map;
import java.util.Stack;

import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.cards.Card.Rank;
import ca.mcgill.cs.stg.solitaire.cards.Deck;
import ca.mcgill.cs.stg.solitaire.model.GameModel.StackIndex;

/**
 * Manages the state of the bottom stacks where partial
 * suits are accumulated.
 */
class WorkingStackManager
{
	private Map<StackIndex, Stack<CardView>> aStacks = new HashMap<>();
	
	/**
	 * Creates working stacks with no cards in them.
	 */
	WorkingStackManager()
	{
		for( StackIndex index : StackIndex.values() )
		{
			aStacks.put(index, new Stack<CardView>());
		}
	}
	
	/**
	 * Fills the working stacks by drawing cards from the deck.
	 */
	void initialize(Deck pDeck)
	{
		for( int i = 0; i < StackIndex.values().length; i++ )
		{
			aStacks.get(StackIndex.values()[i]).clear();
			for( int j = 0; j < i+1; j++ )
			{
				CardView view = new CardView(pDeck.draw());
				if( j == i )
				{
					view.makeVisible();
				}
				aStacks.get(StackIndex.values()[i]).push(view);
			}
		}
	}
	
	boolean canMoveTo(Card pCard, StackIndex pIndex )
	{
		Stack<CardView> stack = aStacks.get(pIndex);
		if( stack.isEmpty() )
		{
			return pCard.getRank() == Rank.KING;
		}
		else
		{ 
			return pCard.getRank().ordinal() == stack.peek().getCard().getRank().ordinal()-1 && 
					!pCard.sameColorAs(stack.peek().getCard());
		}
	}
	
	CardView[] getStack(StackIndex pIndex)
	{
		return aStacks.get(pIndex).toArray(new CardView[aStacks.get(pIndex).size()]);
	}
	
	public Card[] getSequence(Card pCard, StackIndex pIndex)
	{
		Stack<CardView> stack = aStacks.get(pIndex);
		List<Card> lReturn = new ArrayList<>();
		boolean aSeen = false;
		for( CardView card : stack )
		{
			if( card.getCard() == pCard )
			{
				aSeen = true;
			}
			if( aSeen )
			{
				lReturn.add(card.getCard());
			}
		}
		return lReturn.toArray(new Card[lReturn.size()]);
	}
	
	boolean isInStacks(Card pCard )
	{
		for( Stack<CardView> cardView : aStacks.values() )
		{
			if(!cardView.isEmpty() && cardView.peek().getCard() == pCard )
			{
				return true;
			}
		}
		return false;
	}
	
	void pop(Card pCard)
	{
		for( Stack<CardView> cardView : aStacks.values() )
		{
			if(!cardView.isEmpty() && cardView.peek().getCard() == pCard )
			{
				cardView.pop();
				if( !cardView.isEmpty() )
				{
					cardView.peek().makeVisible();
				}
			}
		}
	}
	
	void push(Card pCard, StackIndex pIndex)
	{
		CardView cardView = new CardView(pCard);
		cardView.makeVisible();
		aStacks.get(pIndex).push(cardView);
	}
	
	
}
