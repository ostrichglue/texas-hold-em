package com.sscheffler.texasholdem;

public class Card
{
	private String 	suit;
	private int	 	value;
	
	public Card(int value, String suit)
	{
		if(!suit.equalsIgnoreCase("heart") && !suit.equalsIgnoreCase("club") && 
				!suit.equalsIgnoreCase("spade") && !suit.equalsIgnoreCase("diamond"))
		{
			throw new IllegalArgumentException("Card constructor: invalid suit. Must be heart, club, spade, or diamond.");
		}
		
		if(value < 0 || value > 13)
		{
			throw new IllegalArgumentException("Card Constructor: invalid value. Must be between 0 and 13. 0 is ace, 13 is king.");
		}
		
		this.suit	 = suit;
		this.value	 = value;
	}
	
	public String getSuit()
	{
		return this.suit;
	}
	
	public int getValue()
	{
		return this.value;
	}
}
