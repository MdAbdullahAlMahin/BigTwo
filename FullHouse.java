
/**
 * The FullHouse class is a subclass of the hand class and is used to model a hand of type FullHouse.
 * 
 * @author Md Abdullah Al Mahin
 */
public class FullHouse extends Hand {

	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * 
	 * @param player The player playing the specified card.
	 * @param cards The specified list of cards the player is playing.
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	private int tripletRank  = 0; // A private instance variable for storing which rank of this hand is the triplet rank.
	
	/**
	 * A method for checking if this is a valid FullHouse.
	 * 
	 * @return true if it is valid, false otherwise.
	 */
	public boolean isValid() {
		boolean threeCards = false; // A variable to check if there are exactly three cards of the same rank.
		boolean twoCards = false; // A variable to check if there are exactly two cards of the same rank.
		if (this.size() == 5) {
			for (int i = 0; i < 13; i++) {
				if (this.rankCounter()[i] == 3) { // If there are three cards of the same rank, that is the triplet rank.
					threeCards = true;
					tripletRank = i;
				}
				else if (this.rankCounter()[i] == 2) { // Checking if there are exactly two cards of the same rank.
					twoCards = true;
				}
			}
			if (threeCards && twoCards) { // If both conditions are satisfied, the hand is valid.
				return true;
			}
		}
		return false;
	}
	
	// A private method to find the triplet rank of this hand.
	private void findTripletRank() {
		for (int i = 0; i < 13; i++) {
			if (this.rankCounter()[i] == 3) {
				tripletRank = i;
			}
		}
	}
	
	/**
	 * A method for retrieving the top card of this hand.
	 * 
	 * @return The top card of this hand.
	 */
	public Card getTopCard() {
		Card topCard = new Card(0,2); // Creating a 3 of Diamonds (card of lowest hierarchy) for comparison.
		this.findTripletRank();
		for (int i = 0; i < this.size(); i++) {
			if (this.getCard(i).getRank() == tripletRank) { // Card can be top card only if it is of triplet rank;
				if (this.getCard(i).compareTo(topCard) == 1) {
					topCard = this.getCard(i);
				}
			}
		}
		return topCard;
	}
	
	/**
	 * A method for returning a string specifying the type of this hand.
	 * 
	 * @return A string saying "FullHouse".
	 */
	public String getType() {
		return "FullHouse";
	}
	
	/**
	 * A method for checking if this hand beats a specified hand.
	 *  
	 * @param hand The hand to compare this hand against.
	 * @return true if this hand beats the given hand, false otherwise.
	 */
	public boolean beats (Hand hand) {
		if ((hand.getType() == "Straight") || (hand.getType() == "Flush")) { // FullHouse beats Straight and Flush automatically.
			return true;
		}
		else if  ((this.size() == hand.size()) && (hand.getType() != "FullHouse")) { // Other five card hands beat FullHouse.
			return false;
		}
		else {
			int x = 0;
			if ((this.size() == hand.size()) && hand.isValid() && this.isValid()) { // If both hands are of type FullHouse, compare the top cards.
				x = this.getTopCard().compareTo(hand.getTopCard());
			}
			if (x == 1) {
				return true;
			}
			else
				return false;
		}
	}
}
