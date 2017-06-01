package es.uvigo.ei.sing.mahmi.browser;

import static es.uvigo.ei.sing.mahmi.browser.runners.BlastDbCmdRunner.blastdbcmd;
import static es.uvigo.ei.sing.mahmi.browser.runners.BlastpRunner.blastp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import es.uvigo.ei.sing.mahmi.browser.utils.BlastAlignment;
import es.uvigo.ei.sing.mahmi.browser.utils.BlastOptions;
import es.uvigo.ei.sing.mahmi.browser.utils.PeptideCalculator;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.utils.Constants;
import lombok.extern.slf4j.Slf4j;

/**
 * {@linkplain Browser} is a class that provides Blast database search engine
 * 
 * @author Aitor Blanco-Miguez
 * 
 * @see PeptideCalculator
 * @see AminoAcidSequence
 * @see BlastOptions
 * @see BlastAlignment
 *
 */
@Slf4j
public class Browser {
	
	/**
	 *  The {@link PeptideCalculator} that calculates the molecular mass and the isoelectric point
	 *  of the sequences
	 */
	final PeptideCalculator pc;
	
	/**
	 *  {@linkplain Browser} private default constructor
	 */
	private Browser() {
		pc = new PeptideCalculator();
	}

	/**
	 * Constructs a new instance of {@linkplain Browser}
	 * 
	 * @return a new instance of {@linkplain Browser}
	 */
	public static Browser browser() {
		return new Browser();
	}

	/**
	 * Performs a MAHMI web search against the reference/posible Blast database
	 * Search an {@link AminiAcidSequence} aligning against the MAHMI Blast database(s)
	 * 
	 * @param sequence The sequence to align
	 * @param databases The list of databases against witch to align. Accepted databases are 
	 * {@code refdb} and {@code posdb} 
	 * @param threshold The Blast execution potential bioactivity threshold
	 * @param bioactivity The list of potential bioactivities to search
	 * @param path The temporal alignment folder
	 * @param blastOptions The Blast execution options
	 * @return the list of {@link BlastAlignment}s
	 * 
	 * @see BlastOptions
	 * @see AminoAcidSequence
	 */
	public List<BlastAlignment> search(	final AminoAcidSequence sequence,
										final List<String> databases,
										final int threshold,
										final List<String> bioactivity,
										final Path path,
										final BlastOptions blastOptions) {
		createAuxiliaryFolders(path);
		final List<BlastAlignment> alignments = new LinkedList<BlastAlignment>();
		if (databases.contains("refdb"))
			blastOutParser(runBlastP(Paths.get(path.toString() + "/ref"), sequence,
					Constants.blastReferenceDB, blastOptions)).forEach(ba -> alignments.add(ba));
		if (databases.contains("posdb"))
			blastOutParser(runBlastP(Paths.get(path.toString() + "/pos"), sequence,
					Constants.blastPosibleDB, blastOptions)).forEach(ba -> alignments.add(ba));
		return filter(alignments, threshold, bioactivity, path);
	}

	
	/**
	 * Creates the auxiliary folders to perform the alignment
	 * 
	 * @param path The temporal folder in which create the auxiliary folders
	 */
	private void createAuxiliaryFolders(final Path path) {
		final File searchFile = new File(path.toString());
		searchFile.mkdir();
		final File refFile    = new File(path.toString() + "/ref");
		refFile.mkdir();
		final File posFile    = new File(path.toString() + "/pos");
		posFile.mkdir();
	}

	/**
	 * Filters a list of BlastAlignments for its potential bioactivity and its percentage 
	 * (threshold)
	 * 
	 * @param alignments The input list of the Blast alignments
	 * @param threshold The Blast execution potential bioactivity threshold to filter
	 * @param bioactivity The list of bioactivities to filter
	 * @param path The temporal folder of the alignment
	 * @return The list of filter Blast alignments
	 * 
	 * @see BlastAlignment
	 */
	private List<BlastAlignment> filter(final List<BlastAlignment> alignments,
										final int threshold,
										final List<String> bioactivity,
										final Path path) {
		final List<BlastAlignment> filterAlignments = new LinkedList<BlastAlignment>();
		getAlignmentsWithSequences(alignments, path).forEach(a -> {
			if (hasBioactivity(a, bioactivity) && passThreshold(a, threshold))
				filterAlignments.add(a);
		});
		return filterAlignments;
	}

