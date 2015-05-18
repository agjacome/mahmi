package es.uvigo.ei.sing.mahmi.translator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;

@Slf4j
@AllArgsConstructor(staticName = "mhdiscover")
public class MetahitDiscoverRunner {
	private final Path input;
    private final Path output;
    private final Config metahit  = ConfigFactory.load("metahit");
    
    private String getHeader(){
    	try (BufferedReader br = new BufferedReader(new FileReader(input.toString()))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       if(line.startsWith(">")){
		    	   return line.substring(2,line.length());    	   
		       }
		    }
		} catch (IOException e) {
			log.error(e.getMessage());
		}
    	return "";
    }
    
    private AminoAcidSequence getSequence(String header){
    	try (BufferedReader br = new BufferedReader(new FileReader(metahit.getString("mhlocation")))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       if(line.contains(header)){
		    	   return AminoAcidSequence.fromString(br.readLine()).some();	    	   
		       }
		    }
		} catch (IOException e) {
			log.error(e.getMessage());
		}
    	return AminoAcidSequence.empty();
    }
    
    public void run() {
    	val header=getHeader();
    	try (val pw = new PrintWriter(new BufferedWriter(new FileWriter(output.toString(), false)))){	    	
			if (!header.equals("")){
				pw.println(">"+header);
	        	pw.println(getSequence(header).asString());
        	}
        }catch(IOException e){
        	log.error(e.getMessage());
        }
    }

    
}
