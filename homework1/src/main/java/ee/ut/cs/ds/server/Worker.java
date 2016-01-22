package ee.ut.cs.ds.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;


/** Dumps data from socket to file. */
public class Worker implements Runnable {
    public static final int BUFFER_SIZE = 8192;
    private Socket socket;


    public Worker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        OutputStream outputStream = null;
        BufferedReader inputStream = null;

        try {
			outputStream = socket.getOutputStream();
	        inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			
		}

    }

}
