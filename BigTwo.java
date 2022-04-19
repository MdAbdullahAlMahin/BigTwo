
import java.util.*;
import javax.swing.JOptionPane;

/**
 * The BigTwo class implements the CardGame interface and is used to model a Big Two card game.
 * 
 * @author Md Abdullah Al Mahin
 */
public class BigTwo implements CardGame {
	
	/**
	 * A constructor for creating a Big Two card game.
	 */
	BigTwo(){
		// Create a BigTwoUI object for providing the user interface.
		this.ui = new BigTwoGUI(this);
		this.client = new BigTwoClient(this, this.ui);
	}
	
	private int numOfPlayers; // An integer specifying the number of players.
	private Deck deck = new BigTwoDeck(); // A deck of cards.
	private ArrayList<CardGamePlayer> playerList = new ArrayList<CardGamePlayer>(); // A list of players.
	private ArrayList<Hand> handsOnTable = new ArrayList<Hand>(); // A list of hands played on the table.
	private int currentPlayerIdx; // An integer specifying the index of the current player.
	private BigTwoGUI ui; // A BigTwoUI object for providing the user interface.
	private Hand previousMove = null; // A variable to store the previous valid move for comparison.
	private Hand move = null; // A variable to store the current move.
	private boolean moveChecker = false; // A boolean to check whether the current move is valid or not.
	private int numOfPasses; // An integer to record how many passes have been made.
	private int winnerIndex; // An integer to store the ID of the player who wins.
	private BigTwoClient client; // A BigTwoClient object to perform the action of the game's cleint.
	
	/**
	 * A public getter for the gui of the game.
	 * 
	 * @return A reference to the gui of the game.
	 */
	public BigTwoGUI getGUI() {
		return this.ui;
	}
	
	/**
	 * A public setter for the gui of the game.
	 * 
	 * @param gui The new gui of the game.
	 */
	public void setGUI(BigTwoGUI gui) {
		this.ui = gui;
	}
	
	/**
	 * A public getter for the client of the game.
	 * 
	 * @return A reference to the client of the game.
	 */
	public BigTwoClient getClient() {
		return this.client;
	}
	
	/**
	 * A public setter for the client of the game.
	 * 
	 * @param client The new client of the game.
	 */
	public void setClient(BigTwoClient client) {
		this.client = client;
	}
	
	/**
	 * A public getter for the gui of the game.
	 * 
	 * @return A reference to the gui of the game.
	 */
	public int getWinnerIndex() {
		return this.winnerIndex;
	}
	
	/**
	 * A method for getting the number of players.
	 * 
	 * @return The number of players of the game.
	 */
	public int getNumOfPlayers() {
		return this.numOfPlayers;
	}
	
	/**
	 * A method for retrieving the deck of cards being used.
	 * 
	 * @return The an object used to model a deck of cards used in a BigTwo game.
	 */
	public Deck getDeck() {
		return this.deck;
	}
	
	/**
	 * A method for retrieving the list of players.
	 * 
	 * @return The ArrayList playerList.
	 */
	public ArrayList<CardGamePlayer> getPlayerList(){
		return this.playerList;
	}
	
	/**
	 * A method for retrieving the list of hands played on the table.
	 * 
	 * @return The ArrayList handsOnTable.
	 */
	public ArrayList<Hand> getHandsOnTable(){
		return this.handsOnTable;
	}
	
	/**
	 * A method for retrieving the index of the current player.
	 * 
	 * @return The value of the variable currentPlayerIdx.
	 */
	public int getCurrentPlayerIdx() {
		return this.currentPlayerIdx;
	}
	
	/**
	 * A method for starting/restarting the game with a given shuffled deck of cards.
	 *
	 * @param deck The shuffled deck of cards to start the game with. 
	 */
	public void start(Deck deck) {
		// Remove all the cards from the players as well as from the table;
		this.playerList.get(0).removeAllCards();
		this.playerList.get(1).removeAllCards();
		this.playerList.get(2).removeAllCards();
		this.playerList.get(3).removeAllCards();
		this.handsOnTable = new ArrayList<Hand>();
		
		// Distribute the cards to the players.
		int playerCounter = 0;
		for (int i = 0; i < deck.size(); i++) {
			if (playerCounter > 3) {
				playerCounter = 0;
			}
			BigTwoCard card = new BigTwoCard(deck.getCard(i).getSuit(), deck.getCard(i).getRank()); // Creating a new BigTwoCard which is added into the players' cardsInHand. 
			this.playerList.get(playerCounter).addCard(card);
			if ((card.getRank() == 2) && (card.getSuit() == 0)) { // Identify the player who holds the Three of Diamonds.
				// Set both the currentPlayerIdx of the BigTwo object and the activePlayer of the 
				// BigTwoUI object to the index of the player who holds the Three of Diamonds.
				this.currentPlayerIdx = playerCounter;
				this.ui.setActivePlayer(playerCounter);	
			}
			playerCounter++;
		}
		for (int i = 0; i < 4; i++) {
			this.playerList.get(i).getCardsInHand().sort();
		}
		
		this.previousMove = null;
		this.move = null;
		// Call the repaint() method of the BigTwoUI object to show the cards on the table.
		this.ui.repaint();
		// Call the promptActivePlayer() method of the BigTwoUI object to prompt user to select cards and make his/her move.
		this.ui.promptActivePlayer();
	}
	
