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

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import es.uvigo.ei.sing.mahmi.browser.utils.BlastAligment;
import es.uvigo.ei.sing.mahmi.browser.utils.PeptideCalculator;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;

@Slf4j
@AllArgsConstructor(staticName = "browser")
public class Browser {
	private final Config blastConf     = ConfigFactory.load("blast");
	private final PeptideCalculator pc = new PeptideCalculator();

	public List<BlastAligment> search( final AminoAcidSequence sequence,
									   final List<String> databases, 
									   final int threshold,
									   final List<String> bioactivity, 
									   final Path path ) {
		createAuxiliarFolders(path);
		final List<BlastAligment> aligments = new LinkedList<BlastAligment>();
		if(databases.contains("refdb")) blastOutParser(runBlastP(Paths.get(path.toString()+"/ref"), sequence, blastConf.getString("refdb"))).forEach(ba -> aligments.add(ba));
		if(databases.contains("posdb")) blastOutParser(runBlastP(Paths.get(path.toString()+"/pos"), sequence, blastConf.getString("posdb"))).forEach(ba -> aligments.add(ba));		
		return filter(aligments, threshold, bioactivity, path);
	}

	public BlastAligment getAligment(final BlastAligment aligment) {
		final double pI = pc.calculatePI(aligment.getSequence());
		aligment.setpI(pI);
		aligment.setSubLocation("Unknown");
		return aligment;
	}
	
	private void createAuxiliarFolders(final Path path){
		final File searchFile = new File(path.toString());
		searchFile.mkdir();		
		final File refFile = new File(path.toString()+"/ref");
		refFile.mkdir();
		final File posFile = new File(path.toString()+"/pos");
		posFile.mkdir();
	}
	
	private List<BlastAligment> filter( final List<BlastAligment> aligments, 
									    final int threshold, 
									    final List<String> bioactivity, 
									    final Path path ){
		final List<BlastAligment> filterAligments = new LinkedList<BlastAligment>();		
		getAligmentsWithSequences(aligments, path).forEach(a -> {
			if(hasBioactivity(a, bioactivity) && passThreshold(a, threshold)){
				filterAligments.add(a);
			}
		});
		return filterAligments;
	}
	
	private List<BlastAligment> getAligmentsWithSequences( final List<BlastAligment> aligments, 
														   final Path path ){
		final List<BlastAligment> aligmentsWithSequences = new LinkedList<BlastAligment>();		
		final List<BlastAligment> references             = new LinkedList<BlastAligment>();
		final List<BlastAligment> posibles               = new LinkedList<BlastAligment>();
		
		aligments.forEach(a -> {
			if(a.getDescription().startsWith("MHM_POS")) posibles.add(a);
			else references.add(a);
		});
		
		if(!references.isEmpty()) runBlastDbCmd(references, Paths.get(path.toString()+"/ref"), blastConf.getString("refdb"));
		if(!posibles.isEmpty())   runBlastDbCmd(posibles,   Paths.get(path.toString()+"/pos"), blastConf.getString("posdb"));
		
		Map<String, AminoAcidSequence> map = getSequencesMap(path, !references.isEmpty(), !posibles.isEmpty());
		
		addSequences(references, aligmentsWithSequences, map);
		addSequences(posibles,   aligmentsWithSequences, map);		
		return aligmentsWithSequences;
	}
	
	private void addSequences( final List<BlastAligment> aligments, 
							   final List<BlastAligment> aligmentsWithSequence, 
							   final Map<String, AminoAcidSequence> map) {
		aligments.forEach(a-> {
			a.setSequence(map.get(a.getDescription()).asString());
			aligmentsWithSequence.add(a);
		});
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
			while(iterator.hasNext()){
				final String description = iterator.next();
				map.put( description.substring(5, description.length()), 
						 AminoAcidSequence.fromString(iterator.next()).some() );
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private boolean passThreshold( final BlastAligment aligment, 
								   final int threshold ){
		if(aligment.getDescription().startsWith("MHM_POS")){
			if(Integer.parseInt(aligment.getDescription().split(" ")[3].split("%")[0]) >= threshold)
				return true;
			else
				return false;
		}else{
			return true;
		}
		
	}
	
	private boolean hasBioactivity( final BlastAligment aligment,
									final List<String> bioactivity ){
		if(bioactivity.contains(aligment.getDescription().split(" ")[2])){			
			return true;
		}else{
			return false;
		}
	}
	
	private List<BlastAligment> blastOutParser(final Path path){
		final List<BlastAligment> aligments = new LinkedList<BlastAligment>();
		try {
			final List<String> lines = Files.readAllLines(path);
			final Iterator<String> iterator = lines.iterator();
			while(iterator.hasNext()){
				final String firsLine = iterator.next();
				if(firsLine.startsWith(">")){
					final String secondLine = iterator.next();
					iterator.next();
					final String[] thirdLine = iterator.next().split(",");					
					final String[] fourthLine = iterator.next().split(",");
					iterator.next();
					final String fiveLine   = iterator.next().substring(5);
					final String sixLine = iterator.next();
					final String sevenLine   = iterator.next().substring(5);
					aligments.add( new BlastAligment(
							firsLine.substring(1, firsLine.length()),
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
		return aligments;
	}
	
	private Path runBlastP( final Path input,
							final AminoAcidSequence aas,
							final String db) {
		final Path output = Paths.get(input.toString()+"/blastp.out");
		final Path readPath = Paths.get(input.toString()+"/read.faa");
		try (final PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(readPath.toString(), false)))) {
			pw.println(aas.asString());
		} catch (IOException e) {
			log.error("Error creating blast auxiliar file:\n" + e.getMessage());
		}
		blastp(readPath, output, db).run();
		return output;
	}
	
	private Path runBlastDbCmd( final List<BlastAligment> aligments,
								final Path path, 
								final String db ) {
		final Path readPath = Paths.get(path.toString()+"/hits.txt");
		final Path output = Paths.get(path.toString()+"/blastdbcmd.out");
		try (final PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(readPath.toString(), false)))) {
			aligments.forEach(a -> pw.println(a.getDescription()));
		} catch (IOException e) {
			log.error("Error creating blastdbmd auxiliar file:\n" + e.getMessage());
		}		
        blastdbcmd(readPath, output, db).run();
        return output;
    }
}
