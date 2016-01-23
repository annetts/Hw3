package ee.ut.cs.ds.client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import ee.ut.cs.ds.Utils;
import ee.ut.cs.ds.client.characters.AGameCharacter;
import ee.ut.cs.ds.client.characters.GameFly;
import ee.ut.cs.ds.client.characters.GameFrog;
import ee.ut.cs.ds.server.Engine;
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
	private static final int M_MAX_SIZE = 8192;
	private static final String DISCOVER = "Discover";
    
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
        Thread client = new Thread(new Runnable() {
			
			@Override
			public void run() {
		        socket = null;
		        
		        try {
		            socket = new Socket(host, Utils.PORT);
		            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		            outputStream = socket.getOutputStream();

		            //TODO idk. if this here look correct place for this, but difficult to improve.
		            String mesage = Utils.readMessage(inputStream);
		            String[] dimentions = mesage.split("\\|");
		            Constants.grid_width = Integer.parseInt(dimentions[0]);
		            Constants.grid_height = Integer.parseInt(dimentions[1]);
		            Utils.logger("Set Game grid size to: " + Constants.grid_width + "x" + Constants.grid_height, false);
		        } catch (Exception e) {
		        	e.getStackTrace();
					Utils.logger("try again ", false);
				}
			}
		});
        client.start();
    }
	
	boolean serverRunning = false;
	public void startServer(final Runnable done) {
		if (serverRunning) {
			return;
		}
		
		Thread broadCastThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					DatagramSocket socket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
					socket.setBroadcast(true);
					
					while (true) {
						byte[] buffer = new byte[1024];
						DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
						socket.receive(packet);
			            String message = readAll(buffer);
			            
			            if (DISCOVER.equals(message.trim())) {
			            	buffer = DISCOVER.getBytes(Charset.forName("UTF-8"));
		                    packet.setSocketAddress(new InetSocketAddress(packet.getAddress(), packet.getPort()));
		                    packet.setData(buffer);
		                    socket.send(packet);
			            }
					}

				} catch (IOException e) {
					Utils.logger("Could not get server list: " + e.getStackTrace(), true);
				}
			}
		});
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					startListening(done);
				} catch (IOException e) {
					Utils.logger("cannot start server!" + e, true);
				}
			}
		});
		broadCastThread.start();
		thread.start();
	}
	
	private void startListening(Runnable done) throws IOException {
    	// Initialize socket
        ServerSocket ssocket = new ServerSocket(Utils.PORT);
        
        // Start game engine thread
        Thread engine = new Thread(new Engine());
        engine.start();
        boolean started = false;
        try {
            Utils.logger("Waiting for a connection on port " + Utils.PORT, false);
            while (true) {
                // Wait new connections
            	if (!started) {
            		done.run();
            		started = true;
            	}
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
			return;
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
		Utils.debug(characterPositions);
	}
	
	public List<String> getAllServers() {
		List<String> list = new ArrayList<>();
		try {
			DatagramSocket socket = new DatagramSocket();
			socket.setSoTimeout(5000);
			socket.setBroadcast(true);
			
			byte[] buffer = DISCOVER.getBytes(Charset.forName("UTF-8"));
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("255.255.255.255"), 8888);
			
			socket.send(packet);
			
			while (true) {
				// clear buffer from old data
				buffer = new byte[M_MAX_SIZE];
				packet.setData(buffer);
				// wait for OK message
				socket.receive(packet);
				String message = readAll(buffer);
				if (DISCOVER.equals(message.trim())) {
					list.add(packet.getAddress().getHostAddress());
				}
			}

		} catch (IOException e) {
			// hacky solution to wait 5 sec for discovery responses.
			return list;
		}
	}

    private String readAll(byte[] buffer) throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream(buffer), Charset.forName("UTF-8"));
        char buf[] = new char[128];
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            int n = reader.read(buf);
            if (n == -1)
                break; // end of the stream
            stringBuffer.append(buf, 0, n);
        }

        return stringBuffer.toString();
    }
	
	public boolean isConnected() {
		return socket != null && socket.isConnected();
	}
}
