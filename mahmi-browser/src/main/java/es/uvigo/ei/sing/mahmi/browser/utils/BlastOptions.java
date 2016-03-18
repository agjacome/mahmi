package es.uvigo.ei.sing.mahmi.browser.utils;

public class BlastOptions {
	private final int num_alignments;
    private final double evalue;
    private final int blast_threshold;
    private final int window_size;
    private final int word_size;
    private final double best_hit_overhang;
    private final double best_hit_score_edge;
    private final int gapextend;
    private final int gapopen;
    private final boolean ungapped;
    
    public BlastOptions(){
    	this.num_alignments = 250;
    	this.evalue = 10.0;
    	this.blast_threshold = 11;
    	this.window_size = 3;
    	this.word_size = 40;
    	this.best_hit_overhang = 0.1;
    	this.best_hit_score_edge = 0.1;
    	this.gapextend = 1;
    	this.gapopen = 11;
    	this.ungapped = true;
    }
    
    public BlastOptions( final int num_alignments,
    					 final double evalue,
    					 final int blast_threshold,
    					 final int window_size,
    					 final int word_size,
    					 final double best_hit_overhang,
    					 final double best_hit_score_edge,
    					 final int gapextend,
    					 final int gapopen,
    					 final boolean ungapped ){
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
