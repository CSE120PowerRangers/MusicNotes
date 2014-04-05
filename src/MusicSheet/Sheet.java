package MusicSheet;
import java.util.ArrayList;

public class Sheet {
	private String name;
	private ArrayList<Staff> staffs;
	
	public Sheet() {
		name = "";
		staffs.add(new Staff());
	}
	
	public Sheet(String name) {
		this.name = name;
		staffs.add(new Staff());
	}
	
	public Sheet(Sheet toCopy) {
		this.name = new String(toCopy.name);
		this.staffs = new ArrayList<Staff>(toCopy.staffs);
	}
	
	public void addStaff(Staff newStaff) {
		staffs.add(newStaff);
	}
	
	public void deleteStaff(int index) {
		staffs.remove(index);
	}
	
	public void deleteStaff(Staff toDelete) {
		staffs.remove(toDelete);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() { 
		return name;
	}
	
	public Staff getStaff(int index) {
		return staffs.get(index);
	}
	
	public int getStaffSize() {
		return staffs.size();
	}
}
