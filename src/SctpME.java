
public class SctpME {
	
	public SctpME()
	{
	
	}
	
	
	// Application will make call here to request for critical section
	// On success returns true 
	// till then reply differed
	public boolean cs_enter()
	{
		
		//Check if you have token before sending request
		if(SctpToken.doihavetoken== true)
		{
			System.out.println("\n Token // Privalage aquired");
			
			// Lock token for executing CS
			SctpToken.setLocktoken(true);
			
			// update RN
			SctpMain.sv.incrementRequest_node();
			
			//update Timestamp
			SctpVectorClock.incrementTimeStamp(SctpServer.mynodeno);
						
			//Return true on successful lock
			return true;
			
			// execute CS in application
		}
		
		
		// if you dont have token then request one---------------------------------------------------
		SctpVectorClock.setSend_request(true);
		
		// the flags and node request updates are taken care in send request method
		
		do
		{
			// wait till you get token
		//	System.out.println("Token status: "+SctpToken.doihavetoken);
			
		}while(SctpToken.doihavetoken== false);
		
		System.out.println("Token status: "+SctpToken.doihavetoken);
		// Lock token for executing CS
		SctpToken.setLocktoken(true);
		
		//Return true on successful lock
		return true;
		
		// execute CS in application
		
		
	}

	
	public boolean cs_leave()
	{
		// update the LN
		SctpToken.incrementTokenVector();
		
		//unlock the token
		SctpToken.setLocktoken(false);
			
		// return true on successfully leaving CS
		return true;
	}
}
