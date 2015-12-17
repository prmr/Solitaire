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
import ca.mcgill.cs.stg.solitaire.cards.CardImages;
import javafx.scene.image.Image;

/**
 * A mutable wrapper around a card that keeps
 * track of whether the value of the card is 
 * visible to a user or not.
 */
public class CardView
{
	private Card aCard;
	private boolean aVisible;
	
	/**
	 * Create a view of a card that is not
	 * visible by default.
	 * @param pCard The card to wrap.
	 */
	public CardView(Card pCard)
	{
		assert pCard != null;
		aCard = pCard;
		aVisible = false;
	}
	
	/**
	 * Make the card permanently visible.
	 */
	public void makeVisible()
	{
		aVisible = true;
	}
	
	/**
	 * @return The image of the card, either 
	 * the value side of the card if it is visible,
	 * or the back of the card if the card is not visible.
	 */
	public Image getImage()
	{
		if( !aVisible )
		{
			return CardImages.getBack();
		}
		else
		{
			return CardImages.getCard(aCard);
		}
	}
	
	/**
	 * @return The underlying card for this view.
	 */
	public Card getCard()
	{
		return aCard;
	}
}
