import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SctpMain {
	public static int msgexch = 0;
	public static SctpVectorClock sv;
	public static SctpMessage sm;
	public static SctpME sme;
	public static SctpServer s1;
	public static SctpToken st;
	
	
	public static int nodeno;
	public static BufferedReader br = new BufferedReader(new InputStreamReader(
			System.in));
	
	public static void main(String args[]) throws InterruptedException,
			IOException {

		nodeno = Integer.parseInt(args[0]);
		Configfilereader r = new Configfilereader();
		r.readfile();


		
		sm = new SctpMessage(nodeno);
		sv = new SctpVectorClock();
		st = new SctpToken();
		sme=new SctpME();
		

		SctpServer s1 = new SctpServer(nodeno);
		Thread t1 = new Thread(s1);
		t1.start();
		
		Thread.sleep(1000);

		System.out
		.println("Press enter to start Stock Application...Make sure your all node servers are UP");
		br.readLine();
		
		SctpClient c1 = new SctpClient(nodeno);
		Thread t2 = new Thread(c1);
		t2.start();

		t1.join();
		t2.join();
		
		for (int i = 0; i < Configfilereader.totalnodes; i++) {
			System.out.print(sv.Request_Node[i] + "\t");
			
		}
		

	}
}
