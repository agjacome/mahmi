package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import java.util.function.Supplier;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods;
import lombok.experimental.ExtensionMethod;

/**
 * {@linkplain AminoAcidSequenceAdapter} is a class that extends
 * {@link XmlAdapter} with {@link AminoAcidSequence} parsing methods
 * 
 * @author Alberto Gutierrez-Jacome
 * 
 * @see AminoAcidSequence
 *
 */
@ExtensionMethod(OptionExtensionMethods.class)
public final class AminoAcidSequenceAdapter extends XmlAdapter<String, AminoAcidSequence> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public AminoAcidSequence unmarshal(final String str) throws Exception {
		return AminoAcidSequence.fromString(str).orThrow(invalidSeq(str));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(final AminoAcidSequence seq) throws Exception {
		return seq.asString();
	}

	/**
	 * Gets an IllegalArgumentException for a invalid amino acid sequence
	 * 
	 * @param str
	 *            The invalid amino acid sequence
	 * @return An IllegalArgumentException
	 */
	private Supplier<IllegalArgumentException> invalidSeq(final String str) {
		return () -> new IllegalArgumentException(
				String.format("Invalid aminoacid sequence %s", str));
	}

}
