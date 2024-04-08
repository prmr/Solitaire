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

import java.util.IdentityHashMap;
import java.util.Map;

import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.cards.Deck;
import javafx.scene.image.Image;

/**
 * A class to store and manage images of the 52 cards and the back of a card.
 * 
 * To function properly, all 53 images must be accessible from a folder at
 * the root of the path from which the class file for CardImages is loaded.
 * If a file is missing or corrupted, launching the application will fail
 * with "Exception in Application start method" indirectly caused by
 * an ExceptionInInitializerError because this class will fail to 
 * initialize.
 */
public final class CardImages 
{
	private static final Image CARD_BACK = loadBackImage();
	private static final Map<Card, Image> CARD_IMAGES = loadCardImages();
	
	private CardImages()
	{}
	
	/**
	 * Return the image of a card.
	 * @param pCard the target card
	 * @return An icon representing the chosen card.
	 */
	public static Image imageFor( Card pCard )
	{
		assert pCard != null;
		return CARD_IMAGES.get(pCard);
	}
	
	/**
	 * Return an image of the back of a card.
	 * @return An icon representing the back of a card.
	 */
	public static Image imageForBackOfCard()
	{
		return CARD_BACK;
	}
	
	private static Image loadBackImage()
	{
		return new Image(CardImages.class.getClassLoader()
				.getResourceAsStream( "back.gif" ));
	}
	
	/*
	 * Loads images for all 52 cards in a map. Assumes that card objects are 
	 * flyweights.
	 */
	private static Map<Card, Image> loadCardImages()
	{
		Deck deck = new Deck();
		Map<Card, Image> images = new IdentityHashMap<>();
		while( !deck.isEmpty() )
		{
			Card card = deck.draw();
			Image image = new Image(CardImages.class.getClassLoader()
					.getResourceAsStream( getFileNameFor(card) ));
			images.put(card, image);
		}
		return images;
	}
	
	private static String getFileNameFor( Card pCard )
	{
		return String.format("%s-of-%s.gif", pCard.rank().name().toLowerCase(), 
				pCard.suit().name().toLowerCase());
	}
}
