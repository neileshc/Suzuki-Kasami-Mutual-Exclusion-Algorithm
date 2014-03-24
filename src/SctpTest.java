import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.StringTokenizer;


public class SctpTest {

	static String[][] tokens=new String[100][3];
	
	public static void main(String[] args) throws IOException {
				
	safetyPropertyTest();	

		

	}
	
	public static void safetyPropertyTest() throws IOException
	{
		System.out.println("Test : Safety Property test: No 2 process in Critical section at the same time");
		System.out.println("Test : Initiating Safety Property test for the Mutual Exclusion algorithm");
		
		
		String filename = "/home/004/n/nx/nxc121930/AOS/Project2/Log.log";
		InputStream inputS = new FileInputStream(filename);
		int count=0;
		count=tokenize(inputS);
		
		SctpTest.Analyze(count);
	}
	
	public static void Analyze(int count)
	{
		int i=1;
		boolean flagit=true;
		
		do{
			flagit=true;
			
			if(tokens[i][0].contentEquals("Open"))
			{
				//System.out.println(i);
				//System.out.println(tokens[i][0]);
			
				if(tokens[i+1][0].contentEquals("Close"))
				{
					// node no should be same
					if(tokens[i][1].contentEquals(tokens[i+1][1]))
					{
						// seq no should be same
						if(tokens[i][2].contentEquals(tokens[i+1][2]))
						{
							flagit=false;
						}
					}
				}
				
				if(flagit==true)
				{
					System.out.println("Test : Test FAILED");
					System.out.println("Test : Process failure Details : ");
					System.out.println("Test : Process : "+tokens[i][1]+"\t Sequence no : "+tokens[i][2]);
					System.out.println("Test : Process : "+tokens[i+1][1]+"\t Sequence no : "+tokens[i+1][2]);
					break;
				}
				i=i+2;
				//System.out.println(i);
			}
			else
			{
				i++;
			}
			
		}while(i<count);
		
		if(flagit==false)
		{
			System.out.println("\nTest PASSED");
			
		}
		
	}
	 
		public static int tokenize(InputStream inputS) throws IOException
		{
			int count=0;
			String temp;
		    BufferedReader reader = new BufferedReader(new InputStreamReader(inputS, "UTF-8"));
		    while(reader.ready())
		    {
		    	int i=0;
		           String lineRead = reader.readLine();
		           StringTokenizer st = new StringTokenizer(lineRead);
		           while (st.hasMoreTokens())
		           {
		        	   temp=st.nextToken();
		        	  // System.out.println(temp);
		           if(i==0)
		             {
		            	
		        	   tokens[count][0]=temp;
		        	   //System.out.println(temp);
		        	   i++;
		             }
		        	 else if(i==2)
		        	 {
		        		 
		        		 tokens[count][1]=temp;
		        		 //System.out.println(temp);
		        		 i++;
		        	 }
		        	  else if(i==3)
		        	 {
		        		 
		        		 tokens[count][2]=temp;
		        		 //System.out.println(temp);
		        		 i++;
		        		 
		        	 }   
		        	 else
		        	   i++;
		        	 //  System.out.println(st.nextToken());
		           }
		           
		          count++;
		    }
		    
		/*    int i=1;
		    while(i<count)
		    {
		    	System.out.println(tokens[i][0]+"\t"+tokens[i][1]+"\t"+tokens[i][2]);
		    	i++;
		    }*/
		    
		    return count;

		}
	 

}
