package es.uvigo.ei.sing.mahmi.psort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * {@linkplain PSortRunner} is the PSortB binaries executor.
 *
 * @author Aitor Blanco-Miguez
 * 
 * @see PSortGramMode
 *
 */
@Slf4j
@AllArgsConstructor(staticName = "psort")
final class PSortRunner implements Runnable {

	/**
	 * The PSortB gram mode
	 */
	private final PSortGramMode mode;
	
	/**
	 * The sequences input file 
	 */
	private final Path input;
	
	/**
	 * The PSortB output file
	 */
	private final Path output;

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
			log.error("PSORT error", ie);
			throw new RuntimeException(ie);
		}
	}

	/**
	 * Builds the PSortB execution process
	 * 
	 * @return the {@link ProcessBuilder} of PSortB execution
	 */
	private ProcessBuilder buildProcess() {
		return new ProcessBuilder("psort", mode.getModeFlag(), "-o=terse", "--verbose",
				input.toString()).redirectOutput(output.toFile());
	}

	/**
	 * Checks if PSortB execution exited anormally
	 * 
	 * @param value
	 *            The exit value
	 * @throws IOException
	 */
	private void checkExitValue(final int value) throws IOException {
		if (value != 0)
			throw new IOException(String.format("PSORT exited anormally (%d)", value));
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
