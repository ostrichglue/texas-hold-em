package com.sscheffler.texasholdem;

import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Driver
{
	public static void main(String[] args)
	{
		Card				card;
		ArrayList<Card>		cardsOnTable = new ArrayList<>(5);
		StringBuilder		cardsOnTableInformation = new StringBuilder();
		Deck				deck = new Deck(true);
		HandHelper			handHelper;
		Integer[]			options = {1, 2, 3, 4, 5, 6, 7};
		int					numOfPlayersBesidesSelf;
		ArrayList<Player> 	players;
		String				playerName;
		
		//Get input from user
		numOfPlayersBesidesSelf = (int) JOptionPane.showInputDialog(null, "Choose how many players you would like to play against:", "Texas Hold Em",
				JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
		
		playerName = JOptionPane.showInputDialog(null, "What is your name?", "Thaddeus");
		
		players = new ArrayList<>(numOfPlayersBesidesSelf + 1);
		
		//Add human player
		players.add(new Player(playerName, false));
		
		//Add all CPU players
		for(int i = 0; i < numOfPlayersBesidesSelf; i++)
		{
			players.add(new Player(new String("CPU" + (i+1))));
		}
		
		//Deal two cards to each player
		for(int i = 0; i < players.size(); i++)
		{
			players.get(i).addCardToHand(deck.getNextCard());
			players.get(i).addCardToHand(deck.getNextCard());
		}
		
		//Print out the current hands for each player
		for(int i = 0; i < players.size(); i++)
		{
			players.get(i).printHand();
		}
		
		//Start the hand helper
		handHelper = new HandHelper(players);
		
		//Flop
		deck.getNextCard();	//Burn
		cardsOnTable.add(deck.getNextCard());
		cardsOnTable.add(deck.getNextCard());
		cardsOnTable.add(deck.getNextCard());
		
		//Append flop info
		cardsOnTableInformation.append("\nFlop:  ");
		for(int flopInfo = 0; flopInfo < 3; flopInfo++)
		{
			card = cardsOnTable.get(flopInfo);
			cardsOnTableInformation.append(card.getValueAsString() + " of " + card.getSuitWithS());
			if(flopInfo < 2)
			{
				cardsOnTableInformation.append(", ");
			}
		}
		
		//Add to handHelper
		handHelper.addCommunityCards(cardsOnTable);
		
		//Turn
		deck.getNextCard();	//Burn
		cardsOnTable.add(deck.getNextCard());
		
		//Append turn info
		card = cardsOnTable.get(3);
		cardsOnTableInformation.append("\nTurn:  " + card.getValueAsString() + " of " + card.getSuitWithS());
		
		//Add to handHelper
		handHelper.addCommunityCards(card);
		
		//River
		deck.getNextCard();	//Burn
		cardsOnTable.add(deck.getNextCard());
		
		//Append river info
		card = cardsOnTable.get(4);
		cardsOnTableInformation.append("\nRiver: " + card.getValueAsString() + " of " + card.getSuitWithS());
		
		//Add to handHelper
		handHelper.addCommunityCards(card);
		
		//Print collected info on flop, turn, and river
		System.out.println(cardsOnTableInformation.toString());
		
		Player playerWithBestHand = handHelper.determineBestHand();
		
		System.out.println("The winning player is " + playerWithBestHand.getName() + " with " + Hand.ranks[playerWithBestHand.getRank()]);
		
	}
}
