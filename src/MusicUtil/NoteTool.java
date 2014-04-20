package MusicUtil;

import com.example.musicnotes.EditorActivity;

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
