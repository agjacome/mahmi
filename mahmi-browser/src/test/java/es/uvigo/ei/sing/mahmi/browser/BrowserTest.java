package es.uvigo.ei.sing.mahmi.browser;

import static java.lang.Math.abs;
import static org.apache.commons.math3.special.Erf.erf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.geometry.euclidean.twod.Line;

import fj.P;
import fj.P2;
import fj.P4;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;

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
	
	public class TreeNode<T> implements Iterable<TreeNode<T>> {

	    T data;
	    TreeNode<T> parent;
	    List<TreeNode<T>> children;

	    public TreeNode(T data) {
	        this.data = data;
	        this.children = new LinkedList<TreeNode<T>>();
	    }

	    public TreeNode<T> addChild(T child) {
	        TreeNode<T> childNode = new TreeNode<T>(child);
	        childNode.parent = this;
	        this.children.add(childNode);
	        return childNode;
	    }

		@Override
		public Iterator<TreeNode<T>> iterator() {
			// TODO Auto-generated method stub
			return null;
		}

	    // other features ...

	}
	
	private void getMostProbablyTree(){
		final String t = "(Escherichia_fergusonii_ATCC_35469_CU928158_2:11.0,((Escherichia_coli_0127_H6_E234869_FM180568_1:6.0,"
				+ "(Escherichia_coli_ED1a_NC_011745_1:5.0,((Escherichia_coli_536_NC_008253_1:3.0,"
				+ "((Escherichia_coli_S88_CU928161_2:1.0,Escherichia_coli_APEC_O1_CP000468_1:1.0):1.0,"
				+ "Escherichia_coli_UTI89_NC_007946_1:2.0):1.0):1.0,Escherichia_coli_CFT073_AE014075_1:4.0):1.0):1.0):4.0,"
				+ "((Escherichia_coli_UMN026_NC_011751_1:2.0,(Escherichia_coli_SMS_3_5_NC_010498_1:1.0,Escherichia_coli_IAI39_CU928164_2:1.0):1.0):7.0,"
				+ "((Shigella_dysenteriae_Sd197_NC_007606_1:3.0,(Escherichia_coli_O157_H7_str_EC4115_NC_011353_1:2.0,"
				+ "(Escherichia_coli_O157H7_str_EDL933_AE005174_2:1.0,Escherichia_coli_O157_H7_str_Sakai_BA000007_2:1.0):1.0):1.0):5.0,"
				+ "((Shigella_flexneri_5_str_8401_CP000266_1:2.0,(Shigella_flexneri_2a_str_2457T_AE014073_1:1.0,"
				+ "Shigella_flexneri_2a_str_301_AE005674_2:1.0):1.0):5.0,(((Escherichia_coli_O111_H_str_11128_AP010960_1:1.0,"
				+ "Escherichia_coli_O26_H11_str_11368_DNA_NC_013361_1:1.0):4.0,(Escherichia_coli_O103_H2_str_12009_NC_013353_1:3.0,"
				+ "((Escherichia_coli_55989_CU928145_2:1.0,Escherichia_coli_E24377A_NC_009801_1:1.0):1.0,(Escherichia_coli_IAI1_NC_011741_1:1.0,"
				+ "Escherichia_coli_SE11_DNA_NC_011415_1:1.0):1.0):2.0):1.0):1.0,((Escherichia_coli_ATCC_8739_CP000946_1:1.0,"
				+ "Escherichia_coli_HS_NC_009800_1:1.0):3.0,((Escherichia_coli_B_str_REL606_CP000819_1:1.0,"
				+ "Escherichia_coli_BL21_DE3_NC_012892_2:1.0):2.0,(Escherichia_coli_BW2952_NC_012759_1:2.0,"
				+ "(Escherichia_coli_str_K12_substr_MG1655_NC_000913_3:1.0,"
				+ "Escherichia_coli_str_K12_substr_W3110_AP009048_1:1.0):1.0):1.0):1.0):2.0):1.0):1.0):1.0):1.0):1.0);"
				+ "(29,((1,(11,((2,((21,4),25)),9))),((24,(23,14)),((31,(18,(17,19))),((34,(32,33)),(((16,20),((15,((3,10),(13,22))))),(5,12),((8,6),(7,(27,28))))))))));";
		
//		String t = "((9,(11,(((25,21),4),2))),((23,14),(24,((((35,(34,(33,32))),30),(31,29)),(((8,6),(12,5)),"+
//			    "(((28,27),(26,7)),((18,(19,17)),((((20,(22,16)),15),13),(10,3)))))))),1);";
//		
//		
//		TreeNode<String> root = new TreeNode<String>("root");
//		{
//		    TreeNode<String> node0 = root.addChild("node0");
//		    TreeNode<String> node1 = root.addChild("node1");
//		    TreeNode<String> node2 = root.addChild("node2");
//		    {
//		        TreeNode<String> node20 = node2.addChild(null);
//		        TreeNode<String> node21 = node2.addChild("node21");
//		        {
//		            TreeNode<String> node210 = node20.addChild("node210");
//		        }
//		    }
//		}
		
		
		
		final String tree = t.replaceAll(" ","");//.replaceAll("[\\(\\)]","");
		
		System.out.println(tree+"\n\n");
		
		try(PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Downloads/mugsy.tree")))){
    		List<String> all=Files.readAllLines(Paths.get("/home/mahmi/Downloads/34_32-more_all_all_all_3.0_0.6.nexus.run2.t"));
    		all.forEach(l -> {
    			if(l.startsWith("   tree gen.")){
    				String line = l.split("\\[\\&U\\]")[1].replaceAll(":[0-9]*.[0-9]*[e-]*[0-9]*", "").replaceAll(" ","");//.replaceAll("[\\(\\)]","");
//    				System.out.println(line);
    				if(line.startsWith(tree.substring(0, 15))) System.out.println(l);
    			}
    		});
    	}catch(Exception e){;
    		e.printStackTrace();
    	}
		
	}
	
	/*
	 * 
	 *    tree gen.1000000 = [&U] (((14:1.720046e-02,23:1.994811e-02):1.435137e-02,(24:3.711158e-02,
	 *    ((((13:4.111043e-03,(15:1.607625e-02,((22:1.079059e-02,16:1.152181e-02):7.350434e-03,20:1.204693e-02):8.497068e-03):2.469244e-03):5.580389e-03,
	 *    (3:9.025741e-03,10:8.334005e-02):2.505413e-02):1.919965e-02,((19:1.845139e-03,17:1.875747e-02):2.138395e-03,18:6.305241e-03):4.033305e-02):1.549823e-02,
	 *    (((((30:1.060919e-01,35:1.047456e-01):4.249491e-02,(34:1.774236e-02,(33:1.259864e-02,32:1.167367e-03):2.381779e-02):4.718841e-02):1.188831e-02,
	 *    (29:2.619619e-01,31:1.212733e-01):1.879787e-02):7.212429e-02,(7:5.894388e-04,(26:9.336251e-04,
	 *    (27:4.135265e-03,28:1.108694e-04):4.683699e-03):3.940688e-04):2.798001e-02):1.615809e-03,((8:6.385853e-04,6:1.561055e-03):1.495084e-02,
	 *    (5:1.145432e-02,12:1.596069e-02):1.046838e-02):9.611171e-03):3.241114e-03):2.559113e-02):3.192731e-02):3.376614e-02,((11:2.279316e-02,
	 *    ((4:4.522175e-03,(21:5.906150e-03,25:8.258881e-03):6.314593e-03):3.684745e-02,2:2.497610e-02):1.348583e-02):7.545892e-03,
	 *    9:3.088819e-02):1.242260e-02,1:1.935111e-02);
	 *    
	 *    ((9,(11,(((25,21),4),2))),((23,14),(24,((((35,(34,(33,32))),30),(31,29)),(((8,6),(12,5)),(((28,27),(26,7)),((18,(19,17)),((((20,(22,16)),15),13),(10,3)))))))),1);
	 */
	
	List<P4<Integer, Double, Integer, Double>> pairwise = new ArrayList<>();
	//List<List<Double>> sharedPeaks = new ArrayList<>();
	
	public P2<List<String>,List<List<Double>>> readPeaks(final String path){
		final File peaksFolder = new File(path);
		final File[] peakFiles = peaksFolder.listFiles();
		final List<String> names = new ArrayList<>();
		List<List<Double>> peaks = new ArrayList<>();
		try{
			for(int i = 0; i < peakFiles.length; i++){
				names.add(peakFiles[i].getName().replace(".faa",""));
				final List<Double> peakFile = new ArrayList<>();
				Files.readAllLines(Paths.get(peakFiles[i].getPath())).forEach(m -> {
					peakFile.add(Double.parseDouble(m));
				});
				peaks.add(peakFile);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return P.p(names,peaks);
	}
	
	public List<P2<Integer, Double>> getRelated(List<P2<Integer, Double>> related, int position, double peak){
		List<P2<Integer, Double>> auxiliar = new ArrayList<>();
		for(int i=0; i<this.pairwise.size(); i++){
			if((this.pairwise.get(i)._1()==position && this.pairwise.get(i)._2()==peak) ||
				(this.pairwise.get(i)._3()==position && this.pairwise.get(i)._4()==peak)){	
				final P4<Integer, Double, Integer, Double> match = this.pairwise.get(i);
				
				this.pairwise.remove(i);
				i--;
				//this.sharedPeaks.get(match._1()).remove(match._2());
				//this.sharedPeaks.get(match._3()).remove(match._4());
				
				if(match._1()==position && !related.contains(P.p(match._3(),match._4())))
					auxiliar.add(P.p(match._3(),match._4()));
				if(match._3()==position && !related.contains(P.p(match._1(),match._2())))
					auxiliar.add(P.p(match._1(),match._2()));
			}
		}
		related.addAll(auxiliar);

		for(int i=0; i<auxiliar.size(); i++){
			related = getRelated(related, auxiliar.get(i)._1(), auxiliar.get(i)._2());
		}		
		
		return related;
	}
	
	public byte[] parseRelated(List<P2<Integer, Double>> related, int samples){
		byte[] binary = new byte[samples];
		for(int i = 0; i < samples; i++){
			binary[i]=0;
		}
		for(int i=0; i<related.size(); i++){
			binary[related.get(i)._1()] = 1;
		}
		return binary;
	}
	
	public void pairwise(final P2<List<String>,List<List<Double>>> peaks, double sigma, double cutoff){
		final Set<P4<Integer, Double, Integer, Double>> pairwiseSet = new HashSet<>();
		final List<Set<Double>> sharedPeaksSet = new ArrayList<>();
//		for(int i = 0; i< peaks._2().size(); i++){
//			sharedPeaksSet.add(new HashSet<Double>());
//		}
		ExecutorService ex = Executors.newFixedThreadPool(16);
		for(int i = 0; i < peaks._2().size(); i++){
			final int r = i;
			Thread thread = new Thread(){
			    public void run(){
					List<Double> reference = peaks._2().get(r);
					for(int i = 0; i < reference.size(); i++){
						final Double referencePeak = reference.get(i);
						for(int c = 0; c < peaks._2().size(); c++){
							if(c != r){
								for(int j = 0; j < peaks._2().get(c).size(); j++){
									final Double comparisonPeak = peaks._2().get(c).get(j);
									double comparison = 1.0-(erf(abs(referencePeak-comparisonPeak))/2*sigma);
									if(cutoff<=comparison){
										synchronized(pairwiseSet){
										if(r<c)
											pairwiseSet.add(P.p(r,referencePeak,c,comparisonPeak));
										else
											pairwiseSet.add(P.p(c,comparisonPeak,r,referencePeak));	
										}
//										synchronized(sharedPeaksSet){											
//											sharedPeaksSet.get(r).add(referencePeak);
//											sharedPeaksSet.get(c).add(comparisonPeak);
//										}
									}
								}
							}
						}
					}		      
			    }
			};
			ex.execute(thread);
		}
		ex.shutdown();
		try {
			ex.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
//		this.sharedPeaks = new ArrayList<>();
//		for(int i=0; i<sharedPeaksSet.size(); i++){
//			this.sharedPeaks.add(new ArrayList<Double>());
//			this.sharedPeaks.get(i).addAll(sharedPeaksSet.get(i));
//		}
		this.pairwise = new ArrayList<>();
		this.pairwise.addAll(pairwiseSet);
	}
	
	public byte[][] peaksInCommon(final int size){
		List<byte[]> matrix = new ArrayList<>();
		//for(int i = 0; i< this.pairwise.size(); i++){
			while(this.pairwise.size()>0){
				List<P2<Integer, Double>> related = new ArrayList<>();
				related.add(P.p(pairwise.get(0)._1(),this.pairwise.get(0)._2()));
				related = getRelated(related, pairwise.get(0)._1(), pairwise.get(0)._2());				
				matrix.add(parseRelated(related, size));
			}
		//}
		byte[][] peaksInCommon = new byte[matrix.size()][];
		for(int i = 0; i < matrix.size(); i++){
			peaksInCommon[i] = matrix.get(i);
		}
		return transposeMatrix(peaksInCommon);
	}
	
	public byte[][] transposeMatrix(final byte[][] matrix){
		byte[][] matrixT = new byte[matrix[0].length][matrix.length];
		for (int x=0; x < matrix.length; x++) {
			for (int y=0; y < matrix[x].length; y++) {
				matrixT[y][x] = matrix[x][y];
			}
		}
		return matrixT;
	}
	
	public double getSecondarySimilarityScore(final String sequence1, 
											  final String sequence2,
											  final String ss1,
											  final String ss2){
		double mean = (sequence1.length() + sequence2.length())/2;   	
		
		String[] characters1 = ss1.split("(?<!^)");
		String[] characters2 = ss2.split("(?<!^)");
		
		int counter1 = 0;
		int counter2 = 0;
		double score = 0;
		for(int i = 0; i < ss1.length(); i++){
			if(characters1[i].equals(characters2[i])){
				score = score + 1 + (0.5*counter1) + (0.5*counter2);
				counter1 = 0;
				counter2 = 0;
			}else{
				if(characters1[i].equals("C")){
					counter1++;
					score = score + (0.5*counter2);
					counter2=0;
				}else{
					if(characters2[i].equals("C")){
						counter2++;
						score = score + (0.5*counter1);
						counter1=0;
					}else{
						score = score + (0.5*counter1) + (0.5*counter2);
		    			counter1 = 0;
		    			counter2 = 0;
					}    				
				}
			}
		}
		score = score + (0.5*counter1) + (0.5*counter2);
		return score/mean;
	}
	
	public void parsePSIPredResults(final Path path) {
		File folder = path.toFile();
		File[] files = folder.listFiles();
		for(int i = 0; i< files.length; i++) {
			try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter(files[i].toString().replace(".horiz",".ss"))))){
				List<String> lines =Files.readAllLines(Paths.get(files[i].toString()));
				lines.forEach(l -> { 
					if(l.startsWith("Pred: "))
						pw.println(l.replace("Pred: ", ""));
				});
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}			
	}
	
	public void getSimilarity(final Path alignments, final Path sequences, final Path reference){ 
		File folder = alignments.toFile();
		File[] files = folder.listFiles();
		for(int i = 0; i< files.length; i++) {
			try{
				List<String> ref = Files.readAllLines(reference);
				List<String> linesa =Files.readAllLines(Paths.get(files[i].toString()));
				List<String> liness =Files.readAllLines(Paths.get(sequences.toString()+"/"+files[i].getName().replace(".ss.clustalo", ".faa")));
				String s1 = "";
				String s2= "";
				boolean go = false;
				for(int j=1; j<linesa.size(); j++){
					if(!go){
						if(linesa.get(j).startsWith(">")) go = true;
						else s1+= linesa.get(j);
					}else {
						s2+= linesa.get(j);
					}
				}
				System.out.println(files[i].getName().replace(".ss.clustalo", "")+": "+getSecondarySimilarityScore(liness.get(1), ref.get(0), s1, s2));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getFileExtension(File file) {
	    String name = file.getName();
	    int lastIndexOf = name.lastIndexOf(".");
	    if (lastIndexOf == -1) {
	        return ""; // empty extension
	    }
	    return name.substring(lastIndexOf);
	}
	
	private void runClustalO(String input, String output) {
		try {
			val process = buildProcessClustalO(input, output).start();
			redirectErrorToLogs(process);
			checkExitValue(process.waitFor());
		} catch (final IOException | InterruptedException ie) {
			System.out.println("Water error"+ie);
			throw new RuntimeException(ie);
		}
	}

	/**
	 * Builds the PSortB execution process
	 * 
	 * @return the {@link ProcessBuilder} of PSortB execution
	 */
	private ProcessBuilder buildProcessClustalO(String input, String output) {
		return new ProcessBuilder("clustalo", "-i", input, "-o", output);
	}
	
		
	private void runWater(String input1, String input2, String output) {
		try {
			val process = buildProcessWater(input1, input2, output).start();
			redirectErrorToLogs(process);
			checkExitValue(process.waitFor());
		} catch (final IOException | InterruptedException ie) {
			System.out.println("Water error"+ie);
			throw new RuntimeException(ie);
		}
	}

	/**
	 * Builds the PSortB execution process
	 * 
	 * @return the {@link ProcessBuilder} of PSortB execution
	 */
	private ProcessBuilder buildProcessWater(String input1, String input2, String output) {
		return new ProcessBuilder("/opt/EMBOSS-6.6.0/emboss/water", "-asequence", input1, "-bsequence", input2, "-sprotein1", "-sprotein2", "-gapopen", "10", "-gapextend", "0.5", "-outfile", output);
	}

	/**
	 * Checks if PSortB execution exited anormally
	 * 
	 * @param value
	 *            The exit value
	 * @throws IOException
	 */
	private void checkExitValue(final int value) throws IOException {
		if (value != 0)
			throw new IOException(String.format("PSORT exited anormally (%d)", value));
	}

	/**
	 * Redirects errors to log
	 * 
	 * @param proc
	 *            The process to log errors
	 * @throws IOException
	 */
	private void redirectErrorToLogs(final Process proc) throws IOException {
		try (val stderr = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
			stderr.lines().forEach(l -> System.out.println(l));
		}
	}
	
	private Double compareSequences(String s1, String s2, Boolean trim){

//		System.out.println(s1);
//		System.out.println(s2);
		if(trim){
			int counter = 0;
			while(s1.startsWith("-")){
				s1 = s1.substring(1);
				counter ++;
			}
			s2 = s2.substring(counter);
			
			counter = 0;
			while(s2.startsWith("-")){
				s2 = s2.substring(1);
				counter ++;
			}
			s1 = s1.substring(counter);
			
			counter = 0;
			while(s1.endsWith("-")){
				s1 = s1.substring(0, s1.length()-1);
				counter ++;
			}
			s2 = s2.substring(0, s2.length()-counter);
			
			counter = 0;
			while(s2.endsWith("-")){
				s2 = s2.substring(0, s2.length()-1);
				counter ++;
			}
			s1 = s1.substring(0, s1.length()-counter);
//    		System.out.println(s1);
//    		System.out.println(s2);
		}
		
		Double score = 0.0;
		for(int i=0; i<s1.length(); i++){
			char c1 = s1.charAt(i);
			char c2 = s2.charAt(i);
			if(c1 == c2){
				score++;
			}else{
				if((c1 == 'H' && c2 == 'C') || (c1 == 'E' && c2 == 'C') || (c2 == 'H' && c1 == 'C') || (c2 == 'E' && c1 == 'C')){
					score += 0.5;
				}else{
					score -= 0.5;
				}
			}
		}
		return score*100/s1.length();
	}
	
	
    public void testApp(){
    	
    	//Parse Water results
    	
    	try(PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/senescence/local_alignments.csv")))) {			   		
    		File folder = new File("/home/mahmi/Dataset/senescence/local_alignments");
			File[] listOfFiles = folder.listFiles();
			for(int x = 0; x<listOfFiles.length; x++){
				if(getFileExtension(listOfFiles[x]).equals(".water")){
					List<String> lines = Files.readAllLines(Paths.get(listOfFiles[x].getAbsolutePath())); 
		    		String s1 = "";
		    		String s2 = "";
		    		
		    		for(int i=0; i<lines.size(); i++){
		    			if (lines.get(i).startsWith("MAHMI")){
		    				int index = lines.get(i+1).indexOf("|");
		    				s1+=lines.get(i).substring(index).split(" ")[0];
		    				s2+=lines.get(i+2).substring(index).split(" ")[0];
		    			}
		    		}
		    		
		    		System.out.println(s1);
		    		System.out.println(s2);
		    		Double score = compareSequences(s1,s2,false);
		    		if(score < 0) score=0.0;
		    		String[] splitname = listOfFiles[x].getName().split("-");
		    		String senescence_name = "";
		    		for(int j=1; j<splitname.length; j++){
		    			senescence_name += splitname[j]+"-";
		    		}
		    		pw.println(splitname[0]+";"+senescence_name.replace(".water-", "")+";"+score);
				}
			}
    		
    	}catch (Exception e) {
			e.printStackTrace();
		}
    	
    	//Parse ClustalO results
    	
//    	try(PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/senescence/global_alignments.csv")))) {			   		
//    		File folder = new File("/home/mahmi/Dataset/senescence/global_alignments");
//			File[] listOfFiles = folder.listFiles();
//			for(int x = 0; x<listOfFiles.length; x++){
//				if(getFileExtension(listOfFiles[x]).equals(".clustalo")){
//					List<String> lines = Files.readAllLines(Paths.get(listOfFiles[x].getAbsolutePath())); 
//					int i=1;
//		    		String s1 = "";
//		    		String s2 = "";
//		    		while(!lines.get(i).startsWith(">")){
//		    			s1+=lines.get(i);
//		    			i++;
//		    		}
//		    		for(int j=i; j<lines.size(); j++){
//		    			if(!lines.get(j).startsWith(">")){
//		    				s2+=lines.get(j);
//		    			}
//		    		}
//		    		Double score = compareSequences(s1,s2,true);
//		    		if(score < 0) score=0.0;
//		    		String[] splitname = listOfFiles[x].getName().split("-");
//		    		String senescence_name = "";
//		    		for(int j=1; j<splitname.length; j++){
//		    			senescence_name += splitname[j]+"-";
//		    		}
//		    		pw.println(splitname[0]+";"+senescence_name.replace(".clustalo-", "")+";"+score);
//				}
//			}
//    		
//    	}catch (Exception e) {
//			e.printStackTrace();
//		}
    	
    	
    	//ClustalO execution
//    	try{
//    		List<String> lines = Files.readAllLines(Paths.get("/home/mahmi/Dataset/senescence/results_blast.csv")); 
//	    	File folder = new File("/home/mahmi/Dataset/senescence/SecondaryStructure/MAHMI");
//			File[] listOfFiles = folder.listFiles();
//			for(int i = 0; i<listOfFiles.length; i++){
//				String file_name = listOfFiles[i].getName();
//				System.out.println(file_name);
//				for(int j=0; j<lines.size(); j++){
//					
//					String [] linesplited = lines.get(j).split(";");					
//					if(linesplited[0].equals(file_name.replace(".fss", ""))){
////								"/home/mahmi/Dataset/senescence/global_alignments/"+linesplited[0]+"-"+linesplited[1].replaceAll("\\|", "-")+".water");
//						List<String> f1 = Files.readAllLines(Paths.get(listOfFiles[i].getAbsolutePath().toString())); 
//						List<String> f2 = Files.readAllLines(Paths.get("/home/mahmi/Dataset/senescence/SecondaryStructure/Senescence/"+linesplited[1].replaceAll("\\|", "-")+".fss"));
//						try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/senescence/global_alignments/"+linesplited[0]+"-"+linesplited[1].replaceAll("\\|", "-")+".faa")))) {
//							f1.forEach(l -> pw.println(l));	
//							f2.forEach(l -> pw.println(l));	
//						}catch (final IOException e) {
//				            e.printStackTrace();
//				        }
//						runClustalO("/home/mahmi/Dataset/senescence/global_alignments/"+linesplited[0]+"-"+linesplited[1].replaceAll("\\|", "-")+".faa", "/home/mahmi/Dataset/senescence/global_alignments/"+linesplited[0]+"-"+linesplited[1].replaceAll("\\|", "-")+".clustalo");
//					}
//				}
//				
//			}	    	
//	    	
//		}catch (Exception e) {
//			System.err.println(e.getMessage());
//		}
    	
    	//Water execution
//    	try{
//    		List<String> lines = Files.readAllLines(Paths.get("/home/mahmi/Dataset/senescence/results_blast.csv")); 
//	    	File folder = new File("/home/mahmi/Dataset/senescence/SecondaryStructure/MAHMI");
//			File[] listOfFiles = folder.listFiles();
//			for(int i = 0; i<listOfFiles.length; i++){
//				String file_name = listOfFiles[i].getName();
//				System.out.println(file_name);
//				for(int j=0; j<lines.size(); j++){
//					
//					String [] linesplited = lines.get(j).split(";");					
//					if(linesplited[0].equals(file_name.replace(".fss", ""))){
//						runWater(listOfFiles[i].getAbsolutePath().toString(), "/home/mahmi/Dataset/senescence/SecondaryStructure/Senescence/"+linesplited[1].replaceAll("\\|", "-")+".fss",
//								"/home/mahmi/Dataset/senescence/local_alignments/"+linesplited[0]+"-"+linesplited[1].replaceAll("\\|", "-")+".water");
//					}
//				}
//				
//			}	    	
//	    	
//		}catch (Exception e) {
//			System.err.println(e.getMessage());
//		}
//    	try{
//	    	List<String> lines = Files.readAllLines(Paths.get("/home/mahmi/Dataset/senescence/senescence_like_peptides.csv"));    	
//				
//			for(int i = 0; i<lines.size(); i++){
//				String line = lines.get(i);
//				try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/senescence/Senescence_like_peptides/MAHMI"+line.split("\t")[0]+".faa")))) {
//					pw.println(">MAHMI"+line.split("\t")[0]);	
//					pw.println(line.split("\t")[1]);	
//				}catch (final IOException e) {
//		            e.printStackTrace();
//		        }
//			}    	
//	    	
//		}catch (Exception e) {
//			
//		}
    	
    	
//    	BufferedReader fastaReader;
//    	final Map<String, String> fastaSequences = new HashMap<>();
//    	try {
//			fastaReader = new BufferedReader(new FileReader("/home/mahmi/Downloads/Bifidobacterium bifidum 3BIT4A filtered contigs.feature_dna.fasta"));
//		    String p;
//		    String fastaAux = "";	  
//		    //Get fasta sequences
//		    
//		    String header = "";
//		    int counter=0;
//		    while ((p = fastaReader.readLine()) != null) {
//		    	if(p.startsWith(">")){
//		    		if(!header.equals("")){
//			    		fastaSequences.put(header, fastaAux);
//			    		fastaAux = "";		    			
//		    		}
//			    	header=p.split(" ")[0].substring(1);
////			    	System.out.println(header);
//		    	}else{
//		    		fastaAux=fastaAux+p;
//		    	}
//		    }		    
//		    fastaSequences.put(header,fastaAux);
//		    fastaReader.close();	    
//		  
//	    } catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    	
    	
//    	try(PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Downloads/bb_hp/over_positions_1.5.txt")))){
//	    	List<String> line = Files.readAllLines(Paths.get("/home/mahmi/Downloads/bb_hp/bb_over_1.5.txt"));
//			List<String> lines = Files.readAllLines(Paths.get("/home/mahmi/Downloads/bb_hp/Bifidobacterium bifidum 3BIT4A filtered contigs.gff"));
//	    	for(int i=0; i < line.size()-1; i++){
//	    		for(int j=0; j< lines.size(); j++){    	
////	    			System.out.println(line.get(i));
//    				if(lines.get(j).contains(line.get(i))){
////    					System.out.println(lines.get(j));
//    					String match = lines.get(j).split("\t")[8].split(";")[0];
//    					pw.println(match.split("\\.")[3]+"\t"+line.get(i));
//    					break;
//    				}
//		    	}
//	    	}
//	    	
//				
//	    	
//		}catch (IOException e) {
//			e.printStackTrace();
//		}  
    	
    	
    	
//    	try(PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Downloads/annotation_new.gff")))){
//	    	List<String> line = Files.readAllLines(Paths.get("/home/mahmi/Downloads/bb_annotation.gff"));
//	    	int n = 1;
//	    	for(int i=0; i<line.size(); i++){
//	    		if(line.get(i).contains("Name=hypothetical protein")){
//		    		pw.println(line.get(i).replaceAll("Name=hypothetical protein", "Name=hypothetical protein_"+n));
//		    		n++;
//		    	}else{
//		    		pw.println(line.get(i));
//		    	}
//	    	}
//	    	
//				
//	    	
//		}catch (IOException e) {
//			e.printStackTrace();
//		}   
    	
    	
//    	;
    	
//    	try{
//	    	File folder = new File("/home/mahmi/Dataset/Lactobacillus_phylogeny/Reference");
//			File[] listOfFiles = folder.listFiles();
//			for(int i = 0; i<listOfFiles.length; i++){
//				
//				List<String> lines = Files.readAllLines(Paths.get(listOfFiles[i].getAbsolutePath()));
//				System.out.println(listOfFiles[i].getName().split(".faa")[0].replaceAll("[^a-zA-Z0-9\\s+]", "_").replaceAll("\\s","_")+".faa");
//				try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/Lactobacillus_phylogeny/Reference_names-parsed/"+
//				listOfFiles[i].getName().split(".faa")[0].replaceAll("[^a-zA-Z0-9\\s+]", "_").replaceAll("\\s","_")+".faa")))) {
//					lines.forEach(l -> pw.println(l));
//				}catch (final IOException e) {
//		            e.printStackTrace();
//		        }
//			}	    	
//	    	
//		}catch (Exception e) {
//			
//		}
	
    	
    	
//    	
//    	try {
//			final List<String> mappingCSV = Files.readAllLines(Paths.get("/home/mahmi/Dataset/Lactobacillus_phylogeny/Lactobacillus_WGS.csv"), Charset.defaultCharset());			
//			for(int i=1; i<mappingCSV.size(); i++){
//				try{			
//					final String line = mappingCSV.get(i);
//					final String id = line.split(",")[0].substring(1, line.split(",")[0].length() -1);
//					final String name = line.split(",")[1].substring(1, line.split(",")[1].length() -1);
////							.replaceAll(" ", "_").replaceAll("'", "").replaceAll("[\\W]|_", "_");
//										
//					final List<String> source = Files.readAllLines(Paths.get("/home/mahmi/Dataset/Lactobacillus_phylogeny/WGS/"+id+"/"+id+".PATRIC.faa"), Charset.defaultCharset());
//					try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/Lactobacillus_phylogeny/WGS/"+name+"-"+id+".faa")))) {
//						source.forEach(l -> {
//							if(l.startsWith(">"))
//								pw.println(l.replaceAll("-","_").replaceAll(":","_"));
//							else
//								pw.println(l);								
//						});
//					}catch (final IOException e) {
//			            e.printStackTrace();
//			        }
//					
//					final File folder = new File("/home/mahmi/Dataset/Lactobacillus_phylogeny/WGS/"+id);
//					folder.delete();
//				} catch(NoSuchFileException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}	
//		}catch (IOException e ) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
//    	
//    	
    	
    	
    	
    	
    	
//    	try(PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/Escherichia_coli/colibactina/6000Patric/clb12_n_strains_pks.fna")))){
//		    	File folder = new File("/home/mahmi/Dataset/Escherichia_coli/colibactina/6000Patric/clb12_blast_440_n");
//				File[] listOfFiles = folder.listFiles();
//				final List<String> names = new ArrayList<>();
//				for(int i = 0; i<listOfFiles.length; i++){
//					List<String> line = Files.readAllLines(Paths.get(listOfFiles[i].getPath()));
//					final int j = i;
//					if(!names.contains(listOfFiles[j].getName().split("\\.")[0].replace(" ", "_"))){
//						names.add(listOfFiles[j].getName().split("\\.")[0].replace(" ", "_"));
//						for(int k = 0; k< line.size(); k++) {
//							final String l = line.get(k);
//							Double ident = Double.parseDouble(l.split("\t")[2]);											
//						
//							if(ident>90.0){
//	//							System.out.println(p);
//								BufferedReader fastaReader;
//								try {
//									fastaReader = new BufferedReader(new FileReader("/home/mahmi/Dataset/Escherichia_coli/samples/6000PatrickHomoSapiens/GetGenomes/"+listOfFiles[j].getName()));
//									pw.println(">"+listOfFiles[j].getName().split("\\.")[0].replace(" ", "_"));
//								    String p;
//								    String fastaAux = "";	    	
//								    fastaReader.readLine();
//								    //Get fasta sequences
//								    final Map<String, String> fastaSequences = new HashMap<>();
//								    String header = "";
//								    int counter=0;
//								    while ((p = fastaReader.readLine()) != null) {
//								    	if(p.startsWith(">")){
//								    		if(!header.equals("")){
//									    		fastaSequences.put(header, fastaAux);
//									    		fastaAux = "";		    			
//								    		}
//									    	header=p.split(" ")[0].substring(1);
//								    	}else{
//								    		fastaAux=fastaAux+p;
//								    	}
//								    }		    
//								    fastaSequences.put(header,fastaAux);
//								    fastaReader.close();
//								    
//								   pw.println(fastaSequences.get(l.split("\t")[0]));
//							    } catch (Exception e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//								break;
//							}
//						}
//					}
//				}
//		    	
//		    	
//		}catch (IOException e) {
//			e.printStackTrace();
//		}   	
//    	
    	
    	
    	
//    	try(PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/Beta_amyloid/test_strains/peptides.faa")))){	
//    		List<String> cepa_A = Files.readAllLines(Paths.get("/home/mahmi/tempfiles/bichos_bamiloide.csv"));
//    		int count =1; 
//    		for(int i=0; i<cepa_A.size(); i++){
//    			String a = cepa_A.get(i);
//    			pw.println(">STRAINS_PEP"+count);
//    			pw.println(a);
//    			count++;
//    		}
//    		
//    	}catch (IOException e) {
//			e.printStackTrace();
//		}
    	
    	
//    	try(PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/BifidoAerobia/last.results")))){	
//    		BufferedReader in
//    		   = new BufferedReader(new FileReader("/home/mahmi/Dataset/BifidoAerobia/last.maf"));
//    		
//    		String line; 
//    		int counter = 0;
//    		while ((line = in.readLine()) != null) {    
//                 if(line.startsWith("a score=")){ 
//                	 Double evalue = Double.parseDouble(line.split(" ")[3].replaceAll("E=", ""));
//                	 String p_evalue =line.split(" ")[3].replaceAll("E=", "");
//                	 String score = line.split(" ")[1].replaceAll("score=", "");
//                	 if(evalue<0.00001){
//                	 
//	                	 line = in.readLine();
//	                	 String strand1 = line.split(" ")[4];                	 
//	                	 String number1 = line.split("_")[1];
//	                	 String node1[] = line.split(" ");
//	                	 
//	                	 while (!(line = in.readLine()).startsWith("s ")) {    
//	                	 }
//                    	 String node2[] = line.split(" "); 
//	                	 String strand2 = line.split(" ")[4];
//	                	 String number2 = line.split("_")[1];
//	                	 
//	                	 if(number1.equals(number2)){ 
//	                		 
//	                		 if(number1.equals("1") || number1.equals("2") || number1.equals("3") || number1.equals("6") || number1.equals("7")){
//	                			 if(strand1.equals("+") && strand2.equals("-")){
//	                				 counter++;
////	                        		 pw.println(number1+" - "+number2);
//	                        		 pw.println("Alignment score="+score+", expect="+p_evalue+":");
//	                        		 pw.println(node1[1]+" "+node1[2]+" "+node1[3]+" "+node1[4]+" "+node1[5]);
//	                        		 pw.println(node2[1]+" "+node2[2]+" "+node2[3]+" "+node2[4]+" "+node2[5]);
//	                        		 pw.println();
//	                			 }                				  
//	                		 }else{
//	                			 if(strand1.equals("+") && strand2.equals("+")){
//	                				 counter++;
////	                        		 pw.println(number1+" - "+number2);
//	                        		 pw.println("Alignment score="+score+", expect="+p_evalue+":");
//	                        		 pw.println(node1[1]+" "+node1[2]+" "+node1[3]+" "+node1[4]+" "+node1[5]);
//	                        		 pw.println(node2[1]+" "+node2[2]+" "+node2[3]+" "+node2[4]+" "+node2[5]);
//	                        		 pw.println();
//	                			 } 
//	                		 }
//	                	 }
//                	 }	 
//                 }
//            } 
//       	 System.out.println(counter);
//    		
//    	}catch (IOException e) {
//			e.printStackTrace();
//		}
    	
    	
//    	try(PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/BifidoAerobia/results.txt")))){	
//    		List<String> cepa_A = Files.readAllLines(Paths.get("/home/mahmi/Dataset/BifidoAerobia/Bbifidum_3BIT4A_feature_dna.fasta"));
//    		List<String> cepa_U = Files.readAllLines(Paths.get("/home/mahmi/Dataset/BifidoAerobia/Bbifidum_3BIT4u_feature_dna.fasta"));
//    		List<String> alineamientos = Files.readAllLines(Paths.get("/home/mahmi/Dataset/BifidoAerobia/Bbifidum_3BIT4u_vs_A.out"));
//    		List<String> A = new ArrayList<>();
//    		List<String> U = new ArrayList<>();
//    		
//    		cepa_A.forEach(a -> {
//    			if(a.startsWith(">")) A.add(a);
//    		});
//    		
//    		cepa_U.forEach(u -> {
//    			if(u.startsWith(">")) U.add(u);
//    		});
//    		
//    		alineamientos.forEach(a -> {
//    			final String[] split = a.split("\t");
//    			if(!split[2].equals("100.000")){
//    				cepa_A.forEach(aa -> {
//    	    			if(aa.startsWith(">"+split[1].replace(":", "|")+" "))pw.println(aa);
//    	    		});
//    	    		
//    	    		cepa_U.forEach(u -> {
//    	    			if(u.startsWith(">"+split[0]+" ")) pw.println(u);
//    	    		});
//    	    		pw.println("\n");
//    			}
//    		});
//    		
//    	}catch (IOException e) {
//			e.printStackTrace();
//		}
//    	
    	
    	
    	
////    	getSimilarity(Paths.get("/home/mahmi/Dataset/Beta_amyloid/ss_similarity_40/"),Paths.get("/home/mahmi/Dataset/Beta_amyloid/peptides/"),Paths.get("/home/mahmi/Dataset/Beta_amyloid/ab40.ss"));
//    	String strainn = "Bifidobacterium_breve_DSM_20213";
//    	try(PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/VSL3_bernardo/reports/"+strainn+".tsv")))){
////			
//    		List<String> peptides = Files.readAllLines(Paths.get("/home/mahmi/Dataset/VSL3_bernardo/results_from_serpent/"+strainn+".csv"));
//    		List<String> references = Files.readAllLines(Paths.get("/home/mahmi/blast_db/mahmiRefPeptides596.fasta"));
//    		List<String> strain = Files.readAllLines(Paths.get("/home/mahmi/Dataset/VSL3_bernardo/peptides/"+strainn+".faa"));
//    		Map<String,String> ref = new HashMap<>();
//    		Map<String,String> strn = new HashMap<>();
//    		for(int i=0; i<references.size(); i+=2){
//    			ref.put(references.get(i).split(">")[1].split("\\s+")[0], references.get(i).replace(">", ""));
//    		}
//    		for(int i=0; i<strain.size(); i+=2){
////    			System.out.println(strain.get(i).split(">")[1].split("\\s+")[0]);
//    			strn.put(strain.get(i).split(">")[1].split("\\s+")[0], strain.get(i+1));
//    		}
//    		pw.println(peptides.get(0).replaceAll(" ID", "").replaceAll(",", "\t"));
//    		
//    		for(int i=1; i<peptides.size(); i++){
//    			
//    			
//    			String[] l = peptides.get(i).split(",");
//    			
//    			String sequence = strn.get(l[0]);
//    			String reference = ref.get(l[1]);
//    			String others=l[2];
//    			for(int j=3;j<l.length;j++){
//    				others+="\t"+l[j];
//    			}
//    			pw.println(sequence+"\t"+reference+"\t"+others);	
//    			
//    		}
//    	}catch (IOException e) {
//			e.printStackTrace();
//		}
    	
    	
//try{
//    	File folder = new File("/home/mahmi/Dataset/VSL3_bernardo/peptides");
//		File[] listOfFiles = folder.listFiles();
//		for(int i = 0; i<listOfFiles.length; i++){
//			List<String> peptides = Files.readAllLines(Paths.get(listOfFiles[i].getPath()));
//			Set<String> peptid = new HashSet<>();
//			peptides.forEach(p -> {
//				String peptide = p.split("\t")[1];
//				if(peptide.length()>=13 && peptide.length()<=16){
//					peptid.add(peptide);
//				}
//			});
//			try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/VSL3_bernardo/true_peptides/"+listOfFiles[i].getName().replace(".csv","")+".faa")))){
//				int j=1;
//				for(String p : peptid){
//					pw.println(">peptide_"+j+" "+listOfFiles[i].getName().replace(".csv",""));
//					pw.println(p);
//					j++;
//				};
//			}catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//    	
//    	
//}catch (IOException e) {
//	e.printStackTrace();
//}   	
//    	
    	
    	
    	
    	
    	
    	
    	
//    	try {
//			List<String> peptides = Files.readAllLines(Paths.get("/home/mahmi/Dataset/Beta_amyloid/matches_peptides.faa"));
//			for(int i=0; i<peptides.size(); i+=2){
//				try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/Beta_amyloid/peptides/"+peptides.get(i).replace(">","")+".faa")))){
//					pw.println(peptides.get(i));
//					pw.println(peptides.get(i+1));
//				}catch (IOException e) {
//					e.printStackTrace();
//				}
//				
//			}
//			
//			
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//    	
    	
    	
    	
    	
//    	String sequence1 = "MNRLLNEKVEEFKKGVLKAGWFIEKMFRNSISSLVERN";
//      String sequence2 = "AMANQTSEMLEKSFEVCRMDSKVDDLMNRLLKVEEKG";
////      	String sequence1 = "HHHHHHHCCCHHHHHHH";
////    	String sequence2 = "HHHHHCCCCCCHHHHHH";
//    	String ss1 = "CCCC---HHHHCCCCCEEE-CCCEEECCCCCHHH--CC";
//    	String ss2 = "CCCCCHHHHHHCCCCCEEEECCCEEECCCCCHHHHHCC";
////    	String ss1 = "HHHHCCCC-EEEECCCEEECCCCCHHH";
////    	String ss2 = "HHHHCCCCCEEE-CCCEEECCCCCHHH";
////    	String ss1 = "HHHHHHHCCCHHHHHHH";
////    	String ss2 = "HHHHHCCCCCCHHHHHH";
//
//    	
//    	System.out.println(getSecondarySimilarityScore(sequence1, sequence2, ss1, ss2));
    	
    	
//    	try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/mahmiPeptides.faa")))){
//
//			FileReader in = new FileReader("/home/mahmi/mahmiPeptides.csv");
//		    BufferedReader br = new BufferedReader(in);
//			String line;
//			while ((line = br.readLine()) != null) {
//
//				String[] sp = line.split("\t");
//				pw.println(">MAHMI_PEPTIDE"+sp[0]);
//				pw.println(sp[1]);
//			}
//			
//			in.close();
//			
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    	
    	
    	
//    	try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/Neuropep/MahmiPosVsBDs/mahmiPost_neuropep_hs.nicotinamide")))){
//			List<String> lines = Files.readAllLines(Paths.get("/home/mahmi/Dataset/Neuropep/MahmiPosVsBDs/mahmiPost_neuropep_hs.blastp"));
//			lines.forEach(l -> {				
//				String[] li = l.split("\t");
//				if(li[1].equals("NP03624")){
//					pw.println(li[0]+" "+li[9]+"-"+li[10]);
//				}
//			});
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    	
//    	try{
//    		Date d = new Date();
//    		System.out.println(d.toString());
//    		
//    		//Lectura ficheros de masas
//    		final double sigma = 3.0;
//    		final double cutoff = 0.6;
//    		final String path = "/home/mahmi/tempfiles/spec";///home/mahmi/Dataset/Escherichia_coli/samples/6000PatrickHomoSapiens/Mass";
//	    	P2<List<String>,List<List<Double>>> peaks = readPeaks(path);
//			final List<String> names = peaks._1();
//			
//			//Calculo pairwise	
//			pairwise(peaks, sigma, cutoff);
//			
//			peaks = null;
//			getRuntime().gc();
//			
//			d = new Date();
//			System.out.println(d.toString());
//			
//			byte[][] matrix = peaksInCommon(names.size());
//			
//			
//			System.out.println("#NEXUS\nbegin data;\ndimensions ntax="+names.size()+" nchar="+matrix[0].length+";\nformat datatype=restriction interleave=no gap=-;\nmatrix");
//			
//			for(int i = 0; i < matrix.length; i++){
//				System.out.print(names.get(i)+" ");
//				for(int j = 0; j < matrix[i].length; j++){
//					System.out.print(matrix[i][j]);
//				}
//				System.out.println();
//			}
//			System.out.println(";\nend;");
//			d = new Date();
//			System.out.println(d.toString());
//			
//			
//    	}catch(Exception e){
//			e.printStackTrace();
//		}
    	
    	
    	
//    	List<String> lines;
//		try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/tempfiles/monocytes_modDCs-underexpressed_DAVID_filtered.txt")))){
//			lines = Files.readAllLines(Paths.get("/home/mahmi/tempfiles/monocytes_modDCs-underexpressed_DAVID_filtered.csv"));
//			lines.forEach(l -> {
//				String[] li = l.split(";");
//				String header = li[0].split(":")[0];
//				for(int i=1; i<li.length; i++){
//					pw.println(header+"\t"+li[i]);
//				}
//			});
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    	
    	
    	
//    	List<String> dsm = new ArrayList<>();
//    	List<String> p_dsm = new ArrayList<>();
//    	Map<String, Double> fc = new HashMap<>();
//    	Map<String, String> lps = new HashMap<>();
//    	List<String> p_lps = new ArrayList<>();
//    	List<String> both = new ArrayList<>();
//    	
//    	try {
//			List<String> lines = Files.readAllLines(Paths.get("/home/mahmi/tempfiles/phenotype-data_gene_results_sig_fpkm_plus_1.tsv"));
//			String previous=lines.get(1).split("\t")[1];
//			double[] fpkm = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
//			for(int i=1; i<lines.size(); i++){
//				String[] split = lines.get(i).split("\t");
//				if(split[1].equals(previous)){
//					for(int j=0; j<fpkm.length; j++){
//						fpkm[j]+=Double.parseDouble(lines.get(i).split("\t")[3+j].replaceAll("\"",""));
//					}
//				}else{
//					String add = previous;
//					p_dsm.add(add);
//					for(int j=0; j<fpkm.length; j++){
//						add+=";"+fpkm[j];
//					}
//					dsm.add(add);
//					previous = lines.get(i).split("\t")[1];
//					for(int j=0; j<fpkm.length; j++){
//						fpkm[j]=Double.parseDouble(lines.get(i).split("\t")[3+j].replaceAll("\"",""));
//					}
//					
//				}
//					
//			}	
//			String add = previous;
//			p_dsm.add(add);
//			for(int j=0; j<fpkm.length; j++){
//				add+=";"+fpkm[j];
//			}
//			dsm.add(add);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	try {
//			List<String> lines = Files.readAllLines(Paths.get("/home/mahmi/tempfiles/phenotype-data_gene_results_sig_fpkm_plus_1_lps.tsv"));
//			String previous=lines.get(1).split("\t")[1];
//			double[] fpkm = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
//			for(int i=1; i<lines.size(); i++){
//				String[] split = lines.get(i).split("\t");
//				if(split[1].equals(previous)){
//					for(int j=0; j<fpkm.length; j++){
//						fpkm[j]+=Double.parseDouble(lines.get(i).split("\t")[27+j].replaceAll("\"",""));
//					}
//				}else{
//					String add = "";
//					p_lps.add(previous);
//					for(int j=0; j<fpkm.length; j++){
//						add+=";"+fpkm[j];
//					}
//					lps.put(previous, add);
//					previous = lines.get(i).split("\t")[1];
//					for(int j=0; j<fpkm.length; j++){
//						fpkm[j]=Double.parseDouble(lines.get(i).split("\t")[27+j].replaceAll("\"",""));
//					}
//					
//				}
//					
//			}	
//			String add = "";
//			p_lps.add(previous);
//			for(int j=0; j<fpkm.length; j++){
//				add+=";"+fpkm[j];
//			}
//			lps.put(previous, add);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	
//    	try {
//			List<String> lines = Files.readAllLines(Paths.get("/home/mahmi/tempfiles/phenotype-data_gene_results_sig.tsv"));
//			for(int i=1; i<lines.size(); i++){
//				fc.put(lines.get(i).split("\t")[0], Double.parseDouble(lines.get(i).split("\t")[2]));	
//			}	
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	
//    	try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/tempfiles/dsm_lps_fpkms.tsv")))) {
//    		for(int i=0; i<dsm.size(); i++){
//        		if(p_dsm.contains(dsm.get(i).split(";")[0]) && p_lps.contains(dsm.get(i).split(";")[0]) && Math.abs(fc.get(dsm.get(i).split(";")[0]))>2){
//        			pw.println(dsm.get(i).replaceAll("\"","")+lps.get(dsm.get(i).split(";")[0]));
//        		}
//        	}
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
    	
    	
    	
//    	File folder = new File("/home/mahmi/Dataset/Escherichia_coli/samples/6000PatrickHomoSapiens/Mass");
//		File[] listOfFiles = folder.listFiles();
//		for(int i = 0; i<listOfFiles.length; i++){
//			File file = new File("/home/mahmi/Dataset/Escherichia_coli/samples/6000PatrickHomoSapiens/Masses/"+listOfFiles[i].getName().replaceAll(" ","_").replaceAll(":","-").replace(".faa",".peaks"));
//			try {
//				Files.copy(listOfFiles[i].toPath(), file.toPath());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//    	folder = new File("/home/mahmi/Dataset/Escherichia_coli/samples/6000PatrickHomoSapiens/Masses");
//		listOfFiles = folder.listFiles();
//		try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Dataset/Escherichia_coli/samples/6000PatrickHomoSapiens/out.list")))) {
//			for(int i = 0; i<listOfFiles.length; i++){
//				pw.println("/home/abmiguez/nexus/peakfiles/"+listOfFiles[i].getName());
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
    	
//    	try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/Downloads/phage156_2.embl")))) {
//			BufferedReader fastaReader = new BufferedReader(new FileReader("/home/mahmi/Downloads/phage156.embl"));
//		    String p;
//		    while ((p = fastaReader.readLine()) != null) {
//		    	if(p.length()>80){
//		    		String init="";
//		    		int init_l=0;
//		    		if(p.startsWith("FT                   ")){
//		    			init="FT                   ";
//		    			init_l = 80-init.length();				
//		    		}else{
//		    			init= p.split(" ")[0]+"   ";
//		    			init_l = 80-init.length();		
//		    		}    
//		    		final String c = p.substring(0, 80);
//		    		pw.println(c);
//		    		String c2 = p.substring(80);
//		    		while(c2.length()>init_l){
//		    			String c3 = c2.substring(0, init_l);
//		    			pw.println(init+c3);
//		    			c2 = c2.substring(init_l);
//		    		}
//		    		pw.println(init+c2);
//		    	}else{
//		    		pw.println(p);
//		    	}
//		    }
//		    
//		    fastaReader.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
    	
//    	try (PrintWriter pw = new PrintWriter( new BufferedWriter( new FileWriter("/home/mahmi/tempfiles/chollopaper/chollo_proteins.faa")))) {
//			BufferedReader fastaReader = new BufferedReader(new FileReader("/home/mahmi/tempfiles/chollopaper/chollopaper_proteins.txt"));
//		    String p;
//
//		    while ((p = fastaReader.readLine()) != null) {
//		    	String line[] = p.split("\t");
//				pw.println(">"+line[1]+"|"+line[0]);
//				pw.println(line[2]);		    
//		    }
//		    
//		    fastaReader.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
    	
//    	getMostProbablyTree();
//    	getNeuropep();
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
    	
//    	File folder = new File("/home/mahmi/Dataset/Escherichia_coli/samples/6000PatrickHomoSapiens/Mass");
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
//	  )  		try{
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
