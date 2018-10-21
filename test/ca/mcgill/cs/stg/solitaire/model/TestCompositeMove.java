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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestCompositeMove
{
	class StubMove implements Move
	{
		private boolean aPerformed = false;
		private boolean aUndone = false;
		
		public boolean isPerformed()
		{
			return aPerformed;
		}
		
		public boolean isUndone()
		{
			return aUndone;
		}
		
		@Override
		public void perform()
		{
			aPerformed = true;
		}

		@Override
		public void undo()
		{
			aUndone = true;
		}
	}
	
	@Before
	public void setup()
	{}
	
	@Test
	public void testAll()
	{
		StubMove[] moves = new StubMove[3];
		for( int i = 0 ; i < moves.length; i++ )
		{
			moves[i] = new StubMove();
		}
		CompositeMove composite = new CompositeMove(moves);
		assertFalse(composite.isNull());
		composite.perform();
		for( StubMove move : moves )
		{
			assertTrue(move.isPerformed());
			assertFalse(move.isUndone());
		}
		composite.undo();
		for( StubMove move : moves )
		{
			assertTrue(move.isUndone());
		}
	}
	
}
