package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import lombok.experimental.ExtensionMethod;

import fj.P1;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.AminoAcid;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionUtils;

import static fj.P.lazy;

@ExtensionMethod(OptionUtils.class)
public final class AminoAcidAdapter extends XmlAdapter<Character, AminoAcid> {

    @Override
    public AminoAcid unmarshal(final Character code) throws Exception {
        return AminoAcid.fromCode(code).orThrow(invalidCode(code));
    }

    @Override
    public Character marshal(final AminoAcid aminoacid) throws Exception {
        return aminoacid.getCode();
    }

    private P1<IllegalArgumentException> invalidCode(final char code) {
        return lazy(u -> new IllegalArgumentException(
            String.format("Invalid aminoacid code %c", code)
        ));
    }

}
