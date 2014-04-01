package MusicSheet;

public enum NoteNames {
	C, CSHARP, DFLAT, D, DSHARP, EFLAT, E, ESHARP, FFLAT, F, FSHARP, GFLAT, G, GSHARP, AFLAT, A, ASHARP, BFLAT, B, BSHARP, CFLAT;
	
	public static boolean contains(String test) {
		for(NoteNames n: NoteNames.values()) {
			if(n.name().equals(test)) {
				return true;
			}
		}
		return false;
	}
}