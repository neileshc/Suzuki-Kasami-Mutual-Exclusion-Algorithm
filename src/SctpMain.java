import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sun.nio.sctp.MessageInfo;

public class SctpMain {
	public static int msgexch = 0;
	public static SctpVectorClock sv;
	public static SctpMessage sm;
	public static SctpME sme;
	public static SctpServer s1;
	public static SctpClient c1;
	public static SctpToken st;
	public static SctpValidateReply svr;
	public static SctpApp sap;
	public static SctpLogger LOG;
	
	public static int nodeno;
	public static BufferedReader br = new BufferedReader(new InputStreamReader(
			System.in));
	
	
	// Synchronized block for processing incoming request 
	// Server threads will send the data here as they receive
	public static void Processdata(SctpMessage newmsg)
	{
		// do message processing here
		
		if (newmsg != null) {
			
			// determine if the message type is of request and take action accordingly
			if(newmsg.isIs_msg_request())
			{
				System.out.println("Process Data : New request for critical section received from node: "+ newmsg.getNode_no());
				SctpMain.LOG.logger.info("\tProcess Data :  New request for critical section received from node: "+ newmsg.getNode_no());
				System.out.println("Process Data : Sequence no: "+newmsg.getSeq_no());
				SctpMain.LOG.logger.info("\tProcess Data :  Sequence no: "+newmsg.getSeq_no());

				//Update RN with the received request
				SctpMain.sv.setRequest_Node(newmsg.getNode_no(), newmsg.getSeq_no());
							
							
				// if token is idle and queue is empty then add it to queue
				if((SctpToken.doihavetoken)&&(!SctpToken.isLocktoken()))//&&(SctpToken.getTokenQ().isEmpty()))
				{
					SctpToken.addTokenQ(newmsg.getNode_no());
					
					SctpValidateReply.ValidateReply();
				}
		
				// else we dont have action pending
				// if you have token then its in CS or if not you have queue waiting
				//to which you will transfer control
				
				
			}
			else if(newmsg.isIs_msg_reply())
			{
				
				System.out.println("Process Data : Reply received");
				SctpMain.LOG.logger.info("\tProcess Data :Reply received");	
				// Lock token for executing CS
				// Validate reply: sending token will be prevented until lock is removed
				SctpToken.setLocktoken(true);
							
				System.out.println("Process Data : q content before update" +SctpToken.getTokenQ());
				SctpMain.LOG.logger.info("\tProcess Data : q content before update" +SctpToken.getTokenQ());
				SctpToken.setTokenVector(newmsg.getTokenVector());
				SctpToken.setTokenQ(newmsg.getTokenQueue());
				System.out.println("Process Data : q content after update" +SctpToken.getTokenQ());
				SctpMain.LOG.logger.info("\tProcess Data : q content after update" +SctpToken.getTokenQ());
				
				// you have the token so update the flag
				// ME: the condition will be true and thread will be unblocked
				SctpToken.doihavetoken=true;
							
				System.out.println("Process Data :Token Lock status: "+SctpToken.isLocktoken());
				SctpMain.LOG.logger.info("\tProcess Data :Token Lock status: "+SctpToken.isLocktoken());
				System.out.println("Process Data :Token status: "+SctpToken.doihavetoken);
				SctpMain.LOG.logger.info("\tProcess Data :Token status: "+SctpToken.doihavetoken);
			}
			
						
		}
	}
	
	
	
	public static void main(String args[]) throws InterruptedException,
			IOException {

		nodeno = Integer.parseInt(args[0]);
		Configfilereader r = new Configfilereader();
		r.readfile();
	
		
		SctpServer s1 = new SctpServer(nodeno);
		SctpClient c1 = new SctpClient(nodeno);
		sm = new SctpMessage(nodeno);
		sv = new SctpVectorClock();
		sme=new SctpME();
		svr=new SctpValidateReply();
		SctpToken st=new SctpToken();
		LOG=new SctpLogger();	
		
		Thread t1 = new Thread(s1);
		t1.start();
				
		Thread.sleep(1000);
		
		System.out
		.println("Main : Press enter to start Application...Make sure your all node servers are UP");
		br.readLine();
				
		Thread t2 = new Thread(c1);
		t2.start();

			// start the application thread
		SctpApp sap = new SctpApp();
		Thread t4 = new Thread(sap);
		t4.start();
		
		
		
		t1.join();
		t2.join();
		
		
//		for (int i = 0; i < Configfilereader.totalnodes; i++) {
//			System.out.print(sv.Request_Node[i] + "\t");
//			
//		}
		

	}
}
