import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.SctpChannel;
import com.sun.nio.sctp.SctpServerChannel;

public class SctpServer extends Thread {
	static Integer SERVER_PORT;
	public static int mynodeno;
	public static final int MESSAGE_SIZE = 10000;
	public int msgsent = 0;
	public int[] servervectorclock = new int[Configfilereader.totalnodes];
//	public static SctpChannel[] sc = new SctpChannel[Configfilereader.totalnodes] ;
	public static ArrayList<SctpChannel> sc = new ArrayList<>();
	
	// Initialize server details
	public SctpServer(int nodeno) {
		// Initializing Server Port details
		SERVER_PORT = Configfilereader.Machineport[(nodeno - 1)];
		mynodeno = nodeno;

		for (int i = 0; i < Configfilereader.totalnodes; i++) {
			servervectorclock[i] = 0;
		}
	}

	public void run() {

		try {
			// Opening server Channel
			SctpServerChannel ssc = SctpServerChannel.open();

			// Create socket address
			InetSocketAddress serverAddr = new InetSocketAddress(SERVER_PORT);

			// Bind the channel's socket to the server
			ssc.bind(serverAddr);
			System.out.println("Server : Setting up the distributed network .....");
			System.out.println("Server : Server UP for node : " + mynodeno
					+ " at Port number: " + SERVER_PORT);

			// Server runs in loop for accepting connections from clients
			// as soon as it gets connection it creates seperate thread for that one
			
			SctpChannel sc_temp;
			do {
				// Returns a new SCTPChannel between the server and client
				
					sc_temp=(ssc.accept());
				//sc_temp.getRemoteAddresses();
					sc.add(sc_temp);
				//sc.add(ssc.accept());
				SctpChannelprocessing thread=new SctpChannelprocessing(sc_temp);
				Thread obj = new Thread(thread);		
				obj.start();
				
				System.out.println("Server : connection accepted from another node");

				// Break the loop once you have all the clients connected
				if (sc.size() == (Configfilereader.totalnodes - 1))
					break;

			} while (true);

			// Allow some time to server to accept connections
			System.out
			.println("Server : Node Setup Completed , Preparing network to send messages.....");
		
			
			do
			{
				//do thing
				
			}while(!Thread.currentThread().isInterrupted());
			
			
			System.out.println("Server : Exiting ");
			// handle it better way to avoid server shutting down if you can
			
			
			


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
	}

	
}