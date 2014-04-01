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
			if(SctpMain.Oktoterminate)  
			{
				break;
				
			}
		
			
			// its blocking call
			// you need to continously keep checking for con
			SctpMessage newmsg = null;
			newmsg = receiveMsg(sc);
			
			if(newmsg!=null)
			{
				// Block if the message reply is not intended for you then just ignore message
				if(newmsg.isIs_msg_reply())
				{
									
					//determine if the reply is for you ,if not then break and look for the next message
					if(newmsg.getReply_nodeno()!=SctpServer.mynodeno)
						continue;
							
					
				}
				if(newmsg.isterminationmsg==true)
				{
					SctpMain.Processdata(newmsg);
					break;
				}
				
							
				// after receiving message send it over to main for sync purpose
				//dont do processing except reading in the thread level.
				SctpMain.Processdata(newmsg);
			}
			
		}
		
		System.out.println("Channel Processing : Exiting ");
	}

	SctpMessage receiveMsg(SctpChannel sc) {
						
		SctpMessage newmsg = null;
		ByteBuffer buf = ByteBuffer.allocate(SctpClient.MESSAGE_SIZE);
	
		
		try {
			MessageInfo messageInfo = null;
			messageInfo = sc.receive(buf, System.out, null);
			
			// To resolve corrupted stream error
			// you keep checking if this message is after termination and if yes you dont deserialize it
			if(SctpMain.Oktoterminate)  
			{
				return null;
			}
			

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
