
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
				
			System.out.println("cs_enter : Token // Privalage aquired");
			SctpMain.LOG.logger.info("\tcs_enter : Token // Privalage aquired");
			
			// Lock token for executing CS
			SctpToken.setLocktoken(true);
									
			//Return true on successful lock
			return true;
			}
			
			// Handle the logic of passing token to some other queue and then generating request
			System.out.println("cs_enter :  Error: in SCTP ME class: had queue and generated own request");
			SctpMain.LOG.logger.info("\tcs_enter :  Error: in SCTP ME class: had queue and generated own request");
			// execute CS in application
		}
		
		
		// if you dont have token then request one---------------------------------------------------
		SctpVectorClock.setSend_request(true);
		
		System.out.println("cs_enter :  before ME: "+SctpToken.doihavetoken);
		SctpMain.LOG.logger.info("\tcs_enter :  before ME: "+SctpToken.doihavetoken);
		
		while(true)
		{
			// keep waiting
			if(SctpToken.doihavetoken== true)
				break;
			
			System.out.print("");
		}
		
		System.out.println("cs_enter :  Token status in ME: "+SctpToken.doihavetoken);
		SctpMain.LOG.logger.info("\tcs_enter :  Token status in ME: "+SctpToken.doihavetoken);
		
		//Return true on successful lock
		return true;
		
		// execute CS in application
				
	}

	
	public synchronized boolean cs_leave()
	{
		
		
		if(is_reenter==false)
			
		{

		SctpToken.incrementTokenVector();
		}
		
		else if(is_reenter==true)
		{
			// reset the re_enter flag
			is_reenter=false;
		}
			
		
		// Queue update based on RN values
		for(int i=0;i<Configfilereader.totalnodes;i++)
		{
			if(SctpVectorClock.Request_Node[i]==SctpToken.tokenVector[i])
				continue;
			
			if(SctpToken.getTokenQ().contains(i+1))
				continue;
			
			SctpToken.addTokenQ(i+1);
			
			System.out.println("CS_Leave : Updated the Token Que and LN");
			SctpMain.LOG.logger.info("\tCS_Leave : Updated the Token Que and LN");
			System.out.println("CS_Leave :  Lock Token : "+SctpToken.isLocktoken());
			SctpMain.LOG.logger.info("\tCS_Leave :  Lock Token : "+SctpToken.isLocktoken());
			System.out.println("CS_Leave :  token content" +SctpToken.getTokenQ());
			SctpMain.LOG.logger.info("\tCS_Leave :  token content" +SctpToken.getTokenQ());
		}
		
		
		//unlock the token
		SctpToken.setLocktoken(false);
		System.out.println("CS_Leave : unlocking token . token locked?: "+SctpToken.isLocktoken());
		SctpMain.LOG.logger.info("\tCS_Leave : unlocking token . token locked?: "+SctpToken.isLocktoken());
		if(!(SctpToken.getTokenQ().isEmpty()))
		{
				SctpValidateReply.ValidateReply();
		}
		
		// return true on successfully leaving CS
		return true;
	}
}
