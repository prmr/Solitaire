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
package ca.mcgill.cs.stg.solitaire.ai;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.model.CardView;
import ca.mcgill.cs.stg.solitaire.model.GameModel;
import ca.mcgill.cs.stg.solitaire.model.GameModel.StackIndex;
import ca.mcgill.cs.stg.solitaire.model.GameModel.SuitStackIndex;
import ca.mcgill.cs.stg.solitaire.model.Move;

/**
 * Makes the first move in this order: 
 * 0. Discarding if the discard pile is empty
 * 1. Moving a card from the discard pile to a suit stack
 * 2. Moving a card from the discard pile to a working stack
 * 3. Moving a card from a working stack to a suit stack, in order
 * of working stacks.
 * 4. Moving from a working stack to another, if this either reveals
 * a fresh card or frees up a spot for a kind.
 * 5. If no moves are possible, discards.
 */
public class GreedyPlayingStrategy implements PlayingStrategy
{
	@Override
	public void makeAMoveIfPossible(GameModel pModel)
	{
		if( pModel.isEmptyDiscardPile() && !pModel.isEmptyDeck() )
		{
			pModel.discard();
		}
		else
		{
			ArrayList<Move> moves = new ArrayList<>();
			moves.addAll(movesFromDiscardPileToSuitStack(pModel));
			if( moves.size() > 0 )
			{
				consumeMove(moves, pModel);
				return;
			}
			moves.addAll(movesFromDiscardPileToWorkingStacks(pModel));
			if( moves.size() > 0 )
			{
				consumeMove(moves, pModel);
				return;
			}
			moves.addAll(movesFromWorkingStacksToSuitStacks(pModel));
			if( moves.size() > 0 )
			{
				consumeMove(moves, pModel);
				return;
			}
			moves.addAll(movesFromWorkingStacksRevealsCard(pModel));
			if( moves.size() > 0 )
			{
				consumeMove(moves, pModel);
				return;
			}
			if( !pModel.isEmptyDeck() )
			{
				pModel.discard();
			}
		}
	}
	
	/*
	 * if there is a move in the list, consume it and delete it.
	 */
	private void consumeMove(List<Move> pMoves, GameModel pModel)
	{
		assert pMoves.size() > 0;
		pModel.move(pMoves.get(0).getCard(), pMoves.get(0).getDestination());
		pMoves.remove(0);
	}
	
	private List<Move> movesFromDiscardPileToSuitStack(GameModel pModel)
	{
		ArrayList<Move> moves = new ArrayList<>();
		for(SuitStackIndex index : SuitStackIndex.values())
		{
			if( pModel.isLegalMove(pModel.peekDiscardPile(), index))
			{
				moves.add(new Move(pModel.peekDiscardPile(), index));
				if( pModel.isEmptySuitStack(index))
				{
					break; // we take the first possible blank space
				}
			}
		}
		return moves;
	}
	
	private List<Move> movesFromDiscardPileToWorkingStacks(GameModel pModel)
	{
		ArrayList<Move> moves = new ArrayList<>();
		for(StackIndex index : StackIndex.values())
		{
			if( pModel.isLegalMove(pModel.peekDiscardPile(), index))
			{
				moves.add(new Move(pModel.peekDiscardPile(), index));
			}
		}
		return moves;
	}
	
	private List<Move> movesFromWorkingStacksToSuitStacks(GameModel pModel)
	{
		ArrayList<Move> moves = new ArrayList<>();
		for(StackIndex index : StackIndex.values())
		{
			CardView[] stack = pModel.getStack(index);
			if( stack.length > 0 )
			{
				Card card = stack[stack.length-1].getCard();
				for(SuitStackIndex index2 : SuitStackIndex.values())
				{
					if( pModel.isLegalMove(card, index2))
					{
						moves.add(new Move(card, index2));
						if( pModel.isEmptySuitStack(index2))
						{
							break; // we take the first possible blank space
						}
					}
				}
			}	
		}
		return moves;
	}
	
	private List<Move> movesFromWorkingStacksRevealsCard(GameModel pModel)
	{
		ArrayList<Move> moves = new ArrayList<>();
		for(StackIndex index : StackIndex.values())
		{
			CardView[] stack = pModel.getStack(index);
			for(int i = 0; i < stack.length; i++ )
			{
				if( stack[i].isVisible() && i > 0 && !stack[i-1].isVisible() )
				{
					for( StackIndex index2 : StackIndex.values() )
					{
						if( pModel.isLegalMove(stack[i].getCard(), index2))
						{
							moves.add(new Move(stack[i].getCard(), index2));
						}
					}
				}
				else if( stack[i].isVisible() && i == 0 )
				{
					for( StackIndex index2 : StackIndex.values() )
					{
						// we don't want to just move a card around
						if( pModel.isLegalMove(stack[i].getCard(), index2) && pModel.getStack(index2).length > 0) 
						{
							moves.add(new Move(stack[i].getCard(), index2));
						}
					}
				}
			}
		}
		return moves;
	}
}
