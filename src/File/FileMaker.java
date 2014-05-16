package File;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import MusicSheet.*;
import MusicUtil.*;
import android.content.Context;
import android.os.Environment;

import com.leff.midi.*;
import com.leff.midi.event.*;
import com.leff.midi.event.meta.*;

/*
 * Class responsible for generating different file types
 * Possibly better to make this an inherited relationship for different filetypes
 * 
 * Currently only interacts with MidiFile to interpret the MusicSheet
 */
public class FileMaker {

	public static String TEST_FILENAME = "midiTest.mid";
	public static long signatureOffset = 0;
	/*
	 * SHEET => MIDIFILE
	 */
	public static MidiFile sheetToMidi(Sheet s) {
		// Reset the offset value
		signatureOffset = 0;
		
		if (s != null) {
			// First track is always tempo map -- followed by array of noteTracks (1 entry per staff)
			MidiTrack tempoTrack = new MidiTrack();
			ArrayList<MidiTrack> noteTracks;

			// Start with time events
			TimeSignature ts = new TimeSignature();
			EnumTimeSignature sheetTS = s.get(0).timeSignature();

			// MIDI Clocks per whole note (constant for now I think)
			// TODO Verify the relationship is for whole note not measure
			int midiClocks = 96;
			int numerator = EnumTimeSignature.getNumerator(sheetTS);
			int denominator = EnumTimeSignature.getDenom(sheetTS);
			int meter = midiClocks / denominator; // Midi clocks per metronome click

			ts.setTimeSignature(numerator, denominator, meter, TimeSignature.DEFAULT_DIVISION);

			Tempo t = new Tempo();
			int tempo = s.get(0).tempo();
			t.setBpm(tempo);

			// Insert events to tempoTrack
			tempoTrack.insertEvent(ts);
			tempoTrack.insertEvent(t);

			noteTracks = createNoteTracks(s);

			// tempoTrack should always be the first track
			noteTracks.add(0, tempoTrack);

			return (new MidiFile(MidiFile.DEFAULT_RESOLUTION, noteTracks));
		} else {
			return null;
		}
	}

	private static ArrayList<MidiTrack> createNoteTracks(Sheet s) {
		int numStaffs = s.get(0).size();

		ArrayList<MidiTrack> noteTracks = new ArrayList<MidiTrack>();
		MidiTrack newTrack;

		for (int i = 0; i < numStaffs; i++) {
			newTrack = createSingleTrack(s, i);
			noteTracks.add(newTrack);
		}

		return noteTracks;
	}

	private static MidiTrack createSingleTrack(Sheet s, int staffIndex) {
		int numSignatures = s.size();

		MidiTrack noteTrack = new MidiTrack();
		for (int i = 0; i < numSignatures; i++) {
			noteTrack = insertMeasureEvents(noteTrack, s, staffIndex, i);
		}

		return noteTrack;
	}

	private static MidiTrack insertMeasureEvents(MidiTrack track, Sheet s, int staffIndex, int signatureIndex) {
		int numMeasures = s.get(signatureIndex).get(staffIndex).size();
		long signatureLength = 0;

		for (int i = 0; i < numMeasures; i++) {
			track = insertChordEvents(track, s, staffIndex, signatureIndex, i, signatureLength);
		}
		//System.out.println(track.getLengthInTicks());
		signatureOffset += track.getLengthInTicks();
		return track;
	}

	private static MidiTrack insertChordEvents(MidiTrack track, Sheet s, int staffIndex, int signatureIndex, int measureIndex, long length) {
		// Getting the number of divisions no longer accurate this way, using a temp function to determine division size based on time signature
		//int numChords = s.get(staffIndex).get(signatureIndex).get(measureIndex).size();
		
		int numChords = getDivisions(s.get(signatureIndex).timeSignature());
		Chord currentChord;
		Note currentNote;
		EnumTimeSignature t;

		int num, denom, beatsPerQN, divisionsPerQN;

		// Parameters for note events
		int channel = 0, velocity = 100, pitch;
		long tick, duration;

		// For all chords in the measure
		for (int i = 0; i < numChords; i++) {
			currentChord = s.get(signatureIndex).get(staffIndex).get(measureIndex).get(i);
			if (currentChord != null) {
				// If the chord isn't null, insert each note in the chord at the end of the track
				for (int j = 0; j < currentChord.size(); j++) {
					currentNote = currentChord.get(j);

					pitch = currentNote.getMidiPitch();

					/*
					 * Position in the track -- apparently it's like a
					 * timestamp. Thought events were supposed to be relative.
					 * 
					 * In that case, tick values for an event in the sheeFt
					 * should be...
					 * 
					 * ((MidiFile resolution in PPQ) * (Quarter notes per
					 * measure) * measureIndex) + (Div. Position in measure) *
					 * (Resolution per measure division)
					 * 
					 * Pulses per measure division = PPQ / Divisions per Quarter
					 * Note
					 */

					// TODO Will probably break when using time signatures that
					// don't have even beats per quarter note
					t = s.get(signatureIndex).timeSignature();

					num = EnumTimeSignature.getNumerator(t);
					denom = EnumTimeSignature.getDenom(t);
					beatsPerQN = denom / 4;

					divisionsPerQN = numChords / denom; //Should be divisionType / 4?

					// Finally calculate the tick timestamp
					tick = (MidiFile.DEFAULT_RESOLUTION * (beatsPerQN * num) * measureIndex)
							+ (i * (MidiFile.DEFAULT_RESOLUTION / divisionsPerQN)) + signatureOffset;

					//Update the length of the signature to the last tick
					length = tick;

					duration = currentNote.getNoteDurationInTicks(MidiFile.DEFAULT_RESOLUTION);

					track.insertNote(channel, pitch, velocity, tick, duration);
				}
			}
		}

		return track;
	}