	/**
	 * A method for broadcasting a move by a player with the specified index using the cards specified by the list of indices.
	 * 
	 * @param playerIdx The ID of the player making the move.
	 * @param cardIdx An array with the indexes of the played cards.
	 */
	public void makeMove(int playerIdx, int[] cardIdx) {
		this.client.sendMessage(new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx));
	}

	/**
	 * A method for checking a move made by a player.
	 * 
	 * @param playerIdx The ID of the player making the move.
	 * @param cardIdx An array with the indexes of the played cards.
	 */
	public void checkMove(int playerIdx, int[] cardIdx) {
		// Check if the player has passed his turn.
		if ((cardIdx == null)) {
			if ((numOfPasses < 3) && (this.handsOnTable.size() != 0)) { // Player can only pass if they are not the last person to play a hand.
				this.ui.printMsg("The move was passed.");
				this.numOfPasses++;
				moveChecker = true; // The pass move is valid.
			}
		}
		else {
			// Making a new hand from the given indexes.
			CardList cards = this.playerList.get(this.currentPlayerIdx).play(cardIdx);
			cards.sort();
			// Checking the type of the hand.
			this.move = composeHand(this.getPlayerList().get(playerIdx), cards);
			// Only check if the hand returned is a valid hand.
			if (move != null) {
				if ((this.handsOnTable.size() == 0) || (numOfPasses >= 3)) { // If this is the first move or everyone else has passed, directly add the move to handsOnTable.
					if (this.handsOnTable.size() == 0) {
						if (this.isThreeOfDiamondsInHand(move)) {
							this.handsOnTable.add(move);
							this.previousMove = move; // Make previousMove to current move for later comparison.
							// Remove all played cards from the cards available to the player.
							for (int i = cardIdx.length - 1; i >= 0; i--) {
								this.getPlayerList().get(playerIdx).getCardsInHand().removeCard(cardIdx[i]);
							}
							// Print out the move just played.
							if (this.previousMove != null) {
								this.ui.printMsg(this.getPlayerList().get(this.currentPlayerIdx).getName() + " plays a " + this.previousMove.getType() + ".");
								this.ui.printMsg("Played Hand: " + this.ui.getStringOfLastHand(this.previousMove));
							} 
							else {
								this.ui.printMsg("  [Empty]");
							}
							this.moveChecker = true; // Valid move is played.
							this.numOfPasses = 0; // Reset the number of passes in a row.
						}
					}
					else if (numOfPasses >= 3) {
						this.handsOnTable.add(move);
						this.previousMove = move; // Make previousMove to current move for later comparison.
						// Remove all played cards from the cards available to the player.
						for (int i = cardIdx.length - 1; i >= 0; i--) {
							this.getPlayerList().get(playerIdx).getCardsInHand().removeCard(cardIdx[i]);
						}
						// Print out the move just played.
						if (this.previousMove != null) {
							this.ui.printMsg(this.getPlayerList().get(this.currentPlayerIdx).getName() + " plays a " + this.previousMove.getType() + ".");
							this.ui.printMsg("Played Hand: " + this.ui.getStringOfLastHand(move));
						} 
						else {
							this.ui.printMsg("  [Empty]");
						}
						this.moveChecker = true; // Valid move is played.
						this.numOfPasses = 0; // Reset the number of passes in a row.
					}
					
				}
				else {
					if (move.size() == this.previousMove.size()) {
						if (move.beats(this.previousMove)){ // Checking if the move played beats the previous move on the table.
							this.handsOnTable.add(move);
							previousMove = move;
							// Remove all played cards from the cards available to the player.
							for (int i = cardIdx.length - 1; i >= 0; i--) {
								this.getPlayerList().get(playerIdx).getCardsInHand().removeCard(cardIdx[i]);
							}
							// Print out the move just played.
							if (this.previousMove != null) {
								this.ui.printMsg(this.getPlayerList().get(this.currentPlayerIdx).getName() + " plays a " + this.previousMove.getType() + ".");
								this.ui.printMsg("Played Hand: " + this.ui.getStringOfLastHand(this.previousMove));
							} 
							else {
								this.ui.printMsg("  [Empty]");
							}
							this.moveChecker = true; // Valid move is played.
							this.numOfPasses = 0; // Reset the number of passes in a row.
						}
					}
				}
			}
		}
		// Checking if a valid move, either pass or a valid hand, was played.
		if (!this.moveChecker) {
			this.ui.printMsg("This is an illegal move !!");
			this.ui.promptActivePlayer();
		}
		else {
			if (!this.endOfGame()) {
				this.ui.setLastPlayer(this.currentPlayerIdx);
				this.ui.printMsg("");
				this.moveChecker = false; // Reset moveChecker to check for next move to be played.
				// Moving onto the next player.
				this.updateCurrentPlayer();
				this.ui.setActivePlayer(this.getCurrentPlayerIdx());
				this.ui.repaint(); // Repainting the board.
				this.ui.resetSelected(); // Reset the selected array of the gui for the next player.
				this.ui.promptActivePlayer();
			}
			else {
				// If game ends, print out corresponding ending messages.
				this.ui.printMsg("\n" + "Game Ends." + "\n");
				this.ui.repaint();
				this.ui.disable(); // Disable further interactions with the gui.
				this.ui.printEndingMessage();
			}
		}
	}
	
	// A private method to check if the hand played has a 3 of Diamonds in it.
	private boolean isThreeOfDiamondsInHand(Hand cards) {
		for (int i = 0; i < cards.size(); i++) {
			if (cards.getCard(i).getRank() == 2 && cards.getCard(i).getSuit() == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * A method for checking if the game ends.
	 * 
	 * @return true if the game has ended, false otherwise.
	 */
	public boolean endOfGame() {
	for (int i = 0; i < 4; i++) {
		if (this.getPlayerList().get(i).getCardsInHand().size() == 0) {
			winnerIndex = i; // Set winnerIndex to the index of the player who won.
			return true;
		}
	}
	return false;
	}
	
	/**
	 * A method to update the currentPlayerIdx.
	 */
	public void updateCurrentPlayer() {
		if (this.currentPlayerIdx == 3) { // If currentPlayerIdx is 3, reset it to 0.
			this.currentPlayerIdx = 0;
		}
		else
			this.currentPlayerIdx++;
	}
	
	/**
	 * A method for starting a Big Two card game.
	 * 
	 * @param args The arguments for the main function. Not needed in this case.
	 */
	public static void main (String[] args) {
		// Create a Big Two card game.
		BigTwo game = new BigTwo();
	}
	
	/**
	 * A method for returning a valid hand from the specified list of cards of the player.
	 * 
	 * @param player The player who played the current hand.
	 * @param cards The played hand.
	 * @return A subclass of the Hand class, null if the hand played is not valid.
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		// Checking for single.
		if (cards.size() == 1) {
			Single single = new Single(player, cards);
			if (single.isValid()) {
				return single;
			}
		}
		// Checking for Pair.
		else if (cards.size() == 2) {
			Pair pair = new Pair(player, cards);
			if (pair.isValid()) {
				return pair;
			}
		}
		// Checking for Triple.
		else if (cards.size() == 3) {
			Triple triple = new Triple(player, cards);
			if (triple.isValid()) {
				return triple;
			}
		}
		// Checking for 5 card hands.
		else if (cards.size() == 5) {
			// Create all possible 5 card hands.
			Straight straight = new Straight(player, cards);
			Flush flush = new Flush(player, cards);
			FullHouse fullHouse = new FullHouse(player, cards);
			Quad quad = new Quad(player, cards);
			StraightFlush straightFlush = new StraightFlush(player, cards);
			// Checking for the type of 5 card hand from the best possible hand to worst (i.e StraightFlush is checked for before Quad, Flush, etc and so on).
			if (straightFlush.isValid()) {
				return straightFlush;
			}
			else if (quad.isValid()) {
				return quad;
			}
			else if (fullHouse.isValid()) {
				return fullHouse;
			}
			else if (flush.isValid()) {
				return flush;
			}
			else if (straight.isValid()) {
				return straight;
			}
		}
		return null; // Return null if the hand played constitutes no type of hand.
	}
}
