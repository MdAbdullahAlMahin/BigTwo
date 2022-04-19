
/**
 * The Hand class is a subclass of the CardList class and is used to model a hand of cards.
 * 
 * @author Md Abdullah Al Mahin
 */
abstract class Hand extends CardList {

	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * 
	 * @param player The player playing the specified card.
	 * @param cards The specified list of cards the player is playing.
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for (int i = 0; i < cards.size(); i++) {
			this.addCard(new BigTwoCard(cards.getCard(i).getSuit(), cards.getCard(i).getRank()));
		}
	}
	
	private CardGamePlayer player; // The player playing this hand.
	
	/**
	 * A method for retrieving the player of this hand.
	 * 
	 * @return The player playing this hand.
	 */
	public CardGamePlayer getPlayer() {
		return this.player;
	}
	
	/**
	 * A method for retrieving the top card of this hand.
	 * 
	 * @return The top card of this hand.
	 */
	public Card getTopCard() {
		Card topCard = new BigTwoCard(0, 2); // Creating a 3 of Diamonds (card of lowest hierarchy) for comparison.
		for (int i = 0; i < this.size(); i++) { // Comparing with all cards in this hand.
			if (this.getCard(i).compareTo(topCard) == 1) {
				topCard = this.getCard(i);
			}
		}
		return topCard;
	}

	/**
	 * A public method for calculating how many cards of the same rank are in this hand.
	 * 
	 * @return An array with the count of how many cards are of the same rank, rank being equal to the index.
	 */
	public int[] rankCounter() {
		int[] tally = {0,0,0,0,0,0,0,0,0,0,0,0,0};
		for (int i = 0; i < this.size(); i++) {
			tally[this.getCard(i).getRank()]++;
		}
		return tally;
	}
	
	/**
	 * A public method for calculating how many cards of the same suit are in this hand.
	 * 
	 * @return An array with the count of how many cards are of the same suit, suit being equal to the index.
	 */
	public int[] suitCounter() {
		int[] tally = {0,0,0,0};
		for (int i = 0; i < this.size(); i++) {
			tally[this.getCard(i).getSuit()]++;
		}
		return tally;
	}
	
	/**
	 * A method for checking if this hand beats a specified hand.
	 *  
	 * @param hand The hand to compare this hand against.
	 * @return true if this hand beats the given hand, false otherwise.
	 */
	public boolean beats(Hand hand) {
		int x = 0;
		if (this.size() == hand.size()) { // If the sizes of both hands are equal, compare the hands.
			x = this.getTopCard().compareTo(hand.getTopCard()); // Most scenarios boil down to comparing the top cards of the hands, and in the general case that principle is used.
		}
		if (x == 1) {
			return true;
		}
		else
			return false;
	}
	
	/**
	 * A method for checking if this is a valid hand.
	 * 
	 * @return true if it is valid, false otherwise.
	 */
	abstract boolean isValid();
	
	/**
	 * A method for returning a string specifying the type of this hand.
	 * 
	 * @return A string of the type of the hand (i.e Straight, Flush, etc) based on the type of hand this hand is.
	 */
	abstract String getType();
	
	// An array used in the compareTo method, it is a array with the ranks arranged in terms of their hierarchy.
	private int[] rankComparer = {2,3,4,5,6,7,8,9,10,11,12,0,1};
	
	// An array used in the compareTo method, it is a array with the suits arranged in terms of their hierarchy.
    private int[] suitComparer = {0,1,2,3};

    /**
     * A public method to find the position of a rank in the hierarchy of ranks.
     *
     * @param rank The rank for which to find the hierarchy.
     * @return The hierarchy of the rank, -1 if the rank inputed is invalid.
     */
    public int findRankIndex(int rank) {
    	for (int i = 0; i < 13; i++) {
    		if (rankComparer[i] == rank) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    /**
     * A public method to find the position of a suit in the hierarchy of suits.
     *
     * @param rank The suit for which to find the hierarchy.
     * @return The hierarchy of the suit, -1 if the suit inputed is invalid.
     */
    public int findSuitIndex(int suit) {
    	for (int i = 0; i < 4; i++) {
    		if (suitComparer[i] == suit) {
    			return i;
    		}
    	}
    	return -1;
    }

}
