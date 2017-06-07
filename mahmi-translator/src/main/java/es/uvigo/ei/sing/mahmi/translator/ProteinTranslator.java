package es.uvigo.ei.sing.mahmi.translator;

import static es.uvigo.ei.sing.mahmi.translator.BlastDbCmdRunner.blastdbcmd;
import static es.uvigo.ei.sing.mahmi.translator.BlastxRunner.blastx;
import static es.uvigo.ei.sing.mahmi.translator.MetahitDiscoverRunner.mhdiscover;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;

@Deprecated
@Slf4j
@AllArgsConstructor
public class ProteinTranslator {
	private final String db;
	
	private Path runBlastX(final Path input, final NucleobaseSequence nbs) {
        val output = input.resolveSibling("blastx.out");
        val readPath = input.resolveSibling("read.fna");
        try (val pw = new PrintWriter(new BufferedWriter(new FileWriter(readPath.toString(), false)))){	    	
        	pw.println(nbs.asString()); 
        }catch(IOException e){
        	log.error("Error creating blast auxiliar file:\n"+e.getMessage());
        }
        blastx(readPath, output, db).run();
        try {	    	        	
        	Files.deleteIfExists(readPath);
        }catch(IOException e){
        	log.error("Error deleting blast auxiliar file:\n"+e.getMessage());
        }
        return output;
    }
	
	
	@SuppressWarnings("unused")
	private Path runBlastDbCmd(final String input, final Path inputPath) {
		val output = inputPath.resolveSibling("blastdbcmd.out");
        blastdbcmd(input, output, db).run();
        return output;
    }
	
	private Path runMetahitDiscover(final Path input, final Path blastx){
		val output = input.resolveSibling("mhdiscover.out");
		mhdiscover(blastx, output).run();
		return output;
	}	
	
	public void translate(final Path input){
		val protPath=input.resolveSibling("proteins.faa");
		try (val pw = new PrintWriter(new BufferedWriter(new FileWriter(protPath.toString(), true)))){		
			val genomes = readFromPath(input);
			val fastaIter = genomes.iterator();
			while(fastaIter.hasNext()){	
				val protein = Files.readAllLines(runMetahitDiscover(input, runBlastX(input, fastaIter.next())));
				for(val line : protein){
					pw.println(line);
				}	
				Files.deleteIfExists(input.resolveSibling("blastx.out"));
				Files.deleteIfExists(input.resolveSibling("mhdiscover.out"));
			}
		} catch (IOException e) {
			log.error("Error translating fasta file:\n"+e.getMessage());
		} 
	}	
	
	private Fasta<NucleobaseSequence> readFromPath(Path input){
		List<NucleobaseSequence> reads = new LinkedList<NucleobaseSequence>();
		try {
			val lines = Files.readAllLines(input);
			for(val line : lines){
				if(!line.startsWith(">")){
					reads.add(NucleobaseSequence.fromString(line).some());
				}
			}
		} catch (IOException e) {
			log.error("Error reading fasta file:\n"+e.getMessage());
		}
		return Fasta.of(reads.iterator());
	}
}
