package es.uvigo.ei.sing.mahmi.browser.utils;

/**
 * {@linkplain BlastOptions} is a class that represents Blast program options
 * 
 * @author Aitor Blanco-Miguez
 *
 */
public class BlastOptions {
	/**
	 * The alignments shown
	 */
	private final int num_alignments;

	/**
	 * The expect value (E) for saving hits
	 */
	private final double evalue;

	/**
	 * The minimum score to add a word to the BLAST lookup table
	 */
	private final int blast_threshold;

	/**
	 * The multiple hits window size
	 */
	private final int window_size;

	/**
	 * The word size of initial match
	 */
	private final int word_size;

	/**
	 * The Best Hit algorithm overhang value
	 */
	private final double best_hit_overhang;

	/**
	 * The Best Hit algorithm score edge value
	 */
	private final double best_hit_score_edge;

	/**
	 * The cost to extend a gap
	 */
	private final int gapextend;

	/**
	 * The cost to open a gap
	 */
	private final int gapopen;

	/**
	 * Whether to perform an ungapped alignment
	 */
	private final boolean ungapped;

	/**
	 * The {@linkplain BlastOptions} default constructor
	 */
	public BlastOptions() {
		this.num_alignments 	 = 250;
		this.evalue 		 	 = 10.0;
		this.blast_threshold     = 11;
		this.window_size 	     = 3;
		this.word_size 			 = 40;
		this.best_hit_overhang   = 0.1;
		this.best_hit_score_edge = 0.1;
		this.gapextend		     = 1;
		this.gapopen 			 = 11;
		this.ungapped 			 = true;
	}

	/**
	 * The {@linkplain BlastOptions} parameterized constructor
	 * 
	 * @param num_alignments
	 *            The alignments shown
	 * @param evalue
	 *            The expect value (E) for saving hits
	 * @param blast_threshold
	 *            The minimum score to add a word to the BLAST lookup table
	 * @param window_size
	 *            The multiple hits window size
	 * @param word_size
	 *            The word size of initial match
	 * @param best_hit_overhang
	 *            The Best Hit algorithm overhang value
	 * @param best_hit_score_edge
	 *            The Best Hit algorithm score edge value
	 * @param gapextend
	 *            The cost to extend a gap
	 * @param gapopen
	 *            The cost to open a gap
	 * @param ungapped
	 *            Whether to perform an ungapped alignment
	 */
	public BlastOptions(final int num_alignments,
						final double evalue,
						final int blast_threshold,
						final int window_size,
						final int word_size,
						final double best_hit_overhang,
						final double best_hit_score_edge,
						final int gapextend,
						final int gapopen,
						final boolean ungapped) {
		this.num_alignments		 = num_alignments;
		this.evalue		   		 = evalue;
		this.blast_threshold	 = blast_threshold;
		this.window_size		 = window_size;
		this.word_size 			 = word_size;
		this.best_hit_overhang   = best_hit_overhang;
		this.best_hit_score_edge = best_hit_score_edge;
		this.gapextend 			 = gapextend;
		this.gapopen 			 = gapopen;
		this.ungapped 			 = ungapped;
	}

	/**
	 * The {@code num_alignments} getter function
	 * 
	 * @return the he alignments shown
	 */
	public int getNum_alignments() {
		return num_alignments;
	}

	/**
	 * The {@code evalue} getter function
	 * 
	 * @return the expect value (E) for saving hits
	 */
	public double getEvalue() {
		return evalue;
	}

	/**
	 * The {@code blast_threshold} getter function
	 * 
	 * @return the minimum score to add a word to the BLAST lookup table
	 */
	public int getBlast_threshold() {
		return blast_threshold;
	}

	/**
	 * The {@code window_size} getter function
	 * 
	 * @return the multiple hits window size
	 */
	public int getWindow_size() {
		return window_size;
	}

	/**
	 * The {@code word_size} getter function
	 * 
	 * @return the word size of initial match
	 */
	public int getWord_size() {
		return word_size;
	}

	/**
	 * The {@code best_hit_overhang} getter function
	 * 
	 * @return the Best Hit algorithm overhang value
	 */
	public double getBest_hit_overhang() {
		return best_hit_overhang;
	}

	/**
	 * The {@code best_hit_score_edge} getter function
	 * 
	 * @return the Best Hit algorithm score edge value
	 */
	public double getBest_hit_score_edge() {
		return best_hit_score_edge;
	}

	/**
	 * The {@code gapextend} getter function
	 * 
	 * @return the cost to extend a gap
	 */
	public int getGapextend() {
		return gapextend;
	}

	/**
	 * The {@code gapopen} getter function
	 * 
	 * @return the cost to open a gap
	 */
	public int getGapopen() {
		return gapopen;
	}

	/**
	 * The {@code ungapped} getter function
	 * 
	 * @return whether to perform an ungapped alignment
	 */
	public boolean isUngapped() {
		return ungapped;
	}
}
