import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import sun.rmi.runtime.Log;

public class SctpApp implements Runnable {

	// no of app execution per node
	int count;
	boolean lockattempt;

	BufferedWriter bufferedWriter;
	FileWriter fileWriter;
	String fileName = "/home/004/n/nx/nxc121930/AOS/Project2/Log.log";
	ReadWriteLock lock = new ReentrantReadWriteLock();

	public SctpApp() {
		System.out.println("Application : Application is initiated");
		SctpMain.LOG.logger.info("\tApplication : Application is initiated");

		lockattempt = false;
		count = 0;

		try {
			// only node 1 will clear the content
			if (SctpServer.mynodeno == 1)
				fileWriter = new FileWriter(fileName);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {

		if (SctpServer.mynodeno <= Configfilereader.requestornodes) {

			do {
				try {
					
					// Inducing random sleep so get random requests for critical section
					Random r = new Random();
					Thread.sleep(r.nextInt(5000));

					// Request critical section
					{
						System.out
								.println("Application : Requesting CS access");
						SctpMain.LOG.logger
								.info("\tApplication : Requesting CS access");

						if (SctpMain.sme.cs_enter()) {

							// Execute CS
							System.out
									.println("Application : CS access secured");
							SctpMain.LOG.logger
									.info("\tApplication : CS access secured");

							while (true) {
								if (lock.writeLock().tryLock()) {
									// lock the file
									System.out
											.println("Application : File Locked");
									SctpMain.LOG.logger
											.info("\tApplication : File Locked");
									break;
								} else {
									System.out
											.println("Application : Lock attempt failed....Retrying");
									lockattempt = true;
								}

							}

							// Write your application here

							FileWriter fileWriter = new FileWriter(fileName,
									true);

							// Always wrap FileWriter in BufferedWriter.
							bufferedWriter = new BufferedWriter(fileWriter);

							// this will verify if locking was failed due mutual
							// exclusion violation
							if (lockattempt) {
								bufferedWriter
										.write("ERROR : Lock attempt failed one or more time : "
												+ SctpServer.mynodeno);
								SctpMain.LOG.logger
										.info("\tERROR : Lock attempt failed one or more time : "
												+ SctpServer.mynodeno);
								System.out
										.println("ERROR : Lock attempt failed one or more time : "
												+ SctpServer.mynodeno);
							}

							bufferedWriter
									.write("\nOpen file_by_node_: "
											+ SctpServer.mynodeno
											+ " "
											+ SctpVectorClock
													.get_my_seq_no(SctpServer.mynodeno));
							SctpMain.LOG.logger
									.info("\tOpen file_by_node_: "
											+ SctpServer.mynodeno
											+ " "
											+ SctpVectorClock
													.get_my_seq_no(SctpServer.mynodeno));

							bufferedWriter.close();
							// wait for sometime
							Thread.sleep(3000);
						}

						if (SctpMain.sme.cs_leave()) {
							FileWriter fileWriter = new FileWriter(fileName,
									true);
							bufferedWriter = new BufferedWriter(fileWriter);

							bufferedWriter
									.write("\nClose file_from_node_: "
											+ SctpServer.mynodeno
											+ " "
											+ SctpVectorClock
													.get_my_seq_no(SctpServer.mynodeno));
							SctpMain.LOG.logger
									.info("\tClose file_from_node_: "
											+ SctpServer.mynodeno
											+ " "
											+ SctpVectorClock
													.get_my_seq_no(SctpServer.mynodeno));

							bufferedWriter.close();

							// unlock file
							lock.writeLock().unlock();

							System.out.println("Application : File UnLocked");
							SctpMain.LOG.logger
									.info("\tApplication : File UnLocked");

							// Left CS
							System.out.println("Application : Leaving  CS");
							SctpMain.LOG.logger
									.info("\tApplication : Leaving  CS");

						}
						count++;

					}

					Thread.sleep(3000);

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				} catch (IOException e) {

					// use this block
					e.printStackTrace();
				}

			} while (count < Configfilereader.numberofmessages);

		}

		try {
			// if i am not requestor node and i have token then i terminnate
			// immidiately before receiving request from
			// other nodes, sleep helps avoid that
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Termination Initiation
		int i = 0;
		if (SctpToken.doihavetoken == true) {
			for (i = 0; i < Configfilereader.totalnodes; i++) {
				if (SctpVectorClock.Request_Node[i] == SctpToken.tokenVector[i]) {
					continue;
				} else
					break;
			}

			if (i == Configfilereader.totalnodes) {
				System.out
						.println("Initiating termination as all of the requests are satisfied");
				SctpMain.Oktoterminate = true;

			}
		}
	}
}
