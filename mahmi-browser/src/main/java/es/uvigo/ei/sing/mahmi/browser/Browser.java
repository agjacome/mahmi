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

import lombok.extern.slf4j.Slf4j;
import es.uvigo.ei.sing.mahmi.browser.utils.BlastAlignment;
import es.uvigo.ei.sing.mahmi.browser.utils.BlastOptions;
import es.uvigo.ei.sing.mahmi.browser.utils.PeptideCalculator;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;

@Slf4j
public class Browser {
//	private final Config blastConf     = ConfigFactory.load("blast");
	private final PeptideCalculator pc = new PeptideCalculator();
	
	private Browser() { }
	
	public static Browser browser(){
		return new Browser();
	}

	public List<BlastAlignment> search( final AminoAcidSequence sequence,
									    final List<String> databases, 
									    final int threshold,
									    final List<String> bioactivity, 
									    final Path path,
									    final BlastOptions blastOptions) {
		createAuxiliarFolders(path);
		final List<BlastAlignment> alignments = new LinkedList<BlastAlignment>();
		if(databases.contains("refdb")) blastOutParser(runBlastP(Paths.get(path.toString()+"/ref"), sequence,"/home/mahmi/blast_db/mahmiReference", blastOptions)).forEach(ba -> alignments.add(ba));
		if(databases.contains("posdb")) blastOutParser(runBlastP(Paths.get(path.toString()+"/pos"), sequence,"/home/mahmi/blast_db/mahmiPosible", blastOptions)).forEach(ba -> alignments.add(ba));
		return filter(alignments, threshold, bioactivity, path);
	}
	
	private void createAuxiliarFolders(final Path path){
		final File searchFile = new File(path.toString());
		searchFile.mkdir();		
		final File refFile = new File(path.toString()+"/ref");
		refFile.mkdir();
		final File posFile = new File(path.toString()+"/pos");
		posFile.mkdir();
	}
	
	private List<BlastAlignment> filter( final List<BlastAlignment> alignments, 
									     final int threshold, 
									     final List<String> bioactivity, 
									     final Path path ){		
		final List<BlastAlignment> filterAlignments = new LinkedList<BlastAlignment>();		
		getAlignmentsWithSequences(alignments, path).forEach(a -> {
			if(hasBioactivity(a, bioactivity) && passThreshold(a, threshold)){
				filterAlignments.add(a);
			}
		});		
		return filterAlignments;
	}
	
	private List<BlastAlignment> getAlignmentsWithSequences( final List<BlastAlignment> alignments, 
														     final Path path ){
		final List<BlastAlignment> alignmentsWithSequences = new LinkedList<BlastAlignment>();		
		final List<BlastAlignment> references              = new LinkedList<BlastAlignment>();
		final List<BlastAlignment> posibles                = new LinkedList<BlastAlignment>();
		
		alignments.forEach(a -> {
			if(a.getDescription().startsWith("MHM_POS")) posibles.add(a);
			else references.add(a);
		});
		if(!references.isEmpty()) runBlastDbCmd(references, Paths.get(path.toString()+"/ref"), "/home/mahmi/blast_db/mahmiReference");
		if(!posibles.isEmpty())   runBlastDbCmd(posibles,   Paths.get(path.toString()+"/pos"), "/home/mahmi/blast_db/mahmiPosible");
		Map<String, AminoAcidSequence> map = getSequencesMap(path, !references.isEmpty(), !posibles.isEmpty());
		addSequences(references, alignmentsWithSequences, map);
		addSequences(posibles,   alignmentsWithSequences, map);
		return alignmentsWithSequences;
	}
	
	private void addSequences( final List<BlastAlignment> alignments, 
							   final List<BlastAlignment> alignmentsWithSequence, 
							   final Map<String, AminoAcidSequence> map) {
		alignments.forEach(a-> {
			a.setSequence(map.get(a.getDescription()).asString());
			alignmentsWithSequence.add(calculateMWandPI(a));
		});
	}

	private BlastAlignment calculateMWandPI(final BlastAlignment alignment) {
		alignment.setpI(pc.calculatePI(alignment.getSequence()));
		alignment.setmW(pc.calculateMW(alignment.getSequence()));
		return alignment;
	}
	
	private Map<String, AminoAcidSequence> getSequencesMap( final Path path, 
															final boolean references, 
															final boolean posibles ){
		final Map<String, AminoAcidSequence> sequences = new HashMap<String, AminoAcidSequence>();
		if(references) populateMap(Paths.get(path.toString()+"/ref/blastdbcmd.out"), sequences);
		if(posibles)   populateMap(Paths.get(path.toString()+"/pos/blastdbcmd.out"), sequences);	
		return sequences;
	}
	
