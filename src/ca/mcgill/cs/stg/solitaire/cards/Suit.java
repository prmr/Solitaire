/*******************************************************************************
 * Solitaire
 * 
 * Copyright (C) 2016-2024 by Martin P. Robillard
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
package ca.mcgill.cs.stg.solitaire.cards;

/**
 * Represents the suit of a playing card.
 */
public enum Suit {
	CLUBS, DIAMONDS, HEARTS, SPADES;

	/**
	 * @param pSuit The suit to test against.
	 * @return True if this suit and pSuit are of the same color.
	 * @pre pSuit != null;
	 */
	public boolean sameColorAs(Suit pSuit) {
		assert pSuit != null;
		return color() == pSuit.color();
	}

	private enum Color {
		RED, BLACK
	}

	private Color color() {
		if (this == CLUBS || this == SPADES) {
			return Color.BLACK;
		}
		else {
			return Color.RED;
		}
	}
}