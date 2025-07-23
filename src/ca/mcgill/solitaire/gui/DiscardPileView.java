/*******************************************************************************
 * Solitaire
 * 
 * Copyright (C) 2025 by Martin P. Robillard
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
import ca.mcgill.solitaire.model.GameModel;
import ca.mcgill.solitaire.model.GameModelListener;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Component that shows the state of the discard pile and allows dragging cards
 * from it.
 */
class DiscardPileView extends HBox implements GameModelListener {
	private static final int PADDING = 5;
	private CardDragHandler aDragHandler;
	private final GameModel aModel;

	DiscardPileView(GameModel pModel) {
		aModel = pModel;
		setPadding(new Insets(PADDING));
		final ImageView image = new ImageView(CardImages.imageForBackOfCard());
		image.setVisible(false);
		getChildren().add(image);
		aDragHandler = new CardDragHandler(image);
		image.setOnDragDetected(aDragHandler);
		aModel.addListener(this);
	}

	@Override
	public void gameStateChanged() {
		if (aModel.isDiscardPileEmpty()) {
			getChildren().get(0).setVisible(false);
		}
		else {
			getChildren().get(0).setVisible(true);
			Card topCard = aModel.peekDiscardPile();
			ImageView image = (ImageView) getChildren().get(0);
			image.setImage(CardImages.imageFor(topCard));
			aDragHandler.setCard(topCard);
		}
	}
}