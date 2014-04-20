package File;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import MusicSheet.*;
import android.content.Context;

import com.leff.midi.*;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

/*
 * Class responsible for generating different file types
 * Possibly better to make this an inherited relationship for different filetypes
 * 
 * Currently only interacts with MidiFile to interpret the MusicSheet
 */
public class FileMaker {

	public static String TEST_FILENAME = "midiTest.mid";

	/*
	 * SHEET => MIDIFILE
	 */
	public static MidiFile sheetToMidi(Sheet s) {
		if (s != null) {
			// First track is always tempo map -- followed by array of
			// noteTracks (1 entry per staff)
			MidiTrack tempoTrack = new MidiTrack();
			ArrayList<MidiTrack> noteTracks;

			// Start with time events
			// Time Signature
			TimeSignature ts = new TimeSignature();
			EnumTimeSignature sheetTS = s.getStaff(0).getSignature(0)
					.getTimeSignature();

			// MIDI Clocks per whole note (constant for now I think)
			// TODO Verify the relationship is for whole note not measure
			int midiClocks = 96;
			int numerator = EnumTimeSignature.getNumerator(sheetTS);
			int denominator = EnumTimeSignature.getDenom(sheetTS);
			int meter = midiClocks / denominator; // Midi clocks per metronome
													// click

			ts.setTimeSignature(numerator, denominator, meter,
					TimeSignature.DEFAULT_DIVISION);

			// Tempo
			Tempo t = new Tempo();
			int sheetT = s.getStaff(0).getSignature(0).getTempo();

			t.setBpm(sheetT);

			// Insert events to tempoTrack
			tempoTrack.insertEvent(ts);
			tempoTrack.insertEvent(t);

			// Begin constructing the noteTracks
			noteTracks = createNoteTracks(s);

			// tempoTrack should always be the first track
			noteTracks.add(0, tempoTrack);

			return (new MidiFile(MidiFile.DEFAULT_RESOLUTION, noteTracks));
		} else {
			return null;
		}
	}

	private static ArrayList<MidiTrack> createNoteTracks(Sheet s) {
		int numStaffs = s.getStaffSize();

		ArrayList<MidiTrack> noteTracks = new ArrayList<MidiTrack>();
		MidiTrack newTrack;

		// Create 1 track per staff
		for (int i = 0; i < numStaffs; i++) {
			newTrack = createSingleTrack(s, i);

			// On each track iterate through the all signatures and create
			// the tracks

			noteTracks.add(newTrack);

		}

		return noteTracks;
	}

	private static MidiTrack createSingleTrack(Sheet s, int staffIndex) {
		int numSignatures = s.getStaff(staffIndex).getSize();

		MidiTrack noteTrack = new MidiTrack();

		// For all signatures that belong to the track
		for (int i = 0; i < numSignatures; i++) {

			// Insert note events to the track
			noteTrack = insertMeasureEvents(noteTrack, s, staffIndex, i);
		}

		return noteTrack;
	}

	private static MidiTrack insertMeasureEvents(MidiTrack track, Sheet s,
			int staffIndex, int signatureIndex) {
		int numMeasures = s.getStaff(staffIndex).getSignature(signatureIndex).getSize();
		
		// For all measures in the signature
		for(int i = 0; i < numMeasures; i++){
			
			track = insertChordEvents(track, s, staffIndex, signatureIndex, i);
		}
		
		return track;
	}
	
	private static MidiTrack insertChordEvents(MidiTrack track, Sheet s, int staffIndex, int signatureIndex, int measureIndex){
		int numChords = Measure.getSize();
		Chord currentChord;
		Note currentNote;
		EnumTimeSignature t;
		
		int num, denom, beatsPerQN, divisionsPerQN;
		
		// Parameters for note events
		int channel = 0, velocity = 100, pitch;
		long tick, duration;
		
		// For all chords in the measure
		for(int i = 0; i < numChords; i++){
			currentChord = s.getStaff(staffIndex).getSignature(signatureIndex).getMeasure(measureIndex).getChord(i);
			if(currentChord != null){
				// If the chord isn't null, insert each note in the chord at the end of the track
				for(int j = 0; j < currentChord.getSize(); j++){
					currentNote = currentChord.getNote(j);
					
					pitch = currentNote.getMidiPitch();
					
					
					/*
					 *  Position in the track -- apparently it's like a timestamp. Thought events were supposed to be relative.
					 *  
					 *  In that case, tick values for an event in the sheeFt should be...
					 *  
					 *  ((MidiFile resolution in PPQ) * (Quarter notes per measure) * measureIndex) 
					 *  	+ (Div. Position in measure) * (Resolution per measure division)
					 *  
					 *   Pulses per measure division = PPQ / Divisions per Quarter Note
					 */
					
					// TODO Will probably break when using time signatures that don't have even beats per quarter note
					t = s.getStaff(staffIndex).getSignature(signatureIndex).getTimeSignature();
					
					num = EnumTimeSignature.getNumerator(t);
					denom = EnumTimeSignature.getDenom(t);
					beatsPerQN = denom / 4;
					
					divisionsPerQN = numChords / denom;
					
					// Finally calculate the tick timestamp
					tick = (MidiFile.DEFAULT_RESOLUTION * (beatsPerQN * num) * measureIndex) + (i * (MidiFile.DEFAULT_RESOLUTION / divisionsPerQN));
					
					duration = currentNote.getNoteDurationInTicks(MidiFile.DEFAULT_RESOLUTION);
					
					track.insertNote(channel, pitch, velocity, tick, duration);
				}
			}
		}
		
		return track;
	}
	
	/*
	 * MIDI => SHEET
	 */
	
	public static Sheet midiToSheet(MidiFile midi) {
		return null;
	}

	public MidiFile loadMidiFile(FileInputStream fis) {
		MidiFile midi = null;
		try {
			midi = new MidiFile(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return midi;
	}
	
	public static void writeSheetToMidi(Sheet s, Context c) {
		MidiFile midi = sheetToMidi(s);
		
		String path = c.getFilesDir().toString();

		File output = new File(path, TEST_FILENAME);
		try {
			midi.writeToFile(output);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	private static MidiFile testSampleFile() {
		// Create some sample tracks, just a couple notes

		// Start with tracks,
		MidiTrack tempoTrack = new MidiTrack();
		MidiTrack noteTrack = new MidiTrack();

		// Add events to the tracks -- start with setting timesignature and
		// tempo
		TimeSignature ts = new TimeSignature();
		ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER,
				TimeSignature.DEFAULT_METER);

		Tempo t = new Tempo();
		t.setBpm(120);

		tempoTrack.insertEvent(ts);
		tempoTrack.insertEvent(t);

		// Construct track 1
		for (int i = 0; i < 80; i++) {
			int channel = 0, pitch = 1 + i, velocity = 100;
			noteTrack.insertNote(channel, pitch + 2, velocity, i * 480, 120);
		}

		// Throw them into an arraylist to pass to the midifile object
		ArrayList<MidiTrack> tracks = new ArrayList<MidiTrack>();
		tracks.add(tempoTrack);
		tracks.add(noteTrack);

		// Pass and return
		MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);

		return midi;
	}

	public static void writeTestFile(Context c) {
		MidiFile midi = testSampleFile();

		String path = c.getFilesDir().toString();

		File output = new File(path, TEST_FILENAME);
		try {
			midi.writeToFile(output);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}