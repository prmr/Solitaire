package ca.mcgill.cs.stg.solitaire.cards;

import org.junit.Test;

import ca.mcgill.cs.stg.solitaire.cards.Card.Rank;
import ca.mcgill.cs.stg.solitaire.cards.Card.Suit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestCard
{
	@Test
	public void testToIDString()
	{
		assertEquals("0", Card.get(Rank.ACE, Suit.CLUBS).getIDString());
		assertEquals("9", Card.get(Rank.TEN, Suit.CLUBS).getIDString());
		assertEquals("12", Card.get(Rank.KING, Suit.CLUBS).getIDString());
		assertEquals("13", Card.get(Rank.ACE, Suit.DIAMONDS).getIDString());
		assertEquals("26", Card.get(Rank.ACE, Suit.HEARTS).getIDString());
		assertEquals("39", Card.get(Rank.ACE, Suit.SPADES).getIDString());
	}
	
	@Test
	public void testFromIDString()
	{
		assertEquals(Card.get(Rank.ACE, Suit.CLUBS), Card.get("0"));
		assertEquals(Card.get(Rank.TEN, Suit.CLUBS), Card.get("9"));
		assertEquals(Card.get(Rank.KING, Suit.CLUBS), Card.get("12"));
		assertEquals(Card.get(Rank.ACE, Suit.DIAMONDS), Card.get("13"));
		assertEquals(Card.get(Rank.ACE, Suit.HEARTS), Card.get("26"));
		assertEquals(Card.get(Rank.ACE, Suit.SPADES), Card.get("39"));
	}
}
