/*
 * Created by Eliran Suisa & Eliran Hasin 17_02_18
 */
package ServerPACK;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import CliectHandlerPACK.ClientHandler;
import CliectHandlerPACK.MyClientHandler;

public class MyServer implements Server {
	private volatile boolean stop;
	private int port;
	private ClientHandler ch;
	public MyServer(int port)
	{
		this.port = port;
		stop = false;
//		this.ch = new MyClientHandler();
		this.start(ch);
	}

	@Override
	public void stop() {
		this.stop = true;
		// TODO Auto-generated method stub
		
	}
	@Override
	public void runServer() throws IOException {
		ServerSocket server = new ServerSocket(port); // init the port that the server working on.
		server.setSoTimeout(1000);
		while (!stop)
		{
			try {
				Socket aClient = server.accept(); // init socket to the client.
				try {
					this.ch = new MyClientHandler();
//					System.out.println("Client " + aClient.getRemoteSocketAddress() + "Connected.");
					ch.handleClient(aClient.getInputStream(), aClient.getOutputStream());
					this.ch = null;
					aClient.getInputStream().close();
					aClient.getOutputStream().close();
					aClient.close();
					this.ch = new MyClientHandler();
//					server.close();
				} catch (IOException e) {}
			}catch (SocketTimeoutException e) {}
			
		}
		server.close();
		
	}

	@Override
	public void start(ClientHandler clientHandler) {
			new Thread(()->{
				try {
//					System.out.println("Server started."); // open the server.
					runServer();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}).start();
			
		
	}
	

}
