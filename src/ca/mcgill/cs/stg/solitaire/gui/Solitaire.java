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
import ca.mcgill.cs.stg.solitaire.cards.Card.Suit;
import ca.mcgill.cs.stg.solitaire.model.GameModel.StackIndex;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Application class for Solitaire. The responsibility
 * of this class is limited to assembling the major UI components 
 * and launching the application. All gesture handling logic is 
 * handled by its composed elements, which act as observers
 * of the game model.
 */
public class Solitaire extends Application
{
	private static final int WIDTH = 680;
	private static final int HEIGHT = 500;
	private static final int MARGIN_OUTER = 10;
	private static final String TITLE = "Solitaire";
	private static final String VERSION = "0.1";

    private DeckView aDeckView = new DeckView();
    private DiscardPileView aDiscardPileView = new DiscardPileView();
    private SuitStack[] aSuitStacks = new SuitStack[Suit.values().length];
    private CardStack[] aStacks = new CardStack[StackIndex.values().length];
    
	/**
	 * Launches the application.
	 * @param pArgs This program takes no argument.
	 */
	public static void main(String[] pArgs) 
	{
        launch(pArgs);
    }
    
    @Override
    public void start(Stage pPrimaryStage) 
    {
		pPrimaryStage.setTitle(TITLE + " " + VERSION); 
           
        GridPane root = new GridPane();
        root.setStyle("-fx-background-color: green;");
        root.setHgap(MARGIN_OUTER);
        root.setVgap(MARGIN_OUTER);
        root.setPadding(new Insets(MARGIN_OUTER));
        
        root.add(aDeckView, 0, 0);
        root.add(aDiscardPileView, 1, 0);
                
        for( Card.Suit suit : Card.Suit.values() )
        {
        	aSuitStacks[suit.ordinal()] = new SuitStack(suit);
        	root.add(aSuitStacks[suit.ordinal()], 3+suit.ordinal(), 0);
        }
      
        for( StackIndex index : StackIndex.values() )
        {
        	aStacks[index.ordinal()] = new CardStack(index);
        	root.add(aStacks[index.ordinal()], index.ordinal(), 1);
        }
                    
        pPrimaryStage.setResizable(false);
        pPrimaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        pPrimaryStage.show();
    }
}

