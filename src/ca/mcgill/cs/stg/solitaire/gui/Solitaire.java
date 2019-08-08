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

import ca.mcgill.cs.stg.solitaire.cards.Suit;
import ca.mcgill.cs.stg.solitaire.model.GameModel;
import ca.mcgill.cs.stg.solitaire.model.FoundationPile;
import ca.mcgill.cs.stg.solitaire.model.TableauPile;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
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
	private static final String VERSION = "1.0";

    private DeckView aDeckView = new DeckView();
    private DiscardPileView aDiscardPileView = new DiscardPileView();
    private SuitStack[] aSuitStacks = new SuitStack[Suit.values().length];
    private CardPileView[] aStacks = new CardPileView[TableauPile.values().length];
    
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
                
        for( FoundationPile index : FoundationPile.values() )
        {
        	aSuitStacks[index.ordinal()] = new SuitStack(index);
        	root.add(aSuitStacks[index.ordinal()], 3+index.ordinal(), 0);
        }
      
        for( TableauPile index : TableauPile.values() )
        {
        	aStacks[index.ordinal()] = new CardPileView(index);
        	root.add(aStacks[index.ordinal()], index.ordinal(), 1);
        }
        
        root.setOnKeyTyped(new EventHandler<KeyEvent>()
		{

			@Override
			public void handle(final KeyEvent pEvent)
			{
				if( pEvent.getCharacter().equals("\r"))
				{
					GameModel.instance().tryToAutoPlay();
				}
				else if( pEvent.getCharacter().equals("\b"))
				{
					GameModel.instance().undoLast();
				}
				pEvent.consume();
			}
        	
		});
        
        pPrimaryStage.setResizable(false);
        pPrimaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        pPrimaryStage.show();
    }
}

