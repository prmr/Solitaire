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
import java.util.Stack;

import ca.mcgill.cs.stg.solitaire.ai.GreedyPlayingStrategy;
import ca.mcgill.cs.stg.solitaire.ai.PlayingStrategy;
import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.cards.Card.Rank;
import ca.mcgill.cs.stg.solitaire.cards.Card.Suit;
import ca.mcgill.cs.stg.solitaire.cards.Deck;

/**
 * Keeps track of the current state of the game and provides
 * a facade to it. Implements the Singleton design pattern.
 * 
 * The game state can logically be separated into four distinct 
 * conceptual elements: the deck, the discard pile, the four
 * "suit stacks" where completed suits are accumulated, and the
 * seven "working stacks" where cards can be accumulated in sequences
 * of alternating suit colors.
 * 
 * To prevent
 * this class from degenerating into a God class, responsibilities
 * are separated into package-private "manager" classes 
 * in charge of managing the state. However, these manager classes
 * are not responsible for notifying observers.
 */
public final class GameModel implements GameModelView
{
	private static final GameModel INSTANCE = new GameModel();
	
	private static final Move NULL_MOVE = new Move()
	{
		@Override
		public void perform()
		{} // Does nothing on purpose

		@Override
		public boolean isNull()
		{
			return true;
		}

		@Override
		public void undo()
		{} // Does nothing on purpose
	};
	
	private final Move aDiscardMove = new Move()
	{
		@Override
		public void perform()
		{
			assert !isEmptyDeck();	
			aDiscard.push(aDeck.draw());
			aMoves.push(this);
			notifyListeners();
		}

		@Override
		public boolean isNull()
		{
			return false;
		}

		@Override
		public void undo()
		{
			assert !isEmptyDiscardPile();
			aDeck.push(aDiscard.pop());
			notifyListeners();
		}
	};
	
	private Deck aDeck = new Deck();
	private Stack<Move> aMoves = new Stack<>();
	private Stack<Card> aDiscard = new Stack<>();
	private SuitStackManager aSuitStacks = new SuitStackManager();
	private WorkingStackManager aWorkingStacks = new WorkingStackManager();
	private List<GameModelListener> aListeners = new ArrayList<>();
	private PlayingStrategy aPlayingStrategy = new GreedyPlayingStrategy();
	
	/**
	 * Represents anywhere a card can be placed in 
	 * Solitaire.
	 */
	public interface Location 
	{}
	
	/**
	 * Places where a card can be obtained.
	 */
	public enum CardSources implements Location
	{ DISCARD_PILE  }
	
	/**
	 * Represents the different stacks where cards
	 * can be accumulated.
	 */
	public enum StackIndex implements Location
	{ FIRST, SECOND, THIRD, FOURTH, FIFTH, SIXTH, SEVENTH }
	
	/**
	 * Represents the different stacks where completed
	 * suits can be accumulated.
	 */
	public enum SuitStackIndex implements Location
	{
		FIRST, SECOND, THIRD, FOURTH;
	}
	
	private GameModel()
	{
		reset();
	}
	
	/**
	 * @return The number of cards in the suit stacks.
	 */
	public int getScore()
	{
		return aSuitStacks.getScore();
	}
	
	/**
	 * Try to automatically make a move. 
	 * This may result in nothing happening
	 * if the auto-play strategy cannot make a 
	 * decision.
	 * @return whether a move was performed or not.
	 */
	public boolean tryToAutoPlay()
	{
		Move move = aPlayingStrategy.computeNextMove(this);
		move.perform();
		return !move.isNull();
	}
	
	/**
	 * @return The singleton instance for this class.
	 */
	public static GameModel instance()
	{
		return INSTANCE;
	}
	
	/**
	 * Registers an observer for the state of the game model.
	 * @param pListener A listener to register.
	 */
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
	
	/**
	 * Restores the model to the state 
	 * corresponding to the start of a new game.
	 */
	public void reset()
	{
		aMoves.clear();
		aDeck.shuffle();
		aDiscard.clear();
		aSuitStacks.initialize();
		aWorkingStacks.initialize(aDeck);
		notifyListeners();
	}
	
	/**
	 * @return True if the game is completed.
	 */
	public boolean isCompleted()
	{
		return aSuitStacks.getScore() == Rank.values().length * Suit.values().length;
	}
	
	@Override
	public boolean isEmptyDeck()
	{
		return aDeck.size() == 0;
	}
	
	@Override
	public boolean isEmptyDiscardPile()
	{
		return aDiscard.size() == 0;
	}
	
	@Override
	public boolean isEmptySuitStack(SuitStackIndex pIndex)
	{
		return aSuitStacks.isEmpty(pIndex);
	}
	
	/**
	 * Obtain the card on top of the suit stack for
	 * pIndex without discarding it.
	 * @param pIndex The index of the stack to check
	 * @return The card on top of the stack.
	 * @pre !isEmptySuitStack(pIndex)
	 */
	public Card peekSuitStack(SuitStackIndex pIndex)
	{
		assert !isEmptySuitStack(pIndex);
		return aSuitStacks.peek(pIndex);
	}
	
	@Override
	public Card peekDiscardPile()
	{
		assert aDiscard.size() != 0;
		return aDiscard.peek();
	}
	
