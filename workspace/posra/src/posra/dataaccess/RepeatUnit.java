package posra.dataaccess;

// Generated May 27, 2014 2:46:51 PM by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;

/**
 * RepeatUnit generated by hbm2java
 */
public class RepeatUnit implements java.io.Serializable {

	private Integer repeatUnitId;
	private Set segments = new HashSet(0);
	private Set polymerRepeatUnitSegmentAssociations = new HashSet(0);

	public RepeatUnit() {
	}

	public RepeatUnit(Set segments, Set polymerRepeatUnitSegmentAssociations) {
		this.segments = segments;
		this.polymerRepeatUnitSegmentAssociations = polymerRepeatUnitSegmentAssociations;
	}

	public Integer getRepeatUnitId() {
		return this.repeatUnitId;
	}

	public void setRepeatUnitId(Integer repeatUnitId) {
		this.repeatUnitId = repeatUnitId;
	}

	public Set getSegments() {
		return this.segments;
	}

	public void setSegments(Set segments) {
		this.segments = segments;
	}

	public Set getPolymerRepeatUnitSegmentAssociations() {
		return this.polymerRepeatUnitSegmentAssociations;
	}

	public void setPolymerRepeatUnitSegmentAssociations(
			Set polymerRepeatUnitSegmentAssociations) {
		this.polymerRepeatUnitSegmentAssociations = polymerRepeatUnitSegmentAssociations;
	}

}
