package edu.uab.cis.agents.blackjack;

import java.util.List;

/**
 * An agent for playing blackjack.
 */
public interface Agent
{
	/**
	 * Take an action based on the cards that have been dealt to the Agent.
	 * 
	 * @param cards
	 *           The cards dealt so far to the Agent.
	 * @return An action indicating whether the Agent would like to
	 *         {@link Action#HIT}, {@link Action#STAND}, or {@link Action#BUST}.
	 */
	public Action act(List<Card> cards);
}
