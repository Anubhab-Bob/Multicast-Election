/**
 * @author Anubhab
 */

// Importing necessary classes
import java.util.Random;

public class Voter extends Thread {		// Provides the Presentation layer for each voter
	
	int votes_Smith, votes_Jones;
	int voterID, numOfVoters;
	VotingHelper myVote;
	
	public Voter(int id, int voters) {
		votes_Smith = 0;
		votes_Jones = 0;
		voterID = id;
		numOfVoters = voters;
		// multicast group address and port numbers are considered fixed
		myVote = new VotingHelper("239.1.2.3", 3456, voterID, numOfVoters);
	}
	
	public void vote() {
		try {
			Random rand = new Random();
			int v = rand.nextInt(2);	// generate a random integer in the range 0-1
			// 0 corresponds to vote for Smith
			// 1 corresponds to vote for Jones
			myVote.castVote(voterID, v);	// cast vote corresponding to the number generated
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public void calculateVotes() {
		myVote.calculate();	// voter leaves multicasting session after calculation
	}
	
	@Override
	public synchronized void run() {
		myVote.receiveVote();
	}
}

