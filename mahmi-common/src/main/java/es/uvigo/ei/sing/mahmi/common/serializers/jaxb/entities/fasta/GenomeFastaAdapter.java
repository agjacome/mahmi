package es.uvigo.ei.sing.mahmi.common.serializers.jaxb.entities.fasta;

import static es.uvigo.ei.sing.mahmi.common.entities.sequences.DNASequence.fromString;
import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.fasta.GenomeFasta;

public class GenomeFastaAdapter extends XmlAdapter<GenomeFasta, Map<String, Integer>>  {

    @Override
    public Map<String, Integer> unmarshal(final GenomeFasta fasta) throws Exception {
        return fasta.getSequences().entrySet().stream().collect(toMap(
            e -> e.getKey().toString(), Entry::getValue
        ));
    }

    @Override
    public GenomeFasta marshal(final Map<String, Integer> map) throws Exception {
        return GenomeFasta.of(map.entrySet().stream().collect(toMap(
            e -> fromString(e.getKey()).valueE("Invalid amino-acid sequence"),
            Entry::getValue
        )));
    }

}
