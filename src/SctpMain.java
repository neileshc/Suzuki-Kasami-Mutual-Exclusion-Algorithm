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
	
	public static int nodeno;
	public static BufferedReader br = new BufferedReader(new InputStreamReader(
			System.in));
	
	
	// Synchronized block for processing incoming request 
	// Server threads will send the data here as they receive
	public synchronized static void Processdata(SctpMessage newmsg)
	{
		// do message processing here
		
		if (newmsg != null) {
			
			// determine if the message type is of request and take action accordingly
			if(newmsg.isIs_msg_request())
			{
				System.out.println("\n New request for critical section received from node: "+ newmsg.getNode_no());
				System.out.println("\n Sequence no: "+newmsg.getSeq_no());
				

				//Update RN with the received request
				SctpMain.sv.setRequest_Node(newmsg.getNode_no(), newmsg.getSeq_no());
							
							
				// if token is idle and queue is empty then add it to queue
				if((SctpToken.doihavetoken)&&(!SctpToken.isLocktoken())&&(SctpToken.getTokenQ().isEmpty()))
				{
					SctpToken.addTokenQ(newmsg.getNode_no());
				}
		
				// else we dont have action pending
				// if you have token then its in CS or if not you have queue waiting
				//to which you will transfer control
				
				
			}
			else if(newmsg.isIs_msg_reply())
			{
				
				System.out.println("\n Process Data :Reply received");
							
				// Lock token for executing CS
				// Validate reply: sending token will be prevented until lock is removed
				SctpToken.setLocktoken(true);
							
				System.out.println("\n Process Data : q content before update" +SctpToken.getTokenQ());
				SctpToken.setTokenVector(newmsg.getTokenVector());
				SctpToken.setTokenQ(newmsg.getTokenQueue());
				System.out.println("\n Process Data : q content after update" +SctpToken.getTokenQ());
			
				
				// you have the token so update the flag
				// ME: the condition will be true and thread will be unblocked
				SctpToken.doihavetoken=true;
							
				System.out.println("\n Process Data :Token Lock status: "+SctpToken.isLocktoken());
				System.out.println("\n Process Data :Token status: "+SctpToken.doihavetoken);
				
			}
			
						
		}
	}
	
	
	
	public static void main(String args[]) throws InterruptedException,
			IOException {

		nodeno = Integer.parseInt(args[0]);
		Configfilereader r = new Configfilereader();
		r.readfile();


		
		sm = new SctpMessage(nodeno);
		sv = new SctpVectorClock();
		sme=new SctpME();
		svr=new SctpValidateReply();
			
		
		
		SctpServer s1 = new SctpServer(nodeno);
		Thread t1 = new Thread(s1);
		t1.start();
		
		Thread.sleep(1000);
		
		//Token needs to access Sctp server var
		SctpToken st=new SctpToken();
		

		System.out
		.println("Press enter to start Stock Application...Make sure your all node servers are UP");
		br.readLine();
		
		SctpClient c1 = new SctpClient(nodeno);
		Thread t2 = new Thread(c1);
		t2.start();

		
	// start thread for continously validating request type messgaes
		
		SctpValidateReply thread=new SctpValidateReply();
		Thread t3 = new Thread(thread);		
		t3.start();
		
		// start the application thread
		SctpApp sap = new SctpApp();
		Thread t4 = new Thread(sap);
		t4.start();
		
		
		
		t1.join();
		t2.join();
		
		

		
		
		for (int i = 0; i < Configfilereader.totalnodes; i++) {
			System.out.print(sv.Request_Node[i] + "\t");
			
		}
		

	}
}
