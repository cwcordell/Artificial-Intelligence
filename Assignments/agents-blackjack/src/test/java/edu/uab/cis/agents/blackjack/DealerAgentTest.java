package edu.uab.cis.agents.blackjack;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import edu.uab.cis.agents.blackjack.Action;
import edu.uab.cis.agents.blackjack.Card;
import edu.uab.cis.agents.blackjack.DealerAgent;
import edu.uab.cis.agents.blackjack.Card.Rank;
import edu.uab.cis.agents.blackjack.Card.Suit;

public class DealerAgentTest
{
	@Test
	public void testTenKing()
	{
		DealerAgent agent = new DealerAgent();
		Card s10 = new Card(Rank.TEN, Suit.SPADES);
		Card hK = new Card(Rank.KING, Suit.HEARTS);
		Action action = agent.act(Arrays.asList(s10, hK));
		Assert.assertEquals(Action.STAND, action);
	}

	@Test
	public void testSixJack()
	{
		DealerAgent agent = new DealerAgent();
		Card d6 = new Card(Rank.SIX, Suit.DIAMONDS);
		Card cJ = new Card(Rank.JACK, Suit.CLUBS);
		Action action = agent.act(Arrays.asList(d6, cJ));
		Assert.assertEquals(Action.HIT, action);
	}
	
	@Test
	public void testSevenJack()
	{
		DealerAgent agent = new DealerAgent();
		Card d7 = new Card(Rank.SEVEN, Suit.DIAMONDS);
		Card cJ = new Card(Rank.JACK, Suit.CLUBS);
		Action action = agent.act(Arrays.asList(d7, cJ));
		Assert.assertEquals(Action.STAND, action);
	}

	@Test
	public void testAceAce()
	{
		DealerAgent agent = new DealerAgent();
		Card dA = new Card(Rank.ACE, Suit.DIAMONDS);
		Card cA = new Card(Rank.ACE, Suit.CLUBS);
		Action action = agent.act(Arrays.asList(dA, cA));
		Assert.assertEquals(Action.HIT, action);
	}
	
	@Test
	public void testAceKing()
	{
		DealerAgent agent = new DealerAgent();
		Card dA = new Card(Rank.ACE, Suit.DIAMONDS);
		Card cK = new Card(Rank.KING, Suit.CLUBS);
		Action action = agent.act(Arrays.asList(dA, cK));
		Assert.assertEquals(Action.STAND, action);
	}
	
	@Test
	public void testAceNineAce()
	{
		DealerAgent agent = new DealerAgent();
		Card dA = new Card(Rank.ACE, Suit.DIAMONDS);
		Card c9 = new Card(Rank.NINE, Suit.CLUBS);
		Card sA = new Card(Rank.ACE, Suit.SPADES);
		Action action = agent.act(Arrays.asList(dA, c9, sA));
		Assert.assertEquals(Action.STAND, action);
	}
	
	@Test
	public void testAceKingAce()
	{
		DealerAgent agent = new DealerAgent();
		Card dA = new Card(Rank.ACE, Suit.DIAMONDS);
		Card cK = new Card(Rank.KING, Suit.CLUBS);
		Card sA = new Card(Rank.ACE, Suit.SPADES);
		Action action = agent.act(Arrays.asList(dA, cK, sA));
		Assert.assertEquals(Action.HIT, action);
	}
	
	@Test
	public void testAceKingQueen()
	{
		DealerAgent agent = new DealerAgent();
		Card dA = new Card(Rank.ACE, Suit.DIAMONDS);
		Card cK = new Card(Rank.KING, Suit.CLUBS);
		Card sQ = new Card(Rank.QUEEN, Suit.SPADES);
		Action action = agent.act(Arrays.asList(dA, cK, sQ));
		Assert.assertEquals(Action.STAND, action);
	}
	
	@Test
	public void testTwoFourEightThreeFive()
	{
		DealerAgent agent = new DealerAgent();
		Card d2 = new Card(Rank.TWO, Suit.DIAMONDS);
		Card c4 = new Card(Rank.FOUR, Suit.CLUBS);
		Card s5 = new Card(Rank.FIVE, Suit.SPADES);
		Card h3 = new Card(Rank.THREE, Suit.HEARTS);
		Card s8 = new Card(Rank.EIGHT, Suit.SPADES);
		Action action = agent.act(Arrays.asList(d2, c4, s5, h3, s8));
		Assert.assertEquals(Action.BUST, action);
	}
}