	/**
	 * Converts a list of alignments without sequence into a list of alignments with sequence
	 * 
	 * @param alignments The input list of the Blast alignments 
	 * @param path The temporal folder of the alignment
	 * @return The list of Blast alignments with sequence
	 * 
	 * @see BlastAlignment
	 */
	private List<BlastAlignment> getAlignmentsWithSequences(final List<BlastAlignment> alignments,
															final Path path) {
		final List<BlastAlignment> alignmentsWithSequences = new LinkedList<BlastAlignment>();
		final List<BlastAlignment> references			   = new LinkedList<BlastAlignment>();
		final List<BlastAlignment> posibles 			   = new LinkedList<BlastAlignment>();

		alignments.forEach(a -> {
			if (a.getDescription().startsWith("MHM_POS"))
				posibles.add(a);
			else
				references.add(a);
		});
		if (!references.isEmpty())
			runBlastDbCmd(references, Paths.get(path.toString() + "/ref"),
					Constants.blastReferenceDB);
		if (!posibles.isEmpty())
			runBlastDbCmd(posibles, Paths.get(path.toString() + "/pos"), Constants.blastPosibleDB);
		final Map<String, AminoAcidSequence> map = getSequencesMap(path, !references.isEmpty(),
				!posibles.isEmpty());
		addSequences(references, alignmentsWithSequences, map);
		addSequences(posibles, alignmentsWithSequences, map);
		return alignmentsWithSequences;
	}

	/**
	 * Adds to the second list the alignments of the first list with the sequences of the map, 
	 * calculating too the molecular weight and isoelectric point of the sequences
	 * 
	 * @param alignments The list of Blast alignments to add
	 * @param alignmentsWithSequence The list to add the Blast alignments. 
	 * @param map The map contains the id and the sequences of the alignments.
	 * 
	 * @see BlastAlignment
	 * @see AminoAcidSequence
	 */
	private void addSequences(	final List<BlastAlignment> alignments,
								final List<BlastAlignment> alignmentsWithSequence,
								final Map<String, AminoAcidSequence> map) {
		alignments.forEach(a -> {
			a.setSequence(map.get(a.getDescription()).asString());
			alignmentsWithSequence.add(calculateMWandPI(a));
		});
	}

	/**
	 * Calculates the isoelectric point and molecular weight of the input alignment
	 * 
	 * @param alignment The alignment without isoelectric point and molecular weight
	 * @return The alignment with isoelectric point and molecular weight
	 * 
	 * @see BlastAlignment
	 */
	private BlastAlignment calculateMWandPI(final BlastAlignment alignment) {
		alignment.setpI(this.pc.calculatePI(alignment.getSequence()));
		alignment.setmW(this.pc.calculateMW(alignment.getSequence()));
		return alignment;
	}

	/**
	 * Gets a {@link Map} relating alignments with their associated sequences
	 * 
	 * @param path The temporal path of the alignment
	 * @param references Whether to search in MAHMI reference database
	 * @param posibles Whether to search in MAHMI posible database
	 * @return A map relating alignment with sequence
	 * 
	 * @see AminoAcidSequence
	 */
	private Map<String, AminoAcidSequence> getSequencesMap(	final Path path,
															final boolean references,
															final boolean posibles) {
		final Map<String, AminoAcidSequence> sequences = new HashMap<String, AminoAcidSequence>();
		if (references)
			populateMap(Paths.get(path.toString() + "/ref/blastdbcmd.out"), sequences);
		if (posibles)
			populateMap(Paths.get(path.toString() + "/pos/blastdbcmd.out"), sequences);
		return sequences;
	}

