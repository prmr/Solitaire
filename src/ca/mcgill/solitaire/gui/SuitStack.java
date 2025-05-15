/*******************************************************************************
 * Solitaire
 * 
 * Copyright (C) 2016-2024 by Martin P. Robillard
 * 
 * See: https://github.com/prmr/Solitaire
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see http://www.gnu.org/licenses/.
 *******************************************************************************/
package ca.mcgill.solitaire.gui;

import ca.mcgill.solitaire.cards.Card;
import ca.mcgill.solitaire.cards.CardSerializer;
import ca.mcgill.solitaire.cards.CardStack;
import ca.mcgill.solitaire.model.FoundationPile;
import ca.mcgill.solitaire.model.GameModel;
import ca.mcgill.solitaire.model.GameModelListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

/**
 * Component that shows a stack of cards in which a completed suit is
 * accumulated.
 */
public class SuitStack extends StackPane implements GameModelListener {
	private static final int PADDING = 5;

	private static final String BORDER_STYLE = """
			-fx-border-color: lightgray; \
			-fx-border-width: 3; \
			-fx-border-radius: 10.0""";

	private static final String BORDER_STYLE_DRAGGED = """
			-fx-border-color: darkgray; \
			-fx-border-width: 3; \
			-fx-border-radius: 10.0""";

	private static final String BORDER_STYLE_NORMAL = """
			-fx-border-color: lightgray; \
			-fx-border-width: 3;
			-fx-border-radius: 10.0""";

	private CardDragHandler aDragHandler;
	private FoundationPile aIndex;
	private final GameModel aModel;

	SuitStack(GameModel pModel, FoundationPile pIndex) {
		aModel = pModel;
		aIndex = pIndex;
		setPadding(new Insets(PADDING));
		setStyle(BORDER_STYLE);
		final ImageView image = new ImageView(CardImages.imageForBackOfCard());
		image.setVisible(false);
		getChildren().add(image);
		aDragHandler = new CardDragHandler(image);
		image.setOnDragDetected(aDragHandler);
		setOnDragOver(createOnDragOverHandler(image));
		setOnDragEntered(createOnDragEnteredHandler());
		setOnDragExited(createOnDragExitedHandler());
		setOnDragDropped(createOnDragDroppedHandler());
		aModel.addListener(this);
	}

	@Override
	public void gameStateChanged() {
		if (aModel.isFoundationPileEmpty(aIndex)) {
			getChildren().get(0).setVisible(false);
		}
		else {
			getChildren().get(0).setVisible(true);
			Card topCard = aModel.peekSuitStack(aIndex);
			ImageView image = (ImageView) getChildren().get(0);
			image.setImage(CardImages.imageFor(topCard));
			aDragHandler.setCard(topCard);
		}
	}

	private EventHandler<DragEvent> createOnDragOverHandler(final ImageView pView) {
		return new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent pEvent) {
				if (pEvent.getGestureSource() != pView && pEvent.getDragboard().hasString()) {
					CardStack transfer = CardSerializer.deserialize(pEvent.getDragboard().getString());
					if (transfer.size() == 1 && aModel.isLegalMove(transfer.peek(), aIndex)) {
						pEvent.acceptTransferModes(TransferMode.MOVE);
					}
				}

				pEvent.consume();
			}
		};
	}

	private EventHandler<DragEvent> createOnDragEnteredHandler() {
		return new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent pEvent) {
				CardStack transfer = CardSerializer.deserialize(pEvent.getDragboard().getString());
				if (transfer.size() == 1 && aModel.isLegalMove(transfer.peek(), aIndex)) {
					setStyle(BORDER_STYLE_DRAGGED);
				}
				pEvent.consume();
			}
		};
	}

	private EventHandler<DragEvent> createOnDragExitedHandler() {
		return new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent pEvent) {
				setStyle(BORDER_STYLE_NORMAL);
				pEvent.consume();
			}
		};
	}

	private EventHandler<DragEvent> createOnDragDroppedHandler() {
		return new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent pEvent) {
				Dragboard db = pEvent.getDragboard();
				boolean success = false;
				if (db.hasString()) {
					aModel.getCardMove(CardSerializer.deserializeBottomCard(pEvent.getDragboard().getString()), aIndex)
							.perform();
					success = true;
				}
				pEvent.setDropCompleted(success);
				pEvent.consume();
			}
		};
	}
}