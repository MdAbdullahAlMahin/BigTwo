
import java.net.*;
import java.io.*;
import java.lang.Thread;
import javax.swing.*;
import java.util.*;

/**
 * The BigTwoClient class implements the NetworkGame interface. It is used to model a Big 
 * Two game client that is responsible for establishing a connection and communicating with 
 * the Big Two game server.
 * 
 * @author Md Abdullah Al Mahin
 */
public class BigTwoClient implements NetworkGame{
	
	private BigTwo game; // A BigTwo object for the Big Two card game.
	private BigTwoGUI gui; // BigTwoGUI object for the Big Two card game.
	private Socket sock; // A socket connection to the game server.
	private ObjectOutputStream oos; // An ObjectOutputStream for sending messages to the server.
	private int playerID; // An  integer specifying the playerID (i.e., index) of the local player.
	private String playerName; // A string specifying the name of the local player.
	private String serverIP; // A string specifying the IP address of the game server.
	private int serverPort; // An integer specifying the TCP port of the game server.
	private ObjectInputStream ois; // An ObjectInputStream for receiving messages from the server.
	
	/**
	 * A constructor for creating a Big Two client. 
	 * The first parameter is a reference to a BigTwo object associated with this client 
	 * and the second parameter is a reference to a BigTwoGUI object associated the BigTwo 
	 * object.
	 * 
	 * @param game The BigTwo object linked to the client
	 * @param gui The BigTwoGUI object associated the BigTwo object.
	 */
	public BigTwoClient(BigTwo game, BigTwoGUI gui) {
		// Asking for a name from the player until player inputs a valid name.
		this.playerName = JOptionPane.showInputDialog("Enter A Name:");
		this.game = game;
		String regex = "^[a-zA-Z]*$";
		while (this.playerName == null || this.playerName.equals("") || !(this.playerName.substring(0, 1).matches(regex))) {
			this.playerName = JOptionPane.showInputDialog("Plase Enter A Name (Make sure you begin with an alphabet!)");	
		}
		this.gui = gui;
		// Connecting to the server.
		this.connect();
	}
	
	/**
	 * A method for getting the playerID (i.e., index) of the local player.
	 * 
	 * @return The playerID.
	 */
	public int getPlayerID() {
		return this.playerID;
	}

	/**
	 * A method for setting the playerID (i.e., index) of the local player.
	 * 
	 * @param playerID The index of the local player.
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	/**
	 * A method for getting the name of the local player.
	 * 
	 * @return The playerName.
	 */
	public String getPlayerName() {
		return this.playerName;
	}

	/**
	 * A method for setting the name of the local player.
	 * 
	 * @param playerName The name of the local player.	
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * A method for getting the IP address of the game server.
	 * 
	 * @return The serverIP.
	 */
	public String getServerIP() {
		return this.serverIP;
	}

	/**
	 * A method for setting the IP address of the game server.
	 * 
	 * @param serverIP The IP of the server to be set.
	 */
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	/**
	 * A method for getting the TCP port of the game server.
	 * 
	 * @return The serverPort.
	 */
	public int getServerPort() {
		return this.serverPort;
	}

