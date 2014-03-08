public class Measure {
	private static int numDivisions = 32;
	private int chordMap;
	private Chord[] chordList;
	
	public Measure() {
		chordMap = 0;
		chordList = new Chord[numDivisions];
	}
	
	/**
	 * Adds a chord to the measure at the specified division. 
	 * Measures are divided into 32 segments as to allow for up to 32nd notes
	 * @param startPoint falls between 0 and numDivisions, zero inclusive.
	 */
	public void addChord(int startPoint) {
		assert (startPoint > -1 && startPoint < numDivisions) : "Invalid chord starting position on the measure!";
		if(chordList[startPoint] == null) {
			//Valid position for the chord
			chordList[startPoint] = new Chord();
			chordMap = (chordMap >> startPoint) | 1;
		} else {
			//Chord already exists at this position on the measure
			return;
		}
	}
	
	/**
	 * Deletes a chord from the measure at the specified division
	 * @param startPoint falls between 0 and numDivisions, zero inclusive.
	 * Specifies which chord to get rid of
	 */
	public void deleteChord(int startPoint) {
		assert (startPoint > -1 && startPoint < numDivisions) : "Invalid chord starting position on the measure!";
		//Sketchy object deletion. May change in the future
		chordList[startPoint] = null;
	}
	
	/**
	 * Retrieves the chord beginning at the specified division
	 * @param targetPoint falls between 0 and numDivisions, zero inclusive. Details which division on the measure to look at
	 * @return target chord on the specified division
	 */
	public Chord getChord(int targetPoint) {
		if(chordList[targetPoint] == null) {
			//Chord doesn't exist here
			return null;
		}
		return chordList[targetPoint];
	}
}
