package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleotideSequence;
import es.uvigo.ei.sing.mahmi.common.serializers.jaxb.NucleotideFastaAdapter;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

import static es.uvigo.ei.sing.mahmi.common.entities.Project.Project;
import static es.uvigo.ei.sing.mahmi.common.utils.Contracts.requireNonNull;

@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class MetaGenome extends Entity {

    private final Project project;

    @XmlJavaTypeAdapter(NucleotideFastaAdapter.class)
    private final Fasta<NucleotideSequence> fasta;

    @VisibleForJAXB
    public MetaGenome() {
        this(Identifier.empty(), Project("", ""), Fasta.empty());
    }

    private MetaGenome(
        final Identifier id,
        final Project    project,
        final Fasta<NucleotideSequence> fasta
    ) {
        super(id);

        this.project = requireNonNull(project, "MetaGenome project cannot be NULL");
        this.fasta   = requireNonNull(fasta, "MetaGenome fasta cannot be NULL");
    }

    public static MetaGenome MetaGenome(
        final Identifier id,
        final Project    project,
        final Fasta<NucleotideSequence> fasta
    ) {
        return new MetaGenome(id, project, fasta);
    }

    public static MetaGenome MetaGenome(
        final Project project, final Fasta<NucleotideSequence> fasta
    ) {
        return new MetaGenome(Identifier.empty(), project, fasta);
    }

    public Project getProject() {
        return project;
    }

    public Fasta<NucleotideSequence> getFasta() {
        return fasta;
    }

}
