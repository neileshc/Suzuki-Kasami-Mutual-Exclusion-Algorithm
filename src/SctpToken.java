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
	public static int[] tokenQueue=new int[Configfilereader.totalnodes];
	public static int[] tokenVector=new int[Configfilereader.totalnodes];
	public static int queueTOP;
	public static boolean isempty_tokenqueue;
	
	public SctpToken()
	{
		System.out.println("Initializing Token content to 0");
		for(int i=0;i<Configfilereader.totalnodes;i++)
		{
			tokenQueue[i]=0;
			tokenVector[i]=0;
			
		}
		queueTOP =-1;
		isempty_tokenqueue=true;
	}
	
	
	
	public static int[] getTokenQueue() {
		return tokenQueue;
	}

	public static void setTokenQueue(int[] tokenQueue) {
		SctpToken.tokenQueue = tokenQueue;
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
	
}
