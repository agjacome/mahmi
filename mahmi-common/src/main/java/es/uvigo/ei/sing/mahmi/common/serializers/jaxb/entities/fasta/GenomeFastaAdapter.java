package es.uvigo.ei.sing.mahmi.common.serializers.jaxb.entities.fasta;

import static es.uvigo.ei.sing.mahmi.common.utils.extensions.CollectionsExtensionMethods.mapKeys;

import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.fasta.GenomeFasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.DNASequence;

public class GenomeFastaAdapter extends XmlAdapter<GenomeFasta, Map<String, Integer>>  {

    @Override
    public Map<String, Integer> unmarshal(final GenomeFasta fasta) throws Exception {
        return mapKeys(fasta.getSequences(), DNASequence::toString);
    }

    @Override
    public GenomeFasta marshal(final Map<String, Integer> map) throws Exception {
        return GenomeFasta.of(mapKeys(map, DNASequence::fromString));
    }

}