	private void populateMap( final Path path, 
							  final Map<String, AminoAcidSequence> map ){
		try {
			final Iterator<String> iterator = Files.readAllLines(path).iterator();
			String auxiliarLine = iterator.next();
			while(iterator.hasNext()){
				final String description = auxiliarLine;
				String sequence = iterator.next();
				while(iterator.hasNext()){
					auxiliarLine = iterator.next();
					if(auxiliarLine.startsWith(">")){
						break;
					}else{
						sequence+=auxiliarLine;
					}
				}
				map.put( description.substring(5, description.length()), 
						 AminoAcidSequence.fromString(sequence).some() );
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private boolean passThreshold( final BlastAlignment alignment, 
								   final int threshold ){
		if(alignment.getDescription().startsWith("MHM_POS")){
			if(Double.parseDouble(alignment.getDescription().split(" ")[3].split("%")[0]) >= threshold)
				return true;
			else
				return false;
		}else{
			return true;
		}
		
	}
	
	private boolean hasBioactivity( final BlastAlignment alignment,
									final List<String> bioactivity ){
		if(bioactivity.contains(alignment.getDescription().split(" ")[2])){			
			return true;
		}else{
			return false;
		}
	}
	
	private List<BlastAlignment> blastOutParser(final Path path){
		final List<BlastAlignment> alignments = new LinkedList<BlastAlignment>();
		try {
			final List<String> lines = Files.readAllLines(path);
			final Iterator<String> iterator = lines.iterator();
			while(iterator.hasNext()){
				final String realFirstLine = iterator.next();
				if(realFirstLine.startsWith(">")){	
					String firstLine = realFirstLine.substring(1/*5*/, realFirstLine.length());
					String secondLine="";
					while(secondLine == ""){
						final String perhapsSecond = iterator.next();
						if(perhapsSecond.startsWith("Length=")){
							secondLine = perhapsSecond;
						}else{
							firstLine+=perhapsSecond;
						}
					}
					iterator.next();
					final String[] thirdLine = iterator.next().split(",");					
					final String[] fourthLine = iterator.next().split(",");
					iterator.next();
					final String fiveLine = iterator.next().substring(5);					
					final String sixLine = iterator.next();
					final String sevenLine = iterator.next().substring(5);
					alignments.add( new BlastAlignment(
							firstLine,
							Double.parseDouble(thirdLine[0].split(" ")[3]),
							Double.parseDouble(thirdLine[1].split(" ")[4]),
							Integer.parseInt(fourthLine[0].split("\\(")[1].substring(0, fourthLine[0].split("\\(")[1].length()-2)),
							Integer.parseInt(fourthLine[1].split("\\(")[1].substring(0, fourthLine[1].split("\\(")[1].length()-2)),
							Integer.parseInt(fourthLine[2].split("\\(")[1].substring(0, fourthLine[2].split("\\(")[1].length()-2)),
							Integer.parseInt(secondLine.substring(7, secondLine.length())),
							fiveLine.replace(" " , "").replaceAll("\\d",""),
							sixLine.replaceAll("^\\s*",""),
							sevenLine.replace(" " , "").replaceAll("\\d",""),
							path.getParent().getParent().toString(),
							Integer.parseInt(fiveLine.replaceAll("^\\s*","").split(" ")[0]),
							Integer.parseInt(fiveLine.replaceAll("^\\s*","").split(" ")[fiveLine.replaceAll("^\\s*","").split(" ").length-1]),
							Integer.parseInt(sevenLine.replaceAll("^\\s*","").split(" ")[0]),
							Integer.parseInt(sevenLine.replaceAll("^\\s*","").split(" ")[sevenLine.replaceAll("^\\s*","").split(" ").length-1])  ));
				}
			}			
		} catch (IOException e) {
			log.error(e.getMessage());
		}		
		return alignments;
	}
	
	private Path runBlastP( final Path input,
							final AminoAcidSequence aas,
							final String db,
							final BlastOptions blastOptions) {
		final Path output   = Paths.get(input.toString()+"/blastp.out");
		final Path readPath = Paths.get(input.toString()+"/read.faa");
		try (final PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(readPath.toString(), false)))) {
			pw.println(aas.asString());
		} catch (IOException e) {
			log.error("Error creating blast auxiliar file:\n" + e.getMessage());
		}
		blastp(readPath, output, db, blastOptions).run();
		return output;
	}
	
	private Path runBlastDbCmd( final List<BlastAlignment> alignments,
								final Path path, 
								final String db ) {
		final Path readPath = Paths.get(path.toString()+"/hits.txt");
		final Path output   = Paths.get(path.toString()+"/blastdbcmd.out");
		try (final PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(readPath.toString(), false)))) {
			alignments.forEach(a -> pw.println(a.getDescription()));
		} catch (IOException e) {
			log.error("Error creating blastdbmd auxiliar file:\n" + e.getMessage());
		}		
        blastdbcmd(readPath, output, db).run();
        return output;
    }
}
