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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import ca.mcgill.cs.stg.solitaire.cards.Card.Suit;
import ca.mcgill.cs.stg.solitaire.model.GameModel;
import ca.mcgill.cs.stg.solitaire.model.GameModel.StackIndex;
import ca.mcgill.cs.stg.solitaire.model.GameModel.SuitStackIndex;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Application class for Solitaire. The responsibility
 * of this class is limited to assembling the major UI components 
 * and launching the application. All gesture handling logic is 
 * handled by its composed elements, which act as observers
 * of the game model.
 */
public class Solitaire extends Application
{
    private static MediaPlayer player = null;

	private static final int WIDTH = 680;
	private static final int HEIGHT = 500;
	private static final int MARGIN_OUTER = 10;
	private static final String TITLE = "Solitaire";
	private static final String VERSION = "0.3";

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
                
        for( SuitStackIndex index : SuitStackIndex.values() )
        {
        	aSuitStacks[index.ordinal()] = new SuitStack(index);
        	root.add(aSuitStacks[index.ordinal()], 3+index.ordinal(), 0);
        }
      
        for( StackIndex index : StackIndex.values() )
        {
        	aStacks[index.ordinal()] = new CardStack(index);
        	root.add(aStacks[index.ordinal()], index.ordinal(), 1);
        }
        
        final Timeline timeline = new Timeline(
			    new KeyFrame(
			      Duration.ZERO,
			      new EventHandler<ActionEvent>()
			      {
					@Override
					public void handle(ActionEvent pEvent)
					{
						GameModel.instance().tryToAutoPlay();
					}
					}
			    ),
			    new KeyFrame(Duration.seconds(2)
			    )
			);
		timeline.setCycleCount(Timeline.INDEFINITE);
        
        root.setOnKeyTyped(new EventHandler<KeyEvent>()
		{
        	
			@Override
			public void handle(final KeyEvent pEvent)
			{
				if( pEvent.getCharacter().equals("\r"))
				{
					System.out.println("Enter" + Thread.currentThread().getName());
					GameModel.instance().tryToAutoPlay();
				}
				else if( pEvent.getCharacter().equals("\b"))
				{
					GameModel.instance().undoLast();
				}
				else if( pEvent.getCharacter().equals("a"))
				{
					if( timeline.getCurrentRate() == 0 )
					{
						timeline.play();
						aDeckView.animate(true);
						if( player == null )
						{
							play();
						}
						else
						{
							player.play();
						}
					}
					else
					{
						timeline.pause();
						aDeckView.animate(false);
						player.pause();
					}
				}
				pEvent.consume();
			}
        	
		});
        
        pPrimaryStage.setResizable(false);
        pPrimaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        pPrimaryStage.show();
    }
    
    /**
     * Starts playing the music.
     */
    public static void play()
    {
    	try
    	{
    		URL thing = new File("C:\\temp\\georgia.mp3").toURI().toURL();
    		Media audioFile = new Media( thing.toString() ); 
    		player = new MediaPlayer(audioFile);
            player.play();   
    	}
    	catch( MalformedURLException e )
    	{
    		e.printStackTrace();
    		System.exit(0);
    	}
    }
}

