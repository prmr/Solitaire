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

import ca.mcgill.cs.stg.solitaire.cards.CardImages;
import ca.mcgill.cs.stg.solitaire.model.GameModel;
import ca.mcgill.cs.stg.solitaire.model.GameModelListener;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * Component that shows the deck and allows clicking
 * it to draw cards. Listens to game model state changes
 * and updates itself to disappear if it is empty.
 */
class DeckView extends HBox implements GameModelListener
{
	private static final String BUTTON_STYLE_NORMAL = "-fx-background-color: transparent; -fx-padding: 5, 5, 5, 5;";
    private static final String BUTTON_STYLE_PRESSED = "-fx-background-color: transparent; -fx-padding: 6 4 4 6;";
    private static final int IMAGE_NEW_LINE_WIDTH = 10;
    private static final int IMAGE_FONT_SIZE = 15;
    private static final ColorAdjust ADJUST = new ColorAdjust();
    
    static 
    {
    	ADJUST.setBrightness(0.5);
    }
    
    private final Timeline aTimeLine;
	
	DeckView()
	{
        final Button button = new Button();
        
        aTimeLine = new Timeline(
    		    new KeyFrame(
    		      Duration.ZERO,
    		      new EventHandler<ActionEvent>(){
    		    	  
    		    	  private boolean aOn = true;
    				@Override
    				public void handle(ActionEvent pEvent)
    				{
    					if(aOn)
    					{
    						aOn = false;
    						button.getGraphic().setEffect(ADJUST);
    					}
    					else
    					{
    						aOn = true;
    						button.getGraphic().setEffect(null);
    					}
    				}
    				}
    		    ),
    		    new KeyFrame(Duration.seconds(0.6)
    		    )
    		);
    	aTimeLine.setCycleCount(Timeline.INDEFINITE);
        button.setGraphic(new ImageView(CardImages.getBack()));
        button.setStyle(BUTTON_STYLE_NORMAL);

    	button.setOnMousePressed(new EventHandler<MouseEvent>() 
    	{
    		@Override
    		public void handle(MouseEvent pEvent) 
    		{
    			((Button)pEvent.getSource()).setStyle(BUTTON_STYLE_PRESSED);
    		}            
    	});

    	button.setOnMouseReleased(new EventHandler<MouseEvent>() 
    	{
    		@Override
    		public void handle(MouseEvent pEvent) 
    		{
    			((Button)pEvent.getSource()).setStyle(BUTTON_STYLE_NORMAL);
    			if( GameModel.instance().isEmptyDeck() )
    			{
    				GameModel.instance().reset();
    			}
    			else
    			{
    				GameModel.instance().getDiscardMove().perform();
    			}
    		}            
    	});
        
        getChildren().add(button);
    	GameModel.instance().addListener(this);
	}
	
	public void animate(boolean pOn)
	{
		if( pOn )
		{
			aTimeLine.play();
		}
		else
		{
			aTimeLine.pause();
		}
	}
	
	private Canvas createNewGameImage()
	{
		double width = CardImages.getBack().getWidth();
		double height = CardImages.getBack().getHeight();
		Canvas canvas = new Canvas( width, height );
		GraphicsContext context = canvas.getGraphicsContext2D();
		
		// The reset image
		context.setStroke(Color.DARKGREEN);
		context.setLineWidth(IMAGE_NEW_LINE_WIDTH);
		context.strokeOval(width/4, height/2-width/4 + IMAGE_FONT_SIZE, width/2, width/2);

		// The text
		
		context.setTextAlign(TextAlignment.CENTER);
		context.setTextBaseline(VPos.CENTER);
		context.setFill(Color.DARKKHAKI);
		context.setFont(Font.font(Font.getDefault().getName(), IMAGE_FONT_SIZE));
		
		
		
		if( GameModel.instance().isCompleted() )
		{
			context.fillText("You won!", Math.round(width/2), IMAGE_FONT_SIZE);
		}
		else
		{
			context.fillText("Give up?", Math.round(width/2), IMAGE_FONT_SIZE);
		}
		context.setTextAlign(TextAlignment.CENTER);
		return canvas;
	}
	
	@Override
	public void gameStateChanged()
	{
		if( GameModel.instance().isEmptyDeck() )
		{
			((Button)getChildren().get(0)).setGraphic(createNewGameImage());
		}
		else
		{
			((Button)getChildren().get(0)).setGraphic(new ImageView(CardImages.getBack()));
		}
	}
	
	public void reset()
	{
		getChildren().get(0).setVisible(true);
	}
}