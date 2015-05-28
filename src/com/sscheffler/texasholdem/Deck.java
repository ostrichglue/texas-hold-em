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
	
	/**
	 * Initializes the deck with all 52 unique cards without shuffling them
	 */
	public Deck()
	{
		this.fillDeck();
	}
	
	/**
	 * Initializes the Deck with all 52 unique cards, and shuffles if asked for.
	 * 
	 * @param shuffleInitially Shuffles the deck randomly after creation if true
	 */
	public Deck(boolean shuffleInitially)
	{
		this.fillDeck();
		
		if(shuffleInitially == true)
		{
			this.shuffleDeck();
		}
	}
	
	/**
	 * Fills the deckOfCards ArrayList with all 52 cards.
	 */
	public void fillDeck()
	{
		//"Delete" all cards from deck
		this.deckOfCards 		= new ArrayList<Card>(Deck.numOfCards);
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
	 * 
	 * @return A random card from the deck.
	 */
	public Card getRandomCard()
	{
		this.numOfCardsInDeck--;
		return this.deckOfCards.get(new Random().nextInt(this.numOfCardsInDeck));
	}
	
	/**
	 * Removes and returns the next card in the deck.
	 * 
	 * @return The card at index 0 in the deck.
	 */
	public Card getNextCard()
	{
		this.numOfCardsInDeck--;
		return this.deckOfCards.remove(0);
	}
	
	/**
	 * Shuffles the deck randomly several times.
	 */
	public void shuffleDeck()
	{
		for(int i = 0; i < 20; i++){ Collections.shuffle(deckOfCards); }
	}
}
