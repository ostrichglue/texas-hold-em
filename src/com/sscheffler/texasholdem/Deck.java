package com.sscheffler.texasholdem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck
{
	private ArrayList<Card>  		deckOfCards;
	private static final int 		numOfCards = 52;
	private static final int 		numOfCardsInSuit = 13;
	private				 int 		numOfCardsInDeck;
	private static final String[] 	suits = {"heart", "diamond", "spade", "club"}; 
	
	public Deck()
	{
		this.fillDeck();
	}
	
	public Deck(boolean shuffleInitially)
	{
		if(shuffleInitially == true)
		{
			this.fillDeck();
			this.shuffleDeck();
		}
		else
		{
			this.fillDeck();
		}
	}
	
	/**
	 * Fills the deckOfCards ArrayList with all 52 cards.
	 */
	public void fillDeck()
	{
		//"Delete" all cards from deck
		this.deckOfCards 		= new ArrayList<Card>(52);
		this.numOfCardsInDeck 	= 0;
		
		Card	cardToAdd;
		//For each suit
		for(int suitsComplete = 0; suitsComplete < suits.length; suitsComplete++)
		{
			//Insert each card in the suit
			for(int cardsInSuitComplete = 0; cardsInSuitComplete < numOfCardsInSuit; cardsInSuitComplete++)
			{
				//Generate card, then add it
				cardToAdd = new Card(cardsInSuitComplete, suits[suitsComplete]);
				deckOfCards.add(cardToAdd);
				
				this.numOfCardsInDeck++;
			}
		}
	}
	
	/**
	 * Returns a random card from the deck.
	 * @return A random card from the deck.
	 */
	public Card getRandomCard()
	{
		this.numOfCardsInDeck--;
		return this.deckOfCards.get(new Random().nextInt(this.deckOfCards.size()));
	}
	
	/**
	 * Removes and returns the next card in the deck.
	 * @return The card at index 0 in the deck.
	 */
	public Card getNextCard()
	{
		this.numOfCardsInDeck--;
		return this.deckOfCards.remove(0);
	}
	
	/**
	 * Shuffles the deck randomly.
	 */
	public void shuffleDeck()
	{
		Collections.shuffle(deckOfCards);
	}
}