	/**
	 * Populates the alignment-sequence {@link Map} with the text plain blastdbcmd output
	 * 
	 * @param path The temporal path of the alignment
	 * @param map The map to populate
	 * 
	 * @see AminoAcidSequence
	 */
	private void populateMap(final Path path, final Map<String, AminoAcidSequence> map) {
		try {
			final Iterator<String> iterator = Files.readAllLines(path).iterator();
			String auxiliarLine = iterator.next();
			while (iterator.hasNext()) {
				final String description = auxiliarLine;
				String sequence = iterator.next();
				while (iterator.hasNext()) {
					auxiliarLine = iterator.next();
					if (auxiliarLine.startsWith(">"))
						break;
					else
						sequence += auxiliarLine;
				}
				map.put(description.substring(5, description.length()),
						AminoAcidSequence.fromString(sequence).some());
			}
		} catch (final IOException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * Checks if an alignments passes the potential bioactivity threshold
	 * 
	 * @param alignment The alignment to check
	 * @param threshold The threshold to check
	 * @return Whether the alignment pass the threshold
	 * 
	 * @see BlastAlignment
	 */
	private boolean passThreshold(final BlastAlignment alignment, final int threshold) {
		if (alignment.getDescription().startsWith("MHM_POS")) {
			if (Double.parseDouble(
					alignment.getDescription().split(" ")[3].split("%")[0]) >= threshold)
				return true;
			else
				return false;
		} else
			return true;

	}

	/**
	 * Checks if an alignment has one of the bioactivities of the list
	 * 
	 * @param alignment The alignment to check bioactivity
	 * @param bioactivity The list of bioactivities to check
	 * @return Whether the alignment has one of the bioactivities
	 * 
	 * @see BlastAlignment
	 */
	private boolean hasBioactivity(final BlastAlignment alignment, final List<String> bioactivity) {
		if (bioactivity.contains(alignment.getDescription().split(" ")[2]))
			return true;
		else
			return false;
	}

	/**
	 * Parses BlastP output file into a {@linkplain List} of Blast alignments
	 * 
	 * @param path The Blastp output file
	 * @return The parsed list of Blast alignments
	 * 
	 * @see BlastAlignment
	 */
	private List<BlastAlignment> blastOutParser(final Path path) {
		final List<BlastAlignment> alignments = new LinkedList<BlastAlignment>();
		try {
			final List<String> lines = Files.readAllLines(path);
			final Iterator<String> iterator = lines.iterator();
			while (iterator.hasNext()) {
				final String realFirstLine = iterator.next();
				if (realFirstLine.startsWith(">")) {
					String firstLine = realFirstLine.substring(5/* 1 */, realFirstLine.length());
					String secondLine = "";
					while (secondLine == "") {
						final String perhapsSecond = iterator.next();
						if (perhapsSecond.startsWith("Length="))
							secondLine = perhapsSecond;
						else
							firstLine += perhapsSecond;
					}
					iterator.next();
					final String[] thirdLine  = iterator.next().split(",");
					final String[] fourthLine = iterator.next().split(",");
					iterator.next();
					final String fiveLine 	  = iterator.next().substring(5);
					final String sixLine 	  = iterator.next();
					final String sevenLine 	  = iterator.next().substring(5);
					alignments.add(new BlastAlignment(firstLine,
							Double.parseDouble(thirdLine[0].split(" ")[3]),
							Double.parseDouble(thirdLine[1].split(" ")[4]),
							Integer.parseInt(fourthLine[0].split("\\(")[1].substring(0,
									fourthLine[0].split("\\(")[1].length() - 2)),
							Integer.parseInt(fourthLine[1].split("\\(")[1].substring(0,
									fourthLine[1].split("\\(")[1].length() - 2)),
							Integer.parseInt(fourthLine[2].split("\\(")[1].substring(0,
									fourthLine[2].split("\\(")[1].length() - 2)),
							Integer.parseInt(secondLine.substring(7, secondLine.length())),
							fiveLine.replace(" ", "").replaceAll("\\d", ""),
							sixLine.replaceAll("^\\s*", ""),
							sevenLine.replace(" ", "").replaceAll("\\d", ""),
							path.getParent().getParent().toString(),
							Integer.parseInt(fiveLine.replaceAll("^\\s*", "").split(" ")[0]),
							Integer.parseInt(fiveLine.replaceAll("^\\s*", "").split(
									" ")[fiveLine.replaceAll("^\\s*", "").split(" ").length - 1]),
							Integer.parseInt(sevenLine.replaceAll("^\\s*", "").split(" ")[0]),
							Integer.parseInt(sevenLine.replaceAll("^\\s*", "")
									.split(" ")[sevenLine.replaceAll("^\\s*", "").split(" ").length
											- 1])));
				}
			}
		} catch (final IOException e) {
			log.error(e.getMessage());
		}
		return alignments;
	}

	/**
	 * Executes BlastP to perform an alignment
	 * 
	 * @param input The {@linkplain Path} in which perform the alignment
	 * @param aas The sequence to align
	 * @param db The Blast database against which align
	 * @param blastOptions The BlastP execution options
	 * @return The {@linkplain Path} to the BlastP output file
	 * 
	 * @see AminoAcidSequence
	 */
	private Path runBlastP(	final Path input,
							final AminoAcidSequence aas,
							final String db,
							final BlastOptions blastOptions) {
		final Path output = Paths.get(input.toString() + "/blastp.out");
		final Path readPath = Paths.get(input.toString() + "/read.faa");
		try (final PrintWriter pw = new PrintWriter(
				new BufferedWriter(new FileWriter(readPath.toString(), false)))) {
			pw.println(aas.asString());
		} catch (final IOException e) {
			log.error("Error creating blast auxiliar file:\n" + e.getMessage());
		}
		blastp(readPath, output, db, blastOptions).run();
		return output;
	}

	/**
	 * Executes BlastDbCmd to search alignment sequences
	 * 
	 * @param alignments The {@linkplain List} of alignments to search sequences
	 * @param path The {@linkplain Path} in which execute BlastDbCmd
	 * @param db The Blast database in which search
	 * @return The {@linkplain Path} to the BlastDbCmd output file
	 */
	private Path runBlastDbCmd(	final List<BlastAlignment> alignments,
								final Path path,
								final String db) {
		final Path readPath = Paths.get(path.toString() + "/hits.txt");
		final Path output   = Paths.get(path.toString() + "/blastdbcmd.out");
		try (final PrintWriter pw = new PrintWriter(
				new BufferedWriter(new FileWriter(readPath.toString(), false)))) {
			alignments.forEach(a -> pw.println(a.getDescription()));
		} catch (final IOException e) {
			log.error("Error creating blastdbmd auxiliar file:\n" + e.getMessage());
		}
		blastdbcmd(readPath, output, db).run();
		return output;
	}
}
