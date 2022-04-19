
/**
 * The BigTwoDeck class is a subclass of the Deck class and is used to model a deck of cards 
 * used in a Big Two card game.
 * 
 * @author Md Abdullah Al Mahin
 */
public class BigTwoDeck extends Deck {
	/**
	 * A method for initializing a deck of Big Two cards.
	 */
	public void initialize() {
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard card = new BigTwoCard(i, j); // Creating all possible cards in terms of a BigTwo card and then adding them to the deck.
				addCard(card);
			}
		}
	}
	
}
