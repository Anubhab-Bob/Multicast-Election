public class VoterThreads {	// This takes care of multiple voter threads

	public static void main(String args[]) {
		if (args.length != 1) {
			System.out.println("Server requires number of voters as argument!");
			return;
		}
		try {		
			int groupSize = Integer.parseInt(args[0]);
			Voter voterThreads[] = new Voter[groupSize];			
			for(int i = 0; i < groupSize; i++) {	// each voter is ready to receive votes
				voterThreads[i] =  new Voter(i+1, groupSize);
				voterThreads[i].start();
			}
			for(int i = 0; i < groupSize; i++)		// cast vote from each voter
				voterThreads[i].vote();
				
			for(int i = 0; i < groupSize; i++)		// ensure all voters have voted and calculated results
				voterThreads[i].join();
			
			System.out.println("\n\nResults : \n");
			for(int i = 0; i < groupSize; i++) {	// display results for each voter
				System.out.println("Voter " + (i + 1) + " : ");
				voterThreads[i].calculateVotes();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}