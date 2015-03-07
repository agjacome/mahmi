package es.uvigo.ei.sing.mahmi.psort;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PSortExec {
    
    private PSortB psb=new PSortB();
    
    public void exec(char gram, Path inputPath){
        val outputPath=Paths.get(inputPath.getParent()+"/psortb.out");
        if(psb.sort(gram, inputPath ,outputPath) == 0){
            List<String> out=new ArrayList<String>();
            List<String> in=new ArrayList<String>();
            try {
                out=Files.readAllLines(outputPath,Charset.defaultCharset());
                in=Files.readAllLines(inputPath,Charset.defaultCharset());
            } catch (IOException e) {
                log.error(e.getMessage());
            }            

            createSortFile(in, out, inputPath.getParent());
        }
        deleteOutFile(outputPath);
    }    
    
    private void deleteOutFile(Path path){
    	try {
			Files.delete(path);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
    }
    
    private void createSortFile(List<String> in, List<String> out, Path path){
        int currentLine=0;
        try (val bw = new BufferedWriter(new FileWriter(path+"/sort.faa",false))) {   
            for(String protein:out){
                if(protein.contains("Extracellular")||protein.contains("Unknown")){
                    bw.write(in.get((currentLine*2)-2)+"\n");
                    bw.write(in.get((currentLine*2)-1)+"\n");
                }
                currentLine++;
            }
        }catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
