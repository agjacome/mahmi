package es.uvigo.ei.sing.mahmi.http.wrappers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import es.uvigo.ei.sing.mahmi.common.entities.MetagenomeInformation;
import es.uvigo.ei.sing.mahmi.common.entities.MetagenomeMIxS;
import es.uvigo.ei.sing.mahmi.common.entities.Protein;
import es.uvigo.ei.sing.mahmi.common.entities.ProteinInformation;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "wrap")
@XmlRootElement(name = "sourceProtein") @XmlAccessorType(XmlAccessType.FIELD)
public final class SourceProtein {

	private final Protein protein;
    private final ProteinInformation uniprotData;
    private final MetagenomeMIxS metagenomeMIxSData;
    private final MetagenomeInformation additionalMetagenomeInformation;

    @VisibleForJAXB public SourceProtein() {
    	this.protein = new Protein();
        this.uniprotData = new ProteinInformation();
        this.metagenomeMIxSData = new MetagenomeMIxS();
        this.additionalMetagenomeInformation = new MetagenomeInformation();
    }

}