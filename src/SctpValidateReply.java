
public class SctpValidateReply {

	SctpMessage temp_msg;
	static int sendtoken;

	
	public SctpValidateReply()
	{
		temp_msg=null;
	}


	public static void ValidateReply() {
		try {
	
				System.out.println("Validate Reply : Q content : " +SctpToken.getTokenQ());
				SctpMain.LOG.logger.info("\tValidate Reply : Q content : " +SctpToken.getTokenQ());
				
				sendtoken=(int) SctpToken.getTokenQ().poll();
			
				System.out.println("Validate Reply : Q content after polling : " +SctpToken.getTokenQ());
				SctpMain.LOG.logger.info("\tValidate Reply : Q content after polling : " +SctpToken.getTokenQ());
				System.out.println("Validate Reply : send token data updated");
				SctpMain.LOG.logger.info("\tValidate Reply : send token data updated");
				System.out.println("Validate Reply : Sending Token to node : "+sendtoken);
				SctpMain.LOG.logger.info("\tValidate Reply : Sending Token to node : "+sendtoken);
				SctpToken.doihavetoken=false;
								
				
				SctpVectorClock.setSend_reply(sendtoken);
				//SctpMain.c1.send_reply(sendtoken);
				
				// induce sleep so the send reply in client unflags the doihavetoken 
				//Thread.sleep(1000);
			
					
		
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	}
	
}
}

