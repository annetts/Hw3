package ee.ut.cs.ds.client;

import static java.lang.String.format;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import ee.ut.cs.ds.Utils;
import ee.ut.cs.ds.client.characters.AGameCharacter;
import ee.ut.cs.ds.client.characters.GameFly;
import ee.ut.cs.ds.client.characters.GameFrog;
import ee.ut.cs.ds.server.Engine;
import ee.ut.cs.ds.server.Server;
import ee.ut.cs.ds.server.Worker;

/**
 * Connects client to server.
 * @author rauno
 *
 */
public class Connector {

	private List<AGameCharacter> ch;
	private OutputStream outputStream = null;
    private BufferedReader inputStream = null;
    private String cache; // if chache to check if something is changed
	private Socket socket;

    
	public Connector(List<AGameCharacter> characters) throws IOException {
		this.ch = characters;
		this.cache = "";
	}
    
	/**
	 * Starts the main loop of connector. Connector waits all character
	 * positions form client and refreshed list of characters after every
	 * message.
	 * 
	 * @throws IOException
	 */
	public void startGame() throws IOException {
        Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
		        while (true) {
		        	if (inputStream != null) {
						try {
							String characterPositions = Utils.readMessage(inputStream);
			                setCharacters(characterPositions);    
						} catch (IOException e) {
						}
		        	}
		        }
			}
		});
        t.start();
	}
	/**
	 * Connectst to host.
	 * @param host
	 * @throws IOException
	 */
	public void connect(String host) throws IOException {
        if (host == null) {
            System.err.println(format("Usage: java %s <host>", Connector.class.getCanonicalName()));
            System.exit(1);
        }
        
        socket = null;
        
        try {
            socket = new Socket(host, Server.PORT);
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = socket.getOutputStream();

            //TODO idk. if this here look correct place for this, but difficult to improve.
            String mesage = Utils.readMessage(inputStream);
            System.out.println(mesage);
            String[] dimentions = mesage.split("\\|");
            Constants.grid_width = Integer.parseInt(dimentions[0]);
            Constants.grid_height = Integer.parseInt(dimentions[1]);
            Utils.logger("Set Game grid size to: " + Constants.grid_width + "x" + Constants.grid_height, false);
        } catch (Exception e) {
			Utils.logger("try again", false);
		}
    }
	
	
	public void startServer() throws IOException {
    	// Initialize socket
        ServerSocket ssocket = new ServerSocket(Server.PORT);
        
        // Start game engine thread
        Thread engine = new Thread(new Engine());
        engine.start();
        
        try {
            Utils.logger("Waiting for a connection on port " + Server.PORT, false);
            while (true) {
                // Wait new connections
                Socket socket = ssocket.accept();
                Utils.logger("connected client: " + socket.getRemoteSocketAddress(), false);
                // Create a thread from each client.
                Thread thread = new Thread(new Worker(socket));
                thread.start();
            }
        } finally {
            ssocket.close();
        }
	}
    
    /**
     * Send character type to server.
     * @param character - frog or fly
     */
    public void sendCharacter(String character) {
    	try {
			Utils.writeMessage(outputStream, character);
		} catch (IOException e) {
			Utils.logger("Unable to send message: " + e, true);
		}
    }
    
    /**
     * Send main character position to server.
     * @param x
     * @param y
     */
    public void sendPosition(int x, int y) {
    	//TODO maybe do check if movement is valid.
    	try {
			Utils.writeMessage(outputStream, x + "|" + y);
		} catch (IOException e) {
			Utils.logger("Unable to send message: " + e, true);
		}
    }

    // Updates the list of characters to be drawn on screen.
	private void setCharacters(String characterPositions) {
		// optimization, if nothing is changed don't update characters!
		if (characterPositions.equals(cache)) {
			return;
		}
		if (characterPositions.equals("")) {
			System.exit(0);
		}
		// update Cache
		cache = characterPositions;
		
		// parse the list of characters.
		String[] data = characterPositions.split("#");	
		
		// clear the current character list.
		ch.clear();
		// populate new data to character list.
		for (int i = 1; i < data.length; i++) {
			// parse character coorinates
			String[] coords = data[i].split("\\|");
			
			// create new character object according to server message. 
			AGameCharacter c;
			if (coords[2].equals("FROG")) {
				c = new GameFrog();
			} else {
				c = new GameFly();
			}
			// set character position and add it to list.
			c.setX(Integer.parseInt(coords[0]));
			c.setY(Integer.parseInt(coords[1]));
			ch.add(c);
		}
		//TODO implement somkind of debug flag maybe to no pullute client console.
		Utils.logger(characterPositions, false);
	}

	public boolean isConnected() {
		return socket != null && socket.isConnected();
	}
}