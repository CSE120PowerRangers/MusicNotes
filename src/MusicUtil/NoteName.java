package MusicUtil;

public enum NoteName {
	C, 
	CSHARP, 
	DFLAT, 
	D, 
	DSHARP, 
	EFLAT, 
	E, 
	ESHARP, 
	FFLAT, 
	F, 
	FSHARP, 
	GFLAT, 
	G, 
	GSHARP, 
	AFLAT, 
	A, 
	ASHARP, 
	BFLAT, 
	B, 
	BSHARP, 
	CFLAT;
	
	public static boolean contains(String test) {
		for(NoteName n: NoteName.values()) {
			if(n.name().equals(test)) {
				return true;
			}
		}
		return false;
	}
}