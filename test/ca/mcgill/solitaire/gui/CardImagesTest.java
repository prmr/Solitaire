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
package ca.mcgill.solitaire.gui;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ca.mcgill.solitaire.cards.Card;
import ca.mcgill.solitaire.testutils.TestUtils;
import javafx.scene.image.Image;

public class CardImagesTest {

	@Test
	void testimageForBackOfCard() {
		assertSame(CardImages.imageForBackOfCard(), CardImages.imageForBackOfCard());
		assertNotNull(CardImages.imageForBackOfCard());
	}

	@ParameterizedTest
	@MethodSource("allCards")
	void testImageForCard(Card pCard) {
		try {
			Image image1 = CardImages.imageFor(pCard);
			Image image2 = CardImages.imageFor(pCard);
			assertSame(image1, image2);
		}
		catch (NullPointerException e) {
			throw new AssertionError(String.format("Image for %s cannot be loaded", pCard));
		}
	}

	private static List<Card> allCards() {
		return TestUtils.allCards();
	}
}
