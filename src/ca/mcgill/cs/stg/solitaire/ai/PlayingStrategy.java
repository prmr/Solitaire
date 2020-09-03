/*******************************************************************************
 * Solitaire
 *  
 *  Copyright (C) 2016 by Martin P. Robillard
 *  
 *  See: https://github.com/prmr/Solitaire
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/.
 *******************************************************************************/
package ca.mcgill.cs.stg.solitaire.ai;

import ca.mcgill.cs.stg.solitaire.model.GameModelView;
import ca.mcgill.cs.stg.solitaire.model.Move;

/**
 * Game-playing behavior. Implementations of this interface
 * are responsible for ensuring that the sequence of Move 
 * instances returned does not put the game in and endless
 * cycle.
 */
public interface PlayingStrategy
{
	/**
	 * Returns a legal move for the game, or the 
	 * Null move if that is not possible.
	 * 
	 * @param pModel A game model to query.
	 * @return The move computed.
	 */
	Move getLegalMove(GameModelView pModel);
}
