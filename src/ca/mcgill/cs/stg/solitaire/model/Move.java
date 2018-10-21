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
package ca.mcgill.cs.stg.solitaire.model;

/**
 * Represents one possible action in the game.
 */
public interface Move
{
	/**
	 * Performs the move. 
	 * @pre The move is legal
	 */
	void perform();
	
	/**
	 * Undoes the move by reversing
	 * its effect.
	 */
	void undo();
	
	/**
	 * @return True if the move is not a move that
	 * advances the game. False by default.
	 */
	default boolean isNull()
	{ return false; }
}
