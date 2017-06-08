package es.uvigo.ei.sing.mahmi.browser.runners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import es.uvigo.ei.sing.mahmi.browser.utils.BlastOptions;
import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * {@linkplain BlastpRunner} is the blastp binaries executor.
 *
 * @author Aitor Blanco-Miguez
 * 
 * @see BlastOptions
 *
 */
@Slf4j
@AllArgsConstructor(staticName = "blastp")
final public class BlastpRunner implements Runnable {

	/**
	 * The input sequence path
	 */
	private final Path input;

	/**
	 * The alignments output path
	 */
	private final Path output;

	/**
	 * The Blast database against which to align
	 */
	private final String db;

	/**
	 * The BlastP execution options
	 */
	private final BlastOptions blastOptions;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			val process = buildProcess().start();
			redirectErrorToLogs(process);
			checkExitValue(process.waitFor());
		} catch (final IOException | InterruptedException ie) {
			log.error("BlastP error", ie);
			throw new RuntimeException(ie);
		}
	}

	/**
	 * Builds the blastp execution process
	 * 
	 * @return the {@link ProcessBuilder} of blastp execution
	 */
	private ProcessBuilder buildProcess() {
		final List<String> blastWithOptions = getBlastWithOptions();
		return new ProcessBuilder(blastWithOptions.toArray(new String[blastWithOptions.size()]))
				.redirectOutput(this.output.toFile());
	}

	/**
	 * Constructs the BlastP command with the {@link BlastOptions}
	 * 
	 * @return The {@code List} of constructed BlastP options
	 */
	private List<String> getBlastWithOptions() {
		final List<String> blast = new ArrayList<String>();
		blast.add("blastp");
		blast.add("-query");
		blast.add(this.input.toString());
		blast.add("-db");
		blast.add(this.db);
		blast.add("-evalue");
		blast.add(String.valueOf(this.blastOptions.getEvalue()));
		blast.add("-word_size");
		blast.add(String.valueOf(this.blastOptions.getWord_size()));
		blast.add("-gapopen");
		blast.add(String.valueOf(this.blastOptions.getGapopen()));
		blast.add("-gapextend");
		blast.add(String.valueOf(this.blastOptions.getGapextend()));
		blast.add("-threshold");
		blast.add(String.valueOf(this.blastOptions.getBlast_threshold()));
		blast.add("-num_alignments");
		blast.add(String.valueOf(this.blastOptions.getNum_alignments()));
		blast.add("-window_size");
		blast.add(String.valueOf(this.blastOptions.getWindow_size()));

		if (this.blastOptions.isUngapped()) {
			blast.add("-comp_based_stats");
			blast.add("-ungapped");
		}
		if (this.blastOptions.getBest_hit_overhang() > 0) {
			blast.add("-best_hit_overhang");
			blast.add(String.valueOf(this.blastOptions.getBest_hit_overhang()));
		}
		if (this.blastOptions.getBest_hit_score_edge() > 0) {
			blast.add("-best_hit_score_edge");
			blast.add(String.valueOf(this.blastOptions.getBest_hit_score_edge()));
		}

		return blast;
	}

	/**
	 * Checks if BlastP execution exited anormally
	 * 
	 * @param value
	 *            The exit value
	 * @throws IOException
	 */
	private void checkExitValue(final int value) throws IOException {
		if (value != 0)
			throw new IOException(MessageFormat.format("BlastP exited anormally ({})", value));
	}

	/**
	 * Redirects errors to log
	 * 
	 * @param proc
	 *            The process to log errors
	 * @throws IOException
	 */
	private void redirectErrorToLogs(final Process proc) throws IOException {
		try (val stderr = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
			stderr.lines().forEach(log::info);
		}
	}

}
