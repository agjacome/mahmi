package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor(staticName = "metagenomeProteins")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
@Data public final class MetaGenomeProteins implements Entity<MetaGenomeProteins> {

	private Identifier id;
    private MetaGenome metagenome;
    private Protein    protein;
    private long       counter;

    @VisibleForJAXB public MetaGenomeProteins() {
        this(Identifier.empty(), new MetaGenome(), new Protein(), 0L);
    }

    public static MetaGenomeProteins metagenomeProteins(
        final MetaGenome metagenome, final Protein protein
    ) {
        return metagenomeProteins(Identifier.empty(), metagenome, protein, 0L);
    }

    public static MetaGenomeProteins metagenomeProteins(
        final Identifier id,
        final MetaGenome metagenome,
        final Protein    protein
    ) {
        return metagenomeProteins(id, metagenome, protein, 0L);
    }

    public static MetaGenomeProteins metagenomeProteins(
    	final MetaGenome metagenome,
        final Protein    protein,
        final long       counter
    ) {
        return metagenomeProteins(Identifier.empty(), metagenome, protein, counter);
    }

}
