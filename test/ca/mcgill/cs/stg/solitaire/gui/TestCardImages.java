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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.cards.Rank;
import ca.mcgill.cs.stg.solitaire.cards.Suit;
import javafx.scene.image.Image;

public class TestCardImages
{
	/*
	 * Checks that the card back image can be loaded and that it is a
	 * flyweight. 
	 */
	@Test
	void testimageForBackOfCard()
	{
		assertSame(CardImages.imageForBackOfCard(), 
				CardImages.imageForBackOfCard());
		assertNotNull(CardImages.imageForBackOfCard());
	}
	
	/*
	 * Checks that the image of each card can be loaded and that they are
	 * flyweights. 
	 */
	@ParameterizedTest
	@MethodSource("provideAllCards")
	void testimageForCard( Card pCard )
	{
		try 
		{
			Image image1 = CardImages.imageFor( pCard );
			Image image2 = CardImages.imageFor( pCard );
			assertSame(image1, image2 );
		}
		catch( NullPointerException e )
		{
			throw new AssertionError(String.format("Image for %s cannot be loaded", pCard));
		}
	}
	
	private static List<Card> provideAllCards() 
	{
	    List<Card> allCards = new ArrayList<>();
	    for( Suit suit : Suit.values() )
	    {
	    	for( Rank rank : Rank.values() )
	    	{
	    		allCards.add(Card.get(rank, suit));
	    	}
	    }
	    return allCards;
	}
}