	/**
	 * @param pCard A card to locate
	 * @return The game location where this card currently is.
	 * @pre the card is in a location where it can be found and moved.
	 */
	private Location find(Card pCard)
	{
		if( !aDiscard.isEmpty() && aDiscard.peek() == pCard )
		{
			return CardSources.DISCARD_PILE;
		}
		for( SuitStackIndex index : SuitStackIndex.values() )
		{
			if( !aSuitStacks.isEmpty(index) && aSuitStacks.peek(index) == pCard )
			{
				return index;
			}
		}
		for( StackIndex index : StackIndex.values() )
		{
			if( aWorkingStacks.contains(pCard, index))
			{
				return index;
			}
		}
		
		assert false; // We did not find the card: the precondition was not met.
		return null;
	}
	
	/**
	 * Undoes the last move.
	 */
	public void undoLast()
	{
		if( !aMoves.isEmpty() )
		{
			aMoves.pop().undo();
		}
	}
	
	/**
	 * @return If there is a move to undo.
	 */
	public boolean canUndo()
	{
		return !aMoves.isEmpty();
	}
	
	/*
	 * Removes the moveable card from pLocation.
	 */
	private void absorbCard(Location pLocation)
	{
		if( pLocation == CardSources.DISCARD_PILE )
		{
			assert !aDiscard.isEmpty();
			aDiscard.pop();
		}
		else if( pLocation instanceof SuitStackIndex )
		{
			assert aSuitStacks.isEmpty((SuitStackIndex)pLocation);
			aSuitStacks.pop((SuitStackIndex)pLocation);
		}
		else
		{
			assert pLocation instanceof StackIndex;
			aWorkingStacks.pop((StackIndex)pLocation);
		}
	}
	
	private void move(Card pCard, Location pDestination)
	{
		Location source = find(pCard);
		if( source instanceof StackIndex && pDestination instanceof StackIndex )
		{
			aWorkingStacks.moveWithin(pCard, (StackIndex)source, (StackIndex) pDestination);
		}
		else
		{
			absorbCard(source);
			if( pDestination instanceof SuitStackIndex )
			{
				aSuitStacks.push(pCard, (SuitStackIndex)pDestination);
			}
			else if( pDestination == CardSources.DISCARD_PILE )
			{
				aDiscard.push(pCard);
			}
			else
			{
				assert pDestination instanceof StackIndex;
				aWorkingStacks.push(pCard, (StackIndex)pDestination);
			}
		}
		notifyListeners();
	}
	
	@Override
	public Card[] getStack(StackIndex pIndex)
	{
		return aWorkingStacks.getStack(pIndex); 
	}
	
	@Override
	public boolean isVisibleInWorkingStack(Card pCard)
	{
		return aWorkingStacks.contains(pCard) && aWorkingStacks.isVisible(pCard);
	}
	
	/**
	 * Get the sub-stack consisting of pCard and all 
	 * the other cards below it.
	 * @param pCard The top card of the sub-stack
	 * @param pIndex The position of the stack to return.
	 * @return A non-empty sequence of cards.
	 * @pre pCard is in stack pIndex
	 */
	public Card[] getSubStack(Card pCard, StackIndex pIndex)
	{
		return aWorkingStacks.getSequence(pCard, pIndex);
	}

	@Override
	public boolean isLegalMove(Card pCard, Location pDestination )
	{ 
		if( pDestination instanceof SuitStackIndex )
		{
			return aSuitStacks.canMoveTo(pCard, (SuitStackIndex) pDestination);
		}
		else if( pDestination instanceof StackIndex )
		{
			return aWorkingStacks.canMoveTo(pCard, (StackIndex) pDestination);
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public Move getNullMove()
	{
		return NULL_MOVE;
	}
	
	@Override
	public Move getDiscardMove()
	{
		return aDiscardMove;
	}
	
	@Override
	public Move getCardMove(Card pCard, Location pDestination)
	{
		Location source = find( pCard );
		if( source instanceof StackIndex  && aWorkingStacks.revealsTop(pCard, (StackIndex)source))
		{
			return new CompositeMove(new CardMove(pCard, pDestination), new RevealTopMove((StackIndex)source) );
		}
		return new CardMove(pCard, pDestination);
	} 
	
	/**
	 * A move that represents the intention to move pCard
	 * to pDestination, possibly including all cards stacked
	 * on top of pCard if pCard is in a working stack.
	 */
	private class CardMove implements Move
	{
		private Card aCard;
		private Location aOrigin; 
		private Location aDestination; 
		
		CardMove(Card pCard, Location pDestination)
		{
			aCard = pCard;
			aDestination = pDestination;
			aOrigin = find(pCard);
		}

		@Override
		public void perform()
		{
			assert isLegalMove(aCard, aDestination);
			move(aCard, aDestination);
			aMoves.push(this);
		}

		@Override
		public boolean isNull()
		{
			return false;
		}

		@Override
		public void undo()
		{
			move(aCard, aOrigin);
		}
	}
	
	/**
	 * reveals the top of the stack.
	 *
	 */
	private class RevealTopMove implements Move
	{
		private final StackIndex aIndex;
		
		RevealTopMove(StackIndex pIndex)
		{
			aIndex = pIndex;
		}
		
		@Override
		public void perform()
		{
			aWorkingStacks.showTop(aIndex);
			aMoves.push(this);
			notifyListeners();
		}

		@Override
		public boolean isNull()
		{
			return false;
		}

		@Override
		public void undo()
		{
			aWorkingStacks.hideTop(aIndex);
			aMoves.pop().undo();
			notifyListeners();
		}
	}
}
