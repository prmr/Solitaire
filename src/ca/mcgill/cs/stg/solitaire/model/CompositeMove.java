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

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the Composite object in the composite 
 * design pattern.
 */
public class CompositeMove implements Move
{
	private final List<Move> aMoves = new ArrayList<>();
	
	/**
	 * @param pMoves Any move to be added to this composite
	 */
	public CompositeMove( Move ... pMoves)
	{
		for( Move move : pMoves )
		{
			aMoves.add(move);
		}
	}
	
	@Override
	public void perform()
	{
		for( Move move : aMoves )
		{
			move.perform();
		}
	}

	@Override
	public void undo()
	{
		for( int i = aMoves.size()-1; i >=0; i-- )
		{
			aMoves.get(i).undo();
		}
	}
}
