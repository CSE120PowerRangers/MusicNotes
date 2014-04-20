package MusicUtil;

public enum EnumTimeSignature {
	TWO_FOUR,
	THREE_FOUR,
	FOUR_FOUR,
	THREE_EIGHT,
	SIX_EIGHT,
	SEVEN_EIGHT;
	
	public static int getNumerator(EnumTimeSignature t){
		switch(t){
		case FOUR_FOUR:
			return 4;
		case SEVEN_EIGHT:
			return 7;
		case SIX_EIGHT:
			return 6;
		case THREE_EIGHT:
			return 3;
		case THREE_FOUR:
			return 3;
		case TWO_FOUR:
			return 2;
		default:
			return 4;
		}
	}
	
	public static int getDenom(EnumTimeSignature t) {
		switch(t){
		case FOUR_FOUR:
			return 4;
		case SEVEN_EIGHT:
			return 8;
		case SIX_EIGHT:
			return 8;
		case THREE_EIGHT:
			return 8;
		case THREE_FOUR:
			return 4;
		case TWO_FOUR:
			return 4;
		default:
			return 4;
		
		}
	}
}
