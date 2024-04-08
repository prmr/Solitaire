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

import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.cards.CardSerializer;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

/**
 * Stores a string representing the card dragged.
 */
public class CardDragHandler implements EventHandler<MouseEvent>
{
	private static final ClipboardContent CLIPBOARD_CONTENT = new ClipboardContent();
	
	private Card aCard;
	private ImageView aImageView;
	
	CardDragHandler( ImageView pView )
	{
		aImageView = pView;
	}
	
	void setCard(Card pCard)
	{
		aCard = pCard;
	}
	
	@Override
	public void handle(MouseEvent pMouseEvent)
	{
		Dragboard db = aImageView.startDragAndDrop(TransferMode.ANY);
        CLIPBOARD_CONTENT.putString(CardSerializer.serialize(aCard));
        db.setContent(CLIPBOARD_CONTENT);
        pMouseEvent.consume();
	}
}