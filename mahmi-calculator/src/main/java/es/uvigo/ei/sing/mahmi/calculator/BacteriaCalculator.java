package es.uvigo.ei.sing.mahmi.calculator;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import lombok.val;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;

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
        catch(ClassNotFoundException ex){
            JOptionPane.showMessageDialog(null, ex, "Error1 en la Conexión con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex, "Error2 en la Conexión con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex, "Error3 en la Conexión con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        finally{
            return conexion;
        }
    }
	
	public void CreateMWFiles(String path){
		if(conection!=null)
        {
        	FileWriter fichero1 = null;
	        PrintWriter pw1 = null;
	        FileWriter fichero2 = null;
	        PrintWriter pw2 = null;
	        FileWriter fichero3 = null;
	        PrintWriter pw3 = null;
	        PeptideCalculator a=new PeptideCalculator();
			try {
				for(int i=1;i<4;i++){
					val stmt = conection.createStatement();
					val rs = stmt.executeQuery("select distinct peptide_sequence "
													+ "from digestions natural "
													+ "join peptides natural join "
													+ "metagenome_proteins where "
													+ "metagenome_id="+i);					

					String name="Bacillus_thuringiensis";
					if(i==2) name = "Bacillus_cereus";
					if(i==3) name = "Bacillus_anthracis";
					
					fichero1 = new FileWriter(path+"/1-9/"+name+".peaks");
			        pw1 = new PrintWriter(fichero1);
					  
			        fichero2 = new FileWriter(path+"/10-20/"+name+".peaks");
			        pw2 = new PrintWriter(fichero2);
			           

					fichero3 = new FileWriter(path+"/21-30/"+name+".peaks");
				    pw3 = new PrintWriter(fichero3);
			           
					
				    List<PeptideMass> aaa=new ArrayList<PeptideMass>();
					while (rs.next()) {						
						String peptide = rs.getString("peptide_sequence");
						aaa.add(new PeptideMass(a.calculateMW(peptide),AminoAcidSequence.fromString(peptide).some()));
							  
					}
					
					Collections.sort(aaa);
					
					for(PeptideMass b: aaa){
						if(b.getSequence().toString().length()<=9){
							pw1.println(b.getMW());
								  
						}else{
							if(b.getSequence().toString().length()>9 && b.getSequence().toString().length()<=20){
							     pw2.println(b.getMW());
							}else{
								 pw3.println(b.getMW());
							}
						}
					}
					
					try {
						if (null != fichero1)
							fichero1.close();
					    if (null != fichero2)
						    fichero2.close();
					    if (null != fichero3)
						     fichero3.close();
					 } catch (Exception e2) {
					    e2.printStackTrace();
					 }
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
		        e.printStackTrace();
	        } 
		}
	}	
}
