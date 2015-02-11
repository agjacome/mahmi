package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;
import es.uvigo.ei.sing.mahmi.common.serializers.jaxb.NucleobaseFastaAdapter;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@Getter @Wither
@AllArgsConstructor(staticName = "metagenome")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class MetaGenome implements Entity<MetaGenome> {

    private final Identifier id;
    private final Project    project;

    @XmlJavaTypeAdapter(NucleobaseFastaAdapter.class)
    private final Fasta<NucleobaseSequence> fasta;

    @VisibleForJAXB
    public MetaGenome() {
        this(new Identifier(), new Project(), Fasta.empty());
    }

    public static MetaGenome metagenome(
        final Project project, final Fasta<NucleobaseSequence> fasta
    ) {
        return metagenome(Identifier.empty(), project, fasta);
    }

}
