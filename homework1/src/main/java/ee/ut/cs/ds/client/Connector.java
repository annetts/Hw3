package ee.ut.cs.ds.client;

import static java.lang.String.format;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import ee.ut.cs.ds.Utils;
import ee.ut.cs.ds.server.Server;

public class Connector {

	
    
    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
        if (args.length != 1) {
            System.err.println(format("Usage: java %s <host>", Connector.class.getCanonicalName()));
            System.exit(1);
        }

        String host = args[0];

        Socket socket = null;
        OutputStream outputStream = null;
        BufferedReader inputStream = null;

        try {
            socket = new Socket(host, Server.PORT);
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = socket.getOutputStream();

            String[] fromC = Utils.readUserInput().split(" ");


        } finally {
            // always close the streams and sockets
            if (socket != null)
                socket.close();
            if (inputStream != null)
                inputStream.close();
            if (outputStream != null)
                outputStream.close();
        }
    }

	
}
