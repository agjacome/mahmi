package es.uvigo.ei.sing.mahmi.common.serializers.jaxb.entities.sequences;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;

public final class AminoAcidSequenceAdapter extends XmlAdapter<String, AminoAcidSequence> {

    @Override
    public AminoAcidSequence unmarshal(final String str) throws Exception {
        return AminoAcidSequence.fromString(str);
    }

    @Override
    public String marshal(final AminoAcidSequence seq) throws Exception {
        return seq.toString();
    }

}
