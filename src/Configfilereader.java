import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Configfilereader {
	public static Integer totalnodes;
	public static int numberofmessages = 0;
	public static int inittokenholder = 0;
	public static int[] Machineno = new int[100];
	public static String[] Machinename = new String[100];
	public static int[] Machineport = new int[100];
	int i = -1;
	public static boolean isbug = false;
	public static int requestornodes = 0;

	public void readfile() throws IOException {
		File file = new File("Config.txt");
		BufferedReader fr = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "ASCII"));
		String line = fr.readLine();
		String[] tokens = line.split(" ");
		
		// Fetching the first line of config file
		totalnodes = Integer.parseInt(tokens[0]);
		numberofmessages = Integer.parseInt(tokens[1]);
		inittokenholder = Integer.parseInt(tokens[2]);
		requestornodes = Integer.parseInt(tokens[3]);

		// Fetch rest of the details from config file
		if (tokens[4].equals("bugit")) {
			isbug = true;
			System.out.println("ConfigfileReader : Bug introduced in the code");
			SctpMain.LOG.logger.info("\tConfigfileReader : Bug introduced in the code");
		}

		while (true) {
			line = fr.readLine();
			if (line == null)
				break;
			i++;
			tokens = line.split(" ");// those are your words

			Machineno[i] = Integer.parseInt(tokens[0]);
			Machinename[i] = tokens[1];
			Machineport[i] = Integer.parseInt(tokens[2]);
		}

	}


}
