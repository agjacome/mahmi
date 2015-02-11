package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@ToString(exclude = "fasta")
@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor(staticName = "metagenome")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
@Data public final class MetaGenome implements Entity<MetaGenome> {

    private Identifier id;
    private Project    project;

    private Fasta<NucleobaseSequence> fasta;

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
