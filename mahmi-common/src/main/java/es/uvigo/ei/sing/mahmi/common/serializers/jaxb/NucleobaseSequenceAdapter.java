package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import java.util.function.Supplier;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;
import es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods;
import lombok.experimental.ExtensionMethod;

/**
 * {@linkplain NucleobaseSequenceAdapter} is a class that extends
 * {@link XmlAdapter} with {@link NucleobaseSequence} parsing methods
 * 
 * @author Alberto Gutierrez-Jacome
 * 
 * @see NucleobaseSequence
 *
 */
@ExtensionMethod(OptionExtensionMethods.class)
public final class NucleobaseSequenceAdapter extends XmlAdapter<String, NucleobaseSequence> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public NucleobaseSequence unmarshal(final String str) throws Exception {
		return NucleobaseSequence.fromString(str).orThrow(invalidSeq(str));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(final NucleobaseSequence seq) throws Exception {
		return seq.asString();
	}

	/**
	 * Gets an IllegalArgumentException for a invalid nucleo base sequence
	 * 
	 * @param str
	 *            The invalid nucleo base sequence
	 * @return An IllegalArgumentException
	 */
	private Supplier<IllegalArgumentException> invalidSeq(final String str) {
		return () -> new IllegalArgumentException(
				String.format("Invalid nucleobase sequence %s", str));
	}

}
