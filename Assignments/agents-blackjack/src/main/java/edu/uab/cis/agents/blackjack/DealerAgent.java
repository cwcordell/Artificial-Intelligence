package edu.uab.cis.agents.blackjack;

import java.util.Iterator;
import java.util.List;

import edu.uab.cis.agents.blackjack.Card.Rank;

/**
 * A dealer takes the {@link Action#HIT} action until their cards total 17 or
 * more points. Then the dealer takes the {@link Action#STAND} action.
 */
public class DealerAgent implements Agent
{
	@Override
	public Action act(List<Card> cards)
	{
		int sum = sum(cards);
		if (sum <= 16)
			return Action.HIT;
		if (sum <= 21)
			return Action.STAND;
		return Action.BUST;
	}

	private int sum(List<Card> cards)
	{
		Card card;
		int sum = 0;
		int aceCount = 0;
		Iterator<Card> iter = cards.iterator();

		while (iter.hasNext())
		{
			card = iter.next();
			if (card.getRank() == Rank.ACE)
				aceCount++;
			sum += getCardValue(card);
		}

		while (aceCount > 0 && sum > 21)
		{
			sum -= 10;
			aceCount--;
		}

		return sum;
	}

	private int getCardValue(Card card)
	{
		switch (card.getRank())
		{
			case ACE:
				return 11;
			case TWO:
				return 2;
			case THREE:
				return 3;
			case FOUR:
				return 4;
			case FIVE:
				return 5;
			case SIX:
				return 6;
			case SEVEN:
				return 7;
			case EIGHT:
				return 8;
			case NINE:
				return 9;
			default:
				return 10;
		}
	}
}
