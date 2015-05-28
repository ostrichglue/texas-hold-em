package com.sscheffler.texasholdem;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class HandHelper
{
	private ArrayList<Player> players; 
	private ArrayList<Card>	  communityCards;
	
	@SuppressWarnings("unchecked")
	public HandHelper(ArrayList<Player> players)
	{
		this.players = players;
		this.communityCards = new ArrayList<Card>(5);
	}
	
	public Player determineBestHand()
	{
		int 	highestRank = 0;
		Player 	playerWithBestHand = new Player("Temp");
		for(Player player : this.players)
		{
			System.out.println("Checking " + player.getName());
			Hand playerHand = new Hand(player.getHand());
			playerHand.addCards(this.communityCards);
			
			HandChecker.runThroughChecks(playerHand);
			player.setRank(playerHand.getRank());
			if(playerHand.getRank() >= highestRank)
			{
				playerWithBestHand = player;
				highestRank = playerHand.getRank();
			}
		}
		return playerWithBestHand;
	}
	
	public void addCommunityCards(Card card1)
	{
		this.communityCards.add(card1);
	}
	
	public void addCommunityCards(Card card1, Card card2)
	{
		this.addCommunityCards(card1);
		this.addCommunityCards(card2);
	}
	
	public void addCommunityCards(Card card1, Card card2, Card card3)
	{
		this.addCommunityCards(card1);
		this.addCommunityCards(card2);
		this.addCommunityCards(card3);
	}
	
	public void addCommunityCards(ArrayList<Card> cards)
	{
		this.communityCards.addAll(cards);
	}
	
	private static class HandChecker
	{
		//Overall this implementation is done very poorly, but I wanted to see if I could whip something up
		//without checking for solutions. I will probably go back and implement one of the smarter approaches to this
		//now that I have seen better ways of doing these checks
		private static void runThroughChecks(Hand handToCheck)
		{
			//Need at least one pair to check for two pair, three of a kind, four pair, and full house
			if(hasOnePair(handToCheck) != null)
			{
				handToCheck.setRank(1);
				
				//Check for two pair, and if it has two pair, see if it is four of a kind
				if(hasTwoPair(handToCheck) != null)			{ handToCheck.setRank(2); 
					if(hasFourOfAKind(handToCheck) != null)	{ handToCheck.setRank(7); }
				}
				//Check for three of a kind, and if it has three of a kind, check for full house
				//Doesn't check if four of a kind was already found, since nothing in this will beat it
				if(handToCheck.getRank() < 7 && hasThreeOfAKind(handToCheck) != null)	
				{
					handToCheck.setRank(3); 
					if(hasFullHouse(handToCheck) != null)	{ handToCheck.setRank(6); }
				}	
			}
			//If the rank is two pair or higher so far, it's impossible to have a straight, so don't check
			if(handToCheck.getRank() == 1 && hasStraight(handToCheck) != null)
			{
				handToCheck.setRank(4);
				if(hasFlush(handToCheck) != null)
				{
					handToCheck.setRank(5);
					if(hasStraightFlush(handToCheck) != null)
					{
						handToCheck.setRank(8);
					}
				}
			}
			else if(hasFlush(handToCheck) != null)
			{
				handToCheck.setRank(5);
			}
		}
		
		private static ArrayList<Card> hasStraightFlush(Hand handToCheck)
		{
			/*
			 * Same method as checking for Straight. The reason this is done is for the very off chance
			 * where a full 7 card straight flush is had, to ensure that the highest straight flush is 
			 * returned
			*/
			//TODO Check for special case of A 2 3 4 5 
			//Put cards into ascending order
			ArrayList<Card> cardsFound;
			
			handToCheck.sortLowestToHighest();
			
			ArrayList<Card> duplicatesRemovedList = handToCheck.getCards();
			
			//Need to get rid of any duplicate valued cards to make checking easier
			LinkedHashSet<Card> removeDuplicates = new LinkedHashSet<Card>(duplicatesRemovedList);
			duplicatesRemovedList.clear();
			duplicatesRemovedList.addAll(removeDuplicates);
	
			if (duplicatesRemovedList.size() >= 5)
			{
				for (int i = (duplicatesRemovedList.size() - 5); i > -1; i--)
				{
					Card card1 = handToCheck.getCards().get(i);
					Card card2 = handToCheck.getCards().get(i + 1);
					if (card1.getValue() == (card2.getValue() - 1) && card1.getSuit() == card2.getSuit())
					{
						//Have a straight flush through 2 cards
						Card card3 = handToCheck.getCards().get(i + 2);
						if (card2.getValue() == (card3.getValue() - 1) && card1.getSuit() == card2.getSuit())
						{
							//Have a straight flush through 3 cards
							Card card4 = handToCheck.getCards().get(i + 3);
							if (card3.getValue() == (card4.getValue() - 1) && card1.getSuit() == card2.getSuit())
							{
								//Have a straight flush through 4 cards
								Card card5 = handToCheck.getCards().get(i + 4);
								if (card4.getValue() == (card5.getValue() - 1) && card1.getSuit() == card2.getSuit())
								{
									//We have a straight flush!
									cardsFound = new ArrayList<Card>(5);

									cardsFound.add(card1);
									cardsFound.add(card2);
									cardsFound.add(card3);
									cardsFound.add(card4);
									cardsFound.add(card5);

									return cardsFound;

								}//4 and 5 check
							}//3 and 4 check
							//If we get this far, only one more possible way to have a straight
							else{ i--; }
						}
						//Cannot contain a straight
						else{ return null; }
					}
					//Cannot contain a straight
					else{ return null; }
				}//for
			}//if size at least 5
			return null;
		}
		
		private static ArrayList<Card> hasFourOfAKind(Hand handToCheck)
		{
			//I could probably take advantage of this sorted order more in future
			handToCheck.sortHighestToLowest(); //Make checking a bit faster
			
			
			ArrayList<Card> cardsFound;
			Card card1;
			Card card2;
			Card card3;
			Card card4;
			
			for(int i = 0; i < handToCheck.getCards().size(); i ++)
			{
				card1 = handToCheck.getCards().get(i);
				for(int j = i+1; j < handToCheck.getCards().size(); j++)
				{
					card2 = handToCheck.getCards().get(j);
					if(card1.getValue() == card2.getValue())
					{
						//Pair found
						for(int k = j+1; k < handToCheck.getCards().size(); k++)
						{
							card3 = handToCheck.getCards().get(k);
							if(card3.getValue() == card2.getValue())
							{
								//Three of a kind found
								for(int l = k+1; l < handToCheck.getCards().size(); l++)
								{
									card4 = handToCheck.getCards().get(l);
									if(card4.getValue() == card3.getValue())
									{
										//Four of a kind found
										
										cardsFound = new ArrayList<Card>(4);
										
										cardsFound.add(card1);
										cardsFound.add(card2);
										cardsFound.add(card3);
										cardsFound.add(card4);
										
										return cardsFound;
									}
								}
							}
						}//for k
					}//if pair found
				}//for j
			}//for i
			return null;
		}
		
		@SuppressWarnings("unchecked")
		private static ArrayList<Card> hasFullHouse(Hand handToCheck)
		{
			ArrayList<Card> newCardsToCheck;
			Hand			newHandToCheck;
			
			ArrayList<Card> cardsFoundThreeOfAKind 	= hasThreeOfAKind(handToCheck);
			ArrayList<Card> cardsFoundOnePair		= new ArrayList<Card>();
			ArrayList<Card> allCardsFound 			= new ArrayList<Card>();
			
			//First check to make sure three of a kind was found
			if(cardsFoundThreeOfAKind == null){ return null; }
			else
			{			
				//Preserve original hand
				newCardsToCheck = handToCheck.getCards();
				newHandToCheck  = new Hand(newCardsToCheck);
				//Remove the first three of a kind to ensure a separate pair is found
				newHandToCheck.removeCards(cardsFoundThreeOfAKind);
				
				//Add the three of a kind to the cards found
				allCardsFound.addAll(cardsFoundThreeOfAKind);
				
				//Now  find the other pair in the remaining cards
				cardsFoundOnePair = hasOnePair(newHandToCheck);
				
				if(cardsFoundOnePair == null){ return null; }
				else
				{
					allCardsFound.addAll(cardsFoundOnePair);
					return allCardsFound;
				}
			}
		}
		
		private static ArrayList<Card> hasFlush(Hand handToCheck)
		{
			//Check if the flush exists
			if(handToCheck.getHighestNumberOfSuits() < 5){ return null; }
			
			//Ensure highest values of flush are taken
			handToCheck.sortHighestToLowest();
			
			//Now we have one, so get the cards comprising the flush
			ArrayList<Card> cardsFound = new ArrayList<Card>();
			int suit = handToCheck.getSuitWithHighestAmountOfCards();
			
			for(Card card : handToCheck.getCards())
			{
				int i = 0;
				if(card.getSuitAsInteger() == suit && i < 6)
				{
					cardsFound.add(card);
					i++;
				}
			}
			return cardsFound;
		}
		
		/**
		 * Checks a given Hand to determine if a straight exists in it.
		 * @param handToCheck The hand to check for a straight in
		 * @return An ArrayList containing the cards forming a straight, or none if no straight was found
		 */
		@SuppressWarnings("unchecked")
		private static ArrayList<Card> hasStraight(Hand handToCheck)
		{
			//TODO Check for special case of A 2 3 4 5 
			//Put cards into ascending order
			ArrayList<Card> cardsFound;
			
			handToCheck.sortLowestToHighest();
			
			ArrayList<Card> duplicatesRemovedList = handToCheck.getCards();
			
			//Need to get rid of any duplicate valued cards to make checking easier
			LinkedHashSet<Card> removeDuplicates = new LinkedHashSet<Card>(duplicatesRemovedList);
			duplicatesRemovedList.clear();
			duplicatesRemovedList.addAll(removeDuplicates);
	
			if (duplicatesRemovedList.size() >= 5)
			{
				for (int i = (duplicatesRemovedList.size() - 5); i > -1; i--)
				{
					Card card1 = handToCheck.getCards().get(i);
					Card card2 = handToCheck.getCards().get(i + 1);
					if (card1.getValue() == (card2.getValue() - 1))
					{
						//Have a straight through 2 cards
						Card card3 = handToCheck.getCards().get(i + 2);
						if (card2.getValue() == (card3.getValue() - 1))
						{
							//Have a straight through 3 cards
							Card card4 = handToCheck.getCards().get(i + 3);
							if (card3.getValue() == (card4.getValue() - 1))
							{
								//Have a straight through 4 cards
								Card card5 = handToCheck.getCards().get(i + 4);
								if (card4.getValue() == (card5.getValue() - 1))
								{
									//We have a straight!
									cardsFound = new ArrayList<Card>(5);

									cardsFound.add(card1);
									cardsFound.add(card2);
									cardsFound.add(card3);
									cardsFound.add(card4);
									cardsFound.add(card5);

									return cardsFound;

								}//4 and 5 check
							}//3 and 4 check
							//If we get this far, only one more possible way to have a straight
							else{ i--; }
						}
						//Cannot contain a straight
						else{ return null; }
					}
					//Cannot contain a straight
					else{ return null; }
				}//for
			}//if size at least 5
			return null;
		}
		
		/**
		 * Checks a given Hand to determine if three Cards with matching values are present in it.
		 * @param handToCheck The hand to check for three of a kind in
		 * @return An ArrayList containing the three of a kind, or null if none is found
		 */
		private static ArrayList<Card> hasThreeOfAKind(Hand handToCheck)
		{
			//Sort so that highest three of a kind is always found first
			handToCheck.sortHighestToLowest();
			
			ArrayList<Card> cardsFound;
			Card 			card1;
			Card			card2;
			Card			card3;
			
			for(int i = 0; i < handToCheck.getCards().size(); i ++)
			{
				card1 = handToCheck.getCards().get(i);
				for(int j = i+1; j < handToCheck.getCards().size(); j++)
				{
					card2 = handToCheck.getCards().get(j);
					if(card1.getValue() == card2.getValue())
					{
						//Pair found
						for(int k = j+1; k < handToCheck.getCards().size(); k++)
						{
							card3 = handToCheck.getCards().get(k);
							if(card3.getValue() == card2.getValue())
							{
								//Three of a kind found
								cardsFound = new ArrayList<Card>(3);
								
								cardsFound.add(card1);
								cardsFound.add(card2);
								cardsFound.add(card3);
								
								return cardsFound;
							}
						}//for k
					}//if pair found
				}//for j
			}//for i
			return null;
		}//hasThreeOfAKind
		
		/**
		 * Checks to see if two pairs exist in a given Hand.
		 * @param handToCheck The hand to check for two pairs in
		 * @return An ArrayList of the two pairs found, or null if two pairs are not found
		 */
		@SuppressWarnings("unchecked")
		private static ArrayList<Card> hasTwoPair(Hand handToCheck)
		{
			//Sort so that highest pairs are always found first
			handToCheck.sortHighestToLowest();
			
			ArrayList<Card> newCardsToCheck;
			Hand			newHandToCheck;
			
			ArrayList<Card> cardsFoundOnePair = hasOnePair(handToCheck);
			ArrayList<Card> cardsFoundTwoPair = new ArrayList<Card>();
			
			if(cardsFoundOnePair == null){ return null; }
			else
			{
				newCardsToCheck	= handToCheck.getCards();
				newHandToCheck	= new Hand(newCardsToCheck);
				//Remove first pair to check if there is a different pair
				newHandToCheck.removeCards(cardsFoundOnePair);
				
				//Add the first pair found to the ArrayList to be returned
				cardsFoundTwoPair.addAll(cardsFoundOnePair);
				
				cardsFoundOnePair = hasOnePair(newHandToCheck);
				
				if(cardsFoundOnePair == null)
				{
					//If another pair is not found, it does not have two pairs
					return null;
				}
				else
				{
					//Add the pair found to the ArrayList, then return it
					cardsFoundTwoPair.addAll(cardsFoundOnePair);
					
					return cardsFoundTwoPair;
				}
			}
		}
		
		/**
		 * Given a Hand, will check to see if it contains a pair, meaning two cards of equal value.
		 * @param handToCheck The hand to check for a pair in
		 * @return An ArrayList of the Cards forming the pair, or null if none are found
		 */
		private static ArrayList<Card> hasOnePair(Hand handToCheck)
		{
			//Sort so that highest pair is always found first
			handToCheck.sortHighestToLowest();
			
			ArrayList<Card> cardsFound;
			Card 			card1;
			Card			card2;
			
			//Compare each card against every other card to see if a pair is found
			for(int i = 0; i < handToCheck.getCards().size(); i++)
			{
				card1 = handToCheck.getCards().get(i);
				for(int j = i+1; j < handToCheck.getCards().size(); j++)
				{
					card2 = handToCheck.getCards().get(j);
					if(card1.getValue() == card2.getValue())
					{
						cardsFound = new ArrayList<Card>(2);
						cardsFound.add(card1);
						cardsFound.add(card2);
						
						return cardsFound;
					}
				}
			}
			return null;
		}
	}
}
