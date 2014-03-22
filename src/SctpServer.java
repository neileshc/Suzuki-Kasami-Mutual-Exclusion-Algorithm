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
			System.out.println("Setting up the distributed network .....");
			System.out.println("Server UP for node : " + mynodeno
					+ " at Port number: " + SERVER_PORT);

			// Server runs in loop for accepting connections from clients
			do {
				// Returns a new SCTPChannel between the server and client
				sc.add(ssc.accept());
				
				System.out.println("connection accepted from another node");

				// Break the loop once you have all the clients connected
				if (sc.size() == (Configfilereader.totalnodes - 1))
					break;

			} while (true);

			// Allow some time to server to accept connections
			System.out
			.println("Node Setup Completed , Preparing network to send messages.....");
			this.sleep(3000);
			
			
			
//-----------------------------------------------------------------------------------------------
			
		for(int i=0;i<5;i++)
		{
			send_request();
			//System.out.println("Sent :"+ i);
		}
		//this.sleep(3000);
		
		
		

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//broadcasts request for critical section
	public void send_request()
	{
		// Increment the sequence no for new cs request
		SctpMain.sv.incrementVectorClock(mynodeno);
		SctpMain.sm.setSeq_no(SctpVectorClock.get_my_seq_no(mynodeno));
		SctpMain.sm.setIs_msg_contains_token(false);
		SctpMain.sm.setIs_msg_request(true);
		
			for (int j = 0; j < sc.size(); j++) {
				// call for send message method
				
				SendMsg(sc.get(j));
				}
	}
	
	
	public void SendMsg(SctpChannel sc) {
		// Buffer to hold messages in byte format
		ByteBuffer buf = ByteBuffer.allocate(MESSAGE_SIZE);
		try {
			
				SctpMain.sm.setContent("\n Hello from Machine : "+Configfilereader.Machinename[(mynodeno - 1)] +"(Port : "
						+ SERVER_PORT );

			
			// Before sending messages add additional information about the
			// message
			MessageInfo messageInfo = MessageInfo.createOutgoing(null, 0);

			buf.clear();

			// convert the string message into bytes and put it in the byte
			// buffer
			buf.put(SctpMessage.serialize(SctpMain.sm));

			// Reset a pointer to point to the start of buffer
			buf.flip();

			// Send a message in the channel (byte format)
			sc.send(buf, messageInfo);
			
			// System.out.println("Server" + newmsg.getContent());

			buf.clear();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}