
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * The BigTwoGUI class implements the CardGameUI interface and is used to provide the GUI for a BigTwo game.
 * 
 * @author Md Abdullah Al Mahin
 */
public class BigTwoGUI implements CardGameUI{
	
	private BigTwo game; // An object of the BigTwo class to provide the necessary materials for the GUI.
	private int activePlayer, lastPlayer; // Two integer variables to account for who is the current player and who played the last valid hand respectively.
	// A boolean array to check which cards in a players hand has been selected.
	private boolean[] selected = {false, false, false, false, false, false, false, false, false, false, false, false, false};
	private int thisPlayer;
	
	private JFrame frame; // The main window of the application.
	private JPanel chatPanel, basePanel, bottomPanel; // Three panels to hold the chat and message area, the main board of the game, and the panel for the play and pass buttons respectively.
	private ChatBox chatBox; // An object of the ChatBox inner class used to model the message area, chat area, and chat input.
	private JMenuBar menuBar; // A JMenuBar object to represent the menu bar of the GUI.
	private JMenu gameBar, messageBar; // Two JMenu objects to hold the items of the menu bar of the GUI. 
	private JMenuItem connect, quit, clear; // Three JMenuItems to perform the connect, quit, and clear messages functions respectively.
	private JLayeredPane boardPanel; // A JLayeredPane used to model the board on which the game is being played.
	private JButton play, pass; // Two JButtons to carry out the actions playing a hand and passing the turn respectively.
	
	private boolean enabled = true;
	static public final String title = "Big Two"; // The title of the game.
	static public final Color DEEP_GREEN = new Color(72, 150, 72); // A color used multiple times in the GUI.
	
	/**
	 * A public constructor for the GUI. Initializes all components and the frame and then adds the components to the frame.
	 * 
	 * @param game A BigTwo object to get the appropriate data for the GUI.
	 */
	public BigTwoGUI(BigTwo game) {
		this.game = game;
		this.frame = this.initializeFrame(); // Initializing the JFrame.
		this.chatPanel = this.initializeChatPanel();
		frame.add(chatPanel, BorderLayout.EAST);
		this.basePanel = initializeBasePanel();
		frame.add(basePanel, BorderLayout.CENTER);
		this.menuBar = this.initializeJMenuBar();
		frame.add(menuBar, BorderLayout.NORTH);
		frame.pack(); // Calling the pack function to properly organize all components.
		this.frame.setVisible(true);
		this.clearMsgArea();
	}
	
