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

import ca.mcgill.solitaire.auto.GreedyPlayingStrategy;
import ca.mcgill.solitaire.model.FoundationPile;
import ca.mcgill.solitaire.model.GameModel;
import ca.mcgill.solitaire.model.TableauPile;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Application class for Solitaire. The responsibility of this class is limited
 * to assembling the major UI components and launching the application. All
 * gesture handling logic is handled by its composed elements, which act as
 * observers of the game model.
 */
public class Solitaire extends Application {
	private static final int WIDTH = 680;
	private static final int HEIGHT = 500;
	private static final int MARGIN_OUTER = 10;
	private static final String TITLE = "Solitaire";
	private static final String VERSION = "1.3";

	/**
	 * Application head.
	 */
	public Solitaire() {}

	/**
	 * Launches the application.
	 * 
	 * @param pArgs This program takes no argument.
	 */
	public static void main(String[] pArgs) {
		launch(pArgs);
	}

	@Override
	public void start(Stage pPrimaryStage) {
		pPrimaryStage.setTitle(TITLE + " " + VERSION);

		GridPane root = new GridPane();
		root.setStyle("-fx-background-color: green;");
		root.setHgap(MARGIN_OUTER);
		root.setVgap(MARGIN_OUTER);
		root.setPadding(new Insets(MARGIN_OUTER));

		final GameModel model = new GameModel(new GreedyPlayingStrategy());
		DeckView deckView = new DeckView(model);
		DiscardPileView discardPileView = new DiscardPileView(model);

		root.add(deckView, 0, 0);
		root.add(discardPileView, 1, 0);

		for (FoundationPile index : FoundationPile.values()) {
			root.add(new SuitStack(model, index), 3 + index.ordinal(), 0);
		}

		for (TableauPile index : TableauPile.values()) {
			root.add(new CardPileView(model, index), index.ordinal(), 1);
		}

		root.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(final KeyEvent pEvent) {
				if (pEvent.getCharacter().equals("\r")) {
					model.tryToAutoPlay();
				}
				else if (pEvent.getCharacter().equals("\b")) {
					model.undoLast();
				}
				pEvent.consume();
			}
		});

		pPrimaryStage.setResizable(false);
		pPrimaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
		pPrimaryStage.show();
	}
}