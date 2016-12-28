package es.uvigo.ei.sing.mahmi.browser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class BrowserTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public BrowserTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( BrowserTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws FileNotFoundException, IOException 
    {
//    	try (PrintWriter pw1 = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/tempfiles/ecoli/error.txt")))){
//			final List<String> proteomes = Files.readAllLines(Paths.get("/home/mahmi/Downloads/ecoli.txt"), Charset.defaultCharset());			
//			proteomes.forEach(x-> {		
//			try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/tempfiles/ecoli/"
//			+x.split("\t")[1].replace("/","-").replace(" ","_")+"&"+x.split("\t")[2]+"&"+x.split("\t")[3]+"["+x.split("\t")[0]+"].faa")))) {
//			      URL url = new URL("http://www.uniprot.org/uniprot/?query=proteome:"+x.split("\t")[0]+"&format=fasta&compress=no");
//			      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			      conn.setRequestMethod("GET");
//			      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			      String line;
//			      while ((line = rd.readLine()) != null) {
//			         pw.println(line);
//			      }
//			      rd.close();
//			}catch (final IOException e) {
//	            System.out.println("Error in: "+x.split("\t")[0]);
//	            pw1.println(x);
//	        }});
//		}catch (final IOException e1) {
//            e1.printStackTrace();;
//        }
    	
//    	try (PrintWriter pw1 = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Downloads/metahit_mixs.sql")))){
//			final List<String> proteomes = Files.readAllLines(Paths.get("/home/mahmi/Downloads/metahit_01.csv"), Charset.defaultCharset());			
//			proteomes.forEach(x-> {		
//			try  {
//			      URL url = new URL("https://www.ncbi.nlm.nih.gov/biosample/?term="+x.split(";")[1]+"&report=full&format=text");
//			      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			      conn.setRequestMethod("GET");
//			      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			      String line;
//			      int counter = 1;
//			      String insert="INSERT INTO metagenome_mixs (metagenome_mixs_name, metagenome_mixs_investigation_type, metagenome_mixs_project_name, metagenome_mixs_sequencing_method, metagenome_mixs_collection_date, metagenome_mixs_enviromental_package"+
//			    		  		", metagenome_mixs_latitude, metagenome_mixs_longitude, metagenome_mixs_location, metagenome_mixs_biome, metagenome_mixs_feature, metagenome_mixs_material) values (\""+x.split(";")[0]+"\", ";
//			      while ((line = rd.readLine()) != null) {
//			         if(counter>6 && counter<18){
//			        	 if(counter<17) insert += line.split("=")[1]+", ";
//			        	 else insert += line.split("=")[1]+");";
//			         }
//			         counter++;
//			      }
//			      pw1.println(insert);
//			      rd.close();
//			}catch (final Exception e) {
//				e.printStackTrace();
//	        }
//			});
//			
//			final List<String> proteomes2 = Files.readAllLines(Paths.get("/home/mahmi/Downloads/metahit_02.csv"), Charset.defaultCharset());			
//			proteomes2.forEach(x-> {		
//			try  {
//			      URL url = new URL("https://www.ncbi.nlm.nih.gov/biosample/?term="+x.split(";")[1]+"&report=full&format=text");
//			      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			      conn.setRequestMethod("GET");
//			      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			      String line;
//			      int counter = 1;
//			      String location= "\"not provided\"";
//			      while ((line = rd.readLine()) != null) {
//			         if(counter==7){
//			        	 location = line.split("=")[1];
//			         }
//			         counter++;
//			      }
//			      pw1.println("INSERT INTO metagenome_mixs (metagenome_mixs_name, metagenome_mixs_investigation_type, metagenome_mixs_project_name, metagenome_mixs_sequencing_method, metagenome_mixs_collection_date, metagenome_mixs_enviromental_package"+
//		    		  		", metagenome_mixs_latitude, metagenome_mixs_longitude, metagenome_mixs_location, metagenome_mixs_biome, metagenome_mixs_feature, metagenome_mixs_material) values (\""+x.split(";")[0]+"\", "
//		    		  		+ "\"metagenome\", \"BBGI Type 2 Diabetes study\", \"Illumina\", \"not provided\", \"human-gut\", \"not provided\", \"not provided\", "+location+", \"human-gut\", \"human-associated habitat\", \"faeces\");");
//			      rd.close();
//			}catch (final Exception e) {
//				e.printStackTrace();
//	        }
//			});
//			
//			final List<String> proteomes3 = Files.readAllLines(Paths.get("/home/mahmi/Downloads/metahit_03.csv"), Charset.defaultCharset());			
//			proteomes3.forEach(x-> {		
//		      pw1.println("INSERT INTO metagenome_mixs (metagenome_mixs_name, metagenome_mixs_investigation_type, metagenome_mixs_project_name, metagenome_mixs_sequencing_method, metagenome_mixs_collection_date, metagenome_mixs_enviromental_package"+
//	    		  		", metagenome_mixs_latitude, metagenome_mixs_longitude, metagenome_mixs_location, metagenome_mixs_biome, metagenome_mixs_feature, metagenome_mixs_material) values (\""+x.split(";")[0]+"\", "
//	    		  		+ "\"metagenome\", \"Human Microbiome Project\", \"Illumina\", \"not provided\", \"human-gut\", \"not provided\", \"not provided\", \"not provided\", \"human-gut\", \"human-associated habitat\", \"faeces\");");
//			});
//			
//			
//			
//		}catch (final IOException e1) {
//            e1.printStackTrace();;
//        }
    	
    	
//    	try {
//			final List<String> peptides = Files.readAllLines(Paths.get("/home/mahmi/tempfiles/Uniprot/peptides.csv"), Charset.defaultCharset());			
//						
//			try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/tempfiles/Uniprot/mhproteins.15.fasta")))) {
//				peptides.forEach(p -> {
//					pw.println(">"+p.split("\t")[0]);
//					pw.println(p.split("\t")[1]);
//				});
//			}catch (final IOException e) {
//	            e.printStackTrace();
//	        }
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    	
//    	
//    	
//    	try ( PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("/home/mahmi/tempfiles/Uniprot/mhproteins.14.sql", false)))){	    	
//	    	InputStream in = new FileInputStream(new File("/home/mahmi/tempfiles/Uniprot/mhproteins.14.out"));
//	        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//	        String line;
//	        while ((line = reader.readLine()) != null) {
//	            if(line.startsWith("Query=")){
//	            	String nextLine = reader.readLine();
//	            	boolean nohits = false;
//	            	while(!nextLine.startsWith(">")){
//	            		nextLine = reader.readLine();
//	            		if(nextLine.startsWith("*****")){
//	            			nohits = true;
//	            			break;
//	            		}
//	            	}
//	            	if(!nohits){
//		            	String indv = nextLine;
//		            	nextLine = reader.readLine();
//		            	while(!nextLine.startsWith("Length=")){
//		            		indv+=nextLine;
//		            		nextLine = reader.readLine();
//		            	}
//		            	String prot_id = line.substring(7);
//		            	String description = indv.substring(1);
//		            	String info = description.split("[|]")[2];
//		            	String uniprot_id = description.split("[|]")[1];
//		            	String uniprot_protein = "";
//		            	String uniprot_organism = "";
//		            	String uniprot_gene = "";
//		            	try{
//		            		uniprot_protein = info.split("OS=")[0].replace("'", "");
//		            		uniprot_protein = uniprot_protein.substring(0, uniprot_protein.length()-1);
//		            	}catch(Exception e){ }
//		            	try{
//		            		uniprot_gene = info.split("OS=")[1].split("GN=")[1].split("PE=")[0].replace("'", "");
//		            		uniprot_gene = uniprot_gene.substring(0, uniprot_gene.length()-1);
//		            	}catch(Exception e){ }
//		            	try{
//		            		uniprot_organism = info.split("OS=")[1].split("GN=")[0].replace("'", "");
//		            		uniprot_organism = uniprot_organism.substring(0, uniprot_organism.length()-1);
//		            	}catch(Exception e){ }
//		            	String sql = "INSERT INTO protein_information (protein_id, uniprot_id, uniprot_organism, uniprot_protein, uniprot_gene) VALUES "
//		            			+ "("+prot_id+",'"+uniprot_id+"', '"+uniprot_organism+"', '"+uniprot_protein+"', '"+uniprot_gene+"');";
////		            	System.out.println(sql);
//		            	pw.println(sql);
//	            	}
//	            }
//	        }  //Prints the string content read from input stream
//	        reader.close();
//
//        }catch(IOException e){
//        	e.printStackTrace();
//        }    	
//    	
//    	try ( PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("/home/mahmi/tempfiles/insert.sql", false)))){	    	
//	    	InputStream in = new FileInputStream(new File("/home/mahmi/Downloads/chemdner_patents_all.txt"));
//	        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//	        String line;
//	        while ((line = reader.readLine()) != null) {
//	            String[] fields = line.split("\t");
//	            pw.println("insert into patents (patent_external_id, patent_title, patent_abstract) values ('"+fields[0]+"', '"+fields[1].replaceAll("\"", "").replaceAll("'", "")+"', '"+fields[2].replaceAll("\"", "").replaceAll("'", "")+"');");	            	
//	            
//	        }  //Prints the string content read from input stream
//	        reader.close();
//
//        }catch(IOException e){
//        	e.printStackTrace();
//        }  
    	
    	
        assertTrue( true );
    }
}
