package ee.ut.cs.ds.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ee.ut.cs.ds.Utils;
import ee.ut.cs.ds.server.Character.Characters;

public class Engine implements Runnable {
	
	@Override
	public void run() {
		
        OutputStream outputStream = null;
		
      //TODO can be made a lot shorter... also. Kill character is not implemented
		while (true) {
			List<Socket> list = new LinkedList<Socket>();
			try {
				Map<Socket, Character> clients = ConnectedClients.getClients();
				// loop through all characters
				Iterator<Entry<Socket, Character>> iterator = clients.entrySet().iterator();
				long currentTime = System.currentTimeMillis();
				while(iterator.hasNext()) {
					Entry<Socket, Character> entry = iterator.next();
				 	if (entry.getValue() != null) {
				 		if (entry.getValue().getEatTime() + 120000 < currentTime) {
				 			entry.getValue().setEatTime(System.currentTimeMillis());
				 			try {
				 				if (entry.getValue().getType().equals(Characters.FROG)) {
				 					entry.getValue().setPoints(entry.getValue().getPoints() - 1);
				 					if (entry.getValue().getPoints() <= 0) {
										outputStream = entry.getKey().getOutputStream();
										Utils.writeMessage(outputStream, "");
							 			iterator.remove();
				 					}
				 				} else {
				 					entry.getValue().setPoints(entry.getValue().getPoints() + 1);
				 				}
							} catch (IOException e) {
							}
				 		}
				 	} 
				 	if (entry.getValue() != null && entry.getValue().getType().equals(Characters.FLY) ) {
			 			for (Entry<Socket, Character> other : clients.entrySet()) {
			 				if (other.getValue() != null && other.getValue().getType().equals(Characters.FROG)) {
			 					if (other.getValue().getX() == entry.getValue().getX() &&
			 							other.getValue().getY() == entry.getValue().getY()) {
						 			try {
						 				other.getValue().setPoints(other.getValue().getPoints() + 1);
										outputStream = entry.getKey().getOutputStream();
										Utils.writeMessage(outputStream, "");
							 			iterator.remove();
							 			break;
									} catch (IOException e) {
									}
			 					}
			 				}
			 			}
			 		}
				}
				
				for (Entry<Socket, Character> entry : clients.entrySet()) {
					
			        try {
			        	// get player connection specific socket streams
						outputStream = entry.getKey().getOutputStream();
						
				        // message to send
			        	String message = "";
				        
				        // if no charcter object, player is spectator else real player
				        if (entry.getValue() == null) {
				        	// loop through all other chars, including player itself.
				        	for (Character otherChar : clients.values()) {
				        		if (otherChar != null) {
				        			// add all chars
				        			message += "#" + otherChar.getX() + "|" + otherChar.getY() + "|" + otherChar.getType();
				        		}
			        	}
				        } else {
				        	Character character = entry.getValue();
				        	// check player character type and calculate its message accoding to visibility
				        	if (character.getType().equals(Characters.FROG)) {
				        		// add own character as first on list
			        			message += validateData(
			        					character.getX(), character.getY(),
			        					character.getX(), character.getY(),
			        					1,
			        					character.getType());
					        	for (Character otherChar : clients.values()) {
					        		if (otherChar != null && otherChar != character) {
					        			message += validateData(
					        					character.getX(), character.getY(),
					        					otherChar.getX(), otherChar.getY(),
					        					1,
					        					otherChar.getType());
					        		}
					        	}
				        	} else if (character.getType().equals(Characters.FLY)) {
					        	for (Character otherChar : clients.values()) {
					        		// add own character as first on list
				        			message += validateData(
				        					character.getX(), character.getY(),
				        					character.getX(), character.getY(),
				        					1,
				        					character.getType());
					        		if (otherChar != null && otherChar != character) {
					        			message += validateData(
					        					character.getX(), character.getY(),
					        					otherChar.getX(), otherChar.getY(),
					        					2,
					        					otherChar.getType());
					        		}
					        	}
					        	
				        	} else {
				        		// invalid character
				        		continue;
				        	}
				        	character.setMoved(false);
				        }
				        // send message to client
				        Utils.writeMessage(outputStream, message);
					} catch (IOException e) {
						Utils.logger("Remove client " + entry.getKey().getRemoteSocketAddress(), false);
						list.add(entry.getKey());
					}
				}
				for (Socket socket : list) {
					clients.remove(socket);
				}
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Utils.logger("Game engine unexpected error!" + e, true);
			}
		}
	}
	
	// checks if character info should be sent to client.. list of arguments can be shorter.
	private String validateData(int origX, int origY, int x, int y, int range, Characters characters) {
		int distance = (int)Math.sqrt(Math.pow((origX - x) , 2) + Math.pow((origY - y) , 2));
		if (distance <= range) {
			return "#" + x + "|" + y + "|" + characters;
		}
		return "";
	}
}
