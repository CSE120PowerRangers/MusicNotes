package MusicUtil;

public class NoteTool {

	NoteType myType;
	int imageID;
	
	public NoteTool( NoteType currentType, int imageID)
	{
		this.myType = currentType;
		this.imageID = imageID;
	}
	
	public NoteType getType()
	{
		return myType;
	}
	public int getID()
	{
		return imageID;
	}
}
