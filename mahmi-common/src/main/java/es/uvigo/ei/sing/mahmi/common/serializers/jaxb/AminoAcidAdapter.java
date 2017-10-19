package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import java.util.function.Supplier;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.AminoAcid;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods;
import lombok.experimental.ExtensionMethod;

/**
 * {@linkplain AminoAcidAdapter} is a class that extends {@link XmlAdapter} with
 * {@link AminoAcid} parsing methods
 * 
 * @author Alberto Gutierrez-Jacome
 * 
 * @see AminoAcid
 *
 */
@ExtensionMethod(OptionExtensionMethods.class)
public final class AminoAcidAdapter extends XmlAdapter<Character, AminoAcid> {

    /* (non-Javadoc)
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     */
    @Override
    public AminoAcid unmarshal(final Character code) throws Exception {
        return AminoAcid.fromCode(code).orThrow(invalidCode(code));
    }

    /* (non-Javadoc)
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     */
    @Override
    public Character marshal(final AminoAcid aminoacid) throws Exception {
        return aminoacid.getCode();
    }

    /**
     * Gets an IllegalArgumentException for a invalid amino acid code
     * 
     * @param code The invalid amino acid code
     * @return An IllegalArgumentException
     */
    private Supplier<IllegalArgumentException> invalidCode(final char code) {
        return () -> new IllegalArgumentException(
            String.format("Invalid aminoacid code %c", code)
        );
    }

}
