package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import java.util.function.Supplier;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.Nucleobase;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods;
import lombok.experimental.ExtensionMethod;

/**
 * {@linkplain NucleobaseAdapter} is a class that extends {@link XmlAdapter} with
 * {@link Nucleobase} parsing methods
 * 
 * @author Alberto Gutierrez-Jacome
 * 
 * @see Nucleobase
 *
 */
@ExtensionMethod(OptionExtensionMethods.class)
public final class NucleobaseAdapter extends XmlAdapter<Character, Nucleobase> {

    /* (non-Javadoc)
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     */
    @Override
    public Nucleobase unmarshal(final Character code) throws Exception {
        return Nucleobase.fromCode(code).orThrow(invalidCode(code));
    }

    /* (non-Javadoc)
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     */
    @Override
    public Character marshal(final Nucleobase nucleobase) throws Exception {
        return nucleobase.getCode();
    }

    /**
     * Gets an IllegalArgumentException for a invalid nucleo base code
     * 
     * @param code The invalid nucleo base code
     * @return An IllegalArgumentException
     */
    private Supplier<IllegalArgumentException> invalidCode(final char code) {
        return () -> new IllegalArgumentException(
            String.format("Invalid nucleobase code %c", code)
        );
    }

}
