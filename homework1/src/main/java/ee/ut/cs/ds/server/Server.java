package ee.ut.cs.ds.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    public static final int PORT = 7777;

    public static void main(String[] args) throws IOException, InterruptedException {



        ServerSocket ssocket = new ServerSocket(PORT);
        try {
            while (true) {
                System.err.println("Waiting for a connection on port " + PORT);
                Socket socket = ssocket.accept();
                // create a thread and let it read from socket
                Thread thread = new Thread(new Worker(socket));
                thread.start();
            }
        } finally {
            ssocket.close();
        }
    }

}