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
	public static int send_reply_to;
	
	
	SctpClient(int nodeno) {
		mynodeno = nodeno;
		
			send_reply_to=0;
			
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
					
			
			

						
						do{
							// This checks the queue if any reply needs to send
							// Queue is part of Vector class 
							// Queue is updated by Validate Reply class based on conditions
							//System.out.println("\n \n Inside loop");
							send_reply_to=SctpVectorClock.getSend_reply();
														
								if(send_reply_to!=0)
								{
									send_reply(send_reply_to);
									SctpVectorClock.setSend_reply(0);
									
								}
						
							
							// Check if send request flag is set and send request based on it
							if(SctpVectorClock.isSend_request())
							{
								// Crosscheck if you have token then you should not be making req
								if(SctpToken.doihavetoken==true)
								{
								System.out.println("\n Undesirable : I have token and i am still sending request");	
									
								}
								
								System.out.println("\n \n Do i have Privalage:"+SctpToken.doihavetoken);
								
								send_request();
								
								// Unset the request flag after sending message.
								SctpVectorClock.setSend_request(false);
								
								
							}
							
							
							
						}while(true);
						
		
		}

		catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Message of type request
	
			
		public void send_reply(int reply_nodeno)
		{
						
			//Set the flag of type message
			SctpMain.sm.setIs_msg_request(false);
			SctpMain.sm.setIs_msg_reply(true);
			SctpMain.sm.setReply_nodeno(reply_nodeno);
			
			SctpMain.sm.setTokenQueue(SctpToken.getTokenQ());
			SctpMain.sm.setTokenVector(SctpToken.getTokenVector());
			
			SctpToken.doihavetoken=false;
//			SctpMain.sm.setQueueTop(SctpToken.queueTOP);
			
			
			
			
			// update the time stamp 
			//SctpMain.sm.setTimeStamp(SctpVectorClock.getTimeStamp());
			
				// Broadcast message to every node in network along with the node no so the intended
			   // Recipient only will retrieve the reply
				for (int j = 0; j < sc.size(); j++) {
										
					SendMsg(sc.get(j));
					
									}
				System.out.println("\n Reply sent to request");
				
		}	
	
	
	
	// Message of type request
	public void send_request()
	{
		System.out.println("\n Request Sent");
		
		// Increment the sequence no for new cs request
		SctpMain.sv.incrementRequest_node();
		
		// Set seq no in message object to send
		SctpMain.sm.setSeq_no(SctpVectorClock.get_my_seq_no(mynodeno));
		
		// set Reply node no as your node no
		SctpMain.sm.setReply_nodeno(mynodeno);
		
		//Set the flag of type message
		SctpMain.sm.setIs_msg_request(true);
		//SctpMain.sm.setIs_msg_contains_token(false);
		SctpMain.sm.setIs_msg_reply(false);
		
		//Increment the Time stamp 
		//SctpVectorClock.incrementTimeStamp(mynodeno);
		
		// update the time stamp 
		//SctpMain.sm.setTimeStamp(SctpVectorClock.getTimeStamp());
	
				
			// Broadcast message to every node in network
			for (int j = 0; j < sc.size(); j++) {
								
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
	

	public String byteToString(ByteBuffer byteBuffer) {
		byteBuffer.position(0);
		byteBuffer.limit(MESSAGE_SIZE);
		byte[] bufArr = new byte[byteBuffer.remaining()];
		byteBuffer.get(bufArr);
		return new String(bufArr);
	}

}