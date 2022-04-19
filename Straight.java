
/**
 * The Straight class is a subclass of the hand class and is used to model a hand of type Straight.
 * 
 * @author Md Abdullah Al Mahin
 */
public class Straight extends Hand{
	
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * 
	 * @param player The player playing the specified card.
	 * @param cards The specified list of cards the player is playing.
	 */
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	// An array with the ranks arranged in terms of their hierarchy.
	private int[] rankComparer = {2,3,4,5,6,7,8,9,10,11,12,0,1};
	
	/**
	 * A method for checking if this is a valid Straight.
	 * 
	 * @return true if it is valid, false otherwise.
	 */
	public boolean isValid() {
		if (this.size() == 5) {
			for (int i = 0; i < this.size(); i++) {
				int currentCardRankIndex = this.findRankIndex(this.getCard(i).getRank()); // Finding the position of the rank of the current card in the hierarchy. 
				if (currentCardRankIndex < 9) {
					// If the next four cards of the consecutive ranks in the hierarchy are present, then this hand is a valid straight.
					if (this.isCardWithRankInHand(this.rankComparer[currentCardRankIndex+1]) && this.isCardWithRankInHand(this.rankComparer[currentCardRankIndex+2]) && this.isCardWithRankInHand(this.rankComparer[currentCardRankIndex+3]) && this.isCardWithRankInHand(this.rankComparer[currentCardRankIndex+4])) {
							return true;
						}
					}
				}
			}
		return false;
	}
	
	/**
	 * A method for returning a string specifying the type of this hand.
	 * 
	 * @return A string saying "Straight".
	 */
	public String getType() {
		return "Straight";
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
	 * A method for checking if this hand beats a specified hand.
	 *  
	 * @param hand The hand to compare this hand against.
	 * @return true if this hand beats the given hand, false otherwise.
	 */
	public boolean beats(Hand hand) {
		if  ((this.size() == hand.size()) && (hand.getType() != "Straight")) { // Other valid five cards hands always beat a hand of type Straight.
			return false;
		}
		else {
			int x = 0;
			if ((this.size() == hand.size()) && hand.isValid() && this.isValid()) { // If both hands are of type Straight, compare the top cards.
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
