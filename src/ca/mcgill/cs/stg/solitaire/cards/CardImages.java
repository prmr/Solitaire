package ca.mcgill.cs.stg.solitaire.cards;


import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

/**
 * A class to store and manage images of the 52 cards.
 */
public final class CardImages 
{
	private static final String IMAGE_LOCATION = "";
	private static final String IMAGE_SUFFIX = ".gif";
	private static final String[] RANK_CODES = {"a", "2", "3", "4", "5", "6", "7", "8", "9", "t", "j", "q", "k"};
	private static final String[] SUIT_CODES = {"c", "d", "h", "s"};	
	
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
		return getCard( getCode( pCard ) );
	}
	
	/**
	 * Return an image of the back of a card.
	 * @return An icon representing the back of a card.
	 */
	public static Image getBack()
	{
		return getCard( "b" );
	}
	
	private static String getCode( Card pCard )
	{
		return RANK_CODES[ pCard.getRank().ordinal() ] + SUIT_CODES[ pCard.getSuit().ordinal() ];		
	}
	
	private static Image getCard( String pCode )
	{
		Image image = (Image) aCards.get( pCode );
		if( image == null )
		{
			image = new Image(CardImages.class.getClassLoader().getResourceAsStream( IMAGE_LOCATION + pCode + IMAGE_SUFFIX ));
			aCards.put( pCode, image );
		}
		return image;
	}
}
