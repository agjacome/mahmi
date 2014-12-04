package es.uvigo.ei.sing.mahmi.common.serializers.jaxb.entities.fasta;

import static es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence.fromString;
import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.fasta.ProteinFasta;

public final class ProteinFastaAdapter extends XmlAdapter<ProteinFasta, Map<String, Integer>> {

    @Override
    public Map<String, Integer> unmarshal(final ProteinFasta fasta) throws Exception {
        return fasta.getSequences().entrySet().stream().collect(toMap(
            e -> e.getKey().toString(), Entry::getValue
        ));
    }

    @Override
    public ProteinFasta marshal(final Map<String, Integer> map) throws Exception {
        return ProteinFasta.of(map.entrySet().stream().collect(toMap(
            e -> fromString(e.getKey()).valueE("Invalid amino-acid sequence"),
            Entry::getValue
        )));
    }

}
