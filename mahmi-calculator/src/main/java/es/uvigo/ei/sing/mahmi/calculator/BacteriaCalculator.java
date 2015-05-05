package es.uvigo.ei.sing.mahmi.calculator;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;

@Slf4j
public class BacteriaCalculator {
	final Connection conection=GetConnection();
	
	@SuppressWarnings("finally")
	public static Connection GetConnection(){
        Connection conexion=null;     
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String servidor = "jdbc:mysql://localhost/mahmi";
            String usuarioDB="root";
            String passwordDB="sing33$";
            conexion= DriverManager.getConnection(servidor,usuarioDB,passwordDB);
        }
        catch(ClassNotFoundException e){
        	log.error(e.getMessage());
        	conexion=null;
        }
        catch(SQLException e){
        	log.error(e.getMessage());
        	conexion=null;
        }
        catch(Exception e){
        	log.error(e.getMessage());
        	conexion=null;
        }
        finally{
            return conexion;
        }
    }
	
	public void CreateMWFiles(String path){
		if(conection!=null)
        {
        	FileWriter f1_9 = null;
	        PrintWriter pw1_9= null;
	       /* FileWriter f10_20= null;
	        PrintWriter pw10_20= null;
	        FileWriter f21_30= null;
	        PrintWriter pw21_30= null;*/
	        PeptideCalculator pc=new PeptideCalculator();
			try {
				for(int i=1;i<33;i++){
					val stmt = conection.createStatement();
					val rs = stmt.executeQuery("select distinct peptide_sequence "
													+ "from digestions natural "
													+ "join peptides natural join "
													+ "metagenome_proteins where "
													+ "metagenome_id="+i);					

					String name="Bacillus_thuringiensis_HD_771_uid173374";
					if(i==2) name="Bacillus_cereus_E33L_uid58103";
					if(i==3) name="Bacillus_thuringiensis_MC28_uid176369";
					if(i==4) name="Bacillus_subtilis_natto_BEST195_uid183001";
					if(i==5) name="Bacillus_cereus_B4264_uid58757";
					if(i==6) name="Bacillus_thuringiensis_serovar_konkukian_97_27_uid58089";
					if(i==7) name="Bacillus_cereus_03BB102_uid59299";
					if(i==8) name="Bacillus_cereus_NC7401_uid82815";
					if(i==9) name="Bacillus_anthracis_A0248_uid59385";
					if(i==10) name="Bacillus_anthracis_Ames_uid57909";
					if(i==11) name="Bacillus_cereus_FRI_35_uid173403";
					if(i==12) name="Bacillus_thuringiensis_serovar_chinensis_CT_43_uid158151";
					if(i==13) name="Bacillus_thuringiensis_YBT_1518_uid229419";
					if(i==14) name="Bacillus_cereus_Q1_uid58529";
					if(i==15) name="Bacillus_anthracis__Ames_Ancestor__uid58083";
					if(i==16) name="Bacillus_cereus_ATCC_10987_uid57673";
					if(i==17) name="Bacillus_cereus_G9842_uid58759";
					if(i==18) name="Bacillus_cereus_AH820_uid58751";
					if(i==19) name="Bacillus_thuringiensis_serovar_finitimus_YBT_020_uid158875";
					if(i==20) name="Bacillus_anthracis_Sterne_uid58091";
					if(i==21) name="Bacillus_cereus_F837_76_uid83611";
					if(i==22) name="Bacillus_anthracis_H9401_uid162021";
					if(i==23) name="Bacillus_thuringiensis_serovar_kurstaki_HD73_uid189188";
					if(i==24) name="Bacillus_thuringiensis_HD_789_uid173860";
					if(i==25) name="Bacillus_anthracis_CDC_684_uid59303";
					if(i==26) name="Bacillus_thuringiensis_Bt407_uid177931";
					if(i==27) name="Bacillus_cereus_ATCC_14579_uid57975";
					if(i==28) name="Bacillus_thuringiensis_Al_Hakam_uid58795";
					if(i==29) name="Bacillus_thuringiensis_serovar_IS5056_uid190186";
					if(i==30) name="Bacillus_cereus_biovar_anthracis_CI_uid50615";
					if(i==31) name="Bacillus_cereus_AH187_uid58753";
					if(i==32) name="Bacillus_thuringiensis_BMB171_uid49135";
				
					f1_9 = new FileWriter(path+"/18-42/"+name+".peaks");
			        pw1_9 = new PrintWriter(f1_9);
					  
			        /*f10_20 = new FileWriter(path+"/41-50/"+name+".peaks");
			        pw10_20 = new PrintWriter(f10_20);
			           

					f21_30 = new FileWriter(path+"/51-60/"+name+".peaks");
				    pw21_30 = new PrintWriter(f21_30);*/
			           
					
				    List<PeptideMass> massList=new ArrayList<PeptideMass>();
					while (rs.next()) {						
						String peptide = rs.getString("peptide_sequence");
						massList.add(new PeptideMass(pc.calculateMW(peptide),AminoAcidSequence.fromString(peptide).some())); 
					}
					
					Collections.sort(massList);
					Set<PeptideMass> linkedHashSet = new LinkedHashSet<PeptideMass>();
					linkedHashSet.addAll(massList);
					massList.clear();
					massList.addAll(linkedHashSet); 
					
					for(PeptideMass mass: massList){
						if(mass.getSequence().asString().length()>17 &&mass.getSequence().asString().length()<=42){
							pw1_9.println(mass.getMW());
								  
						}/*else{
							if(mass.getSequence().asString().length()>40 && mass.getSequence().asString().length()<=50){
							     pw10_20.println(mass.getMW());
							}else{
								if(mass.getSequence().asString().length()>50 && mass.getSequence().asString().length()<=60){
								     pw21_30.println(mass.getMW());
								}
							}
						}*/
					}
					
					try {
						if (null != f1_9)
							f1_9.close();
					   /* if (null != f10_20)
						    f10_20.close();
					    if (null != f21_30)
						     f21_30.close();*/
					 } catch (Exception e2) {
						 log.error(e2.getMessage());
					 }
				}
			} catch (SQLException e) {
				log.error(e.getMessage());
			} catch (Exception e) {
				log.error(e.getMessage());
	        } 
		}
	}	
}
