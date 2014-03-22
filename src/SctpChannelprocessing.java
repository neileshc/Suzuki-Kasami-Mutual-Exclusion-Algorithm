import java.nio.ByteBuffer;

import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.SctpChannel;


public class SctpChannelprocessing implements Runnable{

	SctpChannel sc;
	
public SctpChannelprocessing(SctpChannel sc) {
	this.sc=sc;
}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(true)
		{
			
			SctpMessage newmsg = null;
			newmsg = receiveMsg(sc);
			
			
			// Block if the message reply is not intended for you then just ignore message
			if(newmsg.isIs_msg_reply())
			{
								
				//determine if the reply is for you ,if not then break and look for the next message
				if(newmsg.getReply_nodeno()!=SctpServer.mynodeno)
					continue;
						
				
			}
			
						
			// after receiving message send it over to main for sync purpose
			//dont do processing except reading in the thread level.
			SctpMain.Processdata(newmsg);
			
		}
		
		
	}

	SctpMessage receiveMsg(SctpChannel sc) {
		SctpMessage newmsg = null;
		ByteBuffer buf = ByteBuffer.allocate(SctpClient.MESSAGE_SIZE);
		//int msgid = 0;
		//int[] receivedvectorClock = new int[Configfilereader.totalnodes];
		
		try {
			MessageInfo messageInfo = null;
			messageInfo = sc.receive(buf, System.out, null);

			// Converting bytes to string.
			if (messageInfo != null) {
				newmsg = (SctpMessage) SctpMessage.deserialize(buf.array());
					
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return newmsg;
	}	
	
		
}
