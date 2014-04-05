package MusicSheet;
import java.util.ArrayList;

public class Sheet {
	private String name;
	private ArrayList<Staff> staffs;
	
	public Sheet() {
		name = "";
	}
	
	public Sheet(String name) {
		this.name = name;
	}
	
	public void addStaff(Staff newStaff) {
		staffs.add(newStaff);
	}
}
