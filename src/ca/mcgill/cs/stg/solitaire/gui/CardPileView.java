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
package ca.mcgill.cs.stg.solitaire.gui;

import ca.mcgill.cs.stg.solitaire.cards.Card;
import ca.mcgill.cs.stg.solitaire.cards.CardImages;
import ca.mcgill.cs.stg.solitaire.cards.CardStack;
import ca.mcgill.cs.stg.solitaire.model.GameModel;
import ca.mcgill.cs.stg.solitaire.model.GameModelListener;
import ca.mcgill.cs.stg.solitaire.model.TableauPile;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

/**
 * Component that shows a stack of cards in 
 * the bottom stacks.
 */
public class CardPileView extends StackPane implements GameModelListener
{
	private static final int PADDING = 5;
	private static final int Y_OFFSET = 17;
	
	private TableauPile aIndex;
	
	CardPileView(TableauPile pIndex)
	{
		aIndex = pIndex;
		setPadding(new Insets(PADDING));
    	setAlignment(Pos.TOP_CENTER);
    	buildLayout();
    	GameModel.instance().addListener(this);
	}
	
	private static Image getImage(Card pCard)
	{
		if( GameModel.instance().isVisibleInTableau(pCard) )
		{
			return CardImages.getCard(pCard);
		}
		else
		{
			return CardImages.getBack();
		}
	}
	
	private void buildLayout()
    {
		getChildren().clear();
		
		int offset = 0;
		CardStack stack = GameModel.instance().getTableauPile(aIndex);
		if( stack.isEmpty() ) // this essentially acts as a spacer
		{
			ImageView image = new ImageView(CardImages.getBack());
			image.setVisible(false);
			getChildren().add(image);
			return;
		}
		
		for( Card cardView : stack)
		{
			final ImageView image = new ImageView(getImage(cardView));
        	image.setTranslateY(Y_OFFSET * offset);
        	offset++;
        	getChildren().add(image);
        
        	setOnDragOver(createDragOverHandler(image, cardView));
    		setOnDragEntered(createDragEnteredHandler(image, cardView));
    		setOnDragExited(createDragExitedHandler(image, cardView));
    		setOnDragDropped(createDragDroppedHandler(image, cardView));
    		
        	if( GameModel.instance().isVisibleInTableau(cardView))
        	{
        		image.setOnDragDetected(createDragDetectedHandler(image, cardView));
        	}
		}
    }
	
	private EventHandler<MouseEvent> createDragDetectedHandler(final ImageView pImageView, final Card pCard)
	{
		return new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent pMouseEvent) 
			{
				Dragboard db = pImageView.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				content.putString(CardTransfer.serialize(GameModel.instance().getSubStack(pCard, aIndex)));
				db.setContent(content);
				pMouseEvent.consume();
			}
		};
	}
	
	private EventHandler<DragEvent> createDragOverHandler(final ImageView pImageView, final Card pCard)
	{
		return new EventHandler<DragEvent>()
		{
			@Override
			public void handle(DragEvent pEvent) 
			{
				if(pEvent.getGestureSource() != pImageView && pEvent.getDragboard().hasString())
				{
					CardTransfer transfer = new CardTransfer(pEvent.getDragboard().getString());
					if( GameModel.instance().isLegalMove(transfer.getTop(), aIndex) )
					{
						pEvent.acceptTransferModes(TransferMode.MOVE);
					}
				}
				pEvent.consume();
			}
		};
	}
	
	private EventHandler<DragEvent> createDragEnteredHandler(final ImageView pImageView, final Card pCard)
	{
		return new EventHandler<DragEvent>()
		{
			@Override
			public void handle(DragEvent pEvent)
			{
				CardTransfer transfer = new CardTransfer(pEvent.getDragboard().getString());
				if( GameModel.instance().isLegalMove(transfer.getTop(), aIndex) )
				{
					pImageView.setEffect(new DropShadow());
				}
				pEvent.consume();
			}
		};
	}
	
	private EventHandler<DragEvent> createDragExitedHandler(final ImageView pImageView, final Card pCard)
	{
		return new EventHandler<DragEvent>()
		{
			@Override
			public void handle(DragEvent pEvent)
			{
				pImageView.setEffect(null);
				pEvent.consume();
			}
		};
	}
	
	private EventHandler<DragEvent> createDragDroppedHandler(final ImageView pImageView, final Card pCard)
	{
		return new EventHandler<DragEvent>() 
		{
			@Override
			public void handle(DragEvent pEvent)
			{
				Dragboard db = pEvent.getDragboard();
				boolean success = false;
				if(db.hasString()) 
				{
					GameModel.instance().getCardMove(new CardTransfer(db.getString()).getTop(), aIndex).perform(); 
					success = true;
				}

				pEvent.setDropCompleted(success);

				pEvent.consume();
			}
		};
	}

	@Override
	public void gameStateChanged()
	{
		buildLayout();
	}
}