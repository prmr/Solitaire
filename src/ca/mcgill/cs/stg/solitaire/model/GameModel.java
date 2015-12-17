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
import java.util.List;

import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.cards.Card.Rank;
import ca.mcgill.cs.stg.solitaire.cards.Card.Suit;

/**
 * Keeps track of the current state of the game and provides
 * a facade to it. Implements the Singleton design pattern.
 * 
 * To prevent
 * this class from degenerating into a God class, responsibilities
 * are separated into three package-private "manager" classes 
 * in charge of managing the state. However, these manager classes
 * are not responsible for notifying observers.
 */
public final class GameModel 
{
	private static final GameModel INSTANCE = new GameModel();
	
	private DeckManager aDeckManager = new DeckManager();
	private TopStackManager aTopStackManager = new TopStackManager();
	private BottomStackManager aBottomStackManager;
	private List<GameModelListener> aListeners = new ArrayList<>();
	
	/**
	 * Represents the different stacks where cards
	 * can be accumulated.
	 */
	public enum StackIndex 
	{ FIRST, SECOND, THIRD, FOURTH, FIFTH, SIXTH, SEVENTH }
	
	private GameModel()
	{
		initialize();
	}
	
	public boolean hasTopPileCard(Suit pSuit)
	{
		return !aTopStackManager.stackEmpty(pSuit);
	}
	
	public Card getTopPileCard(Suit pSuit)
	{
		return aTopStackManager.seeTopCard(pSuit);
	}
	
	public static GameModel instance()
	{
		return INSTANCE;
	}
	
	public void addListener(GameModelListener pListener)
	{
		aListeners.add(pListener);
	}
	
	private void notifyListeners()
	{
		for( GameModelListener listener : aListeners )
		{
			listener.gameStateChanged();
		}
	}
	
	private void initialize()
	{
		aDeckManager.initialize();
		aTopStackManager.initialize();
		aBottomStackManager.initialize(aDeckManager);
	}
	
	public boolean canDropOnTopPile(Card pCard, Suit pSuit )
	{
		if( pCard.getRank() == Rank.ACE && pCard.getSuit() == pSuit )
		{
			return true;
		}
		if( aTopStackManager.stackEmpty(pSuit) )
		{
			return false;
		}

		if( pCard.getRank().ordinal() == aTopStackManager.seeTopCard(pSuit).getRank().ordinal()+1 &&
				pCard.getSuit() == pSuit)
		{
			return true;
		}
		return false;
	}
	
	public boolean canDropOnStack(Card pCard, int pIndex )
	{
		return aBottomStackManager.canDropOnStack(pCard, StackIndex.values()[pIndex]); // TODO change the method signature
	}
	
	public void dropToTopPile(Card pCard)
	{
		// look for the card
		Card card = null;
		if( aDeckManager.isOnTopOfDiscardPile(pCard))
		{
			aDeckManager.popDiscardPile();
			card = pCard;
		}
		else if( aBottomStackManager.isInStacks(pCard))
		{
			aBottomStackManager.popTopCard(pCard);
			card = pCard;
		}
		assert card != null;
		aTopStackManager.push(card);
		notifyListeners();
	}
	
	public void dropToStack(Card pCard, int pIndex)
	{
		// look for the card
		Card card = null;
		if( aDeckManager.isOnTopOfDiscardPile(pCard))
		{
			aDeckManager.popDiscardPile();
			card = pCard;
		}
		else if( aBottomStackManager.isInStacks(pCard))
		{
			aBottomStackManager.popTopCard(pCard);
			card = pCard;
		}
		if( card == null )
		{
			if( aTopStackManager.isInTopStacks(pCard))
			{
				card = pCard;
				aTopStackManager.popCard(pCard);
			}
		}
		assert card != null;
		aBottomStackManager.push(pCard, StackIndex.values()[pIndex]); // TODO
		notifyListeners();
	}
	
	public CardView[] getStackAt(int pIndex)
	{
		return aBottomStackManager.getStack(StackIndex.values()[pIndex]); // TODO
	}
	
	public boolean hasEmptyDeck()
	{
		return aDeckManager.deckEmpty();
	}
	
	public boolean hasEmptyDiscardPile()
	{
		return aDeckManager.discardPileEmpty();
	}
	
	public Card getDiscardPileTop()
	{
		assert !aDeckManager.discardPileEmpty();
		return aDeckManager.getDiscardPileTop();
	}
	
	public void discard()
	{
		aDeckManager.discardFromDeck();
		notifyListeners();
	}
}
