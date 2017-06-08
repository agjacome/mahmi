package es.uvigo.ei.sing.mahmi.browser.runners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.text.MessageFormat;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * {@linkplain BlastDbCmdRunner} is the blastdbcmd binaries executor.
 *
 * @author Aitor Blanco-Miguez
 *
 */
@Slf4j
@AllArgsConstructor(staticName = "blastdbcmd")
final public class BlastDbCmdRunner implements Runnable {
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
			log.error("BlastDbCmd error", ie);
			throw new RuntimeException(ie);
		}
	}

	/**
	 * Builds the blastdbcmd execution process
	 * 
	 * @return the {@link ProcessBuilder} of blastdbcmd execution
	 */
	private ProcessBuilder buildProcess() {
		return new ProcessBuilder("blastdbcmd", "-db", this.db, "-dbtype", "prot", "-entry_batch",
				this.input.toString()).redirectOutput(this.output.toFile());
	}

	/**
	 * Checks if BlastDbCMD execution exited anormally
	 * 
	 * @param value
	 *            The exit value
	 * @throws IOException
	 */
	private void checkExitValue(final int value) throws IOException {
		if (value != 0)
			throw new IOException(MessageFormat.format("BlastDbCmd exited anormally ({})", value));
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
