
/**
 * The Pair class is a subclass of the hand class and is used to model a hand of type Pair.
 * 
 * @author Md Abdullah Al Mahin
 */
public class Pair extends Hand{
	
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * 
	 * @param player The player playing the specified card.
	 * @param cards The specified list of cards the player is playing.
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * A method for checking if this is a valid Pair.
	 * 
	 * @return true if it is valid, false otherwise.
	 */
	public boolean isValid() {
		if (this.size() == 2) {
			if ((this.getCard(0).getRank()) == (this.getCard(1).getRank())) { // If rank of both cards is the same, then hand is valid.
				return true;
			}
			else return false;
		}
		else
			return false;
	}
	
	/**
	 * A method for returning a string specifying the type of this hand.
	 * 
	 * @return A string saying "Pair".
	 */
	public String getType() {
		return "Pair";
	}
}
