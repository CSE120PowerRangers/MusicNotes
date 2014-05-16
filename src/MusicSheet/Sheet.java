package MusicSheet;
import java.io.Serializable;
import java.util.ArrayList;

public class Sheet implements Serializable{
	private String name;
	private ArrayList<Signature> signatures;

	/**
	 * The sheet constructor creates a new sheet and automatically generates a default Signature
	 */
	public Sheet() {
		name = "Default";
		signatures = new ArrayList<Signature>();
		signatures.add(new Signature());
	}
	
	/**
	 * Instantiates a sheet with a given name
	 * @param name
	 */
	public Sheet(String name) {
		this.name = name;
		signatures = new ArrayList<Signature>();
		signatures.add(new Signature());
	}
	
	/**
	 * Copy constructor for a sheet
	 * @param toCopy
	 */
	public Sheet(Sheet toCopy) {
		this.name = new String(toCopy.name);
		this.signatures = new ArrayList<Signature>(toCopy.signatures);
	}
	
	/**
	 * Adds a Signature to the sheet
	 * @param newSignature
	 */
	public void add(Signature newSignature) {
		signatures.add(newSignature);
	}
	
	/**
	 * Deletes a given Signature by index lookup
	 * @param index
	 */
	public void delete(int index) {
		signatures.remove(index);
	}
	
	/**
	 * Deletes a given Signature by object lookup
	 * @param toDelete
	 */
	public void delete(Signature toDelete) {
		signatures.remove(toDelete);
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
	public String name() { 
		return name;
	}
	
	/**
	 * Get a Signature from the sheet
	 * @param index
	 * @return
	 */
	public Signature get(int index) {
		return signatures.get(index);
	}
	
	/**
	 * Gets the number of Signatures in the sheet
	 * @return 
	 */
	public int size() {
		return signatures.size();
	}

	public String getFileName()
	{
		return (name+".mid");
	}
}
