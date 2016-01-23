package ee.ut.cs.ds.server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds a list of Connected clients. Each client is specified by socket and
 * owns character.
 * 
 * @author rauno
 *
 */
//TODO check need of synchronizations.. programmed somekind of randomly atm. Might be buggy.
public class ConnectedClients extends Object{

	private static ConnectedClients instance = new ConnectedClients();
	
	   public static ConnectedClients getInstance() {
		      if(instance == null) {
		         instance = new ConnectedClients();
		      }
		      return instance;
		   }
	// Holds a list of clients
	protected static Map<Socket, Character> clients = new HashMap<Socket, Character>();
	
	/**
	 * Adds client to connected list. Socket is related to null object (client
	 * is no specified its character yet).
	 * 
	 * @param socket - client socket
	 */
	public static void addClient(Socket socket) {
		synchronized (getInstance()) {
			clients.put(socket, null);
		}
	}
	
	/**
	 * Adds character to connected clients list.
	 * @param socket - client socket
	 * @param character - client character
	 * @return
	 */
	public static int addCharacter(Socket socket, Character character) {
		synchronized (getInstance()) {
			if (clients.containsKey(socket)) {
				clients.put(socket, character);
			}
			return clients.size();
		}
	}
	
	/**
	 * Returns list of clients.
	 * @return
	 */
	public static Map<Socket, Character> getClients() {
		synchronized (getInstance()) {
			return clients;
		}
	}
}
