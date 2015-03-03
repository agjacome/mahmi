package es.uvigo.ei.sing.mahmi.psort;

import java.io.*;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PSortB {
    
    public int sort(char gram, Path input, Path output){
        try{
            final String[] command = {"sh","-c","psort -"+gram+" -o=terse --verbose "+
                                input.toString()+" > "+
                                output.toString()};
            final Process process = Runtime.getRuntime().exec(command);
            
            logExec(process);
            logErrors(process);
    
            return process.waitFor(); 
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }        
    }
    
    private void logExec(Process process){
        new Thread(){
            public void run(){
                try{
                    InputStream is = process.getInputStream();
                    log.info(IOUtils.toString(is, "UTF-8"));
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
    
    private void logErrors(Process process){
        new Thread(){
            public void run(){
                try{
                    InputStream is = process.getErrorStream();
                    log.error(IOUtils.toString(is, "UTF-8"));
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
