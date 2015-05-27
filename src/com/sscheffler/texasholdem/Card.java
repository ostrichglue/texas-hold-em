package com.sscheffler.texasholdem;

public class Card
{
	private String 				suit;
	private int	 				value;
	private static String[] 	valuesAsStrings = {"Ace", "2", "3", "4", "5", "6", "7", 
													"8", "9", "10", "Jack", "Queen", "King"};
	
	/**
	 * Initializes the Card to the given value and suit.
	 * @param value The value to set the card to, between 0 (Ace) and 12 (King)
	 * @param suit The suit to set the card to, e.g. "heart". "hearts" will not work.
	 * @throws IllegalArgumentException if the suit is not correct or if the value is not between 0 and 12
	 */
	public Card(int value, String suit)
	{
		if(!suit.equalsIgnoreCase("heart") && !suit.equalsIgnoreCase("club") && 
				!suit.equalsIgnoreCase("spade") && !suit.equalsIgnoreCase("diamond"))
		{
			throw new IllegalArgumentException("Card constructor: invalid suit. Must be heart, club, spade, or diamond.");
		}
		
		if(value < 0 || value > 12)
		{
			throw new IllegalArgumentException("Card Constructor: invalid value. Must be between 0 and 13. "
												+ "0 is ace, 12 is king.");
		}
		
		this.suit	 = suit;
		this.value	 = value;
	}
	
	/**
	 * Returns the suit of the current card.
	 * @return The suit of the card as a String
	 */
	public String getSuit()
	{
		return this.suit;
	}
	
	/**
	 * Returns the suit of the card with an "s" appended.
	 * Useful when printing out the cards information, such as 
	 * "King of hearts"
	 * @return The suit of the card with an "s" appended
	 */
	public String getSuitWithS()
	{
		return (this.suit + "s");
	}
	
	/**
	 * Returns the value of the card as an integer
	 * @return The integer value of the card
	 */
	public int getValue()
	{
		return this.value;
	}
	
	/**
	 * Returns the value of the card as a String, such as "King" or "2"
	 * @return The value of the card as a String
	 */
	public String getValueAsString()
	{
		return Card.valuesAsStrings[this.value];
	}
}
