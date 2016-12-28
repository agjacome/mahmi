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

@Getter @Wither
@AllArgsConstructor(staticName = "metagenomeMIxS")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class MetagenomeMIxS implements Entity<MetagenomeMIxS> {
	@XmlTransient
    private Identifier id;
    private String   identifier;
    private String   investigation_type;
    private String 	 project_name;
    private String 	 sequencing_method;
    private String 	 collection_date;
    private String 	 enviromental_package;
    private String 	 latitude;
    private String 	 longitude;
    private String 	 location;
    private String 	 biome;
    private String 	 feature;
    private String 	 material;

    @VisibleForJAXB public MetagenomeMIxS() {
    }

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
