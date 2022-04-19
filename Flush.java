
/**
 * The Flush class is a subclass of the hand class and is used to model a hand of type Flush.
 * 
 * @author Md Abdullah Al Mahin
 */
public class Flush extends Hand {
	
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * 
	 * @param player The player playing the specified card.
	 * @param cards The specified list of cards the player is playing.
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * A method for checking if this is a valid Flush.
	 * 
	 * @return true if it is valid, false otherwise.
	 */
	public boolean isValid() {
		if (this.size() == 5) {
			for (int i = 0; i < 4; i++) {
				if (this.suitCounter()[i] == 5) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * A method for returning a string specifying the type of this hand.
	 * 
	 * @return A string saying "Flush".
	 */
	public String getType() {
		return "Flush";
	}
	
	/**
	 * A method for checking if this hand beats a specified hand.
	 *  
	 * @param hand The hand to compare this hand against.
	 * @return true if this hand beats the given hand, false otherwise.
	 */
	public boolean beats (Hand hand) {
		if (hand.getType() == "Straight") { // A Flush will always beat a Straight.
			return true;
		}
		else if  ((this.size() == hand.size()) && (hand.getType() != "Flush")) { // Other valid five cards hands always beat a hand of type Flush.
			return false;
		}
		else {
			int x = 0;
			if ((this.size() == hand.size()) && hand.isValid() && this.isValid()) { // If both hands are of type Flush, compare the top cards.
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
