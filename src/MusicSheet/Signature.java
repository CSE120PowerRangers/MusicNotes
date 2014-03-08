package MusicSheet;
import java.util.ArrayList;

public class Signature {
	private int tempo;
	private int timeSignature;
	private int keySignature;
	private int[] flats;
	private int[] sharps;

	public Signature() {
		flats = new int[8];
		sharps = new int[8];
	}
	
	public void setKeySignature(String key) {
		KeySignature whichKey = KeySignature.valueOf(key);
		switch(whichKey) {
		case C_MAJOR:
			for(int i = 0; i < 8; i++) {
				sharps[i] = 0;
				flats[i] = 0;
			}
			break;
		//Sharp keys
		case CSHARP_MAJOR:
			sharps[NoteNames.B.ordinal()] = 1;
		case FSHARP_MAJOR:
			sharps[NoteNames.E.ordinal()] = 1;
		case B_MAJOR:
			sharps[NoteNames.A.ordinal()] = 1;
		case E_MAJOR:
			sharps[NoteNames.D.ordinal()] = 1;
		case A_MAJOR:
			sharps[NoteNames.G.ordinal()] = 1;
		case D_MAJOR:
			sharps[NoteNames.C.ordinal()] = 1;
		case G_MAJOR:
			sharps[NoteNames.F.ordinal()] = 1;
			for(int i = 0; i < 8; i++) {
				flats[i] = 0;
			}
			break;
			
		//Flat keys
		case CFLAT_MAJOR:
			flats[NoteNames.F.ordinal()] = 1;
		case GFLAT_MAJOR:
			flats[NoteNames.C.ordinal()] = 1;
		case DFLAT_MAJOR:
			flats[NoteNames.G.ordinal()] = 1;
		case AFLAT_MAJOR:
			flats[NoteNames.D.ordinal()] = 1;
		case EFLAT_MAJOR:
			flats[NoteNames.A.ordinal()] = 1;
		case BFLAT_MAJOR:
			flats[NoteNames.E.ordinal()] = 1;
		case F_MAJOR:
			flats[NoteNames.B.ordinal()] = 1;
			for(int i = 0; i < 8; i++) {
				sharps[i] = 0;
			}
			break;
			
		default:
			break;
		}
	}
	
	public void setTimeSignature(int tSig) {
		timeSignature = tSig;
	}
}