	private static int getDivisions(EnumTimeSignature timeSignature) {
		// TODO Auto-generated method stub
		switch(timeSignature){
		case FOUR_FOUR:
			return 8;
		case SEVEN_EIGHT:
			return 7;
		case SIX_EIGHT:
			return 6;
		case THREE_EIGHT:
			return 3;
		case THREE_FOUR:
			return 6;
		case TWO_FOUR:
			return 4;
		default:
			return 8;
		}
	}

	/*
	 * MIDI => SHEET
	 */

	public static Sheet midiToSheet(MidiFile midi) {

		int numStaffs = midi.getTrackCount() - 1; // 1 staff per track - 1 for the tempo mapping track

		// Signature information
		EnumTimeSignature timeSig = EnumTimeSignature.FOUR_FOUR;
		EnumKeySignature keySig = EnumKeySignature.C_MAJOR;
		int tempo = 20, num = 4, den = 4;

		ArrayList<MidiTrack> tracks = midi.getTracks();

		// Decode the tempo mapping track
		TreeSet<MidiEvent> currentEvents = tracks.get(0).getEvents();
		Iterator<MidiEvent> it = currentEvents.iterator();
		MidiEvent currentEvent = it.next();

		while (it.hasNext()) {
			// Either the time signature event
			if (currentEvent.getClass() == TimeSignature.class) {
				num = ((TimeSignature) currentEvent).getNumerator();
				den = ((TimeSignature) currentEvent).getDenominatorValue();
				den = (int) Math.pow(2, den);

				timeSig = EnumTimeSignature.getTimeSig(num, den);
			}
			// Or the tempo event
			else if (currentEvent.getClass() == Tempo.class) {
				tempo = (int) ((Tempo) currentEvent).getBpm();
			}
			// Or something we probably shouldn't care about.
			else {

			}
			currentEvent = it.next();
		}

		// Create the sheet
		Sheet sheet = new Sheet();

		// Insert signature events.
		for (int i = 0; i < numStaffs; i++) {
			sheet.get(i).setKeySignature(keySig);
			sheet.get(i).setTempo(tempo);
			sheet.get(i).setTimeSignature(timeSig);
		}

		// Start deciphering the note on events
		for (int i = 0; i < numStaffs; i++) {
			// Set the current track events
			currentEvents = tracks.get(i+1).getEvents();

			it = currentEvents.iterator();
			currentEvent = it.next();

			/*
			 * Decipher the current event, Notes should come in pairs of NoteOn
			 * events, the first with a non-zero velocity, the second with a 0
			 * velocity, indicating the end of that note
			 */
			long lastTickValue = 0;
			Chord currentChord = new Chord();

			while (it.hasNext()) {
				if (currentEvent.getClass() == NoteOn.class) {
					// Construct the note based on event data
					NoteOn noteStart = (NoteOn) currentEvent;
					// Every NoteOn event comes in pairs -- One to start, one to end
					NoteOn noteEnd = (NoteOn) it.next();

					Note n = getNoteFromEvents(noteStart, noteEnd);

					// Determine if the note was valid
					if (n.type() != NoteType.NOTANOTE) {
						// Determine if this belongs in a new chord or not
						if (currentEvent.getTick() == lastTickValue) {
							currentChord.add(n.name(), n.type(), n.octave());
						} else {
							// Determine the position of the old chord
							int staffPos = i;
							int signaturePos = 0; // Determine this crap later.
							// Lazy.
							int measurePos = findMeasurePos(sheet, staffPos, signaturePos, lastTickValue);
							int chordPos = findChordPos(sheet, staffPos, signaturePos, lastTickValue);

							// Add the old chord to the sheet, create a new chord.
							sheet = insertChord(sheet, currentChord, staffPos, signaturePos, measurePos, chordPos);

							currentChord = new Chord();
							currentChord.add(n.name(), n.type(), n.octave());

							// Update the new tick value
							lastTickValue = currentEvent.getTick();
						}

					}
				}
			}
		}

		return sheet;
	}

	private static int findMeasurePos(Sheet s, int staffPos, int signaturePos, long lastTickValue) {
		// Determine the ticks per measure division
		EnumTimeSignature t;
		int num, den, QNPerMeasure, ticksPerMeasure;

		t = s.get(staffPos).timeSignature();
		num = EnumTimeSignature.getNumerator(t);
		den = EnumTimeSignature.getDenom(t);
		QNPerMeasure = (den / 4) * num;
		ticksPerMeasure = MidiFile.DEFAULT_RESOLUTION * QNPerMeasure;

		return (int) lastTickValue / ticksPerMeasure;
	}

