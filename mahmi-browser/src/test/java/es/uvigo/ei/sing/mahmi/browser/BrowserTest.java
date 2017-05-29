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
//    	try (PrintWriter pw1 = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/tempfiles/rnaseq/fmpk-dentriticas.csv")))){
//			List<String> lines= Files.readAllLines(Paths.get("/home/mahmi/rna_seq/sf_data/dentriticasv2/1a/t_data.ctab"), Charset.defaultCharset());		
//			final Set<P2<String, String>> list = new HashSet<>();
//			final Map<String, Double> m1a = new HashMap<String, Double>();
//			final Map<String, Double> m1b = new HashMap<String, Double>();
//			final Map<String, Double> m3a = new HashMap<String, Double>();
//			final Map<String, Double> m3b = new HashMap<String, Double>();
//			final Map<String, Double> m4a = new HashMap<String, Double>();
//			final Map<String, Double> m4b = new HashMap<String, Double>();
//			final Map<String, Double> m5a = new HashMap<String, Double>();
//			final Map<String, Double> m5b = new HashMap<String, Double>();
//			final Map<String, Double> m6a = new HashMap<String, Double>();
//			final Map<String, Double> m6b = new HashMap<String, Double>();
//			final Map<String, Double> m7a = new HashMap<String, Double>();
//			final Map<String, Double> m7b = new HashMap<String, Double>();
//			final Map<String, Double> m8a = new HashMap<String, Double>();
//			final Map<String, Double> m8b = new HashMap<String, Double>();
//			for(int i=1; i<lines.size(); i++){
//				final String[] line = lines.get(i).split("\t");
//				list.add(p(line[5], line[9]));
//				m1a.put(line[5], Double.parseDouble(line[11]));
//			}			
//			lines = Files.readAllLines(Paths.get("/home/mahmi/rna_seq/sf_data/dentriticasv2/1b/t_data.ctab"), Charset.defaultCharset());
//			for(int i=1; i<lines.size(); i++){
//				final String[] line = lines.get(i).split("\t");
//				list.add(p(line[5], line[9]));
//				m1b.put(line[5], Double.parseDouble(line[11]));
//			}				
//			lines = Files.readAllLines(Paths.get("/home/mahmi/rna_seq/sf_data/dentriticasv2/3a/t_data.ctab"), Charset.defaultCharset());
//			for(int i=1; i<lines.size(); i++){
//				final String[] line = lines.get(i).split("\t");
//				list.add(p(line[5], line[9]));
//				m3a.put(line[5], Double.parseDouble(line[11]));
//			}			
//			lines = Files.readAllLines(Paths.get("/home/mahmi/rna_seq/sf_data/dentriticasv2/3b/t_data.ctab"), Charset.defaultCharset());
//			for(int i=1; i<lines.size(); i++){
//				final String[] line = lines.get(i).split("\t");
//				list.add(p(line[5], line[9]));
//				m3b.put(line[5], Double.parseDouble(line[11]));
//			}				
//			lines = Files.readAllLines(Paths.get("/home/mahmi/rna_seq/sf_data/dentriticasv2/4a/t_data.ctab"), Charset.defaultCharset());
//			for(int i=1; i<lines.size(); i++){
//				final String[] line = lines.get(i).split("\t");
//				list.add(p(line[5], line[9]));
//				m4a.put(line[5], Double.parseDouble(line[11]));
//			}			
//			lines = Files.readAllLines(Paths.get("/home/mahmi/rna_seq/sf_data/dentriticasv2/4b/t_data.ctab"), Charset.defaultCharset());
//			for(int i=1; i<lines.size(); i++){
//				final String[] line = lines.get(i).split("\t");
//				list.add(p(line[5], line[9]));
//				m4b.put(line[5], Double.parseDouble(line[11]));
//			}			
//			lines = Files.readAllLines(Paths.get("/home/mahmi/rna_seq/sf_data/dentriticasv2/5a/t_data.ctab"), Charset.defaultCharset());
//			for(int i=1; i<lines.size(); i++){
//				final String[] line = lines.get(i).split("\t");
//				list.add(p(line[5], line[9]));
//				m5a.put(line[5], Double.parseDouble(line[11]));
//			}			
//			lines = Files.readAllLines(Paths.get("/home/mahmi/rna_seq/sf_data/dentriticasv2/5b/t_data.ctab"), Charset.defaultCharset());
//			for(int i=1; i<lines.size(); i++){
//				final String[] line = lines.get(i).split("\t");
//				list.add(p(line[5], line[9]));
//				m5b.put(line[5], Double.parseDouble(line[11]));
//			}			
//			lines = Files.readAllLines(Paths.get("/home/mahmi/rna_seq/sf_data/dentriticasv2/6a/t_data.ctab"), Charset.defaultCharset());
//			for(int i=1; i<lines.size(); i++){
//				final String[] line = lines.get(i).split("\t");
//				list.add(p(line[5], line[9]));
//				m6a.put(line[5], Double.parseDouble(line[11]));
//			}			
//			lines = Files.readAllLines(Paths.get("/home/mahmi/rna_seq/sf_data/dentriticasv2/6b/t_data.ctab"), Charset.defaultCharset());
//			for(int i=1; i<lines.size(); i++){
//				final String[] line = lines.get(i).split("\t");
//				list.add(p(line[5], line[9]));
//				m6b.put(line[5], Double.parseDouble(line[11]));
//			}			
//			lines = Files.readAllLines(Paths.get("/home/mahmi/rna_seq/sf_data/dentriticasv2/7a/t_data.ctab"), Charset.defaultCharset());
//			for(int i=1; i<lines.size(); i++){
//				final String[] line = lines.get(i).split("\t");
//				list.add(p(line[5], line[9]));
//				m7a.put(line[5], Double.parseDouble(line[11]));
//			}			
//			lines = Files.readAllLines(Paths.get("/home/mahmi/rna_seq/sf_data/dentriticasv2/7b/t_data.ctab"), Charset.defaultCharset());
//			for(int i=1; i<lines.size(); i++){
//				final String[] line = lines.get(i).split("\t");
//				list.add(p(line[5], line[9]));
//				m7b.put(line[5], Double.parseDouble(line[11]));
//			}			
//			lines = Files.readAllLines(Paths.get("/home/mahmi/rna_seq/sf_data/dentriticasv2/8a/t_data.ctab"), Charset.defaultCharset());
//			for(int i=1; i<lines.size(); i++){
//				final String[] line = lines.get(i).split("\t");
//				list.add(p(line[5], line[9]));
//				m8a.put(line[5], Double.parseDouble(line[11]));
//			}			
//			lines = Files.readAllLines(Paths.get("/home/mahmi/rna_seq/sf_data/dentriticasv2/8b/t_data.ctab"), Charset.defaultCharset());
//			for(int i=1; i<lines.size(); i++){
//				final String[] line = lines.get(i).split("\t");
//				list.add(p(line[5], line[9]));
//				m8b.put(line[5], Double.parseDouble(line[11]));
//			}
//			
//			
//			Iterator<P2<String, String>> iterator = list.iterator();
//			
//			while(iterator.hasNext()){
//				P2<String, String> x = iterator.next();
//				double v1a = 0.000000;
//				if(m1a.get(x._1())!=null) v1a = m1a.get(x._1());
//				double v1b = 0.000000;
//				if(m1b.get(x._1())!=null) v1b = m1b.get(x._1());
//				double v3a = 0.000000;
//				if(m3a.get(x._1())!=null) v3a = m3a.get(x._1());
//				double v3b = 0.000000;
//				if(m3b.get(x._1())!=null) v3b = m3b.get(x._1());
//				double v4a = 0.000000;
//				if(m4a.get(x._1())!=null) v4a = m4a.get(x._1());
//				double v4b = 0.000000;
//				if(m4b.get(x._1())!=null) v4b = m4b.get(x._1());
//				double v5a = 0.000000;
//				if(m5a.get(x._1())!=null) v5a = m5a.get(x._1());
//				double v5b = 0.000000;
//				if(m5b.get(x._1())!=null) v5b = m5b.get(x._1());
//				double v6a = 0.000000;
//				if(m6a.get(x._1())!=null) v6a = m6a.get(x._1());
//				double v6b = 0.000000;
//				if(m6b.get(x._1())!=null) v6b = m6b.get(x._1());
//				double v7a = 0.000000;
//				if(m7a.get(x._1())!=null) v7a = m7a.get(x._1());
//				double v7b = 0.000000;
//				if(m7b.get(x._1())!=null) v7b = m7b.get(x._1());
//				double v8a = 0.000000;
//				if(m8a.get(x._1())!=null) v8a = m8a.get(x._1());
//				double v8b = 0.000000;
//				if(m8b.get(x._1())!=null) v8b = m8b.get(x._1());
//					
//				
//				pw1.println(x._1()+";"+x._2()+";"+v1a+";"+v1b+";"+v3a+";"+v3b+";"+v4a+";"+v4b+";"+v5a+";"+v5b+";"+v6a+";"+v6b+";"+v7a+";"+v7b+";"+v8a+";"+v8b);
//			}
//    	try (PrintWriter pw1 = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/tempfiles/rnaseq/fmpk.csv")))){
//			List<String> lines= Files.readAllLines(Paths.get("/home/mahmi/rna_seq/sf_data/testtest/md1_a/t_data.ctab"), Charset.defaultCharset());		
//			final Set<P2<String, String>> list = new HashSet<>();
//			final Map<String, Double> m1a = new HashMap<String, Double>();
//			final Map<String, Double> m1b = new HashMap<String, Double>();
//			final Map<String, Double> m2a = new HashMap<String, Double>();
//			final Map<String, Double> m2b = new HashMap<String, Double>();
//			for(int i=1; i<lines.size(); i++){
//				final String[] line = lines.get(i).split("\t");
//				list.add(p(line[5], line[9]));
//				m1a.put(line[5], Double.parseDouble(line[11]));
//			}			
//			lines = Files.readAllLines(Paths.get("/home/mahmi/rna_seq/sf_data/testtest/md1_b/t_data.ctab"), Charset.defaultCharset());
//			for(int i=1; i<lines.size(); i++){
//				final String[] line = lines.get(i).split("\t");
//				list.add(p(line[5], line[9]));
//				m1b.put(line[5], Double.parseDouble(line[11]));
//			}				
//			lines = Files.readAllLines(Paths.get("/home/mahmi/rna_seq/sf_data/testtest/md2_a/t_data.ctab"), Charset.defaultCharset());
//			for(int i=1; i<lines.size(); i++){
//				final String[] line = lines.get(i).split("\t");
//				list.add(p(line[5], line[9]));
//				m2a.put(line[5], Double.parseDouble(line[11]));
//			}			
//			lines = Files.readAllLines(Paths.get("/home/mahmi/rna_seq/sf_data/testtest/md2_b/t_data.ctab"), Charset.defaultCharset());
//			for(int i=1; i<lines.size(); i++){
//				final String[] line = lines.get(i).split("\t");
//				list.add(p(line[5], line[9]));
//				m2b.put(line[5], Double.parseDouble(line[11]));
//			}	
//			
//			
//			Iterator<P2<String, String>> iterator = list.iterator();
//			
//			while(iterator.hasNext()){
//				P2<String, String> x = iterator.next();
//				double v1a = 0.000000;
//				if(m1a.get(x._1())!=null) v1a = m1a.get(x._1());
//				double v1b = 0.000000;
//				if(m1b.get(x._1())!=null) v1b = m1b.get(x._1());
//				double v2a = 0.000000;
//				if(m2a.get(x._1())!=null) v2a = m2a.get(x._1());
//				double v2b = 0.000000;
//				if(m2b.get(x._1())!=null) v2b = m2b.get(x._1());
//					
//				
//				pw1.println(x._1()+";"+x._2()+";"+v1a+";"+v1b+";"+v2a+";"+v2b);
//			}
//			
//			
//			
//    	}catch (final IOException e1) {
//          e1.printStackTrace();;
//    	}	
    	
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
			
