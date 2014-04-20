package MusicSheet;
import java.util.ArrayList;

public class Sheet {
	private String name;
	private ArrayList<Staff> staffs;
	
	/**
	 * The sheet constructor creates a new sheet and automatically generates a default staff
	 */
	public Sheet() {
		name = "";
		staffs = new ArrayList<Staff>();
		staffs.add(new Staff());
	}
	
	/**
	 * Instantiates a sheet with a given name
	 * @param name
	 */
	public Sheet(String name) {
		this.name = name;
		staffs = new ArrayList<Staff>();
		staffs.add(new Staff());
	}
	
	/**
	 * Copy constructor for a sheet
	 * @param toCopy
	 */
	public Sheet(Sheet toCopy) {
		this.name = new String(toCopy.name);
		this.staffs = new ArrayList<Staff>(toCopy.staffs);
	}
	
	/**
	 * Adds a staff to the sheet
	 * @param newStaff
	 */
	public void addStaff(Staff newStaff) {
		staffs.add(newStaff);
	}
	
	/**
	 * Deletes a given staff by index lookup
	 * @param index
	 */
	public void deleteStaff(int index) {
		staffs.remove(index);
	}
	
	/**
	 * Deletes a given staff by object lookup
	 * @param toDelete
	 */
	public void deleteStaff(Staff toDelete) {
		staffs.remove(toDelete);
	}
	
	/**
	 * Change the name of a sheet
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get the name of a sheet
	 * @return
	 */
	public String getName() { 
		return name;
	}
	
	/**
	 * Get a staff from the sheet
	 * @param index
	 * @return
	 */
	public Staff getStaff(int index) {
		return staffs.get(index);
	}
	
	/**
	 * Gets the number of staffs in the sheet
	 * @return 
	 */
	public int getStaffSize() {
		return staffs.size();
	}
}
