package com.sscheffler.texasholdem;

import java.util.ArrayList;

public class Player
{
	private boolean			folded;
	private ArrayList<Card> hand;
	private	Boolean			isCPU;
	private String			name;
	private int				rank;
	
	/**
	 * Initializes the Player object as a CPU.
	 * @param name The name of the player
	 * @throws IllegalArgumentException if the name is greater than 16 characters
	 */
	public Player(String name)
	{
		if(name == null){throw new IllegalArgumentException("Player constructor: name is null");}
		if(name.length() > 16){throw new IllegalArgumentException("Player constructor: name is more than 16 chars");}
		
		this.folded= false;
		this.hand 	= new ArrayList<Card>(2);
		this.isCPU  = true;
		this.name 	= name;
		this.rank 	= 0;
	}
	
	/**
	 * Initializes the Player object, specifying if the Player is a CPU or not.
	 * @param name The name of the player
	 * @param isCPU Whether or not the Player is computer controlled
	 * @throws IllegalArgumentException if the name is greater than 16 characters
	 */
	public Player(String name, boolean isCPU)
	{
		if(name == null){throw new IllegalArgumentException("Player constructor: name is null");}
		if(name.length() > 16){throw new IllegalArgumentException("Player constructor: name is more than 16 chars");}
		
		this.hand 	= new ArrayList<Card>(2);
		this.isCPU 	= isCPU;
		this.name 	= name;
	}
	
	/**
	 * Adds the given card into the player's hand at the next available slot
	 * @param cardToAdd Adds the card to the players hand
	 * @throws IllegalArgumentException if the player's hand is full
	 */
	public void addCardToHand(Card cardToAdd)
	{
		if(cardToAdd == null){throw new IllegalArgumentException("addCardToHand method: Card is null");}
		if(this.hand.size() > 2){throw new IllegalArgumentException("addCardToHand method: hand is already full");}
		if(this.hand.size() == 0)
		{
			this.hand.add(cardToAdd);
		}
		else
		{
			this.hand.add(cardToAdd);
		}
	}
	
	/**
	 * Sets the name of the player to a new name.
	 * @param newName The new name of the Player
	 * @throws IllegalArgumentException if the name is greater than 16 chars
	 */
	public void changeName(String newName)
	{
		if(newName == null){throw new IllegalArgumentException("Player constructor: name is null");}
		if(newName.length() > 16){throw new IllegalArgumentException("Player constructor: name is more than 16 chars");}
		
		this.name = newName;
	}
	
	public void flop()
	{
		this.folded = true;
	}

	/**
	 * Prints the info on all of the cards in the player's current hand.
	 */
	public void printHand()
	{
		System.out.println("Current hand for " + this.name + ": " + this.hand.get(0).getValueAsString() + " of " + this.hand.get(0).getSuitWithS() + ", " 
				+ this.hand.get(1).getValueAsString() + " of " + this.hand.get(1).getSuitWithS());
	}

	/**
	 * Returns true if the Player is a CPU, and false if not.
	 * @return The isCPU boolean
	 */
	public Boolean getIsCPU(){ return this.isCPU; }
	
	public ArrayList<Card> getHand(){ return this.hand; }
	
	public String getName(){ return this.name; }
	
	public int getRank(){ return this.rank; }
	
	public void setRank(int newRank){ this.rank = newRank; }
	
	public boolean isFolded(){ return this.folded; }
	
	public void fold(){ this.folded = true; }
	
}
