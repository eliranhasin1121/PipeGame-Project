package test;

import CliectHandlerPACK.CacheManager;
import CliectHandlerPACK.ClientHandler;
import CliectHandlerPACK.MyCacheManager;
import CliectHandlerPACK.MyClientHandler;
import CliectHandlerPACK.MySolver;
import CliectHandlerPACK.Solver;
import ServerPACK.MyServer;
import ServerPACK.Server;

public class TestSetter {
	
	public static void setClasses(DesignTest dt){
		
		// set the server's Interface, e.g., "Server.class"
		// don't forget to import the correct package e.g., "import server.Server"
		dt.setServerInteface(Server.class);
		// now fill in the other types according to their names
		dt.setServerClass(MyServer.class);
		dt.setClientHandlerInterface(ClientHandler.class);
		dt.setClientHandlerClass(MyClientHandler.class);
		dt.setCacheManagerInterface(CacheManager.class);
		dt.setCacheManagerClass(MyCacheManager.class);
		dt.setSolverInterface(Solver.class);
		dt.setSolverClass(MySolver.class);
	}
	
	// run your server here
	static Server s;
	public static void runServer(int port){
		s=new MyServer(port);
		s.start(new MyClientHandler());
	}
	// stop your server here
	public static void stopServer(){
		s.stop();
	}

}
