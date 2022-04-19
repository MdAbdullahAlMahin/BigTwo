
/**
 * The Quad class is a subclass of the hand class and is used to model a hand of type Quad.
 * 
 * @author Md Abdullah Al Mahin
 */
public class Quad extends Hand{
	
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * 
	 * @param player The player playing the specified card.
	 * @param cards The specified list of cards the player is playing.
	 */
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	private int quadRank  = 0; // A private instance variable for storing which rank of this hand is the quartet rank.
	
	/**
	 * A method for checking if this is a valid Quad.
	 * 
	 * @return true if it is valid, false otherwise.
	 */
	public boolean isValid() {
		if (this.size() == 5) {
			for (int i = 0; i < 13; i++) {
				if (this.rankCounter()[i] == 4) {
					quadRank = i;
					return true;
				}
			}
		}
		return false;
	}
	
	// A private method to find the quartet rank of this hand.
	private void findQuadRank() {
		for (int i = 0; i < 13; i++) {
			if (this.rankCounter()[i] == 4) {
				quadRank = i;
			}
		}
	}
	
	/**
	 * A method for retrieving the top card of this hand.
	 * 
	 * @return The top card of this hand.
	 */
	public Card getTopCard() {
		Card topCard = new Card(0, 2); // Creating a 3 of Diamonds (card of lowest hierarchy) for comparison.
		findQuadRank();
		for (int i = 0; i < this.size(); i++) {
			if (this.getCard(i).getRank() == quadRank) { // Card can be top card only if it is of quartet rank;
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
	 * @return A string saying "Quad".
	 */
	public String getType() {
		return "Quad";
	}
	
	/**
	 * A method for checking if this hand beats a specified hand.
	 *  
	 * @param hand The hand to compare this hand against.
	 * @return true if this hand beats the given hand, false otherwise.
	 */
	public boolean beats (Hand hand) {
		if ((hand.getType() == "Straight") || (hand.getType() == "Flush") || (hand.getType() == "FullHouse")) { // Quad beats Straight, Flush, and FullHouse automatically.
			return true;
		}
		else if  ((this.size() == hand.size()) && (hand.getType() != "Quad")) { // Other five card hands beat Quad.
			return false;
		}
		else {
			int x = 0;
			if ((this.size() == hand.size()) && hand.isValid() && this.isValid()) { // If both hands are of type Quad, compare the top cards.
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
