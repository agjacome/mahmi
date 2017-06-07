package es.uvigo.ei.sing.mahmi.loader.mgrast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

@Deprecated
@Slf4j
public class MGRastGetter {
    
    public boolean existsProject(String id){
        try {
            val response = getJSON(new URL("http://api.metagenomics.anl.gov/1/project/"+id));
            if(response.toString().equals("{}"))
            return false;
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
            return false;
        }            
        return true;
    }
    
    public String getProjectName(String id){
        try {
            val response = getJSON(new URL("http://api.metagenomics.anl.gov/1/project/"+id));
            return response.get("name").toString();
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
        }            
        return "";
    }
    
    @SuppressWarnings("unchecked")
    public List<String> getProjectMetagenomes(String id){
        val metagenomes = new ArrayList<String>();
        try {
            val response = getJSON(new URL("http://api.metagenomics.anl.gov/1/project/"+id+"?verbosity=full"));
            val array = (JSONArray) response.get("metagenomes");            
            final Iterator<JSONArray> iterator = array.iterator();
            while (iterator.hasNext()) {
                metagenomes.add(iterator.next().get(0).toString());
            } 
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
        }
        return metagenomes;
    }
    
    public void getMetagenome(String id, String directory){
        try {
            val response = getJSON(new URL("http://api.metagenomics.anl.gov/1/download/"+id));
            val array = (JSONArray) response.get("data");
            val metagenomeData =(JSONObject) array.get(0);
            downloadFile(new URL(metagenomeData.get("url").toString()), directory+"/"+metagenomeData.get("file_name").toString());
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
        }
    }
    
    public void getProtein(String id, String directory){
        try {
            val response = getJSON(new URL("http://api.metagenomics.anl.gov/1/download/"+id));
            val array = (JSONArray) response.get("data");
            val metagenomeData =(JSONObject) array.get(0);
            downloadFile(new URL("http://api.metagenomics.anl.gov/1/download/"+id+"?file=350.1"), directory+"/"+metagenomeData.get("file_name").toString());      
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
        }
    }
    
    private JSONObject getJSON(URL url) {    
        val conn = createConnection(url);
        JSONObject response = new JSONObject();
        String line;
        String result = ""; 
        if(conn!=null){
            try (val rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
               while ((line = rd.readLine()) != null) {
                  result += line;
               }
               response = (JSONObject) JSONValue.parse(result);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return response;
     }
    
    private void downloadFile(URL url, String directory){
        try (val fos = new FileOutputStream(directory)){
            val rbc = Channels.newChannel(url.openStream());                
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            log.error(e.getMessage());
        }        
    
    }
    
    private HttpURLConnection createConnection(URL url){
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
