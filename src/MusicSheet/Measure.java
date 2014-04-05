package MusicSheet;

public class Measure {
	private static int numDivisions = 8; //Currently supporting up to eighth notes only
	private int chordMap;
	private Chord[] chordList;
	
	/**
	 * Measure() is a constructor that creates a new measure object
	 * It sets the chord bitmap to 0 and creates a new array of chords with a set number of divisions
	 * By default we support 32 divisions of a measure to allow for 32nd notes
	 * The chord bitmap merely says whether or not a chord starts at this division or not
	 */
	public Measure() {
		chordMap = 0;
		chordList = new Chord[numDivisions];
	}
	
	public Measure(Measure toCopy) {
		this.chordMap = toCopy.chordMap;
		for(int i = 0; i < numDivisions; i++) {
			if(toCopy.chordList[i].equals(null)) {
				this.chordList[i] = null;
			} else {
				this.chordList[i] = new Chord(toCopy.chordList[i]);
			}
		}
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
	
	public void addChord(int startPoint, Chord newChord) {
		assert (startPoint > -1 && startPoint < numDivisions) : "Invalid chord starting position on the measure!";
		if(chordList[startPoint] == null) {
			chordList[startPoint] = newChord;
		} else {
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
