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
package ca.mcgill.cs.stg.solitaire.gui;

import java.util.HashMap;
import java.util.Map;

import ca.mcgill.cs.stg.solitaire.cards.Card;
import javafx.scene.image.Image;

/**
 * A class to store and manage images of the 52 cards and the back of a card.
 */
public final class CardImages 
{
	private static Map<String, Image> aCards = new HashMap<String, Image>();
	
	private CardImages()
	{}
	
	/**
	 * Return the image of a card.
	 * @param pCard the target card
	 * @return An icon representing the chosen card.
	 */
	public static Image getCard( Card pCard )
	{
		assert pCard != null;
		return getCard( getFileNameFor( pCard ) );
	}
	
	private static Image getCard( String pCode )
	{
		Image image = aCards.get( pCode );
		if( image == null )
		{
			image = new Image(CardImages.class.getClassLoader().getResourceAsStream( pCode ));
			aCards.put( pCode, image );
		}
		return image;
	}
	
	/**
	 * Return an image of the back of a card.
	 * @return An icon representing the back of a card.
	 */
	public static Image getBack()
	{
		return getCard( "back.gif" );
	}
	
	private static String getFileNameFor( Card pCard )
	{
		return String.format("%s-of-%s.gif", pCard.getRank().name().toLowerCase(), 
				pCard.getSuit().name().toLowerCase());
	}
}
