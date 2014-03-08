import java.util.ArrayList;

public class Measure {
	private int chordMap;
	private ArrayList<Chord> chordList;
	
	public Measure() {
		chordMap = 0;
	}
	
	/**
	 * Adds a chord to the measure at the specified division. 
	 * Measures are divided into 32 segments as to allow for up to 32nd notes
	 * @param startPoint falls between 0 and 31, inclusive.
	 */
	
	public void addChord(int startPoint) {
		assert startPoint > -1 && startPoint < 32;
		
	}
}
