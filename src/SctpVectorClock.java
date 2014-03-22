/*
- RN : request node :
Each node has an array RN of size N for recording the largest sequence number ever received from each one of the other nodes
when we make new request update RN as well
*/


public class SctpVectorClock {
public static int[] Request_Node=new int[Configfilereader.totalnodes];

public SctpVectorClock()
{
	System.out.println("Initializing Vector Clock content to 0");
	for(int i=0;i<Configfilereader.totalnodes;i++)
	{
				Request_Node[i]=0;
	}
}

//call this method when you generate own request
public void incrementVectorClock(int node_no)
{
	Request_Node[(node_no-1)]=Request_Node[(node_no-1)] + 1;
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
