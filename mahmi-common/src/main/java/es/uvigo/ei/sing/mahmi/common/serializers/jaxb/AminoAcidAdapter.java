package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import java.util.function.Supplier;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.AminoAcid;

public final class AminoAcidAdapter extends XmlAdapter<Character, AminoAcid> {

    @Override
    public AminoAcid unmarshal(final Character code) throws Exception {
        return AminoAcid.fromCode(code).orElseThrow(invalid(code));
    }

    @Override
    public Character marshal(final AminoAcid aminoacid) throws Exception {
        return aminoacid.getCode();
    }

    private Supplier<IllegalArgumentException> invalid(final char code) {
        return () -> new IllegalArgumentException(String.format(
            "Invalid aminoacid code %c", code
        ));
    }

}
