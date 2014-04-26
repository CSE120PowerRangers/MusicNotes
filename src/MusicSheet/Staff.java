package MusicSheet;

import java.util.ArrayList;

import MusicUtil.EnumClef;
import MusicUtil.NoteName;


public class Staff {
	private EnumClef clef;
	private NoteName[] notes;
	private int octave;
	private int program; //Midi program number
	private ArrayList<Measure> measures;
	
	/**
	 * Default constructor creates a staff with a treble clef and a default measure
	 */
	public Staff() {
		clef = EnumClef.TREBLE;
		setOctave();
		setScale();
		program = 0;
		measures = new ArrayList<Measure>();
		measures.add(new Measure());
	}
	
	/**
	 * Creates a staff with a given clef and a default measure
	 * @param clef
	 */
	public Staff(EnumClef clef) {
		this.clef = clef;
		setOctave();
		setScale();
		program = 0;
		measures = new ArrayList<Measure>();
		measures.add(new Measure());
	}
	
	/**
	 * Creates a staff with a given clef and measure
	 * @param clef
	 * @param measure
	 */
	public Staff(EnumClef clef, Measure measure) {
		this.clef = clef;
		setOctave();
		setScale();
		program = 0;
		measures = new ArrayList<Measure>();
		measures.add(new Measure(measure));
	}
	
	/**
	 * Copy constructor for the staff
	 * @param clef
	 * @param measure
	 */
	public Staff(Staff toCopy) {
		this.clef = toCopy.clef;
		this.measures = new ArrayList<Measure>(toCopy.measures);
		this.program = toCopy.program;
		this.octave = toCopy.octave;
	}
	
	/**
	 * Sets the clef of the staff
	 * @param newClef
	 */
	public void setClef(EnumClef newClef) {
		clef = newClef;
		setOctave();
		setScale();
	}

	/**
	 * Adds a given measure to the end of the list
	 * @param newMeasure
	 */
	public void add(Measure newMeasure) {
		measures.add(newMeasure);
	}

	/**
	 * Inserts a given measure into the given index
	 * @param index
	 * @param newMeasure
	 */
	public void add(int index, Measure newMeasure) {
		if(index >= 0 && index < measures.size()) {
			//Force creation of new copy of measure
			newMeasure = new Measure(newMeasure);
			measures.add(index, newMeasure);
		}
	}
	
	/**
	 * Inserts a given number of measures
	 * @param numberOfMeasures
	 */
	public void add(int numberOfMeasures) {
		for(int i = 0; i < numberOfMeasures; i++) {
			measures.add(new Measure());
		}
	}

	/**
	 * Gets the measure from the given index
	 * @param index
	 * @return
	 */
	public Measure get(int index) {
		if(index >= 0 && index < measures.size()) {
			return measures.get(index);
		} else {
			return null;
		}
	}
	
	/**
	 * Deletes a given measure from the list
	 * @param oldMeasure
	 */
	public void delete(Measure oldMeasure) {
		for(int i = 0; i < measures.size(); i++) {
			if(measures.get(i).equals(oldMeasure)) {
				measures.remove(i);
				break;
			}
		}
	}

	/**
	 * Returns the number of measures in the staff
	 * @return
	 */
	public int size() {
		return measures.size();
	}
	
	/**
	 * Returns the clef of the staff
	 * @return
	 */
	public EnumClef clef() {
		return clef;
	}
	
	/**
	 * Returns the octave
	 */
	public int octave()
	{
		return octave;
	}
	
	/**
	 * Sets the appropriate octave for the Clef
	 */
	private void setOctave()
	{
		switch(clef)
		{
		case TREBLE:
			octave = 5;
			break;
		case TENOR:
			octave = 4;
			break;
		case BASS:
			octave = 3;
			break;
		}
	}
	
	public NoteName[] scale()
	{
		return notes;
	}
	
	/**
	 * Sets the appropriate notes for the Clef
	 */
	private void setScale()
	{
		notes = new NoteName[15];
		switch(clef)
		{
		// TREBLE AND TENOR ARE THE SAME
		case TREBLE:
		case TENOR:
			notes[0] = NoteName.B;
			notes[1] = NoteName.A;
			notes[2] = NoteName.G;
			notes[3] = NoteName.F;
			notes[4] = NoteName.E;
			notes[5] = NoteName.D;
			notes[6] = NoteName.C;
			notes[7] = NoteName.B;
			notes[8] = NoteName.A;
			notes[9] = NoteName.G;
			notes[10] = NoteName.F;
			notes[11] = NoteName.E;
			notes[12] = NoteName.D;
			notes[13] = NoteName.C;
			notes[14] = NoteName.B;
			break;
		case BASS:
			notes[0] = NoteName.D;
			notes[1] = NoteName.C;
			notes[2] = NoteName.B;
			notes[3] = NoteName.A;
			notes[4] = NoteName.G;
			notes[5] = NoteName.F;
			notes[6] = NoteName.E;
			notes[7] = NoteName.D;
			notes[8] = NoteName.C;
			notes[9] = NoteName.B;
			notes[10] = NoteName.A;
			notes[11] = NoteName.G;
			notes[12] = NoteName.F;
			notes[13] = NoteName.E;
			notes[14] = NoteName.D;
			break;
		}
	}
}
