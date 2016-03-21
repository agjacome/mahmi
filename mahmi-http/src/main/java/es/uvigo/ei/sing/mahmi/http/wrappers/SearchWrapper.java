package es.uvigo.ei.sing.mahmi.http.wrappers;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@XmlRootElement(name = "browserSearch") @XmlAccessorType(XmlAccessType.FIELD)
public final class SearchWrapper {	

    private final String       sequence;
    @XmlElementWrapper(name = "databases")
    @XmlElements({@XmlElement(name = "database", type = String.class)})
    private final List<String> databases;
    private final int          threshold;   
    @XmlElementWrapper(name = "bioactivities")
    @XmlElements({@XmlElement(name = "bioactivity", type = String.class)})
    private final List<String> bioactivity;    
    private final String       path;    
    private final int     	   num_alignments;
    private final double       evalue;
    private final int          blast_threshold;
    private final int          window_size;
    private final int          word_size;
    private final double       best_hit_overhang;
    private final double       best_hit_score_edge;
    private final int          gapextend;
    private final int     	   gapopen;
    private final boolean 	   ungapped;    
    
    
    
    @VisibleForJAXB
    public SearchWrapper() {
    	this.sequence			 = "";
    	this.databases			 = new LinkedList<String>();
    	this.threshold			 = 60;
    	this.bioactivity		 = new LinkedList<String>();
    	this.path				 = "";
    	this.num_alignments		 = 250;
    	this.evalue				 = 10.0;
    	this.blast_threshold	 = 11;
    	this.window_size 		 = 3;
    	this.word_size 			 = 40;
    	this.best_hit_overhang   = 0.1;
    	this.best_hit_score_edge = 0.1;
    	this.gapextend 			 = 1;
    	this.gapopen 			 = 11;
    	this.ungapped 			 = true;
    }

	private SearchWrapper(String sequence, List<String> databases,
			int threshold, List<String> bioactivity, String path,
			int num_alignments, double evalue, int blast_threshold,
			int window_size, int word_size, double best_hit_overhang,
			double best_hit_score_edge, int gapextend, int gapopen,
			boolean ungapped) {
		super();
		this.sequence = sequence;
		this.databases = databases;
		this.threshold = threshold;
		this.bioactivity = bioactivity;
		this.path = path;
		this.num_alignments = num_alignments;
		this.evalue = evalue;
		this.blast_threshold = blast_threshold;
		this.window_size = window_size;
		this.word_size = word_size;
		this.best_hit_overhang = best_hit_overhang;
		this.best_hit_score_edge = best_hit_score_edge;
		this.gapextend = gapextend;
		this.gapopen = gapopen;
		this.ungapped = ungapped;
	}
	
	public static SearchWrapper wrap ( String sequence, 
									   List<String> databases,
									   int threshold,
									   List<String> bioactivity, 
									   String path,
									   int num_alignments, 
									   double evalue, 
									   int blast_threshold,
									   int window_size, 
									   int word_size, 
									   double best_hit_overhang,
									   double best_hit_score_edge, 
									   int gapextend, 
									   int gapopen,
									   boolean ungapped ){
		return new SearchWrapper(sequence, databases, threshold, bioactivity, path, num_alignments, 
								 evalue, blast_threshold, window_size, word_size, best_hit_overhang, 
								 best_hit_score_edge, gapextend, gapopen, ungapped);		
	}

	public String getSequence() {
		return sequence;
	}

	public List<String> getDatabases() {
		return databases;
	}

	public int getThreshold() {
		return threshold;
	}

	public List<String> getBioactivity() {
		return bioactivity;
	}

	public String getPath() {
		return path;
	}

	public int getNum_alignments() {
		return num_alignments;
	}

	public double getEvalue() {
		return evalue;
	}

	public int getBlast_threshold() {
		return blast_threshold;
	}

	public int getWindow_size() {
		return window_size;
	}

	public int getWord_size() {
		return word_size;
	}

	public double getBest_hit_overhang() {
		return best_hit_overhang;
	}

	public double getBest_hit_score_edge() {
		return best_hit_score_edge;
	}

	public int getGapextend() {
		return gapextend;
	}

	public int getGapopen() {
		return gapopen;
	}

	public boolean isUngapped() {
		return ungapped;
	}
}
