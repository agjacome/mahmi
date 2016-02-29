package es.uvigo.ei.sing.mahmi.browser.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@XmlRootElement(name = "blastAligment") @XmlAccessorType(XmlAccessType.FIELD)
public class BlastAligment {
	private String description;
	private String sequence;
	private double score;
	private double eValue;
	private int identities;
	private int positives;
	private int gaps;
	private int length;
	private String subLocation;
	private double pI;
	private String query;
	private String comparation;
	private String subject;
	private String path;
	
	@VisibleForJAXB public BlastAligment(){
		super();
		this.description = "";
		this.sequence    = "";
		this.score		 = 0.0;
		this.eValue		 = 0.0;
		this.identities  = 0;
		this.positives   = 0;
		this.gaps  		 = 0;
		this.length	  	 = 0;
		this.subLocation = "Unknown";
		this.pI			 = 0.0;
		this.query       = "";
		this.comparation = "";
		this.subject     = "";
		this.path 		 = "";
	}
	
	public BlastAligment (final BlastAligment aligment){
		this.description = aligment.getDescription();
		this.sequence    = aligment.getSequence();
		this.score       = aligment.getScore();
		this.eValue      = aligment.geteValue();
		this.identities  = aligment.getIdentities();
		this.positives   = aligment.getPositives();
		this.gaps 		 = aligment.getGaps();
		this.length      = aligment.getLength();
		this.subLocation = aligment.getSubLocation();
		this.pI          = aligment.getpI();
		this.query       = aligment.getQuery();
		this.comparation = aligment.getComparation();
		this.subject     = aligment.getSubject();
		this.path 		 = aligment.getPath();
	}
	
	public BlastAligment( final String description, 
						  final String sequence,
						  final double score, 
						  final double eValue, 
						  final int identities, 
						  final int positives,
						  final int gaps, 
						  final int length, 
						  final String subLocation, 
						  final double pI,
						  final String query,
						  final String comparation,
						  final String subject,
						  final String path ){
		super();
		this.description = description;
		this.sequence    = sequence;
		this.score       = score;
		this.eValue      = eValue;
		this.identities  = identities;
		this.positives   = positives;
		this.gaps        = gaps;
		this.length      = length;
		this.subLocation = subLocation;
		this.pI          = pI;
		this.query       = query;
		this.comparation = comparation;
		this.subject     = subject;
		this.path        = path;
	}
	
	public BlastAligment( final String description, 
			  final double score, 
			  final double eValue, 
			  final int identities, 
			  final int positives,
			  final int gaps, 
			  final int length,
			  final String query,
			  final String comparation,
			  final String subject,
			  final String path ){
		super();
		this.description = description;
		this.sequence    = "";
		this.score       = score;
		this.eValue      = eValue;
		this.identities  = identities;
		this.positives   = positives;
		this.gaps        = gaps;
		this.length      = length;
		this.subLocation = "Unknown";
		this.pI          = 0.0;
		this.query       = query;
		this.comparation = comparation;
		this.subject     = subject;
		this.path        = path;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getSequence() {
		return sequence;
	}
	
	public void setSequence(String sequence) {
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

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getComparation() {
		return comparation;
	}

	public void setComparation(String comparation) {
		this.comparation = comparation;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}	
	
}
