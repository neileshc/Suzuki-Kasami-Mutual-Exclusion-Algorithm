import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SctpMessage implements Serializable {
	
	//Declare all the content and add it to appropriate functions here that you want to be a part of message
	private String content;
	private int[] tokenVector = new int[Configfilereader.totalnodes];
	private int[] tokenQueue = new int[Configfilereader.totalnodes];
	private boolean is_msg_contains_token;
	private boolean is_msg_request;
	private int node_no;
	private int seq_no;
	
	public void setSeq_no(int seq_no) {
		this.seq_no = seq_no;
	}

	public int getSeq_no() {
		return seq_no;
	}

	public int getNode_no() {
		return node_no;
	}

	
	
	


	public SctpMessage(int nodeno) {
				
		// Initialize Vector clock to 0
		for (int i = 0; i < Configfilereader.totalnodes; i++) {
			tokenVector[i] = 0;
			tokenQueue[i] = 0;
		}
		content = "Initialized message";
		is_msg_contains_token=false;
		node_no=nodeno;
		seq_no=SctpVectorClock.get_my_seq_no(node_no);
		is_msg_request=false;
	}

	
	
	public boolean isIs_msg_contains_token() {
		return is_msg_contains_token;
	}



	public void setIs_msg_contains_token(boolean is_msg_contains_token) {
		this.is_msg_contains_token = is_msg_contains_token;
	}


	public boolean isIs_msg_request() {
		return is_msg_request;
	}


	public void setIs_msg_request(boolean is_msg_request) {
		this.is_msg_request = is_msg_request;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
	public int[] getTokenVector() {
		return tokenVector;
	}

	public void setTokenVector(int[] tokenVector) {
		this.tokenVector = tokenVector;
	}

		
	public int[] getTokenQueue() {
		return tokenQueue;
	}


	public void setTokenQueue(int[] tokenQueue) {
		this.tokenQueue = tokenQueue;
	}

	
	
	
	
	
	public static byte[] serialize(Object obj) throws IOException {
		ObjectOutputStream out;// = new ObjectOutputStream();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		out = new ObjectOutputStream(bos);
		out.writeObject(obj);
		return bos.toByteArray();
	}

	public static Object deserialize(byte[] obj) throws IOException,
			ClassNotFoundException {
		ObjectInputStream in;// = new ObjectOutputStream();
		ByteArrayInputStream bos = new ByteArrayInputStream(obj);
		in = new ObjectInputStream(bos);
		return in.readObject();
	}

}
