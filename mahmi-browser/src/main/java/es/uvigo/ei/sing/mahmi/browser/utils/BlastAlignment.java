package es.uvigo.ei.sing.mahmi.browser.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

/**
 * {@linkplain BlastAlignment} is a class that represents a Blast alignment
 * 
 * @author Aitor Blanco-Miguez
 *
 */
@XmlRootElement(name = "blastAlignment") @XmlAccessorType(XmlAccessType.FIELD)
public class BlastAlignment {
	/**
	 * The brief description of the alignment
	 */
	private String description;
	
	/**
	 * The sequence to align
	 */
	private String sequence;
	
	/**
	 * The sum of the scoring matrix values in the segment pair being displayed
	 */
	private double score;
	
	/**
	 * The number of times one might Expect to see such a match merely by chance
	 */
	private double eValue;
	
	/**
	 * The number and fraction of total residues in the HSP which are identical
	 */
	private int identities;
	
	/**
	 * The number and fraction of residues for which the alignment scores have 
	 * positive values
	 */
	private int    positives;
	
	/**
	 * The number of gaps in the alignment
	 */
	private int gaps;
	
	/**
	 * The length of the alignment
	 */
	private int length;
	
	/**
	 * The molecular weight of the sequence
	 */
	private double mW;
	
	/**
	 * The isoelectric point of the sequence
	 */
	private double pI;
	
	/**
	 * The query sequence
	 */
	private String query;
	
	/**
	 * The comparison between query and subject
	 */
	private String comparison;
	
	/**
	 * The subject sequence
	 */
	private String subject;
	
	/**
	 * The temporal folder of the alignment
	 */
	private String path;
	
	/**
	 * The query amino acid from which the alignment begins
	 */
	private int queryStart;
	
	/**
	 * The query amino acid from which the alignment ends
	 */
	private int queryEnd;
	
	/**
	 * The subject amino acid from which the alignment begins
	 */
	private int subjectStart;
	
