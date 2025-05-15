/*******************************************************************************
 * Solitaire
 * 
 * Copyright (C) 2016 by Martin P. Robillard
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
package ca.mcgill.solitaire.auto;

import ca.mcgill.solitaire.model.GameModel;

/**
 * Plays N games and, for each game, undo all moves and redo them.
 */
public final class CrashTest {
	private static final int NUMBER_OF_GAMES = 1000;

	private CrashTest() {
	}

	/**
	 * @param pArgs Not used.
	 */
	public static void main(String[] pArgs) {
		GameModel model = new GameModel(new GreedyPlayingStrategy());
		for (int i = 0; i < NUMBER_OF_GAMES; i++) {
			playGame(model);
		}
		System.out.println("Runs completed.");
	}

	private static void playGame(GameModel pModel) {
		pModel.reset();
		boolean advanced = true;
		while (advanced) {
			advanced = pModel.tryToAutoPlay();
		}
		while (pModel.canUndo()) {
			pModel.undoLast();
		}
		advanced = true;
		while (advanced) {
			advanced = pModel.tryToAutoPlay();
		}
	}
}