	private static int findChordPos(Sheet s, int staffPos, int signaturePos, long lastTickValue) {
		EnumTimeSignature t;
		int num, den, QNPerMeasure, ticksPerMeasure, ticksPerDiv, measurePos;
		long tickDiff;

		t = s.get(staffPos).timeSignature();

		num = EnumTimeSignature.getNumerator(t);
		den = EnumTimeSignature.getDenom(t);
		QNPerMeasure = (den / 4) * num;
		ticksPerMeasure = MidiFile.DEFAULT_RESOLUTION * QNPerMeasure;
		measurePos = (int) lastTickValue / ticksPerMeasure;

		ticksPerDiv = ticksPerMeasure / s.get(staffPos).get(signaturePos).get(0).size();
		tickDiff = lastTickValue - (measurePos * ticksPerMeasure);

		return (int) tickDiff / ticksPerDiv;
	}

	private static Sheet insertChord(Sheet sheet, Chord currentChord,
			int staffPos, int signaturePos, int measurePos, int chordPos) {
		Sheet newSheet = new Sheet(sheet);

		newSheet.get(staffPos).get(signaturePos).get(measurePos).add(chordPos, currentChord);

		return newSheet;
	}

	private static Note getNoteFromEvents(NoteOn noteStart, NoteOn noteEnd) {

		Note n = new Note(NoteName.C, NoteType.NOTANOTE, 0);

		// Determine if these two notes are related by checking their pitch and channel
		if (noteStart.getChannel() == noteEnd.getChannel()
				&& noteStart.getNoteValue() == noteEnd.getNoteValue()
				&& noteStart.getVelocity() != 0 && noteEnd.getVelocity() == 0) {

			// Same note, determine pitch and duration
			NoteName name = getNoteNameFromMidiValue(noteStart.getNoteValue());
			NoteType duration = getNoteTypeFromMidiDelta(noteStart, noteEnd);

			int octave = getOctaveFromMidiValue(noteStart.getNoteValue());

			n = new Note(name, duration, octave);
		}
		return n;
	}

	private static int getOctaveFromMidiValue(int noteValue) {
		return (noteValue / 12);
	}

	private static NoteType getNoteTypeFromMidiDelta(NoteOn noteStart, NoteOn noteEnd) {
		// Get the duration of the note in ticks
		long delta = noteEnd.getTick() - noteStart.getTick();

		// Figure out how long that is based on the PPQ
		int modifier = (int) ((delta * 8) / MidiFile.DEFAULT_RESOLUTION);

		switch (modifier) {
		case 0:
			return NoteType.NOTANOTE;
		case 1:
			return NoteType.THIRTYSECOND_NOTE;
		case 2:
			return NoteType.SIXTEENTH_NOTE;
		case 4:
			return NoteType.EIGHTH_NOTE;
		case 8:
			return NoteType.QUARTER_NOTE;
		case 16:
			return NoteType.HALF_NOTE;
		case 32:
			return NoteType.WHOLE_NOTE;
		default:
			return NoteType.NOTANOTE;
		}
	}

	private static NoteName getNoteNameFromMidiValue(int noteValue) {
		int noteInOctave = noteValue % 12;

		switch (noteInOctave) {
		case 0:
			return NoteName.C;
		case 1:
			return NoteName.CSHARP;
		case 2:
			return NoteName.D;
		case 3:
			return NoteName.DSHARP;
		case 4:
			return NoteName.E;
		case 5:
			return NoteName.F;
		case 6:
			return NoteName.FSHARP;
		case 7:
			return NoteName.G;
		case 8:
			return NoteName.GSHARP;
		case 9:
			return NoteName.A;
		case 10:
			return NoteName.ASHARP;
		case 11:
			return NoteName.B;
		default:
			return null;
		}
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

	public static void writeSheetToMidiInternal(Sheet s, Context c) {
		MidiFile midi = sheetToMidi(s);
		File path = new File(Environment.getExternalStorageDirectory().toString()+"/MusicNotes/Midi");
		path.mkdirs();
		//File path = new File(c.getFilesDir().getAbsolutePath()+"/");
		File output = new File(path, s.getFileName());
		try {
			midi.writeToFile(output);
		} catch (IOException e) {
			System.err.println(e);
		}


	}


	public static Sheet loadMidi(Context context) {
		String stringPath = context.getFilesDir().getAbsolutePath() + "/" + FileMaker.TEST_FILENAME;

		File o = new File(stringPath);
		MidiFile midi;
		Sheet newSheet = new Sheet();

		try {
			midi = new MidiFile(o);
			newSheet = midiToSheet(midi);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newSheet;
	}

	private static MidiFile testSampleFile() {
		// Create some sample tracks, just a couple notes

		// Start with tracks,
		MidiTrack tempoTrack = new MidiTrack();
		MidiTrack noteTrack = new MidiTrack();

		// Add events to the tracks -- start with setting timesignature and
		// tempo
		TimeSignature ts = new TimeSignature();
		ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_METER);

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