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

import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.model.GameModel.Location;
import ca.mcgill.cs.stg.solitaire.model.GameModel.StackIndex;
import ca.mcgill.cs.stg.solitaire.model.GameModel.SuitStackIndex;

/**
 * A read-only version of a game model.
 */
public interface GameModelView
{
	/**
	 * @return True if the discard pile has no card in it.
	 */
	boolean isEmptyDiscardPile();
	
	/**
	 * @return True if the deck has no card left in it.
	 */
	boolean isEmptyDeck();
	
	/**
	 * @param pIndex The suit stack to check
	 * @return True if the suit stack for pSuit is empty
	 */
	boolean isEmptySuitStack(SuitStackIndex pIndex);
	
	/**
	 * @return The card on top of the discard pile.
	 * @pre !emptyDiscardPile()
	 */
	Card peekDiscardPile();
	
	/**
	 * @param pIndex The position of the stack to return.
	 * @return A copy of the stack at position pIndex
	 */
	CardView[] getStack(StackIndex pIndex);
	
	/**
	 * Determines if pCard can be moved to pLocation
	 * according to the rules of the game and given the current
	 * game state. 
	 * @param pCard The card to move. 
	 * @param pDestination The destination of the move.
	 * @return True if the move is a legal move.
	 */
	boolean isLegalMove(Card pCard, Location pDestination );
}
