
public class SctpME {
	
	public boolean is_reenter;
	public SctpME()
	{
		is_reenter=false;
	}
	
	
	// Application will make call here to request for critical section
	// On success returns true 
	// till then reply differed
	public synchronized boolean cs_enter()
	{
		
		//Check if you have token before sending request
		if(SctpToken.doihavetoken== true)
		{
			if(SctpToken.TokenQ.isEmpty())
			{
			
			// set reenter flag , on flag set we dont update RN and LN
			is_reenter= true;
				
			System.out.println("\n Token // Privalage aquired");
			
			// Lock token for executing CS
			SctpToken.setLocktoken(true);
									
			//Return true on successful lock
			return true;
			}
			
			// Handle the logic of passing token to some other queue and then generating request
			System.out.println("\n Error: in SCTP ME class: had queue and generated own request");
			// execute CS in application
		}
		
		
		// if you dont have token then request one---------------------------------------------------
		SctpVectorClock.setSend_request(true);
		
		System.out.println("before ME: "+SctpToken.doihavetoken);
			
		while(true)
		{
			// keep waiting
			if(SctpToken.doihavetoken== true)
				break;
			
			System.out.print(".");
		}
		
		System.out.println("Token status in ME: "+SctpToken.doihavetoken);
		
		// Token lock is done in main as we have to lock it as soon as we receive token so to prevent 
		// validate reply from transferring token
		
		//Return true on successful lock
		return true;
		
		// execute CS in application
				
	}

	
	public synchronized boolean cs_leave()
	{
		//System.out.println("****content:"+SctpToken.tokenVector.length);//+ SctpToken.tokenVector[(SctpServer.mynodeno-1)]);
      
		
		if(is_reenter==false)
			
		{

		SctpToken.incrementTokenVector();
		}
			
		
		// Queue update based on RN values
		for(int i=0;i<Configfilereader.totalnodes;i++)
		{
			if(SctpVectorClock.Request_Node[i]==SctpToken.tokenVector[i])
				continue;
			
			if(SctpToken.getTokenQ().contains(i+1))
				continue;
			
			SctpToken.addTokenQ(i+1);
			
			System.out.println("\n Updated the Token Que and LN");
			System.out.println("\n Lock Token : "+SctpToken.isLocktoken());
			
			System.out.println("\n token content" +SctpToken.getTokenQ());
		}
		
		
		//unlock the token
		SctpToken.setLocktoken(false);
		System.out.println("\n unlocking token . token locked?: "+SctpToken.isLocktoken());
			
		// return true on successfully leaving CS
		return true;
	}
}
