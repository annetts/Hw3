package ee.ut.cs.ds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Calendar;

public class Utils {

	public static void writeMessage(OutputStream out, String msg) throws IOException {
		out.write((msg + "\n").getBytes());
		out.flush();
	}

	public static String readMessage(BufferedReader inputStream) throws IOException {
		String message = inputStream.readLine();
		return message;
	}

	public static void logger(String message, boolean error) {
		if (error) {
			System.err.println("[" + Calendar.getInstance().getTime() + "] " + message.trim());
		} else {
			System.out.println("[" + Calendar.getInstance().getTime() + "] " + message.trim());
		}
	}
	
    public static String readUserInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter command:");
        return reader.readLine();
    }
}
