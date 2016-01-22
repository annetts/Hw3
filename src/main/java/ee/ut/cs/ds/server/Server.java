package ee.ut.cs.ds.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ee.ut.cs.ds.Utils;

public class Server {
	
    public static final int PORT = 7777;

    public static void main(String[] args) throws IOException, InterruptedException {

    	// Initialize socket
        ServerSocket ssocket = new ServerSocket(PORT);
        
        // Start game engine thread
        Thread engine = new Thread(new Engine());
        engine.start();
        
        try {
            Utils.logger("Waiting for a connection on port " + PORT, false);
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

}