package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

/**
 * {@linkplain MetagenomeMIxS} is a class that represents a metagenome MIxS information
 * 
 * @author Aitor Blanco-Miguez
 * 
 * @see Entity
 * @see Identifier
 * @see Protein
 *
 */
@Getter @Wither
@AllArgsConstructor(staticName = "metagenomeMIxS")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class MetagenomeMIxS implements Entity<MetagenomeMIxS> {
	/**
	 * The identifier of the MIxS information
	 */
	@XmlTransient
    private Identifier id;
	
    /**
     * The name of the metagenome
     */
    private String   identifier;
    
    /**
     * The investigation type
     */
    private String   investigation_type;
    
    /**
     * The name of the investigation project
     */
    private String 	 project_name;
    
    /**
     * The sequencing method
     */
    private String 	 sequencing_method;
    
    /**
     * The collection date
     */
    private String 	 collection_date;
    
    /**
     * The environmental package
     */
    private String 	 enviromental_package;
    
    /**
     * The metagenome location latitude
     */
    private String 	 latitude;
    
    /**
     * The metagenome location longitude
     */
    private String 	 longitude;
    
    /**
     * The metagenome location
     */
    private String 	 location;
    
    /**
     * The biome information
     */
    private String 	 biome;
    
    /**
     * The feature information
     */
    private String 	 feature;
    
    /**
     * The material information
     */
    private String 	 material;

    /**
     * {@linkplain MetagenomeMIxS} default constructor
     */
    @VisibleForJAXB public MetagenomeMIxS() {
    }

    /**
     * Constructs a new instance of {@linkplain MetagenomeMIxS} without {@link Identifier}
     * 
     * @param identifier The name of the metagenome
     * @param investigation_type The investigation type
     * @param project_name The name of the investigation project
     * @param sequencing_method The sequencing method
     * @param collection_date The collection date
     * @param enviromental_package The environmental package
     * @param latitude The metagenome location latitude
     * @param longitude The metagenome location longitude
     * @param location The metagenome location
     * @param biome The biome information
     * @param feature The feature information
     * @param material The material information
     * @return A new instance of {@linkplain MetagenomeMIxS}
     */
    public static MetagenomeMIxS metagenomeInformation(
	    final String     identifier,
	    final String     investigation_type,
	    final String 	 project_name,
	    final String 	 sequencing_method,
	    final String 	 collection_date,
	    final String 	 enviromental_package,
	    final String 	 latitude,
	    final String 	 longitude,
	    final String 	 location,
	    final String 	 biome,
	    final String 	 feature,
	    final String 	 material ) {
        return metagenomeMIxS(Identifier.empty(), identifier, investigation_type, project_name, 
        					  sequencing_method, collection_date, enviromental_package,
        					  latitude, longitude, location, biome, feature, material );
    }

}
