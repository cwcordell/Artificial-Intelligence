package edu.uab.cis.agents.blackjack;

/**
 * A card in a Blackjack deck.
 */
public class Card
{
	/**
	 * The rank of a card
	 */
	public enum Rank
	{
		ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING
	}

	/**
	 * The suit of a card
	 */
	public enum Suit
	{
		SPADES, HEARTS, DIAMONDS, CLUBS
	}

	private Rank rank;
	private Suit suit;

	/**
	 * Creates a new Card with the given rank and suit.
	 * 
	 * @param rank
	 *           A rank
	 * @param suit
	 *           A suit
	 */
	public Card(Rank rank, Suit suit)
	{
		this.rank = rank;
		this.suit = suit;
	}

	/**
	 * Gets the rank of this card.
	 * 
	 * @return The rank
	 */
	public Rank getRank()
	{
		return this.rank;
	}

	/**
	 * Gets the suit of this card.
	 * 
	 * @return The suit
	 */
	public Suit getSuit()
	{
		return this.suit;
	}

}
