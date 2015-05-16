package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import lombok.experimental.ExtensionMethod;

import fj.P1;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.Nucleotide;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionUtils;

import static fj.P.lazy;

@ExtensionMethod(OptionUtils.class)
public final class NucleobaseAdapter extends XmlAdapter<Character, Nucleotide> {

    @Override
    public Nucleotide unmarshal(final Character code) throws Exception {
        return Nucleotide.fromCode(code).orThrow(invalidCode(code));
    }

    @Override
    public Character marshal(final Nucleotide nucleobase) throws Exception {
        return nucleobase.getCode();
    }

    private P1<IllegalArgumentException> invalidCode(final char code) {
        return lazy(u -> new IllegalArgumentException(
            String.format("Invalid nucleobase code %c", code)
        ));
    }

}
