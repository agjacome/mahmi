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
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;

@Slf4j
@AllArgsConstructor
public class Browser {
	private final Config blastConf = ConfigFactory.load("blast");

	public List<BlastAligment> search(final AminoAcidSequence sequence,
			final String[] databases, final int threshold,
			final String[] bioactivity, final Path path) {
		
		createAuxiliarFolders(path);		
		
		if (databases.length == 2) {
			return filter(
					threshold, bioactivity,
					runBlastP(Paths.get(path.toString()+"/ref"), sequence, blastConf.getString("refdb")),
					runBlastP(Paths.get(path.toString()+"/pos"), sequence, blastConf.getString("posdb")));

		} else {
			if (databases[0].equals("refdb"))
				return filter(
						threshold, bioactivity,
						runBlastP(Paths.get(path.toString()+"/ref"), sequence, blastConf.getString("refdb")));
			else
				return filter(
						threshold, bioactivity,
						runBlastP(Paths.get(path.toString()+"/pos"), sequence, blastConf.getString("posdb")));
		}
	}
	
	private void createAuxiliarFolders(final Path path){
		final File refFile = new File(path.toString()+"/ref");
		refFile.mkdir();
		final File posFile = new File(path.toString()+"/pos");
		posFile.mkdir();
	}

	private List<BlastAligment> filter(final int threshold, final String[] bioactivity, final Path ... paths) {
		final List<BlastAligment> aligments = new LinkedList<BlastAligment>();
		if (paths.length == 2) {
			blastOutParser(paths[0]).forEach(ba -> aligments.add(ba));
			blastOutParser(paths[1]).forEach(ba -> aligments.add(ba));
		} else {
			blastOutParser(paths[0]).forEach(ba -> aligments.add(ba));
		}
		return filterAligments(aligments, bioactivity, threshold, paths[0].getParent().getParent());
	}
	
	private List<BlastAligment> filterAligments(final List<BlastAligment> aligments, 
												final String[] bioactivity, 
												final int threshold, 
												final Path path){
		final List<BlastAligment> filterAligments = new LinkedList<BlastAligment>();		
		getAligmentsWithSequences(aligments, path).forEach(a -> {
			if(hasBioactivity(a, bioactivity) && passThreshold(a, threshold)){
				filterAligments.add(a);
			}
		});
		return filterAligments;
	}
	
	private List<BlastAligment> getAligmentsWithSequences(final List<BlastAligment> aligments, final Path path){
		final List<BlastAligment> aligmentsWithSequences = new LinkedList<BlastAligment>();
		final List<BlastAligment> references = new LinkedList<BlastAligment>();
		final List<BlastAligment> posibles = new LinkedList<BlastAligment>();
		aligments.forEach(a -> {
			if(a.getDescription().startsWith("MHM_POS")) posibles.add(a);
			else references.add(a);
		});
		if(!references.isEmpty()) runBlastDbCmd(references, Paths.get(path.toString()+"/ref"), blastConf.getString("refdb"));
		if(!posibles.isEmpty()) runBlastDbCmd(posibles, Paths.get(path.toString()+"/pos"), blastConf.getString("posdb"));
		final Map<String, AminoAcidSequence> sequences = getSequencesMap(path, !references.isEmpty(), !posibles.isEmpty());
		references.forEach(a-> {
			a.setSequence(sequences.get(a.getDescription()));
			aligmentsWithSequences.add(a);
		});
		posibles.forEach(a-> {
			a.setSequence(sequences.get(a.getDescription()));
			aligmentsWithSequences.add(a);
		});
		
		return aligmentsWithSequences;
	}
	
	private Map<String, AminoAcidSequence> getSequencesMap(final Path path, final boolean references, final boolean posibles){
		Map<String, AminoAcidSequence> sequences = new HashMap<String, AminoAcidSequence>();		
		if(references) sequences = populateMap(Paths.get(path.toString()+"/ref/blastdbcmd.out"), sequences);		
		if(posibles)   sequences = populateMap(Paths.get(path.toString()+"/pos/blastdbcmd.out"), sequences);		
		return sequences;
	}
	
	private Map<String, AminoAcidSequence> populateMap(final Path path, final Map<String, AminoAcidSequence> map){
		try {
			final List<String> refs = Files.readAllLines(path);
			Iterator<String> iterator = refs.iterator();
			while(iterator.hasNext()){
				final String line = iterator.next();
				final String key = line.substring(5, line.length());
				final AminoAcidSequence value = AminoAcidSequence.fromString(iterator.next()).some();
				map.put(key, value);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return map;
	}
	
	//TODO
	private boolean passThreshold(final BlastAligment aligment, final int threshold){
		if(aligment.getDescription().startsWith("MHM_POS")){
			
			return true;
		}else{
			return true;
		}
		
	}
	
	//TODO
	private boolean hasBioactivity(final BlastAligment aligment, final String[] bioactivity){
		if(bioactivity.length != 8){
			
			return true;
		}else{
			return true;
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
					aligments.add( new BlastAligment(
							firsLine.substring(1, firsLine.length()),
							Double.parseDouble(thirdLine[0].split(" ")[3]),
							Double.parseDouble(thirdLine[1].split(" ")[4]),
							Integer.parseInt(fourthLine[0].split("\\(")[1].substring(0, fourthLine[0].split("\\(")[1].length()-2)),
							Integer.parseInt(fourthLine[1].split("\\(")[1].substring(0, fourthLine[1].split("\\(")[1].length()-2)),
							Integer.parseInt(fourthLine[2].split("\\(")[1].substring(0, fourthLine[2].split("\\(")[1].length()-2)),
							Integer.parseInt(secondLine.substring(7, secondLine.length())) ));
				}
			}			
		} catch (IOException e) {
			log.error(e.getMessage());
		}		
		return aligments;
	}

	public void getAligment(final BlastAligment aligment) {
		// TODO
	}

	private Path runBlastP(final Path input, final AminoAcidSequence aas,
			final String db) {
		final Path output = Paths.get(input.toString()+"/blastp.out");
		final Path readPath = Paths.get(input.toString()+"/read.faa");
		try (final PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(readPath.toString(), false)))) {
			pw.println(aas.asString());
		} catch (IOException e) {
			log.error("Error creating blast auxiliar file:\n" + e.getMessage());
		}
		blastp(readPath, output, db).run();
		try {
			Files.deleteIfExists(readPath);
		} catch (IOException e) {
			log.error("Error deleting blast auxiliar file:\n" + e.getMessage());
		}
		return output;
	}
	
	private Path runBlastDbCmd(final List<BlastAligment> aligments, final Path path, final String db) {
		final Path readPath = Paths.get(path.toString()+"/hits.txt");
		final Path output = Paths.get(path.toString()+"/blastdbcmd.out");
		try (final PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(readPath.toString(), false)))) {
			aligments.forEach(a -> pw.println(a.getDescription()));
		} catch (IOException e) {
			log.error("Error creating blastdbmd auxiliar file:\n" + e.getMessage());
		}		
        blastdbcmd(readPath, output, db).run();
        try {
			Files.deleteIfExists(readPath);
		} catch (IOException e) {
			log.error("Error deleting blastdbcmd auxiliar file:\n" + e.getMessage());
		}
        return output;
    }
}
