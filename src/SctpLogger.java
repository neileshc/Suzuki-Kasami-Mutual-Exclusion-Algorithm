import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class SctpLogger {
	
	 Logger logger;
	 FileHandler fh; 
	 String filename;
	  
	public SctpLogger()
	{
		    logger = Logger.getLogger("MyLog");  
		   filename = "Log_node_"+SctpServer.mynodeno+".log";
		   LogManager.getLogManager().reset();
		   
		    try {  

		        // This block configure the logger with handler and formatter  
		        fh = new FileHandler("/home/004/n/nx/nxc121930/AOS/Project2/"+ filename);  
		        logger.addHandler(fh);
		        SimpleFormatter formatter = new SimpleFormatter();  
		        fh.setFormatter(formatter);  
		    		         

		    } catch (SecurityException e) {  
		        e.printStackTrace();  
		    } catch (IOException e) {  
		        e.printStackTrace();  
		    }  
	}
		   // logger.info("Hi How r u?");  

		}



