package es.uvigo.ei.sing.mahmi.mgrast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MGRastGetter {
    public String getProjectName(String id){
        //get http://api.metagenomics.anl.gov/1/project/ID?verbosity=full
        return "";
    }
    
    public List<String> getProject(String id){
        //get http://api.metagenomics.anl.gov/1/project/ID?verbosity=full
        val metagenomes = new ArrayList<String>();
        
        return metagenomes;
    }
    
    public void getMetagenome(String id){
        //get [data]{0}{stadistics}.url
        getProtein(id);
    }
    
    public void getProtein(String id){
        //get http://api.metagenomics.anl.gov/1/download/ID?file=350.1
        //or get [data]{6}{stadistics}.url
    }
    
    public String get(URL url) {
        val conn = createConnection(url);
        String line;
        String result = ""; 
        if(conn!=null){
            try (val rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
               while ((line = rd.readLine()) != null) {
                  result += line;
               }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }else result = "Connection error";
        return result;
     }
    
    public HttpURLConnection createConnection(URL url){
        final HttpURLConnection conn;
        try{
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            return conn;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }        
    }
}
