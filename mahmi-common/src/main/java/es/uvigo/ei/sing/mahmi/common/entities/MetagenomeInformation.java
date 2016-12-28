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
@AllArgsConstructor(staticName = "metagenomeInformation")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class MetagenomeInformation implements Entity<MetagenomeInformation> {
	@XmlTransient
    private Identifier id;
	@XmlTransient
    private Protein  protein;
    private String   geneLength;
    private String 	 geneCompleteness;
    private String 	 geneOrigin;
    private String 	 speciesAnnotationPhylum;
    private String 	 speciesAnnotationGenus;
    private String 	 keggAnnotationPhylum;
    private String 	 sampleOcurrenceFrequency;
    private String 	 individualOcurrenceFrequency;
    private String 	 keggFunctionalCategory;

    @VisibleForJAXB public MetagenomeInformation() {
    }

    public static MetagenomeInformation metagenomeInformation(
	    final Protein protein,
	    final String  geneLength,
	    final String  geneCompleteness,
	    final String  geneOrigin,
	    final String  speciesAnnotationPhylum,
	    final String  speciesAnnotationGenus,
	    final String  keggAnnotationPhylum,
	    final String  sampleOcurrenceFrequency,
	    final String  individualOcurrenceFrequency,
	    final String  keggFunctionalCategory ) {
        return metagenomeInformation(Identifier.empty(), protein, geneLength, 
					        		 geneCompleteness, geneOrigin, speciesAnnotationPhylum, speciesAnnotationGenus, 
					        		 keggAnnotationPhylum, sampleOcurrenceFrequency,  individualOcurrenceFrequency,
					        		 keggFunctionalCategory  );
    }

}
