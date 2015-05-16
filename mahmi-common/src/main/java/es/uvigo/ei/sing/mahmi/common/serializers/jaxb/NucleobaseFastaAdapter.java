package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.sequences.Fasta;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.NucleotideSequence;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaReader;
import es.uvigo.ei.sing.mahmi.common.serializers.fasta.FastaWriter;

public class NucleobaseFastaAdapter extends XmlAdapter<String, Fasta<NucleotideSequence>> {

    @Override
    public Fasta<NucleotideSequence> unmarshal(final String str) throws Exception {
        return FastaReader.forNucleobase().fromReader(
            new BufferedReader(new StringReader(str))
        );
    }

    @Override
    public String marshal(final Fasta<NucleotideSequence> fasta) throws Exception {
        try (final StringWriter writer = new StringWriter()) {
            FastaWriter.forNucleobase().toWriter(fasta, writer);
            return writer.toString();
        }
    }

}
