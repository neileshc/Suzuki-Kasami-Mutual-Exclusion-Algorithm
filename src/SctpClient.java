import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;

import com.sun.nio.sctp.AbstractNotificationHandler;
import com.sun.nio.sctp.AssociationChangeNotification;
import com.sun.nio.sctp.HandlerResult;
import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.SctpChannel;
import com.sun.nio.sctp.ShutdownNotification;

public class SctpClient extends Thread {
	static Integer SERVER_PORT;
	String machine = "";
	public static int mynodeno = 0;
	public static final int MESSAGE_SIZE = 10000;
	public int msgcntr = 0;
	ArrayList<SctpChannel> sc = new ArrayList<>();  // holding all connections with other servers
	
	SctpClient(int nodeno) {
		mynodeno = nodeno;
	}

	public void run() {
		
		SctpChannel temp_sc = null;
		try {
			// Loop for making desired connections
			for (int j = 0; j < Configfilereader.totalnodes; j++) {
				if (j == (mynodeno - 1))
					continue;

				// Allocating the Server port for connection
				SERVER_PORT = Configfilereader.Machineport[j];
				machine = Configfilereader.Machinename[j];

				// Create a socket address for server
				InetSocketAddress serverAddr = new InetSocketAddress(machine,
						SERVER_PORT);

				// Open a channel.
				temp_sc = SctpChannel.open(serverAddr, 0, 0);
				if(temp_sc==null)
				{
					j=j-1;
					continue;
				}
				// Save this servers connection in list
				sc.add(temp_sc);
						
				System.out.println("Client connected to Machine: " + machine
						+ " at Port number: " + SERVER_PORT);
			}

			SctpMessage newmsg = null;
			
			// Inducing sleep as when connection established we may see corrupt
						// stream error
						this.sleep(3000);

								
						
						
//------------------------------------------------------------------------------------------------------			
					
			
			
// code for revceiving message
			do {
				for (int i = 0; i < sc.size(); i++) {
					sc.get(i).configureBlocking(false); 
													
					newmsg = receiveMsg(sc.get(i));

					if (newmsg != null) {
						System.out.println("\n---------------------------------");
						System.out.println(newmsg.getContent());
						msgcntr++;
					}
				}
			} while (msgcntr < 5*(Configfilereader.totalnodes - 1));
			
			
		
			
		

		}

		catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	SctpMessage receiveMsg(SctpChannel sc) {
		SctpMessage newmsg = null;
		ByteBuffer buf = ByteBuffer.allocate(MESSAGE_SIZE);
		int msgid = 0;
		int[] receivedvectorClock = new int[Configfilereader.totalnodes];
		
		try {
			MessageInfo messageInfo = null;
			messageInfo = sc.receive(buf, System.out, null);

			// Converting bytes to string.
			if (messageInfo != null) {
				newmsg = (SctpMessage) SctpMessage.deserialize(buf.array());

				if (newmsg != null) {
					
					// determine if the message type is of request and take action accordingly
					if(newmsg.isIs_msg_request())
					{
						System.out.println("\n New request for critical section received from node: "+ newmsg.getNode_no());
						System.out.println("\n Sequence no: "+newmsg.getSeq_no());
						SctpMain.sv.setRequest_Node(newmsg.getNode_no(), newmsg.getSeq_no());
						
					}
	
					
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return newmsg;
	}

	public String byteToString(ByteBuffer byteBuffer) {
		byteBuffer.position(0);
		byteBuffer.limit(MESSAGE_SIZE);
		byte[] bufArr = new byte[byteBuffer.remaining()];
		byteBuffer.get(bufArr);
		return new String(bufArr);
	}

}