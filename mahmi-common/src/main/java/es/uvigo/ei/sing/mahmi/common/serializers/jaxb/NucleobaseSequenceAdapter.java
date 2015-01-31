package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import static fj.P.lazy;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import lombok.experimental.ExtensionMethod;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods;
import fj.P1;
import fj.data.Option;

@ExtensionMethod({ Option.class, OptionExtensionMethods.class })
public final class NucleobaseSequenceAdapter extends XmlAdapter<String, NucleobaseSequence> {

    @Override
    public NucleobaseSequence unmarshal(final String str) throws Exception {
        return NucleobaseSequence.fromString(str).orThrow(invalidSeq(str));
    }

    @Override
    public String marshal(final NucleobaseSequence seq) throws Exception {
        return seq.toString();
    }

    private P1<IllegalArgumentException> invalidSeq(final String str) {
        return lazy(u -> new IllegalArgumentException(
            String.format("Invalid nucleobase sequence %s", str)
        ));
    }

}
