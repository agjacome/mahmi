package es.uvigo.ei.sing.mahmi.common.serializers.jaxb.entities.fasta;

import static es.uvigo.ei.sing.mahmi.common.utils.extensions.CollectionsExtensionMethods.mapKeys;

import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.fasta.ProteinFasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;

public final class ProteinFastaAdapter extends XmlAdapter<ProteinFasta, Map<String, Long>> {

    @Override
    public Map<String, Long> unmarshal(final ProteinFasta fasta) throws Exception {
        return mapKeys(fasta.getSequences(), AminoAcidSequence::toString);
    }

    @Override
    public ProteinFasta marshal(final Map<String, Long> map) throws Exception {
        return ProteinFasta.of(mapKeys(map, AminoAcidSequence::fromString));
    }

}
