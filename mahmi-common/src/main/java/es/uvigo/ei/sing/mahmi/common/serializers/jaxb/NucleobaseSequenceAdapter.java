package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import java.util.function.Supplier;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleotideSequence;

public final class NucleobaseSequenceAdapter extends XmlAdapter<String, NucleotideSequence> {

    @Override
    public NucleotideSequence unmarshal(final String str) throws Exception {
        return NucleotideSequence.fromString(str).orElseThrow(invalid(str));
    }

    @Override
    public String marshal(final NucleotideSequence seq) throws Exception {
        return seq.toString();
    }

    private Supplier<IllegalArgumentException> invalid(final String str) {
        return () -> new IllegalArgumentException(String.format(
            "Invalid nucleobase sequence %s", str
        ));
    }

}
