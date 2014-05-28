package posra.dataaccess;

// Generated May 28, 2014 12:19:44 PM by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;

/**
 * Smiles generated by hbm2java
 */
public class Smiles implements java.io.Serializable {

	private Integer smilesid;
	private String smilesstring;
	private Set segments = new HashSet(0);

	public Smiles() {
	}

	public Smiles(String smilesstring) {
		this.smilesstring = smilesstring;
	}

	public Smiles(String smilesstring, Set segments) {
		this.smilesstring = smilesstring;
		this.segments = segments;
	}

	public Integer getSmilesid() {
		return this.smilesid;
	}

	public void setSmilesid(Integer smilesid) {
		this.smilesid = smilesid;
	}

	public String getSmilesstring() {
		return this.smilesstring;
	}

	public void setSmilesstring(String smilesstring) {
		this.smilesstring = smilesstring;
	}

	public Set getSegments() {
		return this.segments;
	}

	public void setSegments(Set segments) {
		this.segments = segments;
	}

}
