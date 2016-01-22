package ee.ut.cs.ds.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

import ee.ut.cs.ds.Utils;


/** Dumps data from socket to file. */
public class Worker implements Runnable {
    public static final int BUFFER_SIZE = 8192;
    private Socket socket;
    private String name;
    private int gridY = 30;
    private int gridX = 30;
    
    private Character character;

    public Worker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        OutputStream outputStream = null;
        BufferedReader inputStream = null;

        try {
        	// initialize streams
			outputStream = socket.getOutputStream();
	        inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        
	        // send client the game area size
	        Utils.writeMessage(outputStream, gridX + "|" + gridY);
	        
	        // register client socket - client is registered as spectactor
            ConnectedClients.addClient(socket);
	        
	        // wait client to send character type and join game.
	        String charType = Utils.readMessage(inputStream);	        
	        createCharacterAndJoinGame(charType);
            
	        // loop to wait client messages about movements.
	        while (true) {
	        	// wait message from client
		        String read = Utils.readMessage(inputStream);
		        
		        // exception should be thrown.
		        String[] coords = getCoordinates(read);
		        
		        // coodinates got from client
		        int x = character.getX() + Integer.parseInt(coords[0]);
		        int y = character.getY() + Integer.parseInt(coords[1]);
		        
		        //TODO chack if coorinates should be valid -- improve.. 
		        if (!character.isMoved() && x >= 0 && x <= gridX && y >= 0 && y <= gridY) {
		        	character.setX(x);
		        	character.setY(y);
		        	character.setMoved(true);
		        }
	        }

		} catch (IOException e) {
			Utils.logger(e.getMessage(), false);
		}

    }

	// create character with random position
	private void createCharacterAndJoinGame(String charType) {
		Character c = new Character(charType);
		c.setX(new Random().nextInt(10));
		c.setY(new Random().nextInt(10));
		name =  "Player" + ConnectedClients.addCharacter(socket, c);
		this.character = c;
		c.setName(name);
		Utils.logger(name + " joined as " + charType, false);
	}
    
	// parse coordinates from strings
    private String[] getCoordinates(String message) {
    	String[] data = message.split("\\|");
    	return data;
    }
}
