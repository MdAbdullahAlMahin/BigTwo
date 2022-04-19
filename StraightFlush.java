
/**
 * The StraightFlush class is a subclass of the hand class and is used to model a hand of type StraightFlush.
 * 
 * @author Md Abdullah Al Mahin
 */
public class StraightFlush extends Hand{
	
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * 
	 * @param player The player playing the specified card.
	 * @param cards The specified list of cards the player is playing.
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
				super(player, cards);
	}
	
	// An array with the ranks arranged in terms of their hierarchy.
	private int[] rankComparer = {2,3,4,5,6,7,8,9,10,11,12,0,1};
	
	/**
	 * A method for checking if this is a valid StraightFLush.
	 * If a card is both a Straight and a Flush, it can be classified as Straight Flush.
	 * 
	 * @return true if it is valid, false otherwise.
	 */
	public boolean isValid() {
		boolean straight = false; // Boolean value to check whether the hand is a valid Straight.
		boolean flush = false; // Boolean value to check whether the hand is a valid Flush.
		if (this.size() == 5) {
			// Checking whether it is a valid Straight.
			for (int i = 0; i < this.size(); i++) {
				int currentCardRankIndex = this.findRankIndex(this.getCard(i).getRank()); // Finding the position of the rank of the current card in the hierarchy. 
				if (currentCardRankIndex < 9) {
					// If the next four cards of the consecutive ranks in the hierarchy are present, then this hand is a valid straight.
					if (this.isCardWithRankInHand(this.rankComparer[currentCardRankIndex+1]) && this.isCardWithRankInHand(this.rankComparer[currentCardRankIndex+2]) && this.isCardWithRankInHand(this.rankComparer[currentCardRankIndex+3]) && this.isCardWithRankInHand(this.rankComparer[currentCardRankIndex+4])) {
							straight = true;
					}
				}
			}
			// Checking whether it is a valid Flush.
			for (int i = 0; i < 4; i++) {
				if (this.suitCounter()[i] == 5) {
					flush = true;
				}
			}
		}
		// If the hand is both a Straight and a Flush, return true.
		if (straight && flush) {
			return true;
		}
		return false;
	}
	
	/**
	 * A public method to check if a card with the rank specified is in this hand.
	 * 
	 * @param rank The rank of the card to be searched for.
	 * @return true if a card with the rank is in the hand, false otherwise.
	 */
	public boolean isCardWithRankInHand(int rank) {
		for (int i = 0; i < this.size(); i++) {
			if (this.getCard(i).getRank() == rank)
					return true;
		}
		return false;
	}
	
	/**
	 * A method for returning a string specifying the type of this hand.
	 * 
	 * @return A string saying "StraightFlush".
	 */
	public String getType() {
		return "StraightFlush";
	}
	
	/**
	 * A method for checking if this hand beats a specified hand.
	 *  
	 * @param hand The hand to compare this hand against.
	 * @return true if this hand beats the given hand, false otherwise.
	 */
	public boolean beats (Hand hand) {
		
		if  ((this.size() == hand.size()) && (hand.getType() != "StraightFlush")) { // Other valid five cards hands are always beaten by StraightFlush.
			return true;
		}
		else {
			int x = 0;
			if ((this.size() == hand.size()) && hand.isValid() && this.isValid()) { // If both hands are of type StraightFlush, compare the top cards.
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
