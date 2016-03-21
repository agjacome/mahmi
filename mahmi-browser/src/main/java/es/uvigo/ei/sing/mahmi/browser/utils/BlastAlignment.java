package es.uvigo.ei.sing.mahmi.browser.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@XmlRootElement(name = "blastAlignment") @XmlAccessorType(XmlAccessType.FIELD)
public class BlastAlignment {
	private String description;
	private String sequence;
	private double score;
	private double eValue;
	private int    identities;
	private int    positives;
	private int    gaps;
	private int    length;
	private double mW;
	private double pI;
	private String query;
	private String comparation;
	private String subject;
	private String path;
	private int    queryStart;
	private int    queryEnd;
	private int    subjectStart;
	private int    subjectEnd;
	
	@VisibleForJAXB public BlastAlignment(){
		super();
		this.description  = "";
		this.sequence     = "";
		this.score		  = 0.0;
		this.eValue		  = 0.0;
		this.identities   = 0;
		this.positives    = 0;
		this.gaps  		  = 0;
		this.length	  	  = 0;
		this.mW           = 0.0;
		this.pI			  = 0.0;
		this.query        = "";
		this.comparation  = "";
		this.subject      = "";
		this.path 		  = "";
		this.queryStart   = 0;
		this.queryEnd     = 0;
		this.subjectStart = 0;
		this.subjectEnd   = 0;
	}
	
	public BlastAlignment (final BlastAlignment alignment){
		this.description  = alignment.getDescription();
		this.sequence     = alignment.getSequence();
		this.score        = alignment.getScore();
		this.eValue       = alignment.geteValue();
		this.identities   = alignment.getIdentities();
		this.positives    = alignment.getPositives();
		this.gaps 		  = alignment.getGaps();
		this.length       = alignment.getLength();
		this.mW           = alignment.getmW();
		this.pI           = alignment.getpI();
		this.query        = alignment.getQuery();
		this.comparation  = alignment.getComparation();
		this.subject      = alignment.getSubject();
		this.path 		  = alignment.getPath();
		this.queryStart   = alignment.getQueryStart();
		this.queryEnd     = alignment.getQueryEnd();
		this.subjectStart = alignment.getSubjectStart();
		this.subjectEnd   = alignment.getSubjectEnd();
	}
	
	public BlastAlignment( final String description, 
						   final String sequence,
						   final double score, 
						   final double eValue, 
						   final int identities, 
						   final int positives,
						   final int gaps, 
						   final int length, 
						   final double mW, 
						   final double pI,
						   final String query,
						   final String comparation,
						   final String subject,
						   final String path,
						   final int queryStart,
						   final int queryEnd,
						   final int subjectStart,
						   final int subjectEnd ){
		super();
		this.description  = description;
		this.sequence     = sequence;
		this.score        = score;
		this.eValue       = eValue;
		this.identities   = identities;
		this.positives    = positives;
		this.gaps         = gaps;
		this.length       = length;
		this.mW			  = mW;
		this.pI           = pI;
		this.query        = query;
		this.comparation  = comparation;
		this.subject      = subject;
		this.path         = path;
		this.queryStart   = queryStart;
		this.queryEnd     = queryEnd;
		this.subjectStart = subjectStart;
		this.subjectEnd   = subjectEnd;
	}
	
	public BlastAlignment( final String description, 
						   final double score, 
						   final double eValue, 
						   final int identities, 
						   final int positives,
						   final int gaps, 
						   final int length,
						   final String query,
						   final String comparation,
						   final String subject,
						   final String path,
						   final int queryStart,
						   final int queryEnd,
						   final int subjectStart,
						   final int subjectEnd ){
		super();
		this.description  = description;
		this.sequence     = "";
		this.score        = score;
		this.eValue       = eValue;
		this.identities   = identities;
		this.positives    = positives;
		this.gaps         = gaps;
		this.length       = length;
		this.mW    		  = 0.0;
		this.pI           = 0.0;
		this.query        = query;
		this.comparation  = comparation;
		this.subject      = subject;
		this.path         = path;
		this.queryStart   = queryStart;
		this.queryEnd     = queryEnd;
		this.subjectStart = subjectStart;
		this.subjectEnd   = subjectEnd;
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
	
	public double getmW() {
		return mW;
	}

	public void setmW(double mW) {
		this.mW = mW;
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

	public int getQueryStart() {
		return queryStart;
	}

	public void setQueryStart(int queryStart) {
		this.queryStart = queryStart;
	}

	public int getQueryEnd() {
		return queryEnd;
	}

	public void setQueryEnd(int queryEnd) {
		this.queryEnd = queryEnd;
	}

	public int getSubjectStart() {
		return subjectStart;
	}

	public void setSubjectStart(int subjectStart) {
		this.subjectStart = subjectStart;
	}

	public int getSubjectEnd() {
		return subjectEnd;
	}

	public void setSubjectEnd(int subjectEnd) {
		this.subjectEnd = subjectEnd;
	}	
}
