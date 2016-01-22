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

/**
 * Represents a single card-playing action
 * in solitaire.
 */
public class CardMove implements Move
{
	private Card aCard;
	private Location aDestination; 
	
	/**
	 * A move that represents the intention to move pCard
	 * to pDestination, possibly including all cards stacked
	 * on top of pCard if pCard is in a working stack.
	 * @param pCard The card to move.
	 * @param pDestination The destination.
	 */
	public CardMove(Card pCard, Location pDestination)
	{
		aCard = pCard;
		aDestination = pDestination;
	}
	
	/**
	 * @return The card to move.
	 */
	public Card getCard()
	{
		return aCard;
	}
	
	/**
	 * @return The destination.
	 */
	public Location getDestination()
	{
		return aDestination;
	}
	
	@Override
	public String toString()
	{
		return aCard + " to " + aDestination.getClass().getSimpleName() + "."+ aDestination;
	}

	@Override
	public void perform(GameModel pModel)
	{
		assert pModel.isLegalMove(aCard, aDestination);
		pModel.move(aCard, aDestination);
	}
}
