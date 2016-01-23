package ee.ut.cs.ds.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import ee.ut.cs.ds.Utils;

public class ClientRunnable implements Runnable {
	
	private final Set<Runnable> listeners = new HashSet<Runnable>();
	private Socket socket;
	private String host;
	private OutputStream outputStream;
    private BufferedReader inputStream;
	
	public ClientRunnable(Socket socket, String host, OutputStream outputStream, BufferedReader inputStream) {
		this.socket = socket;
		this.host = host;
		this.outputStream = outputStream;
		this.inputStream = inputStream;
	}

	public final void addListener(Runnable listener) {
		listeners.add(listener);
	}

	public final void removeListener(Runnable listener) {
		listeners.remove(listener);
	}

	private final void notifyListeners() {
		for (Runnable listener : listeners) {
			listener.run();
		}
	}

	@Override
	public void run() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e1) {

		}
		socket = null;

		try {
			socket = new Socket(host, Utils.PORT);
			inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outputStream = socket.getOutputStream();

			// TODO idk. if this here look correct place for this, but difficult
			// to improve.
			String mesage = Utils.readMessage(inputStream);
			System.out.println(mesage);
			String[] dimentions = mesage.split("\\|");
			Constants.grid_width = Integer.parseInt(dimentions[0]);
			Constants.grid_height = Integer.parseInt(dimentions[1]);
			Utils.logger("Set Game grid size to: " + Constants.grid_width + "x" + Constants.grid_height, false);
		} catch (Exception e) {
			Utils.logger("try again " + e, false);
		}
	}
}
