package es.uvigo.ei.sing.mahmi.psort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.val;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaReader;

import static es.uvigo.ei.sing.mahmi.psort.PSortRunner.psort;

/**
 * {@linkplain PSortFastaFilter} is a class that executes PSortB
 * 
 * @author Aitor Blanco-Miguez
 * @author Alberto Gutierrez-Jacome
 * 
 * @see Fasta
 * @see AminoAcidSequence
 * @see PSortGramMode
 * @see PSortFilterType
 *
 */
@AllArgsConstructor(staticName = "of")
public final class PSortFastaFilter {

	/**
	 * The PSortB gram mode
	 */
	private final PSortGramMode mode;

	/**
	 * The PSortB subcellular location filter
	 */
	private final EnumSet<PSortFilterType> filter;

	/**
	 * Runs PSortB over a sequence input file and filter by subcellular location
	 * 
	 * @param input
	 *            The sequences input file
	 * @return The filtered input sequences {@linkplain Fasta}
	 * @throws IOException
	 * 
	 * @see Fasta
	 * @see AminoAcidSequence
	 */
	public Fasta<AminoAcidSequence> filter(final Path input) throws IOException {
		val output = runPsort(input);
		val fasta = filterFasta(FastaReader.forAminoAcid().fromPath(input), output);

		Files.deleteIfExists(output);

		return fasta;
	}

	/**
	 * Runs PSortB and returns the ouput file
	 * 
	 * @param input
	 *            The sequences input file
	 * @return The PSortB output file
	 */
	private Path runPsort(final Path input) {
		val output = input.resolveSibling("psortb.out");
		psort(mode, input, output).run();
		return output;
	}

	/*
	 * FIXME: all filtered sequences and contents of outfile are kept in memory,
	 * if the original Fasta file is too big or if the filter does not delete
	 * enough sequences, this can cause memory-related problems.
	 */
	/**
	 * Filters a {@linkplain Fasta} sequences by subcellular location
	 * 
	 * @param fasta
	 *            The input {@linkplain Fasta} sequences
	 * @param output
	 *            The output PSortB file
	 * @return The filtered {@linkplain Fasta} sequences
	 * @throws IOException
	 * 
	 * @see Fasta
	 * @see AminoAcidSequence
	 */
	private Fasta<AminoAcidSequence> filterFasta(	final Fasta<AminoAcidSequence> fasta,
													final Path output) throws IOException {
		val pattern = PSortFilterType.compile(filter);

		val outLines = Files.lines(output).skip(1);
		val fastaIter = fasta.iterator();

		val filtered = new java.util.LinkedList<AminoAcidSequence>();

		outLines.forEach(line -> {
			val sequence = fastaIter.next();
			if (Pattern.matches(pattern, line))
				filtered.add(sequence);
		});

		return Fasta.of(filtered.iterator());
	}

}
