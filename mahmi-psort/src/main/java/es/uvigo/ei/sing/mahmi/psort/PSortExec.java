package es.uvigo.ei.sing.mahmi.psort;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaReader;

@Slf4j
public class PSortExec {
    
	private static final FastaReader<AminoAcidSequence>  proteinReader = FastaReader.forAminoAcid();
    private PSortB psb=new PSortB();
    
    public Fasta<AminoAcidSequence> exec(char gram, Path inputPath){
        val outputPath=Paths.get(inputPath.getParent()+"/psortb.out");
        Fasta<AminoAcidSequence> fasta = Fasta.empty();
        if(psb.sort(gram, inputPath ,outputPath) == 0){
            List<String> out=new ArrayList<String>();
            try {
                out=Files.readAllLines(outputPath,Charset.defaultCharset());
            } catch (IOException e) {
                log.error(e.getMessage());
            }            

            fasta = getSortFasta(out, inputPath);
        }
        deleteOutFile(outputPath);
        return fasta;
    }    
    
    private void deleteOutFile(Path path){
    	try {
			Files.delete(path);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
    }
    
    private Fasta<AminoAcidSequence> getSortFasta(List<String> out, Path path){
    	 try {
			val iterator = proteinReader.fromPath(path).iterator();
			List<AminoAcidSequence> list = new ArrayList<AminoAcidSequence>();
			
	    	for(String protein:out){
	    		if(!protein.equals("SeqID	Localization	Score")){
	    			val aminoAcid = iterator.next();
		            if(protein.contains("Extracellular")){//||protein.contains("Unknown")){	            	
		            	list.add(aminoAcid);
		            	
		            }	
	    		}
	        }
            return Fasta.of(list.iterator());
	    	
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return Fasta.empty();
    }
}
