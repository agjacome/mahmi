package es.uvigo.ei.sing.mahmi.common.entities;

import static es.uvigo.ei.sing.mahmi.common.entities.Project.project;
import static fj.P.p;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import es.uvigo.ei.sing.mahmi.common.entities.fasta.GenomeFasta;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;
import fj.P3;

@Getter
@ToString
@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class MetaGenome {

    private final Identifier  id;
    private final Project     project;
    private final GenomeFasta genomeFasta;

    @VisibleForJAXB
    public MetaGenome() {
        this(Identifier.empty(), project("", ""), GenomeFasta.empty());
    }

    public static MetaGenome metaGenome(
        final Project project, final GenomeFasta fasta
    ) {
        return new MetaGenome(Identifier.empty(), project, fasta);
    }

    public static MetaGenome metaGenome(
        final int id, final Project project, final GenomeFasta fasta
    ) {
        return metaGenome(project, fasta).withId(id);
    }

    public MetaGenome withId(final int id) {
        return new MetaGenome(Identifier.of(id), project, genomeFasta);
    }

    public P3<Identifier, Project, GenomeFasta> toProduct() {
        return p(id, project, genomeFasta);
    }

}
