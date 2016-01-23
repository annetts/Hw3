package ee.ut.cs.ds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Calendar;

/**
 * List of static methods that make life easier.
 * @author rauno
 *
 */
public class Utils {

	public static final int PORT = 7777;
	private static final boolean DEBUG = false;
	
	/**
	 * Write message to output stream
	 * @param out - output stream of socket.
	 * @param msg - message to write to stream
	 * @throws IOException
	 */
	public static void writeMessage(OutputStream out, String msg) throws IOException {
		out.write((msg + "\n").getBytes());
		out.flush();
	}

	/**
	 * Read message from inputstream
	 * @param inputStream - inputstream of socket
	 * @return
	 * @throws IOException
	 */
	public static String readMessage(BufferedReader inputStream) throws IOException {
		String message = inputStream.readLine();
		return message;
	}

	/**
	 * Logger to write log. Adds proper format to message ([time_of_message] message).
	 * @param message
	 * @param error
	 */
	public static void logger(String message, boolean error) {
		if (error) {
			System.err.println("[" + Calendar.getInstance().getTime() + "] " + message.trim());
		} else {
			System.out.println("[" + Calendar.getInstance().getTime() + "] " + message.trim());
		}
	}
	
	/**
	 * Read text from commandline
	 * @return
	 * @throws IOException
	 */
    public static String readUserInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter command:");
        return reader.readLine();
    }

	public static void debug(String characterPositions) {
		if (DEBUG) {
			System.out.println("[" + Calendar.getInstance().getTime() + "] " + characterPositions.trim());			
		}
	}
}
