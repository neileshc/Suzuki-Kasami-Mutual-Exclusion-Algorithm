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
	public static SctpMessage[] tokenQueue=new SctpMessage[Configfilereader.totalnodes];
	public static int[] tokenVector=new int[Configfilereader.totalnodes];
	public static int queueTOP;
	public static boolean isempty_tokenqueue;
	public static boolean doihavetoken;
	public static boolean Locktoken;



	public SctpToken()
	{
		System.out.println("Initializing Token content to 0");
		for(int i=0;i<Configfilereader.totalnodes;i++)
		{
			tokenQueue[i]=null;
			tokenVector[i]=0;
			
		}
		queueTOP =-1;
		isempty_tokenqueue=true;
		Locktoken=false;
		
		//start token with node no 1 initially
		if(SctpServer.mynodeno==1)
			doihavetoken=true;
		else
			doihavetoken= false;
			
	}
	
	
	// before transferring token check if it is locked
	public static boolean isLocktoken() {
		return Locktoken;
	}

	//Lock token before entering CS so that no changes will be made
	public static void setLocktoken(boolean locktoken) {
		Locktoken = locktoken;
	}

	
	
	
	public static SctpMessage[] getTokenQueue() {
		return tokenQueue;
	}

	public static void setTokenQueue(SctpMessage[] tokenQueue,int receivedQuetop) {
				
		SctpToken.tokenQueue = tokenQueue;
		queueTOP=receivedQuetop;
	}

	public static int[] getTokenVector() {
		
		if(isempty_tokenqueue==false)
		return tokenVector;
		
		// if the queue is empty we return null
		else
			return null;  
	}

	public static void setTokenVector(int[] tokenVector) {
		SctpToken.tokenVector = tokenVector;
	}
	
	// after CS execution always call increment to update the token vector.
	public static void incrementTokenVector()
	{
		tokenVector[(SctpServer.mynodeno-1)]=tokenVector[SctpServer.mynodeno-1] + 1;
	}
}