//		}catch (final IOException e1) {
//            e1.printStackTrace();;
//        }
//    	
    	
//    	try {
//			final List<String> peptides = Files.readAllLines(Paths.get("/home/mahmi/tempfiles/uniprot/peptides1.csv"), Charset.defaultCharset());			
//						
//			try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/tempfiles/uniprot/mhproteins.18.fasta")))) {
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
//    	try {
//			final List<String> peptides = Files.readAllLines(Paths.get("/home/mahmi/tempfiles/uniprot/peptides2.csv"), Charset.defaultCharset());			
//						
//			try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/tempfiles/uniprot/mhproteins.19.fasta")))) {
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
//    	try {
//			final List<String> peptides = Files.readAllLines(Paths.get("/home/mahmi/tempfiles/uniprot/peptides3.csv"), Charset.defaultCharset());			
//						
//			try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/tempfiles/uniprot/mhproteins.20.fasta")))) {
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

    	
//    	try {
//			final List<String> peptides = Files.readAllLines(Paths.get("/home/mahmi/tempfiles/uniprot/peptides4.csv"), Charset.defaultCharset());			
//						
//			try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/tempfiles/uniprot/mhproteins.21.fasta")))) {
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
    	
    	
    	try ( PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("/home/mahmi/tempfiles/uniprot/mhproteins.21.sql", false)))){	    	
	    	InputStream in = new FileInputStream(new File("/home/mahmi/tempfiles/uniprot/mhproteins.21.out"));
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	        String line;
	        while ((line = reader.readLine()) != null) {
	            if(line.startsWith("Query=")){
	            	String nextLine = reader.readLine();
	            	boolean nohits = false;
	            	while(!nextLine.startsWith(">")){
	            		nextLine = reader.readLine();
	            		if(nextLine.startsWith("*****")){
	            			nohits = true;
	            			break;
	            		}
	            	}
	            	if(!nohits){
		            	String indv = nextLine;
		            	nextLine = reader.readLine();
		            	while(!nextLine.startsWith("Length=")){
		            		indv+=nextLine;
		            		nextLine = reader.readLine();
		            	}
		            	String prot_id = line.substring(7);
		            	String description = indv.substring(1);
		            	String info = description.split("[|]")[2];
		            	String uniprot_id = description.split("[|]")[1];
		            	String uniprot_protein = "";
		            	String uniprot_organism = "";
		            	String uniprot_gene = "";
		            	try{
		            		uniprot_protein = info.split("OS=")[0].replace("'", "");
		            		uniprot_protein = uniprot_protein.substring(0, uniprot_protein.length()-1);
		            	}catch(Exception e){ }
		            	try{
		            		uniprot_gene = info.split("OS=")[1].split("GN=")[1].split("PE=")[0].replace("'", "");
		            		uniprot_gene = uniprot_gene.substring(0, uniprot_gene.length()-1);
		            	}catch(Exception e){ }
		            	try{
		            		uniprot_organism = info.split("OS=")[1].split("GN=")[0].replace("'", "");
		            		uniprot_organism = uniprot_organism.substring(0, uniprot_organism.length()-1);
		            	}catch(Exception e){ }
		            	String sql = "INSERT INTO protein_information (protein_id, uniprot_id, uniprot_organism, uniprot_protein, uniprot_gene) VALUES "
		            			+ "("+prot_id+",'"+uniprot_id+"', '"+uniprot_organism+"', '"+uniprot_protein+"', '"+uniprot_gene+"');";
//		            	System.out.println(sql);
		            	pw.println(sql);
	            	}
	            }
	        }  //Prints the string content read from input stream
	        reader.close();

        }catch(IOException e){
        	e.printStackTrace();
        }    	
    	
//    	try ( PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("/home/mahmi/tempfiles/insertChemprotTrain.sql", false)))){	    	
//	    	InputStream in = new FileInputStream(new File("/home/mahmi/Downloads/chemprot_train_abstracts.tsv"));
//	        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//	        String line;
//	        while ((line = reader.readLine()) != null) {
//	            String[] fields = line.split("\t");
//	            pw.println("insert ignore into abstracts (abstract_external_id, abstract_title, abstract_text) values ('"+fields[0]+"', '"+fields[1].replaceAll("\"", "").replaceAll("'", "")+"', '"+fields[2].replaceAll("\"", "").replaceAll("'", "")+"');");	            	
//	            
//	        }  //Prints the string content read from input stream
//	        reader.close();
//
//        }catch(IOException e){
//        	e.printStackTrace();
//        }  
//    	
//    	
//        assertTrue( true );
    }
}
