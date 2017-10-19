package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleobaseSequence;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaReader;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaWriter;

/**
 * {@linkplain NucleobaseSequenceAdapter} is a class that extends
 * {@link XmlAdapter} with {@link Fasta}<{@link NucleobaseSequence}> parsing
 * methods
 * 
 * @author Alberto Gutierrez-Jacome
 * 
 * @see Fasta
 * @see NucleobaseSequence
 *
 */
public class NucleobaseFastaAdapter extends XmlAdapter<String, Fasta<NucleobaseSequence>> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public Fasta<NucleobaseSequence> unmarshal(final String str) throws Exception {
		return FastaReader.forNucleobase().fromReader(new BufferedReader(new StringReader(str)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public String marshal(final Fasta<NucleobaseSequence> fasta) throws Exception {
		try (final StringWriter writer = new StringWriter()) {
			FastaWriter.forNucleobase().toWriter(fasta, writer);
			return writer.toString();
		}
	}

}
