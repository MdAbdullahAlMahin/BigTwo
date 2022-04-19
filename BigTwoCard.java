
/**
 * The BigTwoCard class is a subclass of the Card class and is used to model 
 * a card used in a Big Two card game.
 * 
 * @author Md Abdullah Al Mahin
 */
public class BigTwoCard extends Card {
	
	public BigTwoCard(int suit, int rank) {
		super(suit, rank);
	}

	// An array used in the compareTo method, it is a array with the ranks arranged in terms of their hierarchy.
    int[] rankComparer = {2,3,4,5,6,7,8,9,10,11,12,0,1};
    // An array used in the compareTo method, it is a array with the suits arranged in terms of their hierarchy.
    int[] suitComparer = {0,1,2,3};
    
    // A private method to find the position of a rank in the hierarchy of ranks.
    private int findRankIndex(int rank) {
    	for (int i = 0; i < 13; i++) {
    		if (rankComparer[i] == rank) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    // A private method to find the position of a suit in the hierarchy of suit.
    private int findSuitIndex(int suit) {
    	for (int i = 0; i < 4; i++) {
    		if (suitComparer[i] == suit) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    /**
     * A method for comparing the order of this card with the specified card.
     * Returns a negative integer, zero, or a positive integer when this card is 
     * less than, equal to, or greater than the specified card.
     * 
     * @param card The card to compare this card with.
     * @return -1, 0, or 1 when this card is less than, equal to, or greater than the specified card.
     */
    public int compareTo(Card card) {
    	// Finding the rank and suit hierarchy of both the cards and then comparing based on it.
    	int thisRank = findRankIndex(this.rank); 
    	int cardRank = findRankIndex(card.getRank());
    	int thisSuit = findSuitIndex(this.suit);
    	int cardSuit = findSuitIndex(card.getSuit());
    	if (thisRank > cardRank) { // Rank hierarchy is compared first.
			return 1;
		} else if (thisRank < cardRank) {
			return -1;
		} else if (thisSuit > cardSuit) { // If rank is equal, suit hierarchy is compared.
			return 1;
		} else if (thisSuit < cardSuit) {
			return -1;
		} else {
			return 0;
		}
    }
    
}