	// A private function used to initialize the main JFrame of the GUI.
	private JFrame initializeFrame() {
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(1050, 750)); // Setting a cap on the minimum size of the frame so that players cannot shorten the frame too much.
		return frame;
	}
	
	// A private method used to initialize and return the chatPanel. 
	private JPanel initializeChatPanel() {
		JPanel chatPanel = new JPanel();
		this.chatBox = this.initializeChatBox(); // Getting the ChatBox object and adding it to the panel.
		chatPanel.add(chatBox);
		return chatPanel;
	}
	
	// A private function to return an initialized object of the class ChatBox.
	private ChatBox initializeChatBox() {
		ChatBox chatBox = new ChatBox();
		return chatBox;
	}
	
	// A private function to return an initialized object of the main basePanel.
	private JPanel initializeBasePanel() {
		basePanel = new JPanel();
		bottomPanel = this.initializeBottomPanel(); // Getting the bottomPanel and adding it to this panel.
		boardPanel = this.initializeBoard(); // Getting the board and adding it to this panel.
		basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.Y_AXIS));
		basePanel.add(Box.createRigidArea(new Dimension(12, 5))); // Adding some space between components for aesthetic purposes.
		basePanel.add(boardPanel);
		basePanel.add(Box.createRigidArea(new Dimension(12, 4)));
		basePanel.add(bottomPanel);
		basePanel.add(Box.createRigidArea(new Dimension(12, 4)));
		return basePanel;
	}
	
	// A private function to return an initialized board to play the game on.
	private JLayeredPane initializeBoard() {
		JLayeredPane boardPanel = new JLayeredPane();
		boardPanel.setLayout(null);
		boardPanel.setBackground(DEEP_GREEN); // Set the color of the board to the previously initialized deep green color.
		boardPanel.setOpaque(true);
		boardPanel.setBorder(BorderFactory.createLineBorder(DEEP_GREEN, 3)); // Adding a border for aesthetic purposes.
		this.paintCards(boardPanel); // Adding the cards of all the players and the last played hand to the board.
		return boardPanel;
	}
	
	// A private function to add all the cards and the last played hand to the board.
	private synchronized void paintCards(JLayeredPane board) {
		// Iterate for all 4 players.
		for (int j = 0; j < this.game.getPlayerList().size(); j++) {
			JLabel player; // A JLabel to print out the name of the players on the board.
			JLabel playerImage = new JLabel(new ImageIcon(new ImageIcon("cards/" + (j+1) + ".png").getImage().getScaledInstance(80, 97, Image.SCALE_SMOOTH)));
			// For the current player, the text and its color is different.
			if (j == this.thisPlayer) {
				player = new JLabel("You");
				player.setForeground(Color.BLUE);
				if (this.thisPlayer == this.activePlayer) {
					player.setForeground(Color.GREEN);
				}
			}
			else {
				player = new JLabel(this.game.getPlayerList().get(j).getName());
				player.setForeground(Color.WHITE);
				if (j == this.activePlayer) {
					player.setForeground(Color.YELLOW);
				}
			}
			// Setting the position of the player names.
			player.setBounds(15, 3 + (122 * j), 73, 15);
			// Setting the position of the player icons.
			playerImage.setBounds(15, 20 + (122 * (j)), 80, 97);
			// Giving the player icons a border foe aesthetic purposes.
			//playerImage.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			player.setOpaque(true);
			// Setting the background of the text to be the same color as the board.
			player.setBackground(DEEP_GREEN);
			board.add(player);
			board.add(playerImage);
			// For a player, iterate over for how many cards a player has in his hands.
			for (int i = 0; i < this.game.getPlayerList().get(j).getNumOfCards(); i++) {
				PlayingCard temp; // A placeholder variable needed to initialize a playing card to the GUI.
				// If the player the gui is representing is the same as the player during the current iteration, print the side of the card showing the suit and rank.
				if (j == this.thisPlayer && this.game.getPlayerList().get(j).getName() != "" && this.game.getClient().connected()) {
					int suit = this.game.getPlayerList().get(j).getCardsInHand().getCard(i).getSuit();
					int rank = this.game.getPlayerList().get(j).getCardsInHand().getCard(i).getRank();
					Image image = new ImageIcon(this.returnPath(suit, rank)).getImage(); // Get the path to the image.
					temp = new PlayingCard(150 + ((i) * 30) , 20 + (122 * (j)), i, new ImageIcon(image)); // Initializing a new playing card.
				}
				else {
					temp = new PlayingCard(150 + ((i) * 30) , 20 + (122 * (j)), i, new ImageIcon("cards/b.gif"));
				}
				// Setting the index of the card, where the index is the position of the card in the players hand.
				temp.index = i;
				// Indication which player has this card in his/her hand.
				temp.player = j;
				board.add(temp, temp.depth);
			}
			
		}
		
		// Printing out the last played hand to the GUI is there is a last played hand.
		if (this.game.getHandsOnTable().size() != 0) {
			// Some text for aesthetic purposes.
			JLabel player;
			if (this.lastPlayer == this.thisPlayer) {
				player = new JLabel("Previous Hand played by You");
				player.setForeground(Color.BLUE);
			}		
			else {
				player = new JLabel("Previous Hand played by " + this.game.getPlayerList().get(this.lastPlayer).getName());
				player.setForeground(Color.WHITE);
			}
			// Setting the specifics of the text.
			player.setBounds(15, 500, 200, 15);
			player.setOpaque(true);
			player.setBackground(DEEP_GREEN);
			
			boardPanel.add(player);
			
			// Printing out the previous hand to the board.
			for (int i = 0; i < this.game.getHandsOnTable().get(this.game.getHandsOnTable().size() - 1).size(); i++) {
				PlayingCard temp; // A placeholder variable needed to initialize a card.
				// Getting the suit and rank of each card.
				int suit = this.game.getHandsOnTable().get(game.getHandsOnTable().size() - 1).getCard(i).getSuit();
				int rank = this.game.getHandsOnTable().get(game.getHandsOnTable().size() - 1).getCard(i).getRank();
				temp = new PlayingCard(15 + (80 * i), 520, 0, new ImageIcon(this.returnPath(suit, rank))); // Initializing a card of the last played hand.
				temp.player = -1; // Setting the player to -1 so that the card cannot be selected.
				boardPanel.add(temp, temp.depth);
			}
		}
	}
	
	/**
	 * A public method to get the String version of the cards of a Hand object. 
	 * 
	 * @param hand The Hand object to get the string for.
	 * @return The string form of the cards of the Hand object. 
	 */
	public String getStringOfLastHand(Hand hand) {
		String s = "";
		for (int i = 0; i < hand.size(); i++) {
			s += hand.getCard(i) + "  ";
		}
		return s;
	}
	
	// A private method used for debugging purposes.
	private void printSelected() {
		this.printMsg(this.selected[0] + " " +  this.selected[1] + " " + this.selected[2] + " " + this.selected[3] + " " + this.selected[4] + " " + this.selected[5] + " " + this.selected[6] + " " + this.selected[7] + " " + this.selected[8] + " " + this.selected[9] + " " + this.selected[10] + " " + this.selected[11] + " " + this.selected[12]);
	}
	
	// A private function to return an initialized bottomPanel containing the two game buttons.
	private JPanel initializeBottomPanel() {
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		this.play = new JButton("Play"); // Creating the play button.
		play.addActionListener(new PlayButtonActions()); // Adding the listener for the play button.
		this.pass = new JButton("Pass"); // Creating the pass button.
		pass.addActionListener(new PassButtonActions()); // Adding the listener for the pass button.
		bottomPanel.add(play);
		bottomPanel.add(Box.createRigidArea(new Dimension(12, 0)));
		bottomPanel.add(pass);
		return bottomPanel;
	}
	
	/**
	 * A Listener of type ActionListener to account for the necessary actions related to the play button.
	 * 
	 * @author Md Abdullah Al Mahin
	 */
	class PlayButtonActions implements ActionListener{
		
		/**
		 * A public function to accounts for the events when the player presses the play button.
		 * 
		 * @param e The action of pressing the button.
		 */
		public void actionPerformed(ActionEvent e) {
				// Get the list of selected cards.
				int[] cardIdx = getSelected();
				// If some amount of cards are selected, make the move.
				if (cardIdx != null) {
					game.makeMove(activePlayer, cardIdx);
				}
				// Else print out an error message.
				else {
					printMsg("No cards have been selected, pass if necessary!");
				}	
		}
	}

	/**
	 * A Listener of type ActionListener to account for the necessary actions related to the pass button.
	 * 
	 * @author Md Abdullah Al Mahin
	 */
	class PassButtonActions implements ActionListener{
		
		/**
		 * A public function to accounts for the events when the player presses the pass button.
		 * 
		 * @param e The action of pressing the button.
		 */
		public void actionPerformed(ActionEvent e) {
				// Make the move with an empty list of cards.
				int[] cardIdx = null;
				game.makeMove(activePlayer, cardIdx);
		}
	}
	
	// A private function to return an initialized menu bar.
	private JMenuBar initializeJMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		gameBar = new JMenu("Game");
		messageBar = new JMenu("Messages");
		quit = new JMenuItem("Quit");
		quit.addActionListener(e -> {this.quit();}); // Adding an action listener to the quit item which exits the program on activation.
		connect = new JMenuItem("Connect");
		connect.addActionListener(e -> {this.connectAgain();}); // Adding an action listener to the connect item which resets the GUI on activation.
		clear = new JMenuItem("Clear Messages");
		clear.addActionListener(e -> {this.chatBox.chatArea.setText("");}); // Adding an action listener to the clear item which clears the chat area on activation.
		
		// Adding all the components and returning the final menu bar.
		gameBar.add(connect);
		gameBar.add(quit);
		messageBar.add(clear);
		menuBar.add(gameBar);
		menuBar.add(messageBar);
		
		return menuBar;
	}
	
	// A private method carrying out the function of the connect button.
	private void connectAgain(){
		if (!this.game.getClient().connected()) {
			this.game.getClient().connect();
		}
	}
	
	/**
	 * A public method which closes the gui upon being called.
	 */
	public void quit() {
		System.exit(0);
	}

	/**
	 * A setter function to update the value of activePlayer.
	 * 
	 * @param activePlayer The player to change the value of activePlayer to.
	 */
	public void setActivePlayer(int activePlayer) {
		this.activePlayer = activePlayer;
	}
	
	/**
	 * A getter function to get the value of activePlayer.
	 * 
	 * @return The value of activePlayer.
	 */
	public int getActivePlayer() {
		return this.activePlayer;
	}
	
	/**
	 * A public setter to set the index of the player the gui is representing.
	 * 
	 * @param thisPlayer The value to set to.
	 */
	public void setThisPlayer(int thisPlayer) {
		this.thisPlayer = thisPlayer;
	}
	
	/**
	 * A public getter to get the index of the player the gui is representing.
	 * 
	 * @return The value of thisPlayer
	 */
	public int getThisPlayer() {
		return this.thisPlayer;
	}
	
	/**
	 * A public setter to set the index of the player who played the last move.
	 * 
	 * @param lastPlayer The value to set to.
	 */
	public void setLastPlayer(int lastPlayer) {
		this.lastPlayer = lastPlayer;
	}
	
	// A private method to get a random color if necessary.
	// Used for debugging purposes.
	private Color generateRandomColor() {
		Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		return (new Color(r,g,b));
	}
	
	/**
	 * A function to repaint the board the game is being played to reflect the progress of the game.
	 */
	public synchronized void repaint() {
		this.disable();
		// Enabling only the current player to make moves.
		if (this.thisPlayer == this.activePlayer) {
			this.enable();
		}
		this.boardPanel.removeAll(); // Removing all components from the board.
		boardPanel.setLayout(null);
		boardPanel.setBackground(DEEP_GREEN);
		boardPanel.setOpaque(true);
		boardPanel.setBorder(BorderFactory.createLineBorder(DEEP_GREEN, 3));
		this.paintCards(boardPanel); // Repainting the cards on the board.
	}
	
	/**
	 * A function to print a string to the message area.
	 * 
	 * @param msg The string to be printed to the message area.
	 */
	public void printMsg(String msg) {
		this.chatBox.msgArea.append(msg);
		this.chatBox.msgArea.append("\n");
	}
	
	/**
	 * A function to clear the message area of its contents.
	 */
	public void clearMsgArea() {
		this.chatBox.msgArea.setText("");
	}
	
	/**
	 * A method for resetting the GUI.
	 */
	public void reset() {
		this.chatBox.chatArea.setText("");
		this.resetSelected();
	}
	
	/**
	 * A method for disabling user interactions with the GUI.
	 */
	public void disable() {		
		this.enabled = false;
		this.boardPanel.setEnabled(enabled);
		this.play.setEnabled(enabled);		
		this.pass.setEnabled(enabled);
	}
	
	/**
	 * A method for enabling user interactions with the GUI.
	 */
	public void enable() {
		this.enabled = true;
		this.boardPanel.setEnabled(enabled);
		this.play.setEnabled(enabled);		
		this.pass.setEnabled(enabled);
	}
	
	/**
	 * A method for prompting the active player to select cards and make his/her move.
	 */
	public void promptActivePlayer() {
		this.printMsg(game.getPlayerList().get(this.activePlayer).getName() + "'s turn: ");
	}
	
	// A private method to get the indexes of the cards the player selected to play.
	private int[] getSelected() {
		int[] cardIdx = null; // Initialize the array to hold the cards.
		int count = 0; // Counting how many cards the player selected.
		for (int j = 0; j < selected.length; j++) {
			if (selected[j]) {
				count++;
			}
		}
		
		// Getting the indexes of the selected cards.
		if (count != 0) {
			cardIdx = new int[count];
			count = 0;
			for (int j = 0; j < selected.length; j++) {
				if (selected[j]) {
					cardIdx[count] = j;
					count++;
				}
			}
		}
		return cardIdx;
	}
	
	/**
	 * A public method to reset the value of the array selected for use of the next player.
	 */
	public void resetSelected() {
		for (int j = 0; j < selected.length; j++) {
			selected[j] = false;
		}
	}
	
	/**
	 * An inner class which extends JLabel and implements a MouseListener, used to model a card on the GUI.
	 * 
	 * @author Md Abdullah Al Mahin
	 */
	private class PlayingCard extends JLabel implements MouseListener{
		
		public int x, y, depth, index, player; // Integer values to record the x and y coordinates, depth of the JLabel in a JLayeredPane, index of the card in a hand, and the player the card belongs to, respectively.
		public boolean clicked = false; // A boolean variable to check whether the card has been clicked or not.
		
		/**
		 * A public constructor that initializes the Card.
		 * 
		 * @param x The x position of the card in the screen coordinate system.
		 * @param y The y position of the card in the screen coordinate system.
		 * @param depth The depth of the card in a JLayeredPane.
		 * @param icon The image on the face of the card.
		 */
		public PlayingCard(int x, int y, int depth, ImageIcon icon){
			this.x = x;
			this.y = y;
			this.depth = depth;
			this.setIcon(icon);
			// Setting the position of the card.
			this.setBounds(this.x, this.y, this.getIcon().getIconWidth(), this.getIcon().getIconHeight());
			this.addMouseListener(this);
		}
		
		// Dummy function not used.
		public void mouseEntered(MouseEvent e){
		}
		
		/**
		 * A function to account for the events happening if the card is clicked.
		 * 
		 * @param e A mouse event.
		 */
		public void mousePressed(MouseEvent e){
			// Card will only be selected if it belongs to the current player.
			if (player == activePlayer && activePlayer == thisPlayer) {
				// If the card has not been clicked, raise it up.
				if (!clicked) {
					this.setBounds(this.x, this.y - 15, this.getIcon().getIconWidth(), this.getIcon().getIconHeight());
					clicked = true;
					selected[this.index] = true; // Set selected[card's index] to true to account for the player move.
				}
				// If the card has been clicked, lower it.
				else {
					this.setBounds(this.x, this.y, this.getIcon().getIconWidth(), this.getIcon().getIconHeight());	
					clicked = false;
					selected[this.index] = false; // Set selected[card's index] to false to remove the card from the player move.
				}		
			}
		}
		
		// Dummy function not used.
		public void mouseReleased(MouseEvent e){	
		}
		
		// Dummy function not used.
		public void mouseExited(MouseEvent e){
		}
		
		// Dummy function not used.
		public void mouseClicked(MouseEvent e){
		}
	}
	
	/**
	 * A public method which outputs the contents of the string chat to the chat area of the gui.
	 * 
	 * @param chat The string to be outputted.
	 */
	public void printChat(String chat) {
		this.chatBox.chatArea.append(chat); // If the enter/return key is pressed, append the string in the input to the chat area.
		this.chatBox.chatArea.append("\n");
	}
	
	/**
	 * An inner class which extends JPanel and implements a KeyListener, used to model the different text areas on the GUI.
	 * 
	 * @author Md Abdullah Al Mahin
	 */
	private class ChatBox extends JPanel implements KeyListener{
		private JTextField chatInput; // The component to take in input.
		private JTextArea chatArea; // The component to display the chat of all the players.
		private JScrollPane scroll; // To make the text areas able to be scrolled through.
		private JTextArea msgArea; // The component to display the game messages.
		private static final int GAME_COLUMNS = 35; // A final variable accounting for the number of columns of the different areas.
		
		/**
		 * A public constructor to initialize a ChatBox object and its components.
		 */
		public ChatBox() {
			msgArea = new JTextArea(20, GAME_COLUMNS);
			msgArea.setEditable(false); // So that players cannot directly type into the message area.
			msgArea.setLineWrap(true); // So that the GUI fits all the text inside the visible area.
			msgArea.setWrapStyleWord(true);
			scroll = new JScrollPane(msgArea); // Making the text area able to be scrolled through.
			scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scroll.setBorder(BorderFactory.createTitledBorder("Game Status"));
			// Making the scrolling follow the latest input.
			DefaultCaret caret = (DefaultCaret)msgArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(scroll);
			this.add(Box.createRigidArea(new Dimension(10, 4))); // For aesthetic purposes.
			
			chatInput = new JTextField(GAME_COLUMNS + 1);
			chatInput.addKeyListener(this); // Adding a listener.
			JPanel temp = new JPanel();
			temp.add(chatInput);
			temp.setBorder(BorderFactory.createTitledBorder("Message"));
			chatArea = new JTextArea(19, GAME_COLUMNS);
			chatArea.setEditable(false); // So that players cannot directly type into the chat area.
			chatArea.setLineWrap(true); // So that the GUI fits all the text inside the visible area.
			chatArea.setWrapStyleWord(true);
			chatArea.setForeground(Color.BLUE);
			scroll = new JScrollPane(chatArea); // Making the text area able to be scrolled through.
			scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			scroll.setBorder(BorderFactory.createTitledBorder("Chat Area"));
			// Making the scrolling follow the latest input.
			DefaultCaret caret1 = (DefaultCaret)chatArea.getCaret();
			caret1.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			this.add(scroll);
			this.add(Box.createRigidArea(new Dimension(10, 5))); // For aesthetic purposes.
			this.add(temp);
		}
		
		/**
		 * A public function to account for what happens when the return/enter key is pressed.
		 * 
		 * @param e A key press.
		 */
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Checking if the key pressed is the enter/return key.
				if (!this.chatInput.getText().equals("")) {
					// Broadcasting the message.
					game.getClient().sendMessage(new CardGameMessage(CardGameMessage.MSG, -1, this.chatInput.getText()));
					this.chatInput.setText("");
				}
			}
		}
		
		// Dummy function not used.
		public void keyReleased(KeyEvent e) {
		}
		
		// Dummy function not used.
		public void keyTyped(KeyEvent e) {
		}
	}
	
	// A function to the return the path to the image of the card of particular suit and rank.
	private String returnPath(int  suit, int rank) {
		return ("cards/" + returnCardRank(rank) + returnCardSuit(suit) + ".gif");
	}
	
	// Used in finding the name of the image of a card.
	private String returnCardSuit(int p) {
		if (p == 0) {
			return "d";
		}
		else if (p == 1) {
			return "c";
		}
		else if (p == 2) {
			return "h";
		}
		else {
			return "s";
		}
	}
	
	// Used in finding the name of the image of a card.
	private String returnCardRank(int p) {
		if (p == 0) {
			return "a";
		}
		else if (p == 1) {
			return "2";
		}
		else if (p == 2) {
			return "3";
		}
		else if (p == 3) {
			return "4";
		}
		else if (p == 4) {
			return "5";
		}
		else if (p == 5) {
			return "6";
		}
		else if (p == 6) {
			return "7";
		}
		else if (p == 7) {
			return "8";
		}
		else if (p == 8) {
			return "9";
		}
		else if (p == 9) {
			return "10";
		}
		else if (p == 10) {
			return "j";
		}
		else if (p == 11) {
			return "q";
		}
		else {
			return "k";
		}
	}
	
	/**
	 * A public method to output the end of game texts using a message pane.
	 */
	public void printEndingMessage() {
		// Getting the contents for each individual end of game message.
		String s = " Number of cards in each players hand = { ";
		
		for (int i = 0; i < 4; i++) {
			s += this.game.getPlayerList().get(i).getNumOfCards() + ", ";
		}
		
		// Show the appropriate end of game message.
		if (this.getThisPlayer() == this.game.getWinnerIndex()) {
			JOptionPane.showMessageDialog(this.frame, s + " } ", "You Win!", JOptionPane.PLAIN_MESSAGE);
		}
		else {
			JOptionPane.showMessageDialog(this.frame, s + " }", "You Lost.", JOptionPane.PLAIN_MESSAGE);
		}
		// Broadcast to everyone that the player is ready to play a new game.
		this.game.getClient().sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
	}
	
	
	public void printWinningMSG() {
		int winner = -1;
		for (int i = 0; i < 4; i++) {
			if (this.game.getPlayerList().get(i).getNumOfCards() == 0) {
				winner = i;
			}
		}
		String msg = "";
		for (int i = 0; i < 4; i++) {
			if (winner == i && this.thisPlayer == winner) {
				msg += "You win \n";
			}
			else if (winner == i && this.thisPlayer != winner) {
				msg+= this.game.getPlayerList().get(i).getName() + " wins \n";
			}
			else if (winner != i && this.thisPlayer != i) {
				msg+= this.game.getPlayerList().get(i).getName() + " have " + this.game.getPlayerList().get(i).getNumOfCards() + " cards in hand\n";
			}
			else if (winner != i && this.thisPlayer == i) {
				msg+= "You have " + this.game.getPlayerList().get(i).getNumOfCards() + " cards in hand\\n";
			}
		}
		
		if (winner == this.thisPlayer) {
			JOptionPane.showMessageDialog(this.frame, msg, "You Win", JOptionPane.PLAIN_MESSAGE);
		}
		else {
			JOptionPane.showMessageDialog(this.frame, msg, "You Loose", JOptionPane.PLAIN_MESSAGE);
		}
		this.game.getClient().sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
	}
}



