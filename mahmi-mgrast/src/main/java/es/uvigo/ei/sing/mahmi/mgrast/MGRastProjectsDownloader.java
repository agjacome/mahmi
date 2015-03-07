package es.uvigo.ei.sing.mahmi.mgrast;

import java.io.File;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@Slf4j
public class MGRastProjectsDownloader {
        
    final Config config          = ConfigFactory.load("downloader");
    final MGRastGetter mgrGetter = new MGRastGetter(); 
    
    public void downloadProject(String id){
        val projectName = mgrGetter.getProjectName(id);
        createFolder(config.getString("downloadDirectory")+projectName);
        val metagenomes = mgrGetter.getProject(id);
        for(String metagenome : metagenomes){
            createFolder(config.getString("downloadDirectory")+id+"/"+metagenome);
            mgrGetter.getMetagenome(metagenome);
            mgrGetter.getProtein(metagenome);
        }       
    }
    
    public boolean createFolder(String path){
        val projectDir = new File(path);
        try{
            projectDir.mkdir();
         } catch(SecurityException se){
             log.error(se.getMessage());
             return false;
         }
         return true;
    }   
}
