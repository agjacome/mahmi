package es.uvigo.ei.sing.mahmi.common.serializers.jaxb.entities.sequences;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.DNASequence;

public final class DNASequenceAdapter extends XmlAdapter<String, DNASequence> {

    @Override
    public DNASequence unmarshal(final String str) throws Exception {
        return DNASequence.fromString(str).valueE("Invalid DNA nucleobase sequence: " + str);
    }

    @Override
    public String marshal(final DNASequence seq) throws Exception {
        return seq.toString();
    }

}
