/*
- RN : request node :
Each node has an array RN of size N for recording the largest sequence number ever received from each one of the other nodes
when we make new request update RN as well
*/


public class SctpVectorClock {

// Time stamp for every message 
//public static int[]TimeStamp = new int[Configfilereader.totalnodes];
	
// Mentains data about so far requests received by every node
public static int[] Request_Node=new int[Configfilereader.totalnodes];

// this is no of replies received , will be used for making decision to enter CS
public static int Reply_counter;

//Queue for the send reply function to check pending replies
public static int send_reply;

//Flag for the send request function 
public static boolean send_request; 





public SctpVectorClock()
{
	System.out.println("Initializing Request node and timestamp content to 0");
	for(int i=0;i<Configfilereader.totalnodes;i++)
	{
				Request_Node[i]=0;
				//TimeStamp[i]=0;
	}
	Reply_counter=0;
	
	// Un flag initial send request for all nodes
	send_request=false;
	send_reply=0;
	
}



// BSS Time Stamp Implementation methods------------------------------------

// returns current time stamp
//public static int[] getTimeStamp() {
//	return TimeStamp;
//}
//
//// increments its clock value by one on sending message
//public static void incrementTimeStamp(int node)
//{
//	TimeStamp[(node-1)]=TimeStamp[(node-1)] + 1;
//}
//
//
//// updates its time stamp based on received timestamp values.
//public static void updateTimeStamp(int receivenode,int[] receivetimeStamp) {
//	for(int i=0;i<Configfilereader.totalnodes;i++)
//	{
//		//currentvectorClock[i] = Math.max(currentvectorClock[i], receiveVector[i]);
//	    if(TimeStamp[i] >= receivetimeStamp[i])
//	    	TimeStamp[i] = receivetimeStamp[i];
//	    else
//	    	TimeStamp[i] =receivetimeStamp[i];
//	}
//	incrementTimeStamp(receivenode);
//
//}
//--------------------------------------------------------------------------------

// returns flag to broadcast request
public static boolean isSend_request() {
	return send_request;
}

// Sets the flag to broadcast request
// Client will pick up this flag 
public static void setSend_request(boolean send_request) {
	SctpVectorClock.send_request = send_request;
}


// returns flag status
public static int getSend_reply() {
	return send_reply;
}

// Updates the send reply queue , Send reply function from client will use this data
public static void setSend_reply(int nodeno) {
	send_reply=nodeno;
	
//	int i=0;
//	for(i=0;i<Configfilereader.totalnodes;i++)
//	{
//		// Possible bug in logic - lower priority might shift to higher one
//	if(SctpVectorClock.send_reply_queue[i]==0)
//		{
//			SctpVectorClock.send_reply_queue[i] = send_reply_queue;
//			break;
//		}
//	}
//	if(i==5)
//		System.out.println("\n Error: Send Reply Queue is full : undesirable");
	}




public static int getReply_counter() {
	return Reply_counter;
}

// we increment reply counter here so always receiev 1 as arg
public static void setReply_counter(int reply_counter) {
	Reply_counter = Reply_counter+reply_counter;
}





//call this method when you generate own request
public void incrementRequest_node()
{
	Request_Node[(SctpServer.mynodeno-1)]=Request_Node[(SctpServer.mynodeno-1)] + 1;
}


// retrieve the current RN content
public int[] getRequest_Node() {
	return Request_Node;
}

//retrieve the current sequence no while sending request
public static int get_my_seq_no(int node_no)
{
	return Request_Node[node_no-1];
	
}

//set the particular node with the incoming sequence no
public void setRequest_Node(int node_no,int seq_no) {

	if(Request_Node[node_no-1] >= seq_no)
    	Request_Node[node_no-1] = Request_Node[node_no-1];
    else
    	Request_Node[node_no-1] =seq_no;
		
}
}
