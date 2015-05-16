package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import lombok.experimental.ExtensionMethod;

import fj.P1;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleotideSequence;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionUtils;

import static fj.P.lazy;

@ExtensionMethod(OptionUtils.class)
public final class NucleobaseSequenceAdapter extends XmlAdapter<String, NucleotideSequence> {

    @Override
    public NucleotideSequence unmarshal(final String str) throws Exception {
        return NucleotideSequence.fromString(str).orThrow(invalidSeq(str));
    }

    @Override
    public String marshal(final NucleotideSequence seq) throws Exception {
        return seq.asString();
    }

    private P1<IllegalArgumentException> invalidSeq(final String str) {
        return lazy(u -> new IllegalArgumentException(
            String.format("Invalid nucleobase sequence %s", str)
        ));
    }

}