	/**
	 * The subject amino acid from which the alignment ends
	 */
	private int subjectEnd;
	
	
	/**
	 * The {@linkplain BlastAlignment} empty constructor
	 */
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
		this.comparison  = "";
		this.subject      = "";
		this.path 		  = "";
		this.queryStart   = 0;
		this.queryEnd     = 0;
		this.subjectStart = 0;
		this.subjectEnd   = 0;
	}
	
	/**
	 * The {@linkplain BlastAlignment} parameterized constructor using a {@linkplain BlastAlignment}
	 * instance
	 * 
	 * @param alignment The {@linkplain BlastAlignment} to clone
	 */
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
		this.comparison   = alignment.getComparison();
		this.subject      = alignment.getSubject();
		this.path 		  = alignment.getPath();
		this.queryStart   = alignment.getQueryStart();
		this.queryEnd     = alignment.getQueryEnd();
		this.subjectStart = alignment.getSubjectStart();
		this.subjectEnd   = alignment.getSubjectEnd();
	}
	
	/**
	 * The {@linkplain BlastAlignment} parameterized constructor with sequence
	 * 
	 * @param description The brief description of the alignment
	 * @param sequence The sequence to align
	 * @param score The sum of the scoring matrix values in the segment pair being displayed
	 * @param eValue The number of times one might Expect to see such a match merely by chance
	 * @param identities The number and fraction of total residues in the HSP which are identical
	 * @param positives The number and fraction of residues for which the  alignment scores have 
	 * positive values
	 * @param gaps The number of gaps in the alignment
	 * @param length The length of the alignment
	 * @param mW The molecular weight of the sequence
	 * @param pI The isoelectric point of the sequence
	 * @param query The query sequence
	 * @param comparison The comparison between query and subject
	 * @param subject The subject sequence
	 * @param path The temporal folder of the alignment
	 * @param queryStart The query amino acid from which the alignment begins
	 * @param queryEnd The query The amino acid from which the alignment ends
	 * @param subjectStart The subject amino acid from which the alignment begins
	 * @param subjectEnd The subject The amino acid from which the alignment ends
	 */
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
						   final String comparison,
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
		this.comparison   = comparison;
		this.subject      = subject;
		this.path         = path;
		this.queryStart   = queryStart;
		this.queryEnd     = queryEnd;
		this.subjectStart = subjectStart;
		this.subjectEnd   = subjectEnd;
	}
	
	/**
	 * The {@linkplain BlastAlignment} parameterized constructor without sequence
	 * 
	 * @param description The brief description of the alignment
	 * @param score The sum of the scoring matrix values in the segment pair being displayed
	 * @param eValue The number of times one might Expect to see such a match merely by chance
	 * @param identities The number and fraction of total residues in the HSP which are identical
	 * @param positives The number and fraction of residues for which the  alignment scores have 
	 * positive values
	 * @param gaps The number of gaps in the alignment
	 * @param length The length of the alignment
	 * @param query The query sequence
	 * @param comparison The comparison between query and subject
	 * @param subject The subject sequence
	 * @param path The temporal folder of the alignment
	 * @param queryStart The query amino acid from which the alignment begins
	 * @param queryEnd The query The amino acid from which the alignment ends
	 * @param subjectStart The subject amino acid from which the alignment begins
	 * @param subjectEnd The subject The amino acid from which the alignment ends
	 */
	public BlastAlignment( final String description, 
						   final double score, 
						   final double eValue, 
						   final int identities, 
						   final int positives,
						   final int gaps, 
						   final int length,
						   final String query,
						   final String comparison,
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
		this.comparison   = comparison;
		this.subject      = subject;
		this.path         = path;
		this.queryStart   = queryStart;
		this.queryEnd     = queryEnd;
		this.subjectStart = subjectStart;
		this.subjectEnd   = subjectEnd;
	}
	
	/**
	 * The {@code description} getter function
	 * 
	 * @return the description attribute.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * The {@code description} setter function
	 * 
	 * @param description The brief description of the alignment
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * The {@code sequence} getter function
	 * 
	 * @return the alignment sequence
	 */
	public String getSequence() {
		return sequence;
	}
	
	/**
	 * The {@code sequence} setter function
	 * 
	 * @param sequence The alignment sequence
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
	/**
	 * The {@code score} getter function
	 * 
	 * @return the sum of the scoring matrix values in the segment pair being displayed
	 */
	public double getScore() {
		return score;
	}
	
	/**
	 * The {@code score} setter function
	 * 
	 * @param score The sum of the scoring matrix values in the segment pair being displayed
	 */
	public void setScore(double score) {
		this.score = score;
	}
	
	/**
	 * The {@code eValue} getter function
	 * 
	 * @return the number of times one might Expect to see such a match merely by chance
	 */
	public double geteValue() {
		return eValue;
	}
	
	/**
	 * The {@code eValue} setter function
	 * 
	 * @param eValue The number of times one might Expect to see such a match merely by chance
	 */
	public void seteValue(double eValue) {
		this.eValue = eValue;
	}
	
	/**
	 * The {@code identities} getter function
	 * 
	 * @return the number and fraction of total residues in the HSP which are identical
	 */
	public int getIdentities() {
		return identities;
	}
	
	/**
	 * The {@code identities} setter function
	 * 
	 * @param identities The number and fraction of total residues in the HSP which are identical 
	 */
	public void setIdentities(int identities) {
		this.identities = identities;
	}
	
	/**
	 * The {@code positives} getter function
	 * 
	 * @return the number and fraction of residues for which the  alignment scores have positive 
	 * values
	 */
	public int getPositives() {
		return positives;
	}
	
	/**
	 * The {@code positives} setter function
	 * 
	 * @param positives The he number and fraction of residues for which the  alignment scores have 
	 * positive values
	 */
	public void setPositives(int positives) {
		this.positives = positives;
	}
	
	/**
	 * The {@code gaps} getter function
	 * 
	 * @return the number of gaps of the alignment
	 */
	public int getGaps() {
		return gaps;
	}
	
	/**
	 * The {@code gaps} setter function
	 * 
	 * @param gaps The number of gaps of the alignment
	 */
	public void setGaps(int gaps) {
		this.gaps = gaps;
	}
	
	/**
	 * The {@code length} getter function
	 * 
	 * @return the length of the alignment
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * The {@code length} setter function
	 * 
	 * @param length The length of the alignment
	 */
	public void setLength(int length) {
		this.length = length;
	}
	
	/**
	 * The {@code mW} getter function
	 * 
	 * @return the molecular weight of the sequence
	 */
	public double getmW() {
		return mW;
	}

	/**
	 * The {@code mW} setter function
	 * 
	 * @param mW The molecular weight of the sequence
	 */
	public void setmW(double mW) {
		this.mW = mW;
	}

	/**
	 * The {@code pI} getter function
	 * 
	 * @return the isoelectric point of the sequence
	 */
	public double getpI() {
		return pI;
	}
	
	/**
	 * The {@code pI} setter function
	 * 
	 * @param pI The isoelectric point of the sequence
	 */
	public void setpI(double pI) {
		this.pI = pI;
	}

	/**
	 * The {@code query} getter function
	 * 
	 * @return the query sequence
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * The {@code query} setter function
	 * 
	 * @param query The query sequence
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * The {@code comparison} getter function
	 * 
	 * @return the comparison between query and subject
	 */
	public String getComparison() {
		return comparison;
	}

	/**
	 * The {@code comparison} setter function
	 * 
	 * @param comparation The comparison between query and subject
	 */
	public void setComparison(String comparison) {
		this.comparison = comparison;
	}

	/**
	 * The {@code subject} getter function
	 * 
	 * @return the subject sequence
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * The {@code subject} setter function
	 * 
	 * @param subject The subject sequence
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * The {@code path} getter function
	 * 
	 * @return the temporal folder of the alignment
	 */
	public String getPath() {
		return path;
	}

	/**
	 * The {@code path} setter function
	 * 
	 * @param path The temporal folder of the alignment
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * The {@code queryStart} getter function
	 * 
	 * @return the query amino acid from which the alignment ends
	 */
	public int getQueryStart() {
		return queryStart;
	}

	/**
	 * The {@code queryStart} setter function
	 * 
	 * @param queryStart The query amino acid from which the alignment ends
	 */
	public void setQueryStart(int queryStart) {
		this.queryStart = queryStart;
	}

	/**
	 * The {@code queryEnd} getter function
	 * 
	 * @return the query amino acid from which the alignment ends
	 */
	public int getQueryEnd() {
		return queryEnd;
	}

	/**
	 * The {@code queryEnd} setter function
	 * 
	 * @param queryEnd The query amino acid from which the alignment ends
	 */
	public void setQueryEnd(int queryEnd) {
		this.queryEnd = queryEnd;
	}

	/**
	 * The {@code subjectStart} getter function
	 * 
	 * @return the subject amino acid from which the alignment starts
	 */
	public int getSubjectStart() {
		return subjectStart;
	}

	/**
	 * The {@code subjectStart} setter function
	 * 
	 * @param subjectStart The subject amino acid from which the alignment starts
	 */
	public void setSubjectStart(int subjectStart) {
		this.subjectStart = subjectStart;
	}

	/**
	 * The {@code subjectEnd} getter function 
	 * 
	 * @return the subject amino acid from which the alignment ends
	 */
	public int getSubjectEnd() {
		return subjectEnd;
	}

	/**
	 * The {@code subjectEnd} setter function
	 * 
	 * @param subjectEnd The subject amino acid from which the alignment ends
	 */
	public void setSubjectEnd(int subjectEnd) {
		this.subjectEnd = subjectEnd;
	}	
}
