package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

import static es.uvigo.ei.sing.mahmi.common.entities.MetaGenome.MetaGenome;
import static es.uvigo.ei.sing.mahmi.common.entities.Project.Project;
import static es.uvigo.ei.sing.mahmi.common.entities.Protein.Protein;
import static es.uvigo.ei.sing.mahmi.common.utils.Contracts.requireNonNull;

@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class Translation extends Entity {

    private final MetaGenome metaGenome;
    private final Protein    protein;
    private final long       counter;

    @VisibleForJAXB
    public Translation() {
        this(
            Identifier.empty(),
            MetaGenome(Project("", ""), Fasta.empty()),
            Protein(AminoAcidSequence.empty()), 0L
        );
    }

    public Translation(
        final Identifier id,
        final MetaGenome metagenome,
        final Protein    protein,
        final long       counter
    ) {
        super(id);

        this.metaGenome = requireNonNull(metagenome, "Translation metagenome cannot be NULL");
        this.protein    = requireNonNull(protein, "Translation protein cannot be NULL");
        this.counter    = counter;
    }

    public static Translation Translation(
        final MetaGenome metagenome, final Protein protein
    ) {
        return new Translation(Identifier.empty(), metagenome, protein, 0L);
    }

    public static Translation Translation(
        final Identifier id,
        final MetaGenome metagenome,
        final Protein    protein
    ) {
        return new Translation(id, metagenome, protein, 0L);
    }

    public static Translation Translation(
        final MetaGenome metagenome,
        final Protein    protein,
        final long       counter
    ) {
        return new Translation(Identifier.empty(), metagenome, protein, counter);
    }

    public static Translation Translation(
        final Identifier id,
        final MetaGenome metagenome,
        final Protein    protein,
        final long       counter
    ) {
        return new Translation(id, metagenome, protein, counter);
    }

    public MetaGenome getMetaGenome() {
        return metaGenome;
    }

    public Protein getProtein() {
        return protein;
    }

    public long getCounter() {
        return counter;
    }

    @Override
    public int hashCode() {
        // Entity overridden for special case: counter is irrelevant to equality
        final int metaGenomeHash = metaGenome.hashCode();
        final int proteinHash    = protein.hashCode();
        return 41 * (41 + metaGenomeHash) + proteinHash;
    }

    @Override
    public boolean equals(final Object other) {
        // Entity overridden for special case: counter is irrelevant to equality
        if (this  == other) return true;
        if (other == null ) return false;

        if (getClass() != other.getClass()) return false;

        final Translation that = (Translation) other;
        return this.metaGenome.equals(that.metaGenome)
            && this.protein.equals(that.protein);
    }

}
