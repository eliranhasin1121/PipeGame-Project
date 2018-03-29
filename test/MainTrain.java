//package test;
//
//import java.util.Random;
//
//public class MainTrain {
//
//	public static void main(String[] args) {
//		// design test (50 points)
//		DesignTest dt=new DesignTest();
//		TestSetter.setClasses(dt);
//		dt.testDesign();
//		
//		// execution test (50 points)
//		Random r=new Random();
//		int port=6000+r.nextInt(1000);
//		TestSetter.runServer(port);
//		try{
//			TestServer.runClient(port);
//		}finally{
//			TestSetter.stopServer();
//		}
//		
//		System.out.println("done");
//	}
//
//}
