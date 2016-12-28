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
@AllArgsConstructor(staticName = "proteinInformation")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class ProteinInformation implements Entity<ProteinInformation> {
	@XmlTransient
    private Identifier id;
	@XmlTransient
    private Protein    protein;
    private String uniprotId;
    private String uniprotOrganism;
    private String uniprotProtein;
    private String uniprotGene;

    @VisibleForJAXB public ProteinInformation() {
    }

    public static ProteinInformation proteinInformation(
        final Protein    protein,
        final String	 uniprotId,
        final String     uniprotOrganism,
        final String     uniprotProtein,
        final String     uniprotGene
    ) {
        return proteinInformation(Identifier.empty(), protein, uniprotId, uniprotOrganism, uniprotProtein, uniprotGene);
    }

}