	/**
	 * A method for setting the TCP port of the game server.
	 * 
	 * @param serverPort The TCP of the server. 
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	/**
	 * A method for setting the TCP port of the game server.
	 */
	public synchronized void connect() {
		try {
			// Making the connection to the server.
			sock = new Socket("127.0.0.1", 2396);
			// Making output and input streams for communication with the server.
			ois = new ObjectInputStream(sock.getInputStream());
			oos = new ObjectOutputStream(sock.getOutputStream());
			
			// Reading the first message (PLAYER_LIST).
			GameMessage message = (CardGameMessage)this.ois.readObject();
			if (message.getType() == CardGameMessage.PLAYER_LIST) {
				this.parseMessage(message);
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		// Starting the thread.
		Thread thread = new Thread(new ServerHandler());
		thread.start();
	}

	/**
	 * An inner class that implements the Runnable interface, made to be a part of the thread which interacts with the server.
	 * 
	 * @author Md Abdullah Al Mahin
	 */
	class ServerHandler implements Runnable{
		
		/**
		 * The run method of the Runnable interface.
		 */
		public void run() {
			try {
				// While there is a connection, listen for messages from the server.
				while (sock.isConnected()) {
					parseMessage((CardGameMessage)ois.readObject());
				}
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
	}
	
	/**
	 * A method for checking if the socket is still connected to the server.
	 * 
	 * @return A boolean value based on whether there is a connection or not.
	 */
	public boolean connected() {
		return this.sock.isConnected();
	}
	
	/**
	 * A method for parsing the messages received from the game server.
	 * 
	 * @param message The message to be parsed.
	 */
	public synchronized void parseMessage(GameMessage message) {
		// Check for the different types of messages, and carrying out the tasks related to them.
		if (message.getType() == CardGameMessage.JOIN) {
			if (message.getPlayerID() == this.playerID) {
				try {
					this.sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			else {
				this.game.getPlayerList().get(message.getPlayerID()).setName((String) message.getData());
				this.game.getGUI().repaint();
			}
			
		}
		else if (message.getType() == CardGameMessage.FULL) {
			this.gui.printMsg("Server full! Cannot join the game.");
		}
		else if (message.getType() == CardGameMessage.READY) {
				this.gui.printMsg(this.game.getPlayerList().get(message.getPlayerID()).getName() + " is ready.");
		}
		else if (message.getType() == CardGameMessage.START) {
			this.game.getGUI().clearMsgArea();
			this.game.getGUI().printMsg("Game Starts\n"); 
			this.game.start((BigTwoDeck)message.getData()); // The game is started.
		}
		else if (message.getType() == CardGameMessage.MOVE) {
			this.game.checkMove(message.getPlayerID(), (int[])message.getData()); // The move is checked.
		}
		else if (message.getType() == CardGameMessage.MSG) {
			this.gui.printChat((String) message.getData()); // The string is printed to the chat area.
		}
		else if (message.getType() == CardGameMessage.QUIT) {
			this.gui.printMsg(this.game.getPlayerList().get(message.getPlayerID()).getName() + ", " + message.getData() + " disconnected.");
			this.game.getPlayerList().get(message.getPlayerID()).setName(""); // Setting their names to blank.
			if (!this.game.endOfGame()) {
				this.game.getGUI().repaint();
				this.game.getGUI().disable();
			}
			this.sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
		}
		else if (message.getType() == CardGameMessage.PLAYER_LIST) {
			this.setPlayerID(message.getPlayerID()); // Setting the playerID.
			String[] temp = (String[]) message.getData();
			// Adding all the players before the local player. 
			for (int i = 0; i < 4; i++) {
				if (temp[i] != null) {
					this.game.getPlayerList().add(new CardGamePlayer(temp[i]));
				}
				else {
					this.game.getPlayerList().add(new CardGamePlayer(""));
				}
			}
			// Adding the local player.
			this.game.getPlayerList().get(this.playerID).setName(this.playerName);
			// Send a message that local player is joining.
			this.sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, this.playerName));
			// Setting the index of the local player to the gui.
			this.game.getGUI().setThisPlayer(this.playerID);
			this.game.getGUI().repaint();
		}
	}

	/**
	 * A method for sending the specified message to the game server.
	 * 
	 * @param message The message to be sent. 
	 */
	public synchronized void sendMessage(GameMessage message) {
		// Checking the type of message then sending it to the server.
		if (message.getType() == CardGameMessage.READY) {
			try {
				this.oos.writeObject(new CardGameMessage(CardGameMessage.READY, -1, null));	
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		else if (message.getType() == CardGameMessage.MOVE) {
			try {
				this.oos.writeObject(new CardGameMessage(CardGameMessage.MOVE, -1, (int[])message.getData()));
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		else if (message.getType() == CardGameMessage.MSG) {
			try {
				this.oos.writeObject(new CardGameMessage(CardGameMessage.MSG, -1, (String)message.getData()));
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		else if (message.getType() == CardGameMessage.JOIN) {
			try {
				this.oos.writeObject(new CardGameMessage(CardGameMessage.JOIN, -1, this.playerName));
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
}
