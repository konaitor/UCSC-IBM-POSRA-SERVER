package posra.dataaccess;

// Generated May 20, 2014 10:43:13 PM by Hibernate Tools 4.0.0

/**
 * ExternalId generated by hbm2java
 */
public class ExternalId implements java.io.Serializable {

	private String value;
	private Polymer polymer;

	public ExternalId() {
	}

	public ExternalId(String value, Polymer polymer) {
		this.value = value;
		this.polymer = polymer;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Polymer getPolymer() {
		return this.polymer;
	}

	public void setPolymer(Polymer polymer) {
		this.polymer = polymer;
	}

}