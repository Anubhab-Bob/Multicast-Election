/**
 * @author Anubhab
 */

// Importing necessary classes
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.io.IOException;

public class VotingHelper {		// Provides the Services layer for each voter
	
	int votes_Smith, votes_Jones;
	MulticastSocket s;
	int multicastPort;
    InetAddress group;
    int currentVoterID;
    int multicastSize;
    
    @SuppressWarnings("deprecation")
	VotingHelper(String groupName, int groupPort, int voterID, int numOfVoters) {
    	try {
    		group = InetAddress.getByName(groupName);
    		multicastPort = groupPort;
    		s = new MulticastSocket(multicastPort);		// create multicast socket
    		s.setTimeToLive(32);   // restrict multicast to processes
    		s.joinGroup(group);		// join multicast group
    		votes_Smith = 0;
    		votes_Jones = 0;
    		currentVoterID = voterID;
    		multicastSize = numOfVoters;
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    void castVote(int voterID, int choice) {
    	try {
    		// create string containing vote and voter ID
    		String vote = Integer.toString(voterID) + " " + Integer.toString(choice);
    		// create a Datagram packet for the vote
	        DatagramPacket packet = new DatagramPacket(vote.getBytes(), vote.length(), group, multicastPort);
	        s.send(packet);		// multicast the vote to the group
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    void receiveVote() {
    	try {
    		for(int i = multicastSize; i > 0; i--) {	// receive votes from all members of the group
	    		byte[] buf = new byte[100];		// byte array to receive the messages
		        DatagramPacket recv = new DatagramPacket(buf, buf.length);
		        s.receive(recv);	// receive the vote from the socket
		        String received = new String(buf);
		        received = received.trim();
		        String voter = received.substring(0, received.indexOf(' '));	// extract voter ID
		        String vote = received.substring(received.indexOf(' ') + 1);	// extract vote
		        if(Integer.parseInt(vote) == 0) {	// 0 corresponds to vote for Smith
		        	this.votes_Smith++;
		        	System.out.println("Voter " + currentVoterID + " ---> Voter " + voter + " voted for Smith.");
		        }
		        else if(Integer.parseInt(vote) == 1) {	// 1 corresponds to vote for Jones
		        	this.votes_Jones++;
		        	System.out.println("Voter " + currentVoterID + " ---> Voter " + voter + " voted for Jones.");
		        }
    		}	// election ends when all voters have voted
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @SuppressWarnings("deprecation")
	public void calculate() {	// show results of election per voter
		System.out.println("Smith got " + votes_Smith + " vote(s)\nJones got " + votes_Jones + " vote(s)\n");
		try {
			s.leaveGroup(group);	// leave group after election is complete
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

