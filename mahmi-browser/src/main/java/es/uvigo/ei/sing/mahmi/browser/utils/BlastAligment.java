package es.uvigo.ei.sing.mahmi.browser.utils;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;

public class BlastAligment {
	private String description;
	private AminoAcidSequence sequence;
	private double score;
	private double eValue;
	private int identities;
	private int positives;
	private int gaps;
	private int length;
	private String subLocation;
	private double pI;
	
	public BlastAligment( final String description, 
						  final AminoAcidSequence sequence,
						  final double score, 
						  final double eValue, 
						  final int identities, 
						  final int positives,
						  final int gaps, 
						  final int length, 
						  final String subLocation, 
						  final double pI) {
		super();
		this.description = description;
		this.sequence = sequence;
		this.score = score;
		this.eValue = eValue;
		this.identities = identities;
		this.positives = positives;
		this.gaps = gaps;
		this.length = length;
		this.subLocation = subLocation;
		this.pI = pI;
	}
	
	//FIXME
	public BlastAligment( final String description, 
			  final double score, 
			  final double eValue, 
			  final int identities, 
			  final int positives,
			  final int gaps, 
			  final int length ){
		super();
		this.description = description;
//		this.sequence = sequence;
		this.score = score;
		this.eValue = eValue;
		this.identities = identities;
		this.positives = positives;
		this.gaps = gaps;
		this.length = length;
//		this.subLocation = subLocation;
//		this.pI = pI;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public AminoAcidSequence getSequence() {
		return sequence;
	}
	
	public void setSequence(AminoAcidSequence sequence) {
		this.sequence = sequence;
	}
	
	public double getScore() {
		return score;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
	public double geteValue() {
		return eValue;
	}
	
	public void seteValue(double eValue) {
		this.eValue = eValue;
	}
	
	public int getIdentities() {
		return identities;
	}
	
	public void setIdentities(int identities) {
		this.identities = identities;
	}
	
	public int getPositives() {
		return positives;
	}
	
	public void setPositives(int positives) {
		this.positives = positives;
	}
	
	public int getGaps() {
		return gaps;
	}
	
	public void setGaps(int gaps) {
		this.gaps = gaps;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public String getSubLocation() {
		return subLocation;
	}
	
	public void setSubLocation(String subLocation) {
		this.subLocation = subLocation;
	}
	
	public double getpI() {
		return pI;
	}
	
	public void setpI(double pI) {
		this.pI = pI;
	}	
}
