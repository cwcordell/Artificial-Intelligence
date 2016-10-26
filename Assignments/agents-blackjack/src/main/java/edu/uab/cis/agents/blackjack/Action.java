package edu.uab.cis.agents.blackjack;

/**
 * The actions available to a Blackjack agent.
 */
public enum Action
{
	/**
	 * Indicates that the Agent wants another card.
	 */
	HIT,
	/**
	 * Indicates that the Agent does not want any more cards.
	 */
	STAND,
	/**
	 * Indicates that that the Agent's hand exceeds 21 points.
	 */
	BUST
}
