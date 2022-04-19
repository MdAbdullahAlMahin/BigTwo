
/**
 * The Triple class is a subclass of the hand class and is used to model a hand of type Triple.
 * 
 * @author Md Abdullah Al Mahin
 */
public class Triple extends Hand {
	
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * 
	 * @param player The player playing the specified card.
	 * @param cards The specified list of cards the player is playing.
	 */
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * A method for checking if this is a valid Triple.
	 * 
	 * @return true if it is valid, false otherwise.
	 */
	public boolean isValid() {
		if (this.size() == 3) {
			for (int i = 0; i < 13; i++) {
				if (this.rankCounter()[i] == 3) { // If the cards are all of same rank, this triple is valid.
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * A method for returning a string specifying the type of this hand.
	 * 
	 * @return A string saying "Triple".
	 */
	public String getType() {
		return "Triple";
	}
	
}
