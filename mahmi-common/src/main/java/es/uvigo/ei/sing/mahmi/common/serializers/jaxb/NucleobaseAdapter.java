package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import java.util.function.Supplier;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import lombok.experimental.ExtensionMethod;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.Nucleobase;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods;

@ExtensionMethod(OptionExtensionMethods.class)
public final class NucleobaseAdapter extends XmlAdapter<Character, Nucleobase> {

    @Override
    public Nucleobase unmarshal(final Character code) throws Exception {
        return Nucleobase.fromCode(code).orThrow(invalidCode(code));
    }

    @Override
    public Character marshal(final Nucleobase nucleobase) throws Exception {
        return nucleobase.getCode();
    }

    private Supplier<IllegalArgumentException> invalidCode(final char code) {
        return () -> new IllegalArgumentException(
            String.format("Invalid nucleobase code %c", code)
        );
    }

}
