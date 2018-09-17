package es.uvigo.ei.sing.mahmi.browser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	
	private String getGene(final String header){
		switch(header){
		case "FM180568.1_prot_CAS07957.1_409":
			return "adk";
		case "FM180568.1_prot_CAS08825.1_1277":
			return "icd";
		case "FM180568.1_prot_CAS09244.1_1696":
			return "fumC";
		case "FM180568.1_prot_CAS10510.1_2962":
			return "recA";
		case "FM180568.1_prot_CAS11055.1_3507":
			return "mdh";			
		case "FM180568.1_prot_CAS11558.1_4010":
			return "gyrB";
		case "FM180568.1_prot_CAS12048.1_4500":
			return "purA";
		default:
			return "unk";
		}
	}
	
	private void getNeuropep(){
		try(PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/tempfiles/neuropep.csv")))){
			pw.println("id; sequence; name; organism; family; uniprotID");
			for(int i=1; i<=5949; i++){
				String num = ""+i;
				int zeros = 4-num.length();
				for(int j=0; j<zeros; j++){
					num="0"+num;
				}
				URL url = new URL("http://isyslab.info/NeuroPep/search_info?pepNum=NP0"+num);
				URLConnection con = url.openConnection();
				InputStream in = con.getInputStream();
				String encoding = con.getContentEncoding();  
				encoding = encoding == null ? "UTF-8" : encoding;
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buf = new byte[8192];
				int len = 0;
				while ((len = in.read(buf)) != -1) {
				    baos.write(buf, 0, len);
				}
				String body = new String(baos.toByteArray(), encoding);
				String uniprot = body.split("class=\"title\">UniProt ID</td>")[1].split("</td>")[0].split(">")[1];
				if(!uniprot.contains("\tNA")){
					uniprot = uniprot.split("/entry/")[1].split("'")[0];
				}else{
					uniprot = "NA";
				}
				
				pw.println("NP0"+num+"; "+
						   body.split("toPropeties")[2].split("input1.attr\\(\"value")[1].split(",")[1].split("\\)")[0].replaceAll("'", "")+"; "+
						   body.split("class=\"title\">Name</td>")[1].split("</td>")[0].split(">")[1]+"; "+
						   body.split("class=\"title\">Organism</td>")[1].split("</td>")[0].split(">")[1].replaceAll("\\s*$","")+"; "+
						   body.split("class=\"title\">Family</td>")[1].split("</td>")[0].split(">")[1].replaceAll("\\s*$","")+"; "+
						   uniprot
						   );
				
//				pw.println(">NP0"+num+" "+body.split("class=\"title\">Name</td>")[1].split("</td>")[0].split(">")[1]+" ["
//				+body.split("class=\"title\">Organism</td>")[1].split("</td>")[0].split(">")[1].replaceAll("\\s*$","")+"]");
//				pw.println(body.split("toPropeties")[2].split("input1.attr\\(\"value")[1].split(",")[1].split("\\)")[0].replaceAll("'", ""));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void separateNeuropepHomoSapiens(){
		try(PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/tempfiles/neuropep_others.faa")))){
			try(PrintWriter pwh = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/tempfiles/neuropep_hs.faa")))){
				final List<String> neuropeps = Files.readAllLines(Paths.get("/home/mahmi/tempfiles/neuropep.faa"));
				boolean homo=false;
				for(int i =0; i<neuropeps.size(); i++){
					String n = neuropeps.get(i);
					if(n.startsWith(">")){
						if(n.contains("Homo sapiens") || n.contains("Homo Sapiens")){
						    homo=true;
						    pwh.println(n);
						}else{
							homo=false;
						    pw.println(n);
						}
					}else{
						if(homo)
						    pwh.println(n);
						else
						    pw.println(n);
					}
				}
			}catch(Exception eh){
				eh.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void mergeColibactineClusters(){
		File folder = new File("/home/mahmi/Dataset/Escherichia_coli/colibactina/6000Patric/clusterCluster");
		File[] listOfFiles = folder.listFiles();
		try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/Escherichia_coli/colibactina/6000Patric/clusterCluster/clusterOForClustal.faa")))) {
			for (int i = 0; i < listOfFiles.length; i++){
				final List<String> lines = Files.readAllLines(Paths.get(listOfFiles[i].getPath()));
				pw.println(">"+listOfFiles[i].getName().replace(".faa", "").replaceAll("\\(","_").replaceAll("\\)","_").replaceAll(":","_"));
				lines.forEach(l ->{
					if(!l.startsWith(">"))
						pw.println(l);
				});
			}
		}catch(Exception e){
			e.printStackTrace();	
		}
	}
	
	public void getEverPeptides(){
		BufferedReader fastaReader = null;
		try(PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/blast_db/colibactineEverPeptides.faa")))){
			fastaReader = new BufferedReader(new FileReader("/home/mahmi/blast_db/colibactineEverProteins.faa"));
		    String fastaAux = "";	    	
		    fastaReader.readLine();
		    //Get fasta sequences
		    final Map<String, String> fastaSequences = new HashMap<>();
		    String header = "";
		    String p = "";
		    while ((p = fastaReader.readLine()) != null) {
		    	if(p.startsWith(">")){
		    		if(!header.equals("")){
			    		fastaSequences.put(header, fastaAux);
			    		fastaAux = "";		    			
		    		}
			    	header=p.split(" ")[0].substring(1);
		    	}else{
		    		fastaAux=fastaAux+p;
		    	}
		    }		    
		    fastaSequences.put(header,fastaAux);
			fastaSequences.forEach((k,v) ->{
				
				int size = v.length();
				int rangos = size/20;
				for(int i=0; i<rangos; i++){
					pw.println(k+"."+i+" peptide");
					pw.println(v.substring(i*20,(i*20)+20));
				}
				pw.println(k+"."+rangos+" peptide");
				pw.println(v.substring(rangos*20, v.length()));
				//LLAIVLEMRITGVYFGAMSGTFVAALLAWRLLRLSPRL
			});
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String nuro(List<String> a, String b){
		String c = "";
		for(int i=0;i<a.size();i++){
			if(a.get(i).contains(b))
				return a.get(i);
		}
		return c;
	}
	
	private String bftName(String id){
		switch(id){
			case "Q9S5W0": return "BFT-1";
			case "O05091": return "BFT-2";
			case "O86049": return "BFT-3";
			default: return "BFT";	
		}
	}
	
    public void testApp(){ 
    	getNeuropep();
//    	getEverPeptides();
//    	mergeColibactineClusters();
//    	separateNeuropepHomoSapiens();
//    	System.out.println("init");
//    	try{
//    		List<String> all=Files.readAllLines(Paths.get("/home/mahmi/Dataset/Escherichia_coli/colibactina/6000Patric/strains_with_cluster_complete_all.txt"));
//    		List<String> filtered=Files.readAllLines(Paths.get("/home/mahmi/Dataset/Escherichia_coli/colibactina/6000Patric/strains_with_cluster_complete_filtered.txt"));
//    		all.forEach(l -> {
//    			if(!filtered.contains(l))
//    				System.out.println(l.replace(".faa",""));
//    		});
//    	}catch(Exception e){;
//    		e.printStackTrace();
//    	}
//    	System.out.println("end");
//    {
//    	try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/tempfiles/neurop.txt")))) {
//			List<String> a = Files.readAllLines(Paths.get("/home/mahmi/tempfiles/neuro.txt"));
//			for(int i=0; i<a.size(); i++){
//				if(a.get(i).startsWith("peptide_id")){
//					pw.println(">"+a.get(i+1).split("\t")[0]);
//					pw.println(a.get(i+1).split("\t")[1]);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    	
//    	try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("//home/mahmi/Dataset/PaperSerpent/Human Faeces Kurokawa/In-D.json")))) {
//			List<String> gh = Files.readAllLines(Paths.get("/home/mahmi/Dataset/PaperSerpent/Human Faeces Kurokawa/mecA.faa"));
//			List<String> faa = Files.readAllLines(Paths.get("/home/mahmi/Dataset/PaperSerpent/Human Faeces Kurokawa/In-D_Male_35y_4440948.3_2.faa"));
//			
//			pw.println("{");
//			pw.print("\"reference\": \"");
//			gh.forEach(l -> pw.print(l+"\\n"));
//			pw.println("\",");
//			pw.print("\"comparing\": \"");
//			faa.forEach(l -> pw.print(l+"\\n"));
//			pw.println("\",");
//			pw.println("\"threshold\": 50.0,");
//			pw.println("\"annotations\": {}");
//			pw.println("}");	
//		
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
    	
//    	File folder = new File("/home/mahmi/Dataset/BacteroidesFragilis/Blast");
//		File[] listOfFiles = folder.listFiles();
//		try (PrintWriter pw1 = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/BacteroidesFragilis/results_bft.csv")))) {
//		try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/BacteroidesFragilis/results_filtered.csv")))) {
//			for (int i = 0; i < listOfFiles.length; i++){
//				String name = "N/A";
//				Double percentage = 0.0;
//				List<String> n = new ArrayList<>();
//				List<Double> p = new ArrayList<>();
//				List<String> a = Files.readAllLines(Paths.get(listOfFiles[i].getAbsolutePath()));
//				for(int j=0; j< a.size(); j++){
//					if(Double.parseDouble(a.get(j).split("\t")[2])>percentage){
//						name = this.bftName(a.get(j).split("\t")[1]);
//						percentage = Double.parseDouble(a.get(j).split("\t")[2]);
//					}
//					if(Double.parseDouble(a.get(j).split("\t")[2])>50.0){
//						n.add(this.bftName(a.get(j).split("\t")[1]));
//						p.add(Double.parseDouble(a.get(j).split("\t")[2]));
//					}
//				}
//				
//				if(percentage>50.0)
//					pw.println(listOfFiles[i].getName().split(".faa")[0]+"\t"+name+"\t"+percentage);
//				if(n.size()>0){
//					pw1.print(listOfFiles[i].getName().split(".faa")[0]);
//					for(int j=0; j<n.size(); j++){
//						pw1.print("\t\t"+n.get(j)+"\t"+p.get(j));
//					}
//					pw1.println("");
//				}
//				
//					
//			}
//		
//		}catch (Exception e) {
//			e.printStackTrace();
//		}}catch (Exception e) {
//			e.printStackTrace();
//		}
			
    	
//    	try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/tempfiles/Escherichia_coli_CFT073_10000.faa")))) {
//    		int x= 2;
//    		int n = 10000/x;
//    		for(int c=0; c<x;c++){
//    			List<String> a = Files.readAllLines(Paths.get("/home/mahmi/tempfiles/Escherichia_coli_CFT073.AE014075.1.faa"));
//				int counter =0;
//				
//				for(int i=0; i<a.size(); i++){
//					if(counter>n){
//						break;
//					}
//					if(a.get(i).startsWith(">"))	
//						counter++;
//					
//					if(counter<=n)
//						pw.println(a.get(i));
//				}
//    		}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
    	
    	
    	
//    	File folder = new File("/home/mahmi/Dataset/Escherichia_coli/colibactina/6000Patric/Fault/Blast-MLST");
//		File[] listOfFiles = folder.listFiles();
//		for (int i = 0; i < listOfFiles.length; i++){
//			try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/Escherichia_coli/colibactina/6000Patric/Fault/MLST/"+listOfFiles[i].getName())))) {
//				final List<String> lines = Files.readAllLines(Paths.get(listOfFiles[i].getPath()));
//				BufferedReader fastaReader = new BufferedReader(new FileReader("/home/mahmi/Dataset/Escherichia_coli/colibactina/6000Patric/Strains/"+listOfFiles[i].getName()));
//			    String p;
//			    String fastaAux = "";	    	
//			    fastaReader.readLine();
//			    //Get fasta sequences
//			    final Map<String, String> fastaSequences = new HashMap<>();
//			    String header = "";
//			    int counter=0;
//			    while ((p = fastaReader.readLine()) != null) {
//			    	if(p.startsWith(">")){
//			    		if(!header.equals("")){
//				    		fastaSequences.put(header, fastaAux);
//				    		fastaAux = "";		    			
//			    		}
//				    	header=p.split(" ")[0].substring(1);
//			    	}else{
//			    		fastaAux=fastaAux+p;
//			    	}
//			    }		    
//			    fastaSequences.put(header,fastaAux);
//			    fastaReader.close();
//			    Set<String>set = new HashSet<>();
//			    
//				for(int j=0; j<lines.size(); j++){
//					final String l = lines.get(j);
//					final double evalue = Double.parseDouble(l.split("\t")[10]);
//					final double score = Double.parseDouble(l.split("\t")[2]);
//					final String query = l.split("\t")[0];
//					final String subject = l.split("\t")[1];
//					if(evalue<1e-5 && score>90.0){
//						if(getGene(subject).equals("icd") && fastaSequences.get(query).length()<100){
//							
//						}else{
//							pw.println(">"+query+" [gene="+getGene(subject)+"]");
//							pw.println(fastaSequences.get(query));
//							set.add(getGene(subject));
//							counter++;
//							
//						}
//					}
//				}
//				if(set.size()!=7 || counter!=7)
//				System.out.println(listOfFiles[i].getName()+": "+counter);
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
    	
//    	try{
//	    	List<String> colibactia = Files.readAllLines(Paths.get("/home/mahmi/Dataset/Escherichia_coli/colibactina/6000Patric/colibactines.txt"));
//	    	List<String> colisBreak = Files.readAllLines(Paths.get("/home/mahmi/Dataset/Escherichia_coli/colibactina/6000Patric/colilines.txt"));
//	    	colibactia.forEach(l ->{
//	    		try{
//	    			Files.delete(Paths.get("/home/mahmi/Dataset/Escherichia_coli/colibactina/6000Patric/clustalo-clbs/blastClb/"+l));
//	    		}catch(Exception e1){
//	    			e1.printStackTrace();
//	    		}
//	    	});
//	    	colisBreak.forEach(l ->{
//	    		try{
//	    			if(!l.startsWith("fig"))
//	    				Files.delete(Paths.get("/home/mahmi/Dataset/Escherichia_coli/colibactina/6000Patric/clustalo-clbs/blastClb/"+l));
//	    		}catch(Exception e1){
//	    			e1.printStackTrace();
//	    		}
//	    	});
//    	}catch(Exception e){
//    		e.printStackTrace();
//    	}
//    	final Set<String>set = new HashSet<>();
//    	File folder = new File("/home/mahmi/Dataset/Escherichia_coli/colibactina/6000Patric/clustalo-clbs/blastClb");
//		File[] listOfFiles = folder.listFiles();
//		for (int i = 0; i < listOfFiles.length; i++){
//			try{
//				List<String> x = Files.readAllLines(Paths.get(listOfFiles[i].getAbsolutePath()));
//				x.forEach(l -> set.add(l.split("\t")[1]));
//			}catch(Exception e){
//	    		e.printStackTrace();
//	    	}
//		}
//		set.forEach(l -> System.out.println(l));
    	
//    	List<String> names;
//    	List<String> colibactine;
//    	boolean jeje = false;
//    	int counter = 0;
//    	try {
//			names = Files.readAllLines(Paths.get("/home/mahmi/Dataset/Escherichia_coli/colibactina/6000Patric/genomes.csv"));
//			colibactine = Files.readAllLines(Paths.get("/home/mahmi/Dataset/Escherichia_coli/colibactina/6000Patric/colibactines.txt"));
//	    	for(int i=0; i<colibactine.size(); i++){
//	    		String number = "";	    		
//	    		for(int j=1; j<names.size(); j++){
//	    			final String line = names.get(j);
//		    		if(line.split(",")[1].replaceAll("\"", "").replace("/","_").equals(colibactine.get(i).replace(".faa",""))){
//		    			if(jeje){
//			    			System.out.println(colibactine.get(i));
//			    		}
//		    			number = line.split(",")[0].replaceAll("\"", "");
//		    			counter++;
//		    			jeje=true;
//		    		}		    		
//	    		}
//	    		List<String> lines = Files.readAllLines(Paths.get("/home/mahmi/Dataset/Escherichia_coli/samples/6000PatrickHomoSapiens/Proteins/"+colibactine.get(i)));
//	    		try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/Escherichia_coli/colibactina/6000Patric/Strains/"+colibactine.get(i).replace(".faa","")+"-"+number+".faa")))) {
//	    			lines.forEach(l-> pw.println(l));
//	    		} catch (Exception e) {
//					e.printStackTrace();
//				}
//	    		jeje=false;
////	    		System.out.println(colibactine.get(i).replace(".faa","")+"-"+number+".faa");
////	    		final String line = names.get(i);
////	    		final String number = line.split(",")[0].replaceAll("\"", "");
////	    		final String name = line.split(",")[1].replaceAll("\"", "").replace("/","_");
////	    		try {
////	    		final List<String> strain = Files.readAllLines(Paths.get("/home/mahmi/Dataset/Escherichia_coli/samples/6000PatrickHomoSapiens/Proteins/"+line));
////	    		try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/Escherichia_coli/samples/6000PatrickHomoSapiens/Genomes/00/"+name+".faa")))) {
////					strain.forEach(s -> pw.println(s));
////	        		Files.delete(Paths.get("/home/mahmi/Dataset/Escherichia_coli/samples/6000PatrickHomoSapiens/Genomes/00/"+number+"/"+number+".PATRIC.faa"));
////	        		Files.delete(Paths.get("/home/mahmi/Dataset/Escherichia_coli/samples/6000PatrickHomoSapiens/Genomes/00/"+number));
////				} catch (Exception e) {
////					System.out.println(number+": "+name);
//////					e.printStackTrace();
////				}
////	    		} catch (Exception e) {
////					System.out.println(number+": "+name);
//////					e.printStackTrace();
////				}
//	    	}
//		} catch (IOException e1) { 
//			e1.printStackTrace();
//		}System.out.println(counter);
    	
    	
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
    	
    	
//    	List<String> lista = new ArrayList<>();
//    	lista.add("/vol1/fastq/ERR188/ERR188245/ERR188245_1.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188245/ERR188245_2.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188428/ERR188428_1.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188428/ERR188428_2.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188337/ERR188337_1.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188337/ERR188337_2.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188401/ERR188401_1.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188401/ERR188401_2.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188257/ERR188257_1.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188257/ERR188257_2.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188383/ERR188383_1.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188383/ERR188383_2.fastq.gz");
//    	lista.add("/vol1/fastq/ERR204/ERR204916/ERR204916_1.fastq.gz");
//    	lista.add("/vol1/fastq/ERR204/ERR204916/ERR204916_2.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188234/ERR188234_1.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188234/ERR188234_2.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188273/ERR188273_1.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188273/ERR188273_2.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188454/ERR188454_1.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188454/ERR188454_2.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188104/ERR188104_1.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188104/ERR188104_2.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188044/ERR188044_1.fastq.gz");
//    	lista.add("/vol1/fastq/ERR188/ERR188044/ERR188044_2.fastq.gz");
//    	
//        FTPClient ftpClient = new FTPClient();
//        try {
// 
//        	ftpClient.connect("ftp.sra.ebi.ac.uk");
//        	ftpClient.login("anonymous", "noreplyp4p@gmail.com");
//        	ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//            for(int i = 0; i< lista.size(); i++){
//            	String filename= lista.get(i).split("/")[lista.get(i).split("/").length-1];
//	            File downloadFile1 = new File("/home/mahmi/Downloads/hg38/"+filename);
//	            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
//	            boolean success = ftpClient.retrieveFile(lista.get(i), outputStream1);
//	            outputStream1.close();	 
//	            if (success) {
//	                System.out.println("File "+filename+" has been downloaded successfully.");
//	            }
//            } 
//        } catch (IOException ex) {
//            System.out.println("Error: " + ex.getMessage());
//            ex.printStackTrace();
//        } finally {
//            try {
//                if (ftpClient.isConnected()) {
//                    ftpClient.logout();
//                    ftpClient.disconnect();
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
    
    	
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
    	
//    	FTPClient ftpClient = new FTPClient();
//    	ftpClient.connect("ftp.ncbi.nlm.nih.gov");
//    	ftpClient.login("anonymous", "noreplyp4p@gmail.com");
//    	ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//    	final String bacteriaFolder = "/genomes/archive/old_genbank/Bacteria_DRAFT/";
//    	final String downloadFolder = "/home/mahmi/Dataset/Escherichia_coli/samples/NCBI_DRAFT/";
//    	FTPFile[] files = ftpClient.listFiles(bacteriaFolder);
//    	 
//    	for (FTPFile file : files) {
//    	    String details = file.getName();
//    	    if (file.isDirectory()) {
//    	        if(details.startsWith("Escherichia_coli")){
//		    	    FTPFile[] directory = ftpClient.listFiles(bacteriaFolder+details);
//		    	    NavigableMap<Long, String> faa = new TreeMap<>();
//		    	    NavigableMap<Long, String> fna = new TreeMap<>();
//		    	    for (FTPFile f : directory) {
//		    	    	if(f.getName().endsWith(".contig.faa.tgz"))
//		    	    		faa.put(f.getSize(), f.getName());
//		    	    	if(f.getName().endsWith(".contig.fna.tgz"))
//		    	    		fna.put(f.getSize(), f.getName());
//		    	    }
//		    	    if(faa.isEmpty()||fna.isEmpty()){
//		    	    	
//		    	    }else{
//			    	    File proteins = new File(downloadFolder+"Proteins/"+details);
//			    	    proteins.mkdir();
//			    	    File genomes = new File(downloadFolder+"Genomes/"+details);
//			    	    genomes.mkdir();
//			    	    File downloadFaa = new File(downloadFolder+"Proteins/"+details+"/"+details+".contig.faa.tgz");
//			    	    OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFaa));
//			    	    ftpClient.retrieveFile(bacteriaFolder+details+"/"+faa.lastEntry().getValue(), outputStream);
//			    	    outputStream.close();
//			    	    File downloadFna = new File(downloadFolder+"Genomes/"+details+"/"+details+".contig.fna.tar.gz");
//			    	    outputStream = new BufferedOutputStream(new FileOutputStream(downloadFna));
//			    	    ftpClient.retrieveFile(bacteriaFolder+details+"/"+fna.lastEntry().getValue(), outputStream);
//			    	    outputStream.close();
//	
//			    	    Archiver archiver = ArchiverFactory.createArchiver("tar","gz");
//			    	    archiver.extract(downloadFaa, proteins);
//			    	    archiver.extract(downloadFna, genomes);
//			    	    Files.delete(Paths.get(downloadFaa.getPath()));
//			    	    Files.delete(Paths.get(downloadFna.getPath()));
//			    	    		    	    
//			    	    try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter(downloadFolder+"Proteins/"+details+".faa")))) {
//			    	    	File folder = new File(downloadFolder+"Proteins/"+details);
//			    	    	File[] listOfFiles = folder.listFiles();
//			    	    	Arrays.sort(listOfFiles);
//			    	    	for(File fi: listOfFiles){
//			    	    		List<String> fil = Files.readAllLines(Paths.get(fi.getPath()));
//			    	    		fil.forEach(l -> pw.println(l));
//			    	    		fi.delete();
//			    	    	}
//			    	    	folder.delete();
//						}catch (final IOException e) {
//				            e.printStackTrace();
//				        }
//			    	    try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter(downloadFolder+"Genomes/"+details+".fna")))) {
//			    	    	File folder = new File(downloadFolder+"Genomes/"+details);
//			    	    	File[] listOfFiles = folder.listFiles();
//			    	    	Arrays.sort(listOfFiles);
//			    	    	for(File fi: listOfFiles){
//			    	    		List<String> fil = Files.readAllLines(Paths.get(fi.getPath()));
//			    	    		fil.forEach(l -> pw.println(l));
//			    	    		fi.delete();
//			    	    	}
//			    	    	folder.delete();						
//						}catch (final IOException e) {
//				            e.printStackTrace();
//				        }
//		    	    }
//    	        }
//	        }
//    	}
//    	 
//    	ftpClient.logout();
//    	ftpClient.disconnect();
    	
    	
    	
    	
//    	try {
//			final List<String> peptides = Files.readAllLines(Paths.get("/home/mahmi/tempfiles/uniprot/mhproteins.csv"), Charset.defaultCharset());			
//						
//			try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/tempfiles/uniprot/mhproteins.fasta")))) {
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
    	
//    	String line = "";
//        String previous = "";
//        
//    	try ( PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("/home/mahmi/tempfiles/uniprot/mhproteins.sql", false)))){	    	
//	    	InputStream in = new FileInputStream(new File("/home/mahmi/tempfiles/uniprot/mhproteins.out"));
//	        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//	        while ((line = reader.readLine()) != null) {
//	            String[] ln = line.split("\t");
//	            if(!previous.equals(ln[0])){
//		        	previous = ln[0];
//	            	String prot_id = ln[0];
//	            	String uniprot_id = ln[1].split("[|]")[1];
//	            	String uniprot_protein = "";
//	            	String uniprot_organism = "";
//	            	String uniprot_gene = "";
//	            	try{
//		            	uniprot_protein = ln[1].split("[|]")[2]+" "+ln[2].split("OS=")[0].replaceAll("'", "").replaceAll("\"", "");
//		        		uniprot_protein = uniprot_protein.substring(0, uniprot_protein.length()-1);
//	            	}catch (Exception e){}
//	        		try{
//		            	uniprot_organism = ln[2].split("OS=")[1].split("GN=")[0].replaceAll("'", "").replaceAll("\"", "");
//		        		uniprot_organism = uniprot_organism.substring(0, uniprot_organism.length()-1);
//	        		}catch (Exception e){}
//	        		try{
//		            	uniprot_gene = ln[2].split("OS=")[1].split("GN=")[1].split("PE=")[0].replaceAll("'", "").replaceAll("\"", "");
//		        		uniprot_gene = uniprot_gene.substring(0, uniprot_gene.length()-1);
//	        		}catch (Exception e){}
//	
//	            	String sql = "INSERT INTO protein_information (protein_id, uniprot_id, uniprot_organism, uniprot_protein, uniprot_gene) VALUES "
//	            			+ "("+prot_id+",'"+uniprot_id+"', '"+uniprot_organism+"', '"+uniprot_protein+"', '"+uniprot_gene+"');";
//	//		            	System.out.println(sql);
//	            	pw.println(sql);
//	            }
//	        }
//	        reader.close();
//
//        }catch(IOException e){
//        	e.printStackTrace();
//        }    	
//    	
//    	try ( PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("/home/mahmi/tempfiles/mh.sql", false)))){	    	
//	    	InputStream in = new FileInputStream(new File("/home/mahmi/tempfiles/mh.out"));
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
    	
//    	try ( PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("/home/mahmi/tempfiles/insertBioID.sql", false)))){	    	
//	    	InputStream in = new FileInputStream(new File("/home/mahmi/tempfiles/documents.tsv"));
//	        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//	        String line;
//	        reader.readLine();
//	        while ((line = reader.readLine()) != null) {
//	            String[] fields = line.split("\t");
//	            pw.println("insert into bioid (bioid_external_id, bioid_title, bioid_text) values ('"+fields[0]+"', '"+fields[1].replaceAll("\"", "").replaceAll("'", "")+"', '"+fields[2].replaceAll("\"", "").replaceAll("'", "")+"');");	            	
//	            
//	        }  //Prints the string content read from input stream
//	        reader.close();
//
//        }catch(IOException e){
//        	e.printStackTrace();
//        }  
//    	
    	
        assertTrue( true );
    }
}
