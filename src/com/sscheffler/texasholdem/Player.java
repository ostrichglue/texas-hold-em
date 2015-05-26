package com.sscheffler.texasholdem;

public class Player
{
	private Card[] 	hand;
	private	Boolean	isCPU;
	private String	name;
	
	public Player(String name)
	{
		if(name.length() > 16){throw new IllegalArgumentException("Player constructor: name must be no more than 16 characters.");}
		this.hand = new Card[2];
		this.isCPU 	= true;
		this.name 	= name;
	}
	
	public Player(String name, boolean isCPU)
	{
		if(name.length() > 16){throw new IllegalArgumentException("Player constructor: name must be no more than 16 characters.");}
		this.hand = new Card[2];
		this.isCPU 	= isCPU;
		this.name 	= name;
	}
	
	public void addCardToHand(Card cardToAdd)
	{
		if(cardToAdd == null){throw new IllegalArgumentException("addCardToHand method: Card is null");}
		if(this.hand[0] != null && this.hand[1] != null){throw new IllegalArgumentException("addCardToHand method: hand is already full");}
		if(this.hand[0] == null)
		{
			this.hand[0] = cardToAdd;
		}
		else
		{
			this.hand[1] = cardToAdd;
		}
	}
	
	public void printHand()
	{
		System.out.println("Current hand for " + this.name + ": " + this.hand[0].getValueAsString() + " of " + this.hand[0].getSuitWithS() + ", " 
				+ this.hand[1].getValueAsString() + " of " + this.hand[1].getSuitWithS());
	}
}
