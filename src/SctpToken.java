import java.util.LinkedList;
import java.util.Queue;

/*
  - Queue:
Q - queue of requesting nodes in FIFO
privilage will be passed to top of queue on cs exit

- LN
LN is an array of size N such that, LN[j] is the sequence number of the request of node j granted most recently
stores the successful cs enter  (not requests)

Note: update RN and LN when you leave CS
 */





public class SctpToken {
//	public static SctpMessage[] tokenQueue=new SctpMessage[Configfilereader.totalnodes];
	//	public static int queueTOP;
//	public static boolean isempty_tokenqueue;
	public static boolean doihavetoken;
	public static boolean Locktoken;
	public static int[] tokenVector=new int[Configfilereader.totalnodes];


	public static Queue<Integer> TokenQ=new LinkedList<Integer>();


	public SctpToken()
	{
		System.out.println("Initializing Token content to 0");
		for(int i=0;i<Configfilereader.totalnodes;i++)
		{
			//tokenQueue[i]=null;
			tokenVector[i]=0;
			
		}
		//queueTOP =-1;
		//isempty_tokenqueue=true;
		Locktoken=false;
		
		//Assign the initial token to node no as per config files
		if(SctpServer.mynodeno==Configfilereader.inittokenholder)
			doihavetoken=true;
		else
			doihavetoken= false;
			
		if(Configfilereader.isbug)
		{
			// give token to one more process so we have 2 tokens in the system now
			if(SctpServer.mynodeno==((Configfilereader.inittokenholder+2)/Configfilereader.totalnodes))
					doihavetoken=true;
		}
	}
	
	
	// before transferring token check if it is locked
	public static boolean isLocktoken() {
		return Locktoken;
	}

	//Lock token before entering CS so that no changes will be made
	public static void setLocktoken(boolean locktoken) {
		Locktoken = locktoken;
	}

	
	
	
	
	
	// returns the current queue element
	public synchronized static Queue getTokenQ() {
		return TokenQ;
	}

    //sets the queue to argument content
	public synchronized static void setTokenQ(Queue tokenQ) {
		TokenQ = tokenQ;
	}
	
	// adds new element to Queue
	public synchronized static void addTokenQ(int nodeno)
	{
		TokenQ.add(nodeno);
	}
	
	// removes top of queue 
	// call this method when you are transferring token to top of queue node
	public synchronized static void removeTokenQ()
	{
		TokenQ.remove();
	}
			
	
	
	
	
	

	public static int[] getTokenVector() {
		// if the queue is empty we return null
//		if(TokenQ.isEmpty())
//			return null; 
//		
//		else
			return tokenVector;
	}

	public synchronized static void setTokenVector(int[] tokenVector) {
		SctpToken.tokenVector = tokenVector;
	}
	
	// after CS execution always call increment to update the token vector.
	public synchronized static void incrementTokenVector()
	{
		//System.out.println("content:"+ SctpToken.tokenVector[(SctpServer.mynodeno-1)]);
		SctpToken.tokenVector[(SctpServer.mynodeno-1)]=SctpToken.tokenVector[SctpServer.mynodeno-1] + 1;
		//System.out.println("content:"+ SctpToken.tokenVector[(SctpServer.mynodeno-1)]);
	}
}



