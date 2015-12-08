package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import java.util.function.Supplier;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import lombok.experimental.ExtensionMethod;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods;

@ExtensionMethod(OptionExtensionMethods.class)
public final class NucleobaseSequenceAdapter extends XmlAdapter<String, NucleobaseSequence> {

    @Override
    public NucleobaseSequence unmarshal(final String str) throws Exception {
        return NucleobaseSequence.fromString(str).orThrow(invalidSeq(str));
    }

    @Override
    public String marshal(final NucleobaseSequence seq) throws Exception {
        return seq.asString();
    }

    private Supplier<IllegalArgumentException> invalidSeq(final String str) {
        return () -> new IllegalArgumentException(
            String.format("Invalid nucleobase sequence %s", str)
        );
    }

}
