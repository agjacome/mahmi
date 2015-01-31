package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import static fj.P.lazy;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import lombok.experimental.ExtensionMethod;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.CompoundSequence;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods;
import fj.P1;
import fj.data.Option;

@ExtensionMethod({ Option.class, OptionExtensionMethods.class })
public final class AminoAcidSequenceAdapter extends XmlAdapter<String, AminoAcidSequence> {

    @Override
    public AminoAcidSequence unmarshal(final String str) throws Exception {
        return AminoAcidSequence.fromString(str).orThrow(invalidSeq(str));
    }

    @Override
    public String marshal(final AminoAcidSequence seq) throws Exception {
        return CompoundSequence.show.showS(seq);
    }

    private P1<IllegalArgumentException> invalidSeq(final String str) {
        return lazy(u -> new IllegalArgumentException(
            String.format("Invalid aminoacid sequence %s", str)
        ));
    }

}
