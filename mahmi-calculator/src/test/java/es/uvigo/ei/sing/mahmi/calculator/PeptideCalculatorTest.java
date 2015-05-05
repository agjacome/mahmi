package es.uvigo.ei.sing.mahmi.calculator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import junit.framework.TestCase;
import lombok.val;

/**
 * Unit test for simple App.
 */
public class PeptideCalculatorTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    

    /**
     * Rigourous Test :-)
     */
	
	
    public void testApp() throws IOException
    {
//    	String path="/home/sing/Escritorio/Bacterias/Estudio/Plasmidicas/Nuevo";
//    	File dirr = new File(path);
//    	String[] ficherros = dirr.list();
//    	
//    	for(int z=0;z<ficherros.length;z++){    	
//	    	File dir = new File(path+"/"+ficherros[z]);
//	    	String[] ficheros = dir.list();    	
//	    	List<String> lista=new ArrayList<String>();
//	    	for(int i=0;i<ficheros.length;i++){
//	    		if(ficheros[i].endsWith(".faa") && !ficheros[i].equals("plasmics.faa")){   		
//	    			System.out.println(ficheros[i]);
//		    		List<String> aux=Files.readAllLines(Paths.get(path+"/"+ficherros[z]+"/"+ficheros[i]),Charset.defaultCharset());
//		    		for(String a:aux){
//		    			lista.add(a);
//		    		}
//		    		File fichero = new File(path+"/"+ficherros[z]+"/"+ficheros[i]);
//		    		fichero.delete();
//	    		}
//	    	}
//	    	try{
//	    		FileWriter f = new FileWriter(path+"/"+ficherros[z]+"/plasmics.faa");
//		        PrintWriter pw = new PrintWriter(f);
//		        for(String b:lista){
//		        	pw.println(b);
//		        }
//		        try {
//					if (null != f)
//						f.close();
//				 } catch (Exception e2) {
//				 }
//	    	}catch(Exception e){
//	    		e.printStackTrace();
//	    	}
//    	}
    	
    	
    	
//		String number="1_9";
//		String number="10_20";
//		String number="21_30";
//		String number="31_40";
//		String number="41_50";
		String number="18_42";
    	
//    	BacteriaCalculator bc=new BacteriaCalculator();
//    	bc.CreateMWFiles("/home/sing/Escritorio/Bacterias/Estudio/Plasmidics");
        try {
        	FileWriter f = new FileWriter("/home/sing/Escritorio/Bacterias/Estudio/Cromosomics/c"+number+".csv");
	        PrintWriter pw = new PrintWriter(f);
	        pw.println("MW;Bacillus_cereus_03BB102_uid59299.peaks;"
	        		+ "Bacillus_cereus_AH187_uid58753.peaks;"
	        		+ "Bacillus_cereus_AH820_uid58751.peaks;"
	        		+ "Bacillus_cereus_ATCC_10987_uid57673.peaks;"
	        		+ "Bacillus_cereus_ATCC_14579_uid57975.peaks;"
	        		+ "Bacillus_cereus_B4264_uid58757.peaks;"
	        		+ "Bacillus_cereus_E33L_uid58103.peaks;"
	        		+ "Bacillus_cereus_F837_76_uid83611.peaks;"
	        		+ "Bacillus_cereus_FRI_35_uid173403.peaks;"
	        		+ "Bacillus_cereus_G9842_uid58759.peaks;"
	        		+ "Bacillus_cereus_NC7401_uid82815.peaks;"
	        		+ "Bacillus_cereus_Q1_uid58529.peaks;"
	        		+ "Bacillus_cereus_biovar_anthracis_CI_uid50615.peaks;"
	        		+ "Bacillus_subtilis_168_uid57675.peaks;"
	        		+ "Bacillus_thuringiensis_Al_Hakam_uid58795.peaks;"
	        		+ "Bacillus_thuringiensis_BMB171_uid49135.peaks;"
	        		+ "Bacillus_thuringiensis_Bt407_uid177931.peaks;"
	        		+ "Bacillus_thuringiensis_HD_771_uid173374.peaks;"
	        		+ "Bacillus_thuringiensis_HD_789_uid173860.peaks;"
	        		+ "Bacillus_thuringiensis_MC28_uid176369.peaks;"
	        		+ "Bacillus_thuringiensis_YBT_1518_uid229419.peaks;"
	        		+ "Bacillus_thuringiensis_serovar_IS5056_uid190186.peaks;"
	        		+ "Bacillus_thuringiensis_serovar_chinensis_CT_43_uid158151.peaks;"
	        		+ "Bacillus_thuringiensis_serovar_finitimus_YBT_020_uid158875.peaks;"
	        		+ "Bacillus_thuringiensis_serovar_konkukian_97_27_uid58089.peaks;"
	        		+ "Bacillus_thuringiensis_serovar_kurstaki_HD73_uid189188.peaks");
	        List<String>sp=Files.readAllLines(Paths.get("/home/sing/Escritorio/Bacterias/Estudio/Cromosomics/consensus"+number+".csv"));
			int i=1;
			for(String s:sp){
				if(i!=6){
					i++;
				}else{
					String[] spl=s.split("\\t",-1);
					pw.print(spl[0]);
					for(int j=5;j<30;j++){
						if(!spl[j].equals("")){
							pw.print(";1");
						}else{
							pw.print(";0");
						}
					}
					pw.print("\n");
				}
			}
			try {
				if (null != f)
					f.close();
			 } catch (Exception e2) {
			 }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	try {
    		FileWriter f = new FileWriter("/home/sing/Escritorio/Bacterias/Estudio/Cromosomics/"+number+".nexus");
            PrintWriter pw = new PrintWriter(f);
            pw.println("#NEXUS");
            pw.println("begin data;");
            List<String>sp=Files.readAllLines(Paths.get("/home/sing/Escritorio/Bacterias/Estudio/Cromosomics/c"+number+".csv"));
            val size=sp.size()-1;
            pw.println("dimensions ntax=32 nchar="+size+";");
            pw.println("format datatype=restriction interleave=no gap=-;");
            pw.println("matrix"); 
            int i=1;
            String a8="Bacillus_cereus_03BB102_uid59299.peaks ";
            String a9="Bacillus_cereus_AH187_uid58753.peaks ";
            String a10="Bacillus_cereus_AH820_uid58751.peaks ";
            String a11="Bacillus_cereus_ATCC_10987_uid57673.peaks ";
            String a12="Bacillus_cereus_ATCC_14579_uid57975.peaks ";
            String a13="Bacillus_cereus_B4264_uid58757.peaks ";
            String a14="Bacillus_cereus_E33L_uid58103.peaks ";
            String a15="Bacillus_cereus_F837_76_uid83611.peaks ";
            String a16="Bacillus_cereus_FRI_35_uid173403.peaks ";
            String a17="Bacillus_cereus_G9842_uid58759.peaks ";
            String a18="Bacillus_cereus_NC7401_uid82815.peaks ";
            String a19="Bacillus_cereus_Q1_uid58529.peaks ";
            String a20="Bacillus_cereus_biovar_anthracis_CI_uid50615.peaks ";
            String a21="Bacillus_subtilis_168_uid57675.peaks ";
            String a22="Bacillus_thuringiensis_Al_Hakam_uid58795.peaks ";
            String a23="Bacillus_thuringiensis_BMB171_uid49135.peaks ";
            String a24="Bacillus_thuringiensis_Bt407_uid177931.peaks ";
            String a25="Bacillus_thuringiensis_HD_771_uid173374.peaks ";
            String a26="Bacillus_thuringiensis_HD_789_uid173860.peaks ";
            String a27="Bacillus_thuringiensis_MC28_uid176369.peaks ";
            String a28="Bacillus_thuringiensis_YBT_1518_uid229419.peaks ";
            String a29="Bacillus_thuringiensis_serovar_IS5056_uid190186.peaks ";
            String a30="Bacillus_thuringiensis_serovar_chinensis_CT_43_uid158151.peaks ";
            String a31="Bacillus_thuringiensis_serovar_finitimus_YBT_020_uid158875.peaks ";
            String a32="Bacillus_thuringiensis_serovar_konkukian_97_27_uid58089.peaks ";
            String a33="Bacillus_thuringiensis_serovar_kurstaki_HD73_uid189188.peaks ";

			for(String s:sp){
				if(i!=2){
					i++;
				}else{
					String[]aux=s.split(";");
					a8=a8+aux[7];
					a9=a9+aux[8];
					a10=a10+aux[9];
					a11=a11+aux[10];
					a12=a12+aux[11];
					a13=a13+aux[12];
					a14=a14+aux[13];
					a15=a15+aux[14];
					a16=a16+aux[15];
					a17=a17+aux[16];
					a18=a18+aux[17];
					a19=a19+aux[18];
					a20=a20+aux[19];
					a21=a21+aux[20];
					a22=a22+aux[21];
					a23=a23+aux[22];
					a24=a24+aux[23];
					a25=a25+aux[24];
					a26=a26+aux[25];
					a27=a27+aux[26];
					a28=a28+aux[27];
					a29=a29+aux[28];
					a30=a30+aux[29];
					a31=a31+aux[30];
					a32=a32+aux[31];
					a33=a33+aux[32];
				}
			}
			pw.println(a8);
			pw.println(a9);
			pw.println(a10);
			pw.println(a11);
			pw.println(a12);
			pw.println(a13);
			pw.println(a14);
			pw.println(a15);
			pw.println(a16);
			pw.println(a17);
			pw.println(a18);
			pw.println(a19);
			pw.println(a20);
			pw.println(a21);
			pw.println(a22);
			pw.println(a23);
			pw.println(a24);
			pw.println(a25);
			pw.println(a26);
			pw.println(a27);
			pw.println(a28);
			pw.println(a29);
			pw.println(a30);
			pw.println(a31);
			pw.println(a32);
			pw.println(a33);
			pw.println(";");
			pw.println("end;");
			
			try {
				if (null != f)
					f.close();
			 } catch (Exception e2) {
			 }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
   	assertTrue( true );
    }
}
