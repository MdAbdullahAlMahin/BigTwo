
/**
 * The Single class is a subclass of the hand class and is used to model a hand of type Single.
 * 
 * @author Md Abdullah Al Mahin
 */
public class Single extends Hand{
	
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * 
	 * @param player The player playing the specified card.
	 * @param cards The specified list of cards the player is playing.
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * A method for checking if this is a valid Single.
	 * 
	 * @return true if it is valid, false otherwise.
	 */
	public boolean isValid() {
		if (this.size() == 1) {
			return true;
		}
		else
			return false;
	}
	
	/**
	 * A method for returning a string specifying the type of this hand.
	 * 
	 * @return A string saying "Single".
	 */
	public String getType() {
		return "Single";
	}
}
