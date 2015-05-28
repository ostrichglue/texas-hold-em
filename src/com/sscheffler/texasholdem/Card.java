package com.sscheffler.texasholdem;

public class Card implements Comparable<Card>
{
	private String 				suit;
	private int					suitAsInteger; //0 Club; 1 Diamond; 2 Heart; 3 Diamond
	private int	 				value;
	private static String[] 	valuesAsStrings = {"2", "3", "4", "5", "6", "7", 
													"8", "9", "10", "Jack", "Queen", "King", "Ace"};
	
	/**
	 * Initializes the Card to the given value and suit.
	 * 
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
												+ "0 is Two, 12 is Ace.");
		}
		
		this.suit	 = suit;
		this.value	 = value;
		//Set suit as an integer value to make it easier for helper classes
		if(suit.equals("club"))
		{
			this.suitAsInteger = 0;
		}
		else if(suit.equals("diamond"))
		{
			this.suitAsInteger = 1;
		}
		else if(suit.equals("heart"))
		{
			this.suitAsInteger = 2;
		}
		else //Spade
		{
			this.suitAsInteger = 3;
		}
	}
	
	@Override
	public int compareTo(Card card)
	{
		if(this.value > card.value)
		{
			return 1;
		}
		if(this.value == card.value)
		{
			return 0;
		}
		return -1;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + suitAsInteger;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		Card other = (Card) obj;
//		if (suitAsInteger != other.suitAsInteger)
//		{
//			return false;
//		}
		if (value != other.value)
		{
			return false;
		}
		return true;
	}

	/**
	 * Returns the suit of the current card.
	 * 
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
	 * 
	 * @return The suit of the card with an "s" appended
	 */
	public String getSuitWithS()
	{
		return (this.suit + "s");
	}
	
	public int getSuitAsInteger()
	{
		return this.suitAsInteger;
	}
	
	/**
	 * Returns the value of the card as an integer
	 * 
	 * @return The integer value of the card
	 */
	public int getValue()
	{
		return this.value;
	}
	
	/**
	 * Returns the value of the card as a String, such as "King" or "2"
	 * 
	 * @return The value of the card as a String
	 */
	public String getValueAsString()
	{
		return Card.valuesAsStrings[this.value];
	}
}
