package com.sscheffler.texasholdem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

public class HandHelper
{
	private ArrayList<Player> players; 
	private ArrayList<Card>	  communityCards;

	public HandHelper(ArrayList<Player> players)
	{
		this.players = players;
		this.communityCards = new ArrayList<Card>(5);
	}

	public Player determineBestHand()
	{
		int 	highestRank = -1;
		Player 	playerWithBestHand = new Player("Temp");
		Hand	bestHand = new Hand();
		//Check each player's hand
		for(Player player : this.players)
		{
			System.out.println("Checking " + player.getName());
			//Create a new "hand" of all 7 cards
			Hand playerHand = new Hand(player.getHand());
			playerHand.addCards(this.communityCards);

			//Get the highest rank
			HandChecker.generateBestHand(playerHand);
			
			//Set rank found and see if it is the highest so far
			player.setRank(playerHand.getRank());
			if(playerHand.getRank() > highestRank)
			{
				playerWithBestHand = player;
				highestRank = playerHand.getRank();
				bestHand = playerHand;
			}
			else if(playerHand.getRank() == highestRank)
			{
				if(bestHand.compareTo(playerHand) < 0)
				{ 
					playerWithBestHand = player;
					bestHand = playerHand;
				}
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
		//Overall this implementation is done poorly, but I wanted to see if I could whip something up
		//without checking for solutions. I will probably go back and implement one of the smarter approaches to this
		//now that I have seen better ways of doing these checks
		private static void generateBestHand(Hand handToCheck)
		{
			ArrayList<Card> handChecked = hasOnePair(handToCheck);
			ArrayList<Card> bestHand = handToCheck.getCards();

			//Need at least one pair to check for two pair, three of a kind, four pair, and full house
			if(handChecked != null)
			{
				handToCheck.setRank(1);
				bestHand = handChecked;

				//Check for two pair
				handChecked = hasTwoPair(handToCheck);
				if(handChecked != null)
				{
					handToCheck.setRank(2);
					bestHand = handChecked;
					
					//Check for four of a kind
					handChecked = hasFourOfAKind(handToCheck);
					if(handChecked != null)	
					{ 
						handToCheck.setRank(7);
						bestHand = handChecked;
					}
				}
				//Check for three of a kind, and if it has three of a kind, check for full house
				//Doesn't check if four of a kind was already found, since nothing in this will beat it
				if(handToCheck.getRank() < 7)	
				{
					//Check for three of a kind
					handChecked = hasThreeOfAKind(handToCheck);
					if(handChecked != null)
					{
						handToCheck.setRank(3); 
						bestHand = handChecked;
						
						//Check for full house
						handChecked = hasFullHouse(handToCheck);
						if(handChecked != null)	
						{
							handToCheck.setRank(6);
							bestHand = handChecked;
						}
					}
				}
			}//if has one pair
			
			//If the rank is two pair or higher so far, it's impossible to have a straight, so don't check
			if(handToCheck.getRank() < 2)
			{
				//Check for straight
				handChecked = hasStraight(handToCheck);
				if(handChecked != null)
				{
					handToCheck.setRank(4);
					bestHand = handChecked;
					
					//Check for flush
					handChecked = hasFlush(handToCheck);
					if(handChecked != null)
					{
						handToCheck.setRank(5);
						bestHand = handChecked;
						
						//Check for straight flush
						handChecked = hasStraightFlush(handToCheck);
						if(handChecked != null)
						{
							handToCheck.setRank(8);
							bestHand = handChecked;
						}
					}
				}
				else if(handToCheck.getRank() < 5);
				{
					//Check for flush
					handChecked = hasFlush(handToCheck);
					if(handChecked != null)
					{
						handToCheck.setRank(5);
						bestHand = handChecked;
					}
				}
			}//if one pair or less
			
			//Sort highest to lowest
			Collections.sort(bestHand);
			Collections.reverse(bestHand);
			
			handToCheck.setHighestCard(bestHand.get(0));
			bestHand = fillWithBestRemainingCards(bestHand, handToCheck.getCards());
			handToCheck.setCards(bestHand);
		}//generateBestHand method

		private static ArrayList<Card> hasStraightFlush(Hand handToCheck)
		{
			if(handToCheck.getHighestNumberOfSuits() < 5){ return null; }
			boolean			isFlush  = true;
			ArrayList<Card> straight = hasStraight(handToCheck);	
			
			Collections.sort(straight);

			//If we have a straight
			if(straight != null)
			{
				//Check to see if the straight is all of the same suit
				for(int i = 0; i < straight.size()-1; i++)
				{
					if(straight.get(i).getSuitAsInteger() != straight.get(i+1).getSuitAsInteger())
					{
						isFlush = false;
					}
				}
			}
			if(isFlush){ return straight; }
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

										cardsFound = new ArrayList<Card>();

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
									cardsFound = new ArrayList<Card>();

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
								cardsFound = new ArrayList<Card>();

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
						cardsFound = new ArrayList<Card>();
						cardsFound.add(card1);
						cardsFound.add(card2);

						return cardsFound;
					}
				}
			}
			return null;
		}

		private static ArrayList<Card> fillWithBestRemainingCards(ArrayList<Card> bestHandFound, ArrayList<Card> allCards)
		{
			//Remove the cards that actually make up the hand
			allCards.removeAll(bestHandFound);
			
			//Sort cards highest to lowest
			Collections.sort(allCards);
			Collections.reverse(allCards);
			
			//Add the highest cards to fill any remaining slots
			while(bestHandFound.size() < 5)
			{
				bestHandFound.add(allCards.get(0));
				allCards.remove(0);
			}
			
			return bestHandFound;
		}
	}//HandChecker
}//HandHelper
