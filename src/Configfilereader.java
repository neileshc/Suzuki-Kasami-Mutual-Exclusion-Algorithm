import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Configfilereader {
	public static Integer totalnodes;
	public static int numberofmessages = 5;
	public static int[] Machineno = new int[100];
	public static String[] Machinename = new String[100];
	public static int[] Machineport = new int[100];
	int i = -1;
	public static int timeforinternalmsg=3;

	public void readfile() throws IOException {
		File file = new File("Config.txt");
		BufferedReader fr = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "ASCII"));
		String line = fr.readLine();
		String[] tokens = line.split(" ");
		totalnodes = Integer.parseInt(tokens[0]);

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

	public void findmyserversocketport(Integer node) {

	}

	public void findmyclientsocketports(Integer node) {

	}

}
