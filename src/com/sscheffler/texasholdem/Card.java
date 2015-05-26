package com.sscheffler.texasholdem;

public class Card
{
	private String 		suit;
	private int	 		value;
	private static String[] 	valuesAsStrings = {"Ace", "2", "3", "4", "5", "6", "7", 
			"8", "9", "10", "Jack", "Queen", "King"};
	
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
	
	public String getSuit()
	{
		return this.suit;
	}
	
	public String getSuitWithS()
	{
		return (this.suit + "s");
	}
	
	public int getValue()
	{
		return this.value;
	}
	
	public String getValueAsString()
	{
		return Card.valuesAsStrings[this.value];
	}
}
