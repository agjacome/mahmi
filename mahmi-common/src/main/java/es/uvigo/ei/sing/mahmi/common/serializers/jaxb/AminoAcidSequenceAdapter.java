package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import java.util.function.Supplier;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;

public final class AminoAcidSequenceAdapter extends XmlAdapter<String, AminoAcidSequence> {

    @Override
    public AminoAcidSequence unmarshal(final String str) throws Exception {
        return AminoAcidSequence.fromString(str).orElseThrow(invalid(str));
    }

    @Override
    public String marshal(final AminoAcidSequence seq) throws Exception {
        return seq.toString();
    }

    private Supplier<IllegalArgumentException> invalid(final String str) {
        return () -> new IllegalArgumentException(String.format(
            "Invalid aminoacid sequence %s", str
        ));
    }

}
