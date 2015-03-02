package es.uvigo.ei.sing.mahmi.psort;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PSortExec {
    
    private PSortB psb=new PSortB();
    
    public void exec(char gram, Path inputPath, Path outputPath){
        if(psb.sort(gram, inputPath ,outputPath) == 0){
            List<String> out=new ArrayList<String>();
            List<String> in=new ArrayList<String>();
            try {
                out=Files.readAllLines(outputPath,Charset.defaultCharset());
                in=Files.readAllLines(inputPath,Charset.defaultCharset());
            } catch (IOException e) {
                log.error(e.getMessage());
            }            
            createSortFile(in, out); 
        }
    }    
    
    private void createSortFile(List<String> in, List<String> out){
        File sortFile = new File("/home/mahmi/Escritorio/sort.txt");
        if (sortFile.exists()) {
            sortFile.delete();
            sortFile = new File("/home/mahmi/Escritorio/sort.txt");
        }
        writeSortFile(in, out); 
        
    }
    
    private void writeSortFile(List<String> in, List<String> out){
        int currentProtein=1;
        int currentLine=19; 
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("/home/mahmi/Escritorio/sort.txt"))) {   
            while(currentProtein<=out.size()/22){
                String result=out.get(currentLine-1);
            
                if(result.contains("Extracellular")||result.contains("Unknown")){
                    bw.write(in.get((currentProtein*2)-2)+"\n");
                    bw.write(in.get((currentProtein*2)-1)+"\n");
                }
                currentLine=currentLine+22;
                currentProtein++;
            }
        }catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
