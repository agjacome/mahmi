package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@Getter @Wither
@AllArgsConstructor(staticName = "metagenomeProteins")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class MetaGenomeProteins implements Entity<MetaGenomeProteins> {

    private final Identifier id;
    private final MetaGenome metagenome;
    private final Protein    protein;
    private final long       counter;

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
