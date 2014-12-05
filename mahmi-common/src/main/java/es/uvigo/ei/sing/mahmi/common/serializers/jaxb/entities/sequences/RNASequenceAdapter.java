package es.uvigo.ei.sing.mahmi.common.serializers.jaxb.entities.sequences;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.RNASequence;

public final class RNASequenceAdapter extends XmlAdapter<String, RNASequence> {

    @Override
    public RNASequence unmarshal(final String str) throws Exception {
        return RNASequence.fromString(str);
    }

    @Override
    public String marshal(final RNASequence seq) throws Exception {
        return seq.toString();
    }

}
