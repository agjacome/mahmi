package es.uvigo.ei.sing.mahmi.loader.mgrast;

import java.io.File;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@Slf4j
public class MGRastProjectsDownloader {
        
    final Config config          = ConfigFactory.load("downloader");
    final MGRastGetter mgrGetter = new MGRastGetter(); 
    
    public boolean downloadProject(String id){
        if(mgrGetter.existsProject(id)){
            val projectName = mgrGetter.getProjectName(id);
            createFolder(config.getString("downloadDirectory")+projectName);
            val metagenomes = mgrGetter.getProjectMetagenomes(id);
            for(String metagenome : metagenomes){
                val directory = config.getString("downloadDirectory")+id+"/"+metagenome;
                createFolder(directory);
                mgrGetter.getMetagenome(metagenome, directory);
                mgrGetter.getProtein(metagenome, directory);
            }   
            return true;
        }else{
            log.error("Error. Try to download a project does not exists");
            return false;
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
