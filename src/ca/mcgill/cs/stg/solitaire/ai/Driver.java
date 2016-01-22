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
package ca.mcgill.cs.stg.solitaire.ai;

import ca.mcgill.cs.stg.solitaire.model.GameModel;

/**
 * Plays 100,000 games and computes the 
 * number of wins.
 */
public final class Driver
{
	private static final int NUMBER_OF_GAMES = 1000;
	private static final int TO_PERCENT = 100;
	
	private Driver() {}
	
	/**
	 * @param pArgs Not used.
	 */
	public static void main(String[] pArgs)
	{
		int total = 0;
		int totalWon = 0;
		for( int i = 0; i < NUMBER_OF_GAMES; i++ )
		{
			boolean won = playGame(GameModel.instance());
			total += GameModel.instance().getScore();
			if( won )
			{
				totalWon++;
			}
		}
		System.out.println(String.format("Ratio won     %d/%d=%.1f%%", totalWon, NUMBER_OF_GAMES,
				((double)totalWon)/((double)NUMBER_OF_GAMES)*TO_PERCENT));
		System.out.println(String.format("Average score %d/%d=%.1f", total, NUMBER_OF_GAMES, 
				((double)total)/((double)NUMBER_OF_GAMES)));
	}
	
	/*
	 * @return true if the game is won
	 */
	private static boolean playGame(GameModel pModel)
	{
		pModel.reset();
		while( !pModel.isEmptyDeck() )
		{
			boolean advanced = pModel.tryToAutoPlay();
			if( !advanced )
			{
				break;
			}
		}
		return pModel.isCompleted();
	}
}
