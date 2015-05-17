package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import java.util.function.Supplier;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.Nucleotide;

public final class NucleobaseAdapter extends XmlAdapter<Character, Nucleotide> {

    @Override
    public Nucleotide unmarshal(final Character code) throws Exception {
        return Nucleotide.fromCode(code).orElseThrow(invalid(code));
    }

    @Override
    public Character marshal(final Nucleotide nucleobase) throws Exception {
        return nucleobase.getCode();
    }

    private Supplier<IllegalArgumentException> invalid(final char code) {
        return () -> new IllegalArgumentException(String.format(
            "Invalid nucleobase code %c", code
        ));
    }

}
